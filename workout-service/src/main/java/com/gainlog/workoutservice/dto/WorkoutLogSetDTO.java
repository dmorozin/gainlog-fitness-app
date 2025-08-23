package com.gainlog.workoutservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkoutLogSetDTO {
    private Long setId;
    private int setNumber;
    private Integer repetitions;
    private BigDecimal weight;
    private Long time;
}