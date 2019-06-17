package com.bb.diff.form.button;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.bb.diff.common.CommonConst;

public class BBArrowButtonMouseListener implements MouseListener {

	private boolean targetIsleftSide = false;
	private boolean targetIsRightSide = false;
	private boolean targetIsToUp = false;
	private boolean isToLimit = false;
	
	public BBArrowButtonMouseListener(boolean targetIsleftSide, boolean targetIsRightSide, boolean targetIsToUp, boolean isToLimit) {
		
		this.targetIsleftSide = targetIsleftSide;
		this.targetIsRightSide = targetIsRightSide;
		this.targetIsToUp = targetIsToUp;
		this.isToLimit = isToLimit;

	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
		
		//JScrollPane의 바를 최 하단으로 맞춤

//		jScrollPane.getVerticalScrollBar().setValue(jScrollPane.getVerticalScrollBar().getMaximum());

		
		//예제 1

		//int 형 변수에 jTextArea 객체의 텍스트의 총 길이를 저장
//		int pos = jTextArea.getText().length();
//		
		//caret 포지션을 가장 마지막으로 맞춤
//		jTextArea.setCaretPosition(pos);
//
//		//갱신
//		jTextArea.requestFocus();
//


//System.out.println("-----");
//System.out.println("this.targetIsleftSide    : " + this.targetIsleftSide   );
//System.out.println("this.targetIsRightSide   : " + this.targetIsRightSide  );
//System.out.println("this.targetIsToUp        : " + this.targetIsToUp       );
//System.out.println("this.isToLimit           : " + this.isToLimit          );
//System.out.println("-----");

		// Point point = CConst.leftFileContent.getScrollPane().getVerticalScrollBar().getLocation();
		// int y = point.getY();
		
		if (targetIsleftSide) {
			if (targetIsToUp) {
				if (isToLimit) {
					CommonConst.leftFileContent.setScrollTop();
				} else {
					CommonConst.leftFileContent.addScrollY(CommonConst.movingValueOfTextArea * (-1));
				}
				
			} else {
				if (isToLimit) {
					CommonConst.leftFileContent.setScrollBottom();
				} else {
					CommonConst.leftFileContent.addScrollY(CommonConst.movingValueOfTextArea);
				}
				
			}
			
		}
		
		if (targetIsRightSide) {
			if (targetIsToUp) {
				if (isToLimit) {
					CommonConst.rightFileContent.setScrollTop();
				} else {
					CommonConst.rightFileContent.addScrollY(CommonConst.movingValueOfTextArea * (-1));
				}
			} else {
				if (isToLimit) {
					CommonConst.rightFileContent.setScrollBottom();
				} else {
					CommonConst.rightFileContent.addScrollY(CommonConst.movingValueOfTextArea);
				}
			}
		}
	}

}
