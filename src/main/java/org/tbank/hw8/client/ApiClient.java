package org.tbank.hw8.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.http.HttpStatusCode;
import org.tbank.hw8.exception.ServiceUnavailableException;
import org.tbank.hw8.model.Valute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbank.hw8.model.ValCurs;

import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class ApiClient {
    private final RestClient restClient;
    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);

    @CircuitBreaker(name = "currencyApiCircuitBreaker", fallbackMethod = "returnEmptyValuteList")
    public List<Valute> fetchCurrencyRates() {
        try {
            ValCurs valCurs = restClient.get()
                    .uri("/XML_daily.asp")
                    .retrieve()
                    .onStatus(HttpStatusCode::is5xxServerError, (response, request) -> {
                        logger.error("Error response from currency API: {}", response);
                        throw new ServiceUnavailableException("Currency service is unavailable. Retry after 1 hour.");
                    })
                    .body(ValCurs.class);

            if (valCurs == null) {
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Currencies are empty");
            }

            return valCurs.getValutes();
        } catch (RestClientException e) {
            logger.error("Unexpected error while calling the currency service: {}", e.getMessage());
            throw new RuntimeException("Failed to fetch currency rates due to unexpected error.", e);
        } catch (Exception e) {
            logger.error("An unexpected error occurred: {}", e.getMessage());
            throw new RuntimeException("An unexpected error occurred while fetching currency rates.", e);
        }
    }

    public List<Valute> returnEmptyValuteList(Throwable throwable) {
        return Collections.emptyList();
    }

}
