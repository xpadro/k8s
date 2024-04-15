package com.xpadro.opentel;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {
    private static final String USER_ID = "123";

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessService.class);
    private final ObservationRegistry registry;

    public BusinessService(ObservationRegistry registry) {
        this.registry = registry;
    }

    //This service shows how to create a custom span with business context
    public String perform() {

        Observation.createNotStarted("user-request", registry)
                .contextualName("retrieve-user")
                .lowCardinalityKeyValue("userType", "Customer")
                .highCardinalityKeyValue("userId", USER_ID)
                // Starts an observation, opens a scope, runs user's code, closes the scope and stops the observation
                .observe(() -> LOGGER.info("Returning user {}...", USER_ID));

        return USER_ID;
    }
}
