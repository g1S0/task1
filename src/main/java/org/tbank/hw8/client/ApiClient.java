package org.tbank.hw8.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.tbank.hw8.exception.ServiceUnavailableException;
import org.tbank.hw8.model.ValCurs;
import org.tbank.hw8.model.Valute;

import java.util.List;

@Component
@AllArgsConstructor
public class ApiClient {
    private final RestClient restClient;
    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);

    @CircuitBreaker(name = "currencyApiCircuitBreaker", fallbackMethod = "handleFallback")
    public List<Valute> fetchCurrencyRates() {
        try {
            logger.info("Fetching currency rate");

            ValCurs valCurs = restClient.get()
                    .uri("/XML_daily.asp")
                    .retrieve()
                    .body(ValCurs.class);

            if (valCurs == null) {
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Currencies are empty");
            }

            logger.info("Successfully fetched currency rates from API");

            return valCurs.getValutes();
        } catch (RestClientException e) {
            logger.error("Error response from currency API: {}", e.getMessage());
            throw new ServiceUnavailableException("Currency service is unavailable. Retry after 1 hour.");
        } catch (Exception e) {
            logger.error("An unexpected error occurred: {}", e.getMessage());
            throw new RuntimeException("An unexpected error occurred while fetching currency rates.", e);
        }
    }

    public void handleFallback(Throwable throwable) throws Throwable {
        logger.error("Circuit breaker fallback triggered due to: {}", throwable.getMessage());
        throw throwable;
    }
}
