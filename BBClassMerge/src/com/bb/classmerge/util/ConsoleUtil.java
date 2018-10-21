package com.bb.classmerge.util;

import com.bb.classmerge.exception.MsgException;
import com.bb.classmerge.form.MainForm;

public class ConsoleUtil {
	
	
	public static void print(String message) {
		System.out.println(message);

		if (MainForm.textArea != null) {
			if (MainForm.textArea.getText() != null && MainForm.textArea.getText().length() > 0) {
				MainForm.textArea.append("\r\n" + message);
			} else {
				MainForm.textArea.append(message);
			}
			
//			MainForm.textArea.update(MainForm.textArea.getGraphics());
			MainForm.textArea.setCaretPosition(MainForm.textArea.getDocument().getLength());
		}
	}
	
	
	public static void print(MsgException e) {
		print(e.getMessage());
	}
	
	
	public static void print(Exception e) {
		
		print(e.getClass().getName() + ": " + e.getMessage());
		
		StackTraceElement[] stackTrace = e.getStackTrace();
		int len = stackTrace.length;
		
		StackTraceElement stackElem = null;
		String stackStr = null;
		for (int i=0; i<len; i++) {
			stackElem = stackTrace[i];
			stackStr = stackElem.toString();
			print(" at " + stackStr);
		}
	}
}
