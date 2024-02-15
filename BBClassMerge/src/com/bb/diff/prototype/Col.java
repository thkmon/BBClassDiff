package com.bb.diff.prototype;

public class Col {
	public Col(int beginCol, int endCol) {
		super();
		this.beginCol = beginCol;
		this.endCol = endCol;
	}
	
	private int beginCol = 0;
	private int endCol = 0;
	private String content = "";
	
	public int getBeginCol() {
		return beginCol;
	}
	public void setBeginCol(int beginCol) {
		this.beginCol = beginCol;
	}
	public int getEndCol() {
		return endCol;
	}
	public void setEndCol(int endCol) {
		this.endCol = endCol;
	}
	
	public int getGapCol() {
		return this.endCol - this.beginCol;
	}
	public String getText() {
		String str = "";
		if (content == null || content.trim().length() == 0) {
			return "";
			
		} else {
			str = content.trim();
		}
		
		str = str.replace("\t", " ");
		str = str.replace("\n", " ");
		str = str.replace("\r", " ");
		
		return str;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}

