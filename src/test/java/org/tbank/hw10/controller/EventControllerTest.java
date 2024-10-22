package org.tbank.hw10.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.tbank.hw10.dto.EventDto;
import org.tbank.hw10.dto.ResponseDto;
import org.tbank.hw10.entity.Event;
import org.tbank.hw10.entity.Place;
import org.tbank.hw10.repository.EventRepository;
import org.tbank.hw10.repository.PlaceRepository;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
public class EventControllerTest {

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

    @BeforeAll
    static void setup(@Autowired PlaceRepository placeRepository) {
        Place place = new Place(null, "slug-1", "Place 1", null);
        placeRepository.save(place);
    }

    String eventDtoJson = """
            {
                "name": "Event 1",
                "date": "2024-01-01",
                "placeId": 1
            }
            """;

    @Test
    void testCreateEvent() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventDtoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Event 1"))
                .andExpect(jsonPath("$.data.date").value("2024-01-01"))
                .andReturn();

        EventDto createdEvent = objectMapper.readValue(createResult.getResponse().getContentAsString(), new TypeReference<ResponseDto<EventDto>>() {
        }).getData();

        Optional<Event> optionalEvent = eventRepository.findById(createdEvent.getId());

        assert optionalEvent.isPresent();

        Event firstEvent = optionalEvent.get();
        Assertions.assertEquals("Event 1", firstEvent.getName());
        Assertions.assertEquals(LocalDate.parse("2024-01-01"), firstEvent.getDate());
    }

    @Test
    void testGetEventById() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventDtoJson))
                .andExpect(status().isCreated())
                .andReturn();

        EventDto createdEvent = objectMapper.readValue(createResult.getResponse().getContentAsString(), new TypeReference<ResponseDto<EventDto>>() {
        }).getData();

        mockMvc.perform(get("/api/v1/events/" + createdEvent.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Event 1"))
                .andExpect(jsonPath("$.data.date").value("2024-01-01"));
    }

    @Test
    void testUpdateEvent() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventDtoJson))
                .andExpect(status().isCreated())
                .andReturn();

        EventDto createdEvent = objectMapper.readValue(createResult.getResponse().getContentAsString(), new TypeReference<ResponseDto<EventDto>>() {
        }).getData();

        String updatedEventDtoJson = """
                {
                    "name": "Updated Event",
                    "date": "2024-10-22",
                    "placeId": 1
                }
                """;

        mockMvc.perform(put("/api/v1/events/" + createdEvent.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEventDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Updated Event"))
                .andExpect(jsonPath("$.data.date").value("2024-10-22"));
    }

    @Test
    void testDeleteEvent() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventDtoJson))
                .andExpect(status().isCreated())
                .andReturn();

        EventDto createdEvent = objectMapper.readValue(createResult.getResponse().getContentAsString(), new TypeReference<ResponseDto<EventDto>>() {
        }).getData();

        mockMvc.perform(delete("/api/v1/events/" + createdEvent.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/events/" + createdEvent.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateEventWithNonExistentPlaceId() throws Exception {
        String eventDtoJson = """
        {
            "name": "Event with Non-Existent Place",
            "date": "2024-01-01",
            "placeId": 999 
        }
        """;

        mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventDtoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Place not found with id: 999"));
    }
}