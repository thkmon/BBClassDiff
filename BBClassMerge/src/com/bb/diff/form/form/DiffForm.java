package com.bb.diff.form.form;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.bb.classmerge.main.BBClassMerge;
import com.bb.diff.common.DiffConst;
import com.bb.diff.form.textarea.EditorListener;
import com.bb.diff.form.tree.BBTree;
import com.bb.diff.form.tree.BBTreeCellRenderer;
import com.bb.diff.form.tree.BBTreeNode;
import com.bb.diff.form.tree.TreeUtil;

public class DiffForm {

//	/**
//	 * 생성자
//	 */
//	public DiffForm(PathList originOnlyList, PathList targetOnlyList, DoublePathList diffPathList) {
//		/**
//		 * 폼 생성
//		 */
//		initDiffForm(originOnlyList, targetOnlyList, diffPathList);
//	}

	/**
	 * 생성자
	 * 
	 * @param leftClassesDir
	 * @param rightClassesDir
	 */
	public DiffForm(String leftClassesDir, String rightClassesDir) {
		initDiffForm(leftClassesDir, rightClassesDir);
	}
	
	
	// public void initDiffForm(PathList originOnlyList, PathList targetOnlyList, DoublePathList diffPathList) {
	
	/**
	 * 폼 생성
	 * 
	 * @param leftClassesDir
	 * @param rightClassesDir
	 */
	public void initDiffForm(String leftClassesDir, String rightClassesDir) {
		
		String title = BBClassMerge.title;
		if (title == null) {
			title = "";
		}
		
		String version = BBClassMerge.version;
		if (version != null && version.length() > 0) {
			title = title + "_" + version;
		}
		
		DiffConst.bForm = new BBForm(DiffConst.fullWidth, DiffConst.fullHeight, title);

		DiffConst.leftFilePathText = DiffConst.bForm.addTextInput(0, 0, 0, 0);
		DiffConst.rightFilePathText = DiffConst.bForm.addTextInput(0, 0, 0, 0);
		
		/**
		 * 기존 트리 추가하는 코드 (상속버전)
		 */
		BBTreeNode rootNode = new BBTreeNode("Root");		
		BBTree bbTree = new BBTree(rootNode);
		bbTree.setCellRenderer(new BBTreeCellRenderer());
		
		DiffConst.fileTree = DiffConst.bForm.addTree(0, 0, 0, 0, bbTree);
		DiffConst.leftFileContent = DiffConst.bForm.addTextArea(0, 0, 0, 0);
		DiffConst.rightFileContent = DiffConst.bForm.addTextArea(0, 0, 0, 0);
		
		DiffConst.leftFileContent.getScrollPane().addComponentListener(new EditorListener());
		DiffConst.rightFileContent.getScrollPane().addComponentListener(new EditorListener());
		
		DiffConst.leftFileContent.getScrollPane().addMouseWheelListener(new MouseWheelListener() {
			
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				int leftVal = DiffConst.leftFileContent.getScrollPane().getVerticalScrollBar().getValue();
				DiffConst.rightFileContent.getScrollPane().getVerticalScrollBar().setValue(leftVal);
				
			}
		});
		DiffConst.rightFileContent.getScrollPane().addMouseWheelListener(new MouseWheelListener() {
			
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				
				int rightVal = DiffConst.rightFileContent.getScrollPane().getVerticalScrollBar().getValue();
				DiffConst.leftFileContent.getScrollPane().getVerticalScrollBar().setValue(rightVal);
			}
		});
		
		DiffConst.leftUpButton = DiffConst.bForm.addButton(0, 0, 0, 0, "▲");
		DiffConst.leftDownButton = DiffConst.bForm.addButton(0, 0, 0, 0, "▼");
		DiffConst.leftTopButton = DiffConst.bForm.addButton(0, 0, 0, 0, "B");
		DiffConst.leftBottomButton = DiffConst.bForm.addButton(0, 0, 0, 0, "E");
		
		DiffConst.bothUpButton = DiffConst.bForm.addButton(0, 0, 0, 0, "▲");
		DiffConst.bothDownButton = DiffConst.bForm.addButton(0, 0, 0, 0, "▼");
		DiffConst.bothTopButton = DiffConst.bForm.addButton(0, 0, 0, 0, "B");
		DiffConst.bothBottomButton = DiffConst.bForm.addButton(0, 0, 0, 0, "E");
		
		DiffConst.rightUpButton = DiffConst.bForm.addButton(0, 0, 0, 0, "▲");
		DiffConst.rightDownButton = DiffConst.bForm.addButton(0, 0, 0, 0, "▼");
		DiffConst.rightTopButton = DiffConst.bForm.addButton(0, 0, 0, 0, "B");
		DiffConst.rightBottomButton = DiffConst.bForm.addButton(0, 0, 0, 0, "E");
		
		DiffFormListener formListener = new DiffFormListener();
		DiffConst.bForm.addComponentListener(formListener);

		TreeUtil.drawTree(leftClassesDir, rightClassesDir);
//		TreeUtil.drawTree(originOnlyList, targetOnlyList, diffPathList);
		
		DiffConst.bForm.open();
		
		/**
		 * 크기 재설정
		 */
		formListener.doResizeForm();
	}
}

