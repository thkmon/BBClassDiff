package com.bb.classmerge.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class FileCopyUtil {
	
	public static boolean copyFile(String oldFilePath, String destFilePath) throws Exception {
		
		String destParentDirPath = FileNameUtil.getParentDirPath(destFilePath);
		if (destParentDirPath != null && destParentDirPath.length() > 0) {
			File destDir = new File(destParentDirPath);
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
		}
		
		boolean bCopy = false;
		
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		
		FileChannel fcin = null;
		FileChannel fcout = null; 
				
		try {
			inputStream = new FileInputStream(oldFilePath);
			outputStream = new FileOutputStream(destFilePath);
			
			fcin = inputStream.getChannel();
			fcout = outputStream.getChannel();
			
			long size = fcin.size();
			fcin.transferTo(0, size, fcout);

			bCopy = true;
			
		} catch (Exception e) {
			throw e;
			
		} finally {
			close(fcout);
			close(fcin);
			
			close(outputStream);
			close(inputStream);
		}

		return bCopy;
	}
	
	
	private static void close(FileChannel fileChannel) {
		try {
			if (fileChannel != null) {
				fileChannel.close();
			}
		} catch (Exception e) {
			fileChannel = null;
		}
	}
	
	
	private static void close(FileInputStream fileInputStream) {
		try {
			if (fileInputStream != null) {
				fileInputStream.close();
			}
		} catch (Exception e) {
			fileInputStream = null;
		}
	}
	
	
	private static void close(FileOutputStream fileOutputStream) {
		try {
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		} catch (Exception e) {
			fileOutputStream = null;
		}
	}
}
