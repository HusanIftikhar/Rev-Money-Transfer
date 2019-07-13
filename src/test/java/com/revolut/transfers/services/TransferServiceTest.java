package com.revolut.transfers.services;


import com.revolut.transfers.enums.TransferStatus;
import com.revolut.transfers.exceptions.*;
import com.revolut.transfers.model.Account;
import com.revolut.transfers.repositories.AccountRepository;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.revolut.transfers.utils.ApplicationConstants.UK_CURRENCY_CODE;
import static com.revolut.transfers.utils.ApplicationConstants.US_CURRENCY_CODE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalMatchers.gt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    private static final double TEST_AMOUNT_GBP = 20.00;
    private Account testAccount1 = new Account(1L, "Iftikhar Hussain", 100.00);
    private Account testAccount2 = new Account(2L, "Nadir ", 50.00);
    @InjectMocks
    private TransferServiceImpl transferService;
    @Mock
    private AccountRepository accountRepository;
    private AccountNotFoundException accountNotFoundException =
            new AccountNotFoundException(ExceptionConstants.ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE + Long.MAX_VALUE);


    @DisplayName("Should pass if valid account id is passed")
    @Test
    void testGetAccountFromDbById() {
        when(accountRepository.getAccountById(gt(0L))).thenReturn(testAccount1);
        Account account = transferService.getAccountById(1L);
        assertNotNull(account);
        assertTrue(account.getAccountId() > 0);

    }

    @DisplayName("Should throw exception if account is not found ")
    @Test
    void testGetAccountByIdThrowExceptionIfAccountNotFound() {
        when(accountRepository.getAccountById(Long.MAX_VALUE)).thenThrow(new AccountNotFoundException(ExceptionConstants.ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE + Long.MAX_VALUE));
        assertThrows(AccountNotFoundException.class, () -> transferService.getAccountById(Long.MAX_VALUE),
                "Expected throw AccountNotFoundException but it didn't throw");
        assertThatThrownBy(() -> transferService.getAccountById(Long.MAX_VALUE)).hasMessage(ExceptionConstants.ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE + Long.MAX_VALUE);
    }


    @DisplayName("should withdrawal amount from account if account and withdrawal amount is valid")
    @Test
    void testWithdrawValidAmountFromAccount() {

        when(accountRepository.getAccountById(1L)).thenReturn(testAccount1);
        doNothing().when(accountRepository).updateAccount(testAccount1.getAccountId(), testAccount1);
        TransferStatus status = transferService.withdrawal(1L, 10.00, US_CURRENCY_CODE);
        assertEquals(Money.of(90, US_CURRENCY_CODE), testAccount1.getAvailableBalance());
        assertEquals(TransferStatus.SUCCESS, status);
    }

    @DisplayName(("should throw InvalidAmountException if withdrawal amount is 0 or negative"))
    @Test
    void testWithdrawalInvalidAmount() {
        assertThatThrownBy(() -> transferService.withdrawal(1L, 0.00, US_CURRENCY_CODE))
                .isInstanceOf(InvalidAmountException.class);
        assertThatThrownBy(() -> transferService.withdrawal(1L, -10.00, US_CURRENCY_CODE))
                .isInstanceOf(InvalidAmountException.class);

    }


    @DisplayName("should throw AccountNotFoundException if account is not valid")
    @Test
    void testWithdrawalWithInvalidAccountAndValidAmount() {

        when(accountRepository.getAccountById(Long.MAX_VALUE)).thenThrow(accountNotFoundException);
        assertThatThrownBy(() -> transferService.withdrawal(Long.MAX_VALUE, 10.00, US_CURRENCY_CODE)).isInstanceOf(AccountNotFoundException.class);
    }

    @DisplayName("should pass when currency code is valid currency code and is supported currencies by application ")
    @Test
    void testAccountWithdrawalShouldPassIfCurrencyIsValidIsOfSupportedCurrencies() {
        when(accountRepository.getAccountById(1L)).thenReturn(testAccount1);
        TransferStatus status = transferService.withdrawal(1L, 10.00, US_CURRENCY_CODE);
        assertEquals(TransferStatus.SUCCESS, status);
        status = transferService.withdrawal(1L, TEST_AMOUNT_GBP, UK_CURRENCY_CODE);
        assertEquals(TransferStatus.SUCCESS, status);

    }


    @DisplayName("should pass when amount is valid and account is valid should throw exception if account or amount is not valid")
    @Test
    void testDepositAmountIntoExistingAccount() {
        when(accountRepository.getAccountById(1L)).thenReturn(testAccount1);
        TransferStatus status = transferService.deposit(1L, 10.00, US_CURRENCY_CODE);
        assertEquals(TransferStatus.SUCCESS, status);
        status = transferService.deposit(1L, 10.00,UK_CURRENCY_CODE);

        assertEquals(TransferStatus.SUCCESS, status);
    }

    @DisplayName("should throws invalid exception if amount or account is invalid")
    @Test
    void testDepositAmountWithInvalidAccount() {

        when(accountRepository.getAccountById(Long.MAX_VALUE)).thenThrow(accountNotFoundException);
        assertThrows(AccountNotFoundException.class, () -> transferService.getAccountById(Long.MAX_VALUE),
                "Expected throw AccountNotFoundException but it didn't throw");
        assertThatThrownBy(() -> transferService.getAccountById(Long.MAX_VALUE)).hasMessage(ExceptionConstants.ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE + Long.MAX_VALUE);

    }


    @DisplayName("should throws invalid exception if amount or account is invalid")
    @Test
    void testDepositAmountWithAmount() {

        assertThatThrownBy(() -> transferService.withdrawal(1L, 0.00, US_CURRENCY_CODE))
                .isInstanceOf(InvalidAmountException.class);
        assertThatThrownBy(() -> transferService.withdrawal(1L, -10.00, US_CURRENCY_CODE))
                .isInstanceOf(InvalidAmountException.class);

    }
    @Test
    @DisplayName("should pass when valid data is passed")
    void testFundTransferWithValidSourceAndTargetAccount(){
        when(transferService.getAccountById(anyLong())).thenAnswer(invocationOnMock -> {
        Long argument = invocationOnMock.getArgument(0);
            if (argument.equals(1L)) {
               return testAccount1;
            }else {
                return testAccount2;
            }

        });


        Account account1 = transferService.getAccountById(1L);
        Account account2 = transferService.getAccountById(2L);

        assertNotEquals(account1.getAccountId(),account2.getAccountId());
        Money sourceAccountBalance = account1.getAvailableBalance();
        Money targetAccountBalance = account2.getAvailableBalance();
        doNothing().when(accountRepository).updateAccount(anyLong(), any(Account.class));
        transferService.transfer(account1.getAccountId(),account2.getAccountId(),30.00,US_CURRENCY_CODE);
        assertEquals(account1.getAvailableBalance(),sourceAccountBalance.subtract(Money.of(30.00, US_CURRENCY_CODE)));
        assertEquals(testAccount2.getAvailableBalance(),targetAccountBalance.add(Money.of(30.00, US_CURRENCY_CODE)));



    }
    @Test
    @DisplayName("should throw exception when both source and target accounts are same")
    void testFundTransferWithSameSourceAndTargetAccounts(){
        when(accountRepository.getAccountById(anyLong())).thenReturn(testAccount1);


        Account account1 = transferService.getAccountById(1L);
        Account account2 = transferService.getAccountById(2L);
        assertThrows(SameAccountTransferRequestException.class, () -> transferService.transfer(account1.getAccountId(),account2.getAccountId(),50,US_CURRENCY_CODE),
                "Expected throw SameAccountTransferRequestException but it didn't throw");
        assertThatThrownBy(() -> transferService.transfer(account1.getAccountId(),account2.getAccountId(),50,US_CURRENCY_CODE))
                .hasMessage(ExceptionConstants.SAME_SOURCE_TARGET_ACCOUNT_EXCEPTION_MESSAGE);

    }
    @Test
    @DisplayName("should throw UnSufficientFundException if withdrawal amount is greater than total account balance ")
    void testWithdrawalAmountGreaterThanTotalAccountBalanceThrowNotEnoughFundTransfer(){

        when(transferService.getAccountById(anyLong())).thenReturn(testAccount1);

        Assertions.assertThrows(UnSufficientFundException.class, () -> transferService.withdrawal(1L,500.00,US_CURRENCY_CODE),
                "Expected throw UnSufficientFundException but it didn't throw");

        assertThatThrownBy(() -> transferService.withdrawal(1L,500.00,US_CURRENCY_CODE))
                .hasMessage(ExceptionConstants.NOT_ENOUGH_FUNDS_EXCEPTION_MESSAGE);

    }

    @Test
    @DisplayName("Account withdrawal should be success when currency amount is bigger but its value is small- (INR(Indian Rupees) Rs 1000  are less than $100) ")
    void testWithdrawalWithDifferentCurrenciesByThereValue(){

        when(transferService.getAccountById(anyLong())).thenReturn(testAccount1);
        TransferStatus status = transferService.withdrawal(1L, 1000.00 , "INR");
        assertEquals(status,TransferStatus.SUCCESS);

    }
}
