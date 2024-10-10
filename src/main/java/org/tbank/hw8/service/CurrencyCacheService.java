package org.tbank.hw8.service;

import org.tbank.hw8.dto.CurrencyRateDto;

public interface CurrencyCacheService {
    CurrencyRateDto getCurrencyRate(String code);
}
