package org.tbank.hw5.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tbank.hw5.client.CurrencyClient;
import org.tbank.hw5.client.EventsClient;
import org.tbank.hw5.dto.EventsRequestDto;
import org.tbank.hw5.dto.EventDto;
import org.tbank.hw5.service.EventsService;
import org.tbank.hw5.utils.StringToNumberUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventsServiceImpl implements EventsService {

    private final EventsClient eventsClient;
    private final CurrencyClient currencyClient;

    public EventsServiceImpl(EventsClient eventsClient, CurrencyClient currencyClient) {
        this.eventsClient = eventsClient;
        this.currencyClient = currencyClient;
    }

    @Override
    public CompletableFuture<List<EventDto>> getEvents(EventsRequestDto eventsRequestDto) {
        log.info("Received events request: {}", eventsRequestDto);

        CompletableFuture<List<EventDto>> eventsFuture = CompletableFuture.supplyAsync(() -> {
            log.info("Fetching events from {} to {}", eventsRequestDto.getDateFromAsUnixTimestamp(), eventsRequestDto.getDateToAsUnixTimestamp());
            return eventsClient.getEvents(eventsRequestDto.getDateFromAsUnixTimestamp(), eventsRequestDto.getDateToAsUnixTimestamp());
        });

        CompletableFuture<Double> convertedBudgetFuture = CompletableFuture.supplyAsync(() -> {
            log.info("Converting budget: {}", eventsRequestDto.getBudget());
            return currencyClient.convertCurrency(eventsRequestDto.getBudget());
        });

        CompletableFuture<List<EventDto>> resultFuture = new CompletableFuture<>();

        eventsFuture.thenAcceptBoth(convertedBudgetFuture, (events, convertedBudget) -> {
            log.info("Events fetched: {}", events.size());
            log.info("Converted budget: {}", convertedBudget);

            List<EventDto> filteredEvents = events.stream()
                    .filter(event -> {
                        double price = StringToNumberUtils.getNumberFromString(event.getPrice());
                        log.info("Checking event price: {}, converted budget: {}", price, convertedBudget);
                        return price <= convertedBudget;
                    })
                    .collect(Collectors.toList());

            log.info("Filtered events count: {}", filteredEvents.size());
            resultFuture.complete(filteredEvents);
        }).exceptionally(ex -> {
            log.error("Error processing events or budget conversion", ex);
            resultFuture.completeExceptionally(ex);
            return null;
        });

        return resultFuture;
    }
}
