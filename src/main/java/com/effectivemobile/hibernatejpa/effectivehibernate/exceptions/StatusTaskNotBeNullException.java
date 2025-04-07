package com.effectivemobile.hibernatejpa.effectivehibernate.exceptions;

/**
 * Эксепшен, который выбрасывается, если поле Статус у сущности Задача null
 */
public class StatusTaskNotBeNullException extends Exception {
    public StatusTaskNotBeNullException(String message) {
        super(message);
    }
}
