package org.tbank.hw5.initializer;

import lombok.extern.slf4j.Slf4j;
import org.example.annotation.LogExecutionTime;
import org.springframework.stereotype.Component;
import org.tbank.hw5.client.LocationApiClient;
import org.tbank.hw5.dto.LocationDto;
import org.tbank.hw5.mapper.LocationMapper;
import org.tbank.hw5.model.Location;
import org.tbank.hw5.storage.impl.LocationStorage;

import java.util.List;

@Component
@Slf4j
public class LocationDataLoaderInitializer {
    private final LocationApiClient locationApiClient;
    private final LocationStorage locationStorage;
    private final LocationMapper locationMapper;

    public LocationDataLoaderInitializer(LocationApiClient locationApiClient, LocationStorage locationStorage, LocationMapper locationMapper) {
        this.locationApiClient = locationApiClient;
        this.locationStorage = locationStorage;
        this.locationMapper = locationMapper;
    }


    @LogExecutionTime
    public void initializeLocations() {
        log.info("Starting location data source for locations");

        List<LocationDto> locationsDto = locationApiClient.fetchLocations();

        List<Location> locations = locationMapper.toLocationList(locationsDto);
        locationStorage.clear();
        if (locations != null) {
            for (Location location : locations) {
                locationStorage.save(location.getSlug(), location);
            }
            log.info("Location data source successfully initialized with {} locations.", locations.size());
        } else {
            log.warn("No locations found to initialize data source.");
        }

        log.info("Location data source initialization completed.");
    }
}
