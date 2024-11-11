package org.tbank.hw10.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.tbank.hw10.dto.PlaceDto;
import org.tbank.hw10.dto.ResponseDto;
import org.tbank.hw10.entity.Event;
import org.tbank.hw10.entity.Place;
import org.tbank.hw10.repository.EventRepository;
import org.tbank.hw10.repository.PlaceRepository;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
public class PlaceControllerIT {

    @Container
    static PostgreSQLContainer<?> postgresDB = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("admin")
            .withPassword("admin");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresDB::getJdbcUrl);
        registry.add("spring.datasource.username", postgresDB::getUsername);
        registry.add("spring.datasource.password", postgresDB::getPassword);
    }

    @BeforeEach
    void clearDatabase() {
        placeRepository.deleteAll();
        eventRepository.deleteAll();
    }

    private final String PLACE_SLUG = "slug-1";
    private final String PLACE_NAME = "Place 1";

    private String createPlaceJson(String slug, String name) {
        return String.format("""
                {
                    "slug": "%s",
                    "name": "%s"
                }
                """, slug, name);
    }

    private Long createPlaceAndGetId(String slug, String name) throws Exception {
        String placeJson = createPlaceJson(slug, name);
        MvcResult result = mockMvc.perform(post("/api/v1/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(placeJson))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<ResponseDto<PlaceDto>>() {
                }).getData().getId();
    }

    @Test
    void testCreatePlace() throws Exception {
        Long createdPlaceId = createPlaceAndGetId(PLACE_SLUG, PLACE_NAME);

        Place firstPlace = placeRepository.findById(createdPlaceId).orElse(null);
        assert firstPlace != null;

        Assertions.assertEquals(PLACE_SLUG, firstPlace.getSlug());
        Assertions.assertEquals(PLACE_NAME, firstPlace.getName());
    }

    @Test
    void testUpdatePlace() throws Exception {
        Long createdPlaceId = createPlaceAndGetId(PLACE_SLUG, PLACE_NAME);

        String UPDATED_PLACE_SLUG = "slug-updated";
        String UPDATED_PLACE_NAME = "Place Updated";
        String updatePlaceJson = createPlaceJson(UPDATED_PLACE_SLUG, UPDATED_PLACE_NAME);

        mockMvc.perform(put("/api/v1/places/" + createdPlaceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePlaceJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.slug").value(UPDATED_PLACE_SLUG))
                .andExpect(jsonPath("$.data.name").value(UPDATED_PLACE_NAME));

        Place updatedPlace = placeRepository.findById(createdPlaceId).orElse(null);
        assert updatedPlace != null;
        Assertions.assertEquals(UPDATED_PLACE_SLUG, updatedPlace.getSlug());
        Assertions.assertEquals(UPDATED_PLACE_NAME, updatedPlace.getName());
    }

    @Test
    void testGetPlaceById() throws Exception {
        Long createdPlaceId = createPlaceAndGetId(PLACE_SLUG, PLACE_NAME);

        mockMvc.perform(get("/api/v1/places/" + createdPlaceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.slug").value(PLACE_SLUG))
                .andExpect(jsonPath("$.data.name").value(PLACE_NAME));
    }

    @Test
    void testDeletePlace() throws Exception {
        Long createdPlaceId = createPlaceAndGetId(PLACE_SLUG, PLACE_NAME);

        mockMvc.perform(delete("/api/v1/places/" + createdPlaceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/places/" + createdPlaceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCascadeDeletePlace() throws Exception {
        Place savedPlace = placeRepository.save(new Place(1L, PLACE_SLUG, PLACE_NAME, null));

        String EVENT_NAME = "Event 1";
        String EVENT_DATE = "2024-01-01";
        String eventDtoJson = String.format("""
                    {
                        "name": "%s",
                        "date": "%s",
                        "placeId": %d
                    }
                """, EVENT_NAME, EVENT_DATE, savedPlace.getId());

        mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventDtoJson))
                .andExpect(status().isCreated());

        placeRepository.deleteById(savedPlace.getId());

        List<Event> allEvents = eventRepository.findAll();
        Assertions.assertTrue(allEvents.isEmpty());
    }
}