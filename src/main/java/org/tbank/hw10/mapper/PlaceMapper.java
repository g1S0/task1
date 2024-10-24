package org.tbank.hw10.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.tbank.hw10.dto.PlaceDto;
import org.tbank.hw10.entity.Place;
import org.tbank.hw10.repository.PlaceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", uses = EventMapper.class)
public interface PlaceMapper {
    PlaceDto toDto(Place place);

    Place toEntity(PlaceDto placeDto);

    List<PlaceDto> toDtoList(List<Place> places);

    default void updatePlace(@MappingTarget Place place, PlaceDto placeDto, PlaceRepository placeRepository) {
        place.setName(placeDto.getName());
        place.setSlug(placeDto.getSlug());

        Optional<Place> existingEvents = placeRepository.findById(place.getId());

        if (existingEvents.isPresent()) {
            place.setEvents(existingEvents.get().getEvents());
        } else {
            place.setEvents(new ArrayList<>());
        }
    }
}
