package com.bb.diff.form.form;

import java.awt.Desktop;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.plaf.IconUIResource;

import com.bb.classmerge.form.BBCheckBoxMenuItem;
import com.bb.classmerge.form.BBMenu;
import com.bb.classmerge.form.BBMenuBar;
import com.bb.classmerge.form.BBMenuItem;
import com.bb.classmerge.main.BBClassDiff;
import com.bb.classmerge.util.DateUtil;
import com.bb.classmerge.util.FileNameUtil;
import com.bb.diff.common.CommonConst;
import com.bb.diff.copy.CopyPath;
import com.bb.diff.copy.CopyPathList;
import com.bb.diff.copy.CopyUtil;
import com.bb.diff.decompile.DecompileUtil;
import com.bb.diff.file.FileUtil;
import com.bb.diff.form.button.BBWinmergeButtonMouseListener;
import com.bb.diff.form.textarea.EditorListener;
import com.bb.diff.form.textarea.EditorUtil;
import com.bb.diff.form.tree.BBTree;
import com.bb.diff.form.tree.BBTreeCellRenderer;
import com.bb.diff.form.tree.BBTreeNode;
import com.bb.diff.form.tree.TreeNodeIcon;
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
		
		String title = BBClassDiff.title;
		if (title == null) {
			title = "";
		}
		
		String version = BBClassDiff.version;
		if (version != null && version.length() > 0) {
			title = title + "_" + version;
		}
		
		CommonConst.bForm = new BBForm(CommonConst.fullWidth, CommonConst.fullHeight, title);
		addMenuBar(CommonConst.bForm);

		CommonConst.leftFilePathText = CommonConst.bForm.addTextInput(0, 0, 0, 0);
		CommonConst.rightFilePathText = CommonConst.bForm.addTextInput(0, 0, 0, 0);
		
		CommonConst.winmergeButton = CommonConst.bForm.addButton(4, CommonConst.textAreaTopMargin - 1 , 85, 24, "Compare");
		CommonConst.winmergeButton.addMouseListener(new BBWinmergeButtonMouseListener());
		
		CommonConst.diffPointLabel = CommonConst.bForm.addLabel(95, CommonConst.textAreaTopMargin - 2, 255, 24, "Diff Point : ");
		
		
		/**
		 * 기존 트리 추가하는 코드 (상속버전)
		 */
		// 트리노드 여닫는 아이콘 플러스/마이너스(+/-) 기호로 변경
		UIManager.put("Tree.collapsedIcon", new IconUIResource(new TreeNodeIcon('+')));
		UIManager.put("Tree.expandedIcon",  new IconUIResource(new TreeNodeIcon('-')));
		
		BBTreeNode rootNode = new BBTreeNode("Root");
		rootNode.setDir(true);
		
		BBTree bbTree = new BBTree(rootNode);
		CommonConst.treeModel = bbTree;
		
		BBTreeCellRenderer treeCellRenderer = new BBTreeCellRenderer();
		bbTree.setCellRenderer(treeCellRenderer);
		
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
		
		CommonConst.leftUpButton = CommonConst.bForm.addButton(0, 0, 0, 0, ""); // "▲"
		CommonConst.leftDownButton = CommonConst.bForm.addButton(0, 0, 0, 0, ""); // "▼"
		CommonConst.leftTopButton = CommonConst.bForm.addButton(0, 0, 0, 0, ""); // "B"
		CommonConst.leftBottomButton = CommonConst.bForm.addButton(0, 0, 0, 0, ""); // "E"
		
		CommonConst.bothDiffNextButton = CommonConst.bForm.addButton(0, 0, 0, 0, ""); // "D"
		CommonConst.bothDiffPrevButton = CommonConst.bForm.addButton(0, 0, 0, 0, ""); // "D"
		CommonConst.bothUpButton = CommonConst.bForm.addButton(0, 0, 0, 0, ""); // "▲"
		CommonConst.bothDownButton = CommonConst.bForm.addButton(0, 0, 0, 0, ""); // "▼"
		CommonConst.bothTopButton = CommonConst.bForm.addButton(0, 0, 0, 0, ""); // "B"
		CommonConst.bothBottomButton = CommonConst.bForm.addButton(0, 0, 0, 0, ""); // "E"
		
		CommonConst.rightUpButton = CommonConst.bForm.addButton(0, 0, 0, 0, ""); // "▲"
		CommonConst.rightDownButton = CommonConst.bForm.addButton(0, 0, 0, 0, ""); // "▼"
		CommonConst.rightTopButton = CommonConst.bForm.addButton(0, 0, 0, 0, ""); // "B"
		CommonConst.rightBottomButton = CommonConst.bForm.addButton(0, 0, 0, 0, ""); // "E"
		
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
		UIManager.put("PopupMenu.border", new LineBorder(CommonConst.menuBorderColor));
		BBMenuBar menuBar = new BBMenuBar();
		
		
		int menubarWidth = 200;
		int menubarHeight = 22;
		
		menuBar.setBackground(CommonConst.formBackgroundColor);
		menuBar.setBorderPainted(false);
		menuBar.setBounds(0, 0, menubarWidth, menubarHeight);
		
		
		BBMenu treeMenu = new BBMenu("Tree");
		treeMenu.setMnemonic(KeyEvent.VK_T); // 단축키 ALT + T
		
		{
			final BBMenuItem subMenu1 = new BBMenuItem("전체 확장 (Expand all)");
			treeMenu.add(subMenu1);
			
			subMenu1.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					TreeUtil.expandTree();
				}
			});
		}
		
		{
			final BBMenuItem subMenu2 = new BBMenuItem("전체 축소 (Collapse all)");
			treeMenu.add(subMenu2);
			
			subMenu2.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					TreeUtil.collapseTree();
				}
			});
		}
		
		{
			final BBMenuItem subMenu3 = new BBMenuItem("트리의 모든 파일 추출 (Extract all files in tree)");
			treeMenu.add(subMenu3);
			
			subMenu3.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// 트리의 표시된 모든 파일들을 가져온다.
					CopyPathList pathList = TreeUtil.getAllFilePathInTree(null);
					if (pathList != null && pathList.size() > 0) {
						
						try {
							String tempFolderName = "files_" + DateUtil.getTodayDateTimeWithUnderbar();
							File tempDir = new File("temp\\" + tempFolderName);
							if (!tempDir.exists()) {
								tempDir.mkdirs();
							} else {
								return;
							}
							
							CopyPath oneCopy = null;
							int pathCount = pathList.size();
							for (int i=0; i<pathCount; i++) {
								oneCopy = pathList.get(i);
								
								File originFile = new File(oneCopy.getOriginPath());
								File newFile = new File("temp\\" + tempFolderName + "\\" + oneCopy.getCorePath());
								
								CopyUtil.copyFileCore(originFile, newFile);
							}
							
							if (Desktop.getDesktop() != null) {
								Desktop.getDesktop().open(tempDir);
							}
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});
		}
		
		{
			final BBMenuItem subMenu4 = new BBMenuItem("트리의 모든 클래스 파일로부터 자바 파일 추출 (Extract java files from classes in tree)");
			treeMenu.add(subMenu4);
			
			subMenu4.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// 트리의 표시된 파일 중 클래스 파일들만 가져온다.
					CopyPathList pathList = TreeUtil.getAllFilePathInTree("class");
					if (pathList != null && pathList.size() > 0) {
						
						try {
							String tempFolderName = "files_" + DateUtil.getTodayDateTimeWithUnderbar();
							File tempDir = new File("temp\\" + tempFolderName);
							if (!tempDir.exists()) {
								tempDir.mkdirs();
							} else {
								return;
							}
							
							CopyPath oneCopy = null;
							int pathCount = pathList.size();
							for (int i=0; i<pathCount; i++) {
								oneCopy = pathList.get(i);
								
								StringBuffer decompiledContent = DecompileUtil.readClassFile(oneCopy.getOriginPath());
								
								// 디컴파일 성공시
								if (decompiledContent != null && decompiledContent.length() > 0) {
									File newFile = new File("temp\\" + tempFolderName + "\\" + FileNameUtil.replaceExtension(oneCopy.getCorePath(), "java"));
									
									// 폴더 만들기
									CopyUtil.makeParentDir(newFile.getAbsolutePath());
									
									// 파일 만들기
									newFile.createNewFile();
									
									FileUtil.writeFile(newFile, decompiledContent.toString());
									System.out.println("Decompiled : " + oneCopy.getOriginPath() + " => " + newFile.getAbsolutePath());
								}
							}
							
							if (Desktop.getDesktop() != null) {
								Desktop.getDesktop().open(tempDir);
							}
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			});
		}
		
		{
			final BBMenuItem subMenuLast = new BBMenuItem("새로고침 (Refresh)");
			treeMenu.add(subMenuLast);
			
			subMenuLast.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					TreeUtil.redrawTree(true);
				}
			});
		}
		
		BBMenu findMenu = new BBMenu("Find");
		findMenu.setMnemonic(KeyEvent.VK_F); // 단축키 ALT + F
		
		{
			final BBMenuItem subMenu1 = new BBMenuItem("파일명으로 찾기 (Find by filename)");
			subMenu1.setAccelerator(KeyStroke.getKeyStroke('F', Event.CTRL_MASK)); // 단축키 CTRL + F
			findMenu.add(subMenu1);
			
			subMenu1.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (CommonConst.quickFinderForm != null) {
						CommonConst.quickFinderForm.open();
					} else {
						CommonConst.quickFinderForm = new QuickFinderForm();
					}
				}
			});
		}
		
		BBMenu optionMenu = new BBMenu("Option");
		optionMenu.setMnemonic(KeyEvent.VK_O); // 단축키 ALT + O
		
		{
			final BBCheckBoxMenuItem subMenu1 = new BBCheckBoxMenuItem("용량 차이가 0인 파일 숨기기 (Hide files that capacity difference is 0)");
			
			// 초기값 세팅. true일 경우 check 처리
			if (CommonConst.bHideCapacityGapIsZero) {
				subMenu1.doClick();
			}
			
			optionMenu.add(subMenu1);
			
			subMenu1.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean bState = subMenu1.getState();
					if (bState) {
						CommonConst.bHideCapacityGapIsZero = true;
						TreeUtil.redrawTree(true);
						
					} else {
						CommonConst.bHideCapacityGapIsZero = false;
						TreeUtil.redrawTree(true);
					}
				}
			});
		}
		
		{
			final BBCheckBoxMenuItem subMenu2 = new BBCheckBoxMenuItem("좌측에만 존재하는 파일/폴더 숨기기 (Hide files/dirs that exist only in left)");
			
			// 초기값 세팅. true일 경우 check 처리
			if (CommonConst.bHideLeftOnlyFileDir) {
				subMenu2.doClick();
			}
			
			optionMenu.add(subMenu2);
			
			subMenu2.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean bState = subMenu2.getState();
					if (bState) {
						CommonConst.bHideLeftOnlyFileDir = true;
						TreeUtil.redrawTree(true);
						
					} else {
						CommonConst.bHideLeftOnlyFileDir = false;
						TreeUtil.redrawTree(true);
					}
				}
			});
		}
		
		{
			final BBCheckBoxMenuItem subMenu3 = new BBCheckBoxMenuItem("우측에만 존재하는 파일/폴더 숨기기 (Hide files/dirs that exist only in right)");
			
			// 초기값 세팅. true일 경우 check 처리
			if (CommonConst.bHideRightOnlyFileDir) {
				subMenu3.doClick();
			}
			
			optionMenu.add(subMenu3);
			
			subMenu3.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean bState = subMenu3.getState();
					if (bState) {
						CommonConst.bHideRightOnlyFileDir = true;
						TreeUtil.redrawTree(true);
						
					} else {
						CommonConst.bHideRightOnlyFileDir = false;
						TreeUtil.redrawTree(true);
					}
				}
			});
		}
		
		{
			final BBCheckBoxMenuItem subMenu4 = new BBCheckBoxMenuItem("좌측과 우측 모두에 존재하는 파일/폴더 숨기기 (Hide files/dirs that exist in both)");
			
			// 초기값 세팅. true일 경우 check 처리
			if (CommonConst.bHideBothFileDir) {
				subMenu4.doClick();
			}
			
			optionMenu.add(subMenu4);
			
			subMenu4.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean bState = subMenu4.getState();
					if (bState) {
						CommonConst.bHideBothFileDir = true;
						TreeUtil.redrawTree(true);
						
					} else {
						CommonConst.bHideBothFileDir = false;
						TreeUtil.redrawTree(true);
					}
				}
			});
		}
		
		{
			final BBCheckBoxMenuItem subMenu5 = new BBCheckBoxMenuItem("diff 정보가 없는 빈 폴더 숨기기 (Hide empty directory with no differences)");
			
			// 초기값 세팅. true일 경우 check 처리
			if (CommonConst.bHideEmptyDirWithNoDiff) {
				subMenu5.doClick();
			}
			
			optionMenu.add(subMenu5);
			
			subMenu5.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean bState = subMenu5.getState();
					if (bState) {
						CommonConst.bHideEmptyDirWithNoDiff = true;
						TreeUtil.redrawTree(true);
						
					} else {
						CommonConst.bHideEmptyDirWithNoDiff = false;
						TreeUtil.redrawTree(true);
					}
				}
			});
		}
		
