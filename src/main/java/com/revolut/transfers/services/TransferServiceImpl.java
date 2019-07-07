package com.revolut.transfers.services;

import com.revolut.transfers.exceptions.AccountNotFoundException;
import com.revolut.transfers.model.Account;
import com.revolut.transfers.repositories.AccountRepository;

import java.math.BigDecimal;

public class TransferServiceImpl implements TransferService {

    private AccountRepository accountRepository;
    @Override
    public Account getAccountById(Long accountId) throws AccountNotFoundException {

        return  accountRepository.getAccountById(accountId);
    }

    @Override
    public Double withdrawal(Long accountId, Double amount, String currency) {
        return null;
    }


}
