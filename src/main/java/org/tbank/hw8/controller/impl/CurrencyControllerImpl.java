package org.tbank.hw8.controller.impl;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.tbank.hw8.controller.CurrencyController;
import org.tbank.hw8.dto.ConvertedAmountDto;
import org.tbank.hw8.dto.CurrencyConversionRequestDto;
import org.tbank.hw8.dto.CurrencyRateDto;
import org.tbank.hw8.service.CurrencyService;

@RestController
@AllArgsConstructor
public class CurrencyControllerImpl implements CurrencyController {

    private final CurrencyService currencyService;

    @Override
    public CurrencyRateDto getCurrencyRate(String code) {
        return currencyService.getCurrencyRate(code);
    }

    @Override
    public ConvertedAmountDto convertCurrency(CurrencyConversionRequestDto request) {
        return currencyService.convertCurrency(request);
    }
}
