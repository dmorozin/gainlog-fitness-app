package com.gainlog.authservice.model.dto;

import lombok.Data;

@Data
public class LoginUserDTO {
    private String email;
    private String password;
}