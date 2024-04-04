package com.bb.classmerge.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import com.bb.diff.path.PathUtil;

public class FilePathUtil {
	
	/**
	 * 폴더경로 열기
	 * 
	 * @param filePath
	 */
	public static void openParentFolder(String filePath) {
		// String filePath = node.getLeftAbsoulutePath();
		if (filePath == null || filePath.length() == 0) {
			System.err.println("파일경로가 존재하지 않습니다.");
			return;
		}
		
		String folderPath = PathUtil.getFileFolderPath(filePath);
		if (folderPath == null || folderPath.length() == 0) {
			System.err.println("폴더경로가 존재하지 않습니다.");
			return;
		}
		
	    try {
			// Desktop 인스턴스 가져오기
	        Desktop desktop = Desktop.getDesktop();

	        // 폴더가 존재하고, 지원되는 플랫폼일 경우에만 폴더 열기 시도
	        File folder = new File(folderPath);
	        if (folder.exists() && Desktop.isDesktopSupported()) {
	            desktop.open(folder);
	        } else {
	            System.err.println("폴더가 존재하지 않거나, 지원되지 않는 플랫폼입니다.");
	        }
	        
	    } catch (IOException e2) {
	        e2.printStackTrace();
	    }
	}
	
	/**
	 * 파일 열기
	 * 
	 * @param filePath
	 */
	public static void openFile(String filePath) {
		// String filePath = node.getLeftAbsoulutePath();
		if (filePath == null || filePath.length() == 0) {
			System.err.println("파일경로가 존재하지 않습니다.");
			return;
		}
		
        try {
    		// Desktop 인스턴스 가져오기
	        Desktop desktop = Desktop.getDesktop();

	        // 파일이 존재하고, 지원되는 플랫폼일 경우에만 폴더 열기 시도
	        File file = new File(filePath);
            if (file.exists() && Desktop.isDesktopSupported()) {
                desktop.open(file);
            } else {
                System.err.println("파일이 존재하지 않거나, 지원되지 않는 플랫폼입니다.");
            }
            
        } catch (IOException e2) {
            e2.printStackTrace();
        }
	}
}