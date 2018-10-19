package com.bb.diff.data;

import java.util.ArrayList;

public class ClsPathList extends ArrayList<ClsPath> {

	
	@Override
	public String toString() {
		
		StringBuffer buff = new StringBuffer();
		
		ClsPath clsPath = null;
		int clsCount = this.size();
		
		for (int i=0; i<clsCount; i++) {
			clsPath = this.get(i);
			if (clsPath == null) {
				continue;
			}
			
			if (clsPath.getSimplePath() == null || clsPath.getSimplePath().length() == 0) {
				continue;
			}
			
			if (buff.length() > 0) {
				buff.append("\r\n");
			}
			
			buff.append("[" + (i+1) + "/" + clsCount + "] : " + clsPath.getSimplePath());
		}
		
		return buff.toString();
	}
	
	
	public void addNotDupl(ClsPath clsPath) {
		if (clsPath == null) {
			return;
		}
		
		String clsSimplePath = clsPath.getSimplePath();
		if (clsSimplePath == null || clsSimplePath.length() == 0) {
			return;
		}
		
		String tempSimplePath = null;
		int count = this.size();
		for (int i=0; i<count; i++) {
			if (this.get(i) == null) {
				continue;
			}
			
			tempSimplePath = this.get(i).getSimplePath();
			
			if (tempSimplePath == null || tempSimplePath.length() == 0) {
				continue;
			}
			
			if (clsSimplePath.equals(tempSimplePath)) {
				return;
			}
		}
		
		this.add(clsPath);
	}
}
