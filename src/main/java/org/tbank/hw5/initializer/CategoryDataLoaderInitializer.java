package org.tbank.hw5.initializer;

import org.example.annotation.LogExecutionTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.tbank.hw5.client.CategoryApiClient;
import org.tbank.hw5.dto.CategoryDto;
import org.tbank.hw5.mapper.CategoryMapper;
import org.tbank.hw5.model.Category;
import org.tbank.hw5.storage.impl.CategoryStorage;

import java.util.List;

@Component
public class CategoryDataLoaderInitializer {
    private static final Logger logger = LoggerFactory.getLogger(CategoryDataLoaderInitializer.class);

    private final CategoryApiClient categoryApiClient;
    private final CategoryStorage categoryStorage;
    private final CategoryMapper categoryMapper;

    CategoryDataLoaderInitializer(CategoryApiClient categoryApiClient, CategoryStorage categoryStorage, CategoryMapper categoryMapper) {
        this.categoryApiClient = categoryApiClient;
        this.categoryStorage = categoryStorage;
        this.categoryMapper = categoryMapper;
    }

    @LogExecutionTime
    public void initializeCategories() {
        logger.info("Starting data source for categories");

        List<CategoryDto> categoriesDto = categoryApiClient.fetchCategories();

        List<Category> categories = categoryMapper.toCategoryList(categoriesDto);
        categoryStorage.clear();
        if (categories != null) {
            for (Category category : categories) {
                categoryStorage.save(category.getId(), category);
            }
            logger.info("Data source successfully initialized with {} categories.", categories.size());
        } else {
            logger.warn("No locations found to initialize data source.");
        }

        logger.info("Data source initialization completed.");
    }
}
