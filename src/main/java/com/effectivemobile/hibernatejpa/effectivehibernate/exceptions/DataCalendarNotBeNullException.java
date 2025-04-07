package com.effectivemobile.hibernatejpa.effectivehibernate.exceptions;

/**
 * Эксепшен, который выбрасывается, если поле дата задачи у сущности Задача null
 */
public class DataCalendarNotBeNullException extends Exception {
    public DataCalendarNotBeNullException(String message) {
        super(message);
    }
}
