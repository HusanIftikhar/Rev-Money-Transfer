package com.revolut.transfers.services;

import com.google.inject.Inject;
import com.revolut.transfers.enums.TransferType;
import com.revolut.transfers.enums.TransferStatus;
import com.revolut.transfers.exceptions.*;
import com.revolut.transfers.model.Account;
import com.revolut.transfers.repositories.AccountRepository;
import com.revolut.transfers.utils.ApplicationConstants;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;
import java.util.function.BiFunction;

public class TransferServiceImpl implements TransferService {

    @Inject
    private AccountRepository accountRepository;

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
        if(account.getAvailableBalance().isLessThan(convertDoubleToMoney(amount, currency))){
            throw  new UnSufficientFundException(ExceptionConstants.NOT_ENOUGH_FUNDS_EXCEPTION_MESSAGE);


        }
        account.updateAmount(convertDoubleToMoney(amount, currency), TransferType.WITHDRAWAL);
        accountRepository.updateAccount(accountId,account);
        return TransferStatus.SUCCESS;
    }


    @Override
    public TransferStatus deposit(long accountId, double amount, String currency) {
        if(amount<=0){
            throw new InvalidAmountException(ExceptionConstants.INVALID_AMOUNT_EXCEPTION_MESSAGE);

        }
        Account account = accountRepository.getAccountById(accountId);
        account.updateAmount(convertDoubleToMoney(amount, currency), TransferType.DEPOSIT);
        accountRepository.updateAccount(accountId,account);
        return TransferStatus.SUCCESS;
 }

@Override
 public TransferStatus transfer(Long sourceAccountId, Long targetAccountId, double amount, String currencyCode){
        if(sourceAccountId.equals(targetAccountId)){

            throw new SameAccountTransferRequestException(ExceptionConstants.SAME_SOURCE_TARGET_ACCOUNT_EXCEPTION_MESSAGE);
        }

        TransferStatus status = withdrawal(sourceAccountId,amount,currencyCode);
        if(TransferStatus.SUCCESS==status){
            status = deposit(targetAccountId,amount,currencyCode);
        }

        return status;
 }


   private  BiFunction<Double,String,Double> getAmountInUSD() {
    return     (amount, currencyCode) -> {

            double inDollars ;
           if (currencyCode.equalsIgnoreCase(ApplicationConstants.US_CURRENCY_CODE)) {
            inDollars= Money.of(amount, currencyCode).getNumber().doubleValue();
           } else {

               CurrencyConversion dollarConversion = MonetaryConversions.getConversion(ApplicationConstants.US_CURRENCY_CODE);
               MonetaryAmount requestCurrency = Money.of(amount, currencyCode);
               inDollars = requestCurrency.with(dollarConversion).getNumber().doubleValue();

           }
           return inDollars;
       };

   }

    //convert double amount to Money in USD
    private Money convertDoubleToMoney(Double amount, String currency) {
        return Money.of(this.getAmountInUSD().apply(amount, currency), ApplicationConstants.US_CURRENCY_CODE);
    }

}
