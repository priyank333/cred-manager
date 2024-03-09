package com.credmanager.utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class FileUtility {
  public static List<String> getFilesListFromDirectory(String directoryName) {
    if (directoryName == null || directoryName.isEmpty()) {
      return Collections.emptyList();
    }
    File directory = new File(directoryName);
    if (directory.exists() && directory.isDirectory()) {
      return Arrays.stream(Objects.requireNonNull(directory.listFiles()))
          .filter(File::isFile)
          .map(File::getName)
          .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  public static void moveFile(String sourceFolder, String destinationFolder, String fileName) {
    // Create Path objects for source and destination
    Path sourcePath = Paths.get(sourceFolder, fileName);
    Path destinationPath = Paths.get(destinationFolder, fileName);
    try {
      // Create destination folder if it doesn't exist
      if (!Files.exists(destinationPath.getParent())) {
        Files.createDirectories(destinationPath.getParent());
      }
      // Move the file
      Files.move(sourcePath, destinationPath);
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public static boolean deleteFile(String filePath) {
    File file = new File(filePath);
    // Check if the file exists
    if (file.exists()) {
      return file.delete();
    } else {
      return false;
    }
  }
}
