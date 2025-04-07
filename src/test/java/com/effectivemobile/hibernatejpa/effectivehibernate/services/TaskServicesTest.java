package com.effectivemobile.hibernatejpa.effectivehibernate.services;

import com.emobile.springtodo.dto.TaskDto;
import com.emobile.springtodo.entities.Task;
import com.emobile.springtodo.exceptions.DataCalendarNotBeNullException;
import com.emobile.springtodo.exceptions.EntityNotFoundException;
import com.emobile.springtodo.exceptions.StatusTaskNotBeNullException;
import com.emobile.springtodo.mappers.TaskMapper;
import com.emobile.springtodo.repositories.TaskRepository;
import org.junit.jupiter.api.Assertions;
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

@ExtendWith(MockitoExtension.class)
class TaskServicesTest {

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServicesImpl taskServices;

    private TaskDto taskDto;
    private Task task;

    @BeforeEach
    void setUp() {
        taskDto = new TaskDto(1L, "Test Task", "Description", true, LocalDateTime.now());
        task = new Task(1L, "Test Task", "Description", true, LocalDateTime.now());
    }

    @DisplayName("Проверка возвращаемого значения метода createTasks")
    @Test
    void createTasks_ShouldReturnCreatedTask() throws StatusTaskNotBeNullException, DataCalendarNotBeNullException {
        Mockito.when(taskMapper.convertDtoToTasks(Mockito.any())).thenReturn(task);
        Mockito.when(taskRepository.save(Mockito.any())).thenReturn(task);
        Mockito.when(taskMapper.convertTasksToDto(Mockito.any())).thenReturn(taskDto);

        TaskDto createdTask = taskServices.createTasks(taskDto);

        Assertions.assertNotNull(createdTask);
        Assertions.assertEquals("Test Task", createdTask.getTitle());
        Mockito.verify(taskRepository, Mockito.times(1)).save(Mockito.any(Task.class));
    }

    @DisplayName("Проверка выбрасываемого эксепешена StatusTaskNotBeNullException метода createTasks")
    @Test
    void createTasks_ShouldThrowStatusTaskNotBeNullException() {
        taskDto.setTitle(null);
        Assertions.assertThrows(StatusTaskNotBeNullException.class, () -> taskServices.createTasks(taskDto));
    }

    @DisplayName("Проверка выбрасываемого эксепешена DataCalendarNotBeNullException метода createTasks")
    @Test
    void createTasks_ShouldThrowDataCalendarNotBeNullException() {
        taskDto.setDateCalendar(null);
        Assertions.assertThrows(DataCalendarNotBeNullException.class, () -> taskServices.createTasks(taskDto));
    }

    @DisplayName("Проверка возвращаемого значения метода changeTasks")
    @Test
    void changeTasks_ShouldReturnUpdatedTask() throws EntityNotFoundException {
        Mockito.when(taskRepository.findById(taskDto.getId())).thenReturn(task);
        Mockito.when(taskMapper.convertTasksToDto(Mockito.any())).thenReturn(taskDto);

        Optional<TaskDto> updatedTask = taskServices.changeTasks(taskDto);

        Assertions.assertTrue(updatedTask.isPresent());
        Mockito.verify(taskRepository, Mockito.times(1)).update(Mockito.any(Task.class));
    }

    @DisplayName("роверка выбрасываемого эксепешена EntityNotFoundException метода changeTasks")
    @Test
    void changeTasks_ShouldThrowEntityNotFoundException() {
        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(null);
        Assertions.assertThrows(EntityNotFoundException.class, () -> taskServices.changeTasks(taskDto));
    }

    @DisplayName("Проверка возвращаемого значения метода deleteTasks, сценарий, когда true")
    @Test
    void deleteTasks_ShouldReturnTrue() {
        Mockito.when(taskRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.doNothing().when(taskRepository).deleteById(Mockito.anyLong());

        boolean result = taskServices.deleteTasks(1L);

        Assertions.assertTrue(result);
        Mockito.verify(taskRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
    }

    @DisplayName("Проверка возвращаемого значения метода deleteTasks, сценарий, когда false")
    @Test
    void deleteTasks_ShouldReturnFalse() {
        Mockito.when(taskRepository.existsById(Mockito.anyLong())).thenReturn(false);
        boolean result = taskServices.deleteTasks(1L);

        Assertions.assertFalse(result);
        Mockito.verify(taskRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }
}
