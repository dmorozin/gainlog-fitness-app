package com.gainlog.workoutservice.repository;

import com.gainlog.workoutservice.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    List<Workout> findAllByUserId(long userId);

    Optional<Workout> findByIdAndUserId(long id, long userId);

    int deleteByIdAndUserId(Long id, Long userId);
}
