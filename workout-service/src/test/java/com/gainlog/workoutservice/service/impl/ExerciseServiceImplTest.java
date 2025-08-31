package com.gainlog.workoutservice.service.impl;

import com.gainlog.workoutservice.dto.ExerciseApiDTO;
import com.gainlog.workoutservice.entity.Exercise;
import com.gainlog.workoutservice.repository.ExerciseRepository;
import com.gainlog.workoutservice.service.ExerciseAPIService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ExerciseServiceImplTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private ExerciseAPIService exerciseAPIService;

    @InjectMocks
    private ExerciseServiceImpl exerciseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getExerciseByExerciseApiId_shouldReturnExistingExercise() {
        Exercise exercise = new Exercise();
        exercise.setId(1L);
        exercise.setExerciseApiId("101");
        exercise.setName("Squat");

        when(exerciseRepository.findByExerciseApiId("101")).thenReturn(Optional.of(exercise));

        Exercise result = exerciseService.getExerciseByExerciseApiId("101");

        assertThat(result).isEqualTo(exercise);
        verify(exerciseRepository, never()).save(any());
        verify(exerciseAPIService, never()).getExerciseById(anyString());
    }

    @Test
    void getExerciseByExerciseApiId_shouldCreateExerciseIfNotFound() {
        when(exerciseRepository.findByExerciseApiId("102")).thenReturn(Optional.empty());

        ExerciseApiDTO apiDTO = new ExerciseApiDTO();
        apiDTO.setId("102");
        apiDTO.setName("Lunge");

        when(exerciseAPIService.getExerciseById("102")).thenReturn(apiDTO);

        Exercise result = exerciseService.getExerciseByExerciseApiId("102");

        ArgumentCaptor<Exercise> exerciseCaptor = ArgumentCaptor.forClass(Exercise.class);
        verify(exerciseRepository).save(exerciseCaptor.capture());

        Exercise savedExercise = exerciseCaptor.getValue();
        assertThat(savedExercise.getExerciseApiId()).isEqualTo("102");
        assertThat(savedExercise.getName()).isEqualTo("Lunge");

//        assertThat(result).isEqualTo(savedExercise);
    }
}
