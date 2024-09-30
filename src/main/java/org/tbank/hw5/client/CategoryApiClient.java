package org.tbank.hw5.client;

import org.springframework.stereotype.Component;
import org.tbank.hw5.dto.CategoryDto;

import java.util.List;

@Component
public class CategoryApiClient {
    private final ApiClient apiClient;

    private static final String API_CATEGORY_URL = "https://kudago.com/public-api/v1.4/place-categories/";

    public CategoryApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public List<CategoryDto> fetchCategoriesFromApi() {
        return apiClient.fetchFromApi(API_CATEGORY_URL, CategoryDto[].class, "categories");
    }
}
