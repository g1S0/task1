package org.tbank.hw5.client;

import org.springframework.web.client.RestClient;
import org.springframework.stereotype.Component;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

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
    }
}