package org.tbank.hw5.initializer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.*;

@Component
@Slf4j
public class DataInitializer {
    private final ExecutorService dataLoaderThreadPool;
    private final ScheduledExecutorService scheduledThreadPool;
    private final LocationDataLoaderInitializer locationDataLoaderInitializer;
    private final CategoryDataLoaderInitializer categoryDataLoaderInitializer;

    @Value("${schedule.duration}")
    private Duration scheduleDuration;

    public DataInitializer(
            @Qualifier("dataLoaderThreadPool") ExecutorService dataLoaderThreadPool,
            @Qualifier("scheduledThreadPool") ScheduledExecutorService scheduledThreadPool,
            LocationDataLoaderInitializer locationDataLoaderInitializer,
            CategoryDataLoaderInitializer categoryDataLoaderInitializer
    ) {
        this.dataLoaderThreadPool = dataLoaderThreadPool;
        this.scheduledThreadPool = scheduledThreadPool;
        this.locationDataLoaderInitializer = locationDataLoaderInitializer;
        this.categoryDataLoaderInitializer = categoryDataLoaderInitializer;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void scheduleDataInitialization() {
        scheduledThreadPool.scheduleAtFixedRate(this::initializeData, 0, scheduleDuration.toMillis(), TimeUnit.MILLISECONDS);
    }

    public void initializeData() {
        try {
            var categoriesFuture = dataLoaderThreadPool.submit(categoryDataLoaderInitializer::initializeCategories);
            var locationsFuture = dataLoaderThreadPool.submit(locationDataLoaderInitializer::initializeLocations);

            categoriesFuture.get();
            locationsFuture.get();

            log.info("All data has been successfully initialized.");
        } catch (Exception e) {
            log.error("Error occurred during data initialization: {}", e.getMessage(), e);
        }
    }
}
