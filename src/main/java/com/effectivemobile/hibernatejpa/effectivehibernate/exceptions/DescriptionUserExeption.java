package com.effectivemobile.hibernatejpa.effectivehibernate.exceptions;

/**
 * <pre>
 *     Enum с описанием для кастомных ошибок, которые могут выбрасываться в некоторых частях приложения
 * </pre>
 */
public enum DescriptionUserExeption {

    GENERATION_ERROR("Возникла ошибка в системе: "),

    TASK_NOT_FOUND_BY_ID("Задача по переданному ID не найдена"),

    STATUS_NOT_BE_NULL("Статус задачи не может быть пуст"),

    DATE_CALENDAR_NOT_BE_NULL("Дата задачи в календаре не может быть пустой");

    private String enumDescription;

    DescriptionUserExeption(String enumDescription) {
        this.enumDescription = enumDescription;
    }

    public String getEnumDescription() {
        return enumDescription;
    }
}
