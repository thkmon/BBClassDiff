package com.bb.classmerge.merge;

import java.io.File;

import com.bb.classmerge.decompile.Decompiler;
import com.bb.classmerge.file.FileConverter;
import com.bb.classmerge.util.ConsoleUtil;
import com.bb.classmerge.util.DirUtil;
import com.bb.classmerge.util.FileCopyUtil;
import com.bb.diff.data.PathData;
import com.bb.diff.data.PathDataList;
import com.bb.diff.file.FileDiffController;
import com.bb.diff.file.FileUtil;

public class MergeController {

	
	public String mergeDirs(String dirPath1, String dirPath2) throws Exception {
		// path1  기준으로 머지한다.
		// 양쪽 다 있는 파일은 path1 기준으로 카피한다.
		
		FileDiffController fileDiffCtrl = new FileDiffController();
		PathDataList pathDataList = fileDiffCtrl.diffDirs(dirPath1, dirPath2);
		
		if (pathDataList == null || pathDataList.size() == 0) {
			throw new Exception("MergeController mergeDirs : pathDataList == null || pathDataList.size() == 0");
		}
		
		String destDir = DirUtil.makeDestinationDir("result");
		if (destDir == null || destDir.length() == 0) {
			throw new Exception("MergeController mergeDirs : destDir == null || destDir.length() == 0");
		}
		
		String resultValue = "알 수 없음";
		Decompiler decompiler = new Decompiler();
		
		File file1 = null;
		File file2 = null;
		String leftPath = null;
		String rightPath = null;
		
		PathData pathData = null;
		int totalCount = pathDataList.size();
		for (int i=0; i<totalCount; i++) {
			
			try {
				resultValue = "알 수 없음";
				
				if (pathDataList.get(i) == null) {
					continue;
				}
				
				pathData = pathDataList.get(i);
				
				file1 = null;
				file2 = null;
				
				leftPath = pathData.getLeftFullPath();
				if (leftPath != null && leftPath.trim().length() > 0) {
					leftPath = leftPath.trim();
					file1 = new File(leftPath);
				}
				
				rightPath = pathData.getRightFullPath();
				if (rightPath != null && rightPath.trim().length() > 0) {
					rightPath = rightPath.trim();
					file2 = new File(rightPath);
				}
				
				boolean bFileExists1 = (file1 != null && file1.exists());
				boolean bFileExists2 =(file2 != null && file2.exists());
				
				if (file1 != null && file1.isDirectory()) {
					File newDir = new File(destDir + "/" + pathData.getSimplePath());
					if (!newDir.exists()) {
						newDir.mkdirs();
					}
					
					resultValue = "폴더";
					continue;
				}
				
				if (bFileExists1 && bFileExists2) {
					String oldFilePath = leftPath;
					String destFilePath = destDir + "/" + pathData.getSimplePath();
					
					if (FileUtil.checkIsSameFile(file1, file2)) {
						// 같으면 무시
						resultValue = "동일한 파일(무시)";
						
					} else {
						if (checkIsClassFile(file1)) {
							decompiler.decompile(oldFilePath, destFilePath);
							resultValue = "상이한 파일(디컴파일)";
						} else {
							FileCopyUtil.copyFile(oldFilePath, destFilePath);
							resultValue = "상이한 파일(복사)";
						}
					}
					
				} else if (bFileExists1) {
					String oldFilePath = leftPath;
					String destFilePath = destDir + "/" + pathData.getSimplePath();
					
					if (checkIsClassFile(file1)) {
						decompiler.decompile(oldFilePath, destFilePath);
						resultValue = "좌측 파일(디컴파일)";
					} else {
						FileCopyUtil.copyFile(oldFilePath, destFilePath);
						resultValue = "좌측 파일(복사)";
					}
					
				} else if (bFileExists2) {
					// 우측패스에만 있는 파일은 무시한다.
					resultValue = "우측 파일(무시)";
				}
			
			} catch (Exception ie) {
				resultValue = "오류발생 [" + ie.getMessage() + "]";
				ie.printStackTrace();
				continue;
				
			} finally {
				ConsoleUtil.print("Merge [" + (i+1) + "/" + totalCount + "] " + pathData.getSimplePath() + " => [" + resultValue + "]");
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