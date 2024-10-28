package org.tbank.hw5.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.tbank.hw5.dto.EventDto;
import org.tbank.hw5.dto.EventResponseDto;

import java.util.List;

@Component
public class EventsClient extends ApiClient<EventResponseDto> {
    @Value("${kudago.api.base-url}")
    private String baseUrl;

    private static final String API_URL_TEMPLATE = "%s/events/?actual_since=%d&actual_until=%d&location=%s&fields=%s";

    public EventsClient(RestClient restClient) {
        super(restClient);
    }

    private String getApiUrl(long dateFrom, long dateTo) {
        String location = "krd";
        String fields = "price,title";
        return String.format(API_URL_TEMPLATE, baseUrl, dateFrom, dateTo, location, fields);
    }

    public List<EventDto> getEvents(long dateFrom, long dateTo) {
        return fetchSingleFromApi(getApiUrl(dateFrom, dateTo), EventResponseDto.class).getResults();
    }
}
