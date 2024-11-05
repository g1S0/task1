package org.tbank.hw8.exception;

public class InvalidConfirmationCodeException extends RuntimeException {
    public InvalidConfirmationCodeException(String message) {
        super(message);
    }
}
