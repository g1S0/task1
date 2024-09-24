package org.tbank.hw5.service.impl;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.tbank.hw5.dto.CategoryDto;
import org.tbank.hw5.service.KudagoService;
import org.slf4j.Logger;

@Service
public class KudagoServiceImpl implements KudagoService {
    private static final Logger logger = LoggerFactory.getLogger(KudagoServiceImpl.class);
    private final RestTemplate restTemplate;

    static final String API_LOCATION_URL = "https://kudago.com/public-api/v1.4/place-categories/";

    public KudagoServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CategoryDto[] fetchCategoriesFromApi() {
        logger.info("Fetching locations from KudaGo API...");

        CategoryDto[] categories = restTemplate.getForObject(API_LOCATION_URL, CategoryDto[].class);

        if (categories != null) {
            logger.info("Successfully fetched {} locations from KudaGo API.", categories.length);
        } else {
            logger.warn("No locations fetched from KudaGo API.");
        }

        return categories;
    }
}
