package com.gainlog.workoutservice.service.impl;

import com.gainlog.workoutservice.dto.ExerciseApiDTO;
import com.gainlog.workoutservice.entity.Exercise;
import com.gainlog.workoutservice.repository.ExerciseRepository;
import com.gainlog.workoutservice.service.ExerciseAPIService;
import com.gainlog.workoutservice.service.ExerciseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseAPIService exerciseAPIService;

    @Override
    @Transactional
    public Exercise getExerciseByExerciseApiId(final String exerciseApiId) {
        final Optional<Exercise> exercise = exerciseRepository.findByExerciseApiId(exerciseApiId);
        return exercise.orElseGet(() -> createExerciseFromAPI(exerciseApiId));
    }

    private Exercise createExerciseFromAPI(final String exerciseApiId) {
        final ExerciseApiDTO exerciseApiDTO = exerciseAPIService.getExerciseById(exerciseApiId);
        final Exercise exercise = new Exercise();
        exercise.setExerciseApiId(exerciseApiDTO.getId());
        exercise.setName(exerciseApiDTO.getName());
        return exerciseRepository.save(exercise);
    }
}
