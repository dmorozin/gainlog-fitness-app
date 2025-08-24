package com.gainlog.authservice.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterUserDTO {

    @NotBlank(message = "email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 3, message = "Password must be at least 3 chars long")
    private String password;

    @NotBlank(message = "username is required")
    @Size(max = 70, message = "username cannot exceed 100 characters")
    private String username;

    @NotBlank(message = "firstName is required")
    @Size(max = 100, message = "firstName cannot exceed 100 characters")
    private String firstName;

    @NotBlank(message = "lastName is required")
    @Size(max = 100, message = "lastName cannot exceed 100 characters")
    private String lastName;
}
    