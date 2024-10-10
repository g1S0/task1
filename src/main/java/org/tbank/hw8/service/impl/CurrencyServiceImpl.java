package org.tbank.hw8.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tbank.hw8.dto.ConvertedAmountDto;
import org.tbank.hw8.dto.CurrencyConversionRequestDto;
import org.tbank.hw8.dto.CurrencyRateDto;
import org.tbank.hw8.service.CurrencyCacheService;
import org.tbank.hw8.service.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@AllArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyServiceImpl.class);
    private final CurrencyCacheService currencyCachesService;

    @Override
    public CurrencyRateDto getCurrencyRate(String code) {
        return currencyCachesService.getCurrencyRate(code);
    }

    @Override
    public ConvertedAmountDto convertCurrency(CurrencyConversionRequestDto conversionRequestInfo) {

        logger.info("Converting currency from {} to {} with amount: {}",
                conversionRequestInfo.getFromCurrency(),
                conversionRequestInfo.getToCurrency(),
                conversionRequestInfo.getAmount());

        CurrencyRateDto fromCurrencyRate = getCurrencyRate(conversionRequestInfo.getFromCurrency());
        CurrencyRateDto toCurrencyRate = getCurrencyRate(conversionRequestInfo.getToCurrency());
        double convertedAmount = conversionRequestInfo.getAmount() * (fromCurrencyRate.getRate() / toCurrencyRate.getRate());

        logger.info("Successfully converted {} {} to {} {}",
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
}
