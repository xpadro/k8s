package com.xpadro.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class Consumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    @KafkaListener(
            topics = "#{'${kafka.topic}'}",
            groupId = "demo-consumer-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload String event) {
        LOGGER.info("Message received: {}", event);
    }
}
