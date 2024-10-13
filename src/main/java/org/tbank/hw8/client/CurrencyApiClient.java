package org.tbank.hw8.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
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
@Slf4j
public class CurrencyApiClient {
    private static final String CURRENCY_API_URI = "/XML_daily.asp";
    private final RestClient restClient;

    @CircuitBreaker(name = "currencyApiCircuitBreaker", fallbackMethod = "handleFallback")
    @Cacheable("currencyRate")
    public List<Valute> fetchCurrencyRates() {
        try {
            log.info("Fetching currency rate");

            ValCurs valCurs = restClient.get()
                    .uri(CURRENCY_API_URI)
                    .retrieve()
                    .body(ValCurs.class);

            if (valCurs == null) {
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Currencies are empty");
            }

            log.info("Successfully fetched currency rates from API");

            return valCurs.getValutes();
        } catch (RestClientException e) {
            log.error("Error response from currency API: {}", e.getMessage());
            throw new ServiceUnavailableException("Currency service is unavailable. Retry after 1 hour.");
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage());
            throw new RuntimeException("An unexpected error occurred while fetching currency rates.", e);
        }
    }

    public List<Valute> handleFallback(Throwable throwable) throws Throwable {
        log.error("Circuit breaker fallback triggered due to: {}", throwable.getMessage());
        throw throwable;
    }
}
