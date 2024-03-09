package com.credmanager.utility;

import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryptor {
  private static final String KEY_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA256";
  private static final String ENCRYPTION_ALGORITHM = "AES";
  private static final int ITERATION_COUNT = 65536;
  private static final int KEY_LENGTH = 256;

  public static byte[] encrypt(byte[] inputBytes, byte[] salt, String encryptionKey) {
    try {
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM);
      KeySpec keySpec =
          new PBEKeySpec(encryptionKey.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
      SecretKey secretKey = keyFactory.generateSecret(keySpec);
      SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), ENCRYPTION_ALGORITHM);

      Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);

      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

      return Base64.getEncoder().encode(cipher.doFinal(inputBytes));
    } catch (Exception e) {
      System.out.println(Arrays.toString(e.getStackTrace()));
      return null;
    }
  }

  public static byte[] decrypt(byte[] encryptedBytes, byte[] salt, String encryptionKey) {
    try {

      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM);
      KeySpec keySpec =
          new PBEKeySpec(encryptionKey.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
      SecretKey secretKey = keyFactory.generateSecret(keySpec);
      SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), ENCRYPTION_ALGORITHM);

      Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);

      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

      return cipher.doFinal(Base64.getDecoder().decode(encryptedBytes));
    } catch (Exception e) {
      System.out.println(Arrays.toString(e.getStackTrace()));
      return null;
    }
  }
}
