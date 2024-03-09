package com.credmanager.utility;

import java.awt.*;
import java.awt.datatransfer.*;

public final class ClipboardUtility {
  public static void copyToClipboard(String text) {
    // Get the system clipboard
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    // Create a transferable object to hold the text
    StringSelection selection = new StringSelection(text);

    // Set the content of the clipboard to the transferable object
    clipboard.setContents(selection, null);
  }
}
