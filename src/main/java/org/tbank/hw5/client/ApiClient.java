package org.tbank.hw5.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public abstract class ApiClient<T> {
    private final RestClient restClient;

    public ApiClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<T> fetchFromApi(String apiUrl, Class<T[]> responseType) {
        log.info("Fetching {} from API at {}", responseType.getSimpleName(), apiUrl);

        try {
            T[] responseArray = restClient
                    .get()
                    .uri(apiUrl)
                    .retrieve()
                    .body(responseType);

            if (responseArray == null || responseArray.length == 0) {
                log.error("Failed to fetch {} from API at {}.", responseType.getSimpleName(), apiUrl);
                throw new IllegalStateException("No " + responseType.getSimpleName() + " fetched from API.");
            }

            log.info("Successfully fetched {} from API.", responseType.getSimpleName());
            return Arrays.asList(responseArray);

        } catch (Exception e) {
            log.error("Error occurred while fetching {} from API at {}. Message: {}",
                    responseType.getSimpleName(), apiUrl, e.getMessage());

            return Collections.emptyList();
        }
    }
}