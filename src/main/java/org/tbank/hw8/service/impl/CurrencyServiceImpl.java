package org.tbank.hw8.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tbank.hw8.dto.ConvertedAmountDto;
import org.tbank.hw8.dto.CurrencyConversionRequestDto;
import org.tbank.hw8.dto.CurrencyRateDto;
import org.tbank.hw8.service.CurrencyCachesService;
import org.tbank.hw8.service.CurrencyService;


@Service
@AllArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyCachesService currencyCachesService;

    @Override
    public CurrencyRateDto getCurrencyRate(String code) {
        return currencyCachesService.getCurrencyRate(code);
    }

    @Override
    public ConvertedAmountDto convertCurrency(CurrencyConversionRequestDto conversionRequestInfo) {

        CurrencyRateDto fromCurrencyRate = getCurrencyRate(conversionRequestInfo.getFromCurrency());
        CurrencyRateDto toCurrencyRate = getCurrencyRate(conversionRequestInfo.getToCurrency());
        double convertedAmount = conversionRequestInfo.getAmount() * (fromCurrencyRate.getRate() / toCurrencyRate.getRate());

        return new ConvertedAmountDto(
                conversionRequestInfo.getFromCurrency(),
                conversionRequestInfo.getToCurrency(),
                convertedAmount
        );
    }
}
