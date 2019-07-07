package com.revolut.transfers;


import com.revolut.transfers.exceptions.AccountNotFoundException;
import com.revolut.transfers.model.Account;
import com.revolut.transfers.repositories.AccountRepository;
import com.revolut.transfers.services.TransferServiceImpl;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import static org.mockito.AdditionalMatchers.gt;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TransferServiceTest {


    @InjectMocks
    private TransferServiceImpl transferService;
    @Mock
    private AccountRepository accountRepository;


    @DisplayName("Should pass if valid account id is passed")
    @Test
     void testGetAccountFromDbById(){
        when(accountRepository.getAccountById(gt(0L))).thenReturn(new Account(1L,"Iftikhar hussain", BigDecimal.ZERO));
        Account account =transferService.getAccountById(1L);
        Assert.assertNotNull(account);
        Assert.assertTrue(account.getAccountId() > 0);

    }
    @DisplayName("Should throw exception if account is not found ")
    @Test
    void testGetAccountByIdThrowExceptionIfAccountNotFound(){
        when(accountRepository.getAccountById(Long.MAX_VALUE)).thenThrow(new AccountNotFoundException("Account not found for accountId:"+ Long.MAX_VALUE));
         Assertions.assertThrows(AccountNotFoundException.class,()->transferService.getAccountById(Long.MAX_VALUE),
                "Expected throw AccountNotFoundException but it didn't throw");
        assertThatThrownBy(()->transferService.getAccountById(Long.MAX_VALUE)).hasMessage("Account not found for accountId:"+ Long.MAX_VALUE);





    }





}
