package com.bb.diff.map;

import java.util.HashMap;

import com.bb.diff.path.PathUtil;

public class FileContentMap extends HashMap<String, FileContentInfo>{

	@Override
	public FileContentInfo get(Object arg0) {
		// System.out.println("get : " + arg0);
		return super.get(arg0);
	}

	@Override
	public FileContentInfo put(String arg0, FileContentInfo arg1) {
		arg0 = PathUtil.reviseStandardPath(String.valueOf(arg0));
		
		try {
			if (this.get(arg0) != null &&  this.get(arg0).getFileContent() != null) {
				throw new Exception("이미 값이 있음 : " + arg0);
			}
		
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return super.put(arg0, arg1);
	}
	
}
