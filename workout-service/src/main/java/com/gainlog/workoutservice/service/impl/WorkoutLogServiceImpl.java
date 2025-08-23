package com.gainlog.workoutservice.service.impl;

import com.gainlog.core.exception.ResourceNotFoundException;
import com.gainlog.workoutservice.dto.WorkoutLogDTO;
import com.gainlog.workoutservice.dto.WorkoutLogSetDTO;
import com.gainlog.workoutservice.dto.request.ProgressExerciseSetsDTO;
import com.gainlog.workoutservice.entity.*;
import com.gainlog.workoutservice.repository.WorkoutLogRepository;
import com.gainlog.workoutservice.repository.WorkoutLogSetRepository;
import com.gainlog.workoutservice.repository.WorkoutRepository;
import com.gainlog.workoutservice.service.WorkoutLogService;
import com.gainlog.workoutservice.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.gainlog.workoutservice.util.Constants.*;

@Service
@RequiredArgsConstructor
public class WorkoutLogServiceImpl implements WorkoutLogService {

    private final WorkoutRepository workoutRepository;
    private final WorkoutLogRepository workoutLogRepository;
    private final WorkoutLogSetRepository workoutLogSetRepository;

    @Override
    @Transactional
    public WorkoutLogDTO createWorkoutLog(final long workoutId) {
        final Long userId = SecurityUtil.getUserId();
        final Workout workout = workoutRepository.findByIdAndUserId(workoutId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(WORKOUT_NOT_FOUND, workoutId, userId)));

        final WorkoutLog workoutLog = new WorkoutLog();
        workoutLog.setWorkout(workout);
        workoutLog.setStartDateTime(LocalDateTime.now());
        workoutLogRepository.save(workoutLog);

        return WorkoutLogDTO.builder()
                .workoutLogId(workoutLog.getId())
                .workoutId(workoutId)
                .workoutName(workout.getName())
                .startDateTime(workoutLog.getStartDateTime())
                .build();
    }

    @Override
    @Transactional
    public void saveProgress(final long workoutLogId,
                             final boolean isComplete,
                             final List<ProgressExerciseSetsDTO> workoutLogProgress) {
        Long userId = SecurityUtil.getUserId();
        final WorkoutLog workoutLog = workoutLogRepository.findByIdAndWorkout_UserId(workoutLogId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format(WORKOUT_LOG_NOT_FOUND, workoutLogId, userId))
                );

        if (isComplete) {
            workoutLog.setEndDateTime(LocalDateTime.now());
        }

        workoutLogProgress.forEach(log ->
                log.getSets().forEach(setDTO -> {
                    final WorkoutLogSet workoutLogSet = (setDTO.getSetId() != null)
                            ? workoutLogSetRepository.findById(setDTO.getSetId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(String.format(WORKOUT_LOG_SET_NOT_FOUND,
                                            setDTO.getSetId()))
                            )
                            : createNewWorkoutLogSet(workoutLog, log.getExerciseId());

                    workoutLogSet.setSetNumber(setDTO.getSetNumber());
                    workoutLogSet.setRepetitions(setDTO.getRepetitions());
                    workoutLogSet.setWeight(setDTO.getWeight());
                    workoutLogSet.setTime(setDTO.getTime());

                    workoutLogSetRepository.save(workoutLogSet);
                })
        );
    }

    @Override
    public WorkoutLogDTO getWorkoutLog(final long workoutLogId) {
        final Long userId = SecurityUtil.getUserId();
        final WorkoutLog workoutLog = workoutLogRepository.findByIdAndWorkout_UserId(workoutLogId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(WORKOUT_LOG_NOT_FOUND, workoutLogId, userId)));

        final Workout workout = workoutLog.getWorkout();

        final List<WorkoutLogDTO.WorkoutLogExerciseDTO> exercises = mapWorkoutExercises(workout);

        final Map<Long, List<WorkoutLogSetDTO>> workoutLogSetDTOMap =
                mapWorkoutLogSets(workoutLogId, exercises);

        addSetsToExercises(exercises, workoutLogSetDTOMap);

        return WorkoutLogDTO.builder()
                .workoutLogId(workoutLog.getId())
                .workoutId(workout.getId())
                .workoutName(workout.getName())
                .startDateTime(workoutLog.getStartDateTime())
                .endDateTime(workoutLog.getEndDateTime())
                .exercises(exercises)
                .build();
    }

    private WorkoutLogSet createNewWorkoutLogSet(final WorkoutLog workoutLog, final Long exerciseId) {
        final WorkoutLogSet workoutLogSet = new WorkoutLogSet();
        workoutLogSet.setWorkoutLog(workoutLog);

        final Exercise exercise = new Exercise();
        exercise.setId(exerciseId);
        workoutLogSet.setExercise(exercise);

        return workoutLogSet;
    }

    private List<WorkoutLogDTO.WorkoutLogExerciseDTO> mapWorkoutExercises(final Workout workout) {
        return workout.getWorkoutExercises()
                .stream()
                .sorted(Comparator.comparingInt(WorkoutExercise::getSequence))
                .map(we -> WorkoutLogDTO.WorkoutLogExerciseDTO.builder()
                        .exerciseId(we.getExercise().getId())
                        .exerciseName(we.getExercise().getName())
                        .sequence(we.getSequence())
                        .sets(new ArrayList<>())
                        .build())
                .toList();
    }

    private Map<Long, List<WorkoutLogSetDTO>> mapWorkoutLogSets(final long workoutLogId,
                                                                final List<WorkoutLogDTO.WorkoutLogExerciseDTO> exercises) {
        final List<Long> exerciseIds = exercises.stream()
                .map(WorkoutLogDTO.WorkoutLogExerciseDTO::getExerciseId)
                .toList();

        return workoutLogSetRepository.findAllByWorkoutLogIdAndExerciseIdInOrderBySetNumberAsc(workoutLogId, exerciseIds)
                .stream()
                .collect(Collectors.groupingBy(
                        set -> set.getExercise().getId(),
                        Collectors.mapping(
                                set -> new WorkoutLogSetDTO(
                                        set.getId(),
                                        set.getSetNumber(),
                                        set.getRepetitions(),
                                        set.getWeight(),
                                        set.getTime()
                                ),
                                Collectors.toList()
                        )
                ));
    }

    private void addSetsToExercises(final List<WorkoutLogDTO.WorkoutLogExerciseDTO> exercises,
                                    final Map<Long, List<WorkoutLogSetDTO>> workoutLogSetDTOMap) {
        exercises.forEach(exercise -> {
            final List<WorkoutLogSetDTO> sets =
                    workoutLogSetDTOMap.getOrDefault(exercise.getExerciseId(), Collections.emptyList());
            sets.sort(Comparator.comparingInt(WorkoutLogSetDTO::getSetNumber));
            exercise.getSets().addAll(sets);
        });
    }
}
