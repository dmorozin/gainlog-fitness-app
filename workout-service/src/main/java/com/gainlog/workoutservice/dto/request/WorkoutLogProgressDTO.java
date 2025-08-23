package com.gainlog.workoutservice.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class WorkoutLogProgressDTO {
    private List<ProgressExerciseSetsDTO> exerciseSets;
}
