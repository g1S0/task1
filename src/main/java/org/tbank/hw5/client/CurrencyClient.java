package org.tbank.hw5.client;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CurrencyClient {
    public BigDecimal convertCurrency(BigDecimal amount) {
        BigDecimal conversionRate = BigDecimal.valueOf(10);
        return amount.multiply(conversionRate);
    }
}
