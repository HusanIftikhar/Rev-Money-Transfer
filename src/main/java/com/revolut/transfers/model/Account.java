package com.revolut.transfers.model;

import java.math.BigDecimal;

public class Account {

    private final Long accountId;
    private final String accountTitle;
    private  BigDecimal availableBalance;

    public Account(Long accountId, String accountTitle, BigDecimal availableBalance) {
        this.accountId = accountId;
        this.accountTitle = accountTitle;
        this.availableBalance = availableBalance;
    }


    public Long getAccountId() {
        return accountId;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }


}
