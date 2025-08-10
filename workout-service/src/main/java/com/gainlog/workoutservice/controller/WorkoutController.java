package com.gainlog.workoutservice.controller;

import com.gainlog.workoutservice.model.WorkoutPlan;
import com.gainlog.workoutservice.repository.WorkoutPlanRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutPlanRepository workoutPlanRepository;


    @GetMapping
    public List<WorkoutPlan> getWorkouts() {
        return workoutPlanRepository.findAll();
    }

    @PostMapping
    public WorkoutPlan create(@RequestBody WorkoutPlan plan) {
        return workoutPlanRepository.save(plan);
    }
}
