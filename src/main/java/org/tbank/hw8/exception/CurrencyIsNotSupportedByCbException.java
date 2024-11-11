package org.tbank.hw8.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CurrencyIsNotSupportedByCbException extends RuntimeException {
    public CurrencyIsNotSupportedByCbException(String message) {
        super(message);
    }
}
