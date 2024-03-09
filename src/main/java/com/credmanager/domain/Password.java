package com.credmanager.domain;

import com.credmanager.utility.EncryptionUtility;
import com.credmanager.utility.Encryptor;

import java.io.Serial;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class Password implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;
  private final String password;
  private final byte[] salt;
  private final String encryptionKey;

  private Password(String password, byte[] salt, String encryptionKey) {
    this.password = password;
    this.salt = salt;
    this.encryptionKey = encryptionKey;
  }

  public static Password createNewPassword(String password) {
    if (StringUtils.isBlank(password)) {
      throw new RuntimeException("Password is required");
    }
    byte[] salt = EncryptionUtility.generateSalt(8);
    String encryptionKey = EncryptionUtility.generateEncryptionKey(256);
    String encryptedPassword =
        new String(
            Objects.requireNonNull(
                Encryptor.encrypt(password.trim().getBytes(), salt, encryptionKey)),
            StandardCharsets.UTF_8);
    return new Password(encryptedPassword, salt, encryptionKey);
  }

  public String getDecryptedPassword() {
    return new String(
        Objects.requireNonNull(Encryptor.decrypt(this.password.getBytes(), salt, encryptionKey)),
        StandardCharsets.UTF_8);
  }

  public String getPassword() {
    return password;
  }

  public byte[] getSalt() {
    return salt;
  }

  public String getEncryptionKey() {
    return encryptionKey;
  }

  @Override
  public String toString() {
    return password;
  }
}
