package org.tbank.hw5.controller.impl;

import org.tbank.hw5.dto.ResponseDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.tbank.hw5.dto.LocationDto;
import org.tbank.hw5.mapper.LocationMapper;
import org.tbank.hw5.model.Location;
import org.tbank.hw5.service.LocationService;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocationControllerImplTest {

    @Mock
    private LocationService locationService;

    @Mock
    private LocationMapper locationMapper;

    @InjectMocks
    private LocationControllerImpl locationController;

    private Location location;
    private LocationDto locationDto;

    @BeforeEach
    void setUp() {
        location = new Location("msk", "Москва");
        locationDto = new LocationDto("msk", "Москва");
    }

    @Test
    public void testGetAllLocations() {
        when(locationService.getAllLocations()).thenReturn(List.of(location));
        when(locationMapper.toLocationDtoList(anyList())).thenReturn(List.of(locationDto));

        ResponseEntity<ResponseDto<List<LocationDto>>> response = locationController.getAllLocations();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, Objects.requireNonNull(response.getBody()).getData().size());
        assertEquals("Москва", response.getBody().getData().get(0).getName());

        verify(locationService, times(1)).getAllLocations();
        verify(locationMapper, times(1)).toLocationDtoList(anyList());
    }

    @Test
    public void testGetLocationById() {
        when(locationService.getLocationById("msk")).thenReturn(location);
        when(locationMapper.toLocationDto(any(Location.class))).thenReturn(locationDto);

        ResponseEntity<ResponseDto<LocationDto>> response = locationController.getLocationById("msk");

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Москва", Objects.requireNonNull(response.getBody()).getData().getName());

        verify(locationService, times(1)).getLocationById("msk");
        verify(locationMapper, times(1)).toLocationDto(any(Location.class));
    }

    @Test
    public void testCreateLocation() {
        when(locationMapper.toLocation(any(LocationDto.class))).thenReturn(location);
        when(locationService.createLocation(any(Location.class))).thenReturn(location);
        when(locationMapper.toLocationDto(any(Location.class))).thenReturn(locationDto);

        ResponseEntity<ResponseDto<LocationDto>> response = locationController.createLocation(locationDto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Москва", Objects.requireNonNull(response.getBody()).getData().getName());

        verify(locationMapper, times(1)).toLocation(any(LocationDto.class));
        verify(locationService, times(1)).createLocation(any(Location.class));
        verify(locationMapper, times(1)).toLocationDto(any(Location.class));
    }

    @Test
    public void testUpdateLocation() {
        when(locationMapper.toLocation(any(LocationDto.class))).thenReturn(location);
        when(locationService.updateLocation(eq("msk"), any(Location.class))).thenReturn(location);
        when(locationMapper.toLocationDto(any(Location.class))).thenReturn(locationDto);

        ResponseEntity<ResponseDto<LocationDto>> response = locationController.updateLocation("msk", locationDto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Москва", Objects.requireNonNull(response.getBody()).getData().getName());

        verify(locationMapper, times(1)).toLocation(any(LocationDto.class));
        verify(locationService, times(1)).updateLocation(eq("msk"), any(Location.class));
        verify(locationMapper, times(1)).toLocationDto(any(Location.class));
    }

    @Test
    public void testDeleteLocation() {
        ResponseEntity<Void> response = locationController.deleteLocation("msk");

        assertEquals(204, response.getStatusCode().value());

        verify(locationService, times(1)).deleteLocation("msk");
    }
}