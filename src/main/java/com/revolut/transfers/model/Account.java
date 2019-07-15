package com.revolut.transfers.model;

import com.revolut.transfers.enums.TransferType;
import org.javamoney.moneta.Money;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    private final Long accountId;
    private final String accountTitle;
    private Money availableBalance;
    private final List<Transaction> transactionHistory;
    private Lock accountLock = new ReentrantLock();


    public Account(Long accountId, String accountTitle, double availableBalance) {
        this.accountId = accountId;
        this.accountTitle = accountTitle;
        this.availableBalance = Money.of(availableBalance, "USD");
        this.transactionHistory = new CopyOnWriteArrayList<>();
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public int getTransactionHistorySize(){
        return transactionHistory.size();
    }

    public Long getAccountId() {
        return accountId;
    }


    public List<Transaction> getTransactionHistory(){

        return Collections.unmodifiableList(transactionHistory);

    }
    public Money getAvailableBalance() {
        return availableBalance;
    }

    /**
     *
     *
     * subtract money
     *
     *
     * */
    private void withdrawal(Money amount) {

        this.availableBalance = availableBalance.subtract(amount);

    }

    /**
     *
     *
     *
     * @param amount
     *
     * add amount if its deposit request
     *
     */

    private void deposit(Money amount) {

        this.availableBalance = availableBalance.add(amount);
    }
    /**
     *
     * declare boolean flag to check if account update is successful
     * call {@link #withdrawal(Money)} id is withdrawal request
     * call {@link #deposit(Money)} if deposit request
     * if either of this process successful it add the transaction into {@link #transactionHistory}
     * with status withdrawal or deposit
     * NOTE : vert.x environment is by default is low concurrency
     * vert.x supports reactive model it single event thread per core
     * but for the edge cases locked the object during modification
     * with java concurrent locks
     * @see #accountLock
     * */
    public void updateAmount(Money amount, TransferType transferAction) {
            boolean success ;
        try {
            accountLock.lock();
            if (TransferType.WITHDRAWAL == transferAction) {
                withdrawal(amount);
                success=true;
            } else {
                deposit(amount);
                success=true;
            }
            if(success) {
                this.transactionHistory.add(new Transaction(LocalDateTime.now(), transferAction, amount));
            }
            } finally {
            accountLock.unlock();
        }


    }
}


