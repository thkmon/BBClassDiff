package com.bb.diff.form.form;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import com.bb.classmerge.form.BBMenuItem;
import com.bb.classmerge.util.ClipboardUtil;
import com.bb.classmerge.util.FilePathUtil;
import com.bb.diff.common.CommonConst;
import com.bb.diff.form.button.BBArrowButtonMouseListener;
import com.bb.diff.form.button.BBDiffNextButtonMouseListener;
import com.bb.diff.form.button.BBDiffPrevButtonMouseListener;
import com.bb.diff.form.textarea.BBEditor;
import com.bb.diff.image.ImageIconUtil;
import com.bb.diff.path.PathUtil;

public class DiffFormListener implements ComponentListener {
	
	private static final int DIVIDER_WIDTH = 1;
	private static final int DIVIDER_HOVER_WIDTH = 5; // 호버/드래그 시 활성 영역
	
	// 패널 비율 설정 (트리, 좌측 패널, 우측 패널)
	private static final int TREE_PANEL_RATIO = 14;
	private static final int LEFT_PANEL_RATIO = 43;
	private static final int RIGHT_PANEL_RATIO = 43;
	
	private int leftDividerX = -1;
	private int centerDividerX = -1;
	
	public DiffFormListener() {
		// 생성자에서 초기화
	}

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
		int prevFullWidth = CommonConst.fullWidth;
		CommonConst.fullWidth = CommonConst.bForm.getContentPane().getWidth();
		CommonConst.fullHeight = CommonConst.bForm.getContentPane().getHeight();
		
		// 초기화: 디바이더 위치가 설정되지 않았으면 기본 비율 사용
		// 디바이더 너비를 고려한 콘텐츠 영역 계산
		int availableWidth = CommonConst.fullWidth - CommonConst.treeLeftMargin - DIVIDER_WIDTH * 2 - 20; // 20px 우측 여백
		
		if (leftDividerX < 0 || (prevFullWidth > 0 && prevFullWidth != CommonConst.fullWidth)) {
			// 최초 초기화 또는 창 크기 변경 시: 14%-43%-43%로 리셋
			CommonConst.treeWidth = (int) (availableWidth * TREE_PANEL_RATIO / 100.0);
			leftDividerX = CommonConst.treeLeftMargin + CommonConst.treeWidth;
			// 중앙 디바이더: 트리 + 좌측 패널 비율만큼 이동
			int leftPanelWidth = (int) (availableWidth * LEFT_PANEL_RATIO / 100.0);
			centerDividerX = leftDividerX + DIVIDER_WIDTH + leftPanelWidth;
		} else {
			// 수동 조정 후: 크기 유지
			CommonConst.treeWidth = leftDividerX - CommonConst.treeLeftMargin;
		}
		
		int boxHeight = CommonConst.fullHeight - CommonConst.treeTopMargin - CommonConst.bottomMargin;
		
		// 좌측 패널 위치와 너비
		int box1Left = leftDividerX + DIVIDER_WIDTH;
		int box1Width = centerDividerX - box1Left;
		
		// 우측 패널 위치와 너비 (창 끝까지, 오른쪽에 DiffOverviewPanel 공간 확보)
		int box2Left = centerDividerX + DIVIDER_WIDTH;
		int diffOverviewWidth = 16;
		int box2Width = CommonConst.fullWidth - box2Left - 20 - diffOverviewWidth; // 20px 여백 + 개요 패널
		
		int arrowButtonTop = 0;
		
		
		
		
		/**
		 * 바운더리 적용
		 */
		// 디바이더 설정
		setupDividers(boxHeight, box2Left, box2Width, diffOverviewWidth);
		
		// 트리
		CommonConst.fileTree.setBounds(0, 0, CommonConst.treeWidth, boxHeight);
		CommonConst.fileTree.getScrollPane().setBounds(CommonConst.treeLeftMargin, CommonConst.treeTopMargin, CommonConst.treeWidth, boxHeight);
		
		
		// 좌측 파일패스
		CommonConst.leftFilePathText.setBounds(box1Left, CommonConst.textAreaTopMargin, box1Width, CommonConst.textAreaHeight);
		CommonConst.leftFilePathText.setEditable(false);
		
