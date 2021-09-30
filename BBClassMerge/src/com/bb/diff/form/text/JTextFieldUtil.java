package com.bb.diff.form.text;

import java.awt.Color;
import java.awt.Cursor;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

import com.bb.diff.common.CommonConst;

public class JTextFieldUtil {
	public static JTextField createNewJTextField() {
		final JTextField jTextField = new JTextField();
		
		jTextField.setBackground(Color.white);
		jTextField.setBorder(BorderFactory.createLineBorder(CommonConst.buttonBorderColor));
		jTextField.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		
		return jTextField;
	}
}