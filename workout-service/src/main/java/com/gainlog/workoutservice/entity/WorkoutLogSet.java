package com.gainlog.workoutservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "workout_log_sets")
public class WorkoutLogSet extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "workout_log_id")
    private WorkoutLog workoutLog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @NotNull
    @Column(name = "set_number", nullable = false)
    private Integer setNumber;

    @Column(name = "repetitions")
    private Integer repetitions;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "\"time\"")
    private Long time;
}