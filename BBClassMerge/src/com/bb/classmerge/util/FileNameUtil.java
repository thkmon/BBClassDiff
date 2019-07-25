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
	
	
	public static String getExtensionFromNameOnly(String fileName) {
		if (fileName == null || fileName.length() == 0) {
			return "";
		}
		
		int lastDotIndex = fileName.lastIndexOf(".");
		if (lastDotIndex < 0) {
			return "";
		}
		
		return fileName.substring(lastDotIndex + 1);
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
