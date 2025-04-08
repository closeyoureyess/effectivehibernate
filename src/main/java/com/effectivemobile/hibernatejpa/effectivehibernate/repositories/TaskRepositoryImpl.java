package com.effectivemobile.hibernatejpa.effectivehibernate.repositories;

import com.effectivemobile.hibernatejpa.effectivehibernate.entities.Task;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class TaskRepositoryImpl implements TaskRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public TaskRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<List<Task>> findAllByTitle(String title, Integer offset, Integer limit) {
        String sql = "SELECT t.id, t.title, t.description, t.status, t.date_calendar " +
                "from Task t where t.title like :one order by t.id";
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            String titleParam = "%" + title + "%";
            List<Task> result = session.createQuery(sql, Task.class)
                    .setParameter("one", titleParam)  // Используем реальный параметр
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .list();
            transaction.commit();
            if (result.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(result);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
