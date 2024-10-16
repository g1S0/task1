package org.tbank.hw8.service;

import org.tbank.hw8.dto.CurrencyConversionRequestDto;
import org.tbank.hw8.dto.CurrencyRateDto;
import org.tbank.hw8.dto.ConvertedAmountDto;

public interface CurrencyService {
    CurrencyRateDto getCurrencyRate(String code);

    ConvertedAmountDto convertCurrency(CurrencyConversionRequestDto conversionRequestInfo);
}
