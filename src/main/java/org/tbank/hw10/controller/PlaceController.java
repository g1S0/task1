package org.tbank.hw10.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tbank.hw10.dto.PlaceDto;
import org.tbank.hw10.service.PlaceService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/places")
@AllArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    @PostMapping
    public ResponseEntity<PlaceDto> createPlace(@RequestBody PlaceDto placeDto) {
        PlaceDto createdPlace = placeService.createPlace(placeDto);
        return new ResponseEntity<>(createdPlace, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PlaceDto>> getAllPlaces() {
        List<PlaceDto> places = placeService.getAllPlaces();
        return new ResponseEntity<>(places, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceDto> getPlaceById(@PathVariable Long id) {
        PlaceDto placeDto = placeService.getPlaceById(id);
        return new ResponseEntity<>(placeDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaceDto> updatePlace(@PathVariable Long id, @RequestBody PlaceDto placeDto) {
        PlaceDto updatedPlace = placeService.updatePlace(id, placeDto);
        return new ResponseEntity<>(updatedPlace, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable Long id) {
        placeService.deletePlace(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
