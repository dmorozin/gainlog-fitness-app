package com.gainlog.workoutservice.service;

import com.gainlog.workoutservice.entity.Exercise;

public interface ExerciseService {
    Exercise getExerciseByExerciseApiId(String exerciseApiId);
}
