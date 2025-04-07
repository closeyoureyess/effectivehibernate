package com.effectivemobile.hibernatejpa.effectivehibernate.mappers;

import com.effectivemobile.hibernatejpa.effectivehibernate.dto.TaskDto;
import com.effectivemobile.hibernatejpa.effectivehibernate.entities.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TaskMapperImpl implements TaskMapper {

    @Override
    public Task convertDtoToTasks(TaskDto tasksDto) {
        log.info("Метод convertDtoToTasks()");
        Task taskLocalObject = new Task();
        if (tasksDto != null) {
            taskLocalObject.setId(tasksDto.getId());
            taskLocalObject.setTitle(tasksDto.getTitle());
            taskLocalObject.setDescription(tasksDto.getDescription());
            taskLocalObject.setStatus(tasksDto.getStatus());
            taskLocalObject.setDateCalendar(tasksDto.getDateCalendar());
        }
        return taskLocalObject;
    }

    @Override
    public TaskDto convertTasksToDto(Task tasks) {
        log.info("Метод convertTasksToDto()");
        TaskDto tasksDtoLocalObject = new TaskDto();
        if (tasks != null) {
            tasksDtoLocalObject.setId(tasks.getId());
            tasksDtoLocalObject.setTitle(tasks.getTitle());
            tasksDtoLocalObject.setDescription(tasks.getDescription());
            tasksDtoLocalObject.setStatus(tasks.getStatus());
            tasksDtoLocalObject.setDateCalendar(tasks.getDateCalendar());
        }
        return tasksDtoLocalObject;
    }

    @Override
    public List<TaskDto> transferListTasksToDto(List<Task> tasksList) {
        List<TaskDto> taskDtoList = new ArrayList<>();
        if (tasksList != null) {
            for (Task tasks : tasksList) {
                TaskDto taskDto = convertTasksToDto(tasks);
                taskDtoList.add(taskDto);
            }
        }
        return taskDtoList;
    }

    @Override
    public void compareTaskAndDto(TaskDto tasksDto, Task tasks) {
        if (tasksDto != null) {
            compareTasksAndDtoTitle(tasksDto, tasks);
            compareTasksAndDtoDescription(tasksDto, tasks);
            compareTasksAndDtoStatus(tasksDto, tasks);
            compareTasksAndDtoDateCalendar(tasksDto, tasks);
        }
    }

    private void compareTasksAndDtoTitle(TaskDto tasksDto, Task tasks) {
        if (tasksDto.getTitle() != null) {
            tasks.setTitle(tasksDto.getTitle());
        }
    }

    private void compareTasksAndDtoDescription(TaskDto tasksDto, Task tasks) {
        if (tasksDto.getDescription() != null) {
            tasks.setDescription(tasksDto.getDescription());
        }
    }

    private void compareTasksAndDtoStatus(TaskDto tasksDto, Task tasks) {
        if (tasksDto.getStatus() != null) {
            tasks.setStatus(tasksDto.getStatus());
        }
    }

    private void compareTasksAndDtoDateCalendar(TaskDto tasksDto, Task tasks) {
        if (tasksDto.getDateCalendar() != null) {
            tasks.setDateCalendar(tasksDto.getDateCalendar());
        }
    }
}
