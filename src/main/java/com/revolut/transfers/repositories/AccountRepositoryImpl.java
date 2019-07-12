package com.revolut.transfers.repositories;

import com.google.inject.Inject;
import com.revolut.transfers.exceptions.AccountNotFoundException;
import com.revolut.transfers.exceptions.ExceptionConstants;
import com.revolut.transfers.model.Account;
import com.revolut.transfers.utils.AccountsDatabase;

import java.util.Objects;

public class AccountRepositoryImpl implements AccountRepository{

    @Inject
    private AccountsDatabase accountsDatabase;

    @Override
    public Account getAccountById(Long id) throws AccountNotFoundException {

        Account account = accountsDatabase.getAccount(id);
        if(Objects.isNull(account)) throw new AccountNotFoundException(ExceptionConstants.ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE);
        return account;

    }

    @Override
    public void updateAccount(Long accountId, Account account) {

    }
}
