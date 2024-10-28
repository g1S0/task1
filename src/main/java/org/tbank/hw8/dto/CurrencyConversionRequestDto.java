package org.tbank.hw8.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyConversionRequestDto {
    @NotNull(message = "fromCurrency is required")
    private String fromCurrency;

    @NotNull(message = "toCurrency is required")
    private String toCurrency;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private Double amount;
}
