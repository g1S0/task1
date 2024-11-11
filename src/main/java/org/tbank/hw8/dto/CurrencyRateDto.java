package org.tbank.hw8.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyRateDto {
    private String currency;
    private double rate;
}
