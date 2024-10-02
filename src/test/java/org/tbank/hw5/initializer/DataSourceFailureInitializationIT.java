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

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DataSourceFailureInitializationIT {
    @Container
    static WireMockContainer wireMock = WireMockManager.createContainer();

    @LocalServerPort
    private Integer port;

    @Autowired
    private CategoryApiClient categoryApiClient;

    @Autowired
    private LocationApiClient locationApiClient;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        WireMockManager.setPropertiesPath(registry, wireMock);
    }

    @BeforeAll
    static void beforeAll() {
        WireMock.configureFor(wireMock.getHost(), wireMock.getFirstMappedPort());
        WireMockStubManager.setDefaultCategoryStub();
        WireMockStubManager.setDefaultLocationStub();
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        WireMock.reset();
    }

    @Test
    void shouldThrowExceptionOnInvalidLocationData() {
        WireMockStubManager.setInvalidLocationStub();

        Assertions.assertThrows(IllegalStateException.class, () -> {
            categoryApiClient.fetchCategories();
        });
    }

    @Test
    void shouldThrowExceptionOnInvalidCategoryData() {
        WireMockStubManager.setInvalidCategoryStub();

        Assertions.assertThrows(IllegalStateException.class, () -> {
            locationApiClient.fetchLocations();
        });
    }
}