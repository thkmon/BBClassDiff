package com.bb.diff.file;

import java.io.File;

import com.bb.classmerge.util.StringUtil;
import com.bb.diff.data.ClsPath;
import com.bb.diff.data.ClsPathList;
import com.bb.diff.data.StringList;

public class FileDiffController {
	
	
	/**
	 * decompile 하지 않고 classse 폴더 2개 비교하기
	 * 
	 * @param leftClassesDir
	 * @param rightClassesDir
	 * @return
	 * @throws Exception
	 */
	public ClsPathList diffClsDirs(String leftClassesDir, String rightClassesDir) throws Exception {
		
		ClsPathList clsPathList = getMergedClassPathList(leftClassesDir, rightClassesDir);
		if (clsPathList == null || clsPathList.size() == 0) {
			throw new Exception("FileDiffController diffClsDirs : there are no files.");
		}
		
		return clsPathList;
	}
	
	
	private ClsPathList getMergedClassPathList(String leftClassesDir, String rightClassesDir) throws Exception {
		if (leftClassesDir == null || leftClassesDir.length() == 0) {
			throw new Exception("FileDiffController getMergedClassPathList : leftClassesDir is null or empty.");
		}
		
		if (rightClassesDir == null || rightClassesDir.length() == 0) {
			throw new Exception("FileDiffController getMergedClassPathList : rightClassesDir is null or empty.");
		}
		
		File leftDir = new File(leftClassesDir);
		File rightDir = new File(rightClassesDir);
		
		if (!leftDir.exists()) {
			throw new Exception("FileDiffController getMergedClassPathList : leftDir not exists.");
		}
		
		if (!rightDir.exists()) {
			throw new Exception("FileDiffController getMergedClassPathList : rightDir not exists.");
		}

		StringList clsPathList1 = getPathList(leftDir);
		StringList clsPathList2 = getPathList(rightDir);
		
		StringList mergedPathList = getMergedPathList(clsPathList1, clsPathList2, "class");
		if (mergedPathList == null || mergedPathList.size() == 0) {
			throw new Exception("FileDiffController getMergedClassPathList : mergedPathList is null or empty.");
		}
		
		ClsPathList clsPathList = new ClsPathList();
		
		String onePath = null;
		int pathCount = mergedPathList.size();
		for (int i=0; i<pathCount; i++) {
			onePath = mergedPathList.get(i);
			if (onePath == null || onePath.length() == 0) {
				continue;
			}
			
			String path1 = StringUtil.revisePath(leftDir + onePath);
			String path2 = StringUtil.revisePath(rightDir + onePath);
			
			File file1 = new File(path1);
			File file2 = new File(path2);
			
			boolean existing1 = file1.exists();
			boolean existing2 = file2.exists();
			
			if (existing1 && existing2) {
				long gap = getVolumeGap(file1, file2);
				ClsPath clsPath = new ClsPath(onePath, path1, path2, gap);
				clsPathList.addNotDupl(clsPath);
				
			} else if (existing1) {
				ClsPath clsPath = new ClsPath(onePath, path1, "", 0);
				clsPathList.addNotDupl(clsPath);
				
			} else if (existing2) {
				ClsPath clsPath = new ClsPath(onePath, "", path2, 0);
				clsPathList.addNotDupl(clsPath);
			}
		}
		
		return clsPathList;
	}
	
	
	private long getVolumeGap(File file1, File file2) {
		if (file1 == null || !file1.exists()) {
			return -1;
		}
		
		if (file2 == null || !file2.exists()) {
			return -1;
		}
		
		long vol1 = file1.length();
		long vol2 = file2.length();
		if (vol1 == vol2) {
			return 0;
		}
		
		long gap = vol1 - vol2;
		if (gap < 0) {
			gap = gap * -1;
		}
		
		return gap;
	}

	
	private StringList getMergedPathList(StringList list1, StringList list2, String extension) {
		if (list1 == null) {
			list1 = new StringList();
		}
		
		if (list2 == null) {
			list2 = new StringList();
		}
		
		String path = null;
		int count = list2.size();
		for (int i=0; i<count; i++) {
			path = list2.get(i);
			if (path == null || path.length() == 0) {
				continue;
			}
			
			if (extension != null && extension.length() > 0) {
				if (path.toLowerCase().endsWith("." + extension.toLowerCase())) {
					list1.addNotDupl(path);
				}
			}
		}
		
		return list1;
	}
	
	
	private StringList getPathList(File dirObj) {
		
		StringList pathList = new StringList();
		
		String dirPath = dirObj.getAbsolutePath();
		dirPath = StringUtil.revisePath(dirPath);
		
		addPath(pathList, dirObj, dirPath);
		return pathList;
	}

	
	private void addPath(StringList pathList, File file, String dirPath) {
		
		if (!file.exists()) {
			return;
		}
		
		if (file.isFile()) {
			String onePath = file.getAbsolutePath();
			onePath = StringUtil.revisePath(onePath);
			
			if (onePath.startsWith(dirPath)) {
				onePath = StringUtil.replaceOne(onePath, dirPath, "");
			}
			
			pathList.addNotDupl(onePath);
			
		} else if (file.isDirectory()) {
			File[] fileArray = file.listFiles();
			if (fileArray != null && fileArray.length > 0) {
				int fileCount = fileArray.length;
				for (int i=0; i < fileCount; i++) {
					addPath(pathList, fileArray[i], dirPath);
				}
			}
		}
	}
}