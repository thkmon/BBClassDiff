package com.bb.classmerge.file;

import java.io.File;

import com.bb.classmerge.decompile.Decompiler;
import com.bb.classmerge.exception.MsgException;
import com.bb.classmerge.util.ConsoleUtil;
import com.bb.classmerge.util.DirUtil;
import com.bb.classmerge.util.FileNameUtil;
import com.bb.classmerge.util.StringUtil;

public class FileConverter {
	
	
	/**
	 * 폴더경로 밸리드 체크
	 * 
	 * @param dirPath
	 * @return
	 * @throws MsgException
	 * @throws Exception
	 */
	public boolean checkDirectoryIsValid(String dirPath, String prefix) throws MsgException, Exception {
		if (dirPath == null || dirPath.trim().length() == 0) {
			throw new MsgException(prefix + " 폴더 경로를 입력해주세요.");
		} else {
			dirPath = dirPath.trim();
		}
		
		File dir = new File(dirPath);
		if (!dir.exists()) {
			throw new MsgException(prefix + " 해당 경로가 존재하지 않습니다. [" + StringUtil.revisePath(dir.getAbsolutePath()) + "]");
		}
		
		if (dir.isFile() || !dir.isDirectory()) {
			throw new MsgException(prefix + " 해당 경로는 디렉토리가 아닙니다. [" + StringUtil.revisePath(dir.getAbsolutePath()) + "]");
		}
		
		FileCollector fileCollector = new FileCollector();
		FileList fileList = fileCollector.getFilesAndDirsList(dirPath, false);
		if (fileList == null || fileList.size() == 0) {
			throw new MsgException(prefix + " 해당 경로 안에 파일이 존재하지 않습니다. [" + StringUtil.revisePath(dir.getAbsolutePath()) + "]");
		}
		
		return true;
	}
	
	
	public String convertClassToJava(String dirPath, String prefix) throws MsgException, Exception {
		
		// 폴더경로 밸리드 체크
		checkDirectoryIsValid(dirPath, prefix);
		
		File dir = new File(dirPath);
		String oldDirPath = dir.getAbsolutePath();
		String destDirPath = DirUtil.makeDestinationDir("result");
		
		FileCollector fileCollector = new FileCollector();
		FileList fileList = fileCollector.getFilesAndDirsList(dirPath, true);
		
		Decompiler decompiler = new Decompiler();
		
		File oneFile = null;
		String oldFilePath = null;
		String oldFileName = null;
		String destFilePath = null;
		int fileCount = fileList.size();
		
		ConsoleUtil.print("파일 개수 : " + fileCount);
		
		boolean bResult = false;
		for (int i=0; i<fileCount; i++) {
			oneFile = fileList.get(i);
			if (oneFile == null || !oneFile.exists()) {
				continue;
			}
			
			if (!oneFile.getName().toLowerCase().endsWith(".class")) {
				continue;
			}
			
			oldFilePath = oneFile.getAbsolutePath();
			oldFileName = FileNameUtil.getFileName(oldFilePath);
			
			destFilePath = StringUtil.replaceOne(oldFilePath, oldDirPath, destDirPath);
			destFilePath = FileNameUtil.replaceExtension(destFilePath, "java");
			
			bResult = decompiler.decompile(oldFilePath, destFilePath);
			ConsoleUtil.print((i+1) + "/" + fileCount + " : " + oldFileName + " (" + bResult + ")");
		}
		
		ConsoleUtil.print("결과 폴더 : " + destDirPath);
		ConsoleUtil.print("----------");
		
		return destDirPath;
	}
}
