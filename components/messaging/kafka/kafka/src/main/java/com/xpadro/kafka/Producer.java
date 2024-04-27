package com.xpadro.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class Producer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic}")
    private String topicName;

    @Autowired
    public Producer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send() {
        ProducerRecord<String, Object> record = new ProducerRecord<>(topicName, "test message");
        CompletableFuture<SendResult<String, Object>> result = kafkaTemplate.send(record);
        result.thenAccept(r -> {
            LOGGER.info(r.getProducerRecord().key());
        });
    }
}
