package com.revolut.transfers;

import com.revolut.transfers.model.Account;
import com.revolut.transfers.repositories.AccountRepository;
import com.revolut.transfers.services.TransferService;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.AdditionalMatchers.gt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {


    @InjectMocks
    private TransferService transferService;
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






}
