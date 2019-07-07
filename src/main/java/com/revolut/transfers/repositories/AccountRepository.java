package com.revolut.transfers.repositories;

import com.revolut.transfers.model.Account;

public interface AccountRepository {


     Account getAccountById(Long id);
}
