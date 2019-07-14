package com.revolut.transfers.utils;

import com.revolut.transfers.model.Account;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class AccountsDatabase {

    private static final Map<Long, Account> accountsTable = new ConcurrentHashMap<>();
    private static final AccountsDatabase ACCOUNTS_DATABASE = new AccountsDatabase();

    private AccountsDatabase() {
        accountsTable.put(70001L, new Account(70001L, "Iftikhar Hussain", 500.00));
        accountsTable.put(70002L, new Account(70002L, "Julie Shales", 1450.00));
        accountsTable.put(70003L, new Account(70003L, "Ujjval Thakkur", 510.00));
        accountsTable.put(70004L, new Account(70004L, "Nirmeen Ch", 1150.00));
        accountsTable.put(70005L, new Account(70005L, "Aftab Aslam", 150.00));
        accountsTable.put(70006L, new Account(70006L, "Vilad D", 50.00));
        accountsTable.put(70007L, new Account(70007L, "Maria Fitzgerald", 250.00));
        accountsTable.put(70008L, new Account(70008L, "Dr. Jack Amore", 50.78));
        accountsTable.put(70009L, new Account(70009L, "Van Tibolli", 7750.00));
        accountsTable.put(70010L, new Account(70010L, "Chris Flayer", 1000.00));
    }

    public static AccountsDatabase getInstance() {
        return ACCOUNTS_DATABASE;
    }


    public Account getAccount(Long accountId) {
        return accountsTable.get(accountId);
    }

    public boolean updateAccount(final Account account) {
        return Objects.isNull(accountsTable.computeIfPresent(account.getAccountId(), (key, value) -> account));
    }

}
