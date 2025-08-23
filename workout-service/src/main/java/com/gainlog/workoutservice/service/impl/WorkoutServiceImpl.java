package com.gainlog.workoutservice.service.impl;

import com.gainlog.core.exception.ResourceNotFoundException;
import com.gainlog.workoutservice.dto.WorkoutDetailsDTO;
import com.gainlog.workoutservice.dto.request.WorkoutRequestDTO;
import com.gainlog.workoutservice.entity.Exercise;
import com.gainlog.workoutservice.entity.Workout;
import com.gainlog.workoutservice.entity.WorkoutExercise;
import com.gainlog.workoutservice.entity.WorkoutExerciseId;
import com.gainlog.workoutservice.repository.WorkoutExerciseRepository;
import com.gainlog.workoutservice.repository.WorkoutRepository;
import com.gainlog.workoutservice.service.ExerciseService;
import com.gainlog.workoutservice.service.WorkoutService;
import com.gainlog.workoutservice.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gainlog.workoutservice.util.Constants.WORKOUT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {

    private final ExerciseService exerciseService;
    private final WorkoutRepository workoutRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;

    @Override
    @Transactional
    public void createWorkout(final WorkoutRequestDTO workoutRequestDTO) {
        final Workout workout = new Workout();
        workout.setUserId(SecurityUtil.getUserId());
        workout.setName(workoutRequestDTO.getName());
        workout.setDescription(workoutRequestDTO.getDescription());

        addWorkoutExercises(workout, workoutRequestDTO);

        workoutRepository.save(workout);
    }

    @Override
    public WorkoutDetailsDTO getWorkoutDetails(final long workoutId) {
        final Workout workout = getUserWorkoutById(workoutId);

        final List<WorkoutDetailsDTO.ExerciseDetailDTO> exerciseDetails = workout.getWorkoutExercises()
                .stream()
                .map(this::mapExerciseDetail)
                .toList();

        return WorkoutDetailsDTO.builder()
                .workoutId(workoutId)
                .name(workout.getName())
                .description(workout.getDescription())
                .exerciseDetails(exerciseDetails)
                .build();
    }

    @Override
    public List<WorkoutDetailsDTO> getUserWorkouts() {
        return workoutRepository.findAllByUserId(SecurityUtil.getUserId())
                .stream()
                .map(workout -> WorkoutDetailsDTO.builder()
                        .workoutId(workout.getId())
                        .name(workout.getName())
                        .description(workout.getDescription())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public void deleteWorkout(final long workoutId) {
        final boolean deleted = workoutRepository.deleteByIdAndUserId(workoutId, SecurityUtil.getUserId()) > 0;
        if (!deleted) {
            throw new ResourceNotFoundException(String.format("Workout with id %d does not exist", workoutId));
        }
    }

    @Override
    @Transactional
    public WorkoutDetailsDTO updateWorkout(final long workoutId, final WorkoutRequestDTO workoutRequestDTO) {
        final Workout workout = getUserWorkoutById(workoutId);

        workout.setName(workoutRequestDTO.getName());
        workout.setDescription(workoutRequestDTO.getDescription());

        workoutExerciseRepository.deleteAllByWorkoutId(workoutId);
        addWorkoutExercises(workout, workoutRequestDTO);

        workoutRepository.save(workout);

        return getWorkoutDetails(workoutId);
    }

    private void addWorkoutExercises(final Workout workout, final WorkoutRequestDTO workoutRequestDTO) {
        workoutRequestDTO.getWorkoutExercises()
                .forEach(dto -> {
                    final Exercise exercise = exerciseService.getExerciseByExerciseApiId(dto.getExerciseApiId());
                    final WorkoutExercise workoutExercise = new WorkoutExercise();
                    workoutExercise.setId(new WorkoutExerciseId());
                    workoutExercise.setSequence(dto.getSequence());
                    workoutExercise.setWorkout(workout);
                    workoutExercise.setExercise(exercise);
                    workout.addWorkoutExercise(workoutExercise);
                });
    }

    private WorkoutDetailsDTO.ExerciseDetailDTO mapExerciseDetail(final WorkoutExercise workoutExercise) {
        final Exercise exercise = workoutExercise.getExercise();
        return new WorkoutDetailsDTO.ExerciseDetailDTO(
                exercise.getId(),
                exercise.getExerciseApiId(),
                exercise.getName(),
                workoutExercise.getSequence()
        );
    }

    private Workout getUserWorkoutById(final long workoutId) {
        Long userId = SecurityUtil.getUserId();
        return workoutRepository.findByIdAndUserId(workoutId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format(WORKOUT_NOT_FOUND, workoutId, userId))
                );
    }
}
