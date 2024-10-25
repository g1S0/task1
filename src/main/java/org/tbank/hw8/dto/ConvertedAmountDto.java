package org.tbank.hw8.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConvertedAmountDto {
    private String fromCurrency;
    private String toCurrency;
    private double convertedAmount;
}
