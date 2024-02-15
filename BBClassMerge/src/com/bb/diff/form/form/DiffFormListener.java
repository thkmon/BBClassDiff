package com.bb.diff.form.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.bb.classmerge.form.BBMenuItem;
import com.bb.classmerge.util.ClipboardUtil;
import com.bb.diff.common.CommonConst;
import com.bb.diff.form.button.BBArrowButtonMouseListener;
import com.bb.diff.form.button.BBDiffButtonMouseListener;
import com.bb.diff.form.textarea.BBEditor;
import com.bb.diff.image.ImageIconUtil;
import com.bb.diff.path.PathUtil;

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
		
		int arrowButtonTop = 0;
		
		
		
		
		/**
		 * 바운더리 적용
		 */
		// 트리
		CommonConst.fileTree.setBounds(0, 0, CommonConst.treeWidth, boxHeight);
		CommonConst.fileTree.getScrollPane().setBounds(CommonConst.treeLeftMargin, CommonConst.treeTopMargin, CommonConst.treeWidth, boxHeight);
		
		
		// 좌측 파일패스
		CommonConst.leftFilePathText.setBounds(box1Left, CommonConst.textAreaTopMargin, boxWidth, CommonConst.textAreaHeight);
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
		CommonConst.leftFileContent.setBounds(0, 0, boxWidth, boxHeight);
		CommonConst.leftFileContent.getScrollPane().setBounds(box1Left, CommonConst.treeTopMargin, boxWidth, boxHeight);
		
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
		
		
		// 우측 파일패스
		CommonConst.rightFilePathText.setBounds(box2Left, CommonConst.textAreaTopMargin, boxWidth, CommonConst.textAreaHeight);
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
		CommonConst.rightFileContent.setBounds(0, 0, boxWidth, boxHeight);
		CommonConst.rightFileContent.getScrollPane().setBounds(box2Left, CommonConst.treeTopMargin, boxWidth, boxHeight);
		
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
		int bothArrowButton1Left = box2Left + boxWidth - ((CommonConst.arrowButtonWidth) * 4) - CommonConst.arrowButtonWidth;
		CommonConst.bothDiffButton.setBounds(bothArrowButton1Left, arrowButtonTop, CommonConst.arrowButtonWidth, CommonConst.arrowButtonHeight);
		CommonConst.bothDiffButton.setIcon(ImageIconUtil.getIconFromName("button", "next_diff"));
		
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
		CommonConst.bothDiffButton.addMouseListener(new BBDiffButtonMouseListener());
		CommonConst.bothTopButton.addMouseListener(new BBArrowButtonMouseListener      (true, true, true, true));
		CommonConst.bothBottomButton.addMouseListener(new BBArrowButtonMouseListener   (true, true, false, true));
		CommonConst.bothUpButton.addMouseListener(new BBArrowButtonMouseListener       (true, true, true, false));
		CommonConst.bothDownButton.addMouseListener(new BBArrowButtonMouseListener     (true, true, false, false));
	}
	
	/**
	 * 상단 파일명 텍스트박스 마우스 우클릭 시 팝업메뉴
	 * 
	 * @param textField
	 * @return
	 */
	private JPopupMenu getTextFieldPopupMenu(final JTextField textField) {
		JPopupMenu popupMenu = new JPopupMenu();
		
		final BBMenuItem subMenu1 = new BBMenuItem("파일경로 복사 (Copy file path)");
		subMenu1.setMnemonic(KeyEvent.VK_C); // 단축키 ALT + C
		popupMenu.add(subMenu1);
		
		subMenu1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String filePath = textField.getText();
				if (filePath == null) {
					filePath = "";
				}
				
				ClipboardUtil.copyToClipboard(filePath);
			}
		});
		
		final BBMenuItem subMenu2 = new BBMenuItem("파일명 복사 (Copy file name)");
		subMenu2.setMnemonic(KeyEvent.VK_N); // 단축키 ALT + N
		popupMenu.add(subMenu2);
		
		subMenu2.addActionListener(new ActionListener() {
			
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