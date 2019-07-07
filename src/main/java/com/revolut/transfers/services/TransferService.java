package com.revolut.transfers.services;

import com.revolut.transfers.exceptions.AccountNotFoundException;
import com.revolut.transfers.model.Account;

public interface TransferService {
    Account getAccountById(Long accountId) throws AccountNotFoundException;
}
