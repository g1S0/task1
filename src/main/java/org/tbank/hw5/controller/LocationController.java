package org.tbank.hw5.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tbank.hw5.dto.LocationDto;
import org.tbank.hw5.dto.ResponseDto;

import java.util.List;

@RequestMapping("/api/v1/locations")
public interface LocationController {

    @GetMapping
    ResponseEntity<ResponseDto<List<LocationDto>>> getAllLocations();

    @GetMapping("/{id}")
    ResponseEntity<ResponseDto<LocationDto>> getLocationById(@PathVariable String id);

    @PostMapping
    ResponseEntity<ResponseDto<LocationDto>> createLocation(@RequestBody LocationDto locationDto);

    @PutMapping("/{id}")
    ResponseEntity<ResponseDto<LocationDto>> updateLocation(@PathVariable String id, @RequestBody LocationDto locationDto);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteLocation(@PathVariable String id);
}
