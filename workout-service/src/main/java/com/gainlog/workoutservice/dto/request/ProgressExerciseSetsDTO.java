package com.gainlog.workoutservice.dto.request;

import com.gainlog.workoutservice.dto.WorkoutLogSetDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProgressExerciseSetsDTO {
    private long exerciseId;
    private List<WorkoutLogSetDTO> sets;
}
