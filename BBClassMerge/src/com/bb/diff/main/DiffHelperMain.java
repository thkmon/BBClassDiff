package com.bb.diff.main;

import com.bb.classmerge.main.BBClassDiff;
import com.bb.diff.common.CommonConst;
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
		
		CommonConst.originParentPath = leftDirPath;
		CommonConst.targetParentPath = rightDirPath;
		
		if (CommonConst.originParentPath == null || CommonConst.originParentPath.length() == 0) {
			LogUtil.appendLogFileForError("CConst.originParentPath == null || CConst.originParentPath.length() == 0");
			return;
		}
		
		if (CommonConst.targetParentPath == null || CommonConst.targetParentPath.length() == 0) {
			LogUtil.appendLogFileForError("CConst.targetParentPath == null || CConst.targetParentPath.length() == 0");
			return;
		}
		
		// 패스 보정
		CommonConst.originParentPath = PathUtil.reviseStandardPath(CommonConst.originParentPath);
		CommonConst.targetParentPath = PathUtil.reviseStandardPath(CommonConst.targetParentPath);
		
		LogUtil.appendLogFile("#### DirectoryDiffHelper 시작");
		LogUtil.appendLogFile("#### " + DateUtil.getTodayDateTimeToPrint() + " 에 작성된 파일임.");
		LogUtil.appendLogFile();
		
		
		PathMaker pathMaker = new PathMaker();
		PathList originList = pathMaker.makePathList(CommonConst.originParentPath, CommonConst.includePathPattern, CommonConst.exceptPathPattern);
		PathList targetList = pathMaker.makePathList(CommonConst.targetParentPath, CommonConst.includePathPattern, CommonConst.exceptPathPattern);
		
		LogUtil.appendLogFile("#### 원본 디렉토리 경로(CConst.originParentPath) : " + CommonConst.originParentPath);
		LogUtil.appendLogFile("#### 원본 디렉토리 내 파일 개수 (originList.size()) : " + originList.size());
		LogUtil.appendLogFile();
		
		LogUtil.appendLogFile("#### 대상 디렉토리 경로 (CConst.targetParentPath) : " + CommonConst.targetParentPath);
		LogUtil.appendLogFile("#### 대상 디렉토리 내 파일 개수 (targetList.size()) : " + targetList.size());
		LogUtil.appendLogFile();
		
		/**
		 * 비교 폼을 띄운다.
		 */
		showDiffForm(CommonConst.originParentPath, CommonConst.targetParentPath);
		
		// 기존 메인폼 숨김처리
		if (BBClassDiff.mainForm != null) {
			BBClassDiff.mainForm.setVisible(false);
		}
	}
	
	
	public static void showDiffForm(String leftClassesDir, String rightClassesDir) {
		CommonConst.diffForm = new DiffForm(leftClassesDir, rightClassesDir);
	}
}