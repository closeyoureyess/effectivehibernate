package com.effectivemobile.hibernatejpa.effectivehibernate.services;

import com.effectivemobile.hibernatejpa.effectivehibernate.dto.TaskDto;
import com.effectivemobile.hibernatejpa.effectivehibernate.entities.Task;
import com.effectivemobile.hibernatejpa.effectivehibernate.exceptions.DataCalendarNotBeNullException;
import com.effectivemobile.hibernatejpa.effectivehibernate.exceptions.EntityNotFoundException;
import com.effectivemobile.hibernatejpa.effectivehibernate.exceptions.StatusTaskNotBeNullException;
import com.effectivemobile.hibernatejpa.effectivehibernate.mappers.TaskMapper;
import com.effectivemobile.hibernatejpa.effectivehibernate.repositories.JdbcRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class TaskServicesImplTest {

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private JdbcRepository<Object, Object> jdbcRepository;

    @InjectMocks
    private TaskServicesImpl taskServices;

    private TaskDto taskDto;
    private Task task;

    @BeforeEach
    void setUp() {
        taskDto = new TaskDto();
        taskDto.setId(1L);
        taskDto.setTitle("Test Task");
        taskDto.setDescription("Description");
        taskDto.setStatus(true);
        taskDto.setDateCalendar(LocalDateTime.now());

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Description");
        task.setStatus(true);
        task.setDateCalendar(LocalDateTime.now());
    }

    @Test
    @DisplayName("Создание задачи - успешное создание")
    void createTasks_ShouldReturnTaskDto() throws Exception {

        Task savedTask = new Task();
        savedTask.setId(10L); // <-- Здесь проставляем любой ненулевой ID
        savedTask.setTitle("Test Task");
        savedTask.setDescription("Description");
        savedTask.setStatus(true);
        savedTask.setDateCalendar(LocalDateTime.now());

        // Mock
        Mockito.when(taskMapper.convertDtoToTasks(any())).thenReturn(task);
        Mockito.when(jdbcRepository.save(any())).thenReturn(savedTask);
        Mockito.when(taskMapper.convertTasksToDto(any())).thenReturn(taskDto);

        // Test
        Optional<TaskDto> result = taskServices.createTasks(taskDto);

        // Verify
        assertTrue(result.isPresent());
        assertEquals("Test Task", result.get().getTitle());
        Mockito.verify(jdbcRepository).save(task);
        Mockito.verify(taskMapper).convertTasksToDto(any());
    }

    @Test
    @DisplayName("Создание задачи - пустой статус")
    void createTasks_ShouldThrowStatusException() {
        taskDto.setTitle(null);
        assertThrows(StatusTaskNotBeNullException.class,
                () -> taskServices.createTasks(taskDto));
    }

    @Test
    @DisplayName("Создание задачи - пустая дата")
    void createTasks_ShouldThrowDateException() {
        taskDto.setDateCalendar(null);
        assertThrows(DataCalendarNotBeNullException.class,
                () -> taskServices.createTasks(taskDto));
    }

    @Test
    @DisplayName("Обновление задачи - успешное обновление")
    void changeTasks_ShouldUpdateTask() throws Exception {
        // Mock
        Mockito.when(jdbcRepository.findById(1L, Task.class)).thenReturn(task);
        Mockito.when(jdbcRepository.update(any())).thenReturn(task);
        Mockito.when(taskMapper.convertTasksToDto(any())).thenReturn(taskDto);

        // Test
        Optional<TaskDto> result = taskServices.changeTasks(taskDto);

        // Verify
        assertTrue(result.isPresent());
        Mockito.verify(taskMapper).compareTaskAndDto(taskDto, task);
        Mockito.verify(jdbcRepository).update(task);
    }

    @Test
    @DisplayName("Обновление задачи - задача не найдена")
    void changeTasks_ShouldThrowNotFoundException() throws Exception {
        Mockito.when(jdbcRepository.findById(any(), eq(Task.class))).thenReturn(null);
        assertThrows(EntityNotFoundException.class,
                () -> taskServices.changeTasks(taskDto));
    }

    @Test
    @DisplayName("Удаление задачи - успешное удаление")
    void deleteTasks_ShouldReturnTrue() throws Exception {
        Mockito.when(jdbcRepository.existsById(1L, Task.class)).thenReturn(true);
        assertTrue(taskServices.deleteTasks(1L));
        Mockito.verify(jdbcRepository).deleteById(1L, Task.class);
    }

    @Test
    @DisplayName("Удаление задачи - задача не существует")
    void deleteTasks_ShouldReturnFalse() throws Exception {
        Mockito.when(jdbcRepository.existsById(any(), eq(Task.class))).thenReturn(false);
        assertFalse(taskServices.deleteTasks(999L));
        Mockito.verify(jdbcRepository, Mockito.never()).deleteById(any(), any());
    }
}