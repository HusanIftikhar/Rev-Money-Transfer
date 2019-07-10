package com.revolut.transfers;


import com.revolut.transfers.enums.TransferStatus;
import com.revolut.transfers.exceptions.AccountNotFoundException;
import com.revolut.transfers.exceptions.ExceptionConstants;
import com.revolut.transfers.exceptions.InvalidAmountException;
import com.revolut.transfers.model.Account;
import com.revolut.transfers.repositories.AccountRepository;
import com.revolut.transfers.services.CurrencyConversionService;
import com.revolut.transfers.services.TransferServiceImpl;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.gt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    private static final double TEST_AMOUNT_GBP = 20.00;
    private Account testAccount = new Account(1L, "Iftikhar Hussain", 100.00);
    @InjectMocks
    private TransferServiceImpl transferService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CurrencyConversionService currencyService;


    @DisplayName("Should pass if valid account id is passed")
    @Test
     void testGetAccountFromDbById(){
        when(accountRepository.getAccountById(gt(0L))).thenReturn(testAccount);
        Account account =transferService.getAccountById(1L);
        assertNotNull(account);
        assertTrue(account.getAccountId() > 0);

    }
    @DisplayName("Should throw exception if account is not found ")
    @Test
    void testGetAccountByIdThrowExceptionIfAccountNotFound(){
        when(accountRepository.getAccountById(Long.MAX_VALUE)).thenThrow(new AccountNotFoundException(ExceptionConstants.ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE + Long.MAX_VALUE));
         assertThrows(AccountNotFoundException.class,()->transferService.getAccountById(Long.MAX_VALUE),
                "Expected throw AccountNotFoundException but it didn't throw");
        assertThatThrownBy(()->transferService.getAccountById(Long.MAX_VALUE)).hasMessage(ExceptionConstants.ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE + Long.MAX_VALUE);
    }


    @DisplayName("should withdrawal amount from account if account and withdrawal amount is valid")
    @Test
    void testWithdrawValidAmountFromAccount(){

        when(accountRepository.getAccountById(1L)).thenReturn(testAccount);
        doNothing().when(accountRepository).updateAccount(testAccount.getAccountId(),testAccount);
        TransferStatus status= transferService.withdrawal(1L,10.00,"USD");
         assertEquals(Money.of(90,"USD"),testAccount.getAvailableBalance());
         assertEquals(TransferStatus.SUCCESS,status);
    }

    @DisplayName(("should throw InvalidAmountException if withdrawal amount is 0 or negative"))
    @Test
    void testWithdrawalInvalidAmount(){
       assertThatThrownBy(()->transferService.withdrawal(1L,0.00,"USD"))
         .isInstanceOf(InvalidAmountException.class);
        assertThatThrownBy(()->transferService.withdrawal(1L,-10.00,"USD"))
                .isInstanceOf(InvalidAmountException.class);

    }
    @DisplayName("should throw AccountNotFoundException if account is not valid")
    @Test
    void testWithdrawalWithInvalidAccountAndValidAmount(){
        when(accountRepository.getAccountById(Long.MAX_VALUE)).thenThrow(new AccountNotFoundException(ExceptionConstants.ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE + Long.MAX_VALUE));
        assertThatThrownBy(()->transferService.withdrawal(Long.MAX_VALUE,10.00,"USD")).isInstanceOf(AccountNotFoundException.class);
    }

    @DisplayName("should pass when currency code is valid currency code and is supported currencies by application ")
    @Test
    void testShouldPassIfCurrencyIsValidIsOfSupportedCurrencies(){
       when(accountRepository.getAccountById(1L)).thenReturn(testAccount);
        when(currencyService.validateAndConcvertCurrency(TEST_AMOUNT_GBP,"GBP")).thenReturn(Money.of(10,"USD"));
        TransferStatus status= transferService.withdrawal(1L,10.00,"USD");
        assertEquals(TransferStatus.SUCCESS,status);
        status= transferService.withdrawal(1L,TEST_AMOUNT_GBP,"GBP");
        assertEquals(TransferStatus.SUCCESS,status);

    }










}
