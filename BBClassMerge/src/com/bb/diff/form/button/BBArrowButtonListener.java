package com.bb.diff.form.button;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.text.Position;

import com.bb.diff.common.DiffConst;
import com.bb.diff.form.textarea.BBEditor;

public class BBArrowButtonListener implements ActionListener {

	private long preWhen = 0;
//	private BBTextArea targetTextArea = null;
	
	private boolean targetIsleftSide = false;
	private boolean targetIsRightSide = false;
	private boolean targetIsToUp = false;
	private boolean isToLimit = false;
	
	public BBArrowButtonListener(boolean targetIsleftSide, boolean targetIsRightSide, boolean targetIsToUp, boolean isToLimit) {
		
		this.targetIsleftSide = targetIsleftSide;
		this.targetIsRightSide = targetIsRightSide;
		this.targetIsToUp = targetIsToUp;
		this.isToLimit = isToLimit;
	}

	public void actionPerformed(ActionEvent arg0) {
		
		// 연속적 눌림 방지
		long newWhen = arg0.getWhen();
		
		if (newWhen == preWhen) {
			return;
		} else {
			preWhen = newWhen;
		}
		
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
					DiffConst.leftFileContent.setScrollTop();
				} else {
					DiffConst.leftFileContent.addScrollY(DiffConst.movingValueOfTextArea * (-1));
				}
				
			} else {
				if (isToLimit) {
					DiffConst.leftFileContent.setScrollBottom();
				} else {
					DiffConst.leftFileContent.addScrollY(DiffConst.movingValueOfTextArea);
				}
				
			}
			
		}
		
		if (targetIsRightSide) {
			if (targetIsToUp) {
				if (isToLimit) {
					DiffConst.rightFileContent.setScrollTop();
				} else {
					DiffConst.rightFileContent.addScrollY(DiffConst.movingValueOfTextArea * (-1));
				}
			} else {
				if (isToLimit) {
					DiffConst.rightFileContent.setScrollBottom();
				} else {
					DiffConst.rightFileContent.addScrollY(DiffConst.movingValueOfTextArea);
				}
			}
			
		}
		
		
		
	}

}
