package com.revolut.transfers.services;

import com.revolut.transfers.enums.TransferStatus;
import com.revolut.transfers.exceptions.AccountNotFoundException;
import com.revolut.transfers.model.Account;

public interface TransferService {
    /**
     * Fetch existing account by accountId
     *
     * @param accountId
     * @return Account
     * @throws AccountNotFoundException
     **/
    Account getAccountById(Long accountId) throws AccountNotFoundException;

    /**
     * @param accountId
     * @param amount
     * @param currency
     * @return TransferStatus
     * @throws RuntimeException
     */
    TransferStatus withdrawal(Long accountId, Double amount, String currency) throws RuntimeException;

    /**
     * @param accountId
     * @param amount
     * @param currency
     * @return
     * @throws RuntimeException
     */
    TransferStatus deposit(long accountId, double amount, String currency) throws RuntimeException;

    /**
     * @param sourceAccountId
     * @param targetAccountId
     * @param amount
     * @param currencyCode
     * @return TransferStatus
     * @throws RuntimeException
     */
    TransferStatus transfer(Long sourceAccountId, Long targetAccountId, double amount, String currencyCode) throws RuntimeException;
}
