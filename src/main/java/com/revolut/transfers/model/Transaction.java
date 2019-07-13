package com.revolut.transfers.model;

import com.revolut.transfers.enums.TransferType;
import org.javamoney.moneta.Money;

import java.time.LocalDateTime;

public class Transaction {

    private final LocalDateTime transactionTime;
    private final TransferType transferType;
    private final Money transferAmount;


    public Transaction(LocalDateTime transactionTime, TransferType transferType, Money transferAmount) {
        this.transactionTime = transactionTime;
        this.transferType = transferType;
        this.transferAmount = transferAmount;

    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public TransferType getTransferType() {
        return transferType;
    }

    public Money getTransferAmount() {
        return transferAmount;
    }


}
