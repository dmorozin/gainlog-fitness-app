package com.gainlog.workoutservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "workout_logs")
public class WorkoutLog extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "workout_id")
    private Workout workout;

    @NotNull
    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;

    @OneToMany(mappedBy = "workoutLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkoutLogSet> sets = new LinkedHashSet<>();
}