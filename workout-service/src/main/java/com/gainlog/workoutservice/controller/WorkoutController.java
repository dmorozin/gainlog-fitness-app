package com.gainlog.workoutservice.controller;

import com.gainlog.workoutservice.dto.WorkoutDetailsDTO;
import com.gainlog.workoutservice.dto.request.WorkoutRequestDTO;
import com.gainlog.workoutservice.service.WorkoutService;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> createWorkout(@Valid @RequestBody final WorkoutRequestDTO workoutRequestDTO) {
        workoutService.createWorkout(workoutRequestDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<WorkoutDetailsDTO>> getUserWorkouts() {
        List<WorkoutDetailsDTO> workouts = workoutService.getUserWorkouts();
        return ResponseEntity.ok().body(workouts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutDetailsDTO> getWorkoutDetails(@PathVariable("id") final Long id) {
        WorkoutDetailsDTO workoutDetails = workoutService.getWorkoutDetails(id);
        return ResponseEntity.ok().body(workoutDetails);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutDetailsDTO> updateWorkoutDetails(@PathVariable("id") final Long id,
                                                                  @Valid @RequestBody final WorkoutRequestDTO workoutRequestDTO) {
        WorkoutDetailsDTO workoutDetails = workoutService.updateWorkout(id, workoutRequestDTO);
        return ResponseEntity.ok().body(workoutDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkout(@PathVariable("id") final Long id) {
        workoutService.deleteWorkout(id);
        return ResponseEntity.noContent().build();
    }
}
