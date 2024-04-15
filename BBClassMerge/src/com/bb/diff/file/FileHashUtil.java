package com.bb.diff.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;

public class FileHashUtil {
	
	
	/**
	 * 파일 해시값 계산
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileHash(String filePath) {
	    byte[] fileHashBytes = getFileHashBytes(filePath);
	    String fileHash = "";
	    if (fileHashBytes != null) {
	    	fileHash = bytesToHex(fileHashBytes);
	    }
	    return fileHash;
	}
	
    
	/**
	 * 파일 해시값을 계산하는 메서드
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	private static byte[] getFileHashBytes(String filePath) {
		byte[] result = null;
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(filePath);
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] buffer = new byte[8192]; // 버퍼 크기를 조정할 수 있습니다.
			int bytesRead = -1;

			while ((bytesRead = fis.read(buffer)) != -1) {
				digest.update(buffer, 0, bytesRead);
			}
			fis.close();

			result = digest.digest();

		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			close(fis);
		}

		return result;
	}
	
	
	/**
	 * 바이트 배열을 16진수 문자열로 변환하는 메서드
	 * 
	 * @param bytes
	 * @return
	 */
	private static String bytesToHex(byte[] bytes) {
		StringBuilder result = new StringBuilder();
		for (byte b : bytes) {
			result.append(String.format("%02x", b));
		}
		return result.toString();
	}

	
	private static void close(FileInputStream fis) {
		try {
			if (fis != null) {
				fis.close();
			}
		} catch (IOException e) {
		} catch (Exception e) {
		}
	}
}