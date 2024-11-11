package org.tbank.hw10.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.tbank.hw10.dto.EventDto;
import org.tbank.hw10.entity.Event;
import org.tbank.hw10.entity.Place;

import java.util.List;

@Mapper(componentModel = "spring", uses = PlaceMapperHelper.class)
public interface EventMapper {

    List<EventDto> eventsToDtos(List<Event> events);

    @Mapping(source = "place.id", target = "placeId")
    EventDto eventToDto(Event event);

    @Mapping(target = "place", source = "placeId")
    Event dtoToEvent(EventDto eventDto);

    default void updateEvent(@MappingTarget Event event, EventDto eventDto, Place place) {
        event.setName(eventDto.getName());
        event.setDate(eventDto.getDate());
        event.setPlace(place);
    }
}
