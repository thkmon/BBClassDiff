package com.bb.diff.form.form;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import com.bb.diff.common.CommonConst;
import com.bb.diff.form.button.JButtonUtil;
import com.bb.diff.form.text.JTextFieldUtil;
import com.bb.diff.form.tree.TreeUtil;

/**
 * 빠른 찾기 윈도우
 *
 */
public class QuickFinderForm extends JFrame {
	private final String DEFAULT_TITLE = "Quick Finder";
	
	private final int DEFAULT_WIDTH = 428;
	private final int DEFAULT_HEIGHT = 90;
	
	private int topMargin = 10;
	
	private JTextField textField1 = null;

	private JButton findButton = null;

	private int buttonHeight = 24;

	// private Font basicFont = new Font("돋움", 0, 14);
	private Font smallFont = new Font("돋움", 0, 12);
	
	/**
	 * 빠른 찾기 윈도우 초기화
	 */
	public QuickFinderForm() {
		setLayout(null);
		
		addBasicWindowListener();
		
		addButton();
		addTextField();
		
		open();
	}
	
	/**
	 * 빠른 찾기 윈도우 열기
	 */
	public void open() {
		setTitle(DEFAULT_TITLE);
		
		if (CommonConst.bForm != null && CommonConst.bForm.getBounds() != null) {
			setBounds(CommonConst.bForm.getBounds().x + 100, CommonConst.bForm.getBounds().y + 100, DEFAULT_WIDTH, this.DEFAULT_HEIGHT);
		} else {
			setBounds(0, 0, DEFAULT_WIDTH, this.DEFAULT_HEIGHT);
		}
		
		// 최소화되어 있을 경우 윈도우 복원
		if (this.getState() == Frame.ICONIFIED) {
			this.setState(Frame.NORMAL);
		}
		
		// 윈도우 표시
		this.setVisible(true);
		
		// 윈도우 포커싱
		if (this.getFocusableWindowState()) {
			this.requestFocus();
		}
		
		// 텍스트 박스 포커싱
		textField1.requestFocus();
	}
	
	/**
	 * 빠른 찾기 윈도우 닫기
	 */
	public void close() {
		setVisible(false);
	}
	
	/**
	 * 찾기 실패 시 실행
	 * @param inputText
	 */
	public void doWhenFailToFind(String inputText) {
		this.setTitle("Fail to find \"" + inputText + "\"");
	}
	
	/**
	 * 윈도우 리스너 추가
	 */
	private void addBasicWindowListener() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
			}
		});
		
		addComponentListener(new ComponentListener() {
			public void componentShown(ComponentEvent e) {
			}

			public void componentResized(ComponentEvent e) {
			}

			public void componentMoved(ComponentEvent e) {
			}

			public void componentHidden(ComponentEvent e) {
			}
		});
	}
	
	/**
	 * 버튼 추가
	 */
	private void addButton() {
		findButton = JButtonUtil.createNewJButton("Find");
		findButton.setFont(this.smallFont);
		findButton.setBounds(320, topMargin, 80, this.buttonHeight);
		getContentPane().add(findButton);

		findButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CommonConst.quickFinderForm.doQuickFind();
			}
		});
		
		findButton.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					CommonConst.quickFinderForm.doQuickFind();
				} else if (e.getKeyCode() == 27)
					CommonConst.quickFinderForm.doCancel();
			}

			public void keyPressed(KeyEvent e) {
			}
		});
	}
	
	/**
	 * 텍스트박스 추가
	 */
	private void addTextField() {
		textField1 = JTextFieldUtil.createNewJTextField();
		textField1.setFont(this.smallFont);
		getContentPane().add(textField1);
		textField1.setText("");
		textField1.setBounds(10, topMargin, 300, 24);
		textField1.setCursor(new Cursor(2));

		textField1.requestFocus();

		textField1.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					CommonConst.quickFinderForm.doQuickFind();
				} else if (e.getKeyCode() == 27)
					CommonConst.quickFinderForm.doCancel();
			}

			public void keyPressed(KeyEvent e) {
			}
		});
	}
	
	/**
	 * 빠른 찾기
	 */
	private void doQuickFind() {
		TreeUtil.quickFind(CommonConst.fileTree, textField1.getText());
	}

	/**
	 * 취소(창 닫기)
	 */
	private void doCancel() {
		CommonConst.quickFinderForm.close();
		CommonConst.bForm.focus();
	}
}