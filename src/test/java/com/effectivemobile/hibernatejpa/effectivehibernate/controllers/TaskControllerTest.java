package com.effectivemobile.hibernatejpa.effectivehibernate.controllers;

import com.effectivemobile.hibernatejpa.effectivehibernate.dto.TaskDto;
import com.effectivemobile.hibernatejpa.effectivehibernate.services.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Проверка POST /task/create, при валидном запросе")
    @Test
    void createTask_ValidRequest_ReturnsCreatedTask() throws Exception {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Valid Task");
        taskDto.setStatus(true);
        taskDto.setDateCalendar(LocalDateTime.now());

        when(taskService.createTasks(any())).thenReturn(Optional.of(taskDto));

        mockMvc.perform(post("/task/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Valid Task"));
    }

    @DisplayName("Проверка POST /task/create, при невалидном запросе")
    @Test
    void createTask_InvalidRequest_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/task/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"Invalid Task\"}"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Проверка GET /task/list/{title}, при валидном запросе")
    @Test
    void getTasksByTitle_ExistingTitle_ReturnsTasks() throws Exception {
        when(taskService.getTasksByTitle("Test", 0, 10))
                .thenReturn(Optional.of(Collections.singletonList(new TaskDto())));

        mockMvc.perform(get("/task/list/Test?offset=0&limit=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @DisplayName("Проверка GET /task/list/{title}, при невалидном запросе")
    @Test
    void getTasksByTitle_NonExistingTitle_ReturnsBadRequest() throws Exception {
        when(taskService.getTasksByTitle("Unknown", 0, 10))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/task/list/Unknown?offset=0&limit=10"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Проверка GET /task/list/{title}, при валидном запросе")
    @Test
    void updateTask_ValidRequest_ReturnsUpdatedTask() throws Exception {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);
        taskDto.setTitle("Updated Task");

        when(taskService.changeTasks(any())).thenReturn(Optional.of(taskDto));

        mockMvc.perform(put("/task/update-tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"));
    }

    @DisplayName("Проверка GET /task/list/{title}, при невалидном запросе")
    @Test
    void updateTask_NonExistingTask_ReturnsBadRequest() throws Exception {
        when(taskService.changeTasks(any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/task/update-tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new TaskDto())))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Проверка DELETE /task/delete/{id}, при валидном запросе")
    @Test
    void deleteTask_ExistingId_ReturnsSuccess() throws Exception {
        when(taskService.deleteTasks(1L)).thenReturn(true);

        mockMvc.perform(delete("/task/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Entity was deleted"));
    }

    @DisplayName("Проверка DELETE /task/delete/{id}, при невалидном запросе")
    @Test
    void deleteTask_NonExistingId_ReturnsNoContent() throws Exception {
        when(taskService.deleteTasks(999L)).thenReturn(false);

        mockMvc.perform(delete("/task/delete/999"))
                .andExpect(status().isNoContent());
    }
}