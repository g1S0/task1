package org.tbank.hw10.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.tbank.hw10.dto.PlaceDto;
import org.tbank.hw10.entity.Event;
import org.tbank.hw10.entity.Place;

import java.util.List;

@Mapper(componentModel = "spring", uses = EventMapper.class)
public interface PlaceMapper {
    PlaceDto toDto(Place place);

    Place toEntity(PlaceDto placeDto);

    List<PlaceDto> toDtoList(List<Place> places);

    default void updatePlace(@MappingTarget Place place, PlaceDto placeDto, List<Event> events) {
        place.setName(placeDto.getName());
        place.setSlug(placeDto.getSlug());
        place.setEvents(events);
    }
}
