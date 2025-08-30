package com.fitness.notificationservice.service;

import user.UserProto;

public interface EmailService {

    void sendRegistrationEmail(UserProto.UserResponse userResponse);
}
