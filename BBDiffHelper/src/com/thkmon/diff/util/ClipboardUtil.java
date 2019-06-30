package com.thkmon.diff.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.JTextArea;

public class ClipboardUtil {
	
	
	public static void copyToClipboard(JTextArea textArea) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		String copyString = textArea.getSelectedText();
		if (copyString != null) {
			StringSelection contents = new StringSelection(copyString);
			clipboard.setContents(contents, null);
		}
	}
	

	public static String pasteClipboard() {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(clipboard);

		String pasteString = "";
		if (contents != null) {
			try {
				pasteString = (String) (contents.getTransferData(DataFlavor.stringFlavor));
				// textArea.insert(pasteString, textArea.getCaretPosition());
			} catch (Exception e) {
			}
		}
		
		return pasteString;
	}
}