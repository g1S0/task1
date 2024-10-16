package org.tbank.hw5.initializer;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.tbank.hw5.client.CategoryApiClient;
import org.tbank.hw5.client.LocationApiClient;
import org.tbank.hw5.dto.CategoryDto;
import org.tbank.hw5.dto.LocationDto;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.util.List;

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
    void shouldReturnEmptyListOnInvalidLocationData() {
        WireMockStubManager.setInvalidLocationStub();

        List<LocationDto> locations = locationApiClient.fetchLocations();
        Assertions.assertTrue(locations.isEmpty());
    }

    @Test
    void shouldReturnEmptyListOnInvalidCategoryData() {
        WireMockStubManager.setInvalidCategoryStub();

        List<CategoryDto> categories = categoryApiClient.fetchCategories();
        Assertions.assertTrue(categories.isEmpty());
    }
}