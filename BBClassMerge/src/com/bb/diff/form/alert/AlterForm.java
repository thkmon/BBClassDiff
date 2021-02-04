package com.bb.diff.form.alert;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.bb.diff.common.CommonConst;

public class AlterForm {
	
	
	public static void open(String msg) {
		open(msg, CommonConst.errLogWidth, CommonConst.errLogHeight);
	}
	
	
	private static BasicForm aForm = null;
	private static JScrollPane textPane = null;
	private static JTextArea textArea = null;
	private static JButton okButton = null;
	private static String contentMsg = null;
	
	
	public static void open(String msg, int width, int height) {
		if (aForm != null) {
			textArea.setText(msg);
			
			aForm.setBounds(200, 200, width, height);
			aForm.open();
			return;
		}
		
		aForm = new BasicForm(width, height, "MSG");
		
		if (msg == null || msg.trim().length() == 0) {
			msg = "null";
		}

		contentMsg = msg;
		initForm(width, height);
		
		aForm.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent evt) {
				resizeForm(aForm.getWidth(), aForm.getHeight());
	        }
		});
		
		aForm.open();
		resizeForm(aForm.getWidth(), aForm.getHeight());
	}
	
	
	private static void resizeForm(int formWidth, int formHeight) {
		int textAreaHeight = formHeight - 120;
		
		textArea.setBounds(10, 10, formWidth - 40, textAreaHeight);
		textPane.setBounds(10, 10, formWidth - 40, textAreaHeight);
		okButton.setBounds(10, textAreaHeight + 20, formWidth - 40, 30);
	}
	
	
	private static void initForm(int width, int height) {
		textArea = aForm.addTextArea(10, 10, width - 40, height - 120);
		textArea.setText(contentMsg);
		textArea.setEditable(false);
		
		// 첫번째 컴포넌트인 JScrollPane 를 꺼낸다.
		// textArea 가 JScrollPane 에 담겨있기 때문에 JScrollPane 를 리사이즈 해줘야 한다.
		textPane = aForm.addScrollPane(textArea, 10, 10, width - 40, height - 120);
		
		okButton = aForm.addButton(10, height - 100, width - 40, 30, "OK");
		okButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				aForm.close();
			}
		});
		
		aForm.repaint();
	}
}
