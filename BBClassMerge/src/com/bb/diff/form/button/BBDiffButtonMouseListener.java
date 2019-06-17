package com.bb.diff.form.button;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.bb.diff.common.CommonConst;

public class BBDiffButtonMouseListener implements MouseListener {

	private static long beginTime = 0;
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		if (beginTime + 100 > System.currentTimeMillis()) {
			return;
		}
		
		beginTime = System.currentTimeMillis();
		
		if (CommonConst.diffPointList == null || CommonConst.diffPointList.size() == 0) {
			return;
		}
		
		
		int lastIndex = CommonConst.diffPointList.size() - 1;
		CommonConst.currentDiffPointIndex++;
		
		if (CommonConst.currentDiffPointIndex > lastIndex) {
			CommonConst.currentDiffPointIndex = 0;
		}
		
		int targetRow = CommonConst.diffPointList.get(CommonConst.currentDiffPointIndex);
		int targetY = (int) (targetRow * 17);
		
		try {
			CommonConst.rightFileContent.setScrollY(targetY);
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			CommonConst.leftFileContent.setScrollY(targetY);
			
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
