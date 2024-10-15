package org.tbank.hw5.service.impl;

import org.springframework.stereotype.Service;
import org.tbank.hw5.client.CurrencyClient;
import org.tbank.hw5.client.EventsClient;
import org.tbank.hw5.dto.EventsRequestDto;
import org.tbank.hw5.dto.EventDto;
import org.tbank.hw5.service.EventsService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class EventsServiceImpl implements EventsService {

    private final EventsClient eventsClient;
    private final CurrencyClient currencyClient;

    public EventsServiceImpl(EventsClient eventsClient, CurrencyClient currencyClient) {
        this.eventsClient = eventsClient;
        this.currencyClient = currencyClient;
    }

    @Override
    public CompletableFuture<List<EventDto>> getEvents(EventsRequestDto eventsRequestDto) {
        CompletableFuture<List<EventDto>> eventsFuture = CompletableFuture.supplyAsync(() ->
                eventsClient.getEvents(eventsRequestDto.getDateFromAsUnixTimestamp(), eventsRequestDto.getDateToAsUnixTimestamp())
        );

        CompletableFuture<Double> convertedBudgetFuture = CompletableFuture.supplyAsync(() ->
                currencyClient.convertCurrency(eventsRequestDto.getBudget())
        );

        CompletableFuture<List<EventDto>> resultFuture = new CompletableFuture<>();

        eventsFuture.thenAcceptBoth(convertedBudgetFuture, (events, convertedBudget) -> {
            System.out.println(events);
            List<EventDto> filteredEvents = events.stream()
                    .filter(event -> {
                        double price = event.extractSumFromPrice();;

                        return price <= convertedBudget;
                    })
                    .collect(Collectors.toList());

            resultFuture.complete(filteredEvents);
        }).exceptionally(ex -> {
            resultFuture.completeExceptionally(ex);
            return null;
        });

        return resultFuture;
    }
}
