package com.gainlog.workoutservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "workouts")
public class Workout extends BaseEntity {
    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Size(max = 100)
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description")
    private String description;
}