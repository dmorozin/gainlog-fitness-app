package com.gainlog.workoutservice.service.impl;

import com.gainlog.workoutservice.dto.ExerciseApiDTO;
import com.gainlog.workoutservice.exception.ExerciseApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ExerciseAPIServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExerciseAPIServiceImpl exerciseAPIService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getExercises_shouldReturnList() {
        ExerciseApiDTO dto1 = new ExerciseApiDTO();
        dto1.setId("1");
        dto1.setName("Squat");

        ExerciseApiDTO dto2 = new ExerciseApiDTO();
        dto2.setId("2");
        dto2.setName("Lunge");

        ExerciseApiDTO[] array = {dto1, dto2};

        when(restTemplate.exchange(anyString(), any(), any(), eq(ExerciseApiDTO[].class)))
                .thenReturn(new ResponseEntity<>(array, HttpStatus.OK));

        List<ExerciseApiDTO> result = exerciseAPIService.getExercises();

        assertThat(result).hasSize(2);
        assertThat(result).contains(dto1, dto2);
    }

    @Test
    void getExercises_shouldReturnEmptyListWhenBodyIsNull() {
        when(restTemplate.exchange(anyString(), any(), any(), eq(ExerciseApiDTO[].class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        List<ExerciseApiDTO> result = exerciseAPIService.getExercises();

        assertThat(result).isEmpty();
    }

    @Test
    void getExerciseById_shouldReturnDto() {
        ExerciseApiDTO dto = new ExerciseApiDTO();
        dto.setId("1");
        dto.setName("Squat");

        when(restTemplate.exchange(anyString(), any(), any(), eq(ExerciseApiDTO.class)))
                .thenReturn(new ResponseEntity<>(dto, HttpStatus.OK));

        ExerciseApiDTO result = exerciseAPIService.getExerciseById("1");

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void getExerciseById_shouldThrowExceptionOnRestClientError() {
        when(restTemplate.exchange(anyString(), any(), any(), eq(ExerciseApiDTO.class)))
                .thenThrow(new RuntimeException("Connection error"));

        assertThrows(ExerciseApiException.class, () -> exerciseAPIService.getExerciseById("1"));
    }

    @Test
    void getExercises_shouldThrowExceptionOnRestClientError() {
        when(restTemplate.exchange(anyString(), any(), any(), eq(ExerciseApiDTO[].class)))
                .thenThrow(new RuntimeException("Connection error"));

        assertThrows(ExerciseApiException.class, () -> exerciseAPIService.getExercises());
    }
}
