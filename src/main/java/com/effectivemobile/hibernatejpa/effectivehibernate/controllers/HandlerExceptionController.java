package com.effectivemobile.hibernatejpa.effectivehibernate.controllers;


import com.effectivemobile.hibernatejpa.effectivehibernate.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.effectivemobile.hibernatejpa.effectivehibernate.exceptions.DescriptionUserExeption.GENERATION_ERROR;
import static com.effectivemobile.hibernatejpa.effectivehibernate.other.ConstantsClass.LOGGER_SYNTAX;

/**
 * <pre>
 *     Контроллер, обрабатывающий все эксепшены, котоыре могут быть выброшены в процессе работы приложения
 * </pre>
 */
@ControllerAdvice
@Slf4j
public class HandlerExceptionController {

    /**
     * Обработчик ExecutorNotFoundException
     */
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ApiErrorResponse> handleExecutorNotFoundExeption(EntityNotFoundException e, HttpServletRequest request) {
        log.error(LOGGER_SYNTAX, GENERATION_ERROR.getEnumDescription(), e.getClass(), e.getMessage(), e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataCalendarNotBeNullException.class)
    protected ResponseEntity<ApiErrorResponse> handleExecutorNotFoundExeption(DataCalendarNotBeNullException e, HttpServletRequest request) {
        log.error(LOGGER_SYNTAX, GENERATION_ERROR.getEnumDescription(), e.getClass(), e.getMessage(), e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.NOT_ACCEPTABLE,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(StatusTaskNotBeNullException.class)
    protected ResponseEntity<ApiErrorResponse> handleExecutorNotFoundExeption(StatusTaskNotBeNullException e, HttpServletRequest request) {
        log.error(LOGGER_SYNTAX, GENERATION_ERROR.getEnumDescription(), e.getClass(), e.getMessage(), e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.NOT_ACCEPTABLE,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * Обработчик ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ApiErrorResponse> handleConstraintViolationException(ConstraintViolationException e,
                                                                                  HttpServletRequest request) {
        log.error(LOGGER_SYNTAX, GENERATION_ERROR.getEnumDescription(), e.getClass(), e.getMessage(), e);

        Set<ConstraintViolation<?>> violationsSet = e.getConstraintViolations();
        List<Violation> violations = (violationsSet != null)
                ? violationsSet.stream()
                .map(violation -> new Violation(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()
                ))
                .toList()
                : Collections.emptyList();

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                request.getRequestURI(),
                violations
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработчик MethodArgumentNotValidException
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error(LOGGER_SYNTAX, GENERATION_ERROR.getEnumDescription(), e.getClass(), e.getMessage(), e);

        List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .toList();

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                request.getRequestURI(),
                violations
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработчик HttpMessageNotReadableException
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.error(LOGGER_SYNTAX, GENERATION_ERROR.getEnumDescription(), e.getClass(), e.getMessage(), e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработчик IllegalStateException
     */
    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<ApiErrorResponse> handleIllegalStateException(IllegalStateException e, HttpServletRequest request) {
        log.error(LOGGER_SYNTAX, GENERATION_ERROR.getEnumDescription(), e.getClass(), e.getMessage(), e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Обработчик DataIntegrityViolationException
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ApiErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e,
                                                                                     HttpServletRequest request) {
        log.error(LOGGER_SYNTAX, GENERATION_ERROR.getEnumDescription(), e.getClass(), e.getMessage(), e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Общий обработчик для всех остальных исключений
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiErrorResponse> handleAllExceptions(Exception e, HttpServletRequest request) {
        log.error("{}\\n{}\\n{}\\n{}", GENERATION_ERROR.getEnumDescription(), e.getClass(), e.getMessage(),
                e.getSuppressed(), e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ApiErrorResponse> handleAllRunTimeException(RuntimeException e, HttpServletRequest request) {
        log.error(LOGGER_SYNTAX, GENERATION_ERROR.getEnumDescription(), e.getClass(), e.getMessage(), e);

        ApiErrorResponse apiErrorResponse = buildApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage(),
                request.getRequestURI(),
                null
        );

        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ApiErrorResponse buildApiErrorResponse(HttpStatus status, String message, String path, List<Violation> violations) {
        return ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(path)
                .violations(violations)
                .build();
    }
}
