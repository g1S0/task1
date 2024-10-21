package org.tbank.hw10.mapper;

import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.*;
import org.tbank.hw10.dto.EventDto;
import org.tbank.hw10.entity.Event;
import org.tbank.hw10.repository.EventRepository;

import java.util.List;

@Mapper(componentModel = "spring", uses = PlaceMapperHelper.class)
public interface EventMapper {

    List<EventDto> eventsToDtos(List<Event> events);

    @Mapping(source = "place.id", target = "placeId")
    EventDto eventToDto(Event event);

    @Mapping(target = "place", source = "placeId")
    Event dtoToEvent(EventDto eventDto);

    default void updateEvent(@MappingTarget Event event, EventDto eventDto, EventRepository eventRepository) {
        event.setName(eventDto.getName());
        event.setDate(eventDto.getDate());

        Event place = eventRepository.findById(event.getId())
                .orElseThrow(() -> new EntityNotFoundException("Place not found with id: " + event.getId()));
        event.setPlace(place.getPlace());
    }
}
