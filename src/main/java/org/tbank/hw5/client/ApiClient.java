package org.tbank.hw5.client;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

@Component
@Slf4j
public abstract class ApiClient<T> {
    @Value("${api.maxConcurrentRequests}")
    private int maxConcurrentRequests;
    private Semaphore semaphore;

    private final RestClient restClient;

    public ApiClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @PostConstruct
    private void init() {
        this.semaphore = new Semaphore(maxConcurrentRequests);
    }

    public List<T> fetchFromApi(String apiUrl, Class<T[]> responseType) {
        log.info("Fetching {} from API at {}", responseType.getSimpleName(), apiUrl);
        try {
            semaphore.acquire();
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
            System.out.println(e);
            log.error("Error occurred while fetching {} from API at {}. Message: {}",
                    responseType.getSimpleName(), apiUrl, e.getMessage());

            return Collections.emptyList();
        } finally {
            semaphore.release();
        }
    }

    public T fetchSingleFromApi(String apiUrl, Class<T> responseType) {
        log.info("Fetching single {} from API at {}", responseType.getSimpleName(), apiUrl);
        try {
            semaphore.acquire();
            T response = restClient
                    .get()
                    .uri(apiUrl)
                    .retrieve()
                    .body(responseType);

            if (response == null) {
                log.error("Failed to fetch single {} from API at {}.", responseType.getSimpleName(), apiUrl);
                throw new IllegalStateException("No " + responseType.getSimpleName() + " fetched from API.");
            }
            log.info("Successfully fetched single {} from API.", responseType.getSimpleName());
            return response;
        } catch (Exception e) {
            log.error("e: ", e);
            log.error("Error occurred while fetching single {} from API at {}. Message: {}",
                    responseType.getSimpleName(), apiUrl, e.getMessage());
            throw new RuntimeException("Failed to fetch single " + responseType.getSimpleName() + " from API at " + apiUrl, e);
        } finally {
            semaphore.release();
        }
    }
}