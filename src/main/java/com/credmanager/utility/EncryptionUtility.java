package com.credmanager.utility;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class EncryptionUtility {
  private static final String KEY_GEN_ALGORITHM = "AES";

  public static String generateEncryptionKey(int keySize) {
    KeyGenerator keyGen = null;
    try {
      keyGen = KeyGenerator.getInstance(KEY_GEN_ALGORITHM);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    // Generate a random AES key
    keyGen.init(keySize);
    SecretKey secretKey = keyGen.generateKey();

    // Convert the key to a byte array
    byte[] keyBytes = secretKey.getEncoded();
    StringBuilder hexString = new StringBuilder();
    for (byte b : keyBytes) {
      String hex = Integer.toHexString(0xff & b);
      if (hex.length() == 1) {
        hexString.append('0');
      }
      hexString.append(hex);
    }
    return hexString.toString();
  }

  public static byte[] generateSalt(int bytes) {
    byte[] salt = new byte[bytes];
    SecureRandom random = new SecureRandom();
    random.nextBytes(salt);
    return salt;
  }
}
