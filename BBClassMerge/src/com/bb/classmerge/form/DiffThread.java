package com.bb.classmerge.form;

import java.io.File;

import com.bb.classmerge.exception.MsgException;
import com.bb.classmerge.file.FileConverter;
import com.bb.classmerge.util.ConsoleUtil;
import com.bb.classmerge.util.StringUtil;
import com.bb.diff.main.DiffHelperMain;

public class DiffThread extends Thread {
	@Override
	public void run() {
		super.run();
		
		// 버튼 비활성화
		MainForm.setFormDisable();
				
		FileConverter fileConverter = new FileConverter();
		
		String classesDirText1 = MainForm.textField1.getText();
		String classesDirText2 = MainForm.textField2.getText();
		
		try {
			// 폴더경로 밸리드 체크
			fileConverter.checkDirectoryIsValid(classesDirText1, "[Left Classes Path]");
			fileConverter.checkDirectoryIsValid(classesDirText2, "[Right Classes Path]");
			
			File dir1 = new File(classesDirText1);
			File dir2 = new File(classesDirText2);
			String leftDirPath = StringUtil.revisePath(dir1.getAbsolutePath());
			String rightDirPath = StringUtil.revisePath(dir2.getAbsolutePath());
					
			ConsoleUtil.print("[" + MainForm.labelText1 + "] Input Path : " + leftDirPath);
			ConsoleUtil.print("[" + MainForm.labelText2 + "] Input Path : " + rightDirPath);
			
			DiffHelperMain.doDiffProcess(leftDirPath, rightDirPath);
			
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