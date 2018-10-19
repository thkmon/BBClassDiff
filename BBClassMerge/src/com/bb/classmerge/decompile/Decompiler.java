package com.bb.classmerge.decompile;

import java.io.File;

import com.bb.classmerge.util.FileNameUtil;
import com.bb.classmerge.util.FileUtil;
import com.bb.classmerge.util.ConsoleUtil;

public class Decompiler {

	public boolean decompile(String oldFilePath, String destFilePath) throws Exception {

		if (oldFilePath == null || oldFilePath.length() == 0) {
			ConsoleUtil.print("Decompiler decompile : oldFilePath == null || oldFilePath.length() == 0");
			return false;
		}
		
		if (oldFilePath.indexOf(".") < 0 || !oldFilePath.endsWith(".class")) {
			ConsoleUtil.print("Decompiler decompile : Not Class File. oldFilePath == [" + oldFilePath + "]");
			return false;
		}
		
		File oldFile = new File(oldFilePath);
		
		if (!oldFile.exists()) {
			ConsoleUtil.print("Decompiler decompile : !oldFile.exists()");
			return false;
		}
		
		File tempDir = new File("temp");
		if (!tempDir.exists()) {
			tempDir.mkdirs();
		}
		
		String oldFileName = FileNameUtil.getFileName(oldFilePath);
		FileUtil.copyFile(oldFilePath, tempDir + "\\" + oldFileName);
		
		File copiedFile = new File(tempDir + "\\" + oldFileName);
		if (!copiedFile.exists()) {
			ConsoleUtil.print("Decompiler decompile : !newFile.exists()");
			return false;
		}
		
		String copiedFilePath = copiedFile.getAbsolutePath();
		String exeFileParam = "jad158g/jad.exe -o -sjava " + copiedFilePath;
		// FormConsole.print(exeFileParam);
		
		Process process = null;
		
		try {
			Runtime rt = Runtime.getRuntime();
			process = rt.exec(exeFileParam);
		    process.getErrorStream().close();
		    process.getInputStream().close();
		    process.getOutputStream().close();
		    process.waitFor();
		
		} catch (Exception e) {
			ConsoleUtil.print("decompile fail! exeFileParam == [" + exeFileParam + "]");
		    e.printStackTrace();
		    return false;
		}
		
		File decompiledFile = new File(FileNameUtil.replaceExtension(oldFileName, "java"));
		if (!decompiledFile.exists()) {
			ConsoleUtil.print("Decompiler decompile : !decompiledFile.exists()");
			return false;
		}
		
		FileUtil.copyFile(decompiledFile.getAbsolutePath(), destFilePath);
		
		boolean b1 = decompiledFile.delete();
		boolean b2 = copiedFile.delete();
		
		if (destFilePath == null || destFilePath.length() == 0) {
			ConsoleUtil.print("Decompiler decompile : destFilePath == null || destFilePath.length() == 0");
			return false;
		}
		
		File destFile = new File(destFilePath);
		if (!destFile.exists()) {
			ConsoleUtil.print("Decompiler decompile : !destFile.exists()");
			return false;
		}
		
		if (destFile.length() < 1) {
			ConsoleUtil.print("Decompiler decompile : destFile.length() < 1");
			return false;
		}
		
		return true;
	}
	
	
	/*
	public String compile(String filePath) {
		if (filePath == null || filePath.length() == 0) {
			return "";
		}
		
		if (!filePath.endsWith(".java")) {
			return "";
		}
		
		File file = new File(filePath);
		if (!file.exists()) {
			return "";
		}
		
		String exeFileParam = "C:/Java/jdk1.6.0_45/bin/javac.exe " + file.getAbsolutePath();
		FormConsole.print(exeFileParam);
		
		Process process = null;
		
		try {
			Runtime rt = Runtime.getRuntime();
			process = rt.exec(exeFileParam);
		    process.getErrorStream().close();
		    process.getInputStream().close();
		    process.getOutputStream().close();
		    process.waitFor();
		
		} catch (Exception e) {
		    e.printStackTrace();
		    return "";
		}
		
		String newPath = StringUtil.replaceLastOne(file.getAbsolutePath(), ".java", ".class");
		return newPath;
	}
	*/
}