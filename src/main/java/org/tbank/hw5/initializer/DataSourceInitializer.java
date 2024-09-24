package org.tbank.hw5.initializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tbank.hw5.annotation.LogExecutionTime;
import org.tbank.hw5.dto.CategoryDto;
import org.tbank.hw5.mapper.CategoryMapper;
import org.tbank.hw5.model.Category;
import org.tbank.hw5.service.KudagoService;
import org.tbank.hw5.storage.impl.CategoryStorage;

import java.util.List;

@Component
public class DataSourceInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceInitializer.class);

    private final KudagoService kudagoService;
    private final CategoryStorage categoryStorage;
    private final CategoryMapper categoryMapper;

    public DataSourceInitializer(KudagoService kudagoService, CategoryStorage categoryStorage, CategoryMapper categoryMapper) {
        this.kudagoService = kudagoService;
        this.categoryStorage = categoryStorage;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @LogExecutionTime
    public void run(String... args) {
        logger.info("Starting data source initialization...");

        CategoryDto[] categoriesDto = kudagoService.fetchCategoriesFromApi();

        List<Category> categories = categoryMapper.toCategoryList(List.of(categoriesDto));

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
