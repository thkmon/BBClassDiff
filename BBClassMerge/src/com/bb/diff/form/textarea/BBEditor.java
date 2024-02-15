package com.bb.diff.form.textarea;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.Document;

public class BBEditor extends JTextPane {
	private JScrollPane scrollPane = null;
	
	public JScrollPane getScrollPane() {	
		return scrollPane;
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
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
}
