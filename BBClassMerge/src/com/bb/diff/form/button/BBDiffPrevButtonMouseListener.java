package com.bb.diff.form.button;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.bb.diff.common.CommonConst;

public class BBDiffPrevButtonMouseListener implements MouseListener {

	private static long beginTime = 0;
	
	@Override
	public void mouseClicked(MouseEvent e) {
		BBDiffNextButtonMouseListener.showDiffPoint(false);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
