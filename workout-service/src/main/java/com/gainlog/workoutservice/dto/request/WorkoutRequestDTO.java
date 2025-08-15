package com.gainlog.workoutservice.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class WorkoutRequestDTO {
    private String name;
    private String description;
    private List<WorkoutExerciseDTO> workoutExercises;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class WorkoutExerciseDTO {
        private String exerciseApiId;
        private int sequence;
    }
}
