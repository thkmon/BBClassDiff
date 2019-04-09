package com.bb.classmerge.form;

import java.io.File;

import com.bb.classmerge.exception.MsgException;
import com.bb.classmerge.file.FileConverter;
import com.bb.classmerge.util.ConsoleUtil;
import com.bb.classmerge.util.StringUtil;

public class DecompileThead extends Thread {

	@Override
	public void run() {
		super.run();
		
		// 버튼 비활성화
		MainForm.setFormDisable();
		
		FileConverter fileConverter = new FileConverter();
		
		String classesDirText1 = MainForm.textField1.getText();
		String classesDirText2 = MainForm.textField2.getText();
		
		boolean bDecompileDir2 = (classesDirText2 != null && classesDirText2.length() > 0);
		
		try {
			// 폴더경로 밸리드 체크
			fileConverter.checkDirectoryIsValid(classesDirText1);
			ConsoleUtil.print("[Left Classes Path] is valid.");
			if (bDecompileDir2) {
				fileConverter.checkDirectoryIsValid(classesDirText2);
				ConsoleUtil.print("[Right Classes Path] is valid.");
			} else {
				ConsoleUtil.print("[Right Classes Path] is empty. (skip)");
			}
			
			
			// 디컴파일 실행
			String destDirPath1 = "";
			String destDirPath2 = "";
			
			destDirPath1 = fileConverter.convertClassToJava(classesDirText1);
			if (bDecompileDir2) {
				destDirPath2 = fileConverter.convertClassToJava(classesDirText2);
			}
			
			
			// 파일 패스 보정
			File dir1 = null;
			File dir2 = null;
			String leftDirPath = "";
			String rightDirPath = "";
			
			dir1 = new File(classesDirText1);
			leftDirPath = StringUtil.revisePath(dir1.getAbsolutePath());
			if (bDecompileDir2) {
				dir2 = new File(classesDirText2);
				rightDirPath = StringUtil.revisePath(dir2.getAbsolutePath());
			}
			
			
			// 경로 출력
			ConsoleUtil.print("[" + MainForm.labelText1 + "] Input Path : " + leftDirPath);
			if (bDecompileDir2) {
				ConsoleUtil.print("[" + MainForm.labelText2 + "] Input Path : " + rightDirPath);
			}
			
			ConsoleUtil.print("[" + MainForm.labelText1 + "] Output Path : " + destDirPath1);
			if (bDecompileDir2) {
				ConsoleUtil.print("[" + MainForm.labelText2 + "] Output Path : " + destDirPath2);
			}
			
		} catch (MsgException ex) {
			ConsoleUtil.print(ex);
			
		} catch (Exception ex) {
			ConsoleUtil.print(ex);
			
		} finally {
			// 버튼 활성화
			MainForm.setFormEnable();
		}
	}

}
