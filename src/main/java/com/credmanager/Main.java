package com.credmanager;

import static java.lang.System.*;

import com.credmanager.domain.Account;
import com.credmanager.manager.AccountManager;
import com.credmanager.manager.ActiveAccountsManager;
import com.credmanager.manager.ArchivedAccountManager;
import com.credmanager.utility.*;
import java.time.LocalDateTime;
import java.util.*;

public class Main {
  private static final Scanner SCANNER = new Scanner(System.in);
  private static final ActiveAccountsManager activeAccManager = ActiveAccountsManager.getInstance();
  private static final ArchivedAccountManager archiveAccManager =
      ArchivedAccountManager.getInstance();

  public static void main(String[] args) {
    boolean exit = false;
    do {
      out.println("MENU");
      out.println("1. Add Account");
      out.println("2. Update Account");
      out.println("3. Delete Account");
      out.println("4. View Active Accounts");
      out.println("5. View Archived Accounts");
      out.println("6. Copy active account on clipboard");
      out.println("7. Copy archived account on clipboard");
      out.println("8. Exit");
      out.print("Choice = ");
      int choice = SCANNER.nextInt();
      SCANNER.nextLine();
      switch (choice) {
        case 1:
          addAccount();
          break;
        case 2:
          updateAccount();
          break;
        case 3:
          deleteAccount();
          break;
        case 4:
          activeAccManager.viewAccounts();
          break;
        case 5:
          archiveAccManager.viewAccounts();
          break;
        case 6:
          copyClipboard(activeAccManager);
          break;
        case 7:
          copyClipboard(archiveAccManager);
          break;
        case 8:
          exit = true;
          break;
        default:
          throw new IllegalArgumentException("Illegal argument");
      }
    } while (!exit);
  }

  private static void addAccount() {
    out.println("Account Name: ");
    String accountName = SCANNER.nextLine();
    out.println("Account Id: ");
    String accountId = SCANNER.nextLine();
    out.println("Account Password: ");
    String accountPassword = SCANNER.nextLine();
    Account account = activeAccManager.createAccount(accountName, accountId, accountPassword);
    String fileName = Utility.getFileNameForAccountObject(account);
    SerializationUtil.serializeObject(
        account, ActiveAccountsManager.AccountConfig.activeAccountDirectory, fileName);
    activeAccManager.addAccount(account);
  }

  private static void updateAccount() {
    activeAccManager.viewAccounts();
    out.print("Choice: ");
    int accSelection = SCANNER.nextInt();
    SCANNER.nextLine();
    Account originalAccountObj = activeAccManager.getAccountByIdx(accSelection - 1);
    if (originalAccountObj == null) {
      ConsolePrinter.printWarningMessage("Invalid Choice");
      return;
    }
    out.println("1. Update Id");
    out.println("2. Update Password");
    out.print("Choice: ");
    int updateChoice = SCANNER.nextInt();
    SCANNER.nextLine();
    Account newAccountObj;
    switch (updateChoice) {
      case 1:
        out.print("Id: ");
        String id = SCANNER.nextLine();
        newAccountObj = activeAccManager.editAccountId(originalAccountObj, id);
        break;
      case 2:
        out.print("Password: ");
        String password = SCANNER.nextLine();
        newAccountObj = activeAccManager.editAccountPassword(originalAccountObj, password);
        break;
      default:
        throw new IllegalArgumentException("Illegal argument");
    }
    originalAccountObj.setUpdateOn(LocalDateTime.now());
    newAccountObj.incrementVersion();
    activeAccManager.deleteAccount(originalAccountObj);
    activeAccManager.addAccount(newAccountObj);
    archiveAccManager.addAccount(originalAccountObj);
    String fileName = Utility.getFileNameForAccountObject(newAccountObj);
    SerializationUtil.serializeObject(
        newAccountObj, ActiveAccountsManager.AccountConfig.activeAccountDirectory, fileName);
    String archivedFileName = Utility.getFileNameForAccountObject(originalAccountObj);
    SerializationUtil.serializeObject(
        originalAccountObj,
        ArchivedAccountManager.AccountConfig.archiveAccountDirectory,
        archivedFileName);
    FileUtility.deleteFile(
        ActiveAccountsManager.AccountConfig.activeAccountDirectory
            + archivedFileName
            + SerializationUtil.fileExtension);
  }

  private static void deleteAccount() {
    activeAccManager.viewAccounts();
    out.print("Choice: ");
    int accSelection = SCANNER.nextInt();
    SCANNER.nextLine();
    Account originalAccountObj = activeAccManager.getAccountByIdx(accSelection - 1);
    if (originalAccountObj == null) {
      ConsolePrinter.printWarningMessage("Invalid Choice");
      return;
    }
    String fileName = Utility.getFileNameForAccountObject(originalAccountObj);
    activeAccManager.deleteAccount(originalAccountObj);
    FileUtility.deleteFile(
        ActiveAccountsManager.AccountConfig.activeAccountDirectory
            + fileName
            + SerializationUtil.fileExtension);
  }

  private static void copyClipboard(AccountManager accountManager) {
    if (!accountManager.hasAccounts()) {
      ConsolePrinter.printWarningMessage("No accounts found.");
      return;
    }
    accountManager.viewAccounts();
    out.print("Choice: ");
    int accountChoice = SCANNER.nextInt();
    SCANNER.nextLine();
    Account account = accountManager.getAccountByIdx(accountChoice - 1);
    if (account == null) {
      ConsolePrinter.printWarningMessage("Invalid Choice");
      return;
    }
    out.println("1. Copy Id");
    out.println("2. Copy Password");
    out.print("Choice: ");
    int copyChoice = SCANNER.nextInt();
    SCANNER.nextLine();
    switch (copyChoice) {
      case 1:
        ClipboardUtility.copyToClipboard(account.getId());
        break;
      case 2:
        ClipboardUtility.copyToClipboard(account.getPassword().getDecryptedPassword());
        break;
      default:
        throw new IllegalArgumentException("Invalid choice");
    }
  }
}
