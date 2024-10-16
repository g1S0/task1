package org.tbank.hw8.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.tbank.hw8.client.CurrencyApiClient;
import org.tbank.hw8.dto.ConvertedAmountDto;
import org.tbank.hw8.dto.CurrencyConversionRequestDto;
import org.tbank.hw8.dto.CurrencyRateDto;
import org.tbank.hw8.exception.CurrencyIsNotSupportedByCbException;
import org.tbank.hw8.exception.UnsupportedCurrencyException;
import org.tbank.hw8.model.Valute;
import org.tbank.hw8.service.CurrencyService;

import java.util.Currency;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyApiClient currencyApiClient;

    public CurrencyRateDto getCurrencyRate(String code) {
        if (!isCurrencyValid(code)) {
            log.warn("Invalid currency code: {}", code);
            throw new UnsupportedCurrencyException("Currency code does not exist: " + code);
        }

        final List<Valute> currencyRate = currencyApiClient.fetchCurrencyRates();

        Valute foundValute = currencyRate.stream()
                .filter(valute -> valute.getCharCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Currency {} is not supported by Central Bank", code);
                    return new CurrencyIsNotSupportedByCbException("Currency is not supported by Central Bank");
                });

        final double value = Double.parseDouble(foundValute.getValue().replace(",", "."));
        log.info("Currency rate for {} is {}", code, value);

        return new CurrencyRateDto(code, value);
    }

    @Override
    public ConvertedAmountDto convertCurrency(CurrencyConversionRequestDto conversionRequestInfo) {

        log.info("Converting currency from {} to {} with amount: {}",
                conversionRequestInfo.getFromCurrency(),
                conversionRequestInfo.getToCurrency(),
                conversionRequestInfo.getAmount());

        CurrencyRateDto fromCurrencyRate = getCurrencyRate(conversionRequestInfo.getFromCurrency());
        CurrencyRateDto toCurrencyRate = getCurrencyRate(conversionRequestInfo.getToCurrency());
        double convertedAmount = conversionRequestInfo.getAmount() * (fromCurrencyRate.getRate() / toCurrencyRate.getRate());

        log.info("Successfully converted {} {} to {} {}",
                conversionRequestInfo.getAmount(),
                conversionRequestInfo.getFromCurrency(),
                convertedAmount,
                conversionRequestInfo.getToCurrency());

        return new ConvertedAmountDto(
                conversionRequestInfo.getFromCurrency(),
                conversionRequestInfo.getToCurrency(),
                convertedAmount
        );
    }

    private boolean isCurrencyValid(String code) {
        try {
            return Currency.getAvailableCurrencies().contains(Currency.getInstance(code.toUpperCase()));
        } catch (IllegalArgumentException e) {
            log.warn("Currency code {} is not valid", code);
            return false;
        }
    }
}
