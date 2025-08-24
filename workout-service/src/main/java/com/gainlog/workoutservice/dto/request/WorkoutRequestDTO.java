package com.gainlog.workoutservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class WorkoutRequestDTO {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    private String description;

    @NotEmpty(message = "Workout must contain at least one exercise")
    @Valid
    private List<WorkoutExerciseDTO> workoutExercises;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class WorkoutExerciseDTO {
        @NotBlank(message = "Exercise API ID is required")
        private String exerciseApiId;

        @Min(value = 1, message = "Exercise sequence must be at least 1")
        private int sequence;
    }
}
