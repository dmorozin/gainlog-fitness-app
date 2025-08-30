package com.gainlog.authservice.service;

import com.gainlog.authservice.model.dto.LoginUserDTO;
import com.gainlog.authservice.model.dto.RegisterUserDTO;

public interface AuthenticationService {
    void signup(RegisterUserDTO input);

    String authenticate(LoginUserDTO input);
}
