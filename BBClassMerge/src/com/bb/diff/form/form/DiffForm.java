package com.bb.diff.form.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.bb.classmerge.main.BBClassMerge;
import com.bb.diff.common.CommonConst;
import com.bb.diff.form.button.BBWinmergeButtonMouseListener;
import com.bb.diff.form.textarea.EditorListener;
import com.bb.diff.form.tree.BBTree;
import com.bb.diff.form.tree.BBTreeCellRenderer;
import com.bb.diff.form.tree.BBTreeNode;
import com.bb.diff.form.tree.TreeUtil;

public class DiffForm {

	/**
	 * 생성자
	 * 
	 * @param leftClassesDir
	 * @param rightClassesDir
	 */
	public DiffForm(String leftClassesDir, String rightClassesDir) {
		initDiffForm(leftClassesDir, rightClassesDir);
	}
	
	
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
		
		CommonConst.bForm = new BBForm(CommonConst.fullWidth, CommonConst.fullHeight, title);
		addMenuBar(CommonConst.bForm);

		CommonConst.leftFilePathText = CommonConst.bForm.addTextInput(0, 0, 0, 0);
		CommonConst.rightFilePathText = CommonConst.bForm.addTextInput(0, 0, 0, 0);
		
		CommonConst.winmergeButton = CommonConst.bForm.addButton(0, 0, 80, 24, "Winmerge");
		CommonConst.winmergeButton.addMouseListener(new BBWinmergeButtonMouseListener());
		
		CommonConst.diffPointLabel = CommonConst.bForm.addLabel(90, 0, 80, 24, "Diff Point : ");
		
		
		/**
		 * 기존 트리 추가하는 코드 (상속버전)
		 */
		BBTreeNode rootNode = new BBTreeNode("Root");		
		BBTree bbTree = new BBTree(rootNode);
		CommonConst.treeModel = bbTree;
		bbTree.setCellRenderer(new BBTreeCellRenderer());
		
		CommonConst.fileTree = CommonConst.bForm.addTree(0, 0, 0, 0, bbTree);
		CommonConst.leftFileContent = CommonConst.bForm.addTextArea(0, 0, 0, 0);
		CommonConst.rightFileContent = CommonConst.bForm.addTextArea(0, 0, 0, 0);
		
		CommonConst.leftFileContent.getScrollPane().addComponentListener(new EditorListener());
		CommonConst.rightFileContent.getScrollPane().addComponentListener(new EditorListener());
		
