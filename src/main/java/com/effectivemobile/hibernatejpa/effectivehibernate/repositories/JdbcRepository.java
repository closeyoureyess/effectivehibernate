package com.effectivemobile.hibernatejpa.effectivehibernate.repositories;

/**
 * Интерфейс-репозиторий с общими методами для запросов к БД
 *
 * @param <T> Параметр, в котором идёт инкремент id у сущности
 * @param <R> Класс, который будет сохраняться в БД
 */
public interface JdbcRepository<T, R> {

    /**
     * Метод, позволяющий проверить в БД, существует ли сущность по переданному ID
     *
     * @param id id сущности
     * @return true, если сущность есть в БД
     */
    boolean existsById(T id, R entity) throws Exception;

    /**
     * Метод, позволяющий сохранить объект в БД
     *
     * @param entity объект, который нужно сохранить в БД
     * @return сохраненный в БД объект
     */
    R save(R entity) throws Exception;

    /**
     * Метод, позволяющий обновить объект в БД
     *
     * @param newEntity объект, который нужно обновить в БД
     * @return обновленный в БД объект
     */
    R update(R newEntity) throws Exception;

    /**
     * Метод, позволяющий найти объект в БД по переданному ID
     *
     * @param id сущности
     * @return объект из БД, найденный по ID
     */
    R findById(T id, R entity) throws Exception;

    /**
     * Метод, позволяющий удалить сущность из БД
     *
     * @param id сущности
     */
    void deleteById(T id, R entity) throws Exception;

}
