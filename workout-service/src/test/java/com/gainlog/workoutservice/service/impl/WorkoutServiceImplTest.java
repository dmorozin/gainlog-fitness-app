package com.gainlog.workoutservice.service.impl;

import com.gainlog.core.exception.ResourceNotFoundException;
import com.gainlog.workoutservice.dto.WorkoutDetailsDTO;
import com.gainlog.workoutservice.dto.request.WorkoutRequestDTO;
import com.gainlog.workoutservice.entity.Exercise;
import com.gainlog.workoutservice.entity.Workout;
import com.gainlog.workoutservice.entity.WorkoutExercise;
import com.gainlog.workoutservice.repository.WorkoutExerciseRepository;
import com.gainlog.workoutservice.repository.WorkoutRepository;
import com.gainlog.workoutservice.service.ExerciseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class WorkoutServiceImplTest {

    @Mock
    private ExerciseService exerciseService;

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private WorkoutExerciseRepository workoutExerciseRepository;

    @InjectMocks
    private WorkoutServiceImpl workoutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createWorkout_shouldSaveWorkoutWithExercises() {
        try (var securityMock = mockStatic(com.gainlog.workoutservice.util.SecurityUtil.class)) {
            securityMock.when(com.gainlog.workoutservice.util.SecurityUtil::getUserId).thenReturn(1L);

            WorkoutRequestDTO request = new WorkoutRequestDTO();
            request.setName("Leg Day");
            request.setDescription("Legs only");
            request.setWorkoutExercises(List.of(
                    new WorkoutRequestDTO.WorkoutExerciseDTO("101", 1),
                    new WorkoutRequestDTO.WorkoutExerciseDTO("102", 2)
            ));

            Exercise exercise1 = new Exercise();
            exercise1.setId(1L);
            exercise1.setExerciseApiId("101");
            exercise1.setName("Squat");

            Exercise exercise2 = new Exercise();
            exercise2.setId(2L);
            exercise2.setExerciseApiId("102");
            exercise2.setName("Lunge");

            when(exerciseService.getExerciseByExerciseApiId("101")).thenReturn(exercise1);
            when(exerciseService.getExerciseByExerciseApiId("102")).thenReturn(exercise2);

            workoutService.createWorkout(request);

            ArgumentCaptor<Workout> workoutCaptor = ArgumentCaptor.forClass(Workout.class);
            verify(workoutRepository).save(workoutCaptor.capture());

            Workout savedWorkout = workoutCaptor.getValue();
            assertThat(savedWorkout.getName()).isEqualTo("Leg Day");
            assertThat(savedWorkout.getWorkoutExercises()).hasSize(2);
        }
    }

    @Test
    void getWorkoutDetails_shouldReturnMappedDTO() {
        try (var securityMock = mockStatic(com.gainlog.workoutservice.util.SecurityUtil.class)) {
            securityMock.when(com.gainlog.workoutservice.util.SecurityUtil::getUserId).thenReturn(1L);

            Workout workout = new Workout();
            workout.setId(1L);
            workout.setName("Chest Day");
            workout.setDescription("Chest exercises");

            WorkoutExercise we = new WorkoutExercise();
            Exercise ex = new Exercise();
            ex.setId(1L);
            ex.setExerciseApiId("201");
            ex.setName("Bench Press");
            we.setExercise(ex);
            we.setSequence(1);
            workout.addWorkoutExercise(we);

            when(workoutRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(workout));

            WorkoutDetailsDTO dto = workoutService.getWorkoutDetails(1L);

            assertThat(dto.getName()).isEqualTo("Chest Day");
            assertThat(dto.getExerciseDetails()).hasSize(1);
            assertThat(dto.getExerciseDetails().getFirst().getExerciseApiId()).isEqualTo("201");
        }
    }

    @Test
    void getWorkoutDetails_shouldThrowWhenNotFound() {
        try (var securityMock = mockStatic(com.gainlog.workoutservice.util.SecurityUtil.class)) {
            securityMock.when(com.gainlog.workoutservice.util.SecurityUtil::getUserId).thenReturn(1L);

            when(workoutRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> workoutService.getWorkoutDetails(1L));
        }
    }

    @Test
    void deleteWorkout_shouldThrowWhenNotDeleted() {
        try (var securityMock = mockStatic(com.gainlog.workoutservice.util.SecurityUtil.class)) {
            securityMock.when(com.gainlog.workoutservice.util.SecurityUtil::getUserId).thenReturn(1L);

            when(workoutRepository.deleteByIdAndUserId(1L, 1L)).thenReturn(0);

            assertThrows(ResourceNotFoundException.class, () -> workoutService.deleteWorkout(1L));
        }
    }

    @Test
    void updateWorkout_shouldUpdateWorkoutAndExercises() {
        try (var securityMock = mockStatic(com.gainlog.workoutservice.util.SecurityUtil.class)) {
            securityMock.when(com.gainlog.workoutservice.util.SecurityUtil::getUserId).thenReturn(1L);

            Workout workout = new Workout();
            workout.setId(1L);
            workout.setName("Old Name");
            workout.setDescription("Old desc");

            Exercise exercise = new Exercise();
            exercise.setId(1L);

            when(workoutRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(workout));
            when(exerciseService.getExerciseByExerciseApiId(anyString())).thenReturn(exercise);

            WorkoutRequestDTO request = new WorkoutRequestDTO();
            request.setName("New Name");
            request.setDescription("New desc");
            request.setWorkoutExercises(List.of(new WorkoutRequestDTO.WorkoutExerciseDTO("101", 1)));

            workoutService.updateWorkout(1L, request);

            verify(workoutExerciseRepository).deleteAllByWorkoutId(1L);
            verify(workoutRepository).save(workout);

            assertThat(workout.getName()).isEqualTo("New Name");
            assertThat(workout.getDescription()).isEqualTo("New desc");
        }
    }
}
