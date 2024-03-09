package com.credmanager.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;

public class Account implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  private final String name;
  private final LocalDateTime createdOn;
  private String id;
  private Password password;
  private LocalDateTime updateOn;
  private Integer version;

  private Account(Account account) {
    this.name = account.name;
    this.id = account.id;
    this.password = account.password;
    this.createdOn = account.createdOn;
    this.updateOn = account.updateOn;
    this.version = account.version;
  }

  private Account(String name, String id, Password password) {
    this.name = name;
    this.id = id;
    this.password = password;
    this.createdOn = LocalDateTime.now();
    this.updateOn = null;
    this.version = 1;
  }

  public static Account newAccount(String name, String id, String passwordString) {
    Password password = Password.createNewPassword(passwordString);
    if (StringUtils.isBlank(name) || StringUtils.isBlank(id)) {
      throw new RuntimeException("Invalid input");
    }
    return new Account(name.trim(), id.trim(), password);
  }

  public static Account cloneAccount(Account account) {
    return new Account(account);
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    if (StringUtils.isBlank(id)) {
      throw new RuntimeException("Invalid id");
    }
    this.id = id;
  }

  public Password getPassword() {
    return password;
  }

  public void setPassword(Password password) {
    this.password = password;
  }

  public LocalDateTime getCreatedOn() {
    return createdOn;
  }

  public LocalDateTime getUpdateOn() {
    return updateOn;
  }

  public void setUpdateOn(LocalDateTime updateOn) {
    if (updateOn == null) {
      throw new RuntimeException("Invalid updateOn datetime");
    }
    this.updateOn = updateOn;
  }

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public void incrementVersion() {
    this.version = this.version + 1;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Name: ").append(name).append("\n");
    sb.append("ID: ").append(id).append("\n");
    sb.append("Password: ").append(password).append("\n");
    sb.append("Created On: ").append(createdOn).append("\n");
    sb.append("Updated On: ").append(updateOn == null ? "N/A" : updateOn).append("\n");
    return sb.toString();
  }
}
