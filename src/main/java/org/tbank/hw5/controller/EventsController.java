package org.tbank.hw5.controller;

import org.example.annotation.LogExecutionTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tbank.hw5.dto.EventDto;
import org.tbank.hw5.dto.EventsRequestDto;
import org.tbank.hw5.service.EventsService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@LogExecutionTime
public class EventsController {

    private final EventsService eventsService;

    public EventsController(EventsService eventsService) {
        this.eventsService = eventsService;
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getAllEvents(@RequestBody EventsRequestDto eventsRequestDto) {
        return ResponseEntity.ok(eventsService.getEvents(eventsRequestDto));
    }

    @GetMapping("/projectReactor")
    public ResponseEntity<List<EventDto>> getAllEventsWithProjectReactor(@RequestBody EventsRequestDto eventsRequestDto) {
        return ResponseEntity.ok(eventsService.getEventsWithProjectReactor(eventsRequestDto));
    }

}