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

public class ArchivedAccountManager extends AccountManager {
  private static ArchivedAccountManager archivedAccManager;
  private final TreeSet<Account> archivedAccounts;

  private ArchivedAccountManager() {
    List<String> archivedAccountNames =
        FileUtility.getFilesListFromDirectory(AccountConfig.archiveAccountDirectory);
    archivedAccounts =
        archivedAccountNames.stream()
            .map(
                accountName ->
                    (Account)
                        SerializationUtil.deserializeObject(
                            AccountConfig.archiveAccountDirectory, accountName))
            .collect(
                Collectors.toCollection(
                    () ->
                        new TreeSet<>(
                            Comparator.comparing(Account::getCreatedOn)
                                .thenComparing(Account::getVersion))));
  }

  public static ArchivedAccountManager getInstance() {
    if (archivedAccManager == null) {
      archivedAccManager = new ArchivedAccountManager();
    }
    return archivedAccManager;
  }

  @Override
  public Account addAccount(Account account) {
    archivedAccounts.add(account);
    return account;
  }

  @Override
  public void deleteAccount(Account account) {
    archivedAccounts.remove(account);
  }

  @Override
  public void viewAccounts() {
    ConsolePrinter.printTable(
        Arrays.asList(archivedAccounts.toArray()), List.of("serialVersionUID"));
  }

  @Override
  public Account getAccountByIdx(int index) {
    if (index < 0 || index >= archivedAccounts.size()) {
      return null;
    }
    return Utility.getValueAtIndex(archivedAccounts, index);
  }

  @Override
  public boolean hasAccounts() {
    return !archivedAccounts.isEmpty();
  }

  public static class AccountConfig {
    public static final String archiveAccountDirectory = "archived/";
  }
}