		CommonConst.leftFileContent.getScrollPane().addMouseWheelListener(new MouseWheelListener() {
			
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				int leftVal = CommonConst.leftFileContent.getScrollPane().getVerticalScrollBar().getValue();
				CommonConst.rightFileContent.getScrollPane().getVerticalScrollBar().setValue(leftVal);
				
			}
		});
		
		CommonConst.rightFileContent.getScrollPane().addMouseWheelListener(new MouseWheelListener() {
			
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				
				int rightVal = CommonConst.rightFileContent.getScrollPane().getVerticalScrollBar().getValue();
				CommonConst.leftFileContent.getScrollPane().getVerticalScrollBar().setValue(rightVal);
			}
		});
		
		CommonConst.leftUpButton = CommonConst.bForm.addButton(0, 0, 0, 0, "▲");
		CommonConst.leftDownButton = CommonConst.bForm.addButton(0, 0, 0, 0, "▼");
		CommonConst.leftTopButton = CommonConst.bForm.addButton(0, 0, 0, 0, "B");
		CommonConst.leftBottomButton = CommonConst.bForm.addButton(0, 0, 0, 0, "E");
		
		CommonConst.bothDiffButton = CommonConst.bForm.addButton(0, 0, 0, 0, "D");
		CommonConst.bothUpButton = CommonConst.bForm.addButton(0, 0, 0, 0, "▲");
		CommonConst.bothDownButton = CommonConst.bForm.addButton(0, 0, 0, 0, "▼");
		CommonConst.bothTopButton = CommonConst.bForm.addButton(0, 0, 0, 0, "B");
		CommonConst.bothBottomButton = CommonConst.bForm.addButton(0, 0, 0, 0, "E");
		
		CommonConst.rightUpButton = CommonConst.bForm.addButton(0, 0, 0, 0, "▲");
		CommonConst.rightDownButton = CommonConst.bForm.addButton(0, 0, 0, 0, "▼");
		CommonConst.rightTopButton = CommonConst.bForm.addButton(0, 0, 0, 0, "B");
		CommonConst.rightBottomButton = CommonConst.bForm.addButton(0, 0, 0, 0, "E");
		
		DiffFormListener formListener = new DiffFormListener();
		CommonConst.bForm.addComponentListener(formListener);
		
		TreeUtil.drawTree(leftClassesDir, rightClassesDir);
		
		CommonConst.bForm.open();
		
		/**
		 * 크기 재설정
		 */
		formListener.doResizeForm();
	}
	
	private void addMenuBar(BBForm form) {
		JMenuBar menuBar = new JMenuBar();
		
		/*
		JMenu screenMenu = new JMenu("Menu1");
		screenMenu.add(new JMenuItem("SubMenu1"));
		screenMenu.add(new JMenuItem("SubMenu2"));
		screenMenu.add(new JMenuItem("SubMenu3"));
		screenMenu.addSeparator();
		screenMenu.add(new JMenuItem("SubMenu4"));

		mb.add(screenMenu);
		mb.add(new JMenu("Menu2"));
		mb.add(new JMenu("Menu3"));
		mb.add(new JMenu("Menu4"));
		mb.add(new JMenu("Menu5"));
		*/
		
		
		JMenu treeMenu = new JMenu("Tree");
		
		{
			final JMenuItem subMenu1 = new JMenuItem("전체 확장 (Expand All)");
			treeMenu.add(subMenu1);
			
			subMenu1.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					TreeUtil.expandTree();
				}
			});
		}
		
		{
			final JMenuItem subMenu1 = new JMenuItem("전체 축소 (Collapse All)");
			treeMenu.add(subMenu1);
			
			subMenu1.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					TreeUtil.collapseTree();
				}
			});
		}
		
		
		JMenu optionMenu = new JMenu("Option");
		
		{
			final JCheckBoxMenuItem subMenu1 = new JCheckBoxMenuItem("용량 차이가 0인 파일 숨기기 (Hide files that capacity difference is 0)");
			optionMenu.add(subMenu1);
			
			subMenu1.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean bState = subMenu1.getState();
					if (bState) {
						CommonConst.bHideCapacityGapIsZero = true;
						TreeUtil.redrawTree();
						
					} else {
						CommonConst.bHideCapacityGapIsZero = false;
						TreeUtil.redrawTree();
					}
				}
			});
		}
		
		{
			final JCheckBoxMenuItem subMenu2 = new JCheckBoxMenuItem("좌측에만 존재하는 파일/폴더 숨기기 (Hide files/dirs that exist only in left)");
			optionMenu.add(subMenu2);
			
			subMenu2.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean bState = subMenu2.getState();
					if (bState) {
						CommonConst.bHideLeftOnlyFileDir = true;
						TreeUtil.redrawTree();
						
					} else {
						CommonConst.bHideLeftOnlyFileDir = false;
						TreeUtil.redrawTree();
					}
				}
			});
		}
		
		{
			final JCheckBoxMenuItem subMenu3 = new JCheckBoxMenuItem("우측에만 존재하는 파일/폴더 숨기기 (Hide files/dirs that exist only in right)");
			optionMenu.add(subMenu3);
			
			subMenu3.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean bState = subMenu3.getState();
					if (bState) {
						CommonConst.bHideRightOnlyFileDir = true;
						TreeUtil.redrawTree();
						
					} else {
						CommonConst.bHideRightOnlyFileDir = false;
						TreeUtil.redrawTree();
					}
				}
			});
		}
		
		{
			final JCheckBoxMenuItem subMenu4 = new JCheckBoxMenuItem("좌측과 우측 모두에 존재하는 파일/폴더 숨기기 (Hide files/dirs that exist in both)");
			optionMenu.add(subMenu4);
			
			subMenu4.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean bState = subMenu4.getState();
					if (bState) {
						CommonConst.bHideBothFileDir = true;
						TreeUtil.redrawTree();
						
					} else {
						CommonConst.bHideBothFileDir = false;
						TreeUtil.redrawTree();
					}
				}
			});
		}
		
		
		menuBar.add(treeMenu);
		menuBar.add(optionMenu);
		
		
		form.setJMenuBar(menuBar);
	}
}

