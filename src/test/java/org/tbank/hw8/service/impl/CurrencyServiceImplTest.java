package org.tbank.hw8.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.tbank.hw8.client.CurrencyApiClient;
import org.tbank.hw8.dto.ConvertedAmountDto;
import org.tbank.hw8.dto.CurrencyConversionRequestDto;
import org.tbank.hw8.model.Valute;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CurrencyServiceImplTest {
    @Mock
    private CurrencyApiClient currencyApiClient;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private List<Valute> createMockCurrencyRates() {
        List<Valute> rates = new ArrayList<>();

        Valute aud = new Valute();
        aud.setCharCode("AUD");
        aud.setValue("65,4013");

        Valute azn = new Valute();
        azn.setCharCode("AZN");
        azn.setValue("57,0284");

        rates.add(aud);
        rates.add(azn);

        return rates;
    }

    @Test
    void convertCurrencyToRub() {
        CurrencyConversionRequestDto request = new CurrencyConversionRequestDto("AUD", "AZN", 100.0);

        when(currencyApiClient.fetchCurrencyRates()).thenReturn(createMockCurrencyRates());

        ConvertedAmountDto result = currencyService.convertCurrency(request);

        double expectedConvertedAmount = 114.6810;

        assertEquals("AUD", result.getFromCurrency());
        assertEquals("AZN", result.getToCurrency());
        assertEquals(expectedConvertedAmount, result.getConvertedAmount(), 0.001);
    }
}