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
import org.testcontainers.utility.MountableFile;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DataSourceSuccessInitializationIT {
    @Autowired
    private LocationStorage locationStorage;

    @Autowired
    private CategoryStorage categoryStorage;

    @Container
    static WireMockContainer wireMock = new WireMockContainer("wiremock/wiremock:3.6.0")
            .withCopyFileToContainer(
                    MountableFile.forHostPath("src/test/resources/wiremock/json"),
                    "/home/wiremock/./__files");

    @LocalServerPort
    private Integer port;

    private static void setDefaultCategoryStub() {
        WireMock.stubFor(get(urlEqualTo("/place-categories/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("valid_category.json")
                        .withHeader("Content-Type", "application/json")));
    }

    private static void setDefaultLocationStub() {
        WireMock.stubFor(get(urlEqualTo("/locations/"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("valid_location.json")
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
