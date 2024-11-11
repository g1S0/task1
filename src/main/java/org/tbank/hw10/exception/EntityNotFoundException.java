package org.tbank.hw10.exception;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
