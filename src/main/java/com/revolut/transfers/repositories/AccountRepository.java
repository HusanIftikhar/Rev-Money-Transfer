package com.revolut.transfers.repositories;

import com.revolut.transfers.exceptions.AccountNotFoundException;
import com.revolut.transfers.model.Account;
import org.javamoney.moneta.Money;

public interface AccountRepository {


     Account getAccountById(Long id) throws AccountNotFoundException;

     void updateAccount(Long accountId, Account account);
}
