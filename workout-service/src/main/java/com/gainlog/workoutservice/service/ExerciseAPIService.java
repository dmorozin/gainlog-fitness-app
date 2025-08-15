package com.gainlog.workoutservice.service;

import com.gainlog.workoutservice.dto.ExerciseApiDTO;

import java.util.List;

public interface ExerciseAPIService {
    List<ExerciseApiDTO> getExercises();

    ExerciseApiDTO getExerciseById(String id);

}
