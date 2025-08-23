package com.gainlog.workoutservice.repository;

import com.gainlog.workoutservice.entity.WorkoutLogSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutLogSetRepository extends JpaRepository<WorkoutLogSet, Long> {

    List<WorkoutLogSet> findAllByWorkoutLogIdAndExerciseIdInOrderBySetNumberAsc(long workoutLogId,
                                                                                List<Long> exerciseIds);
}
