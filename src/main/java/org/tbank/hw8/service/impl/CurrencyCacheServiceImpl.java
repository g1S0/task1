package org.tbank.hw8.service.impl;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.tbank.hw8.client.CurrencyApiClient;
import org.tbank.hw8.dto.CurrencyRateDto;
import org.tbank.hw8.exception.CurrencyIsNotSupportedByCbException;
import org.tbank.hw8.exception.UnsupportedCurrencyException;
import org.tbank.hw8.model.Valute;
import org.tbank.hw8.service.CurrencyCacheService;

import java.util.Currency;
import java.util.List;

@Service
@AllArgsConstructor
public class CurrencyCacheServiceImpl implements CurrencyCacheService {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyCacheServiceImpl.class);
    private final CurrencyApiClient apiClient;

    @Override
    @Cacheable("currencyRate")
    public CurrencyRateDto getCurrencyRate(String code) {
        if (!isCurrencyValid(code)) {
            logger.warn("Invalid currency code: {}", code);
            throw new UnsupportedCurrencyException("Currency code does not exist: " + code);
        }

        final List<Valute> currencyRate = apiClient.fetchCurrencyRates();

        Valute foundValute = currencyRate.stream()
                .filter(valute -> valute.getCharCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("Currency {} is not supported by Central Bank", code);
                    return new CurrencyIsNotSupportedByCbException("Currency is not supported by Central Bank");
                });

        final double value = Double.parseDouble(foundValute.getValue().replace(",", "."));
        logger.info("Currency rate for {} is {}", code, value);

        return new CurrencyRateDto(code, value);
    }

    private boolean isCurrencyValid(String code) {
        try {
            return Currency.getAvailableCurrencies().contains(Currency.getInstance(code.toUpperCase()));
        } catch (IllegalArgumentException e) {
            logger.warn("Currency code {} is not valid", code);
            return false;
        }
    }
}
