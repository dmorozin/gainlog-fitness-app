package com.gainlog.workoutservice.service.impl;

import com.gainlog.workoutservice.dto.ExerciseApiDTO;
import com.gainlog.workoutservice.service.ExerciseAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseAPIServiceImpl implements ExerciseAPIService {

    private final RestTemplate restTemplate;

    @Override
    public List<ExerciseApiDTO> getExercises() {
        ResponseEntity<ExerciseApiDTO[]> response = getExerciseApiResponse("", ExerciseApiDTO[].class);

        ExerciseApiDTO[] body = response.getBody();
        if (body == null || body.length == 0) {
            return Collections.emptyList();
        }

        return Arrays.asList(response.getBody());
    }

    @Override
    public ExerciseApiDTO getExerciseById(String id) {
        String urlExtension = "/exercise/" + id;
        ResponseEntity<ExerciseApiDTO> response = getExerciseApiResponse(urlExtension, ExerciseApiDTO.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Exercise API returned an error: " + response.getStatusCode());
        }

        return response.getBody();
    }

    private <T> ResponseEntity<T> getExerciseApiResponse(String urlExtension, Class<T> responseType) {
        String url = "https://exercisedb.p.rapidapi.com/exercises" + urlExtension;

        ResponseEntity<T> response;
        try {
            response = restTemplate.exchange(url,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    responseType);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to call Exercise API: " + ex.getMessage(), ex);
        }

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Exercise API returned an error: " + response.getStatusCode());
        }

        return response;
    }

}
