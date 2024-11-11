package org.tbank.hw5.initializer;

import lombok.AllArgsConstructor;
import org.example.annotation.LogExecutionTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.tbank.hw5.client.LocationApiClient;
import org.tbank.hw5.dto.LocationDto;
import org.tbank.hw5.mapper.LocationMapper;
import org.tbank.hw5.model.Location;
import org.tbank.hw5.storage.DataObserver;

import java.util.List;

@Component
@AllArgsConstructor
public class LocationDataLoaderInitializer implements Command {
    private static final Logger logger = LoggerFactory.getLogger(LocationDataLoaderInitializer.class);

    private final LocationApiClient locationApiClient;
    private final List<DataObserver<Location>> dataObservers;
    private final LocationMapper locationMapper;

    @Override
    @EventListener(ApplicationReadyEvent.class)
    @LogExecutionTime
    public void execute() {
        logger.info("Starting location data source for locations");

        List<LocationDto> locationsDto = locationApiClient.fetchLocations();

        List<Location> locations = locationMapper.toLocationList(locationsDto);

        if (locations != null) {
            notifyObservers(dataObservers, locations);
            logger.info("Location data source successfully initialized with {} locations.", locations.size());
        } else {
            logger.warn("No locations found to initialize data source.");
        }

        logger.info("Location data source initialization completed.");
    }

    private <T> void notifyObservers(List<DataObserver<T>> observers, List<T> data) {
        for (DataObserver<T> observer : observers) {
            observer.update(data);
        }
    }
}