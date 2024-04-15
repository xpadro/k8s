package com.xpadro.opentel;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class App2Client {
    private final RestTemplate restTemplate;

    public App2Client(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String call() {
        return restTemplate.getForEntity("http://localhost:8084/app2", String.class).getBody();
    }

}
