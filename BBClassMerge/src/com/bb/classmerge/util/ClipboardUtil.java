package com.bb.classmerge.util;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.JTextArea;

public class ClipboardUtil {
	
	/**
	 * 복사 (자바 => 클립보드)
	 * @param copyString
	 */
	public static void copyToClipboard(String copyString) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		if (copyString != null) {
			StringSelection contents = new StringSelection(copyString);
			clipboard.setContents(contents, null);
		}
	}
	
	/**
	 * 붙여넣기 (클립보드 => 자바)
	 * 
	 * @param textArea
	 * @return
	 */
	public static String pasteFromClipboard(JTextArea textArea) {
		String pasteString = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(clipboard);

		if (contents != null) {
			try {
				pasteString = (String) (contents.getTransferData(DataFlavor.stringFlavor));
			} catch (Exception e) {
			}
		}
		
		return pasteString;
	}
}