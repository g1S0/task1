package org.tbank.hw5.client;

import org.springframework.stereotype.Component;

@Component
public class CurrencyClient {
    public double convertCurrency(double amount) {
        return amount * 10;
    }
}
