package com.xpadro.opentel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AppController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

    private final BusinessService service;

    public AppController(BusinessService service) {
        this.service = service;
    }

    @GetMapping("/app2")
    public String getMethodName() {
        LOGGER.info("Received request");
        return "result from app2: " + service.perform();
    }

}
