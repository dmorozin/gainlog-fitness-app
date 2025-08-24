package com.gainlog.workoutservice.controller;

import com.gainlog.workoutservice.dto.WorkoutLogDTO;
import com.gainlog.workoutservice.dto.request.WorkoutLogProgressDTO;
import com.gainlog.workoutservice.service.WorkoutLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workout-logs")
@RequiredArgsConstructor
public class WorkoutLogController {

    private final WorkoutLogService workoutLogService;

    @PostMapping
    public ResponseEntity<WorkoutLogDTO> createWorkoutLog(@RequestParam("workoutId") final Long workoutId) {
        WorkoutLogDTO workoutLogDTO = workoutLogService.createWorkoutLog(workoutId);
        return ResponseEntity.ok(workoutLogDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutLogDTO> getWorkoutDetails(@PathVariable("id") final Long id) {
        WorkoutLogDTO workoutLogDTO = workoutLogService.getWorkoutLog(id);
        return ResponseEntity.ok().body(workoutLogDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> saveProgress(@PathVariable("id") final Long id,
                                          @RequestParam(name = "isComplete",
                                                  required = false,
                                                  defaultValue = "false") final boolean isComplete,
                                          @Valid @RequestBody WorkoutLogProgressDTO workoutRequestDTO) {
        workoutLogService.saveProgress(id, isComplete, workoutRequestDTO.getExerciseSets());
        return ResponseEntity.ok().build();
    }
}
