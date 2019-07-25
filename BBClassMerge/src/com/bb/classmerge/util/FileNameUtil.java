package com.bb.classmerge.util;

import java.io.File;

public class FileNameUtil {

	public static int getLastSlashIndex(String filePath) {
		if (filePath == null || filePath.length() == 0) {
			return -1;
		}
		
		int slashIdx = filePath.lastIndexOf("\\");
		int slashIdx2 = filePath.lastIndexOf("/");
		if (slashIdx2 > slashIdx) {
			slashIdx = slashIdx2;
		}
		
		return slashIdx;
	}
	
	
	public static String getFileName(String filePath) {
		
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return "";
		}
		
		if (!file.isFile()) {
			return "";
		}
		
		int slashIdx = getLastSlashIndex(filePath);
		if (slashIdx < 0) {
			return filePath;
		}
		
		return filePath.substring(slashIdx + 1);
	}
	
	
	public static String replaceExtension(String filePath, String newExtension) {
		if (filePath == null || filePath.length() == 0) {
			return "";
		}
		
		if (newExtension == null || newExtension.length() == 0) {
			newExtension = "unknown";
		}
		
		int lastDotIndex = filePath.lastIndexOf(".");
		if (lastDotIndex < 0) {
			return filePath;
		}
		
		return filePath.substring(0, lastDotIndex) + "." + newExtension;
	}
	
	
	public static String getExtensionFromPath(String filePath) {
		if (filePath == null || filePath.length() == 0) {
			return "";
		}
		
		// 가장 뒤쪽의 슬래시 위치를 찾는다.
		int lastSlashIndex = filePath.lastIndexOf("/");
		int lastBackSlashIndex = filePath.lastIndexOf("\\");
		if (lastBackSlashIndex > lastSlashIndex) {
			lastSlashIndex = lastBackSlashIndex;
		}
		
		int lastDotIndex = filePath.lastIndexOf(".");
		
		// 점이 슬래시보다 뒤에 있어야만 확장자를 가져온다.
		if (lastDotIndex < lastSlashIndex) {
			return "";
		}
		
		if (lastDotIndex < 0) {
			return "";
		}
		
		return filePath.substring(lastDotIndex + 1);
	}
	
	
	public static String getParentDirPath(String filePath) {
		if (filePath == null || filePath.length() == 0) {
			return "";
		}
		
		int slashIdx = filePath.lastIndexOf("\\");
		int slashIdx2 = filePath.lastIndexOf("/");
		if (slashIdx2 > slashIdx) {
			slashIdx = slashIdx2;
		}
		
		return filePath.substring(0, slashIdx);
	}
}
