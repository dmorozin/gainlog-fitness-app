package com.gainlog.workoutservice.service.impl;

import com.gainlog.core.exception.ResourceNotFoundException;
import com.gainlog.workoutservice.dto.WorkoutLogDTO;
import com.gainlog.workoutservice.dto.WorkoutLogSetDTO;
import com.gainlog.workoutservice.dto.request.ProgressExerciseSetsDTO;
import com.gainlog.workoutservice.entity.*;
import com.gainlog.workoutservice.repository.WorkoutLogRepository;
import com.gainlog.workoutservice.repository.WorkoutLogSetRepository;
import com.gainlog.workoutservice.repository.WorkoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;

class WorkoutLogServiceImplTest {

    @Mock
    private WorkoutRepository workoutRepository;

    @Mock
    private WorkoutLogRepository workoutLogRepository;

    @Mock
    private WorkoutLogSetRepository workoutLogSetRepository;

    @InjectMocks
    private WorkoutLogServiceImpl workoutLogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createWorkoutLog_shouldReturnWorkoutLogDTO() {
        try (var securityMock = mockStatic(com.gainlog.workoutservice.util.SecurityUtil.class)) {
            securityMock.when(com.gainlog.workoutservice.util.SecurityUtil::getUserId).thenReturn(1L);

            Workout workout = new Workout();
            workout.setId(10L);
            workout.setName("Leg Day");

            when(workoutRepository.findByIdAndUserId(10L, 1L)).thenReturn(Optional.of(workout));

            when(workoutLogRepository.save(any(WorkoutLog.class))).thenAnswer(invocation -> {
                WorkoutLog log = invocation.getArgument(0);
                log.setId(100L);
                return log;
            });

            WorkoutLogDTO dto = workoutLogService.createWorkoutLog(10L);

            assertThat(dto.getWorkoutLogId()).isEqualTo(100L);
            assertThat(dto.getWorkoutId()).isEqualTo(10L);
            assertThat(dto.getWorkoutName()).isEqualTo("Leg Day");
        }
    }

    @Test
    void createWorkoutLog_shouldThrowIfWorkoutNotFound() {
        try (var securityMock = mockStatic(com.gainlog.workoutservice.util.SecurityUtil.class)) {
            securityMock.when(com.gainlog.workoutservice.util.SecurityUtil::getUserId).thenReturn(1L);

            when(workoutRepository.findByIdAndUserId(10L, 1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> workoutLogService.createWorkoutLog(10L));
        }
    }

    @Test
    void saveProgress_shouldCreateNewSets() {
        try (var securityMock = mockStatic(com.gainlog.workoutservice.util.SecurityUtil.class)) {
            securityMock.when(com.gainlog.workoutservice.util.SecurityUtil::getUserId).thenReturn(1L);

            WorkoutLog workoutLog = new WorkoutLog();
            workoutLog.setId(100L);
            workoutLog.setWorkout(new Workout());

            when(workoutLogRepository.findByIdAndWorkout_UserId(100L, 1L)).thenReturn(Optional.of(workoutLog));
            when(workoutLogSetRepository.save(any(WorkoutLogSet.class))).thenAnswer(i -> i.getArguments()[0]);

            ProgressExerciseSetsDTO logDTO = new ProgressExerciseSetsDTO();
            logDTO.setExerciseId(1L);
            logDTO.setSets(List.of(
                    WorkoutLogSetDTO.builder()
                            .setId(null)
                            .setNumber(1)
                            .repetitions(10)
                            .weight(new BigDecimal("50.0"))
                            .time(60L)
                            .build()
            ));

            workoutLogService.saveProgress(100L, true, List.of(logDTO));

            ArgumentCaptor<WorkoutLogSet> captor = ArgumentCaptor.forClass(WorkoutLogSet.class);
            verify(workoutLogSetRepository).save(captor.capture());

            WorkoutLogSet savedSet = captor.getValue();
            assertThat(savedSet.getExercise().getId()).isEqualTo(1L);
            assertThat(savedSet.getSetNumber()).isEqualTo(1);
            assertThat(savedSet.getRepetitions()).isEqualTo(10);
            assertThat(savedSet.getWeight()).isEqualTo(new BigDecimal("50.0"));
            assertThat(savedSet.getTime()).isEqualTo(60);
            assertThat(workoutLog.getEndDateTime()).isNotNull();
        }
    }

    @Test
    void getWorkoutLog_shouldReturnMappedDTO() {
        try (var securityMock = mockStatic(com.gainlog.workoutservice.util.SecurityUtil.class)) {
            securityMock.when(com.gainlog.workoutservice.util.SecurityUtil::getUserId).thenReturn(1L);

            Workout workout = new Workout();
            workout.setId(10L);
            workout.setName("Leg Day");

            Exercise exercise = new Exercise();
            exercise.setId(1L);
            exercise.setName("Squat");

            WorkoutExercise we = new WorkoutExercise();
            we.setExercise(exercise);
            we.setSequence(1);
            workout.setWorkoutExercises(Set.of(we));

            WorkoutLog workoutLog = new WorkoutLog();
            workoutLog.setId(100L);
            workoutLog.setWorkout(workout);
            workoutLog.setStartDateTime(LocalDateTime.now());

            when(workoutLogRepository.findByIdAndWorkout_UserId(100L, 1L)).thenReturn(Optional.of(workoutLog));
            when(workoutLogSetRepository.findAllByWorkoutLogIdAndExerciseIdInOrderBySetNumberAsc(100L, List.of(1L)))
                    .thenReturn(new ArrayList<>());

            WorkoutLogDTO dto = workoutLogService.getWorkoutLog(100L);

            assertThat(dto.getWorkoutLogId()).isEqualTo(100L);
            assertThat(dto.getWorkoutId()).isEqualTo(10L);
            assertThat(dto.getExercises()).hasSize(1);
            assertThat(dto.getExercises().get(0).getExerciseName()).isEqualTo("Squat");
        }
    }

    @Test
    void getWorkoutLog_shouldThrowIfNotFound() {
        try (var securityMock = mockStatic(com.gainlog.workoutservice.util.SecurityUtil.class)) {
            securityMock.when(com.gainlog.workoutservice.util.SecurityUtil::getUserId).thenReturn(1L);

            when(workoutLogRepository.findByIdAndWorkout_UserId(100L, 1L)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> workoutLogService.getWorkoutLog(100L));
        }
    }
}
