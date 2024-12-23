package org.tbank.hw5.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Component
public abstract class ApiClient<T> {

    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);
    private final RestClient restClient;

    public ApiClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<T> fetchFromApi(String apiUrl, Class<T[]> responseType) {
        logger.info("Fetching {} from API at {}", responseType.getSimpleName(), apiUrl);

        try {
            T[] responseArray = restClient
                    .get()
                    .uri(apiUrl)
                    .retrieve()
                    .body(responseType);

            if (responseArray == null || responseArray.length == 0) {
                logger.error("Failed to fetch {} from API at {}.", responseType.getSimpleName(), apiUrl);
                throw new IllegalStateException("No " + responseType.getSimpleName() + " fetched from API.");
            }

            logger.info("Successfully fetched {} from API.", responseType.getSimpleName());
            return Arrays.asList(responseArray);

        } catch (Exception e) {
            logger.error("Error occurred while fetching {} from API at {}. Message: {}",
                    responseType.getSimpleName(), apiUrl, e.getMessage());
            throw new IllegalStateException("Failed to fetch " + responseType.getSimpleName() + " from API at " + apiUrl, e);
        }
    }
}