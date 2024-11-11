package org.tbank.hw10.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class RelatedEntityNotFoundException extends RuntimeException {
    public RelatedEntityNotFoundException(String message) {
        super(message);
    }
}