package com.gainlog.authservice.exception;

import com.gainlog.core.exception.ExceptionDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Log4j2
public class AuthServiceExceptionHandlerAdvice {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionDetails> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        log.error("Authentication failed: {}", ex.getMessage(), ex);
        return getResponseForHttpStatus(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
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
