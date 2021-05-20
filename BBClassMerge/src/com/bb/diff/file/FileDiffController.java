package com.bb.diff.file;

import java.io.File;

import com.bb.classmerge.util.StringUtil;
import com.bb.diff.data.PathData;
import com.bb.diff.data.PathDataList;
import com.bb.diff.prototype.StringList;

public class FileDiffController {
	
	
	/**
	 * 폴더 2개 비교하기
	 * 
	 * @param leftClassesDir
	 * @param rightClassesDir
	 * @return
	 * @throws Exception
	 */
	public PathDataList diffDirs(String leftClassesDir, String rightClassesDir) throws Exception {
		
		PathDataList pathDataList = getMergedClassPathList(leftClassesDir, rightClassesDir);
		if (pathDataList == null || pathDataList.size() == 0) {
			throw new Exception("FileDiffController diffClsDirs : there are no files.");
		}
		
		return pathDataList;
	}
	
	
	private PathDataList getMergedClassPathList(String leftClassesDir, String rightClassesDir) throws Exception {
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

		StringList pathDataList1 = getPathList(leftDir);
		StringList pathDataList2 = getPathList(rightDir);
		
		StringList mergedPathList = getMergedPathList(pathDataList1, pathDataList2);
		if (mergedPathList == null || mergedPathList.size() == 0) {
			throw new Exception("FileDiffController getMergedClassPathList : mergedPathList is null or empty.");
		}
		
		PathDataList pathDataList = new PathDataList();
		
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
				long volGap = getVolumeGap(file1, file2);
				PathData pathData = new PathData(onePath, path1, path2, volGap);
				pathDataList.addNotDupl(pathData);
				
			} else if (existing1) {
				PathData pathData = new PathData(onePath, path1, path2, 0);
				pathDataList.addNotDupl(pathData);
				
			} else if (existing2) {
				PathData pathData = new PathData(onePath, path1, path2, 0);
				pathDataList.addNotDupl(pathData);
			}
		}
		
		return pathDataList;
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

	
	private StringList getMergedPathList(StringList list1, StringList list2) {
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
			
			list1.addNotDupl(path);
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
		
		// 190523. 파일 경로 리스트에 폴더도 추가하도록 개선.
		String onePath = file.getAbsolutePath();
		onePath = StringUtil.revisePath(onePath);
		
		if (onePath.startsWith(dirPath)) {
			onePath = StringUtil.replaceOne(onePath, dirPath, "");
		}
		
		pathList.addNotDupl(onePath);
			
		if (file.isDirectory()) {
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