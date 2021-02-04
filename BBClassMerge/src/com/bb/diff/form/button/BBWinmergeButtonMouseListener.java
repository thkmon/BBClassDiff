package com.bb.diff.form.button;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

import com.bb.classmerge.util.DirUtil;
import com.bb.classmerge.util.PropertiesUtil;
import com.bb.diff.common.CommonConst;
import com.bb.diff.file.FileUtil;
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
			
			String winmergePath = getWinmergePath();
			File winmergeObj = new File(winmergePath);
			if (!winmergeObj.exists()) {
				System.err.println("The program does not exists. (" + winmergeObj.getAbsolutePath() + ") Please modify option.properties.");
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
}