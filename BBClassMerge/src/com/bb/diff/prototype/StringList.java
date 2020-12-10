package com.bb.diff.prototype;

import java.util.ArrayList;

public class StringList extends ArrayList<String>{
	
	
	public void addNotDupl(String str) {
		if (str == null || str.length() == 0) {
			return;
		}
		
		int count = this.size();
		for (int i=0; i<count; i++) {
			if (str.equals(this.get(i))) {
				return;
			}
		}
		
		this.add(str);
	}
}