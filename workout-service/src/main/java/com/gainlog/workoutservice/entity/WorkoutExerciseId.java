package com.gainlog.workoutservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class WorkoutExerciseId implements Serializable {
    @Serial
    private static final long serialVersionUID = 422753172835966603L;
    @NotNull
    @Column(name = "workout_id", nullable = false)
    private Long workoutId;

    @NotNull
    @Column(name = "exercise_id", nullable = false)
    private Long exerciseId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        WorkoutExerciseId entity = (WorkoutExerciseId) o;
        return Objects.equals(this.exerciseId, entity.exerciseId) &&
                Objects.equals(this.workoutId, entity.workoutId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exerciseId, workoutId);
    }

}