package org.tbank.hw10.mapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tbank.hw10.entity.Place;
import org.tbank.hw10.repository.PlaceRepository;

@Component
public class PlaceMapperHelper {

    private final PlaceRepository placeRepository;

    @Autowired
    public PlaceMapperHelper(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public Place fromId(Long placeId) {
        return placeId != null ? placeRepository.findById(placeId)
                .orElseThrow(() -> new EntityNotFoundException("Place not found with id: " + placeId)) : null;
    }
}
