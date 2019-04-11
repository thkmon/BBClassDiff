package com.bb.diff.form.button;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.bb.diff.common.DiffConst;

public class BBDiffButtonMouseListener implements MouseListener {

	private static long beginTime = 0;
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		if (beginTime + 100 > System.currentTimeMillis()) {
			return;
		}
		
		beginTime = System.currentTimeMillis();
		
		if (DiffConst.diffPointList == null || DiffConst.diffPointList.size() == 0) {
			return;
		}
		
		
		int lastIndex = DiffConst.diffPointList.size() - 1;
		DiffConst.currentDiffPointIndex++;
		
		if (DiffConst.currentDiffPointIndex > lastIndex) {
			DiffConst.currentDiffPointIndex = 0;
		}
		
		int targetRow = DiffConst.diffPointList.get(DiffConst.currentDiffPointIndex);
		int targetY = (int) (targetRow * 17);
		
		try {
			DiffConst.rightFileContent.setScrollY(targetY);
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			DiffConst.leftFileContent.setScrollY(targetY);
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}