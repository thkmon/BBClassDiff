package com.bb.diff.prototype;

public class DoublePathList {
	private PathList originList = new PathList();
	private PathList targetList = new PathList();
	private int size = 0;
	
	public PathList getOriginList() {
		return originList;
	}
	public void setOriginList(PathList originList) {
		this.originList = originList;
	}
	public PathList getTargetList() {
		return targetList;
	}
	public void setTargetList(PathList targetList) {
		this.targetList = targetList;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	
}
