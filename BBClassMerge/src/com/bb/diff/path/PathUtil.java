package com.bb.diff.path;


public class PathUtil {
	
	/**
	 * 표준 패스를 보정한다.
	 * 슬래시 -> 역슬래시 등 처리한다.
	 * 
	 * @param pathStr
	 * @return
	 */
	public static String reviseStandardPath(String pathStr) {
		if (pathStr == null || pathStr.length() == 0) {
			return "";
		}
		
		// 슬래시를 역슬래시로
		pathStr = pathStr.replace("/", "\\");
		
		// 역슬래시 2개를 역슬래시 1개로
		while (pathStr.indexOf("\\\\") > -1) {
			pathStr = pathStr.replace("\\\\", "\\");
		}
		
		// 맨 끝의 역슬래시 삭제
		while (pathStr.endsWith("\\")) {
			pathStr = pathStr.substring(0, pathStr.length() - 1);
		}
		
		// 맨 앞의 역슬래시 삭제
		while (pathStr.startsWith("\\")) {
			pathStr = pathStr.substring(1);
		}
		
		return pathStr;
	}
	
	
	/**
	 * 확장자 부분을 포함한 파일명 가져오기
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileNameWithExt(String path) {
		if (path == null || path.length() == 0) {
			return "";
		}
		
		path = reviseStandardPath(path);
		
		if (path.lastIndexOf("\\") > -1) {
			return path.substring(path.lastIndexOf("\\") + 1);
		}
		
		return path;
	}
	
	/**
	 * 확장자 부분만 가져오기
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileExtOnly(String path) {
		if (path == null || path.length() == 0) {
			return "";
		}
		
		path = reviseStandardPath(path);
		
		if (path.lastIndexOf("\\") > -1) {
			path = path.substring(path.lastIndexOf("\\") + 1);
		}
		
		int lastIndexDot = path.lastIndexOf(".");
		if (lastIndexDot < 0) {
			return "";
		}
		
		return path.substring(lastIndexDot + 1);
	}
	
	/**
	 * 파일위치 가져오기 (파일이 속한 폴더경로 가져오기)
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileFolderPath(String path) {
		if (path == null || path.length() == 0) {
			return "";
		}
		
		path = reviseStandardPath(path);
		
		if (path.lastIndexOf("\\") > -1) {
			return path.substring(0, path.lastIndexOf("\\"));
		}
		
		return path;
	}
}