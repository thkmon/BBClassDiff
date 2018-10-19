package com.bb.classmerge.util;

import com.bb.classmerge.file.FileList;

public class StringUtil {
	
	
	/*
	public static void print(String[] strArr, String delimiter) {
		System.out.println(convertToString(strArr, delimiter));
	}
	
	
	public static void print(FileList fileList, String delimiter) {
		System.out.println(convertToString(fileList, delimiter));
	}
	*/
	
	
	public static String convertToString(String[] strArr, String delimiter) {
		if (strArr == null) {
			return "null";
		}
		
		if (strArr.length == 0) {
			return "";
		}
		
		if (delimiter == null) {
			delimiter = ";";
		}
		
		StringBuffer buff = new StringBuffer();
		
		int count = strArr.length;
		for (int i=0; i<count; i++) {
			buff.append(strArr[i] + delimiter);
		}
		
		return buff.toString();
	}
	
	
	public static String convertToString(FileList fileList, String delimiter) {
		if (fileList == null) {
			return "null";
		}
		
		if (fileList.size() == 0) {
			return "";
		}
		
		if (delimiter == null) {
			delimiter = ";";
		}
		
		StringBuffer buff = new StringBuffer();
		
		int count = fileList.size();
		for (int i=0; i<count; i++) {
			buff.append(fileList.get(i).getAbsolutePath() + delimiter);
		}
		
		return buff.toString();
	}
	
	
	public static String replaceOne(String fullStr, String oldStr, String newStr) {
		
		if (fullStr == null || fullStr.length() == 0) {
			return "";
		}

		if (oldStr == null || oldStr.length() == 0) {
			return fullStr;
		}

		int posOldStr = fullStr.indexOf(oldStr);
		if (posOldStr < 0) {
			return fullStr;
		}
		
		StringBuffer res = new StringBuffer();
		res.append(fullStr.substring(0, posOldStr));
		res.append(newStr);
		res.append(fullStr.substring(posOldStr + oldStr.length()));
		return res.toString();
	}
	
	
	public static String replaceLastOne(String fullStr, String oldStr, String newStr) {
		
		if (fullStr == null || fullStr.length() == 0) {
			return "";
		}

		if (oldStr == null || oldStr.length() == 0) {
			return fullStr;
		}

		int posOldStr = fullStr.lastIndexOf(oldStr);
		if (posOldStr < 0) {
			return fullStr;
		}
		
		StringBuffer res = new StringBuffer();
		res.append(fullStr.substring(0, posOldStr));
		res.append(newStr);
		res.append(fullStr.substring(posOldStr + oldStr.length()));
		return res.toString();
	}
	
	
	public static String revisePath(String path) {
		if (path == null || path.trim().length() == 0) {
			return "";
		}
		
		if (path.indexOf("\\") > -1) {
			path = path.replace("\\", "/");
		}
		
		while (path.indexOf("//") > -1) {
			path = path.replace("//", "/");
		}
		
		return path;
	}
}