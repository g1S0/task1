package org.tbank.hw5.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.tbank.hw5.dto.LocationDto;

import java.util.List;

@Component
public class LocationApiClient extends ApiClient<LocationDto> {

    private static final String API_LOCATION_URL = "https://kudago.com/public-api/v1.4/locations/";

    public LocationApiClient(RestClient restClient) {
        super(restClient);
    }

    public List<LocationDto> fetchLocations() {
        return fetchFromApi(API_LOCATION_URL, LocationDto[].class, "locations");
    }
}