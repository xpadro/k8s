package com.xpadro.rabbitmq.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ScheduledProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public ScheduledProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedRate = 5000)
    public void sendMessage() {
        UUID uuid = UUID.randomUUID();
        String message = "Message " + uuid.toString();
        LOGGER.info("Sending: " + message);
        rabbitTemplate.convertAndSend(MessagingConfiguration.topicExchangeName, "messages.test.random", message);
    }
}
