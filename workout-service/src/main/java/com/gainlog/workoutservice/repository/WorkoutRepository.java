package com.gainlog.workoutservice.repository;

import com.gainlog.workoutservice.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    List<Workout> findAllByUserId(long userId);
}
