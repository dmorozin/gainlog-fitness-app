package com.gainlog.workoutservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class WorkoutLogProgressDTO {

    @NotEmpty(message = "At least one exercise set must be provided")
    @Valid
    private List<ProgressExerciseSetsDTO> exerciseSets;
}
