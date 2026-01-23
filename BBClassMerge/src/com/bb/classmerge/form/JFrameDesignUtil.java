package com.bb.classmerge.form;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * JFrame 그래픽을 디자인하기 위한 유틸 (FlatLaf 스타일)
 */
public class JFrameDesignUtil {

	// 모던 스크롤바 색상
	private static final Color THUMB_COLOR = new Color(180, 180, 180);
	private static final Color THUMB_HOVER_COLOR = new Color(150, 150, 150);
	private static final Color TRACK_COLOR = new Color(245, 245, 245);
	
	/**
	 * JScrollPane에 모던 스크롤바 스타일 적용
	 * @param scrollPane 대상 JScrollPane
	 * @return 성공 여부
	 */
	public static boolean setColorToJScrollPane(JScrollPane scrollPane) {
		if (scrollPane == null) {
			return false;
		}
		
		// 모던 스크롤바 UI 적용
		scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
		scrollPane.getHorizontalScrollBar().setUI(new ModernScrollBarUI());
		
		// 스크롤바 두께 설정 (여러운 느낌)
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
		scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));
		
		return true;
	}
	
	/**
	 * 모던 스크롤바 UI (여러고 둘근 스타일)
	 */
	private static class ModernScrollBarUI extends BasicScrollBarUI {
		
		private boolean isThumbRollover = false;
		
		@Override
		protected void configureScrollBarColors() {
			this.thumbColor = THUMB_COLOR;
			this.thumbHighlightColor = THUMB_HOVER_COLOR;
			this.thumbDarkShadowColor = THUMB_COLOR;
			this.thumbLightShadowColor = THUMB_COLOR;
			this.trackColor = TRACK_COLOR;
			this.trackHighlightColor = TRACK_COLOR;
		}
		
		@Override
		protected JButton createDecreaseButton(int orientation) {
			// 화살표 버튼 숨기기 (모던 스타일)
			return createZeroButton();
		}
		
		@Override
		protected JButton createIncreaseButton(int orientation) {
			// 화살표 버튼 숨기기 (모던 스타일)
			return createZeroButton();
		}
		
		private JButton createZeroButton() {
			JButton button = new JButton();
			button.setPreferredSize(new Dimension(0, 0));
			button.setMinimumSize(new Dimension(0, 0));
			button.setMaximumSize(new Dimension(0, 0));
			return button;
		}
		
		@Override
		protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
			if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
				return;
			}
			
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			// 호버 시 색상 변경
			Color thumbColor = isThumbRollover ? THUMB_HOVER_COLOR : THUMB_COLOR;
			g2.setColor(thumbColor);
			
			// 둥근 모서리로 그리기 (arc 값을 크게 하면 더 둥글게)
			int arc = 8;
			int margin = 2;
			
			if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
				g2.fillRoundRect(
					thumbBounds.x + margin,
					thumbBounds.y,
					thumbBounds.width - margin * 2,
					thumbBounds.height,
					arc, arc
				);
			} else {
				g2.fillRoundRect(
					thumbBounds.x,
					thumbBounds.y + margin,
					thumbBounds.width,
					thumbBounds.height - margin * 2,
					arc, arc
				);
			}
			
			g2.dispose();
		}
		
		@Override
		protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
			// 트랙은 그리지 않음 (깨끗한 느낌)
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setColor(TRACK_COLOR);
			g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
			g2.dispose();
		}
		
		@Override
		protected void setThumbRollover(boolean active) {
			if (isThumbRollover != active) {
				isThumbRollover = active;
				scrollbar.repaint();
			}
		}
	}
}
