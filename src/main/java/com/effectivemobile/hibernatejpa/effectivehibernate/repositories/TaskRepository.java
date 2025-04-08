package com.effectivemobile.hibernatejpa.effectivehibernate.repositories;

import com.effectivemobile.hibernatejpa.effectivehibernate.entities.Task;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс-репозиторий с кастомными методами для task
 */
public interface TaskRepository {

    Optional<List<Task>> findAllByTitle(String title, Integer offset, Integer limit);

}
