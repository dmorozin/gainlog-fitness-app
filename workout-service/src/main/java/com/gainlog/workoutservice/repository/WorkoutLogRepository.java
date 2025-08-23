package com.gainlog.workoutservice.repository;

import com.gainlog.workoutservice.entity.WorkoutLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, Long> {

    Optional<WorkoutLog> findByIdAndWorkout_UserId(long id, long userId);
}
