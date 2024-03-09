package com.credmanager.utility;

import com.credmanager.domain.Account;
import java.util.Set;

public final class Utility {
  public static <T> T getValueAtIndex(Set<T> set, int index) {
    if (index < 0 || index >= set.size()) {
      throw new IndexOutOfBoundsException("Index out of bounds: " + index);
    }

    int i = 0;
    for (T element : set) {
      if (i == index) {
        return element;
      }
      i++;
    }

    throw new IndexOutOfBoundsException("Index out of bounds: " + index);
  }

  public static String getFileNameForAccountObject(Account account) {
    return HashUtility.hash(account.getName() + account.getId() + account.getVersion());
  }
}
