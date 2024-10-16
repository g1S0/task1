package org.tbank.hw5.controller;

import org.example.annotation.LogExecutionTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tbank.hw5.dto.LocationDto;
import org.tbank.hw5.dto.ResponseDto;
import org.tbank.hw5.mapper.LocationMapper;
import org.tbank.hw5.model.Location;
import org.tbank.hw5.service.LocationService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
@LogExecutionTime
public class LocationController {

    private final LocationService locationService;
    private final LocationMapper locationMapper;

    public LocationController(LocationService locationService, LocationMapper locationMapper) {
        this.locationService = locationService;
        this.locationMapper = locationMapper;
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<LocationDto>>> getAllLocations() {
        List<Location> locations = locationService.getAllLocations();
        List<LocationDto> locationDtoList = locationMapper.toLocationDtoList(locations);
        return ResponseEntity.ok(new ResponseDto<>(locationDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<LocationDto>> getLocationById(@PathVariable String id) {
        Location location = locationService.getLocationById(id);
        LocationDto locationDto = locationMapper.toLocationDto(location);
        return ResponseEntity.ok(new ResponseDto<>(locationDto));
    }

    @PostMapping
    public ResponseEntity<ResponseDto<LocationDto>> createLocation(@RequestBody LocationDto locationDto) {
        Location location = locationMapper.toLocation(locationDto);
        Location createdLocation = locationService.createLocation(location);
        LocationDto createdLocationDto = locationMapper.toLocationDto(createdLocation);
        return ResponseEntity.ok(new ResponseDto<>(createdLocationDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<LocationDto>> updateLocation(@PathVariable String id, @RequestBody LocationDto locationDto) {
        Location location = locationMapper.toLocation(locationDto);
        Location updatedLocation = locationService.updateLocation(id, location);
        LocationDto updatedLocationDto = locationMapper.toLocationDto(updatedLocation);
        return ResponseEntity.ok(new ResponseDto<>(updatedLocationDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable String id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
