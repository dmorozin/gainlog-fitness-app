package com.gainlog.workoutservice.service;

import com.gainlog.workoutservice.dto.WorkoutDetailsDTO;
import com.gainlog.workoutservice.dto.request.WorkoutRequestDTO;

import java.util.List;

public interface WorkoutService {
    void createWorkout(WorkoutRequestDTO workoutRequestDTO);

    WorkoutDetailsDTO getWorkoutDetails(long workoutId);

    List<WorkoutDetailsDTO> getUserWorkouts();

    void deleteWorkout(long workoutId);

    WorkoutDetailsDTO updateWorkout(long workoutId, WorkoutRequestDTO workoutRequestDTO);
}
