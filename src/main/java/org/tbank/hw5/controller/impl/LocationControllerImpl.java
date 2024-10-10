package org.tbank.hw5.controller.impl;

import org.example.annotation.LogExecutionTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.tbank.hw5.controller.LocationController;
import org.tbank.hw5.dto.ResponseDto;
import org.tbank.hw5.dto.LocationDto;
import org.tbank.hw5.mapper.LocationMapper;
import org.tbank.hw5.model.Location;
import org.tbank.hw5.service.LocationService;

import java.util.List;

@RestController
@LogExecutionTime
public class LocationControllerImpl implements LocationController {

    private final LocationService locationService;
    private final LocationMapper locationMapper;

    public LocationControllerImpl(LocationService locationService, LocationMapper locationMapper) {
        this.locationService = locationService;
        this.locationMapper = locationMapper;
    }

    @Override
    public ResponseEntity<ResponseDto<List<LocationDto>>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        List<LocationDto> locationDtoList = locationMapper.toLocationDtoList(locations);
        return ResponseEntity.ok(new ResponseDto<>(locationDtoList));
    }

    @Override
    public ResponseEntity<ResponseDto<LocationDto>> getLocationById(String id) {
        Location location = locationService.getLocationById(id);
        LocationDto locationDto = locationMapper.toLocationDto(location);
        return ResponseEntity.ok(new ResponseDto<>(locationDto));
    }

    @Override
    public ResponseEntity<ResponseDto<LocationDto>> createLocation(LocationDto locationDto) {
        Location location = locationMapper.toLocation(locationDto);
        Location createdLocation = locationService.createLocation(location);
        LocationDto createdLocationDto = locationMapper.toLocationDto(createdLocation);
        return ResponseEntity.ok(new ResponseDto<>(createdLocationDto));
    }

    @Override
    public ResponseEntity<ResponseDto<LocationDto>> updateLocation(String id, LocationDto locationDto) {
        Location location = locationMapper.toLocation(locationDto);
        Location updatedLocation = locationService.updateLocation(id, location);
        LocationDto updatedLocationDto = locationMapper.toLocationDto(updatedLocation);
        return ResponseEntity.ok(new ResponseDto<>(updatedLocationDto));
    }

    @Override
    public ResponseEntity<Void> deleteLocation(String id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
