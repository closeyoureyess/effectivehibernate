package com.effectivemobile.hibernatejpa.effectivehibernate.repositories;

import com.effectivemobile.hibernatejpa.effectivehibernate.entities.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskRepositoryTest {

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    @Mock
    private Query<Task> query;

    @InjectMocks
    private TaskRepositoryImpl taskRepository;

    @BeforeEach
    void setUp() {
        when(sessionFactory.openSession()).thenReturn(session);
    }

    @DisplayName("Пустой результат при отсутствии задач")
    @Test
    void findAllByTitle_NoTasks_ReturnsEmpty() {
        // Arrange
        Transaction mockTransaction = Mockito.mock(Transaction.class);
        when(session.beginTransaction()).thenReturn(mockTransaction); // Мокируем транзакцию
        when(session.createQuery(anyString(), eq(Task.class))).thenReturn(query);
        when(query.setParameter(eq("one"), anyString())).thenReturn(query);
        when(query.setFirstResult(Mockito.anyInt())).thenReturn(query);
        when(query.setMaxResults(Mockito.anyInt())).thenReturn(query);
        when(query.list()).thenReturn(Collections.emptyList());

        // Act
        Optional<List<Task>> result = taskRepository.findAllByTitle("empty", 0, 10);

        // Assert
        Assertions.assertTrue(result.isEmpty());
        verify(mockTransaction).commit(); // Проверяем коммит транзакции
        verify(session).close(); // Закрытие сессии
    }

    @DisplayName("Поиск задач по заголовку с пагинацией")
    @Test
    void findAllByTitle_ValidParams_ReturnsTasks() {
        // Arrange
        Transaction mockTransaction = Mockito.mock(Transaction.class);
        when(session.beginTransaction()).thenReturn(mockTransaction);

        String expectedParam = "%test%";
        List<Task> expectedTasks = List.of(new Task());

        when(session.createQuery(anyString(), eq(Task.class))).thenReturn(query);
        when(query.setParameter(eq("one"), eq(expectedParam))).thenReturn(query); // Ожидаемый параметр
        when(query.setFirstResult(0)).thenReturn(query);
        when(query.setMaxResults(10)).thenReturn(query);
        when(query.list()).thenReturn(expectedTasks);

        // Act
        Optional<List<Task>> result = taskRepository.findAllByTitle("test", 0, 10);

        // Assert
        verify(query).setParameter("one", expectedParam);
        verify(mockTransaction).commit();
        verify(session).close();

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(1, result.get().size());
    }

    @DisplayName("Обработка SQL исключения")
    @Test
    void findAllByTitle_DatabaseError_ReturnsEmpty() {
        // Arrange
        when(session.createQuery(anyString(), eq(Task.class))).thenThrow(new RuntimeException("DB error"));

        // Act & Assert
        Assertions.assertThrows(RuntimeException.class, () ->
                taskRepository.findAllByTitle("error", 0, 10)
        );
    }
}