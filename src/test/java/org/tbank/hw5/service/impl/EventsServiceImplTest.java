package org.tbank.hw5.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.tbank.hw5.client.CurrencyClient;
import org.tbank.hw5.client.EventsClient;
import org.tbank.hw5.dto.EventDto;
import org.tbank.hw5.dto.EventsRequestDto;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventsServiceImplTest {
    @Mock
    private EventsClient eventsClient;
    @Mock
    private CurrencyClient currencyClient;
    @InjectMocks
    private EventsServiceImpl eventsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEvents_Success() throws ExecutionException, InterruptedException {
        EventsRequestDto request = new EventsRequestDto(1000L, "RUB", null, null);

        List<EventDto> events = Arrays.asList(
                new EventDto("Event 1", "100"),
                new EventDto("Event 2", "300"),
                new EventDto("Event 3", "700")
        );

        when(eventsClient.getEvents(anyLong(), anyLong())).thenReturn(events);
        when(currencyClient.convertCurrency(anyDouble())).thenReturn(500.0);

        CompletableFuture<List<EventDto>> resultFuture = eventsService.getEvents(request);
        List<EventDto> result = resultFuture.get();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(e -> e.getTitle().equals("Event 1")));
        assertTrue(result.stream().anyMatch(e -> e.getTitle().equals("Event 2")));
    }
}