package org.tbank.hw5.client;

import org.springframework.web.client.RestClient;
import org.springframework.stereotype.Component;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

@Component
public class ApiClient {

    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);
    private final RestClient restClient;

    public ApiClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public <T> List<T> fetchFromApi(String apiUrl, Class<T[]> responseType, String entityType) {
        logger.info("Fetching {} from API at {}", entityType, apiUrl);

        T[] responseArray = restClient
                .get()
                .uri(apiUrl)
                .retrieve()
                .body(responseType);

        if (responseArray == null || responseArray.length == 0) {
            logger.error("Failed to fetch {} from API at {}.", entityType, apiUrl);
            throw new IllegalStateException("No " + entityType + " fetched from API.");
        }

        logger.info("Successfully fetched {} from API.", entityType);
        return Arrays.asList(responseArray);
    }
}