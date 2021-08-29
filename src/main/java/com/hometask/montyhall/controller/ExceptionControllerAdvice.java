package com.hometask.montyhall.controller;

import com.hometask.montyhall.exception.GameResultException;
import com.hometask.montyhall.exception.NoSuchEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler(NoSuchEntityException.class)
    public ResponseEntity<String> handleNoSuchEntityException(NoSuchEntityException e) {
        LOGGER.debug(e.getMessage(), e.getCause());
        return new ResponseEntity<>(e.getMessage(), createHttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GameResultException.class)
    public ResponseEntity<String> handleGameResultException(GameResultException e) {
        LOGGER.debug(e.getMessage(), e.getCause());
        return new ResponseEntity<>(e.getMessage(), createHttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        LOGGER.debug(e.getMessage(), e.getCause());

        BindingResult bindingResult = e.getBindingResult();
        List<String> errorDetails = new ArrayList<>();

        bindingResult.getFieldErrors().forEach(fieldError -> errorDetails.add(fieldError.getDefaultMessage()));

        return new ResponseEntity<>(errorDetails, createHttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<String> handleMissingPathVariableException(MissingPathVariableException e) {
        LOGGER.debug(e.getMessage(), e.getCause());
        return new ResponseEntity<>(e.getMessage(), createHttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        LOGGER.debug(e.getMessage(), e.getCause());
        return new ResponseEntity<>(e.getMessage(), createHttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        LOGGER.debug(e.getMessage(), e.getCause());
        return new ResponseEntity<>(e.getMessage(), createHttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception e) {
        LOGGER.debug(e.getMessage(), e.getCause());
        return new ResponseEntity<>(e.getMessage(), createHttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpHeaders createHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");
        return httpHeaders;
    }
}