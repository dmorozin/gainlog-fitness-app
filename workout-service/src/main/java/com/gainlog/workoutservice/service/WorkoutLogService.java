package com.gainlog.workoutservice.service;

import com.gainlog.workoutservice.dto.WorkoutLogDTO;
import com.gainlog.workoutservice.dto.request.ProgressExerciseSetsDTO;

import java.util.List;

public interface WorkoutLogService {

    WorkoutLogDTO createWorkoutLog(long workoutId);

    WorkoutLogDTO getWorkoutLog(long workoutLogId);

    void saveProgress(long workoutLogId,
                      boolean isComplete,
                      List<ProgressExerciseSetsDTO> workoutLogProgress);
}
