package com.gainlog.authservice.model.dto;

import lombok.Data;

@Data
public class RegisterUserDTO {
    private String email;
    private String password;
    private String username;
    private String firstName;
    private String lastName;
}
    