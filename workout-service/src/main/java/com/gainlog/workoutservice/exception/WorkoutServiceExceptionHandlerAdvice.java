package com.gainlog.workoutservice.exception;

import com.gainlog.core.exception.ExceptionDetails;
import com.gainlog.core.exception.ResourceNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestControllerAdvice
@Log4j2
public class WorkoutServiceExceptionHandlerAdvice {

    @ExceptionHandler(ExerciseApiException.class)
    public ResponseEntity<ExceptionDetails> handleExerciseApiException(ExerciseApiException ex, WebRequest request) {
        log.error("Exercise API error: {}", ex.getMessage(), ex);
        return getResponseForHttpStatus(HttpStatus.BAD_GATEWAY, ex.getMessage() != null ? ex.getMessage() : "Error occurred while processing exercise API request", request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        log.error("Resource not found: {}", ex.getMessage(), ex);
        String errorMessage = ex.getMessage() != null ? ex.getMessage() : "The requested resource was not found";
        return getResponseForHttpStatus(HttpStatus.NOT_FOUND, errorMessage, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDetails> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        String message = String.join("; ", errors);

        return getResponseForHttpStatus(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDetails> handleGlobalException(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return getResponseForHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    private ResponseEntity<ExceptionDetails> getResponseForHttpStatus(HttpStatus status,
                                                                      String message,
                                                                      WebRequest request) {
        return new ResponseEntity<>(ExceptionDetails.buildExceptionDetails(message, request, status), status);
    }
}
