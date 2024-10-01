package org.tbank.hw5.initializer;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.tbank.hw5.client.CategoryApiClient;
import org.tbank.hw5.client.LocationApiClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DataSourceFailureInitializationIT {
    @Container
    static WireMockContainer wireMock = new WireMockContainer("wiremock/wiremock:3.6.0");

    @LocalServerPort
    private Integer port;

    @Autowired
    private CategoryApiClient categoryApiClient;

    @Autowired
    private LocationApiClient locationApiClient;

    private static void setDefaultCategoryStub() {
        WireMock.stubFor(get(urlEqualTo("/place-categories/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("[{\"id\": 1, \"slug\":\"airports\", \"name\":\"Airports\"}, " +
                                "{\"id\": 2, \"slug\":\"parks\", \"name\":\"Parks\"}]")
                        .withHeader("Content-Type", "application/json")));
    }

    private static void setDefaultLocationStub() {
        WireMock.stubFor(get(urlEqualTo("/locations/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("[{\"slug\":\"ekb\",\"name\":\"Yekaterinburg\"},{\"slug\":\"krd\",\"name\":\"Krasnodar\"}]")
                        .withHeader("Content-Type", "application/json")));
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("kudago.api.base-url", () -> String.format("http://%s:%d",
                wireMock.getHost(), wireMock.getFirstMappedPort()));
    }

    @BeforeAll
    static void beforeAll() {
        WireMock.configureFor(wireMock.getHost(), wireMock.getFirstMappedPort());
        setDefaultCategoryStub();
        setDefaultLocationStub();
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    private static void setInvalidLocationStub() {
        WireMock.stubFor(get(urlEqualTo("/locations/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("[{\"slug\":\"ekb\",\"name\":\"Yekaterinburg\"}, {invalid json here}]")
                        .withHeader("Content-Type", "application/json")));
    }

    @Test
    void shouldGetLocations() {
        WireMock.reset();

        setInvalidLocationStub();

        Assertions.assertThrows(IllegalStateException.class, () -> {
            categoryApiClient.fetchCategories();
        });
    }

    private static void setInvalidCategoryStub() {
        WireMock.stubFor(get(urlEqualTo("/place-categories/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("[{\"id\": 1, \"slug\":\"airports\", \"name\":\"Airports\"}, {invalid json here}]")
                        .withHeader("Content-Type", "application/json")));
    }

    @Test
    void shouldGetCategories() {
        WireMock.reset();

        setInvalidCategoryStub();

        Assertions.assertThrows(IllegalStateException.class, () -> {
            locationApiClient.fetchLocations();
        });
    }
}