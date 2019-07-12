package com.revolut.transfers.exceptions;

public class UnSufficientFundException extends RuntimeException {

    public UnSufficientFundException(String  message){
        super(message);
    }
}
