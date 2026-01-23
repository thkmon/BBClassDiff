package com.bb.diff.form.form;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

/**
 * 드래그로 크기 조절 가능한 디바이더 패널
 */
public class ResizablePanelDivider extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static final int DIVIDER_WIDTH = 6;
	private static final Color DIVIDER_COLOR = new Color(220, 220, 220);
	private static final Color DIVIDER_HOVER_COLOR = new Color(180, 180, 180);
	
	private boolean isDragging = false;
	private boolean isHover = false;
	private int dragStartX;
	private int initialDividerX;
	
	private DividerDragListener dragListener;
	
	public interface DividerDragListener {
		void onDividerDragged(int newX);
		void onDividerDragCompleted(int newX);
	}
	
	public ResizablePanelDivider(DividerDragListener listener) {
		this.dragListener = listener;
		setBackground(DIVIDER_COLOR);
		setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				isDragging = true;
				dragStartX = e.getXOnScreen();
				initialDividerX = getX();
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if (isDragging && dragListener != null) {
					dragListener.onDividerDragCompleted(getX());
				}
				isDragging = false;
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				isHover = true;
				repaint();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				isHover = false;
				repaint();
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (isDragging && dragListener != null) {
					int deltaX = e.getXOnScreen() - dragStartX;
					int newX = initialDividerX + deltaX;
					dragListener.onDividerDragged(newX);
				}
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// 호버 시 색상 변경
		if (isHover || isDragging) {
			g.setColor(DIVIDER_HOVER_COLOR);
		} else {
			g.setColor(DIVIDER_COLOR);
		}
		g.fillRect(0, 0, getWidth(), getHeight());
		
		// 중앙에 그립 표시 (3개의 점)
		g.setColor(new Color(150, 150, 150));
		int centerY = getHeight() / 2;
		int centerX = getWidth() / 2;
		
		// 세로로 3개의 점
		for (int i = -1; i <= 1; i++) {
			int y = centerY + (i * 8);
			g.fillOval(centerX - 1, y - 1, 3, 3);
		}
	}
	
	public static int getDividerWidth() {
		return DIVIDER_WIDTH;
	}
}
