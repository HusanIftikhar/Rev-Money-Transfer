package com.revolut.transfers.repositories;

import com.revolut.transfers.exceptions.AccountNotFoundException;
import com.revolut.transfers.model.Account;

public interface AccountRepository {


     Account getAccountById(Long id) throws AccountNotFoundException;

     void updateAccount(Long accountId, Account account);
}
