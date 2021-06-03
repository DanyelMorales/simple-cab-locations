package com.awesome.cab.cab;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {IllegalArgumentException.class, IllegalStateException.class, NullPointerException.class})
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "something went wrong, payload not well formed or not initialized...";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value
            = {org.hibernate.PropertyValueException.class})
    protected ResponseEntity<Object> handleEmptyPropertyValue(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "There are required fields you must provide, check documentation";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value
            = {java.sql.SQLIntegrityConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolation(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Not allowed, verify if entity is unique";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
