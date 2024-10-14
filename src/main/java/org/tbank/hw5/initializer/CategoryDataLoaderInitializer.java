package org.tbank.hw5.initializer;

import lombok.extern.slf4j.Slf4j;
import org.example.annotation.LogExecutionTime;
import org.springframework.stereotype.Component;
import org.tbank.hw5.client.CategoryApiClient;
import org.tbank.hw5.dto.CategoryDto;
import org.tbank.hw5.mapper.CategoryMapper;
import org.tbank.hw5.model.Category;
import org.tbank.hw5.storage.impl.CategoryStorage;

import java.util.List;

@Component
@Slf4j
public class CategoryDataLoaderInitializer {
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
        log.info("Starting data source for categories");

        List<CategoryDto> categoriesDto = categoryApiClient.fetchCategories();

        List<Category> categories = categoryMapper.toCategoryList(categoriesDto);
        categoryStorage.clear();
        if (categories != null) {
            for (Category category : categories) {
                categoryStorage.save(category.getId(), category);
            }
            log.info("Data source successfully initialized with {} categories.", categories.size());
        } else {
            log.warn("No locations found to initialize data source.");
        }

        log.info("Data source initialization completed.");
    }
}
