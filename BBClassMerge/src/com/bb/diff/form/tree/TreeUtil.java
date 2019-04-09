package com.bb.diff.form.tree;

import java.io.File;

import com.bb.diff.common.DiffConst;
import com.bb.diff.data.PathData;
import com.bb.diff.data.PathDataList;
import com.bb.diff.file.FileDiffController;
import com.bb.diff.log.LogUtil;
import com.bb.diff.path.PathUtil;
import com.bb.diff.prototype.StringList;
import com.bb.diff.string.StringUtil;

public class TreeUtil {
	private static String leftMark = "[Left]";
	private static String rightMark = "[Right]";
	
	public static void drawTree(String leftClassesDir, String rightClassesDir) {
		
		try {
			FileDiffController fileDiffCtrl = new FileDiffController();
			PathDataList pathDataList = fileDiffCtrl.diffDirs(leftClassesDir, rightClassesDir);
			
			if (pathDataList != null && pathDataList.size() > 0) {
				int clsCount = pathDataList.size();
				for (int i=0; i<clsCount; i++) {
					if (pathDataList.get(i) == null) {
						continue;
					}
					
					PathData pathObj = pathDataList.get(i);
					
					boolean bLeftFileExists = false;
					String leftPath = pathObj.getLeftFullPath();
					if (leftPath != null && leftPath.length() > 0) {
						if (new File(leftPath).exists()) {
							bLeftFileExists = true;
						}
					}
					
					boolean bRightFileExists = false;
					String rightPath = pathObj.getRightFullPath();
					if (rightPath != null && rightPath.length() > 0) {
						if (new File(rightPath).exists()) {
							bRightFileExists = true;
						}
					}
					
					String simplePath = pathObj.getSimplePath();
					
					addNodeByAbsolutePath(leftPath, rightPath, simplePath, bLeftFileExists, bRightFileExists);
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void addNodeByAbsolutePath(String leftAbsolutePath, String rightAbsolutePath, String inputCorePath, boolean inLeftList, boolean intRightList) {		
		
		if (inputCorePath == null || inputCorePath.length() == 0) {
			LogUtil.appendLogFileForError("addNodeByAbsolutePath : 입력된 패스가 null입니다. 노드를 추가할 수 없습니다.");
			return;
		}
		
		inputCorePath = PathUtil.reviseStandardPath(inputCorePath);
		
		if (inputCorePath == null || inputCorePath.length() == 0) {
			LogUtil.appendLogFileForError("addNodeByAbsolutePath : 잘못된 패스가 넘어왔습니다. 노드를 추가할 수 없습니다.");
		}
		
		addToRootNodeByCorePath(leftAbsolutePath, rightAbsolutePath, inputCorePath, inLeftList, intRightList);
	}
	
	/**
	 * root 노드까지의 패스를 뗀 패스(==corePath)를 인자로 넘기면 추가된다.
	 * 
	 * @param corePath
	 */
	private static void addToRootNodeByCorePath(String leftAbsolutePath, String rightAbsolutePath, String corePath, boolean inLeftList, boolean intRightList) {
		if (corePath == null || corePath.length() == 0) {
			LogUtil.appendLogFileForError("addToRootNodeByCorePath : 패스 내용이 없습니다. 노드를 추가할 수 없습니다. corePath is null or empty.");
		}
		
		StringList corePathList = StringUtil.splitByOneDigit(corePath, "\\");
		
		int count = corePathList.size();
		if (count < 1) {
			LogUtil.appendLogFileForError("addToRootNodeByCorePath : 추가할 패스 구성요소가 없습니다. 노드를 추가할 수 없습니다. corePath is [" + corePath + "]");
		}

		BBTreeNode rootNode = DiffConst.fileTree.getRootNode();
		BBTreeNode parentNode = rootNode;
		BBTreeNode childNode = null;
		
		boolean lastChunkElement = true;
		File oneFile = null;
		String oneChunk = "";
		
		for (int i=0; i<count; i++) {
			oneFile = null;
			oneChunk = corePathList.get(i);
			
			if (oneChunk == null || oneChunk.length() == 0) {
				// 일어날 수 없는 상황. 노드를 추가할 수 없습니다.
				LogUtil.appendLogFileForError("addToRootNodeByCorePath : 패스 형식이 잘못되었습니다. 노드를 추가할 수 없습니다. corePath is [" + corePath + "]");
				return;
			}
			
			// 마지막 청크인지 검사
			if (i == (count - 1)) {
				lastChunkElement = true;
			} else {
				lastChunkElement = false;
			}
			
			String nodeTitle = "";
			nodeTitle = oneChunk;
			
			if (oneChunk.indexOf(".") > 0) {
				if (inLeftList && intRightList) {
					// 양쪽에 있는 파일은 용량비교해서 gap을 표시한다.
					long gap = getVolumeGap(leftAbsolutePath, rightAbsolutePath);
					nodeTitle = oneChunk + " " + "[" + gap + "]";
					
				} else if (inLeftList) {
					nodeTitle = oneChunk + " " + leftMark;
				} else if (intRightList) {
					nodeTitle = oneChunk + " " + rightMark;
				}
			}
		
//			if (inLeftList && intRightList) {
//			} else {
//				// 둘 다 해당이 아닐 경우 띄우지 않는다.
//				return;
//			}
			
			// 중복 아닐 경우만 트리에 노드 추가하기
			childNode = parentNode.addIfNotDupl(nodeTitle);
			
			if (childNode.getLeftAbsoulutePath() == null || childNode.getLeftAbsoulutePath().length() == 0) {
				if (leftAbsolutePath != null && leftAbsolutePath.length() > 0) {
					leftAbsolutePath = PathUtil.reviseStandardPath(leftAbsolutePath);
					if (leftAbsolutePath.length() > 0) {
						childNode.setLeftAbsoulutePath(leftAbsolutePath);
						// 마지막 청크일 때만 파일인지 검사한다. (전체 패스가 C:/aaa/bbb/temp.java 일 경우, 마지막 청크는 temp.java 임)
						if (lastChunkElement && oneFile == null) {
							oneFile = new File(leftAbsolutePath);
						}
					}
				}
			}
			
			if (childNode.getRightAbsoulutePath() == null || childNode.getRightAbsoulutePath().length() == 0) {
				if (rightAbsolutePath != null && rightAbsolutePath.length() > 0) {
					rightAbsolutePath = PathUtil.reviseStandardPath(rightAbsolutePath);
					if (rightAbsolutePath.length() > 0) {
						childNode.setRightAbsoulutePath(rightAbsolutePath);
						// 마지막 청크일 때만 파일인지 검사한다. (전체 패스가 C:/aaa/bbb/temp.java 일 경우, 마지막 청크는 temp.java 임)
						if (lastChunkElement && oneFile == null) {
							oneFile = new File(rightAbsolutePath);
						}
					}
				}
			}
			
			// 마지막 청크일 때만 파일인지 검사한다. (전체 패스가 C:/aaa/bbb/temp.java 일 경우, 마지막 청크는 temp.java 임)
			if (lastChunkElement && oneFile != null && oneFile.isFile()) {
				childNode.setDir(false);
				childNode.setFile(true);
				
			} else {
				childNode.setDir(true);
				childNode.setFile(false);
			}
			
			parentNode = childNode;
		}
		
		// 그리기
		DiffConst.fileTree.refreshTreeSet();
	}
	
	
	/**
	 * 파일 1과 파일 2의 용량 차이를 계산한다.
	 * 물론, 클래스는 용량이 다르더라도, 버전 등 어떤 컴파일러에 의해 컴파일되었느냐에 따라 실제로는 동일할 수 있다.
	 * 
	 * @param filePath1
	 * @param filePath2
	 * @return
	 */
	private static long getVolumeGap(String filePath1, String filePath2) {
		File file1 = new File(filePath1);
		File file2 = new File(filePath2);
		
		long volume1 = 0;
		long volume2 = 0;
		
		if (file1 != null && file1.exists()) {
			volume1 = file1.length();
		}
		
		if (file2 != null && file2.exists()) {
			volume2 = file2.length();
		}
		
		long gap = volume1 - volume2;
		if (gap < 0) {
			gap = gap * -1;
		}
		
		return gap;
	}
}