package com.gainlog.workoutservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/workouts")
public class WorkoutController {

    @GetMapping
    public List<String> getWorkouts() {
        return List.of("Push", "Pull", "Legs");
    }
}
