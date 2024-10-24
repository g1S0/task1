package org.tbank.hw10.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tbank.hw10.dto.PlaceDto;
import org.tbank.hw10.dto.ResponseDto;
import org.tbank.hw10.service.PlaceService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/places")
@AllArgsConstructor
@Validated
public class PlaceController {

    private final PlaceService placeService;

    @PostMapping
    public ResponseEntity<ResponseDto<PlaceDto>> createPlace(@Valid @RequestBody PlaceDto placeDto) {
        PlaceDto createdPlace = placeService.createPlace(placeDto);
        return new ResponseEntity<>(new ResponseDto<>(createdPlace), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<PlaceDto>>> getAllPlaces() {
        List<PlaceDto> places = placeService.getAllPlaces();
        return new ResponseEntity<>(new ResponseDto<>(places), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<PlaceDto>> getPlaceById(@PathVariable Long id) {
        PlaceDto placeDto = placeService.getPlaceById(id);
        return new ResponseEntity<>(new ResponseDto<>(placeDto), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<PlaceDto>> updatePlace(@PathVariable Long id, @Valid @RequestBody PlaceDto placeDto) {
        PlaceDto updatedPlace = placeService.updatePlace(id, placeDto);
        return new ResponseEntity<>(new ResponseDto<>(updatedPlace), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> deletePlace(@PathVariable Long id) {
        placeService.deletePlace(id);
        return new ResponseEntity<>(new ResponseDto<>(null), HttpStatus.NO_CONTENT);
    }
}