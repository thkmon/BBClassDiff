package com.bb.diff.map;

import com.bb.diff.prototype.Col;
import com.bb.diff.prototype.ColList;


public class FileContentUtil {
	
	/*
	public static StringBuffer getContentBuffer(String fileFullPath) {
		
		System.out.println("읽기 시도 : " + fileFullPath);
		
		StringBuffer tempBuffer = null;
		
		if (fileFullPath == null || fileFullPath.length() == 0) {
			return new StringBuffer("파일 패스를 알 수 없습니다. fileFullPath == null || fileFullPath.length() == 0");
		}
		
		
		boolean isClassFile = false;
		
		if (fileFullPath.endsWith(".class")) {
			isClassFile = true;
		}
		
		FileContentInfo info = CConst.fileContentMap.get(fileFullPath);
		if (info != null) {
			
			if (isClassFile) {
				tempBuffer = info.getFileDecompileContent();
				if (tempBuffer != null && tempBuffer.length() > 0) {
					return tempBuffer;
				}
				
			} else {
				tempBuffer = info.getFileContent();
				if (tempBuffer != null && tempBuffer.length() > 0) {
					return tempBuffer;
				}
			}
		}
		
//		**
//		 * 초기화
//		 *
		
		tempBuffer = null;

//		**
//		 * 정보가 없는 상태임. 새로 파일을 읽어들인다.
//		 *
		StringBuffer newBuffer = FileUtil.readFile(new File(fileFullPath));
		
		if (newBuffer == null || newBuffer.length() == 0) {
			return new StringBuffer("실제 파일 내용이 없습니다. newBuffer == null || newBuffer.length() == 0"); 
		}
		
		if (isClassFile) {
			// 클래스 파일일 경우
			try {
				tempBuffer = DecompileUtil.readClassFile(fileFullPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			FileContentInfo newContentinfo = new FileContentInfo();
			newContentinfo.setFileContent(null);
			newContentinfo.setFileDecompileContent(tempBuffer);
			
		} else {
			// 일반 파일일 경우
			try {
				tempBuffer = FileUtil.readFile(new File(fileFullPath));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			FileContentInfo newContentinfo = new FileContentInfo();
			newContentinfo.setFileContent(tempBuffer);
			newContentinfo.setFileDecompileContent(null);			
		}
		
		return tempBuffer;
	}
*/
	
	public static ColList createColList(StringBuffer contentBuffer) {
		if (contentBuffer == null || contentBuffer.length() == 0) {
			return null;
		}
		
		ColList colList1 = new ColList();
		
		String[] conLineArr1 = contentBuffer.toString().split("\n");
		int lineSize1 = conLineArr1.length;
	
		int beginCol = 0;
		int endCol = 0;
		
		String oneContent = "";
		Col oneCol = null;
		for (int i=0; i<lineSize1; i++) {
			endCol = beginCol + conLineArr1[i].length();
			oneCol = new Col(beginCol, endCol);
			
			oneContent = contentBuffer.substring(beginCol+i, endCol+i);
			
			if (!conLineArr1[i].equals(oneContent)) {
				System.err.println("beginCol : " + beginCol + " / endCol : " + endCol + " / ");
				System.err.println("con [" + getTextToPrint(conLineArr1[i]) + "] ---- [" + getTextToPrint(oneContent) + "]");
				System.err.println("------");
				
			} else {
//				System.out.println("con [" + getTextToPrint(conLineArr1[i]) + "] ---- [" + getTextToPrint(oneContent) + "]");
//				System.out.println("------");
			}
			
			oneCol.setContent(oneContent);
			colList1.add(oneCol);
			
			beginCol = endCol;
		}
		
		return colList1;
	}
	
	public static String getTextToPrint(String str) {
		str = str.replace("\n", "\\n");
		str = str.replace("\r", "\\r");
		str = str.replace("\t", "\\t");
		return str;
		
	}
}

