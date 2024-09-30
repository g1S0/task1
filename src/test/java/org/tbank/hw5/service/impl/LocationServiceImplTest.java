package org.tbank.hw5.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tbank.hw5.exception.EntityAlreadyExistsException;
import org.tbank.hw5.exception.EntityNotFoundException;
import org.tbank.hw5.model.Location;
import org.tbank.hw5.storage.impl.LocationStorage;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LocationServiceImplTest {

    @Mock
    private LocationStorage locationStorage;

    @InjectMocks
    private LocationServiceImpl locationService;

    private Location getLocation() {
        return new Location("msk", "Москва");
    }

    @Test
    public void testGetAllLocations_PositiveScenario() {
        List<Location> mockLocations = List.of(
                new Location("msk", "Москва"),
                new Location("spb", "Санкт-Петербург")
        );

        when(locationStorage.findAll()).thenReturn(mockLocations);

        List<Location> locations = locationService.getAllLocations();

        assertEquals(2, locations.size());
        assertEquals("Москва", locations.get(0).getName());
    }

    @Test
    public void testGetAllLocations_EmptyResult() {
        when(locationStorage.findAll()).thenReturn(Collections.emptyList());

        List<Location> locations = locationService.getAllLocations();

        assertTrue(locations.isEmpty());
    }

    @Test
    public void testGetLocationBySlug_PositiveScenario() {
        when(locationStorage.findById("msk")).thenReturn(getLocation());

        Location location = locationService.getLocationById("msk");

        assertNotNull(location);
        assertEquals("Москва", location.getName());
    }

    @Test
    public void testGetLocationBySlug_NegativeScenario() {
        when(locationStorage.findById("msk")).thenReturn(null);

        Location location = locationService.getLocationById("msk");

        assertNull(location);
    }

    @Test
    public void testCreateLocation_PositiveScenario() throws EntityAlreadyExistsException {
        Location newLocation = getLocation();

        when(locationStorage.save("msk", newLocation)).thenReturn(newLocation);

        Location savedLocation = locationService.createLocation(newLocation);

        assertEquals(newLocation, savedLocation);
    }

    @Test
    public void testCreateLocation_NegativeScenario() throws EntityAlreadyExistsException {
        Location newLocation = getLocation();

        when(locationStorage.save("msk", newLocation))
                .thenThrow(new EntityAlreadyExistsException("Entity already exists"));

        assertThrows(EntityAlreadyExistsException.class, () -> locationService.createLocation(newLocation));
    }

    @Test
    public void testUpdateLocation_PositiveScenario() {
        Location updatedLocation = new Location("msk", "Updated Москва");

        when(locationStorage.update("msk", "msk", updatedLocation)).thenReturn(updatedLocation);

        Location result = locationService.updateLocation("msk", updatedLocation);

        assertEquals("Updated Москва", result.getName());
    }

    @Test
    public void testUpdateLocation_NegativeScenario() {
        Location updatedLocation = getLocation();

        when(locationStorage.update("msk", "msk", updatedLocation))
                .thenThrow(new EntityNotFoundException("Entity not found"));

        assertThrows(EntityNotFoundException.class, () -> locationService.updateLocation("msk", updatedLocation));
    }

    @Test
    public void testDeleteLocation_PositiveScenario() {
        Mockito.doNothing().when(locationStorage).deleteById("msk");

        locationService.deleteLocation("msk");

        Mockito.verify(locationStorage, Mockito.times(1)).deleteById("msk");
    }

    @Test
    public void testDeleteLocation_NegativeScenario() {
        doThrow(new EntityNotFoundException("Entity not found")).when(locationStorage).deleteById("msk");

        assertThrows(EntityNotFoundException.class, () -> locationService.deleteLocation("msk"));
    }
}