package com.gainlog.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-role")
@RequiredArgsConstructor
public class TestRoleController {

    @GetMapping
    public ResponseEntity<String> testRole() {
        return ResponseEntity.ok("can access");
    }
}



