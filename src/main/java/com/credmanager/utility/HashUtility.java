package com.credmanager.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtility {
  public static String hash(String text) {
    try {
      return bytesToHex(getSHA256Hash(text));
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  private static String bytesToHex(byte[] hash) {
    StringBuilder hexString = new StringBuilder();
    for (byte b : hash) {
      String hex = Integer.toHexString(0xff & b);
      if (hex.length() == 1) {
        hexString.append('0');
      }
      hexString.append(hex);
    }
    return hexString.toString();
  }

  private static byte[] getSHA256Hash(String input) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    return digest.digest(input.getBytes());
  }
}
