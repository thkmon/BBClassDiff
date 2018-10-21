package com.bb.diff.data;

public class PathData {
	
	private String simplePath = "";
	private String leftFullPath = "";
	private String rightFullPath = "";
	
	private long gapValue = 0;
	
	
	public PathData(String simplePath, String leftFullPath, String rightFullPath, long gapValue) {
		this.simplePath = simplePath;
		this.leftFullPath = leftFullPath;
		this.rightFullPath = rightFullPath;
		this.gapValue = gapValue;
	}


	public String getSimplePath() {
		return simplePath;
	}


	public String getLeftFullPath() {
		return leftFullPath;
	}
	
	
	public String getRightFullPath() {
		return rightFullPath;
	}


	public long getGapValue() {
		return gapValue;
	}


	public boolean isbLeftPath() {
		if (leftFullPath != null && leftFullPath.trim().length() > 0) {
			return true;
		}
		
		return false;
	}


	public boolean isbRightPath() {
		if (rightFullPath != null && rightFullPath.trim().length() > 0) {
			return true;
		}
		
		return false;
	}
}