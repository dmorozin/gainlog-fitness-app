package com.gainlog.workoutservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Value("${exercise_db_api_key}")
    private String rapidApiKey;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.additionalInterceptors((request, body, execution) -> {
                    request.getHeaders().set("X-RapidAPI-Key", rapidApiKey);
                    request.getHeaders().set("X-RapidAPI-Host", "exercisedb.p.rapidapi.com");
                    return execution.execute(request, body);
                })
                .build();
    }
}

