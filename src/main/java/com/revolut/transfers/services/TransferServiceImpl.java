package com.revolut.transfers.services;

import com.revolut.transfers.enums.TransferActions;
import com.revolut.transfers.enums.TransferStatus;
import com.revolut.transfers.exceptions.AccountNotFoundException;
import com.revolut.transfers.exceptions.ExceptionConstants;
import com.revolut.transfers.exceptions.InvalidAmountException;
import com.revolut.transfers.model.Account;
import com.revolut.transfers.repositories.AccountRepository;
import org.javamoney.moneta.Money;

import javax.money.MonetaryContext;
import javax.money.MonetaryContextBuilder;

public class TransferServiceImpl implements TransferService {

    private AccountRepository accountRepository;
    private CurrencyConversionService currencyService;
    @Override
    public Account getAccountById(Long accountId) throws AccountNotFoundException {
        return  accountRepository.getAccountById(accountId);
    }

    @Override
    public TransferStatus withdrawal(Long accountId, Double amount, String currency) throws RuntimeException {
        if(amount <= 0){
            throw new InvalidAmountException(ExceptionConstants.INVALID_AMOUNT_EXCEPTION_MESSAGE);
        }
        Account account = accountRepository.getAccountById(accountId);
        Money withdrawalAmount;
        if(currency.equalsIgnoreCase("USD")) {
             withdrawalAmount = Money.of(amount, currency.toUpperCase());
        }else{
            withdrawalAmount = currencyService.validateAndConcvertCurrency(amount,currency);

        }
        //TODO: throw insufficient amount exception if withdrawal amount is greater then available amount
         account.updateAmount(withdrawalAmount, TransferActions.WITHDRAWAL);
        accountRepository.updateAccount(accountId,account);
        return TransferStatus.SUCCESS;
    }

    @Override
    public TransferStatus deposit(long accountId, double amount, String currency) {
        if(amount<=0){
            throw new InvalidAmountException(ExceptionConstants.INVALID_AMOUNT_EXCEPTION_MESSAGE);

        }
        Account account = accountRepository.getAccountById(accountId);

        Money depositAmount;

        if(currency.equalsIgnoreCase("USD")){
            MonetaryContext monetaryContext = MonetaryContextBuilder.of().setPrecision(4).build();
            depositAmount = Money.of(amount,currency);
        }else{
        depositAmount=    currencyService.validateAndConcvertCurrency(amount,currency.toUpperCase());
        }

        account.updateAmount(depositAmount, TransferActions.DEPOSIT);
        accountRepository.updateAccount(accountId,account);
        return TransferStatus.SUCCESS;


    }
}
