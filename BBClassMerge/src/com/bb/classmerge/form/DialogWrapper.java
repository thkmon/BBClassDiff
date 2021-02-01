package com.bb.classmerge.form;


import java.io.File;

import javax.swing.JFileChooser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * SWT 다이얼로그를 사용하고 오류 발생 시 AWT 다이얼로그 사용
 */
public class DialogWrapper {

	/**
	 * 폴더 선택 다이얼로그 열기
	 * 
	 * @param defaultPath
	 * @return
	 */
	public String openFolderDialog(String defaultPath) {
		String resultPath = "";

		Display display = null;
		Shell shell = null;

		defaultPath = getDefaltPath(defaultPath);

		try {
			display = new Display();
			shell = new Shell(display, SWT.OPEN);

			// 폴더만 선택
			DirectoryDialog dialog = new DirectoryDialog(shell);

			// 기본 위치 변경
			dialog.setFilterPath(defaultPath);

			// 폴더 선택 다이얼로그 열기
			resultPath = dialog.open();

		} catch (Exception e) {
			// 폴더만 선택
			JFileChooser fileDialog = new JFileChooser();
			fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			// 기본 위치 변경
			fileDialog.setCurrentDirectory(new File(defaultPath));

			// 폴더 선택 다이얼로그 열기
			int returnVal = fileDialog.showOpenDialog(null);
			if (returnVal == 0) {
				String selectedPath = fileDialog.getSelectedFile().getAbsolutePath();
				resultPath = selectedPath;
			}

		} finally {
			close(shell, display);
		}

		return resultPath;
	}

	/**
	 * 파일 선택 다이얼로그 열기
	 * 
	 * @param defaultPath
	 * @return
	 */
	public String openFileDialog(String defaultPath) {
		String resultPath = "";

		Display display = null;
		Shell shell = null;

		defaultPath = getDefaltPath(defaultPath);

		try {
			display = new Display();
			shell = new Shell(display);

			// 파일만 선택
			// FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
			FileDialog dialog = new FileDialog(shell, SWT.OPEN);

			// 기본 위치 변경
			dialog.setFilterPath(defaultPath);

			/*
			 * String[] extensions, String[] filterNames
			 * dialog.setFilterExtensions(extensions); dialog.setFilterNames(filterNames);
			 */

			// 파일 선택 다이얼로그 열기
			resultPath = dialog.open();

		} catch (Exception e) {
			// 파일만 선택
			JFileChooser fileDialog = new JFileChooser();
			fileDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);

			// 기본 위치 변경
			fileDialog.setCurrentDirectory(new File(defaultPath));

			// 파일 선택 다이얼로그 열기
			int returnVal = fileDialog.showOpenDialog(null);
			if (returnVal == 0) {
				String selectedPath = fileDialog.getSelectedFile().getAbsolutePath();
				resultPath = selectedPath;
			}

		} finally {
			close(shell, display);
		}

		return resultPath;
	}

	/**
	 * 기본 패스 가져오기
	 * 
	 * @param defaultPath
	 * @return
	 */
	private String getDefaltPath(String defaultPath) {
		if (defaultPath != null && defaultPath.length() > 0) {
			File fileObj = new File(defaultPath);
			if (fileObj.exists()) {
				return fileObj.getAbsolutePath();
			}
		}

		File fileObj = new File("C:\\");
		if (fileObj.exists()) {
			return fileObj.getAbsolutePath();
		}

		return "";
	}

	private void close(Shell shell, Display display) {
		try {
			if (shell != null) {
				shell.close();
			}
		} catch (NullPointerException e) {
		} catch (Exception e) {
		}

		try {
			if (shell != null && display != null) {
				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}
			}
		} catch (NullPointerException e) {
		} catch (Exception e) {
		}

		try {
			if (display != null) {
				display.dispose();
			}
		} catch (NullPointerException e) {
		} catch (Exception e) {
		}
		
		shell = null;
		display = null;
	}
}