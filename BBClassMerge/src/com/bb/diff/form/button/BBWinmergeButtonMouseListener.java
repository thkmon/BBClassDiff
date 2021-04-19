package com.bb.diff.form.button;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

import com.bb.classmerge.util.DirUtil;
import com.bb.classmerge.util.PropertiesUtil;
import com.bb.diff.common.CommonConst;
import com.bb.diff.file.FileUtil;
import com.bb.diff.form.alert.AlterForm;
import com.bb.diff.path.PathUtil;

public class BBWinmergeButtonMouseListener implements MouseListener {
	
	/**
	 * 윈머지 경로를 프로퍼티 파일에서 가져오도록 처리한다.
	 */
	private static String compareToolPath = "";
	
	
	/**
	 * 윈머지 경로를 프로퍼티 파일에서 가져오도록 처리한다.
	 * @return
	 */
	private static String getWinmergePath() {
		if (compareToolPath != null && compareToolPath.length() > 0) {
			return compareToolPath;
		}
		
		// 윈머지 경로를 프로퍼티 파일에서 가져오도록 처리한다.
		if (compareToolPath == null || compareToolPath.trim().length() == 0) {
			compareToolPath = PropertiesUtil.getValueFromProperties("compareToolPath");
			compareToolPath = PathUtil.reviseStandardPath(compareToolPath);
		}
		
		// 프로퍼티 내에 값이 없다면, 윈머지 기본경로를 넣어준다.
		if (compareToolPath == null || compareToolPath.trim().length() == 0) {
			compareToolPath = "C:\\Program Files (x86)\\WinMerge\\WinMergeU.exe";
		}
		
		return compareToolPath;
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}
	

	@Override
	public void mouseReleased(MouseEvent e) {
		
		try {
			String leftText = CommonConst.leftFileContent.getText();
			if (leftText == null) {
				leftText = "";
			}
			
			String rightText = CommonConst.rightFileContent.getText();
			if (rightText == null) {
				rightText = "";
			}
			
			
			/**
			 * winmerge를 위한 임시파일 만들기
			 */
			String fileName = PathUtil.getFileNameWithExt(CommonConst.leftFilePathText.getText());
			
			if (fileName == null || fileName.length() == 0) {
				fileName = PathUtil.getFileNameWithExt(CommonConst.rightFilePathText.getText());
			}
			
			if (fileName == null || fileName.length() == 0) {
				fileName = "temp";
			}
			
			fileName = fileName.replace(".", "_");
			fileName = fileName.replace("\\", "_");
			fileName = fileName.replace("/", "_");
			
			String fileName1 = fileName + "_l";
			String fileName2 = fileName + "_r";
			
			// temp 폴더 생성
			String destDirPath = DirUtil.makeDestinationDir("temp");
			
			String txtPath1 = PathUtil.reviseStandardPath(destDirPath + "/" + fileName1 + ".txt");
			String txtPath2 = PathUtil.reviseStandardPath(destDirPath + "/" + fileName2 + ".txt");
			
			// diff 프로그램이 존재하지 않을 경우 에러 메시지 표시
			String winmergePath = getWinmergePath();
			File winmergeObj = new File(winmergePath);
			if (!winmergeObj.exists()) {
				String korErrMsg = "프로그램이 존재하지 않습니다. (" + winmergeObj.getAbsolutePath() + ") option.properties 파일 내의 경로를 수정해주세요.";
				String enErrMsg = "The program does not exists. (" + winmergeObj.getAbsolutePath() + ") Please modify option.properties.";
				
				System.err.println(korErrMsg);
				System.err.println(enErrMsg);
				
				AlterForm.open(korErrMsg);
				return;
			}
			
			/**
			 * 파일 쓰기
			 */
			File file1 = new File(txtPath1);
			if (!file1.exists()) {
				file1.createNewFile();
			}
			
			File file2 = new File(txtPath2);
			if (!file2.exists()) {
				file2.createNewFile();
			}
			
			// 보다 수월한 winmerge 비교를 위해 내용 보정
			leftText = reviseFileContentForWinmerge(leftText);
			rightText = reviseFileContentForWinmerge(rightText);
			
			FileUtil.writeFile(file1, leftText);
			FileUtil.writeFile(file2, rightText);
			
			ProcessBuilder builder = new ProcessBuilder();
			
			ArrayList<String> argList = new ArrayList<String>();
			argList.add(winmergePath);
			argList.add(file1.getAbsolutePath());
			argList.add(file2.getAbsolutePath());
	
			builder.command(argList);
			builder.start();
			
		} catch (Exception e0) {
			e0.printStackTrace();
		}
	}
	
	/**
	 * 보다 수월한 winmerge 비교를 위해 내용 보정
	 * 
	 * @param str1
	 * @return
	 */
	private String reviseFileContentForWinmerge(String str1) {
		if (str1 == null || str1.length() == 0) {
			return "";
		}
		
		/*
		// MISSING_BLOCK_LABEL_ 고려
		str1 = str1.replaceAll("MISSING_BLOCK_LABEL\\_[0-9]*", "");
		
		// JVM INSTR new #96  <Class StringBuilder>; 와 JVM INSTR new #95  <Class StringBuilder>; 같게 인식하도록 처리
		str1 = str1.replaceAll("JVM INSTR #[0-9]*", "");
		
		str1 = str1.replace("org.w3c.dom.Document", "Document");
		
		str1 = str1.replace("super.", "");
		
		// new StringBuilder 와 new StringBuffer 는 편의를 위해 적당히 비교한다.
		// 예를 들어 좌측 클래스는 StringBuilder, 우측 클래스는 단순 String 으로 컴파일 되었을 경우
		// 사실상 같은 내용인데 diff 로 체크되면 사람이 눈으로 일일히 대조해봐야 한다.
		// 문자열을 적당히 잘라내어 유사하다고 판단되면 동일한 문자열로 본다.
		str1 = str1.replace(".append(\"\")", "");
		str1 = str1.replace("new StringBuilder", "");
		str1 = str1.replace("new StringBuffer", "");
		str1 = str1.replace(".append", "");
		str1 = str1.replace("String.valueOf", "");
		str1 = str1.replace(".toString()", "");
		
		str1 = str1.replace("(", "").replace(")", "");
		
		str1 = str1.replace(" + ", "");
		str1 = str1.replace("// Misplaced declaration of an exception variable", "");
		*/
		
		return str1;
	}
}