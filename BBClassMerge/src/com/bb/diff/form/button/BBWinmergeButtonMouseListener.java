package com.bb.diff.form.button;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

import com.bb.classmerge.date.DateUtil;
import com.bb.classmerge.file.FileConverter;
import com.bb.diff.common.DiffConst;
import com.bb.diff.file.FileUtil;

public class BBWinmergeButtonMouseListener implements MouseListener {
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		try {
			String leftText = DiffConst.leftFileContent.getText();
			if (leftText == null) {
				leftText = "";
			}
			
			String rightText = DiffConst.rightFileContent.getText();
			if (rightText == null) {
				rightText = "";
			}
			
			String strDate = DateUtil.getTodayDateTime();
			String date1 = strDate + "_l";
			String date2 = strDate + "_r";
			
			String destDir = new FileConverter().makeDestinationDir();
			File file1 = new File(destDir + "/" + date1 + ".txt");
			if (!file1.exists()) {
				file1.createNewFile();
			}
			
			File file2 = new File(destDir + "/" + date2 + ".txt");
			if (!file2.exists()) {
				file2.createNewFile();
			}
			
			FileUtil.writeFile(file1, leftText);
			FileUtil.writeFile(file2, rightText);
			
			ProcessBuilder b = new ProcessBuilder();
			
			ArrayList<String> v = new ArrayList<String>();
			v.add("C:\\Program Files (x86)\\WinMerge\\WinMergeU.exe");
			v.add(file1.getAbsolutePath());
			v.add(file2.getAbsolutePath());
	
			b.command(v);
			b.start();
			
		} catch (Exception e0) {
			e0.printStackTrace();
		}
	}

}
