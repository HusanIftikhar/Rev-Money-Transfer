package com.revolut.transfers.repositories;

import com.google.inject.Inject;
import com.revolut.transfers.exceptions.AccountNotFoundException;
import com.revolut.transfers.model.Account;
import com.revolut.transfers.utils.AccountsDatabase;

public class AccountRepositoryImpl implements AccountRepository{

    @Inject
    private AccountsDatabase accountsDatabase;

    @Override
    public Account getAccountById(Long id) throws AccountNotFoundException {
        return null;
    }

    @Override
    public void updateAccount(Long accountId, Account account) {

    }
}
