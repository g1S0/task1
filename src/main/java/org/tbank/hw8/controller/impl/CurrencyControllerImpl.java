package org.tbank.hw8.controller.impl;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CurrencyRateDto> getCurrencyRate(String code) {
        final CurrencyRateDto currencyRate = currencyService.getCurrencyRate(code);
        return new ResponseEntity<>(currencyRate, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ConvertedAmountDto> convertCurrency(CurrencyConversionRequestDto request) {
        final ConvertedAmountDto convertedAmountDto = currencyService.convertCurrency(request);
        return new ResponseEntity<>(convertedAmountDto, HttpStatus.OK);

    }
}
