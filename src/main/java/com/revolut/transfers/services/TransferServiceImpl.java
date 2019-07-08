package com.revolut.transfers.services;

import com.revolut.transfers.enums.TransferActions;
import com.revolut.transfers.enums.TransferStatus;
import com.revolut.transfers.exceptions.AccountNotFoundException;
import com.revolut.transfers.exceptions.InvalidAmountException;
import com.revolut.transfers.model.Account;
import com.revolut.transfers.repositories.AccountRepository;
import org.javamoney.moneta.Money;

public class TransferServiceImpl implements TransferService {

    private AccountRepository accountRepository;
    @Override
    public Account getAccountById(Long accountId) throws AccountNotFoundException {
        return  accountRepository.getAccountById(accountId);
    }

    @Override
    public TransferStatus withdrawal(Long accountId, Double amount, String currency) throws RuntimeException {
        if(amount <= 0){
            throw new InvalidAmountException("Invalid amount should be greater than 0");
        }

        Account account = accountRepository.getAccountById(accountId);
        TransferStatus transferStatus = TransferStatus.SUCCESS;
        Money withdrawalAmount  = Money.of(amount,currency);
        account.updateAmount(withdrawalAmount, TransferActions.WITHDRAWAL);
        accountRepository.updateAccount(accountId,account);
        return transferStatus;
    }


}
