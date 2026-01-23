package com.bb.diff.form.text;

import java.awt.Cursor;

import javax.swing.JTextField;

public class JTextFieldUtil {
	public static JTextField createNewJTextField() {
		final JTextField jTextField = new JTextField();
		
		// FlatLaf의 기본 스타일 사용
		jTextField.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		
		return jTextField;
	}
}
