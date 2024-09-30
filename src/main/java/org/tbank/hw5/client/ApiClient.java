package org.tbank.hw5.client;

import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

@Component
public class ApiClient {

    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);
    private final RestTemplate restTemplate;

    public ApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> List<T> fetchFromApi(String apiUrl, Class<T[]> responseType, String entityType) {
        logger.info("Fetching {} from API at {}", entityType, apiUrl);

        T[] responseArray = restTemplate.getForObject(apiUrl, responseType);

        if (responseArray == null || responseArray.length == 0) {
            logger.error("Failed to fetch {} from API at {}.", entityType, apiUrl);
            throw new IllegalStateException("No " + entityType + " fetched from API.");
        }

        logger.info("Successfully fetched {} from API.", entityType);
        return Arrays.asList(responseArray);
    }
}