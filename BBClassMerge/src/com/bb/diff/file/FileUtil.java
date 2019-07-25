package com.bb.diff.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;

import com.bb.diff.common.CommonConst;
import com.bb.diff.decompile.DecompileUtil;
import com.bb.diff.log.LogUtil;

public class FileUtil {
	
//	private static StringBuffer diffTempBuffer = new StringBuffer();
//	private static long tmpFileLen1 = 0;
//	private static long tmpFileLen2 = 0;
	
	
	/**
	 * 파일을 복사한다.
	 * 
	 * @param originFilePath
	 * @param newFilePath
	 * @return
	 */
	public static boolean copyFile(String originFilePath, String newFilePath) {
		
		FileChannel fcin = null;
		FileChannel fcout = null;
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		
		try {
			
			int lastSlash = newFilePath.lastIndexOf("\\");
			if (lastSlash > -1) {
				String parentDirPath = newFilePath.substring(0, newFilePath.lastIndexOf("\\"));
				File parentDir = new File(parentDirPath);
				if (!parentDir.exists()) {
					parentDir.mkdirs();
				}
			}
			
			File destinationFile = new File(newFilePath);
			if (!destinationFile.exists()) {
				destinationFile.createNewFile();
			}
			
			inputStream = new FileInputStream(originFilePath);
			outputStream = new FileOutputStream(newFilePath);
	
			fcin = inputStream.getChannel();
			fcout = outputStream.getChannel();
	
			long size = fcin.size();
			fcin.transferTo(0, size, fcout);
	
			fcout.close();
			fcin.close();
	
			outputStream.close();
			inputStream.close();
		
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			
		} finally {
			try {
				fcout.close();
			} catch (Exception ie) {
				ie.printStackTrace();
				fcout = null;
			}
			
			try {
				fcin.close();
			} catch (Exception ie) {
				ie.printStackTrace();
				fcin = null;
			}
			
			try {
				outputStream.close();
			} catch (Exception ie) {
				ie.printStackTrace();
				outputStream = null;
			}
			
			try {
				inputStream.close();
			} catch (Exception ie) {
				ie.printStackTrace();
				inputStream = null;
			}
		}
		
		return true;
	}
	
	
	/**
	 * 파일을 쓴다.
	 * 
	 * @param file
	 * @param buffer
	 */
	public static boolean writeFile(File file, String fileContent) {
		
		BufferedWriter writer = null;
		
		try {
			if (file == null) {
				LogUtil.appendLogFileForError("file == null");
				return false;
			}
			
			if (!file.exists()) {
				LogUtil.appendLogFileForError(file.getAbsolutePath() + " : 파일이 존재하지 않습니다.");
				return false;
			}
			
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			writer.write(fileContent, 0, fileContent.length());
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
			
		} finally {
			try {
				writer.close();
			} catch (Exception ie) {
				ie.printStackTrace();
				writer = null;
			}
		}
		
		return true;
	}
	
	
	/**
	 * 파일을 이어서 쓴다.
	 * 
	 * @param file
	 * @param fileContentBuffer
	 * @return
	 */
	public static boolean appendFile(File file, StringBuffer fileContentBuffer, boolean putEnter) {
		return appendFile(file, fileContentBuffer.toString(), putEnter);
	}
	
	
	/**
	 * 파일을 이어서 쓴다.
	 * 
	 * @param file
	 * @param buffer
	 */
	public static boolean appendFile(File file, String fileContent, boolean putEnter) {
		
		BufferedWriter writer = null;
		
		try {
			if (file == null) {
				LogUtil.appendLogFileForError("file == null");
				return false;
			}
			
			if (!file.exists()) {
				file.createNewFile();
			}
			
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8"));
			writer.write(fileContent, 0, fileContent.length());
			if (putEnter) {
				writer.newLine();
			}
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
			
		} finally {
			try {
				writer.close();
			} catch (Exception ie) {
				ie.printStackTrace();
				writer = null;
			}
		}
		
		return true;
	}
	

