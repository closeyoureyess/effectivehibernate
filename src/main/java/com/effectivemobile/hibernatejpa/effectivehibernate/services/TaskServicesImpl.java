package com.effectivemobile.hibernatejpa.effectivehibernate.services;

import com.effectivemobile.hibernatejpa.effectivehibernate.dto.TaskDto;
import com.effectivemobile.hibernatejpa.effectivehibernate.entities.Task;
import com.effectivemobile.hibernatejpa.effectivehibernate.exceptions.DataCalendarNotBeNullException;
import com.effectivemobile.hibernatejpa.effectivehibernate.exceptions.EntityNotFoundException;
import com.effectivemobile.hibernatejpa.effectivehibernate.exceptions.StatusTaskNotBeNullException;
import com.effectivemobile.hibernatejpa.effectivehibernate.mappers.TaskMapper;
import com.effectivemobile.hibernatejpa.effectivehibernate.repositories.JdbcRepository;
import com.effectivemobile.hibernatejpa.effectivehibernate.repositories.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.effectivemobile.hibernatejpa.effectivehibernate.exceptions.DescriptionUserExeption.*;
import static com.effectivemobile.hibernatejpa.effectivehibernate.other.ConstantsClass.*;

@Service
@Slf4j
public class TaskServicesImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final JdbcRepository<Object, Object> jdbcRepository;

    @Autowired
    public TaskServicesImpl(TaskMapper taskMapper, TaskRepository taskRepository, JdbcRepository<Object, Object> jdbcRepository) {
        this.taskMapper = taskMapper;
        this.taskRepository = taskRepository;
        this.jdbcRepository = jdbcRepository;
    }

    @CachePut(cacheNames = "taskServiceCache", key = "#result.id")
    @Transactional
    @Override
    public Optional<TaskDto> createTasks(TaskDto tasksDto) throws Exception {
        if (tasksDto.getTitle() == null) {
            throw new StatusTaskNotBeNullException(STATUS_NOT_BE_NULL.getEnumDescription());
        } else if (tasksDto.getDateCalendar() == null) {
            throw new DataCalendarNotBeNullException(DATE_CALENDAR_NOT_BE_NULL.getEnumDescription());
        }
        Task task = taskMapper.convertDtoToTasks(tasksDto);
        task.setId(null);
        task = (Task) jdbcRepository.save(task);
        log.info("create");
        if (task.getId() != null) {
            TaskDto taskDtoResult = taskMapper.convertTasksToDto(task);
            return Optional.of(taskDtoResult);
        }
        return Optional.empty();
    }

    @CachePut(cacheNames = "taskServiceCache", key = "#tasksDto.id")
    @Transactional
    @Override
    public Optional<TaskDto> changeTasks(TaskDto tasksDto) throws Exception {
        Object objectFromDB = jdbcRepository.findById(tasksDto.getId(), Task.class);
        if (objectFromDB == null) {
            throw new EntityNotFoundException(TASK_NOT_FOUND_BY_ID.getEnumDescription());
        }
        Task taskFromDB = (Task) objectFromDB;
        taskMapper.compareTaskAndDto(tasksDto, taskFromDB);
        Object updatedObject = jdbcRepository.update(taskFromDB);
        if (updatedObject == null) {
            return Optional.empty();
        }
        Task updatedTask = (Task) updatedObject;
        log.info("change");
        return Optional.of(taskMapper.convertTasksToDto(updatedTask));
    }

    @Cacheable(cacheNames = "taskServiceCache", key = "#result.?[].id")
    @Transactional
    @Override
    public Optional<List<TaskDto>> getTasksByTitle(String taskTitle, Integer offset, Integer limit) {
        StringBuilder localStringBuilder = new StringBuilder(taskTitle);
        char[] chars = taskTitle.toCharArray();
        if (chars[ZERO] != PERCENT_CHAR_PRIMITIVE) {
            localStringBuilder.insert(ZERO, PERCENT);
        }
        if (chars[chars.length - ONE] != PERCENT_CHAR_PRIMITIVE) {
            localStringBuilder.append(PERCENT);
        }
        Optional<List<Task>> optionalTaskList = taskRepository.findAllByTitle(localStringBuilder.toString(), offset, limit);
        if (optionalTaskList.isPresent() && !optionalTaskList.get().isEmpty()) {
            return Optional.of(taskMapper.transferListTasksToDto(optionalTaskList.get()));
        }
        log.info("get");
        return Optional.empty();
    }

    @CacheEvict(cacheNames = "taskServiceCache", key = "#idTasks")
    @Transactional
    @Override
    public boolean deleteTasks(Long idTasks) throws Exception {
        boolean taskExist = jdbcRepository.existsById(idTasks, Task.class);
        if (taskExist) {
            jdbcRepository.deleteById(idTasks, Task.class);
            log.info("delete");
            return true;
        }
        log.info("delete");
        return false;
    }
}
