package com.gainlog.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ExceptionDetails {
    private LocalDateTime timestamp;
    private String message;
    private String details;
    private int status;
    private String error;

    public static ExceptionDetails buildExceptionDetails(String message, WebRequest request, HttpStatus status) {
        return new ExceptionDetails(
                LocalDateTime.now(),
                message,
                request.getDescription(false),
                status.value(),
                status.getReasonPhrase()
        );
    }
}
