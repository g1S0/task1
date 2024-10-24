package org.tbank.hw10.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tbank.hw10.dto.PlaceDto;
import org.tbank.hw10.entity.Place;
import org.tbank.hw10.mapper.PlaceMapper;
import org.tbank.hw10.repository.PlaceRepository;
import org.tbank.hw10.exception.EntityNotFoundException;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;

    @Autowired
    public PlaceService(PlaceRepository placeRepository, PlaceMapper placeMapper) {
        this.placeRepository = placeRepository;
        this.placeMapper = placeMapper;
    }

    public PlaceDto createPlace(PlaceDto placeDto) {
        log.info("Creating place: {}", placeDto);
        Place place = placeMapper.toEntity(placeDto);
        PlaceDto createdPlaceDto = placeMapper.toDto(placeRepository.save(place));
        log.info("Place created successfully: {}", createdPlaceDto);
        return createdPlaceDto;
    }

    public List<PlaceDto> getAllPlaces() {
        log.info("Fetching all places");
        List<Place> places = placeRepository.findAll();
        return placeMapper.toDtoList(places);
    }

    public PlaceDto getPlaceById(Long id) {
        log.info("Fetching place with id: {}", id);
        Place place = placeRepository.findByIdWithEvents(id)
                .orElseThrow(() -> new EntityNotFoundException("Place not found with id: " + id));
        return placeMapper.toDto(place);
    }

    public PlaceDto updatePlace(Long id, PlaceDto placeDto) {
        log.info("Updating place with id: {}", id);
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Place not found with id: " + id));

        placeMapper.updatePlace(place, placeDto, placeRepository);
        PlaceDto updatedPlaceDto = placeMapper.toDto(placeRepository.save(place));
        log.info("Place updated successfully: {}", updatedPlaceDto);
        return updatedPlaceDto;
    }

    public void deletePlace(Long id) {
        log.info("Deleting place with id: {}", id);
        if (!placeRepository.existsById(id)) {
            throw new EntityNotFoundException("Place not found with id: " + id);
        }
        placeRepository.deleteById(id);
        log.info("Place deleted successfully with id: {}", id);
    }
}
