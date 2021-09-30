package com.bb.diff.form.button;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import com.bb.diff.common.CommonConst;

public class JButtonUtil {
	
	public static JButton createNewJButton(final String value) {
		final JButton jButton = new JButton();
		
		if (value != null && value.length() > 0) {
			jButton.setBackground(CommonConst.buttonBackgroundColor);
			jButton.setBorder(BorderFactory.createLineBorder(CommonConst.buttonBorderColor));
		} else {
			jButton.setBackground(CommonConst.formBackgroundColor);
			jButton.setBorder(BorderFactory.createEmptyBorder());
		}
		
		// 마우스 오버(hover) 시 기존 이벤트 비활성화
		jButton.setRolloverEnabled(false);
		
		// 버튼 클릭 시 텍스트 테두리에 선 잡히는 현상 해결(포커싱 테두리 제거)
		jButton.setFocusPainted(false);
		
		if (value != null && value.length() > 0) {
			jButton.setText(value);
		}
		
		jButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				if (value != null && value.length() > 0) {
					jButton.setBackground(CommonConst.buttonBackgroundColor);
					jButton.setBorder(BorderFactory.createLineBorder(CommonConst.buttonBorderColor));
				} else {
					jButton.setBackground(CommonConst.formBackgroundColor);
					jButton.setBorder(BorderFactory.createEmptyBorder());
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				if (value != null && value.length() > 0) {
					jButton.setBackground(CommonConst.buttonBackgroundColorWhenHover);
					jButton.setBorder(BorderFactory.createLineBorder(CommonConst.buttonBorderColorWhenHover));
				} else {
					jButton.setBackground(CommonConst.buttonBackgroundColorWhenHover);
					jButton.setBorder(BorderFactory.createLineBorder(CommonConst.buttonBorderColorWhenHover));
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});

		return jButton;
	}
}
