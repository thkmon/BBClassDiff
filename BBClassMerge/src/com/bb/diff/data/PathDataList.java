package com.bb.diff.data;

import java.util.ArrayList;

public class PathDataList extends ArrayList<PathData> {

	
	@Override
	public String toString() {
		
		StringBuffer buff = new StringBuffer();
		
		PathData pathData = null;
		int clsCount = this.size();
		
		for (int i=0; i<clsCount; i++) {
			pathData = this.get(i);
			if (pathData == null) {
				continue;
			}
			
			if (pathData.getSimplePath() == null || pathData.getSimplePath().length() == 0) {
				continue;
			}
			
			if (buff.length() > 0) {
				buff.append("\r\n");
			}
			
			buff.append("[" + (i+1) + "/" + clsCount + "] : " + pathData.getSimplePath());
		}
		
		return buff.toString();
	}
	
	
	public void addNotDupl(PathData pathData) {
		if (pathData == null) {
			return;
		}
		
		String clsSimplePath = pathData.getSimplePath();
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
		
		this.add(pathData);
	}
}
