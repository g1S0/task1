package org.tbank.hw10.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tbank.hw10.dto.EventDto;
import org.tbank.hw10.dto.ResponseDto;
import org.tbank.hw10.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
@AllArgsConstructor
@Validated
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<ResponseDto<EventDto>> createEvent(@Valid @RequestBody EventDto eventDto) {
        EventDto createdEvent = eventService.createEvent(eventDto);
        return new ResponseEntity<>(new ResponseDto<>(createdEvent), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<EventDto>>> getAllEvents() {
        List<EventDto> events = eventService.getAllEvents();
        return new ResponseEntity<>(new ResponseDto<>(events), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<EventDto>> getEventById(@PathVariable Long id) {
        EventDto eventDto = eventService.getEventById(id);
        return new ResponseEntity<>(new ResponseDto<>(eventDto), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<EventDto>> updateEvent(@PathVariable Long id, @Valid @RequestBody EventDto eventDto) {
        EventDto updatedEvent = eventService.updateEvent(id, eventDto);
        return new ResponseEntity<>(new ResponseDto<>(updatedEvent), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return new ResponseEntity<>(new ResponseDto<>(null), HttpStatus.NO_CONTENT);
    }
}
