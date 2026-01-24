package com.bb.diff.form.button;

import javax.swing.JButton;

import com.bb.diff.common.CommonConst;

public class JButtonUtil {
	
	public static JButton createNewJButton(final String value) {
		final JButton jButton = new JButton();
		
		// FlatLaf의 기본 스타일 사용
		if (CommonConst.buttonBackgroundColor != null) {
			jButton.setBackground(CommonConst.buttonBackgroundColor);
		}
		
		if (value != null && value.length() > 0) {
			jButton.setText(value);
		}
		
		if (CommonConst.buttonTextColor != null) {
			jButton.setForeground(CommonConst.buttonTextColor);
		}

		return jButton;
	}


	/**
	 * 버튼 배경색 없애기
	 * 
	 * @param jButton
	 */
	public static void setTransparent(JButton jButton) {
		if (jButton == null) return;
		jButton.setContentAreaFilled(false);
		jButton.setBorderPainted(false);
		jButton.setFocusPainted(false);
		jButton.setOpaque(false);
	}
}
