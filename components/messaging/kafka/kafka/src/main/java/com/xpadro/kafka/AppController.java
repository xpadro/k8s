package com.xpadro.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

    private final Producer producer;

    public AppController(Producer producer) {
        this.producer = producer;
    }

    @GetMapping("/demo")
    public ResponseEntity<String> sendMessage() {
        LOGGER.info("Sending message");
        producer.send();
        return ResponseEntity.ok("Request received");
    }
}
