package com.gainlog.authservice.exception;

import com.gainlog.core.exception.ExceptionDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@RestControllerAdvice
@Log4j2
public class AuthServiceExceptionHandlerAdvice {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionDetails> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        log.error("Authentication failed: {}", ex.getMessage(), ex);
        return getResponseForHttpStatus(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
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
