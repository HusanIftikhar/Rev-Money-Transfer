package com.revolut.transfers.exceptions;

public class UnSupportedCurrencyException extends RuntimeException {
    public UnSupportedCurrencyException(String message){

        super(message);
    }}