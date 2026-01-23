package com.bb.diff.form.button;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.bb.diff.common.CommonConst;

public class BBDiffNextButtonMouseListener implements MouseListener {

	private static long beginTime = 0;
	
	@Override
	public void mouseClicked(MouseEvent e) {
		BBDiffNextButtonMouseListener.showDiffPoint(true);
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
	
	public static void showDiffPoint(boolean isDiffNext) {
		if (beginTime + 100 > System.currentTimeMillis()) {
			return;
		}
		
		beginTime = System.currentTimeMillis();
		
		if (CommonConst.diffPointList1 == null || CommonConst.diffPointList1.size() == 0) {
			return;
		}
		
		
		int lastIndex = CommonConst.diffPointList1.size() - 1;
		int totalDiffs = CommonConst.diffPointList1.size();
		
		if (isDiffNext) {
			CommonConst.currentDiffPointIndex++;
			if (CommonConst.currentDiffPointIndex > lastIndex) {
				CommonConst.currentDiffPointIndex = 0;
			}
		} else {
			CommonConst.currentDiffPointIndex--;
			if (CommonConst.currentDiffPointIndex < 0) {
				CommonConst.currentDiffPointIndex = lastIndex;
			}
		}
		
		// Diff 카운터 업데이트 (1-based index)
		CommonConst.diffPointLabel.setText("Diff: " + (CommonConst.currentDiffPointIndex + 1) + "/" + totalDiffs);
		
		int targetRow1 = CommonConst.diffPointList1.get(CommonConst.currentDiffPointIndex) + 1;
		int targetY1 = 0;
		if (targetRow1 > 0) {
			targetY1 = (int) ((targetRow1 - 1) * 16);
			// 최상단여백 제거
			targetY1 = targetY1 + 2;
		}
		
		
		int targetRow2 = CommonConst.diffPointList2.get(CommonConst.currentDiffPointIndex) + 1;
		int targetY2 = 0;
		if (targetRow2 > 0) {
			targetY2 = (int) ((targetRow2 - 1) * 16);
			// 최상단여백 제거
			targetY2 = targetY2 + 2;
		}
		
		try {
			CommonConst.leftFileContent.setScrollY(targetY1);
			// System.out.println("targetRow1 : " + targetRow1 + " / targetY1 : " + targetY1);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			CommonConst.rightFileContent.setScrollY(targetY2);
			// System.out.println("targetRow2 : " + targetRow2 + " / targetY2 : " + targetY2);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
