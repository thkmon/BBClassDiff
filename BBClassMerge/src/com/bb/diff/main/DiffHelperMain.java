package com.bb.diff.main;

import com.bb.classmerge.main.BBClassMerge;
import com.bb.diff.common.DiffConst;
import com.bb.diff.date.DateUtil;
import com.bb.diff.form.form.DiffForm;
import com.bb.diff.log.LogUtil;
import com.bb.diff.path.PathMaker;
import com.bb.diff.path.PathUtil;
import com.bb.diff.prototype.PathList;

public class DiffHelperMain {

	
	/**
	 * Diff 시작한다.
	 * 
	 */
	public static void doDiffProcess(String leftDirPath, String rightDirPath) {
		
		DiffConst.originParentPath = leftDirPath;
		DiffConst.targetParentPath = rightDirPath;
		
		if (DiffConst.originParentPath == null || DiffConst.originParentPath.length() == 0) {
			LogUtil.appendLogFileForError("CConst.originParentPath == null || CConst.originParentPath.length() == 0");
			return;
		}
		
		if (DiffConst.targetParentPath == null || DiffConst.targetParentPath.length() == 0) {
			LogUtil.appendLogFileForError("CConst.targetParentPath == null || CConst.targetParentPath.length() == 0");
			return;
		}
		
		// 패스 보정
		DiffConst.originParentPath = PathUtil.reviseStandardPath(DiffConst.originParentPath);
		DiffConst.targetParentPath = PathUtil.reviseStandardPath(DiffConst.targetParentPath);
		
		LogUtil.appendLogFile("#### DirectoryDiffHelper 시작");
		LogUtil.appendLogFile("#### " + DateUtil.getTodayDateTimeToPrint() + " 에 작성된 파일임.");
		LogUtil.appendLogFile();
		
		
		PathMaker pathMaker = new PathMaker();
		PathList originList = pathMaker.makePathList(DiffConst.originParentPath, DiffConst.includePathPattern, DiffConst.exceptPathPattern);
		PathList targetList = pathMaker.makePathList(DiffConst.targetParentPath, DiffConst.includePathPattern, DiffConst.exceptPathPattern);
		
		LogUtil.appendLogFile("#### 원본 디렉토리 경로(CConst.originParentPath) : " + DiffConst.originParentPath);
		LogUtil.appendLogFile("#### 원본 디렉토리 내 파일 개수 (originList.size()) : " + originList.size());
		LogUtil.appendLogFile();
		
		LogUtil.appendLogFile("#### 대상 디렉토리 경로 (CConst.targetParentPath) : " + DiffConst.targetParentPath);
		LogUtil.appendLogFile("#### 대상 디렉토리 내 파일 개수 (targetList.size()) : " + targetList.size());
		LogUtil.appendLogFile();
		
		/**
		 * 비교 폼을 띄운다.
		 */
		showDiffForm(DiffConst.originParentPath, DiffConst.targetParentPath);
		
		// 기존 메인폼 숨김처리
		if (BBClassMerge.mainForm != null) {
			BBClassMerge.mainForm.setVisible(false);
		}
	}
	
	
	public static void showDiffForm(String leftClassesDir, String rightClassesDir) {
		DiffConst.diffForm = new DiffForm(leftClassesDir, rightClassesDir);
	}
}