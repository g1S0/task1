package org.tbank.hw8.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConvertedAmountDto {
    private String fromCurrency;
    private String toCurrency;
    private double convertedAmount;
}
