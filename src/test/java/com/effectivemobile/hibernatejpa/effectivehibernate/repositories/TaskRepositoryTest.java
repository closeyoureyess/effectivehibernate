package com.effectivemobile.hibernatejpa.effectivehibernate.repositories;

import com.emobile.springtodo.entities.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TaskRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private TaskRepositoryImpl taskRepository;

    @DisplayName("Проверка метода existsById")
    @Test
    void existsById_WhenExists_ReturnsTrue() {
        Mockito.when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.eq(Integer.class), Mockito.anyLong()))
                .thenReturn(1);

        Assertions.assertTrue(taskRepository.existsById(1L));
        Mockito.verify(jdbcTemplate).queryForObject(
                "SELECT COUNT(*) FROM tasks_entity WHERE id = ?",
                Integer.class, 1L
        );
    }

    @DisplayName("Проверка метода save")
    @Test
    void save_ValidTask_GeneratesId() {
        Task task = new Task();
        task.setTitle("Test");
        task.setStatus(true);
        task.setDateCalendar(LocalDateTime.now());

        Mockito.when(jdbcTemplate.update(Mockito.any(PreparedStatementCreator.class),
                        Mockito.any(GeneratedKeyHolder.class)))
                .thenAnswer(inv -> {
                    GeneratedKeyHolder keyHolder = inv.getArgument(1);
                    keyHolder.getKeyList().add(new HashMap<>() {{
                        put("id", 1L);
                    }});
                    return 1;
                });

        Task result = taskRepository.save(task);
        Assertions.assertEquals(1L, result.getId());
    }

    @DisplayName("Проверка метода findAll")
    @Test
    void findAllByTitle_ValidTitle_ReturnsTasks() {
        Task task = new Task();
        Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(RowMapper.class), Mockito.any(),
                        Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(task));

        Optional<List<Task>> result = taskRepository.findAllByTitle("test", 0, 10);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(1, result.get().size());
    }
}