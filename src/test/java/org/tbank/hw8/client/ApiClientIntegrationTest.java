package org.tbank.hw8.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.tbank.hw8.model.Valute;

import java.util.List;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class ApiClientIntegrationTest {

    private WireMockServer wireMockServer;

    @Autowired
    private ApiClient apiClient;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    private CacheManager cacheManager;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("currency-api.cbr-api", () -> "http://localhost:8081");
    }

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8081));
        wireMockServer.start();
        WireMock.configureFor("localhost", 8081);

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("currencyApiCircuitBreaker");
        circuitBreaker.reset();

        Objects.requireNonNull(cacheManager.getCache("currencyRate")).clear();
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void testFetchCurrencyRatesSuccess() {
        stubFor(get(urlEqualTo("/XML_daily.asp"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/xml")
                        .withBodyFile("currencyRates.xml")));

        List<Valute> result = apiClient.fetchCurrencyRates();

        assertEquals(2, result.size());
        assertEquals("USD", result.get(0).getCharCode());
        assertEquals("EUR", result.get(1).getCharCode());
    }

    @Test
    void testFetchCurrencyRatesCircuitBreakerFallback() {
        stubFor(get(urlEqualTo("/XML_daily.asp"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/xml")
                        .withBody("Internal Server Error")));

        List<Valute> result = apiClient.fetchCurrencyRates();

        assertTrue(result.isEmpty());
    }

    @Test
    void testCircuitBreakerOpensAfterFailures() {
        stubFor(get(urlEqualTo("/XML_daily.asp"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/xml")
                        .withBody("Internal Server Error")));

        for (int i = 0; i < 5; i++) {
            apiClient.fetchCurrencyRates();
        }

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("currencyApiCircuitBreaker");
        assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState());
    }

    @Test
    void testCircuitBreakerResetsAfterSuccess() {
        stubFor(get(urlEqualTo("/XML_daily.asp"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/xml")
                        .withBody("Internal Server Error")));

        for (int i = 0; i < 5; i++) {
            apiClient.fetchCurrencyRates();
        }

        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("currencyApiCircuitBreaker");
        assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState());

        stubFor(get(urlEqualTo("/XML_daily.asp"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/xml")
                        .withBodyFile("currencyRates.xml")));

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        apiClient.fetchCurrencyRates();

        assertTrue(circuitBreaker.getState() == CircuitBreaker.State.HALF_OPEN || circuitBreaker.getState() == CircuitBreaker.State.CLOSED);
    }
}