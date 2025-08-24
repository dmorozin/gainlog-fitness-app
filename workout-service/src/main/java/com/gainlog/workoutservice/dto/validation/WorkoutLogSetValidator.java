package com.gainlog.workoutservice.dto.validation;

import com.gainlog.workoutservice.dto.WorkoutLogSetDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WorkoutLogSetValidator implements ConstraintValidator<ValidWorkoutLogSet, WorkoutLogSetDTO> {

    @Override
    public boolean isValid(WorkoutLogSetDTO set, ConstraintValidatorContext context) {
        if (set == null) {
            return true;
        }

        boolean hasReps = set.getRepetitions() != null && set.getRepetitions() > 0;
        boolean hasTime = set.getTime() != null && set.getTime() > 0;

        return hasReps || hasTime;
    }
}