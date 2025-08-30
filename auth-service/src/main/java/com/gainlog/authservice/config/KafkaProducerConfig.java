package com.gainlog.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public KafkaTemplate<String, byte[]> kafkaTemplate(ProducerFactory<String, byte[]> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
