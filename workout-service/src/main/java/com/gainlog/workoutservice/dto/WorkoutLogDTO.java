package com.gainlog.workoutservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class WorkoutLogDTO {
    private long workoutLogId;
    private long workoutId;
    private String workoutName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime; // null at start
    private List<WorkoutLogExerciseDTO> exercises;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class WorkoutLogExerciseDTO {
        private long exerciseId;
        private String exerciseName;
        private int sequence;
        private List<WorkoutLogSetDTO> sets;
    }
}
