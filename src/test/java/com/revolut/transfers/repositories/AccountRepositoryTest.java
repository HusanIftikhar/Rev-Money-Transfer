package com.revolut.transfers.repositories;

import com.revolut.transfers.exceptions.AccountNotFoundException;
import com.revolut.transfers.model.Account;
import com.revolut.transfers.utils.AccountsDatabase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class AccountRepositoryTest {
    @InjectMocks
    AccountRepositoryImpl accountRepository;
    @Mock
    AccountsDatabase accountsDatabase;
    private long validAccountId1ForTest = 70001L;
    private Account testAccount= new Account(validAccountId1ForTest, "Iftikhar Hussain", 50.00);;


    @Test    @DisplayName("Should return a valid account when valid accountId is passed to method")
    void testGetAccountBYIdWithValidAccountId(){

        when(accountsDatabase.getAccount(validAccountId1ForTest)).thenReturn(testAccount);
        Account account =accountRepository.getAccountById(validAccountId1ForTest);
        assertNotNull(account);




    }

    @Test
    @DisplayName("should throw account not found exception if invalid id is passed to method")
    void testGetAccountByIdWithInvalidAccountId(){
        when(accountsDatabase.getAccount(Long.MAX_VALUE)).thenReturn(null);
        assertThatThrownBy(()->accountRepository.getAccountById(Long.MAX_VALUE)).isInstanceOf(AccountNotFoundException.class);

    }







}
