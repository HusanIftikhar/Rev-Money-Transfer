package com.revolut.transfers.model;

import com.revolut.transfers.enums.TransferActions;
import org.javamoney.moneta.Money;

import java.time.LocalDateTime;

public class Transactions {

    private final LocalDateTime transactionTime;
    private final TransferActions transferType;
    private final Money transferAmount;
    private final Long transferAccount;

    public Transactions(LocalDateTime transactionTime, TransferActions transferType, Money transferAmount, Long transferAccount) {
        this.transactionTime = transactionTime;
        this.transferType = transferType;
        this.transferAmount = transferAmount;
        this.transferAccount = transferAccount;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public TransferActions getTransferType() {
        return transferType;
    }

    public Money getTransferAmount() {
        return transferAmount;
    }

    public Long getTransferAccount() {
        return transferAccount;
    }
}
