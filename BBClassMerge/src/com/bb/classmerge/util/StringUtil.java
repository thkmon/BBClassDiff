package com.bb.classmerge.util;

import com.bb.classmerge.file.FileList;
import com.bb.diff.log.LogUtil;
import com.bb.diff.prototype.StringList;

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
	
	
	/**
	 * n개의 딜리미터로 split 처리
	 * 
	 * @param fullStr
	 * @param delimeters
	 * @return
	 */
	public static StringList splitMulti(String fullStr, String... delimeters) {

		StringList resList = new StringList();

		if (fullStr == null || fullStr.length() == 0) {
			return null;
		}

		if (delimeters == null) {
			System.out.println("splitMulti : delimeters are null");
			return null;
		}

		int deliCount = delimeters.length;
		if (deliCount < 1) {
			System.out.println("splitMulti : delimeters' count is 0");
			return null;
		}

		StringBuffer contentStack = new StringBuffer();

		int fullLen = fullStr.length();
		String oneDeli = "";

		boolean isDeli = false;

		for (int i = 0; i < fullLen; i++) {
			isDeli = false;

			for (int k = 0; k < deliCount; k++) {
				oneDeli = delimeters[k];
				if (oneDeli == null || oneDeli.length() == 0) {
					continue;
				}

				if (i + oneDeli.length() > fullLen) {
					continue;
				}

				if (fullStr.substring(i, i + oneDeli.length()).equals(oneDeli)) {
					resList.add(contentStack.toString());
					contentStack.delete(0, contentStack.length());

					// oneDeli 로 자른다.
					isDeli = true;
					break;
				}
			}

			if (!isDeli) {
				contentStack.append(fullStr.substring(i, i + 1));
			}
		}

		if (contentStack.length() > 0) {
			resList.add(contentStack.toString());
		}

		return resList;
	}
	
	
	public static boolean containsOutsideDoubleQuotes(String str, String slice) {
		if (str == null || str.length() == 0) {
			return false;
		}
		
		if (slice == null || slice.length() == 0) {
			return false;
		}
		
		StringBuffer buff = new StringBuffer();
		
		boolean bOutsideDoubleQuotes = false;
		String oneChar = "";
		int len = str.length();
		for (int i=0; i<len; i++) {
			oneChar = str.substring(i, i+1);
			if (oneChar.equals("\"")) {
				bOutsideDoubleQuotes = !bOutsideDoubleQuotes;
				continue;
			}
			
			if (!bOutsideDoubleQuotes) {
				buff.append(oneChar);
			}
		}
		
		if (buff.toString().contains(slice)) {
			return true;
		}
		
		return false;
	}
}