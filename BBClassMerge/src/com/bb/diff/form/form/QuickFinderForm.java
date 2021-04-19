package com.bb.diff.form.form;

import com.bb.diff.common.CommonConst;
import com.bb.diff.form.tree.TreeUtil;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
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

public class QuickFinderForm extends JFrame {
	public static JTextField textField1 = null;

	private static JButton findButton = null;

	private int buttonHeight = 35;

	private Font basicFont = new Font("돋움", 0, 14);
	private Font smallFont = new Font("돋움", 0, 12);

	private int defaultHeight = 110;

	public QuickFinderForm(String title) {
		if (title == null) {
			title = "";
		}

		setLayout(null);
		setTitle(title);

		addBasicWindowListener();

		addButton();
		addTextField();

		setBounds(0, 0, 800, this.defaultHeight);
		setVisible(true);

		textField1.requestFocus();
	}

	public void open() {
		if (getState() == 1) {
			setState(0);
		}

		setVisible(true);

		if (getFocusableWindowState()) {
			requestFocus();
		}

		textField1.requestFocus();
	}

	public void close() {
		setVisible(false);
	}

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

	private void addButton() {
		findButton = new JButton("Find");
		findButton.setBackground(CommonConst.buttonColor);
		findButton.setFont(this.basicFont);
		findButton.setBounds(645, 10, 120, this.buttonHeight);
		getContentPane().add(findButton);

		findButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				QuickFinderForm.doQuickFind();
			}
		});
		
		findButton.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					QuickFinderForm.doQuickFind();
				} else if (e.getKeyCode() == 27)
					QuickFinderForm.doCancel();
			}

			public void keyPressed(KeyEvent e) {
			}
		});
	}

	private void addTextField() {
		textField1 = new JTextField();
		textField1.setFont(this.smallFont);
		getContentPane().add(textField1);
		textField1.setText("");
		textField1.setBounds(20, 10, 610, 35);
		textField1.setCursor(new Cursor(2));

		textField1.requestFocus();

		textField1.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					QuickFinderForm.doQuickFind();
				} else if (e.getKeyCode() == 27)
					QuickFinderForm.doCancel();
			}

			public void keyPressed(KeyEvent e) {
			}
		});
	}

	public static void setFormDisable() {
		textField1.setEnabled(false);

		findButton.setEnabled(false);
	}

	public static void setFormEnable() {
		textField1.setEnabled(true);

		findButton.setEnabled(true);
	}

	/**
	 * 빠른 찾기
	 */
	private static void doQuickFind() {
		TreeUtil.quickFind(CommonConst.fileTree, textField1.getText());
	}

	/**
	 * 취소(창 닫기)
	 */
	private static void doCancel() {
		CommonConst.quickFinderForm.close();
		CommonConst.bForm.focus();
	}
}