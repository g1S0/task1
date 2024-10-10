package org.tbank.hw8.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.tbank.hw8.dto.ConvertedAmountDto;
import org.tbank.hw8.dto.CurrencyConversionRequestDto;
import org.tbank.hw8.dto.CurrencyRateDto;
import org.tbank.hw8.service.CurrencyCacheService;

import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CurrencyServiceImplTest {

    @Mock
    private CurrencyCacheService currencyCachesService;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void convertCurrencyToRub() {
        CurrencyConversionRequestDto request = new CurrencyConversionRequestDto("AUD", "AZN", 100.0);

        when(currencyCachesService.getCurrencyRate("AUD")).thenReturn(new CurrencyRateDto("AUD", 65.4013));
        when(currencyCachesService.getCurrencyRate("AZN")).thenReturn(new CurrencyRateDto("AZN", 57.0284));

        ConvertedAmountDto result = currencyService.convertCurrency(request);

        double expectedConvertedAmount = 114.6810;

        assertEquals("AUD", result.getFromCurrency());
        assertEquals("AZN", result.getToCurrency());
        assertEquals(expectedConvertedAmount, result.getConvertedAmount(), 0.001);
    }
}