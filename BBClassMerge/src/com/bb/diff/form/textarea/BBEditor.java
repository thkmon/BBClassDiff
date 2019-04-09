package com.bb.diff.form.textarea;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.StringReader;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.ChangeListener;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.JTextComponent;

public class BBEditor extends JTextPane {
	private JScrollPane scrollPane = null;
	
	public JScrollPane getScrollPane() {	
		return scrollPane;
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
//		요지는 textPane 을 scrollPane 에 넣으면 자동으로 line wrapping 되니까
//		textPane 을 JPanel 로 한번 감싼 다음에 scrollPane 에 JPanel 을 넣는 것.
	}
	
	public int getScrollY() {
		int y = this.getScrollPane().getVerticalScrollBar().getValue();
		return y;
	}
	
	public void setScrollY(int newY) {
		this.getScrollPane().getVerticalScrollBar().setValue(newY);
	}
	
	public void addScrollY(int yToAdd) {
		this.setScrollY(this.getScrollY() + yToAdd);
	}
	
	public void setScrollBottom() {
		this.scrollPane.getVerticalScrollBar().setValue(this.scrollPane.getVerticalScrollBar().getMaximum());
	}
	
	public void setScrollTop() {
		int minValue = this.scrollPane.getVerticalScrollBar().getMinimum();
		this.scrollPane.getVerticalScrollBar().setValue(minValue);
	}
	
	public void clear() {
		
		try {
			Document arg1 = this.getDocument();
	        arg1.remove(0, arg1.getLength());
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addContent(String arg0) {
		
		try {
			Document arg1 = this.getDocument();
			StringReader arg2 = new StringReader(arg0);
			EditorKit arg3 = this.getEditorKit();
			
			// 아래에 쓴다.
			arg3.read(arg2, arg1, arg1.getLength());
			
			// 위에 쓴다.
			arg3.read(arg2, arg1, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
