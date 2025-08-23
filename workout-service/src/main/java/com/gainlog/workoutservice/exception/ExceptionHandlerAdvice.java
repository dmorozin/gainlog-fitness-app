package com.gainlog.workoutservice.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
@Log4j2
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(buildExceptionDetails(ex, request, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExerciseApiException.class)
    public ResponseEntity<ExceptionDetails> handleExerciseApiException(ExerciseApiException ex, WebRequest request) {
        log.error("Exercise API error: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(buildExceptionDetails(ex, request, HttpStatus.BAD_GATEWAY),
                HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDetails> handleGlobalException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(buildExceptionDetails(ex, request, HttpStatus.INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ExceptionDetails buildExceptionDetails(Exception ex, WebRequest request, HttpStatus status) {
        return new ExceptionDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false),
                status.value(),
                status.getReasonPhrase()
        );
    }
}
