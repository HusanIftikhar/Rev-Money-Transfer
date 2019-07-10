package com.revolut.transfers.exceptions;

public class InvalidCurrencyCodeException extends RuntimeException {
    public InvalidCurrencyCodeException(String message) {

        super(message);
    }
}
