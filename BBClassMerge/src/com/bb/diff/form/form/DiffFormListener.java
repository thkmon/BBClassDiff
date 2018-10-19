package com.bb.diff.form.form;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import com.bb.diff.common.DiffConst;
import com.bb.diff.form.button.BBArrowButtonListener;

public class DiffFormListener implements ComponentListener {

	public void componentHidden(ComponentEvent arg0) {
		
	}

	public void componentMoved(ComponentEvent arg0) {
		
	}

	public void componentResized(ComponentEvent arg0) {
		doResizeForm();
	}

	public void componentShown(ComponentEvent arg0) {
		
	}
	
	public void doResizeForm() {
		
		/**
		 * 바운더리 계산
		 */
		DiffConst.fullWidth = DiffConst.bForm.getWidth();
		DiffConst.fullHeight = DiffConst.bForm.getHeight();
		
		DiffConst.treeWidth = (int) (DiffConst.fullWidth * DiffConst.treeWidthRatio);
		
		int boxWidth = (DiffConst.fullWidth - DiffConst.treeWidth) / 2 - 16;
		int boxHeight = DiffConst.fullHeight - DiffConst.treeTopMargin - DiffConst.bottomMargin;
		
		int box1Left = 4 + DiffConst.treeWidth + 4;
		int box2Left = box1Left + boxWidth + 4;
		
		int arrowButtonTop = DiffConst.fullHeight - DiffConst.bottomMargin + 4;
		
		
		
		
		/**
		 * 바운더리 적용
		 */
		
		// 트리
		DiffConst.fileTree.setBounds(DiffConst.treeLeftMargin, DiffConst.treeTopMargin, DiffConst.treeWidth, boxHeight);
		DiffConst.fileTree.getScrollPane().setBounds(DiffConst.treeLeftMargin, DiffConst.treeTopMargin, DiffConst.treeWidth, boxHeight);
		
		
		// 좌측 파일패스
		DiffConst.leftFilePathText.setBounds(box1Left, DiffConst.textAreaTopMargin, boxWidth, DiffConst.textAreaHeight);
		DiffConst.leftFilePathText.setEditable(false);
		
		// 좌측 파일내용
		DiffConst.leftFileContent.setBounds(box1Left, DiffConst.treeTopMargin, boxWidth, boxHeight);
		DiffConst.leftFileContent.getScrollPane().setBounds(box1Left, DiffConst.treeTopMargin, boxWidth, boxHeight);
		

		// 좌측 버튼
		int leftButtonLeft = box1Left;
		DiffConst.leftUpButton.setBounds(leftButtonLeft, arrowButtonTop, DiffConst.arrowButtonWidth, DiffConst.arrowButtonHeight);
		
		leftButtonLeft = leftButtonLeft + DiffConst.arrowButtonWidth;
		DiffConst.leftDownButton.setBounds(leftButtonLeft, arrowButtonTop, DiffConst.arrowButtonWidth, DiffConst.arrowButtonHeight);
		
		leftButtonLeft = leftButtonLeft + DiffConst.arrowButtonWidth;
		DiffConst.leftTopButton.setBounds(leftButtonLeft, arrowButtonTop, DiffConst.arrowButtonWidth, DiffConst.arrowButtonHeight);
		
		leftButtonLeft = leftButtonLeft + DiffConst.arrowButtonWidth;
		DiffConst.leftBottomButton.setBounds(leftButtonLeft, arrowButtonTop, DiffConst.arrowButtonWidth, DiffConst.arrowButtonHeight);
		
		// 버튼 액션
		DiffConst.leftTopButton.addActionListener(new BBArrowButtonListener   (true, false, true, true));
		DiffConst.leftBottomButton.addActionListener(new BBArrowButtonListener(true, false, false, true));
		DiffConst.leftUpButton.addActionListener(new BBArrowButtonListener    (true, false, true, false));
		DiffConst.leftDownButton.addActionListener(new BBArrowButtonListener  (true, false, false, false));
		
		
		// 우측 파일패스
		DiffConst.rightFilePathText.setBounds(box2Left, DiffConst.textAreaTopMargin, boxWidth, DiffConst.textAreaHeight);
		DiffConst.rightFilePathText.setEditable(false);
		
		// 우측 파일내용
		DiffConst.rightFileContent.setBounds(box2Left, DiffConst.treeTopMargin, boxWidth, boxHeight);
		DiffConst.rightFileContent.getScrollPane().setBounds(box2Left, DiffConst.treeTopMargin, boxWidth, boxHeight);
		
		
		// 우측 버튼
		int rightButtonLeft = box2Left;
		DiffConst.rightUpButton.setBounds(rightButtonLeft, arrowButtonTop, DiffConst.arrowButtonWidth, DiffConst.arrowButtonHeight);
		
		rightButtonLeft = rightButtonLeft + DiffConst.arrowButtonWidth;
		DiffConst.rightDownButton.setBounds(rightButtonLeft, arrowButtonTop, DiffConst.arrowButtonWidth, DiffConst.arrowButtonHeight);
		
		rightButtonLeft = rightButtonLeft + DiffConst.arrowButtonWidth;
		DiffConst.rightTopButton.setBounds(rightButtonLeft, arrowButtonTop, DiffConst.arrowButtonWidth, DiffConst.arrowButtonHeight);
		
		rightButtonLeft = rightButtonLeft + DiffConst.arrowButtonWidth;
		DiffConst.rightBottomButton.setBounds(rightButtonLeft, arrowButtonTop, DiffConst.arrowButtonWidth, DiffConst.arrowButtonHeight);
		
		
		
		// 버튼 액션
		DiffConst.rightTopButton.addActionListener(new BBArrowButtonListener   (false, true, true, true));
		DiffConst.rightBottomButton.addActionListener(new BBArrowButtonListener(false, true, false, true));
		DiffConst.rightUpButton.addActionListener(new BBArrowButtonListener   (false, true, true, false));
		DiffConst.rightDownButton.addActionListener(new BBArrowButtonListener  (false, true, false, false));
		
		// 공통 버튼
		int bothArrowButton1Left = box2Left + boxWidth - ((DiffConst.arrowButtonWidth - 4) * 4) - 6;
		DiffConst.bothUpButton.setBounds(bothArrowButton1Left, arrowButtonTop, DiffConst.arrowButtonWidth, DiffConst.arrowButtonHeight);
		
		bothArrowButton1Left = bothArrowButton1Left + DiffConst.arrowButtonWidth - 4;
		DiffConst.bothDownButton.setBounds(bothArrowButton1Left, arrowButtonTop, DiffConst.arrowButtonWidth, DiffConst.arrowButtonHeight);
		
		bothArrowButton1Left = bothArrowButton1Left + DiffConst.arrowButtonWidth - 4;
		DiffConst.bothTopButton.setBounds(bothArrowButton1Left, arrowButtonTop, DiffConst.arrowButtonWidth, DiffConst.arrowButtonHeight);
		
		bothArrowButton1Left = bothArrowButton1Left + DiffConst.arrowButtonWidth - 4;		
		DiffConst.bothBottomButton.setBounds(bothArrowButton1Left, arrowButtonTop, DiffConst.arrowButtonWidth, DiffConst.arrowButtonHeight);
		
		
		
		// 버튼 액션
		DiffConst.bothTopButton.addActionListener(new BBArrowButtonListener      (true, true, true, true));
		DiffConst.bothBottomButton.addActionListener(new BBArrowButtonListener   (true, true, false, true));
		DiffConst.bothUpButton.addActionListener(new BBArrowButtonListener       (true, true, true, false));
		DiffConst.bothDownButton.addActionListener(new BBArrowButtonListener     (true, true, false, false));
	}
}
