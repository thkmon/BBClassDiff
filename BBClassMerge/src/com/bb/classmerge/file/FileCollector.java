package com.bb.classmerge.file;

import java.io.File;

public class FileCollector {
	
	
	public FileList getFileList(String dirPath, boolean bGetClassOnly) throws Exception {
		File dirFile = new File(dirPath);
		if (!dirFile.exists()) {
			throw new Exception("getFileList : Directory Not Exists. dirPath == [" + dirFile.getAbsolutePath() + "]");
		}
		
		FileList resultList = new FileList();
		addFiles(resultList, dirFile, bGetClassOnly);
		return resultList;
	}
	
	
	private void addFiles(FileList fileList, File file, boolean bGetClassOnly) throws Exception {
		
		if (fileList == null) {
			return;
		}

		if (file == null) {
			return;
		}
		
		if (!file.exists()) {
			return;
		}
		
		if (file.isFile()) {
			if (bGetClassOnly) {
				if (file.getName().toLowerCase().endsWith(".class")) {
					fileList.add(file);
				}
				
			} else {
				fileList.add(file);
			}
			
			return;
		}
		
		if (file.isDirectory()) {
			File[] fileArr = file.listFiles();
			if (fileArr == null || fileArr.length == 0) {
				return;
			}
			
			int fileCount = fileArr.length;
			for (int i=0; i<fileCount; i++) {
				addFiles(fileList, fileArr[i], bGetClassOnly);
			}
		}
	}
}
