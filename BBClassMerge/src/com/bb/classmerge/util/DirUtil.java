package com.bb.classmerge.util;

import java.io.File;

import com.bb.classmerge.date.DateUtil;

public class DirUtil {
	
	public static String makeDestinationDir(String parentFolderName) throws Exception {
		File resultDir = new File(parentFolderName);
		if (!resultDir.exists()) {
			resultDir.mkdirs();
		}
		
		String dirPath = resultDir.getAbsolutePath();
		
		String newPath = StringUtil.revisePath(dirPath + "/" + DateUtil.getTodayDateTime());
		File newDir = new File(newPath);
		if (!newDir.exists()) {
			newDir.mkdirs();
		}
		
		String destDirPath = newDir.getAbsolutePath();
		return destDirPath;
	}
}
