package com.bb.classmerge.form;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * JFrame 그래픽을 디자인하기 위한 유틸
 *
 */
public class JFrameDesignUtil {

	private static Color grayColor = new Color(205, 205, 205);
	private static Color lightGrayColor = new Color(241, 241, 241);
	
	/**
	 * JScrollPane 객체의 색상을 세팅한다.
	 * 
	 * @param scrollPane
	 */
	public static boolean setColorToJScrollPane(JScrollPane scrollPane) {
		if (scrollPane == null) {
			return false;
		}
		
		scrollPane.getVerticalScrollBar().setUI(makeBasicScrollBarUI());
		scrollPane.getHorizontalScrollBar().setUI(makeBasicScrollBarUI());
		return true;
	}
	
	public static BasicScrollBarUI makeBasicScrollBarUI() {
		BasicScrollBarUI basicScrollBarUI = new BasicScrollBarUI() {
			@Override
			protected void configureScrollBarColors() {
				super.configureScrollBarColors();
				
				this.thumbColor = grayColor;
				this.thumbDarkShadowColor = lightGrayColor;
				this.thumbHighlightColor = grayColor;
				this.thumbLightShadowColor = grayColor;
				
				this.trackColor = lightGrayColor;
				this.trackHighlightColor = lightGrayColor;
			}

			@Override
			protected JButton createDecreaseButton(int orientation) {
				Color thumbColor = lightGrayColor;
				Color thumbShadowColor = lightGrayColor;
				Color thumbthumbDarkShadowColor = Color.BLACK;
				Color thumbHighlightColor = lightGrayColor;
				
				return new BasicArrowButton(orientation, 
						thumbColor,
						thumbShadowColor,
						thumbthumbDarkShadowColor,
						thumbHighlightColor);
			}

			@Override
			protected JButton createIncreaseButton(int orientation) {
				Color thumbColor = lightGrayColor;
				Color thumbShadowColor = lightGrayColor;
				Color thumbthumbDarkShadowColor = Color.BLACK;
				Color thumbHighlightColor = lightGrayColor;
				
				return new BasicArrowButton(orientation,
						thumbColor,
						thumbShadowColor,
						thumbthumbDarkShadowColor,
						thumbHighlightColor);
			}
		};
		
		return basicScrollBarUI;
	}
}
