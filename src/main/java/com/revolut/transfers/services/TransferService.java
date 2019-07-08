package com.revolut.transfers.services;

import com.revolut.transfers.enums.TransferStatus;
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


    TransferStatus withdrawal(Long accountId, Double amount, String currency);
}
