package com.gainlog.userservice.exception;

import com.gainlog.core.exception.ExceptionDetails;
import com.gainlog.core.exception.ResourceNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Log4j2
public class UserServiceExceptionHandlerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return getResponseForHttpStatus(HttpStatus.NOT_FOUND, ex.getMessage(), request);
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
