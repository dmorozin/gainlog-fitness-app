package com.gainlog.workoutservice.service.impl;

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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {

    private final ExerciseService exerciseService;
    private final WorkoutRepository workoutRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;

    @Override
    @Transactional
    public void createWorkout(WorkoutRequestDTO workoutRequestDTO) {
        Workout workout = new Workout();
        workout.setUserId(1L); // TODO get user from JWT token
        workout.setName(workoutRequestDTO.getName());
        workout.setDescription(workoutRequestDTO.getDescription());

        addWorkoutExercises(workoutRequestDTO, workout);

        workoutRepository.save(workout);
    }

    @Override
    public WorkoutDetailsDTO getWorkoutDetails(long workoutId) {
        Workout workout = getWorkoutById(workoutId);

        WorkoutDetailsDTO.WorkoutDetailsDTOBuilder builder = WorkoutDetailsDTO.builder()
                .workoutId(workoutId)
                .name(workout.getName())
                .description(workout.getDescription());

        List<WorkoutDetailsDTO.ExerciseDetailDTO> exerciseDetails = workout.getWorkoutExercises()
                .stream()
                .map(workoutExercise -> {
                    Exercise exercise = workoutExercise.getExercise();
                    return new WorkoutDetailsDTO.ExerciseDetailDTO(exercise.getId(),
                            exercise.getExerciseApiId(),
                            exercise.getName(),
                            workoutExercise.getSequence());
                })
                .toList();

        return builder
                .exerciseDetails(exerciseDetails)
                .build();
    }

    @Override
    public List<WorkoutDetailsDTO> getWorkouts() {
        long userId = 1L; // TODO get user id from JWT
        List<Workout> workouts = workoutRepository.findAllByUserId(userId);

        return workouts.stream()
                .map(workout -> WorkoutDetailsDTO.builder()
                        .workoutId(workout.getId())
                        .name(workout.getName())
                        .description(workout.getDescription())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public void deleteWorkout(long workoutId) {
        workoutRepository.deleteById(workoutId);
    }

    @Override
    @Transactional
    public WorkoutDetailsDTO updateWorkout(long workoutId, WorkoutRequestDTO workoutRequestDTO) {
        Workout workout = getWorkoutById(workoutId);
        workout.setName(workoutRequestDTO.getName());
        workout.setDescription(workoutRequestDTO.getDescription());

        workoutExerciseRepository.deleteAllByWorkoutId(workoutId);

        addWorkoutExercises(workoutRequestDTO, workout);

        workoutRepository.save(workout);

        return getWorkoutDetails(workoutId);
    }

    private void addWorkoutExercises(WorkoutRequestDTO workoutRequestDTO, Workout workout) {
        workoutRequestDTO.getWorkoutExercises()
                .forEach(dto -> {
                    Exercise exercise = exerciseService.getExerciseByExerciseApiId(dto.getExerciseApiId());
                    WorkoutExercise workoutExercise = new WorkoutExercise();
                    workoutExercise.setId(new WorkoutExerciseId());
                    workoutExercise.setSequence(dto.getSequence());
                    workoutExercise.setWorkout(workout);
                    workoutExercise.setExercise(exercise);
                    workout.addWorkoutExercise(workoutExercise);
                });

    }

    private Workout getWorkoutById(long workoutId) {
        return workoutRepository.findById(workoutId)
                .orElseThrow(() -> new RuntimeException(String.format("Workout with id %d does not exist", workoutId)));
    }
}
