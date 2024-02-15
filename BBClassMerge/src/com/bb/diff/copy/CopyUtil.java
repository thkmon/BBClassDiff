package com.bb.diff.copy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;


public class CopyUtil {
	
	
	public static boolean copyFileCore(File originFile, File newFile) throws Exception {
		
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		FileChannel fcin = null;
		FileChannel fcout = null;
		
		try {
			String originPath = revisePath(originFile.getAbsolutePath());
			if (!originFile.exists()) {
				throw new Exception("copyFileCore : File not exists : " + originPath);
			}
			
			boolean mkdir = makeParentDir(newFile.getAbsolutePath());
			
			if (mkdir) {
				inputStream = new FileInputStream(originFile);         
				outputStream = new FileOutputStream(newFile);
				     
				fcin =  inputStream.getChannel();
				fcout = outputStream.getChannel();
				     
				long size = fcin.size();
				fcin.transferTo(0, size, fcout);
				
				System.out.println("copyFileCore transferTo : " + newFile.getAbsolutePath());
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			close(inputStream);
			close(outputStream);
			close(fcin);
			close(fcout);
		}
		
		return false;
	}
	
	
	private static void close(FileInputStream inputStream) {
		try {
			if (inputStream != null) {
				inputStream.close();
			}
		} catch (Exception e) {
			// 무시
		} finally {
			inputStream = null;
		}
	}
	
	
	private static void close(FileOutputStream outputStream) {
		try {
			if (outputStream != null) {
				outputStream.close();
			}
		} catch (Exception e) {
			// 무시
		} finally {
			outputStream = null;
		}
	}
	
	
	private static void close(FileChannel channel) {
		try {
			if (channel != null) {
				channel.close();
			}
		} catch (Exception e) {
			// 무시
		} finally {
			channel = null;
		}
	}
	
	
	/**
	  * 특정 파일패스의 부모 폴더가 없을 경우 만든다.
	  * 
	  * @param filePath
	  * @return
	  */
	public static boolean makeParentDir(String filePath) {

		if (filePath == null || filePath.trim().length() == 0) {
			System.err.println("makeParentDir : filePath == null || filePath.length() == 0");
			return false;

		} else {
			filePath = filePath.trim();
		}

		if (filePath.indexOf("/") > -1) {
			filePath = filePath.replace("/", "\\");
		}

		while (filePath.indexOf("\\\\") > -1) {
			filePath = filePath.replace("\\\\", "\\");
		}

		// 필요한 디렉토리 만들기
		int lastSlashPos = filePath.lastIndexOf("\\");

		if (lastSlashPos > -1) {
			File d = new File(filePath.substring(0, lastSlashPos));
			if (!d.exists()) {
				d.mkdirs();
			}

		} else {
			System.err.println("makeParentDir : lastSlashPos not exists");
			return false;
		}

		return true;
	}
	
	
	private static String revisePath(String path) {
		if (path == null) {
			return "";
		}
		
		path = path.trim();
		
		path = path.replace("\\", "/");
		while(path.indexOf("//") > -1) {
			path = path.replace("//", "/");
		}
		
		return path;
	}
}
