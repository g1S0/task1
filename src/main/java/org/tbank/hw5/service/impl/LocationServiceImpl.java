package org.tbank.hw5.service.impl;

import org.springframework.stereotype.Service;
import org.tbank.hw5.service.LocationService;
import org.tbank.hw5.storage.impl.LocationStorage;
import org.tbank.hw5.model.Location;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationStorage locationStorage;

    public LocationServiceImpl(LocationStorage locationStorage) {
        this.locationStorage = locationStorage;
    }

    @Override
    public List<Location> getAllLocations() {
        return locationStorage.findAll();
    }

    @Override
    public Location getLocationById(String id) {
        return locationStorage.findById(id);
    }

    @Override
    public Location createLocation(Location location) {
        return locationStorage.save(location.getSlug(), location);
    }

    @Override
    public Location updateLocation(String id, Location location) {
        return locationStorage.update(location.getSlug(), id, location);
    }

    @Override
    public void deleteLocation(String id) {
        locationStorage.deleteById(id);
    }
}