	/**
	 * 파일을 읽어들인다.
	 * 
	 * @param file
	 * @return
	 */
	public static StringBuffer readFile(File file) {
		StringBuffer fileContent = new StringBuffer();
		BufferedReader reader = null;
		
		try {
			if (file == null) {
				LogUtil.appendLogFileForError("readFile : file == null");
				return null;
			}
			
			if (!file.exists()) {
				LogUtil.appendLogFileForError("readFile : file.exists() == false : " + file.getAbsolutePath());
				new Exception().printStackTrace();
				return null;
			}
			
			if (!file.canRead()) {
				LogUtil.appendLogFileForError("readFile : file.canRead() == false : " + file.getAbsolutePath());
				return null;
			}
			
			String returnCode = getReturnCode(file);
			
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

			String oneLine = "";

			while ((oneLine = reader.readLine()) != null) {
				fileContent.append(oneLine);
				fileContent.append(returnCode);
			}
			
			if (fileContent.length() > 0) {
				fileContent.deleteCharAt(fileContent.length()-1);
			}

			reader.close();

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				if (reader != null) {
					reader.close();					
				}
			} catch (Exception ie) {
				reader = null;
			}
		}
		
		return fileContent;
	}
	
	
	/**
	 * 특정 파일의 엔터키 값(리턴값)을 찾아서 String 형태로 리턴한다.
	 * 첫 번째로 발견한 엔터키 값을 리턴한다.
	 * 
	 * @param file
	 * @return
	 */
	public static String getReturnCode(File file) {
		String returnCode = "\n";
		
		java.io.FileReader ioFileReader = null;
		
		try {
			// 엔터값(캐리지 리턴값) 알아내기
			ioFileReader = new java.io.FileReader(file);
			
			int preInt = -1;
			int oneInt = -1;
			while ((oneInt = ioFileReader.read()) > -1) {
				
				if (oneInt == 10) {
					if (preInt == 13) {
						// rn 발견
						returnCode = "\r\n";
						
					} else {
						// n 발견
						returnCode = "\n";
					}
					
					// 엔터 한 번이라도 발견하면 빠져나감
					break;
				}
				preInt = oneInt;
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			try {
				ioFileReader.close();
				
			} catch (Exception ie) {
				ie.printStackTrace();
				ioFileReader = null;
			}
		}
		
		return returnCode;
	}
	
	
	/**
	 * 같은 파일인지 검사해서 boolean을 리턴한다.
	 * 1. 클래스가 아닌 파일
	 * (1) 용량 검사
	 * (2) 파일 내 문자열 길이 검사
	 * (3) 파일 내용 검사
	 * 
	 * 2. 클래스 파일
	 * 역컴파일 내용 검사
	 * 
	 * @param fileObj1
	 * @param fileObj2
	 * @return
	 */
	public static boolean checkIsSameFile(File fileObj1, File fileObj2) {
		
		/**
		 * 검사
		 */
		int diffResult = checkIsSameFileBasic(fileObj1, fileObj2);
		
		if (diffResult == 1) {
			// 용량, 길이, 내용이 같은 파일임
			return true;
		}
		
		if (diffResult == -1) {
			// 한 쪽이 없거나 둘 다 없는 파일임. 파일 손상
			return false;
		}
		
		// DIFF시 클래스의 경우 디컴파일하여 검사할지 여부
		if (!CommonConst.doDecompileWhenClassDiff) {
			return false;
		}
		
		// 클래스라면 한 번 더 검증한다.
		// 디컴파일을 했을 떄는 동일할 수 있기 때문이다.
		boolean bClassFilesAreSame = checkIsSameClassFile(fileObj1, fileObj2);
		return bClassFilesAreSame;
	}
	
	
	/**
	 * 클래스가 아닌 파일들(txt, js, jsp, htm, html 등을 검사한다.)
	 * (1) 용량 (2) 길이 (3) 내용 비교한다.
	 * 
	 * @param fileObj1
	 * @param fileObj2
	 * @return
	 */
	private static int checkIsSameFileBasic(File fileObj1, File fileObj2) {
		
		try {
			if (fileObj1 == null) {
				LogUtil.appendLogFileForError("fileObj1 is null");
				return -1;
			}
			
			if (fileObj2 == null) {
				LogUtil.appendLogFileForError("fileObj1 is '" + fileObj1.getAbsoluteFile() + "'. but fileObj2 is null");
				return -1;
			}
			
			if (!fileObj1.exists()) {
				if (!fileObj2.exists()) {
					// 둘 다 없을 경우 추가할 수 없음.
					// (대신 한 쪽이라도 존재하면 용량이 다른 것과 마찬가지이니 추가하자.)
					LogUtil.appendLogFileForError("파일이 존재하지 않습니다. fileObj1 [" + fileObj1.getAbsoluteFile() + "] , fileObj2 [" + fileObj2.getAbsoluteFile() + "]");
					return -1;
				}
			}
			
			/**
			 * 용량 검사
			 */
			if (fileObj1.length() != fileObj2.length()) {
				// 용량 다를 경우
				return -2;
			}
			
			StringBuffer fileContent1 = readFile(fileObj1);
			StringBuffer fileContent2 = readFile(fileObj2);
			
			long tmpFileLen1 = fileContent1.length();
			long tmpFileLen2 = fileContent2.length();
			
			// 내용 저장
//			fileObj1.setFileContent(fileContent1);
//			fileObj2.setFileContent(fileContent2);
			
			/**
			 * 문자열 길이 검사
			 */
			if (tmpFileLen1 != tmpFileLen2) {
				return -3;
			}

			/**
			 * 내용 검사
			 */
			// contentDiffBuffer == null 이면 동일한 파일임
			// boolean isContentDiff = checkIsSameFileContent(fileContent1, fileContent2, fileObj1, fileObj2);
			boolean isContentDiff = checkIsSameFileContent(fileContent1, fileContent2);
			
			if (!isContentDiff) {
				// 파일 내용 다른 경우
				return -4; 
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 1;
	}
	
	/**
	 * 클래스 비교를 위한 메서드.
	 * 클래스 파일은 용량과 길이가 달라도, 역컴파일을 했을 때 내용이 동일한 경우가 있어서 따로 검사한다.
	 * (내용이 다르다고 무조건 다른게 아니다. 클래스는 역컴파일까지 해봐야 한다.)
	 * 
	 * @param fileObj1
	 * @param fileObj2
	 * @return
	 */
	private static boolean checkIsSameClassFile(File fileObj1, File fileObj2) {
		/**
		 * 2017-02-10 bb_
		 * 클래스가 아닌 파일(txt, jsp, js, java 등)은 (1) 용량, (2) 길이, (3) 내용. 3가지를 비교하면 된다.
		 * 그런데 클래스 파일은 용량, 길이, 내용 3가지가 달라도, 막상 디컴파일 해보면 동일한 경우가 발견되었다.
		 * 나의 추측으로는, 컴파일러가 다르면 동일한 파일도 다소 다르게 컴파일될 수 있다는 것.
		 * 
		 * 따라서
		 * 1. 클래스가 아닌 파일 : 용량, 길이, 내용 3가지 중 하나라도 다르면 다른 파일이다.
		 * 
		 * 2. 클래스 :
		 *   (2-1) 용량, 길이, 내용 3가지가 같으면, 같은 파일이다. (역공학 불필요).
		 *   (2-2) 용량, 길이, 내용 3가지가 다르면, 역공학을 해서 내용을 비교한다. 이것도 다르면 진짜 다른거.
		 * 
		 *  주의 : 디컴파일은 디컴파일러에 따라 지역 변수명이 랜덤하게 바뀌는 등 예외가 있으니 100% 라고 신뢰해서는 안된다.
		 */
		
		/**
		 * 마지막으로, 클래스의 경우 역컴파일 내용 검사
		 */
		if (fileObj1 == null) {
			return false;
		}
		
		if (fileObj1.getAbsolutePath() == null || fileObj1.getAbsolutePath().length() == 0) {
			return false;
		}
		
		if (!fileObj1.getAbsolutePath().endsWith(".class")) {
			return false;
		}
		
		StringBuffer decompiledContent1 = DecompileUtil.readClassFile(fileObj1.getAbsolutePath());
		StringBuffer decompiledContent2 = DecompileUtil.readClassFile(fileObj2.getAbsolutePath());
		
		// 디컴파일 해서 내용 비교
		// boolean clsContentIsDiff = checkIsSameFileContent(decompiledContent1, decompiledContent2, fileObj1, fileObj2);
		boolean clsContentIsDiff = checkIsSameFileContent(decompiledContent1, decompiledContent2);
		
		// 역컴파일 내용을 객체에 저장해둠
//		fileObj1.setFileDecompiledContent(decompiledContent1);
//		fileObj2.setFileDecompiledContent(decompiledContent2);
		
		if (!clsContentIsDiff) {
//			System.err.println("다르다!" + fileObj1.getAbsolutePath());
//			System.err.println("--다르다!" + fileObj2.getAbsolutePath());
//			System.err.println();
//			System.err.println();

			return false;
		}
		
		return true;
	}
	
	
	
	/**
	 * 파일의 내용을 비교한다. 같으면 true를 리턴한다.
	 * 에러일 경우 에러로그를 tmpDiffBuffer 에 저장한다.
	 * 
	 * @param fileContent1
	 * @param fileContent2
	 * @return
	 */
//	private static boolean checkIsSameFileContent(StringBuffer fileContent1, StringBuffer fileContent2, File fileObj1, File fileObj2) {
	private static boolean checkIsSameFileContent(StringBuffer fileContent1, StringBuffer fileContent2) {	
		int fileLen1 = fileContent1.length();
		
		// 파일별 엔터값 가져온다.
//		String file1ReturnCode = "\r\n";
//		if (fileObj1 != null) {
//			file1ReturnCode = getReturnCode(fileObj1);
//		}
		
//		int len1OfReturnCode = file1ReturnCode.length();
		
		// System.out.println("fileReturnCode1 : " + file1ReturnCode.replace("\r", "\\r").replace("\n", "\\n"));
		// System.out.println("len1OfReturnCode : " + len1OfReturnCode);
		
//		int file1RowCnt = 0;
//		int file1axisCol = 0;
		
		String oneChar1 = "";
		String oneChar2 = "";
		
		// 내용으로 비교. 용량이 같아도 내용이 다를 수 있다.
		for (int i=0; i<fileLen1; i++) {
			
//			if (StringUtil.subText(fileContent1, i, i+len1OfReturnCode).equals(file1ReturnCode)) {
//				file1RowCnt++;
//				file1axisCol = i;
//			}
			
			oneChar1 = fileContent1.substring(i, i+1);
			oneChar2 = fileContent2.substring(i, i+1);
			
			if (!oneChar1.equals(oneChar2)) {
				
//				diffTempBuffer = new StringBuffer();
//				
//				diffTempBuffer.append(i).append("번째 글자.");
//				diffTempBuffer.append(" 위치(").append(file1RowCnt).append(", ").append((i-file1axisCol)).append(")");
//				diffTempBuffer.append(" ([").append(StringUtil.subTextToPrint(fileContent1, i-10, i+10));
//				diffTempBuffer.append("] != [").append(StringUtil.subTextToPrint(fileContent2, i-10, i+10)).append("])");
//				System.err.println(diffTempBuffer.toString());
				
				return false;
			}
		}
		
		// 내용이 한 글자도 틀림 없이 같다.
		return true;
	}
	
	
	/**
	 * 특정 패스 2개를 넘겨 파일인지 확인한다. 폴더가 아니고, 현재 존재하는 파일일 경우 true를 리턴.
	 * (첫 번째 패스에 파일 또는 폴더가 존재할 경우, 두 번째 패스는 검사하지 않는다. 첫 번째 패스의 파일 여부만 리턴한다.) 
	 * 
	 * @param leftPath
	 * @param rightPath
	 * @return
	 */
	public static boolean checkIsFile(String leftPath, String rightPath) {
		Boolean bIsFile = checkIsFile(leftPath);
		if (bIsFile != null) {
			// 첫 번째 패스에 파일 또는 폴더가 존재할 경우, 두 번째 패스는 검사하지 않는다. 첫 번째 패스의 파일 여부만 리턴한다.
			return bIsFile;
		}
		
		bIsFile = checkIsFile(rightPath);
		if (bIsFile != null) {
			return bIsFile;
		}
		
		return false;
	}
	
	
	/**
	 * 특정 패스가 파일인지 확인한다. 폴더가 아니고, 현재 존재하는 파일일 경우 true를 리턴.
	 * 
	 * @param path
	 * @return
	 */
	public static Boolean checkIsFile(String path) {
		if (path == null || path.length() == 0) {
			return null;
		}
		
		File file = new File(path);
		if (!file.exists()) {
			return null;
		}
		
		if (file.isFile()) {
			return true;
		} else if (file.isDirectory()) {
			return false;
		}
		
		return null;
	}
}