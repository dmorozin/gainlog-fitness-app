package com.gainlog.workoutservice.dto.request;

import com.gainlog.workoutservice.dto.WorkoutLogSetDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProgressExerciseSetsDTO {

    @Min(value = 1, message = "Exercise ID must be positive")
    private long exerciseId;

    @NotEmpty(message = "At least one set must be logged for exercise")
    @Valid
    private List<WorkoutLogSetDTO> sets;
}
