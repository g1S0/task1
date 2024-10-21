package org.tbank.hw10.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tbank.hw10.dto.PlaceDto;
import org.tbank.hw10.entity.Place;
import org.tbank.hw10.mapper.PlaceMapper;
import org.tbank.hw10.repository.PlaceRepository;
import org.tbank.hw5.exception.EntityNotFoundException;

import java.util.List;

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
        Place place = placeMapper.toEntity(placeDto);
        return placeMapper.toDto(placeRepository.save(place));
    }

    public List<PlaceDto> getAllPlaces() {
        List<Place> places = placeRepository.findAll();
        return placeMapper.toDtoList(places);
    }

    public PlaceDto getPlaceById(Long id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Place not found with id: " + id));
        return placeMapper.toDto(place);
    }

    public PlaceDto updatePlace(Long id, PlaceDto placeDto) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Place not found with id: " + id));

        placeMapper.updatePlace(place, placeDto, placeRepository);
        return placeMapper.toDto(placeRepository.save(place));
    }

    public void deletePlace(Long id) {
        if (!placeRepository.existsById(id)) {
            throw new EntityNotFoundException("Place not found with id: " + id);
        }
        placeRepository.deleteById(id);
    }
}
