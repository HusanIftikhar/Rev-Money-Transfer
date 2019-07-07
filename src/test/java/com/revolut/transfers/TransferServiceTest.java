package com.revolut.transfers;

import com.revolut.transfers.model.Account;
import com.revolut.transfers.services.TransferService;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


 class TransferServiceTest {


    private TransferService transferService;
    @DisplayName("Should pass if valid account id is passed")
    @Test
     void testGetAccountFromDbById(){
        Account account =transferService.getAccountById(1L);
        Assert.assertNotNull(account);

    }

}
