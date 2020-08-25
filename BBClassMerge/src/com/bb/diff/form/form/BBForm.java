package com.bb.diff.form.form;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.bb.diff.common.CommonConst;
import com.bb.diff.form.button.BBButton;
import com.bb.diff.form.textarea.BBEditor;
import com.bb.diff.form.tree.BBTree;

public class BBForm extends JFrame {
	
	Container container = null;
	Font font = null;
	Font smallFont = null;
	
	public BBForm(int width, int height, String title) {
		container = getContentPane();
		container.setLayout(null);
		setSize(width, height);
		setBounds(200, 200, width, height);
		setTitle(title);		
		font = new Font("돋움", 13, 13);
		smallFont = new Font("돋움", 9, 9);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("사용자 명령으로 종료합니다.");
				System.exit(0);
			}
		});
	}
	
	public void open() {
		setVisible(true);
	}
	
	public void close() {
		setVisible(false);
	}
	
	public BBEditor addTextArea(int left, int top, int width, int height) {

//		요지는 textPane 을 scrollPane 에 넣으면 자동으로 line wrapping 되니까
//		textPane 을 JPanel 로 한번 감싼 다음에 scrollPane 에 JPanel 을 넣는 것.
		
//		JPanel jp = new JPanel();
		
		BBEditor obj = new BBEditor();
		obj.setBackground(Color.white);
		obj.setBounds(left, top, width, height);
		obj.setFont(font);
		
		JScrollPane scrollPane = new JScrollPane(obj);  //스크롤판 추가
		scrollPane.setBounds(left, top, width, height);
		container.add(scrollPane); //화면에는 스크롤판 추가
	
		// textPane 을 JPanel 로 한번 감싼 다음에
//		jp.add(obj);
//		// scrollPane 에 JPanel 을 넣는 것.
//		scrollPane.add(jp);
		
		obj.setScrollPane(scrollPane);
		return obj;
	}
	
	public JTextField addTextInput(int left, int top, int width, int height) {
		JTextField obj = new JTextField();
		obj.setBackground(Color.white);
		obj.setBounds(left, top, width, height);
		obj.setFont(font);
		
		container.add(obj);
		return obj;
	}
	
	public JLabel addLabel(int left, int top, int width, int height, String value) {
		JLabel obj = new JLabel();
		obj.setBackground(Color.white);
		obj.setBounds(left, top, width, height);
		obj.setText(value);
		obj.setFont(font);
		
		container.add(obj);
		return obj;
	}
	
	public BBTree addTree(int left, int top, int width, int height, BBTree obj) {
		obj.setBackground(Color.white);
		obj.setBounds(left, top, width, height);
		obj.setFont(font);
		
		// 스크롤판 추가
		JScrollPane scrollPane = new JScrollPane(obj);
		scrollPane.setBounds(left, top, width, height);
		
		// 화면에는 스크롤판 추가
		container.add(scrollPane);
		
		obj.setScrollPane(scrollPane);
		return obj;
	}
	
	public BBButton addButton(int left, int top, int width, int height, String value) {
		BBButton obj = new BBButton();
		obj.setBackground(CommonConst.buttonColor);
		obj.setBounds(left, top, width, height);
		obj.setText(value);
		obj.setFont(font);
		
		container.add(obj);
		return obj;
	}

}
