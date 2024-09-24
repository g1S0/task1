package org.tbank.hw5.client;

import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Component
public class ApiClient {

    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);
    private final RestTemplate restTemplate;

    public ApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> T fetchFromApi(String apiUrl, Class<T> responseType, String entityType) {
        logger.info("Fetching {} from API at {}", entityType, apiUrl);

        T response = restTemplate.getForObject(apiUrl, responseType);

        if (response != null) {
            logger.info("Successfully fetched {} from API.", entityType);
        } else {
            logger.warn("No {} fetched from API.", entityType);
        }

        return response;
    }
}