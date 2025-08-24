package com.gainlog.workoutservice.dto.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WorkoutLogSetValidator.class)
@Documented
public @interface ValidWorkoutLogSet {

    String message() default "Set must have either repetitions or time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
