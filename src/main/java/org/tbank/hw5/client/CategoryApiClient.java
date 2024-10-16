package org.tbank.hw5.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.tbank.hw5.dto.CategoryDto;

import java.util.List;

@Component
public class CategoryApiClient extends ApiClient<CategoryDto> {
    @Value("${kudago.api.base-url}")
    private String baseUrl;

    private String getApiUrl() {
        return baseUrl + "/place-categories/";
    }

    public CategoryApiClient(RestClient restClient) {
        super(restClient);
    }

    public List<CategoryDto> fetchCategories() {
        return fetchFromApi(getApiUrl(), CategoryDto[].class);
    }
}
