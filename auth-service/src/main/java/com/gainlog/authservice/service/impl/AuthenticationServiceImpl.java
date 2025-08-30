package com.gainlog.authservice.service.impl;

import com.gainlog.authservice.grpc.UserServiceGrpcClient;
import com.gainlog.authservice.model.dto.LoginUserDTO;
import com.gainlog.authservice.model.dto.RegisterUserDTO;
import com.gainlog.authservice.service.AuthenticationService;
import com.gainlog.core.security.CustomUserDetails;
import com.gainlog.core.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import user.UserProto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserServiceGrpcClient userServiceGrpcClient;
    private final JwtUtil jwtUtil;
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @Value("${app.kafka.topics.userRegistered}")
    private String userRegisteredTopic;

    public void signup(RegisterUserDTO input) {
        input.setPassword(passwordEncoder.encode(input.getPassword()));
        UserProto.UserResponse userResponse = userServiceGrpcClient.createUser(input);
        try {
            kafkaTemplate.send(userRegisteredTopic, userResponse.toByteArray());
            log.info("user event sent: {}", userResponse);
        } catch (Exception e) {
            log.error("Error sending event: {}", e.getMessage());
        }
    }

    public String authenticate(LoginUserDTO input) {
        UserProto.UserResponse userResponse = userServiceGrpcClient.getUserByEmail(input.getEmail());
        if (!passwordEncoder.matches(input.getPassword(), userResponse.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        List<GrantedAuthority> authorities = userResponse.getRolesList().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        CustomUserDetails user = new CustomUserDetails(userResponse.getId(),
                userResponse.getEmail(),
                userResponse.getPassword(),
                authorities);

        return jwtUtil.generateToken(user);
    }
}
