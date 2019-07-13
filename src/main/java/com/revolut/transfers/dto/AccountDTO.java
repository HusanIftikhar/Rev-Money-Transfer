package com.revolut.transfers.dto;

import com.revolut.transfers.model.Account;

import javax.money.format.AmountFormatQuery;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.util.Locale;

public class AccountDTO {

    private Long accountId;
    private String accountTitle;
    private String availableBalance;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    public static AccountDTO createAccountDTO(Account account){
        AccountDTO response = new AccountDTO();
        response.setAccountId( account.getAccountId());
        response.setAccountTitle(account.getAccountTitle());
        String amount = MonetaryFormats.getAmountFormat(Locale.US).format(account.getAvailableBalance());
        response.setAvailableBalance(amount);

        return response;


    }
}
