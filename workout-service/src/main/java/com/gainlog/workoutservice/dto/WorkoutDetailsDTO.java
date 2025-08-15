package com.gainlog.workoutservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class WorkoutDetailsDTO {
    private long workoutId;
    private String name;
    private String description;
    private List<ExerciseDetailDTO> exerciseDetails;

    @Getter
    @AllArgsConstructor
    public static class ExerciseDetailDTO {
        private long exerciseId;
        private String exerciseApiId;
        private String name;
        private int sequence;
    }
}
