package com.gainlog.workoutservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/workouts")
@RequiredArgsConstructor
public class WorkoutController {


    @GetMapping
    public List<String> getWorkouts() {
        return Arrays.asList("push","pull","legs");
    }

//    @PostMapping
//    public WorkoutPlan create(@RequestBody WorkoutPlan plan) {
//        return workoutPlanRepository.save(plan);
//    }
}
