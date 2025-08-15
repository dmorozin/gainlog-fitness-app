package com.gainlog.workoutservice.controller;

import com.gainlog.workoutservice.dto.WorkoutDetailsDTO;
import com.gainlog.workoutservice.dto.request.WorkoutRequestDTO;
import com.gainlog.workoutservice.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping
    public ResponseEntity<?> createWorkout(@RequestBody WorkoutRequestDTO workoutRequestDTO) {
        workoutService.createWorkout(workoutRequestDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<WorkoutDetailsDTO>> getAllWorkouts() {
        List<WorkoutDetailsDTO> workouts = workoutService.getWorkouts();
        return ResponseEntity.ok().body(workouts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutDetailsDTO> getWorkoutDetails(@PathVariable("id") Long id) {
        WorkoutDetailsDTO workoutDetails = workoutService.getWorkoutDetails(id);
        return ResponseEntity.ok().body(workoutDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutDetailsDTO> updateWorkoutDetails(@PathVariable("id") Long id,
                                                                  @RequestBody WorkoutRequestDTO workoutRequestDTO) {
        WorkoutDetailsDTO workoutDetails = workoutService.updateWorkout(id, workoutRequestDTO);
        return ResponseEntity.ok().body(workoutDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkout(@PathVariable("id") Long id) {
        workoutService.deleteWorkout(id);
        return ResponseEntity.noContent().build();
    }
}
