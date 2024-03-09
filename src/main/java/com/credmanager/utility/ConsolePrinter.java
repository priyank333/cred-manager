package com.credmanager.utility;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public final class ConsolePrinter {
  private static final Scanner SCANNER = new Scanner(System.in);

  public static void printTable(List<?> objects, List<String> ignoreField) {
    if (objects.isEmpty()) {
      printWarningMessage("List is empty.");
      return;
    }

    // Get fields of the object class
    Class<?> clazz = objects.get(0).getClass();
    Field[] fields = clazz.getDeclaredFields();
    String indexColumnHeaderName = "no";
    // Calculate maximum column widths
    Map<String, Integer> columnWidths = new HashMap<>();
    columnWidths.put(indexColumnHeaderName, indexColumnHeaderName.length());
    for (Field field : fields) {
      columnWidths.put(field.getName(), field.getName().length());
    }

    for (Object obj : objects) {
      for (Field field : fields) {
        try {
          field.setAccessible(true);
          Object value = field.get(obj);
          if (value != null) {
            columnWidths.put(
                field.getName(),
                Math.max(columnWidths.get(field.getName()), value.toString().length()));
          }
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }

    System.out.print(
        padRight(indexColumnHeaderName, columnWidths.get(indexColumnHeaderName)) + " | ");
    // Print table header
    for (Field field : fields) {
      if (ignoreField.contains(field.getName())) {
        continue;
      }
      String columnName = field.getName();
      System.out.print(padRight(columnName, columnWidths.get(columnName)) + " | ");
    }
    System.out.println();
    int rowCount = 1;
    // Print table content
    for (Object obj : objects) {
      System.out.print(
          padRight(String.valueOf(rowCount), columnWidths.get(indexColumnHeaderName)) + " | ");
      rowCount++;
      for (Field field : fields) {
        if (ignoreField.contains(field.getName())) {
          continue;
        }
        try {
          field.setAccessible(true);
          Object value = field.get(obj);
          if (value != null) {
            System.out.print(padRight(value.toString(), columnWidths.get(field.getName())) + " | ");
          } else {
            System.out.print(padRight("null", columnWidths.get(field.getName())) + " | ");
          }
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
      System.out.println();
    }
    SCANNER.nextLine();
  }

  public static void printWarningMessage(String message) {
    System.out.println(message.toUpperCase());
    SCANNER.nextLine();
  }

  private static String padRight(String text, int length) {
    return String.format("%-" + length + "s", text);
  }
}
