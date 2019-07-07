package com.revolut.transfers.services;

import com.revolut.transfers.model.Account;
import com.revolut.transfers.repositories.AccountRepository;


public class TransferService {

    private AccountRepository accountRepository;

    public Account getAccountById(Long accountId) {
        return  accountRepository.getAccountById(accountId);
    }



}
