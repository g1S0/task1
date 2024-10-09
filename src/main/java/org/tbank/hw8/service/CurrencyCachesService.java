package org.tbank.hw8.service;

import org.tbank.hw8.dto.CurrencyRateDto;

public interface CurrencyCachesService {
    CurrencyRateDto getCurrencyRate(String code);
}
