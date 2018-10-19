package com.bb.diff.path;

import java.io.File;

import com.bb.diff.log.LogUtil;
import com.bb.diff.prototype.PathList;

public class PathMaker {
	/**
	 * path와 제외할 폴더명들, 제외할 파일명들을 넘기면 어레이리스트로 파일 패스 목록을 리턴하는 메서드.
	 * 
	 * @param path
	 * @param exceptFolders
	 * @param exceptFile
	 * @return
	 */
	public PathList makePathList(String path, String includePattern, String exceptPattern) {
		PathList pathList = new PathList();
		try {
			if (path == null || path.length() < 0) {
				LogUtil.appendLogFile("패스가 없습니다");
			}

			includePattern = includePattern.replace("\\", "\\\\");
			includePattern = includePattern.replace(".", "\\."); // 점은 정규식
			// 점으로
			includePattern = includePattern.replace("*", ".*"); // 별은 정규식
			// 모든문자로
			includePattern = includePattern.replace("?", "."); // 물음표는 정규식
			// 한문자로
			
			exceptPattern = exceptPattern.replace("\\", "\\\\");
			exceptPattern = exceptPattern.replace(".", "\\.");
			exceptPattern = exceptPattern.replace("*", ".*");
			exceptPattern = exceptPattern.replace("?", ".");

			// LogUtil.appendLogFile(includePattern);
			String[] includeArr = includePattern.split(",");
			String[] exceptArr = exceptPattern.split(",");

			path = path.replace("\\", "/");

			File root = new File(path);

			if (root.isDirectory()) {
				addPath(root, pathList, includeArr, exceptArr);
			} else if (root.isFile()) {
				pathList.add(root.getAbsolutePath());
				return pathList;

			} else {
				LogUtil.appendLogFile("해당 패스를 인식할 수 없습니다");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return pathList;
	}

	public void addPath(File dir, PathList pathList, String[] includeArr,
			String[] exceptArr) {
		if (dir.isDirectory()) {

			String[] dirList = dir.list();
			for (int i = 0; i < dirList.length; i++) {
				String fullPath = dir.getAbsolutePath() + "\\" + dirList[i];

				File file = new File(fullPath);
				addPath(file, pathList, includeArr, exceptArr);
			}

		} else if (dir.isFile()) {
			String filePath = dir.getAbsolutePath();

			boolean include = false;
			for (int k = 0; k < includeArr.length; k++) {

				if (filePath.matches(includeArr[k])) {
					// 매치가 되면 break;
					include = true;
					break;
				}
			}

			if (!include) {
				return;
			}

			for (int k = 0; k < exceptArr.length; k++) {
				if (filePath.matches(exceptArr[k])) {
					return;
				}
			}

//			LogUtil.appendLogFile(filePath);
			pathList.add(filePath);
		}
	}
}
