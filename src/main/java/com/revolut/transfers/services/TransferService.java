package com.revolut.transfers.services;

import com.revolut.transfers.exceptions.AccountNotFoundException;
import com.revolut.transfers.model.Account;

public interface TransferService {
  /**
   *
   * Fetch existing account by accountId
    *@param accountId
   *
    *@return com.revolut.transfer.model.Account
   * @throws AccountNotFoundException
    **/
    Account getAccountById(Long accountId) throws AccountNotFoundException;


    Double withdrawal(Long accountId, Double amount, String currency);
}
