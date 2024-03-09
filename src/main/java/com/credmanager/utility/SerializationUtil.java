package com.credmanager.utility;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public final class SerializationUtil {
  public static final String fileExtension = ".ser";
  private static final byte[] SALT = {
    0x12, 0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF
  };
  private static final String ENCRYPTION_KEY;
  private static final String ENCRYPTION_KEY_DIR = "config/encflekey.txt";

  static {
    try {
      Path path = Paths.get(ENCRYPTION_KEY_DIR);
      if (Files.exists(path)) {
        ENCRYPTION_KEY = Files.readString(path);
      } else {
        throw new RuntimeException(ENCRYPTION_KEY_DIR + " is required.");
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void serializeObject(Object obj, String directoryPath, String fileName) {
    try {
      // Create the directory if it doesn't exist
      File directory = new File(directoryPath);
      if (!directory.exists()) {
        boolean isCreated = directory.mkdirs();
        if (!isCreated) {
          throw new RuntimeException("Directory is not able to create");
        }
      }
      // Serialize the object to a byte array
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(bos);
      oos.writeObject(obj);

      byte[] encryptedData = Encryptor.encrypt(bos.toByteArray(), SALT, ENCRYPTION_KEY);

      if (encryptedData == null) {
        throw new RuntimeException("Encryption process is incomplete. Got an error.");
      }
      String filePath = directoryPath + fileName + fileExtension;
      // Save the encrypted data to a file
      try (FileOutputStream fos = new FileOutputStream(filePath)) {
        fos.write(encryptedData);
      }
    } catch (IOException e) {
      System.out.println(Arrays.toString(e.getStackTrace()));
    }
  }

  public static Object deserializeObject(String directoryPath, String fileName) {
    String filePath = directoryPath + fileName;
    try {
      // Read the encrypted data from the file
      byte[] encryptedData;
      try (FileInputStream fis = new FileInputStream(filePath)) {
        encryptedData = fis.readAllBytes();
      }

      byte[] decryptedData = Encryptor.decrypt(encryptedData, SALT, ENCRYPTION_KEY);
      if (decryptedData == null) {
        throw new RuntimeException("Decryption process is incomplete. Got an error.");
      }
      // Deserialize the decrypted byte array
      ByteArrayInputStream bis = new ByteArrayInputStream(decryptedData);
      ObjectInputStream ois = new ObjectInputStream(bis);
      return ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      System.out.println(Arrays.toString(e.getStackTrace()));
      return null;
    }
  }
}
