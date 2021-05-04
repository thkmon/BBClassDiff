package com.bb.diff.form.form;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import com.bb.diff.common.CommonConst;
import com.bb.diff.form.button.BBArrowButtonMouseListener;
import com.bb.diff.form.button.BBDiffButtonMouseListener;

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
		CommonConst.fullWidth = CommonConst.bForm.getWidth();
		CommonConst.fullHeight = CommonConst.bForm.getHeight();
		
		CommonConst.treeWidth = (int) (CommonConst.fullWidth * CommonConst.treeWidthRatio);
		
		int boxWidth = (CommonConst.fullWidth - CommonConst.treeWidth) / 2 - 16;
		int boxHeight = CommonConst.fullHeight - CommonConst.treeTopMargin - CommonConst.bottomMargin;
		
		int box1Left = 4 + CommonConst.treeWidth + 4;
		int box2Left = box1Left + boxWidth + 4;
		
		int arrowButtonTop = CommonConst.fullHeight - CommonConst.bottomMargin + 4;
		
		
		
		
		/**
		 * 바운더리 적용
		 */
		// 트리
		CommonConst.fileTree.setBounds(0, 0, CommonConst.treeWidth, boxHeight);
		CommonConst.fileTree.getScrollPane().setBounds(CommonConst.treeLeftMargin, CommonConst.treeTopMargin, CommonConst.treeWidth, boxHeight);
		
		
		// 좌측 파일패스
		CommonConst.leftFilePathText.setBounds(box1Left, CommonConst.textAreaTopMargin, boxWidth, CommonConst.textAreaHeight);
		CommonConst.leftFilePathText.setEditable(false);
		
		// 좌측 파일내용
		CommonConst.leftFileContent.setBounds(0, 0, boxWidth, boxHeight);
		CommonConst.leftFileContent.getScrollPane().setBounds(box1Left, CommonConst.treeTopMargin, boxWidth, boxHeight);
		
		// 좌측 하단 프로그레스 레이블 추가
		CommonConst.progressLabel.setBounds(10, arrowButtonTop, CommonConst.progressLabelWidth, CommonConst.progressLabelHeight);
		
		// 좌측 버튼
		int leftButtonLeft = box1Left;
		CommonConst.leftUpButton.setBounds(leftButtonLeft, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		
		leftButtonLeft = leftButtonLeft + CommonConst.arrowButtonWidth;
		CommonConst.leftDownButton.setBounds(leftButtonLeft, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		
		leftButtonLeft = leftButtonLeft + CommonConst.arrowButtonWidth;
		CommonConst.leftTopButton.setBounds(leftButtonLeft, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		
		leftButtonLeft = leftButtonLeft + CommonConst.arrowButtonWidth;
		CommonConst.leftBottomButton.setBounds(leftButtonLeft, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		
		// 버튼 액션
		CommonConst.leftTopButton.addMouseListener(new BBArrowButtonMouseListener   (true, false, true, true));
		CommonConst.leftBottomButton.addMouseListener(new BBArrowButtonMouseListener(true, false, false, true));
		CommonConst.leftUpButton.addMouseListener(new BBArrowButtonMouseListener    (true, false, true, false));
		CommonConst.leftDownButton.addMouseListener(new BBArrowButtonMouseListener  (true, false, false, false));
		
		
		// 우측 파일패스
		CommonConst.rightFilePathText.setBounds(box2Left, CommonConst.textAreaTopMargin, boxWidth, CommonConst.textAreaHeight);
		CommonConst.rightFilePathText.setEditable(false);
		
		// 우측 파일내용
		CommonConst.rightFileContent.setBounds(0, 0, boxWidth, boxHeight);
		CommonConst.rightFileContent.getScrollPane().setBounds(box2Left, CommonConst.treeTopMargin, boxWidth, boxHeight);
		
		
		// 우측 버튼
		int rightButtonLeft = box2Left;
		CommonConst.rightUpButton.setBounds(rightButtonLeft, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		
		rightButtonLeft = rightButtonLeft + CommonConst.arrowButtonWidth;
		CommonConst.rightDownButton.setBounds(rightButtonLeft, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		
		rightButtonLeft = rightButtonLeft + CommonConst.arrowButtonWidth;
		CommonConst.rightTopButton.setBounds(rightButtonLeft, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		
		rightButtonLeft = rightButtonLeft + CommonConst.arrowButtonWidth;
		CommonConst.rightBottomButton.setBounds(rightButtonLeft, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		
		
		// 버튼 액션
		CommonConst.rightTopButton.addMouseListener(new BBArrowButtonMouseListener   (false, true, true, true));
		CommonConst.rightBottomButton.addMouseListener(new BBArrowButtonMouseListener(false, true, false, true));
		CommonConst.rightUpButton.addMouseListener(new BBArrowButtonMouseListener   (false, true, true, false));
		CommonConst.rightDownButton.addMouseListener(new BBArrowButtonMouseListener  (false, true, false, false));
		
		
		// 공통 버튼
		int bothArrowButton1Left = box2Left + boxWidth - ((CommonConst.arrowButtonWidth - 4) * 4) - 46;
		CommonConst.bothDiffButton.setBounds(bothArrowButton1Left, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		
		bothArrowButton1Left = bothArrowButton1Left + CommonConst.arrowButtonWidth - 4;
		CommonConst.bothUpButton.setBounds(bothArrowButton1Left, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		
		bothArrowButton1Left = bothArrowButton1Left + CommonConst.arrowButtonWidth - 4;
		CommonConst.bothDownButton.setBounds(bothArrowButton1Left, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		
		bothArrowButton1Left = bothArrowButton1Left + CommonConst.arrowButtonWidth - 4;
		CommonConst.bothTopButton.setBounds(bothArrowButton1Left, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		
		bothArrowButton1Left = bothArrowButton1Left + CommonConst.arrowButtonWidth - 4;		
		CommonConst.bothBottomButton.setBounds(bothArrowButton1Left, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		
		
		// 버튼 액션
		CommonConst.bothDiffButton.addMouseListener(new BBDiffButtonMouseListener());
		CommonConst.bothTopButton.addMouseListener(new BBArrowButtonMouseListener      (true, true, true, true));
		CommonConst.bothBottomButton.addMouseListener(new BBArrowButtonMouseListener   (true, true, false, true));
		CommonConst.bothUpButton.addMouseListener(new BBArrowButtonMouseListener       (true, true, true, false));
		CommonConst.bothDownButton.addMouseListener(new BBArrowButtonMouseListener     (true, true, false, false));
	}
}
