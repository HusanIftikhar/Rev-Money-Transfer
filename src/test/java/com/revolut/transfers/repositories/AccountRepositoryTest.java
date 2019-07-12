package com.revolut.transfers.repositories;

import com.revolut.transfers.model.Account;
import com.revolut.transfers.utils.AccountsDatabase;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

 class AccountRepositoryTest {
    @InjectMocks
    AccountRepositoryImpl accountRepository;
    @Mock
    AccountsDatabase accountsDatabase;
    @Spy
    AccountsDatabase underTest = AccountsDatabase.getInstance();
    private long validAccountId1ForTest = 70001L;

    @Test
    void testGetAccountBYId(){


        when(accountsDatabase.getAccount(validAccountId1ForTest)).thenReturn(underTest.getAccount(validAccountId1ForTest));
        Account account =accountRepository.getAccountById(validAccountId1ForTest);
        assertNotNull(account);

    }



}
