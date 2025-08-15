package com.gainlog.workoutservice.controller;

import com.gainlog.workoutservice.dto.ExerciseApiDTO;
import com.gainlog.workoutservice.service.ExerciseAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/exercise-api")
@RequiredArgsConstructor
public class ExerciseAPIController {

    private final ExerciseAPIService exerciseAPIService;

    @GetMapping
    public ResponseEntity<List<ExerciseApiDTO>> getExercises() {
        List<ExerciseApiDTO> exercises = exerciseAPIService.getExercises();
        return ResponseEntity.ok().body(exercises);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseApiDTO> getExerciseById(@PathVariable("id") String id) {
        ExerciseApiDTO exercise = exerciseAPIService.getExerciseById(id);
        return ResponseEntity.ok().body(exercise);
    }
}
