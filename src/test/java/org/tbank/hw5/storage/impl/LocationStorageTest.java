package org.tbank.hw5.storage.impl;

import org.junit.jupiter.api.Test;
import org.tbank.hw5.exception.EntityAlreadyExistsException;
import org.tbank.hw5.exception.EntityNotFoundException;
import org.tbank.hw5.model.Location;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocationStorageTest {

    private static final String LOCATION_SLUG_EKB = "ekb";
    private static final String LOCATION_SLUG_SPB = "spb";
    private static final String LOCATION_NAME_EKB = "Екатеринбург";
    private static final String UPDATED_LOCATION_NAME_KRD = "Краснодар";
    private static final String LOCATION_NAME_SPB = "Санкт-Петербург";

    public LocationStorage getLocationStorage() {
        return new LocationStorage();
    }

    private Location createLocation(String slug, String name) {
        return new Location(slug, name);
    }

    @Test
    void testSaveAndFindById() {
        LocationStorage locationStorage = getLocationStorage();

        Location location = createLocation(LOCATION_SLUG_EKB, LOCATION_NAME_EKB);

        locationStorage.save(LOCATION_SLUG_EKB, location);
        Location foundLocation = locationStorage.findById(LOCATION_SLUG_EKB);

        assertNotNull(foundLocation);
        assertEquals(LOCATION_SLUG_EKB, foundLocation.getSlug());
        assertEquals(LOCATION_NAME_EKB, foundLocation.getName());
    }

    @Test
    void testFindAll() {
        LocationStorage locationStorage = getLocationStorage();

        Location location1 = createLocation(LOCATION_SLUG_EKB, LOCATION_NAME_EKB);
        Location location2 = createLocation(LOCATION_SLUG_SPB, LOCATION_NAME_SPB);

        locationStorage.save(LOCATION_SLUG_EKB, location1);
        locationStorage.save(LOCATION_SLUG_SPB, location2);
        List<Location> locations = locationStorage.findAll();

        assertEquals(2, locations.size());
        assertTrue(locations.contains(location1));
        assertTrue(locations.contains(location2));
    }

    @Test
    void testUpdate() {
        LocationStorage locationStorage = getLocationStorage();

        Location location = createLocation(LOCATION_SLUG_EKB, LOCATION_NAME_EKB);
        locationStorage.save(LOCATION_SLUG_EKB, location);

        Location updatedLocation = createLocation(LOCATION_SLUG_EKB, UPDATED_LOCATION_NAME_KRD);
        locationStorage.update(LOCATION_SLUG_EKB, updatedLocation.getSlug(), updatedLocation);
        Location foundLocation = locationStorage.findById(LOCATION_SLUG_EKB);

        assertNotNull(foundLocation);
        assertEquals(LOCATION_SLUG_EKB, foundLocation.getSlug());
        assertEquals(UPDATED_LOCATION_NAME_KRD, foundLocation.getName());
    }

    @Test
    void testDeleteById() {
        LocationStorage locationStorage = getLocationStorage();

        Location location = createLocation(LOCATION_SLUG_EKB, LOCATION_NAME_EKB);
        locationStorage.save(LOCATION_SLUG_EKB, location);

        locationStorage.deleteById(LOCATION_SLUG_EKB);
        Location foundLocation = locationStorage.findById(LOCATION_SLUG_EKB);

        assertNull(foundLocation);
    }

    @Test
    void testSaveDuplicateIdThrowsException() {
        LocationStorage locationStorage = getLocationStorage();

        Location location = createLocation(LOCATION_SLUG_EKB, LOCATION_NAME_EKB);
        locationStorage.save(LOCATION_SLUG_EKB, location);

        Location duplicateLocation = createLocation(LOCATION_SLUG_EKB, UPDATED_LOCATION_NAME_KRD);

        assertThrows(EntityAlreadyExistsException.class, () -> {
            locationStorage.save(LOCATION_SLUG_EKB, duplicateLocation);
        });
    }

    @Test
    void testUpdateWithDifferentIdsThrowsException() {
        LocationStorage locationStorage = getLocationStorage();

        Location location = createLocation(LOCATION_SLUG_EKB, LOCATION_NAME_EKB);
        locationStorage.save(LOCATION_SLUG_EKB, location);

        Location updatedLocation = createLocation(LOCATION_SLUG_SPB, LOCATION_NAME_SPB);

        assertThrows(EntityNotFoundException.class, () -> {
            locationStorage.update(updatedLocation.getSlug(), location.getSlug(), updatedLocation);
        });
    }

    @Test
    void testUpdateNonExistentEntityThrowsException() {
        LocationStorage locationStorage = getLocationStorage();

        Location updatedLocation = createLocation(LOCATION_SLUG_EKB, UPDATED_LOCATION_NAME_KRD);

        assertThrows(EntityNotFoundException.class, () -> {
            locationStorage.update(LOCATION_SLUG_EKB, LOCATION_SLUG_EKB, updatedLocation);
        });
    }

    @Test
    void testDeleteNonExistentLocationThrowsException() {
        LocationStorage locationStorage = getLocationStorage();

        assertThrows(EntityNotFoundException.class, () -> {
            locationStorage.deleteById(LOCATION_SLUG_EKB);
        });
    }
}