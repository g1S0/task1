package org.tbank.hw10.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tbank.hw10.dto.EventDto;
import org.tbank.hw10.entity.Event;
import org.tbank.hw10.entity.Place;
import org.tbank.hw10.mapper.EventMapper;
import org.tbank.hw10.repository.EventRepository;
import org.tbank.hw10.repository.PlaceRepository;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final PlaceRepository placeRepository;
    private final EventMapper eventMapper;

    @Autowired
    public EventService(EventRepository eventRepository, PlaceRepository placeRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.placeRepository = placeRepository;
        this.eventMapper = eventMapper;
    }

    public EventDto createEvent(EventDto eventDto) {
        log.info("Creating event: {}", eventDto);
        Place place = placeRepository.findById(eventDto.getPlaceId())
                .orElseThrow(() -> new EntityNotFoundException("Place not found with id: " + eventDto.getPlaceId()));

        Event event = eventMapper.dtoToEvent(eventDto);
        event.setPlace(place);
        EventDto createdEventDto = eventMapper.eventToDto(eventRepository.save(event));
        log.info("Event created successfully: {}", createdEventDto);
        return createdEventDto;
    }

    public List<EventDto> getAllEvents() {
        log.info("Fetching all events");
        List<Event> events = eventRepository.findAll();
        return eventMapper.eventsToDtos(events);
    }

    public EventDto getEventById(Long id) {
        log.info("Fetching event with id: {}", id);
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));
        return eventMapper.eventToDto(event);
    }

    public EventDto updateEvent(Long id, EventDto eventDto) {
        log.info("Updating event with id: {}", id);
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));

        Place place = placeRepository.findById(eventDto.getPlaceId())
                .orElseThrow(() -> new EntityNotFoundException("Place not found with id: " + eventDto.getPlaceId()));

        eventMapper.updateEvent(existingEvent, eventDto, eventRepository);
        existingEvent.setPlace(place);
        EventDto updatedEventDto = eventMapper.eventToDto(eventRepository.save(existingEvent));
        log.info("Event updated successfully: {}", updatedEventDto);
        return updatedEventDto;
    }

    public void deleteEvent(Long id) {
        log.info("Deleting event with id: {}", id);
        if (!eventRepository.existsById(id)) {
            throw new EntityNotFoundException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
        log.info("Event deleted successfully with id: {}", id);
    }
}