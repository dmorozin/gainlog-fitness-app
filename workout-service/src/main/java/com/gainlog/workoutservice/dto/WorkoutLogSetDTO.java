package com.gainlog.workoutservice.dto;

import com.gainlog.workoutservice.dto.validation.ValidWorkoutLogSet;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ValidWorkoutLogSet
@Builder
public class WorkoutLogSetDTO {
    private Long setId;

    @Min(value = 1, message = "Set number must be at least 1")
    private int setNumber;

    @Min(value = 1, message = "Repetitions must be at least 1")
    private Integer repetitions;

    @DecimalMin(value = "0.0", inclusive = false, message = "Weight must be greater than 0")
    private BigDecimal weight;

    @Min(value = 1, message = "Time must be positive (seconds or ms)")
    private Long time;
}