package org.tbank.hw5.initializer;

import lombok.AllArgsConstructor;
import org.example.annotation.LogExecutionTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.tbank.hw5.client.CategoryApiClient;
import org.tbank.hw5.dto.CategoryDto;
import org.tbank.hw5.mapper.CategoryMapper;
import org.tbank.hw5.model.Category;
import org.tbank.hw5.storage.DataObserver;

import java.util.List;

@Component
@AllArgsConstructor
public class CategoryDataLoaderInitializer implements Command {
    private static final Logger logger = LoggerFactory.getLogger(CategoryDataLoaderInitializer.class);

    private final CategoryApiClient categoryApiClient;
    private final List<DataObserver<Category>> dataObservers;
    private final CategoryMapper categoryMapper;

    @Override
    @EventListener(ApplicationReadyEvent.class)
    @LogExecutionTime
    public void execute() {
        logger.info("Starting data source for categories");

        List<CategoryDto> categoriesDto = categoryApiClient.fetchCategories();

        List<Category> categories = categoryMapper.toCategoryList(categoriesDto);

        if (categories != null) {
            notifyObservers(dataObservers, categories);
            logger.info("Data source successfully initialized with {} categories.", categories.size());
        } else {
            logger.warn("No locations found to initialize data source.");
        }

        logger.info("Data source initialization completed.");
    }

    private <T> void notifyObservers(List<DataObserver<T>> observers, List<T> data) {
        for (DataObserver<T> observer : observers) {
            observer.update(data);
        }
    }
}