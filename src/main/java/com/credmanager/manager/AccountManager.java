package com.credmanager.manager;

import com.credmanager.domain.Account;
import com.credmanager.domain.Password;

public abstract class AccountManager {

  public abstract Account addAccount(Account account);

  public Account createAccount(String accountName, String accountId, String accountPassword) {
    return Account.newAccount(accountName, accountId, accountPassword);
  }

  public Account editAccountId(Account account, String accountId) {
    Account updatedAccount = Account.cloneAccount(account);
    updatedAccount.setId(accountId);
    return updatedAccount;
  }

  public Account editAccountPassword(Account account, String accountPassword) {
    Account updatedAccount = Account.cloneAccount(account);
    Password newPassword = Password.createNewPassword(accountPassword);
    updatedAccount.setPassword(newPassword);
    return updatedAccount;
  }

  public abstract void deleteAccount(Account account);

  public abstract void viewAccounts();

  public abstract Account getAccountByIdx(int index);

  public abstract boolean hasAccounts();
}
