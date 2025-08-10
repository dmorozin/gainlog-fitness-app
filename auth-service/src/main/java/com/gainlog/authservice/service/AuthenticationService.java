package com.gainlog.authservice.service;

import com.gainlog.authservice.grpc.UserServiceGrpcClient;
import com.gainlog.authservice.model.dto.LoginUserDTO;
import com.gainlog.authservice.model.dto.RegisterUserDTO;
import com.gainlog.authservice.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import user.UserProto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserServiceGrpcClient userServiceGrpcClient;
    private final JwtUtil jwtUtil;

    public void signup(RegisterUserDTO input) {
        input.setPassword(passwordEncoder.encode(input.getPassword()));
        userServiceGrpcClient.createUser(input);
    }

    public String authenticate(LoginUserDTO input) {
        UserProto.UserResponse userResponse = userServiceGrpcClient.getUserByEmail(input.getEmail());
        if (!passwordEncoder.matches(input.getPassword(), userResponse.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        List<GrantedAuthority> authorities = userResponse.getRolesList().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        User user = new User(userResponse.getEmail(), userResponse.getPassword(), authorities);

        return jwtUtil.generateToken(user);
    }
}
