package org.tbank.hw5.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tbank.hw5.client.CurrencyClient;
import org.tbank.hw5.client.EventsClient;
import org.tbank.hw5.dto.EventDto;
import org.tbank.hw5.dto.EventsRequestDto;
import org.tbank.hw5.service.EventsService;
import org.tbank.hw5.utils.StringToNumberUtils;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import java.util.List;
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

    public List<EventDto> getEvents(EventsRequestDto eventsRequestDto) {
        log.info("Received events request: {}", eventsRequestDto);

        Mono<List<EventDto>> eventsMono = Mono.fromCallable(() -> {
            log.info("Fetching events from {} to {}",
                    eventsRequestDto.getDateFromAsUnixTimestamp(),
                    eventsRequestDto.getDateToAsUnixTimestamp());
            return eventsClient.getEvents(eventsRequestDto.getDateFromAsUnixTimestamp(),
                    eventsRequestDto.getDateToAsUnixTimestamp());
        });

        Mono<Double> convertedBudgetMono = Mono.fromCallable(() -> {
            log.info("Converting budget: {}", eventsRequestDto.getBudget());
            return currencyClient.convertCurrency(eventsRequestDto.getBudget());
        });

        return Mono.zip(eventsMono, convertedBudgetMono)
                .flatMap(tuple -> {
                    List<EventDto> events = tuple.getT1();
                    Double convertedBudget = tuple.getT2();

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

                    return Mono.just(filteredEvents);
                })
                .onErrorResume(ex -> {
                    log.error("Error processing events or budget conversion", ex);
                    return Mono.error(new RuntimeException("Failed to process events or convert budget", ex));
                })
                .block();
    }

//    @Override
//    public List<EventDto> getEvents(EventsRequestDto eventsRequestDto) {
//        log.info("Received events request: {}", eventsRequestDto);
//
//        CompletableFuture<List<EventDto>> eventsFuture = CompletableFuture.supplyAsync(() -> {
//            log.info("Fetching events from {} to {}", eventsRequestDto.getDateFromAsUnixTimestamp(), eventsRequestDto.getDateToAsUnixTimestamp());
//            return eventsClient.getEvents(eventsRequestDto.getDateFromAsUnixTimestamp(), eventsRequestDto.getDateToAsUnixTimestamp());
//        });
//
//        CompletableFuture<Double> convertedBudgetFuture = CompletableFuture.supplyAsync(() -> {
//            log.info("Converting budget: {}", eventsRequestDto.getBudget());
//            return currencyClient.convertCurrency(eventsRequestDto.getBudget());
//        });
//
//        CompletableFuture<List<EventDto>> resultFuture = new CompletableFuture<>();
//
//        eventsFuture.thenAcceptBoth(convertedBudgetFuture, (events, convertedBudget) -> {
//            log.info("Events fetched: {}", events.size());
//            log.info("Converted budget: {}", convertedBudget);
//
//            List<EventDto> filteredEvents = events.stream()
//                    .filter(event -> {
//                        double price = StringToNumberUtils.getNumberFromString(event.getPrice());
//                        log.info("Checking event price: {}, converted budget: {}", price, convertedBudget);
//                        return price <= convertedBudget;
//                    })
//                    .collect(Collectors.toList());
//
//            log.info("Filtered events count: {}", filteredEvents.size());
//            resultFuture.complete(filteredEvents);
//        }).exceptionally(ex -> {
//            log.error("Error processing events or budget conversion", ex);
//            resultFuture.completeExceptionally(ex);
//            return null;
//        });
//
//        try {
//            return resultFuture.get();
//        } catch (Exception e) {
//            log.error("Exception occurred while retrieving events: {}", e.getMessage(), e);
//            throw new RuntimeException("Failed to retrieve events", e); // Выбрасываем исключение с причиной
//        }
//    }
}
