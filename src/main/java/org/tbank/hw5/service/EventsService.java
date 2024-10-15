package org.tbank.hw5.service;

import org.tbank.hw5.dto.EventDto;
import org.tbank.hw5.dto.EventsRequestDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface EventsService {
    CompletableFuture<List<EventDto>> getEvents(EventsRequestDto eventsRequestDto);
}
