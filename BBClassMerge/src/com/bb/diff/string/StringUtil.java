package com.bb.diff.string;

import com.bb.diff.log.LogUtil;
import com.bb.diff.prototype.StringList;

public class StringUtil {
	
	/**
	 * 특정문자를 뒤에서부터 찾는다. limitIdx 아규먼트는 한계치를 설정한다. (예를 들어 10자리 문자가 있고 limitIdx가 8이면 앞에서부터 8자리까지만 검사함.)
	 * @param str
	 * @param strToFind
	 * @param limitIdx
	 * @return
	 */
	public static int getLastIndex(String str, String strToFind, int limitIdx) {
		if (str == null || str.length() == 0) {
			return -1;
		}
		
		if (strToFind == null || strToFind.length() == 0) {
			LogUtil.appendLogFileForError("getLastIndex : strToFind == null || strToFind.length() == 0");
			return -1;
			
		}
		
		String oneStr = "";
		
		int len = str.length();
		
		if (limitIdx < 1) {
			LogUtil.appendLogFileForError("getLastIndex : limitIdx < 1");
			return -1;
		}
		
		if (limitIdx >= (len - 1)) {
			limitIdx = len - 1; // len - 1이 한계다.
		}
		
		for (int i=limitIdx; i>-1; i--) {
			oneStr = subText(str, i, i+1);
			if (oneStr.equals(strToFind)) {
				return i;
			}
		}
		
		return -1;
		
	}
	
	/**
	 * 일종의 substring. 단, 인덱스를 잘못 지정해도 Error 나지 않게 텍스트를 잘라낸다.
	 * 
	 * @param str
	 * @param beginIdx
	 * @param endIdx
	 * @return
	 */
	public static String subText(String str, int beginIdx, int endIdx) {
		if (str == null || str.length() == 0) {
			return "";
		}
		
		if (beginIdx < 0) {
			beginIdx = 0;
		}
		
		if (endIdx > str.length()) {
			endIdx = str.length();
		}
		
		return str.substring(beginIdx, endIdx);
	}
	
	/**
	 * 일종의 substring. 단, 인덱스를 잘못 지정해도 Error 나지 않게 텍스트를 잘라낸다.
	 * 
	 * @param str
	 * @param beginIdx
	 * @param endIdx
	 * @return
	 */
	public static String subText(StringBuffer str, int beginIdx, int endIdx) {
		if (str == null || str.length() == 0) {
			return "";
		}
		
		if (beginIdx < 0) {
			beginIdx = 0;
		}
		
		if (endIdx > str.length()) {
			endIdx = str.length();
		}
		
		return str.substring(beginIdx, endIdx);
	}
	
	/**
	 * 일종의 substring. 단, 인덱스를 잘못 지정해도 Error 나지 않게 텍스트를 잘라낸다.
	 * 출력용은 역슬래시 n, 역슬래시 r, 역슬래시 t를 표시한다.
	 * 
	 * @param str
	 * @param beginIdx
	 * @param endIdx
	 * @return
	 */
//	public static String subTextToPrint(String str, int beginIdx, int endIdx) {
//		return subTextToPrint(str.toString(), beginIdx, endIdx);
//	}
	
	public static String subTextToPrint(StringBuffer str, int beginIdx, int endIdx) {
		if (str == null || str.length() == 0) {
			return "";
		}
		
		if (beginIdx < 0) {
			beginIdx = 0;
		}
		
		if (endIdx > str.length()) {
			endIdx = str.length();
		}
		
		return str.substring(beginIdx, endIdx).replace("\n", "\\n").replace("\t", "\\t").replace("\r", "\\r");
	}
	
	public static StringList splitByOneDigit(String targetStr, String oneDigit) {
		StringList strList = new StringList();
		
		if (targetStr == null || targetStr.length() == 0) {
			return strList;
		}

		StringBuffer buff = new StringBuffer();
		int len = targetStr.length();
		
		String oneChar = "";

		for (int i=0; i<len; i++) {
			oneChar = targetStr.substring(i, i+1);
			
			if (oneChar.equals(oneDigit)) {
				if (buff.toString().length() > 0) {
					strList.add(buff.toString());
				}
				buff = new StringBuffer();

			} else {
				buff.append(oneChar);
			}
		}
		
		if (buff.toString().length() > 0) {
			strList.add(buff.toString());
		}
		
		return strList;
	}
}