//		{
//			final BBCheckBoxMenuItem subMenu6 = new BBCheckBoxMenuItem("디컴파일시 손상을 고려한 비교하기 (Diff considering breakage of decompile)");
//			
//			// 초기값 세팅. true일 경우 check 처리
//			// 디컴파일시 손상을 고려한 비교하기 사용 여부
//			if (CommonConst.bDiffConsideringBreakage) {
//				subMenu6.doClick();
//			}
//			
//			optionMenu.add(subMenu6);
//			
//			subMenu6.addActionListener(new ActionListener() {
//				
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					boolean bState = subMenu6.getState();
//					if (bState) {
//						CommonConst.bDiffConsideringBreakage = true;
//						TreeUtil.redrawTree(true);
//						
//					} else {
//						CommonConst.bDiffConsideringBreakage = false;
//						TreeUtil.redrawTree(true);
//					}
//				}
//			});
//		}
		
		{
			final BBCheckBoxMenuItem subMenu7 = new BBCheckBoxMenuItem("CVS/SVN 리비전 문자열 제외하고 비교하기 (Diff excepting CVS/SVN revision string)");
			
			// 초기값 세팅. true일 경우 check 처리
			// CVS/SVN 리비전 문자열 제외하고 비교하기 여부
			if (CommonConst.bDiffExceptingRivisionString) {
				subMenu7.doClick();
			}
			
			optionMenu.add(subMenu7);
			
			subMenu7.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean bState = subMenu7.getState();
					if (bState) {
						CommonConst.bDiffExceptingRivisionString = true;
						TreeUtil.redrawTree(true);
						
					} else {
						CommonConst.bDiffExceptingRivisionString = false;
						TreeUtil.redrawTree(true);
					}
				}
			});
		}
		
		{
			final BBCheckBoxMenuItem subMenu8 = new BBCheckBoxMenuItem("클래스 핵심 라인만 비교하기 (Diff core lines of classes only)");
			
			// 초기값 세팅. true일 경우 check 처리
			// 클래스 핵심 라인만 비교하기 여부
			if (CommonConst.bDiffCoreContents) {
				subMenu8.doClick();
			}
			
			optionMenu.add(subMenu8);
			
			subMenu8.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean bState = subMenu8.getState();
					if (bState) {
						CommonConst.bDiffCoreContents = true;
						TreeUtil.redrawTree(true);
						
					} else {
						CommonConst.bDiffCoreContents = false;
						TreeUtil.redrawTree(true);
					}
				}
			});
		}
			
		{
			final BBCheckBoxMenuItem subMenu9 = new BBCheckBoxMenuItem("내용 동일해도 파일내용 보기 (View file contents even if the files are the same)");
			
			// 초기값 세팅. true일 경우 check 처리
			// 내용 동일해도 파일내용 보기 여부
			if (CommonConst.bViewContentEvenIfSame) {
				subMenu9.doClick();
			}
			
			optionMenu.add(subMenu9);
			
			subMenu9.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean bState = subMenu9.getState();
					if (bState) {
						CommonConst.bViewContentEvenIfSame = true;
						
						if (CommonConst.currentSelectedTreeNode != null) {
							EditorUtil.loadFileByNode(CommonConst.currentSelectedTreeNode, false, true);
						}
						
					} else {
						CommonConst.bViewContentEvenIfSame = false;
						
						if (CommonConst.currentSelectedTreeNode != null) {
							EditorUtil.loadFileByNode(CommonConst.currentSelectedTreeNode, false, true);
						}
					}
				}
			});
		}
		
		
		
		menuBar.add(treeMenu);
		menuBar.add(findMenu);
		menuBar.add(optionMenu);
		
		
		// 우측 상단에 여백을 확보하기 위해 form에 BBMenuBar를 바로 추가하지 않고,
		// 좌측 상단에 JPanel을 만들고 JPanel에 BBMenuBar를 추가한다.
		// form.setJmenuBar(menuBar);
		JPanel jpanel = new JPanel();
		jpanel.add(menuBar);
		jpanel.setBorder(BorderFactory.createEmptyBorder());
		jpanel.setLayout(null);
		jpanel.setBounds(0, 0, menubarWidth, menubarHeight);
		
		form.add(jpanel);
	}
}

