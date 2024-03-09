package com.credmanager.manager;

import com.credmanager.domain.Account;
import com.credmanager.utility.ConsolePrinter;
import com.credmanager.utility.FileUtility;
import com.credmanager.utility.SerializationUtil;
import com.credmanager.utility.Utility;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ActiveAccountsManager extends AccountManager {
  private static ActiveAccountsManager activeAccountsManager;
  private final TreeSet<Account> activeAccounts;

  private ActiveAccountsManager() {
    List<String> activeAccountNames =
        FileUtility.getFilesListFromDirectory(AccountConfig.activeAccountDirectory);
    activeAccounts =
        activeAccountNames.stream()
            .map(
                accountName ->
                    (Account)
                        SerializationUtil.deserializeObject(
                            AccountConfig.activeAccountDirectory, accountName))
            .collect(
                Collectors.toCollection(
                    () ->
                        new TreeSet<>(
                            Comparator.comparing(Account::getCreatedOn)
                                .thenComparing(Account::getVersion))));
  }

  public static ActiveAccountsManager getInstance() {
    if (activeAccountsManager == null) {
      activeAccountsManager = new ActiveAccountsManager();
    }
    return activeAccountsManager;
  }

  @Override
  public Account addAccount(Account account) {
    //    String fileName = Utility.getFileNameForAccountObject(account);
    //    SerializationUtil.serializeObject(
    //        account, ActiveAccountsManager.AccountConfig.activeAccountDirectory, fileName);
    activeAccounts.add(account);
    return account;
  }

  @Override
  public void deleteAccount(Account account) {
    activeAccounts.remove(account);
  }

  @Override
  public void viewAccounts() {
    ConsolePrinter.printTable(Arrays.asList(activeAccounts.toArray()), List.of("serialVersionUID"));
  }

  @Override
  public Account getAccountByIdx(int index) {
    if (index < 0 || index >= activeAccounts.size()) {
      return null;
    }
    return Utility.getValueAtIndex(activeAccounts, index);
  }

  @Override
  public boolean hasAccounts() {
    return !activeAccounts.isEmpty();
  }

  public static class AccountConfig {
    public static final String activeAccountDirectory = "accounts/";
  }
}
