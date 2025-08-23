package com.gainlog.workoutservice.exception;

public class ExerciseApiException extends RuntimeException {
    public ExerciseApiException(String message) {
        super(message);
    }

    public ExerciseApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
