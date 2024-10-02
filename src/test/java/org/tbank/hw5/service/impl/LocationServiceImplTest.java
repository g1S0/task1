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

    private static final String LOCATION_SLUG_MSK = "msk";
    private static final String LOCATION_SLUG_SPB = "spb";
    private static final String LOCATION_NAME_MSK = "Москва";
    private static final String LOCATION_NAME_SPB = "Санкт-Петербург";
    private static final String UPDATED_LOCATION_NAME_MSK = "Updated Москва";
    private static final String ENTITY_NOT_FOUND_MESSAGE = "Entity not found";
    private static final String ENTITY_ALREADY_EXISTS_MESSAGE = "Entity already exists";

    @Mock
    private LocationStorage locationStorage;

    @InjectMocks
    private LocationServiceImpl locationService;

    private Location createLocation(String slug, String name) {
        return new Location(slug, name);
    }

    private Location getLocation() {
        return createLocation(LOCATION_SLUG_MSK, LOCATION_NAME_MSK);
    }

    @Test
    public void testGetAllLocations_PositiveScenario() {
        List<Location> mockLocations = List.of(
                createLocation(LOCATION_SLUG_MSK, LOCATION_NAME_MSK),
                createLocation(LOCATION_SLUG_SPB, LOCATION_NAME_SPB)
        );

        when(locationStorage.findAll()).thenReturn(mockLocations);

        List<Location> locations = locationService.getAllLocations();

        assertEquals(2, locations.size());
        assertEquals(LOCATION_NAME_MSK, locations.get(0).getName());
    }

    @Test
    public void testGetAllLocations_EmptyResult() {
        when(locationStorage.findAll()).thenReturn(Collections.emptyList());

        List<Location> locations = locationService.getAllLocations();

        assertTrue(locations.isEmpty());
    }

    @Test
    public void testGetLocationBySlug_PositiveScenario() {
        when(locationStorage.findById(LOCATION_SLUG_MSK)).thenReturn(getLocation());

        Location location = locationService.getLocationById(LOCATION_SLUG_MSK);

        assertNotNull(location);
        assertEquals(LOCATION_NAME_MSK, location.getName());
    }

    @Test
    public void testGetLocationBySlug_NegativeScenario() {
        when(locationStorage.findById(LOCATION_SLUG_MSK)).thenReturn(null);

        Location location = locationService.getLocationById(LOCATION_SLUG_MSK);

        assertNull(location);
    }

    @Test
    public void testCreateLocation_PositiveScenario() throws EntityAlreadyExistsException {
        Location newLocation = getLocation();

        when(locationStorage.save(LOCATION_SLUG_MSK, newLocation)).thenReturn(newLocation);

        Location savedLocation = locationService.createLocation(newLocation);

        assertEquals(newLocation, savedLocation);
    }

    @Test
    public void testCreateLocation_NegativeScenario() throws EntityAlreadyExistsException {
        Location newLocation = getLocation();

        when(locationStorage.save(LOCATION_SLUG_MSK, newLocation))
                .thenThrow(new EntityAlreadyExistsException(ENTITY_ALREADY_EXISTS_MESSAGE));

        assertThrows(EntityAlreadyExistsException.class, () -> locationService.createLocation(newLocation));
    }

    @Test
    public void testUpdateLocation_PositiveScenario() {
        Location updatedLocation = createLocation(LOCATION_SLUG_MSK, UPDATED_LOCATION_NAME_MSK);

        when(locationStorage.update(LOCATION_SLUG_MSK, LOCATION_SLUG_MSK, updatedLocation)).thenReturn(updatedLocation);

        Location result = locationService.updateLocation(LOCATION_SLUG_MSK, updatedLocation);

        assertEquals(UPDATED_LOCATION_NAME_MSK, result.getName());
    }

    @Test
    public void testUpdateLocation_NegativeScenario() {
        Location updatedLocation = getLocation();

        when(locationStorage.update(LOCATION_SLUG_MSK, LOCATION_SLUG_MSK, updatedLocation))
                .thenThrow(new EntityNotFoundException(ENTITY_NOT_FOUND_MESSAGE));

        assertThrows(EntityNotFoundException.class, () -> locationService.updateLocation(LOCATION_SLUG_MSK, updatedLocation));
    }

    @Test
    public void testDeleteLocation_PositiveScenario() {
        Mockito.doNothing().when(locationStorage).deleteById(LOCATION_SLUG_MSK);

        locationService.deleteLocation(LOCATION_SLUG_MSK);

        Mockito.verify(locationStorage, Mockito.times(1)).deleteById(LOCATION_SLUG_MSK);
    }

    @Test
    public void testDeleteLocation_NegativeScenario() {
        doThrow(new EntityNotFoundException(ENTITY_NOT_FOUND_MESSAGE)).when(locationStorage).deleteById(LOCATION_SLUG_MSK);

        assertThrows(EntityNotFoundException.class, () -> locationService.deleteLocation(LOCATION_SLUG_MSK));
    }
}