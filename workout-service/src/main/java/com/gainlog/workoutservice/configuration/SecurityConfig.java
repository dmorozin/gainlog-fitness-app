package com.gainlog.workoutservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gainlog.core.security.JwtAuthenticationFilter;
import com.gainlog.core.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SecurityConfig {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(secretKey, jwtExpiration);
    }

    @Bean
    public JwtAuthenticationFilter jwtFilter(JwtUtil jwtUtil) {
        return new JwtAuthenticationFilter(jwtUtil, null);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/workouts/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/workout-log/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/exercise-api/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthEntryPoint())
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint jwtAuthEntryPoint() {
        return (HttpServletRequest request,
                HttpServletResponse response,
                AuthenticationException authException) -> {

            HttpStatus unauthorized = HttpStatus.UNAUTHORIZED;
            response.setStatus(unauthorized.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            Map<String, Object> body = new HashMap<>();
            body.put("timestamp", LocalDateTime.now().toString());
            body.put("status", unauthorized.value());
            body.put("error", unauthorized.getReasonPhrase());
            body.put("message", authException.getMessage());

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), body);
        };
    }

}
