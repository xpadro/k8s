package com.xpadro.opentel;

import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class AppController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

    private final App2Client client;

    public AppController(App2Client client) {
        this.client = client;
    }

    @GetMapping("/app1")
    public String getMethodName() {
        LOGGER.info("Received request");
        return client.call();
    }

}
