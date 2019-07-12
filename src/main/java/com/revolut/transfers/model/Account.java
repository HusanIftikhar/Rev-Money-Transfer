package com.revolut.transfers.model;

import com.revolut.transfers.enums.TransferActions;
import org.javamoney.moneta.Money;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    private final Long accountId;
    private final String accountTitle;
    private Money availableBalance;
    private final List<Transactions> tranactionHistory;
    private Lock accountLock = new ReentrantLock();


    public Account(Long accountId, String accountTitle, double availableBalance) {
        this.accountId = accountId;
        this.accountTitle = accountTitle;
        this.availableBalance = Money.of(availableBalance, "USD");
        this.tranactionHistory = new CopyOnWriteArrayList<>();
    }


    public int getTransactionHistorySize(){
        return tranactionHistory.size();
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public Money getAvailableBalance() {
        return availableBalance;
    }


    private void withdrawal(Money amount) {

        this.availableBalance = availableBalance.subtract(amount);

    }

    private void deposit(Money amount) {

        this.availableBalance = availableBalance.add(amount);
    }
    
    public void updateAmount(Money amount, TransferActions transferAction) {

        try {
            accountLock.lock();
            if (TransferActions.WITHDRAWAL == transferAction) {
                withdrawal(amount);
            } else {
                deposit(amount);
            }
        } finally {
            accountLock.unlock();
        }


    }
}


