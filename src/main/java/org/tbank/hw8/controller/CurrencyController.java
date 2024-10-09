package org.tbank.hw8.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tbank.hw8.dto.ConvertedAmountDto;
import org.tbank.hw8.dto.CurrencyRateDto;
import org.tbank.hw8.dto.CurrencyConversionRequestDto;

@RequestMapping("/currencies")
public interface CurrencyController {

    @GetMapping("/rates/{code}")
    ResponseEntity<CurrencyRateDto> getCurrencyRate(@Valid @PathVariable String code);

    @PostMapping("/convert")
    ResponseEntity<ConvertedAmountDto> convertCurrency(@Valid @RequestBody CurrencyConversionRequestDto request);
}