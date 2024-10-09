package org.tbank.hw8.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.tbank.hw8.client.ApiClient;
import org.tbank.hw8.dto.CurrencyRateDto;
import org.tbank.hw8.exception.CurrencyIsNotSupportedByCbException;
import org.tbank.hw8.exception.UnsupportedCurrencyException;
import org.tbank.hw8.model.Valute;
import org.tbank.hw8.service.CurrencyCachesService;

import java.util.Currency;
import java.util.List;

@Service
@AllArgsConstructor
public class CurrencyCacheServiceImpl implements CurrencyCachesService {
    private final ApiClient apiClient;

    @Override
    @Cacheable("currencyRate")
    public CurrencyRateDto getCurrencyRate(String code) {
        System.out.println("Get currency rate");
        if (!isCurrencyValid(code)) {
            throw new UnsupportedCurrencyException("Currency code does not exist: " + code);
        }

        final List<Valute> currencyRate = apiClient.fetchCurrencyRates();

        Valute foundValute = currencyRate.stream()
                .filter(valute -> valute.getCharCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new CurrencyIsNotSupportedByCbException("Currency is not supported by Central Bank"));

        final double value = Double.parseDouble(foundValute.getValue().replace(",", "."));

        return new CurrencyRateDto(code, value);
    }

    private boolean isCurrencyValid(String code) {
        try {
            return Currency.getAvailableCurrencies().contains(Currency.getInstance(code.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
