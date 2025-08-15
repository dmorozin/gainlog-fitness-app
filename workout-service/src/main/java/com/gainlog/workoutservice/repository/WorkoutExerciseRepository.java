package com.gainlog.workoutservice.repository;

import com.gainlog.workoutservice.entity.WorkoutExercise;
import com.gainlog.workoutservice.entity.WorkoutExerciseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, WorkoutExerciseId> {

    @Modifying
    @Query("DELETE FROM WorkoutExercise we WHERE we.workout.id = :workoutId")
    void deleteAllByWorkoutId(@Param("workoutId") Long workoutId);
}
