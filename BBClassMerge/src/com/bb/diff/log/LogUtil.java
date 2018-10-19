package com.bb.diff.log;

import java.io.File;

import com.bb.diff.common.DiffConst;
import com.bb.diff.file.FileUtil;
import com.bb.diff.prototype.PathList;

public class LogUtil {
	
	private static File commonLogFileObj = null;
	
//	public static String getCommonLogFilePath() {
//		return CConst.commonLogFilePath;
//	}
//
//	public static void setCommonLogFilePath(String commonLogFilePath) {
//		CConst.commonLogFilePath = commonLogFilePath;
//	}

	public static File getCommonLogFileObj() {
		return commonLogFileObj;
	}

	public static void setCommonLogFileObj(File commonLogFileObj) {
		LogUtil.commonLogFileObj = commonLogFileObj;
	}

	/**
	 * 로그 파일 쓴다.
	 * 
	 * @return
	 */
	public static boolean appendLogFile() {
		return appendLogFile("", false);
	}
	
	/**
	 * 로그 파일 쓴다.
	 * 
	 * @param str
	 * @return
	 */
	public static boolean appendLogFile(String str) {
		return appendLogFile(str, false);
	}
	
	/**
	 * 로그 파일 쓴다.
	 * 
	 * @param strList
	 * @return
	 */
	public static boolean appendLogFile(PathList strList) {
		return appendLogFile(strList.toString(), false);
	}
	
	/**
	 * 에러 모드로 로그 파일 쓴다.
	 * 
	 * @param logContent
	 * @return
	 */
	public static boolean appendLogFileForError(String logContent) {
		return appendLogFile("#### ERROR : " + logContent, true);
	}
	/**
	 * 로그 파일 쓴다.
	 * 
	 * @param logContent
	 * @return
	 */
	public static boolean appendLogFile(String logContent, boolean isError) {
		
		if (commonLogFileObj == null) {
			if (DiffConst.bWriteLogFile) {
				boolean logFileCreated = createLogFile();
				if (!logFileCreated) {
					return false;
				}
			}
		}
		
		if (DiffConst.bWriteLogFile) {
			FileUtil.appendFile(commonLogFileObj, logContent, true);
		}
		
		if (!isError) {
			System.out.println(logContent);
		} else {
			System.err.println(logContent);
		}
		return true;
	}
	
	public static boolean createLogFile() {
		
		int seqNum = 0;
		
		try {
			String commonLogDirPath = "log";
			File logDir = new File(commonLogDirPath);
			if (!logDir.exists()) {
				logDir.mkdirs();
			}
			
			File tmpLogFile = null;
			
			String tempFilePath = "";
			
			while (true) {
				tempFilePath = "log\\" + DiffConst.todayDate + "_" + String.valueOf(seqNum);
				
				tmpLogFile = new File(tempFilePath + ".txt");
				if (tmpLogFile != null && !tmpLogFile.exists()) {
					tmpLogFile.createNewFile();
					break;
				}
				
				seqNum++;
				if (seqNum > 1000) {
					System.err.println("로그파일을 생성할 수 없습니다. seqNum is over 100.");
					return false;
				}
			}
			
			DiffConst.seqNumStr = String.valueOf(seqNum);
			DiffConst.commonLogFilePath = tempFilePath + ".txt";
			commonLogFileObj = new File(DiffConst.commonLogFilePath);
			
			DiffConst.commonOriginToCopyDirPath = tempFilePath + "_origin";
			DiffConst.commonTargetToCopyDirPath = tempFilePath + "_target";
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
}
