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
import org.tbank.hw5.model.Category;
import org.tbank.hw5.model.Location;
import org.tbank.hw5.storage.impl.CategoryStorage;
import org.tbank.hw5.storage.impl.LocationStorage;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.util.List;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DataSourceSuccessInitializationIT {
    @Autowired
    private LocationStorage locationStorage;

    @Autowired
    private CategoryStorage categoryStorage;

    @Container
    static WireMockContainer wireMock = WireMockManager.createContainer();

    @LocalServerPort
    private Integer port;

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
    }

    @Test
    void shouldGetLocations() {
        List<Location> locations = locationStorage.findAll();

        List<Location> expectedLocations = List.of(
                new Location("ekb", "Yekaterinburg"),
                new Location("krd", "Krasnodar")
        );

        Assertions.assertEquals(expectedLocations.size(), locations.size());
        Assertions.assertEquals(expectedLocations.get(0), locationStorage.findById(expectedLocations.get(0).getSlug()));
        Assertions.assertEquals(expectedLocations.get(1), locationStorage.findById(expectedLocations.get(1).getSlug()));
    }

    @Test
    void shouldGetCategories() {
        List<Category> categories = categoryStorage.findAll();

        List<Category> expectedCategories = List.of(
                new Category(1L, "airports", "Airports"),
                new Category(2L, "parks", "Parks")
        );

        Assertions.assertEquals(expectedCategories.size(), categories.size());
        Assertions.assertEquals(expectedCategories.get(0), categoryStorage.findById(expectedCategories.get(0).getId()));
        Assertions.assertEquals(expectedCategories.get(1), categoryStorage.findById(expectedCategories.get(1).getId()));
    }
}
