package com.gainlog.workoutservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ExerciseApiDTO {
    private String id;
    private String name;
    private String bodyPart;
    private String target;
    private String equipment;
    private List<String> secondaryMuscles;
    private List<String> instructions;
    private String description;
    private String difficulty; // beginner | intermediate | advanced
    private String category;   // strength | cardio | mobility | balance | stretching | plyometrics | rehabilitation
}
