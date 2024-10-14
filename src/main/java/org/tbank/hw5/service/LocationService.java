package org.tbank.hw5.service;

import org.tbank.hw5.model.Location;

import java.util.List;

public interface LocationService {
    List<Location> getAllLocations();
    Location getLocationById(String id);
    Location createLocation(Location location);
    Location updateLocation(String id, Location location);
    void deleteLocation(String id);
}
