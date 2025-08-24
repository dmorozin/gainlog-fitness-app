package com.gainlog.authservice.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginUserDTO {
    @NotBlank(message = "email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 3, message = "Password must be at least 3 chars long")
    private String password;
}