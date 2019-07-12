package com.revolut.transfers.exceptions;

public class SameAccountTransferRequestException extends RuntimeException {
    public SameAccountTransferRequestException(String message) {
        super(message);
    }
}
