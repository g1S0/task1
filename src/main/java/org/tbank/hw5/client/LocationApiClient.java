package org.tbank.hw5.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.tbank.hw5.dto.LocationDto;

import java.util.List;

@Component
public class LocationApiClient extends ApiClient<LocationDto> {
    @Value("${kudago.api.base-url}")
    private String baseUrl;

    private String getApiUrl() {
        return baseUrl + "/locations/";
    }

    public LocationApiClient(RestClient restClient) {
        super(restClient);
    }

    public List<LocationDto> fetchLocations() {
        return fetchFromApi(getApiUrl(), LocationDto[].class);
    }
}