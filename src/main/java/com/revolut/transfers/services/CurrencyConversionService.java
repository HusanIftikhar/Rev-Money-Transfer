package com.revolut.transfers.services;

import org.javamoney.moneta.Money;

public interface CurrencyConversionService {

     Money validateAndConcvertCurrency(double amount , String currencyCode);
}
