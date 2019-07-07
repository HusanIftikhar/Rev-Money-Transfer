package com.revolut.transfers.services;

import com.revolut.transfers.exceptions.AccountNotFoundException;
import com.revolut.transfers.model.Account;
import com.revolut.transfers.repositories.AccountRepository;


public class TransferServiceImpl {

    private AccountRepository accountRepository;

    public Account getAccountById(Long accountId) throws AccountNotFoundException {

        return  accountRepository.getAccountById(accountId);
    }





}
