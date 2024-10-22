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

    String placeDtoJson = """
            {
                "slug": "slug-1",
                "name": "Place 1"
            }
            """;

    String updatePlaceJson = """
            {
                "slug": "slug-updated",
                "name": "Place Updated"
            }
            """;

    @Test
    void testCreatePlace() throws Exception {
        mockMvc.perform(post("/api/v1/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(placeDtoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.slug").value("slug-1"))
                .andExpect(jsonPath("$.data.name").value("Place 1"));

        List<Place> allPlaces = placeRepository.findAll();
        Place firstPlace = allPlaces.stream().findFirst().orElse(null);

        assert firstPlace != null;

        Assertions.assertEquals(firstPlace.getSlug(), "slug-1");
        Assertions.assertEquals(firstPlace.getName(), "Place 1");
    }

    @Test
    void testUpdatePlace() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/v1/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(placeDtoJson))
                .andExpect(status().isCreated())
                .andReturn();

        PlaceDto createdPlace = objectMapper.readValue(createResult.getResponse().getContentAsString(),
                new TypeReference<ResponseDto<PlaceDto>>() {
                }).getData();

        mockMvc.perform(put("/api/v1/places/" + createdPlace.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePlaceJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.slug").value("slug-updated"))
                .andExpect(jsonPath("$.data.name").value("Place Updated"));

        List<Place> allPlaces = placeRepository.findAll();
        Place firstPlace = allPlaces.stream()
                .filter(place -> place.getId().equals(createdPlace.getId()))
                .findFirst()
                .orElse(null);

        assert firstPlace != null;
        Assertions.assertEquals("slug-updated", firstPlace.getSlug());
        Assertions.assertEquals("Place Updated", firstPlace.getName());
    }

    @Test
    void testGetPlaceById() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(placeDtoJson))
                .andExpect(status().isCreated())
                .andReturn();

        Long createdPlaceId = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<ResponseDto<PlaceDto>>() {
                }).getData().getId();

        mockMvc.perform(get("/api/v1/places/" + createdPlaceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.slug").value("slug-1"))
                .andExpect(jsonPath("$.data.name").value("Place 1"));
    }

    @Test
    void testDeletePlace() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(placeDtoJson))
                .andExpect(status().isCreated())
                .andReturn();

        Long createdPlaceId = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<ResponseDto<PlaceDto>>() {
                }).getData().getId();

        mockMvc.perform(delete("/api/v1/places/" + createdPlaceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/places/" + createdPlaceId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCascadeDeletePlace() throws Exception {
        Place place = new Place(1L, "slug-1", "Place 1", null);
        Place savedPlace = placeRepository.save(place);

        String eventDtoJson = String.format("""
                    {
                        "name": "Event 1",
                        "date": "2024-01-01",
                        "placeId": %d
                    }
                """, savedPlace.getId());

        mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventDtoJson))
                .andExpect(status().isCreated());

        placeRepository.deleteById(savedPlace.getId());

        List<Event> allEvents = eventRepository.findAll();
        Assertions.assertTrue(allEvents.isEmpty());
    }
}