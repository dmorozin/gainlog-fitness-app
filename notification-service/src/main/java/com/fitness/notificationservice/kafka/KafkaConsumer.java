package com.fitness.notificationservice.kafka;

import com.fitness.notificationservice.service.EmailService;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import user.UserProto;

@Service
@Log4j2
@RequiredArgsConstructor
public class KafkaConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "${app.kafka.topics.userRegistered}", groupId = "${app.kafka.groupId}")
    public void listenOnUserRegistered(byte[] message) {
        try {
            UserProto.UserResponse userResponse = UserProto.UserResponse.parseFrom(message);
            log.info("Received user response: {}", userResponse);
            emailService.sendRegistrationEmail(userResponse);
        } catch (InvalidProtocolBufferException e) {
            log.error("Error deserializing event {}", e.getMessage());
        }
    }
}