		CommonConst.leftFilePathText.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// 마우스 우클릭
				if (e.getButton() == 3) {
					JPopupMenu popup = getTextFieldPopupMenu(CommonConst.leftFilePathText);
					popup.show(CommonConst.leftFilePathText, e.getX() + 10, e.getY());
				}
			}
		});
		
		// 좌측 파일내용
		CommonConst.leftFileContent.setBounds(0, 0, box1Width, boxHeight);
		CommonConst.leftFileContent.getScrollPane().setBounds(box1Left, CommonConst.treeTopMargin, box1Width, boxHeight);
		
		CommonConst.leftFileContent.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// 마우스 우클릭
				if (e.getButton() == 3) {
					JPopupMenu popup = getEditorPopupMenu(CommonConst.leftFileContent);
					popup.show(CommonConst.leftFileContent, e.getX() + 10, e.getY());
				}
			}
		});
		
		// 좌측 버튼
		int leftButtonLeft = box1Left;
		CommonConst.leftUpButton.setBounds(leftButtonLeft, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		CommonConst.leftUpButton.setIcon(ImageIconUtil.getIconFromName("button", "up"));
		
		leftButtonLeft = leftButtonLeft + CommonConst.arrowButtonWidth;
		CommonConst.leftDownButton.setBounds(leftButtonLeft, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		CommonConst.leftDownButton.setIcon(ImageIconUtil.getIconFromName("button", "down"));
		
		leftButtonLeft = leftButtonLeft + CommonConst.arrowButtonWidth;
		CommonConst.leftTopButton.setBounds(leftButtonLeft, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		CommonConst.leftTopButton.setIcon(ImageIconUtil.getIconFromName("button", "top"));
		
		leftButtonLeft = leftButtonLeft + CommonConst.arrowButtonWidth;
		CommonConst.leftBottomButton.setBounds(leftButtonLeft, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		CommonConst.leftBottomButton.setIcon(ImageIconUtil.getIconFromName("button", "bottom"));
		
		// 버튼 액션
		CommonConst.leftTopButton.addMouseListener(new BBArrowButtonMouseListener   (true, false, true, true));
		CommonConst.leftBottomButton.addMouseListener(new BBArrowButtonMouseListener(true, false, false, true));
		CommonConst.leftUpButton.addMouseListener(new BBArrowButtonMouseListener    (true, false, true, false));
		CommonConst.leftDownButton.addMouseListener(new BBArrowButtonMouseListener  (true, false, false, false));
		
		CommonConst.bForm.getContentPane().revalidate();
		CommonConst.bForm.getContentPane().repaint();
		
		
		// 우측 파일패스
		CommonConst.rightFilePathText.setBounds(box2Left, CommonConst.textAreaTopMargin, box2Width, CommonConst.textAreaHeight);
		CommonConst.rightFilePathText.setEditable(false);
		
		CommonConst.rightFilePathText.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// 마우스 우클릭
				if (e.getButton() == 3) {
					JPopupMenu popup = getTextFieldPopupMenu(CommonConst.rightFilePathText);
					popup.show(CommonConst.rightFilePathText, e.getX() + 10, e.getY());	
				}
			}
		});
		
		// 우측 파일내용
		CommonConst.rightFileContent.setBounds(0, 0, box2Width, boxHeight);
		CommonConst.rightFileContent.getScrollPane().setBounds(box2Left, CommonConst.treeTopMargin, box2Width, boxHeight);
		
		CommonConst.rightFileContent.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// 마우스 우클릭
				if (e.getButton() == 3) {
					JPopupMenu popup = getEditorPopupMenu(CommonConst.rightFileContent);
					popup.show(CommonConst.rightFileContent, e.getX() + 10, e.getY());
				}
			}
		});
		
		// 우측 버튼
		int rightButtonLeft = box2Left;
		CommonConst.rightUpButton.setBounds(rightButtonLeft, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		CommonConst.rightUpButton.setIcon(ImageIconUtil.getIconFromName("button", "up"));
		
		rightButtonLeft = rightButtonLeft + CommonConst.arrowButtonWidth;
		CommonConst.rightDownButton.setBounds(rightButtonLeft, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		CommonConst.rightDownButton.setIcon(ImageIconUtil.getIconFromName("button", "down"));
		
		rightButtonLeft = rightButtonLeft + CommonConst.arrowButtonWidth;
		CommonConst.rightTopButton.setBounds(rightButtonLeft, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		CommonConst.rightTopButton.setIcon(ImageIconUtil.getIconFromName("button", "top"));
		
		rightButtonLeft = rightButtonLeft + CommonConst.arrowButtonWidth;
		CommonConst.rightBottomButton.setBounds(rightButtonLeft, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		CommonConst.rightBottomButton.setIcon(ImageIconUtil.getIconFromName("button", "bottom"));
		
		
		// 버튼 액션
		CommonConst.rightTopButton.addMouseListener(new BBArrowButtonMouseListener   (false, true, true, true));
		CommonConst.rightBottomButton.addMouseListener(new BBArrowButtonMouseListener(false, true, false, true));
		CommonConst.rightUpButton.addMouseListener(new BBArrowButtonMouseListener   (false, true, true, false));
		CommonConst.rightDownButton.addMouseListener(new BBArrowButtonMouseListener  (false, true, false, false));
		
		
		// 공통 버튼
		int bothArrowButton1Left = box2Left + box2Width - ((CommonConst.arrowButtonWidth) * 5) - CommonConst.arrowButtonWidth;
		CommonConst.bothDiffPrevButton.setBounds(bothArrowButton1Left, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		CommonConst.bothDiffPrevButton.setIcon(ImageIconUtil.getIconFromName("button", "diff_prev"));
		
		bothArrowButton1Left = bothArrowButton1Left + CommonConst.arrowButtonWidth;
		CommonConst.bothDiffNextButton.setBounds(bothArrowButton1Left, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		CommonConst.bothDiffNextButton.setIcon(ImageIconUtil.getIconFromName("button", "diff_next"));
		
		bothArrowButton1Left = bothArrowButton1Left + CommonConst.arrowButtonWidth;
		CommonConst.bothUpButton.setBounds(bothArrowButton1Left, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		CommonConst.bothUpButton.setIcon(ImageIconUtil.getIconFromName("button", "up2"));
		
		bothArrowButton1Left = bothArrowButton1Left + CommonConst.arrowButtonWidth;
		CommonConst.bothDownButton.setBounds(bothArrowButton1Left, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		CommonConst.bothDownButton.setIcon(ImageIconUtil.getIconFromName("button", "down2"));
		
		bothArrowButton1Left = bothArrowButton1Left + CommonConst.arrowButtonWidth;
		CommonConst.bothTopButton.setBounds(bothArrowButton1Left, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		CommonConst.bothTopButton.setIcon(ImageIconUtil.getIconFromName("button", "top2"));
		
		bothArrowButton1Left = bothArrowButton1Left + CommonConst.arrowButtonWidth;		
		CommonConst.bothBottomButton.setBounds(bothArrowButton1Left, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		CommonConst.bothBottomButton.setIcon(ImageIconUtil.getIconFromName("button", "bottom2"));
		
		
		// 버튼 액션
		CommonConst.bothDiffNextButton.addMouseListener(new BBDiffNextButtonMouseListener());
		CommonConst.bothDiffPrevButton.addMouseListener(new BBDiffPrevButtonMouseListener());
		CommonConst.bothTopButton.addMouseListener(new BBArrowButtonMouseListener      (true, true, true, true));
		CommonConst.bothBottomButton.addMouseListener(new BBArrowButtonMouseListener   (true, true, false, true));
		CommonConst.bothUpButton.addMouseListener(new BBArrowButtonMouseListener       (true, true, true, false));
		CommonConst.bothDownButton.addMouseListener(new BBArrowButtonMouseListener     (true, true, false, false));
	}
	
	/**
	 * 디바이더 설정 및 드래그 이벤트 추가
	 */
	private void setupDividers(int boxHeight, int box2Left, int box2Width, int diffOverviewWidth) {
		// 좌측 디바이더 (트리와 좌측 패널 사이)
		CommonConst.leftDivider.setBounds(leftDividerX, CommonConst.treeTopMargin, DIVIDER_WIDTH, boxHeight);
		CommonConst.leftDivider.setBackground(new Color(230, 230, 230));
		CommonConst.leftDivider.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
		
		// 중앙 디바이더 (좌측과 우측 패널 사이)
		CommonConst.centerDivider.setBounds(centerDividerX, CommonConst.treeTopMargin, DIVIDER_WIDTH, boxHeight);
		CommonConst.centerDivider.setBackground(new Color(230, 230, 230));
		CommonConst.centerDivider.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
		
		// DiffOverviewPanel 설정 (우측 에디터 오른쪽에 배치)
		if (CommonConst.diffOverviewPanel != null) {
			int overviewLeft = box2Left + box2Width + 2;
			CommonConst.diffOverviewPanel.setBounds(overviewLeft, CommonConst.treeTopMargin, diffOverviewWidth, boxHeight);
			CommonConst.diffOverviewPanel.repaint();
		}
		
		// 마우스 이벤트 설정 (한 번만 추가)
		if (CommonConst.leftDivider.getMouseListeners().length == 0) {
			setupLeftDividerListeners();
		}
		
		if (CommonConst.centerDivider.getMouseListeners().length == 0) {
			setupCenterDividerListeners();
		}
	}
	
	/**
	 * 좌측 디바이더 드래그 이벤트
	 */
	private void setupLeftDividerListeners() {
		final int[] dragStartX = {0};
		final int[] initialDividerX = {0};
		
		CommonConst.leftDivider.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				dragStartX[0] = e.getXOnScreen();
				initialDividerX[0] = leftDividerX;
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// 호버 시 더 두껑게 표시
				CommonConst.leftDivider.setBackground(new Color(180, 180, 180));
				CommonConst.leftDivider.setBounds(
					leftDividerX - (DIVIDER_HOVER_WIDTH - DIVIDER_WIDTH) / 2,
					CommonConst.treeTopMargin,
					DIVIDER_HOVER_WIDTH,
					CommonConst.leftDivider.getHeight()
				);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// 호버 종료 시 얼게
				CommonConst.leftDivider.setBackground(new Color(230, 230, 230));
				CommonConst.leftDivider.setBounds(
					leftDividerX,
					CommonConst.treeTopMargin,
					DIVIDER_WIDTH,
					CommonConst.leftDivider.getHeight()
				);
			}
		});
		
		CommonConst.leftDivider.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int deltaX = e.getXOnScreen() - dragStartX[0];
				int newX = initialDividerX[0] + deltaX;
				
				// 최소/최대 제한
				if (newX < 100) newX = 100;
				if (newX > centerDividerX - 200) newX = centerDividerX - 200;
				
				leftDividerX = newX;
				doResizeForm();
			}
		});
	}
	
	/**
	 * 중앙 디바이더 드래그 이벤트
	 */
	private void setupCenterDividerListeners() {
		final int[] dragStartX = {0};
		final int[] initialDividerX = {0};
		
		CommonConst.centerDivider.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				dragStartX[0] = e.getXOnScreen();
				initialDividerX[0] = centerDividerX;
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// 호버 시 더 두껑게 표시
				CommonConst.centerDivider.setBackground(new Color(180, 180, 180));
				CommonConst.centerDivider.setBounds(
					centerDividerX - (DIVIDER_HOVER_WIDTH - DIVIDER_WIDTH) / 2,
					CommonConst.treeTopMargin,
					DIVIDER_HOVER_WIDTH,
					CommonConst.centerDivider.getHeight()
				);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// 호버 종료 시 얼게
				CommonConst.centerDivider.setBackground(new Color(230, 230, 230));
				CommonConst.centerDivider.setBounds(
					centerDividerX,
					CommonConst.treeTopMargin,
					DIVIDER_WIDTH,
					CommonConst.centerDivider.getHeight()
				);
			}
		});
		
		CommonConst.centerDivider.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int deltaX = e.getXOnScreen() - dragStartX[0];
				int newX = initialDividerX[0] + deltaX;
				
				// 최소/최대 제한
				if (newX < leftDividerX + DIVIDER_WIDTH + 200) newX = leftDividerX + DIVIDER_WIDTH + 200;
				if (newX > CommonConst.fullWidth - 200) newX = CommonConst.fullWidth - 200;
				
				centerDividerX = newX;
				doResizeForm();
			}
		});
	}
	
	/**
	 * 상단 파일명 텍스트박스 마우스 우클릭 시 팝업메뉴
	 * 
	 * @param textField
	 * @return
	 */
	private JPopupMenu getTextFieldPopupMenu(final JTextField textField) {
		JPopupMenu popupMenu = new JPopupMenu();
		
		{
			final BBMenuItem subMenu = new BBMenuItem("파일경로 복사 (Copy file path)");
			subMenu.setMnemonic(KeyEvent.VK_C); // 단축키 ALT + C
			popupMenu.add(subMenu);
			
			subMenu.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					String filePath = textField.getText();
					if (filePath == null) {
						filePath = "";
					}
					
					ClipboardUtil.copyToClipboard(filePath);
				}
			});
			
			String text = textField.getText();
			if (text != null && text.equals("{empty}")) {
				subMenu.setEnabled(false);
			} else {
				subMenu.setEnabled(true);
			}
		}
		
		{
			final BBMenuItem subMenu = new BBMenuItem("파일명 복사 (Copy file name)");
			subMenu.setMnemonic(KeyEvent.VK_N); // 단축키 ALT + N
			popupMenu.add(subMenu);
			
			subMenu.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					String filePath = textField.getText();
					if (filePath == null) {
						filePath = "";
					}
					
					String fileName = PathUtil.getFileNameWithExt(filePath);
					if (fileName == null) {
						fileName = "";
					}
					
					ClipboardUtil.copyToClipboard(fileName);
				}
			});
			
			String text = textField.getText();
			if (text != null && text.equals("{empty}")) {
				subMenu.setEnabled(false);
			} else {
				subMenu.setEnabled(true);
			}
		}
		
		{
			final BBMenuItem subMenu = new BBMenuItem("상위폴더 열기 (Open parent folder)");
			subMenu.setMnemonic(KeyEvent.VK_F); // 단축키 ALT + F
			popupMenu.add(subMenu);
			
			subMenu.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					String filePath = textField.getText();
					FilePathUtil.openParentFolder(filePath);
				}
			});
			
			String text = textField.getText();
			if (text != null && text.equals("{empty}")) {
				subMenu.setEnabled(false);
			} else {
				subMenu.setEnabled(true);
			}
		}
		
		{
			final BBMenuItem subMenu = new BBMenuItem("파일 열기 (Open file)");
			subMenu.setMnemonic(KeyEvent.VK_O); // 단축키 ALT + O
			popupMenu.add(subMenu);
			
			subMenu.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					String filePath = textField.getText();
					FilePathUtil.openFile(filePath);
				}
			});
			
			String text = textField.getText();
			if (text != null && text.equals("{empty}")) {
				subMenu.setEnabled(false);
			} else {
				subMenu.setEnabled(true);
			}
		}
		
		return popupMenu;
	}
	
	
	/**
	 * 좌우 파일내용 박스 마우스 우클릭 시 팝업메뉴
	 * 
	 * @param textField
	 * @return
	 */
	private JPopupMenu getEditorPopupMenu(final BBEditor editor) {
		JPopupMenu popupMenu = new JPopupMenu();
		
		final BBMenuItem subMenu1 = new BBMenuItem("복사 (Copy)");
		subMenu1.setMnemonic(KeyEvent.VK_C); // 단축키 ALT + C
		popupMenu.add(subMenu1);
		
		subMenu1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String copyString = "";
				
				if (editor != null) {
					copyString = editor.getSelectedText();
				}
				
				if (copyString == null) {
					copyString = "";
				}
				
				ClipboardUtil.copyToClipboard(copyString);
			}
		});
		
		final BBMenuItem subMenu2 = new BBMenuItem("모두 선택 (Select All)");
		subMenu2.setMnemonic(KeyEvent.VK_N); // 단축키 ALT + N
		popupMenu.add(subMenu2);
		
		subMenu2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (editor != null) {
					editor.selectAll();
				}
			}
		});
		
		return popupMenu;
	}
}