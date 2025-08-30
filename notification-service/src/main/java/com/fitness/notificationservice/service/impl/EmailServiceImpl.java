package com.fitness.notificationservice.service.impl;

import com.fitness.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import user.UserProto;

import static com.fitness.notificationservice.util.Constants.REGISTRATION_EMAIL_SUBJECT;
import static com.fitness.notificationservice.util.Constants.REGISTRATION_EMAIL_TEXT;

@Service
@Log4j2
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;

    public void sendRegistrationEmail(UserProto.UserResponse userResponse) {
        String to = userResponse.getEmail();
        try {
            sendEmail(to, REGISTRATION_EMAIL_SUBJECT, String.format(REGISTRATION_EMAIL_TEXT, to));
            log.info("Sent welcome email to: {}", to);
        } catch (MailException e) {
            log.error("Failed to send welcome email to {}: {}", to, e.getMessage());
        }
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("GainLog <noreply@gainlog.com>");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
