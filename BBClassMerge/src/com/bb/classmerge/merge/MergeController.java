package com.bb.classmerge.merge;

import java.io.File;

import com.bb.classmerge.decompile.Decompiler;
import com.bb.classmerge.file.FileConverter;
import com.bb.classmerge.util.ConsoleUtil;
import com.bb.classmerge.util.FileUtil;
import com.bb.diff.data.PathData;
import com.bb.diff.data.PathDataList;
import com.bb.diff.file.FileDiffController;

public class MergeController {

	
	public String mergeDirs(String dirPath1, String dirPath2) throws Exception {
		// path1  기준으로 머지한다.
		// 양쪽 다 있는 파일은 path1 기준으로 카피한다.
		
		// 파일이 둘 다 있으면 용량 비교한다.
		// 파일이 한쪽만 1에만 있으면 가져온다.
		// 파일이 2
		
		FileDiffController fileDiffCtrl = new FileDiffController();
		PathDataList pathDataList = fileDiffCtrl.diffDirs(dirPath1, dirPath2);
		
		if (pathDataList == null || pathDataList.size() == 0) {
			throw new Exception("MergeController mergeDirs : pathDataList == null || pathDataList.size() == 0");
		}
		
		FileConverter fileConverter = new FileConverter();
		String destDir = fileConverter.makeDestinationDir();
		if (destDir == null || destDir.length() == 0) {
			throw new Exception("MergeController mergeDirs : destDir == null || destDir.length() == 0");
		}
		
		Decompiler decompiler = new Decompiler();
		
		File file1 = null;
		File file2 = null;
		String leftPath = null;
		String rightPath = null;
		
		PathData pathData = null;
		int totalCount = pathDataList.size();
		for (int i=0; i<totalCount; i++) {
			if (pathDataList.get(i) == null) {
				continue;
			}
			
			pathData = pathDataList.get(i);
			ConsoleUtil.print("Merge [" + (i+1) + "/" + totalCount + "] " + pathData.getSimplePath());
			
			file1 = null;
			file2 = null;
			
			leftPath = pathData.getLeftFullPath();
			if (leftPath != null && leftPath.trim().length() > 0) {
				file1 = new File(leftPath.trim());
			}
			
			rightPath = pathData.getRightFullPath();
			if (rightPath != null && rightPath.trim().length() > 0) {
				file2 = new File(rightPath.trim());
			}
			
			boolean bFileExists1 = (file1 != null && file1.exists());
			boolean bFileExists2 =(file2 != null && file2.exists());
			
			if (file1.isDirectory()) {
				File newDir = new File(destDir + "/" + pathData.getSimplePath());
				if (!newDir.exists()) {
					newDir.mkdirs();
				}
				continue;
			}
			
			if (bFileExists1 && bFileExists2) {
				String oldFilePath = leftPath;
				String destFilePath = destDir + "/" + pathData.getSimplePath();
				
				if (checkIsClassFile(file1)) {
					decompiler.decompile(oldFilePath, destFilePath);
				} else {
					FileUtil.copyFile(oldFilePath, destFilePath);
				}
				
			} else if (bFileExists1) {
				String oldFilePath = leftPath;
				String destFilePath = destDir + "/" + pathData.getSimplePath();
				
				if (checkIsClassFile(file1)) {
					decompiler.decompile(oldFilePath, destFilePath);
				} else {
					FileUtil.copyFile(oldFilePath, destFilePath);
				}
				
			} else if (bFileExists2) {
				// 우측패스에만 있는 파일은 무시한다.
			}
		}
		
		return destDir;
	}
	
	
	private boolean checkIsClassFile(File file) {
		if (file != null && file.exists()) {
			if (file.getAbsolutePath().trim().toLowerCase().endsWith(".class")) {
				return true;
			}
		}
		
		return false;
	}
}