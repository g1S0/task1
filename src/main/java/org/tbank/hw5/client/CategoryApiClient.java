package org.tbank.hw5.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.tbank.hw5.dto.CategoryDto;

import java.util.List;

@Component
public class CategoryApiClient extends ApiClient<CategoryDto> {

    private static final String API_CATEGORY_URL = "https://kudago.com/public-api/v1.4/place-categories/";

    public CategoryApiClient(RestClient restClient) {
        super(restClient);
    }

    public List<CategoryDto> fetchCategories() {
        return fetchFromApi(API_CATEGORY_URL, CategoryDto[].class);
    }
}
