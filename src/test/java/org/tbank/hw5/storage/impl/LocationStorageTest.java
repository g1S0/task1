package org.tbank.hw5.storage.impl;

import org.junit.jupiter.api.Test;
import org.tbank.hw5.exception.EntityAlreadyExistsException;
import org.tbank.hw5.exception.EntityNotFoundException;
import org.tbank.hw5.model.Location;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocationStorageTest {

    public LocationStorage getLocationStorage() {
        return new LocationStorage();
    }

    @Test
    void testSaveAndFindById() {
        LocationStorage locationStorage = getLocationStorage();

        Location location = new Location("ekb", "Екатеринбург");

        locationStorage.save("ekb", location);
        Location foundLocation = locationStorage.findById("ekb");

        assertNotNull(foundLocation);
        assertEquals("ekb", foundLocation.getSlug());
        assertEquals("Екатеринбург", foundLocation.getName());
    }

    @Test
    void testFindAll() {
        LocationStorage locationStorage = getLocationStorage();

        Location location1 = new Location("ekb", "Екатеринбург");
        Location location2 = new Location("spb", "Санкт-Петербург");

        locationStorage.save("ekb", location1);
        locationStorage.save("spb", location2);
        List<Location> locations = locationStorage.findAll();

        assertEquals(2, locations.size());
        assertTrue(locations.contains(location1));
        assertTrue(locations.contains(location2));
    }

    @Test
    void testUpdate() {
        LocationStorage locationStorage = getLocationStorage();

        Location location = new Location("ekb", "Екатеринбург");
        locationStorage.save("ekb", location);

        Location updatedLocation = new Location("ekb", "Екатеринабург");
        locationStorage.update("ekb", updatedLocation.getSlug(), updatedLocation);
        Location foundLocation = locationStorage.findById("ekb");

        assertNotNull(foundLocation);
        assertEquals("ekb", foundLocation.getSlug());
        assertEquals("Екатеринабург", foundLocation.getName());
    }

    @Test
    void testDeleteById() {
        LocationStorage locationStorage = getLocationStorage();

        Location location = new Location("ekb", "Екатеринбург");
        locationStorage.save("ekb", location);

        locationStorage.deleteById("ekb");
        Location foundLocation = locationStorage.findById("ekb");

        assertNull(foundLocation);
    }

    @Test
    void testSaveDuplicateIdThrowsException() {
        LocationStorage locationStorage = new LocationStorage();

        Location location = new Location("ekb", "Екатеринбург");
        locationStorage.save("ekb", location);

        Location duplicateLocation = new Location("ekb", "Екатеринабург");

        assertThrows(EntityAlreadyExistsException.class, () -> {
            locationStorage.save("ekb", duplicateLocation);
        });
    }

    @Test
    void testUpdateWithDifferentIdsThrowsException() {
        LocationStorage locationStorage = getLocationStorage();

        Location location = new Location("ekb", "Екатеринбург");
        locationStorage.save("ekb", location);

        Location updatedLocation = new Location("spb", "Санкт-Петербург");

        assertThrows(EntityNotFoundException.class, () -> {
            locationStorage.update(updatedLocation.getSlug(), location.getSlug(), updatedLocation);
        });
    }

    @Test
    void testUpdateNonExistentEntityThrowsException() {
        LocationStorage locationStorage = getLocationStorage();

        Location updatedLocation = new Location("ekb", "Екатеринабург");

        assertThrows(EntityNotFoundException.class, () -> {
            locationStorage.update("ekb", "ekb", updatedLocation);
        });
    }

    @Test
    void testDeleteNonExistentLocationThrowsException() {
        LocationStorage locationStorage = getLocationStorage();

        assertThrows(EntityNotFoundException.class, () -> {
            locationStorage.deleteById("ekb"); // Локации с таким ID нет
        });
    }
}