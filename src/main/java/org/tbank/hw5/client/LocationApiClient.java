package org.tbank.hw5.client;

import org.springframework.stereotype.Component;
import org.tbank.hw5.dto.LocationDto;

@Component
public class LocationApiClient {
    private final ApiClient apiClient;

    private static final String API_LOCATION_URL = "https://kudago.com/public-api/v1.4/locations";

    public LocationApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public LocationDto[] fetchLocationsFromApi() {
        return apiClient.fetchFromApi(API_LOCATION_URL, LocationDto[].class, "locations");
    }
}
