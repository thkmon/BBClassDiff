package com.bb.diff.form.alert;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.bb.classmerge.form.JFrameDesignUtil;
import com.bb.classmerge.form.MainForm;
import com.bb.diff.common.CommonConst;
import com.bb.diff.form.button.JButtonUtil;
import com.bb.diff.form.text.JTextFieldUtil;

public class BasicForm extends JFrame {
	
	private Container container = null;
	private Font font = null;
	
	
	public BasicForm(int width, int height, String title) {
		container = getContentPane();
		container.setLayout(null);
		setSize(width, height);
		setBounds(200, 200, width, height);
		
		if (CommonConst.formBackgroundColor != null) {
			setBackground(CommonConst.formBackgroundColor);
			container.setBackground(CommonConst.formBackgroundColor);
		}
		
		setTitle(title);		
		font = MainForm.basicFont13;
		
//		this.addWindowListener(new WindowAdapter() {
//			public void windowClosing(WindowEvent e) {
//				System.out.println("사용자 명령으로 종료합니다.");
//				System.exit(0);
//			}
//		});
		
//		this.addMouseMotionListener(new MouseMotionListener() {
//			@Override
//			public void mouseMoved(MouseEvent e) {
//				setCursor(Cursor.DEFAULT_CURSOR);
//			}
//			
//			@Override
//			public void mouseDragged(MouseEvent e) {
//			}
//		});
	}
	
	
	// 191205 창이 최소화 되어있거나 포커스를 잃어버린 경우에도 창을 보여준다.
	public void open() {
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
	}
	
	
	public void close() {
		this.setVisible(false);
	}
	
	
	public JScrollPane addScrollPane(JTextArea obj, int left, int top, int width, int height) {
		JScrollPane scrollPane = new JScrollPane(obj);
		scrollPane.setBounds(left, top, width, height);
		JFrameDesignUtil.setColorToJScrollPane(scrollPane);
		
		// FlatLaf 스타일의 연한 회색 테두리 사용
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
		
		addComponentObj(scrollPane);
		return scrollPane;
	}
	
	
	public JTextArea addTextArea(int left, int top, int width, int height) {

		JTextArea obj = new JTextArea();
		obj.setBackground(Color.white);
		obj.setBounds(left, top, width, height);
		obj.setFont(font);
		return obj;
	}
	
	
	public JTextField addTextInput(int left, int top, int width, int height) {
		JTextField obj = JTextFieldUtil.createNewJTextField();
		obj.setBounds(left, top, width, height);
		obj.setFont(font);
		
		addComponentObj(obj);
		return obj;
	}
	
	
	public JLabel addLabel(int left, int top, int width, int height, String value) {
		JLabel obj = new JLabel();
		obj.setBackground(Color.white);
		obj.setBounds(left, top, width, height);
		obj.setText(value);
		obj.setFont(font);
		
//		obj.addMouseMotionListener(new MouseMotionListener() {
//			@Override
//			public void mouseMoved(MouseEvent e) {
//				setCursor(Cursor.DEFAULT_CURSOR);
//			}
//			
//			@Override
//			public void mouseDragged(MouseEvent e) {
//			}
//		});
		
		addComponentObj(obj);
		return obj;
	}
	
	
	public JButton addButton(int left, int top, int width, int height, String value) {
		JButton obj = JButtonUtil.createNewJButton(value);
		if (CommonConst.buttonBackgroundColor != null) {
			obj.setBackground(CommonConst.buttonBackgroundColor);
		}
		obj.setBounds(left, top, width, height);
		obj.setFont(font);
		if (CommonConst.buttonTextColor != null) {
			obj.setForeground(CommonConst.buttonTextColor);
		}
		
//		obj.addMouseMotionListener(new MouseMotionListener() {
//			@Override
//			public void mouseMoved(MouseEvent e) {
//				setCursor(Cursor.HAND_CURSOR);
//			}
//			
//			@Override
//			public void mouseDragged(MouseEvent e) {
//			}
//		});
		
		addComponentObj(obj);
		return obj;
	}
	
	
	public JCheckBox addCheckBox(int left, int top, int width, int height, String value) {
		JCheckBox checkBox = new JCheckBox();
		if (CommonConst.formBackgroundColor != null) {
			checkBox.setBackground(CommonConst.formBackgroundColor);
		}
		checkBox.setBounds(left, top, width, height);
		checkBox.setText(value);
		checkBox.setFont(font);
		
//		checkBox.addMouseMotionListener(new MouseMotionListener() {
//			@Override
//			public void mouseMoved(MouseEvent e) {
//				setCursor(Cursor.HAND_CURSOR);
//			}
//			
//			@Override
//			public void mouseDragged(MouseEvent e) {
//			}
//		});
		
		addComponentObj(checkBox);
		return checkBox;
	}
	
	
	private void addComponentObj(Component comp) {
		container.add(comp);
	}
}
