package org.tbank.hw5.initializer;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.tbank.hw5.annotation.LogExecutionTime;
import org.tbank.hw5.client.LocationApiClient;
import org.tbank.hw5.dto.LocationDto;
import org.tbank.hw5.mapper.LocationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbank.hw5.model.Location;
import org.tbank.hw5.storage.impl.LocationStorage;

import java.util.List;

@Component
public class LocationDataLoaderInitializer {
    private static final Logger logger = LoggerFactory.getLogger(LocationDataLoaderInitializer.class);

    private final LocationApiClient locationApiClient;
    private final LocationStorage locationStorage;
    private final LocationMapper locationMapper;

    public LocationDataLoaderInitializer(LocationApiClient locationApiClient, LocationStorage locationStorage, LocationMapper locationMapper) {
        this.locationApiClient = locationApiClient;
        this.locationStorage = locationStorage;
        this.locationMapper = locationMapper;
    }


    @EventListener(ApplicationReadyEvent.class)
    @LogExecutionTime
    public void initializeCategories() {
        logger.info("Starting location data source for locations");

        List<LocationDto> locationsDto = locationApiClient.fetchLocationsFromApi();

        List<Location> locations = locationMapper.toLocationList(locationsDto);

        if (locations != null) {
            for (Location location : locations) {
                locationStorage.save(location.getSlug(), location);
            }
            logger.info("Location data source successfully initialized with {} locations.", locations.size());
        } else {
            logger.warn("No locations found to initialize data source.");
        }

        logger.info("Location data source initialization completed.");
    }
}
