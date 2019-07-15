package com.revolut.transfers.services;

import com.google.inject.Inject;
import com.revolut.transfers.enums.TransferStatus;
import com.revolut.transfers.enums.TransferType;
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
    /**
    * @Description
     * Fetch account from storage by account id
    *
    * @Throws AccountNotFoundException if account is not available
    *
    * @see  com.revolut.transfers.exceptions.AccountNotFoundException
    *
    * */
    @Override
    public Account getAccountById(Long accountId) throws AccountNotFoundException {
        return accountRepository.getAccountById(accountId);
    }

    /**
     *

     *
     * @description
     * 1) check if #amount is not - or 0 throw Invalid amount exception if invalid
     * @see InvalidAmountException
     *
     * 2)Get account from storage. if account not available throws AccountNotFoundException
     *
     * 3) convert request amount to USD and compare in requested amount throws {@link UnSufficientFundException}
     * also
     * @see #getAmountInUSD() in the same class
     *
     *  then calls the update method of account object
     * @see Account#updateAmount(Money, TransferType)
     *
     * update the data in storage by {@link #accountRepository }
     * @return {@link TransferStatus#SUCCESS} if successful
     *
     */

    @Override
    public TransferStatus withdrawal(Long accountId, Double amount, String currency) throws RuntimeException {
        if (amount <= 0) {
            throw new InvalidAmountException(ExceptionConstants.INVALID_AMOUNT_EXCEPTION_MESSAGE);
        }
        Account account = accountRepository.getAccountById(accountId);
        if (account.getAvailableBalance().isLessThan(convertDoubleToMoney(amount, currency))) {
            throw new UnSufficientFundException(ExceptionConstants.NOT_ENOUGH_FUNDS_EXCEPTION_MESSAGE);


        }
        account.updateAmount(convertDoubleToMoney(amount, currency), TransferType.WITHDRAWAL);
        accountRepository.updateAccount(accountId, account);
        return TransferStatus.SUCCESS;
    }

    /**
     *
     *
     * @description :
     *     same as the {@link #withdrawal(Long, Double, String)} method
     *     only exception not thrown by this method is {@link UnSufficientFundException}
     *
     *
     */



    @Override
    public TransferStatus deposit(long accountId, double amount, String currency) {
        if (amount <= 0) {
            throw new InvalidAmountException(ExceptionConstants.INVALID_AMOUNT_EXCEPTION_MESSAGE);

        }
        Account account = accountRepository.getAccountById(accountId);
        account.updateAmount(convertDoubleToMoney(amount, currency), TransferType.DEPOSIT);
        accountRepository.updateAccount(accountId, account);
        return TransferStatus.SUCCESS;
    }

    /**
    * @description
    * checks @param sourceAccountId and targetAccountId throw {@link SameAccountTransferRequestException}
    * if source and target accounts are same
    *
     * uses {@link #withdrawal(Long, Double, String)} and {@link #deposit(long, double, String)}
     * for both sourceAccountId and targetAccountId to update
     *
     *
    * */

    @Override
    public TransferStatus transfer(Long sourceAccountId, Long targetAccountId, double amount, String currencyCode) {
        if (sourceAccountId.equals(targetAccountId)) {

            throw new SameAccountTransferRequestException(ExceptionConstants.SAME_SOURCE_TARGET_ACCOUNT_EXCEPTION_MESSAGE);
        }

        TransferStatus status = withdrawal(sourceAccountId, amount, currencyCode);
        if (TransferStatus.SUCCESS == status) {
            status = deposit(targetAccountId, amount, currencyCode);
        }

        return status;
    }

    /**
    *
    * {@link BiFunction} accepts {@link Double } and {@link String} currency code
    * @return Double amount value in #US_CURRENCY_CODE
    *
    * */
    private BiFunction<Double, String, Double> getAmountInUSD() {
        return (amount, currencyCode) -> {

            double inDollars;
            if (currencyCode.equalsIgnoreCase(ApplicationConstants.US_CURRENCY_CODE)) {
                inDollars = Money.of(amount, currencyCode).getNumber().doubleValue();
            } else {

                CurrencyConversion dollarConversion = MonetaryConversions.getConversion(ApplicationConstants.US_CURRENCY_CODE);
                MonetaryAmount requestCurrency = Money.of(amount, currencyCode);
                inDollars = requestCurrency.with(dollarConversion).getNumber().doubleValue();

            }
            return inDollars;
        };

    }

    /**
    * convert double value to @{@link Money}
    * */
    private Money convertDoubleToMoney(Double amount, String currency) {
        return Money.of(this.getAmountInUSD().apply(amount, currency), ApplicationConstants.US_CURRENCY_CODE);
    }

}
