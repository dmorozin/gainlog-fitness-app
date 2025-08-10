package com.gainlog.authservice.controller;

import com.gainlog.authservice.model.dto.LoginUserDTO;
import com.gainlog.authservice.model.dto.RegisterUserDTO;
import com.gainlog.authservice.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @GetMapping("/test-role")
    public ResponseEntity<String> testRole() {
        return ResponseEntity.ok("can access");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUserDTO registerUserDto) {
        authenticationService.signup(registerUserDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody LoginUserDTO loginUserDto) {
        String token = authenticationService.authenticate(loginUserDto);
        return ResponseEntity.ok(token);
    }
}