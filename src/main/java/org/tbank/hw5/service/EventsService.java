package org.tbank.hw5.service;

import org.tbank.hw5.dto.EventDto;
import org.tbank.hw5.dto.EventsRequestDto;

import java.util.List;

public interface EventsService {
    List<EventDto> getEvents(EventsRequestDto eventsRequestDto);
}
