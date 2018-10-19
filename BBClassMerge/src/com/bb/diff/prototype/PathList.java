package com.bb.diff.prototype;

import java.util.ArrayList;

import com.bb.diff.file.BBFile;
import com.bb.diff.file.BBFileList;

public class PathList extends ArrayList<String>{
	
	public String toString() {

		StringBuffer a = new StringBuffer();
		
		int strCount = this.size();
		
		for (int i=0; i<strCount; i++) {
			a.append(this.get(i));
			if (i < strCount-1) {
				a.append("\r\n");				
			}
		}
		
		return a.toString();
		
	}
	
	public PathList getClone() {
		PathList resultList = new PathList();
		
		int strCount = this.size();
		for (int i=0; i<strCount; i++) {
			resultList.add(this.get(i));
		}
		
		return resultList;
	}

	// 널이 반환되지 않도록 조치
	@Override
	public String get(int arg0) {
		if (super.get(arg0) == null) {
			return "";
		}
		return super.get(arg0);
	}
	
//	public void add(StringList listToAppend) {
//		if (listToAppend == null) {
//			return;
//		}
//		
//		int strCount = listToAppend.size();
//		for (int i=0; i<strCount; i++) {
//			this.add(listToAppend.get(i));
//		}
//	}
//	
	
}
