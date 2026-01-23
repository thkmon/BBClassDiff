package com.bb.diff.form.form;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;

import com.bb.classmerge.form.JFrameDesignUtil;
import com.bb.classmerge.form.MainForm;
import com.bb.classmerge.swing.WrapEditorKit;
import com.bb.diff.common.CommonConst;
import com.bb.diff.form.button.JButtonUtil;
import com.bb.diff.form.text.JTextFieldUtil;
import com.bb.diff.form.textarea.BBEditor;
import com.bb.diff.form.tree.BBTree;

public class BBForm extends JFrame {
	
	Container container = null;
	Font font = null;
	Font smallFont = null;
	
	public BBForm(int width, int height, String title) {
		container = getContentPane();
		container.setLayout(null);
		
		this.setSize(width, height);
		this.setBounds(200, 200, width, height);
		this.setTitle(title);
		
		font = MainForm.basicFont13;
		smallFont = MainForm.basicFont9;
		
		if (CommonConst.formBackgroundColor != null) {
			container.setBackground(CommonConst.formBackgroundColor);
			this.setBackground(CommonConst.formBackgroundColor);
		}
		
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
	
	public void focus() {
		// 윈도우 실행되어있을 때만 동작
		if (!this.isVisible()) {
			return;
		}
		
		// 최소화되어 있을 경우 윈도우 복원
		if (this.getState() == Frame.ICONIFIED) {
			this.setState(Frame.NORMAL);
		}
		
		// 윈도우 포커싱
		if (this.getFocusableWindowState()) {
			this.requestFocus();
		}
	}
	
	public BBEditor addTextArea(int left, int top, int width, int height) {
		
		BBEditor bbEditor = new BBEditor();
		bbEditor.setBackground(Color.white);
		bbEditor.setBounds(left, top, width, height);
		bbEditor.setFont(font);
		bbEditor.setEditorKit(new WrapEditorKit());
		
		// 스크롤판 추가
		JScrollPane scrollPane = new JScrollPane(bbEditor);
		scrollPane.setBounds(left, top, width, height);
		JFrameDesignUtil.setColorToJScrollPane(scrollPane);
		
		// FlatLaf 스타일의 연한 회색 테두리 사용
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
		
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		
		//////////////////////////////////////////////////
		// 에디터 좌측에 라인번호 추가 시작
		//////////////////////////////////////////////////
		JTextPane lineNumbers = new JTextPane();
        lineNumbers.setEditable(false);
        lineNumbers.setBackground(new Color(243, 243, 243));
        
        scrollPane.setRowHeaderView(lineNumbers);
        
        bbEditor.getDocument().addDocumentListener(new DocumentListener() {
            public String getText() {
                int caretPosition = bbEditor.getDocument().getLength();
                Element root = bbEditor.getDocument().getDefaultRootElement();
                String text = "1\n";
                for(int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
                    text += i + "\n";
                }
                return text;
            }
            
            @Override
            public void changedUpdate(DocumentEvent de) {
                lineNumbers.setText(getText());
            }
            
            @Override
            public void insertUpdate(DocumentEvent de) {
                lineNumbers.setText(getText());
            }
            
            @Override
            public void removeUpdate(DocumentEvent de) {
                lineNumbers.setText(getText());
            }
        });
		//////////////////////////////////////////////////
		// 에디터 좌측에 라인번호 추가 끝
		//////////////////////////////////////////////////
        
        
		//화면에는 스크롤판 추가
		container.add(scrollPane);
		
		bbEditor.setScrollPane(scrollPane);
		return bbEditor;
	}
	
	public JTextField addTextInput(int left, int top, int width, int height) {
		JTextField obj = JTextFieldUtil.createNewJTextField();
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
		JFrameDesignUtil.setColorToJScrollPane(scrollPane);
		
		// FlatLaf 스타일의 연한 회색 테두리 사용
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
		
		// 화면에는 스크롤판 추가
		container.add(scrollPane);
		
		obj.setScrollPane(scrollPane);
		return obj;
	}
	
	public JButton addButton(int left, int top, int width, int height, String value) {
		JButton obj = JButtonUtil.createNewJButton(value);
		obj.setBounds(left, top, width, height);
		obj.setFont(font);
		
		container.add(obj);
		return obj;
	}

}
