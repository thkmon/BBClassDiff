package com.bb.diff.form.tree;

import java.io.File;

import com.bb.classmerge.util.FileNameUtil;
import com.bb.classmerge.util.StringUtil;
import com.bb.diff.common.CommonConst;
import com.bb.diff.copy.CopyPath;
import com.bb.diff.copy.CopyPathList;
import com.bb.diff.data.PathData;
import com.bb.diff.data.PathDataList;
import com.bb.diff.file.FileDiffController;
import com.bb.diff.file.FileUtil;
import com.bb.diff.log.LogUtil;
import com.bb.diff.path.PathUtil;
import com.bb.diff.prototype.StringList;

public class TreeUtil {
	public static String leftMark = "[Left]";
	public static String rightMark = "[Right]";
	
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
				
				if (CommonConst.bHideEmptyDirWithNoDiff) {
					// diff 정보가 없는 빈 폴더 숨기기 (Hide empty directory with no differences)
					removeEmptyFoldersInTree();
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

		BBTreeNode rootNode = CommonConst.fileTree.getRootNode();
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
			
			
			if (inLeftList && intRightList) {
				// 좌측과 우측 둘 다 해당하는 경우 띄우지 않는다.
				if (CommonConst.bHideBothFileDir) {
					return;
				}
				
			} else if (inLeftList) {
				// 좌측에만 있는 파일/폴더는  띄우지 않는다.
				if (CommonConst.bHideLeftOnlyFileDir) {
					return;
				}
				
			} else if (intRightList) {
				// 우측에만 있는 파일/폴더는  띄우지 않는다.
				if (CommonConst.bHideRightOnlyFileDir) {
					return;
				}
			}
			
			
			// 파일간 용량 차이
			long fileVolGap = 0;
			
			if (lastChunkElement) {
				if (inLeftList && intRightList) {
					// 폴더는 용량 체크해봤자 0으로 나온다. 파일 용량 차이만 표시.
					if (FileUtil.checkIsFile(leftAbsolutePath, rightAbsolutePath)) {
						// 양쪽에 있는 파일은 용량비교해서 gap을 표시한다.
						fileVolGap = getVolumeGap(leftAbsolutePath, rightAbsolutePath);
						nodeTitle = oneChunk + " " + "[" + fileVolGap + "]";
						
						// 용량 차이 없는 파일
						if (fileVolGap == 0) {
							if (CommonConst.bHideCapacityGapIsZero) {
								// 용량 차이 없는 파일은  띄우지 않는다.
								return;
							}
						}
					}
					
				} else if (inLeftList) {
					nodeTitle = oneChunk + " " + leftMark;
					
				} else if (intRightList) {
					nodeTitle = oneChunk + " " + rightMark;
				}
			}
			
			// 중복 아닐 경우만 트리에 노드 추가하기
			childNode = parentNode.addIfNotDupl(nodeTitle);
			childNode.setCorePath(corePath);
			
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

			/**
			 * 로그 쓰기
			 */
			if (lastChunkElement) {
				if (childNode.isFile() && inLeftList && intRightList) {
					// 용량 차이 없는 파일
					if (fileVolGap == 0) {
						LogUtil.appendLogFile("용량차이 없는 파일 : " + PathUtil.reviseStandardPath(leftAbsolutePath));
					}
				}
				
				if (inLeftList && !intRightList) {
					if (childNode.getLeftAbsoulutePath() != null && childNode.getLeftAbsoulutePath().length() > 0) {
						if (childNode.isFile()) {
							LogUtil.appendLogFile("Left에만 있는 파일 : " + childNode.getLeftAbsoulutePath());
						} else if (childNode.isDir()) {
							LogUtil.appendLogFile("Left에만 있는 폴더 : " + childNode.getLeftAbsoulutePath());
						}
					}
				}
				
				if (!inLeftList && intRightList) {
					if (childNode.getRightAbsoulutePath() != null && childNode.getRightAbsoulutePath().length() > 0) {
						if (childNode.isFile()) {
							LogUtil.appendLogFile("Right에만 있는 파일 : " + childNode.getRightAbsoulutePath());
						} else if (childNode.isDir()) {
							LogUtil.appendLogFile("Right에만 있는 폴더 : " + childNode.getRightAbsoulutePath());
						}
					}
				}
					
//				if (childNode.isFile() && inLeftList && intRightList) {
//					if (childNode.getLeftAbsoulutePath() != null && childNode.getLeftAbsoulutePath().length() > 0) {
//						if (childNode.getRightAbsoulutePath() != null && childNode.getRightAbsoulutePath().length() > 0) {
//							// 클래스 파일이 아닌 경우만 내용 비교
//							if (!childNode.getLeftAbsoulutePath().endsWith(".class")) {
//								if (!checkFileContentAreSame(childNode.getLeftAbsoulutePath(), childNode.getRightAbsoulutePath())) {
//									LogUtil.appendLogFile("내용불일치 : " + childNode.getLeftAbsoulutePath());
//								} else {
//									LogUtil.appendLogFile("내용일치 : " + childNode.getLeftAbsoulutePath());
//								}
//							}
//						}
//					}
//				}
				
			}
			
			parentNode = childNode;
		}
		
		// 그리기
		CommonConst.fileTree.refreshTreeSet();
	}
	
	
	/**
	 * 트리 전부 지우기
	 */
	public static void clearTree() {
		BBTreeNode rootNode = CommonConst.fileTree.getRootNode();
		clearNode(rootNode);
	}
	
	
	public static void clearNode(BBTreeNode node) {
		
		int count = node.getChildCount();
		if (count == 0) {
			node.removeMe();
			
		} else if (count > 0) {
			int lastIndex = count - 1;
			for (int i=lastIndex; i>=0; i--) {
				clearNode((BBTreeNode) node.getChildAt(i));
				
				count = node.getChildCount();
				if (count == 0) {
					node.removeMe();
				}
			}
		}
	}
	
	
	public static void redrawTree(boolean bEraseRightContents) {
		// 트리 전부 지우기
		clearTree();
		
		// 트리 그리기
		TreeUtil.drawTree(CommonConst.originParentPath, CommonConst.targetParentPath);

		// 우측 콘텐츠 내용 비우기
		if (bEraseRightContents) {
			CommonConst.leftFilePathText.setText("");
			CommonConst.rightFilePathText.setText("");
			
			CommonConst.leftFileContent.setText("");
			CommonConst.rightFileContent.setText("");
		}
	}
	
	
	/**
	 * 트리 펼치기 (트리 확장)
	 */
	public static void expandTree() {
		BBTree tree = CommonConst.fileTree;
	    int j = tree.getRowCount();
	    int i = 0;
	    while (i < j) {
	        tree.expandRow(i);
	        i += 1;
	        j = tree.getRowCount();
	    }
	}
	
	
	/**
	 * 트리 감추기 (트리 축소)
	 */
	public static void collapseTree() {
		BBTree tree = CommonConst.fileTree;
		int count = tree.getRowCount();
		int lastIndex = count - 1;
	    for (int i=lastIndex; i>=0; i--) {
	    	tree.collapseRow(i);
	    }
	}
	
	
	/**
	 * 파일 내용이 같은지 단순 검사한다. 클래스 파일이 아닌 경우만 사용할 것.
	 * 
	 * @param path1
	 * @param path2
	 * @return
	 */
	/*
	private static boolean checkFileContentAreSame(String path1, String path2) {
		if (path1 == null || path1.length() == 0) {
			return false;
		}
		
		if (path2 == null || path2.length() == 0) {
			return false;
		}
		
		// System.out.println("checkFileContentAreSame : " + path1);
		
		File f1 = new File(path1);
		File f2 = new File(path2);
		
		StringBuffer buffer1 = FileUtil.readFile(f1);
		StringBuffer buffer2 = FileUtil.readFile(f2);
		
		String content1 = buffer1.toString();
		String content2 = buffer2.toString();
		
		content1 = content1.replace("\r", "").replace("\n", "");
		content2 = content2.replace("\r", "").replace("\n", "");
		
		if (content1.equals(content2)) {
			return true;
		}
		
		return false;
	}
	*/
	
	
	/**
	 * 파일 1과 파일 2의 용량 차이를 계산한다.
	 * 물론, 클래스는 용량이 다르더라도, 버전 등 어떤 컴파일러에 의해 컴파일되었느냐에 따라 실제로는 동일할 수 있다.
	 * 
	 * @param filePath1
	 * @param filePath2
	 * @return
	 */
	public static long getVolumeGap(String filePath1, String filePath2) {
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
	
	
	/**
	 * 트리 내의 모든 빈 폴더 제거. 단, diff 정보가 있을 경우 남겨둔다.
	 */
	public static void removeEmptyFoldersInTree() {
		BBTreeNode rootNode = CommonConst.fileTree.getRootNode();
		removeEmptyFoldersInNode(rootNode, true);
	}
	
	
	/**
	 * 노드 내의 모든 빈 폴더 제거. 단, diff 정보가 있을 경우 남겨둔다.
	 */
	public static void removeEmptyFoldersInNode(BBTreeNode node, boolean bRootFolder) {
		if (node == null) {
			return;
		}
		
		int count = node.getChildCount();
		if (count == 0) {
			String title = node.getTitle();
			if (title.indexOf("[Left]") == -1 && title.indexOf("[Right]") == -1) {
				if (!bRootFolder && node.isDir()) {
					node.removeMe();
				}
			}
			
		} else if (count > 0) {
			int lastIndex = count - 1;
			for (int i=lastIndex; i>=0; i--) {
				removeEmptyFoldersInNode((BBTreeNode) node.getChildAt(i), false);
				
				count = node.getChildCount();
				if (count == 0) {
					String title = node.getTitle();
					if (title.indexOf("[Left]") == -1 && title.indexOf("[Right]") == -1) {
						if (!bRootFolder && node.isDir()) {
							node.removeMe();
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * 트리의 모든 파일 가져오기. 확장자(extension)를 지정할 경우 특정 확장자만 가져온다.
	 * 
	 * @param extension
	 * @return
	 */
	public static CopyPathList getAllFilePathInTree(String extension) {
		BBTreeNode rootNode = CommonConst.fileTree.getRootNode();
		
		CopyPathList copyList = new CopyPathList();
		addFilePath(copyList, rootNode, extension, true);
		return copyList;
	}
	
	
	/**
	 * 재귀 방식으로 CopyPathList 에 파일 경로 객체(CopyPath)를 추가한다.
	 * 
	 * @param copyList
	 * @param node
	 * @param extension
	 * @param bRootFolder
	 */
	private static void addFilePath(CopyPathList copyList, BBTreeNode node, String extension, boolean bRootFolder) {
		if (copyList == null) {
			return;
		}
		
		if (node == null) {
			return;
		}
		
		if (bRootFolder || node.isDir()) {
			BBTreeNode oneNode = null;
			int count = node.getChildCount();
			for (int i=0; i<count; i++) {
				oneNode = (BBTreeNode) node.getChildAt(i);
				addFilePath(copyList, oneNode, extension, false);
			}
			
		} else if (node.isFile()) {
			String originPath = "";
			String corePath = "";
			String title = node.getTitle();
			if (title != null && title.indexOf("[Right]") > -1) {
				originPath = node.getRightAbsoulutePath();
				corePath = node.getCorePath();
			} else {
				originPath = node.getLeftAbsoulutePath();
				corePath = node.getCorePath();
			}
			
			if (originPath != null && originPath.length() > 0) {
				if (corePath != null && corePath.length() > 0) {
					
					// 확장자 인자로 넘어왔을 경우, 확장자 검사
					if (extension != null && extension.length() > 0) {
						String oneExt = FileNameUtil.getExtensionFromPath(corePath);
						if (!extension.equalsIgnoreCase(oneExt)) {
							return;
						}
					}
					
					// 패스 담기
					CopyPath copyPath = new CopyPath();
					copyPath.setCorePath(corePath);
					copyPath.setOriginPath(originPath);
					copyList.add(copyPath);
				}
			}
		}
	}
	
	/**
	 * 빠른 찾기 기능
	 * 
	 * @param fileTree
	 * @param inputText
	 */
	public static void quickFind(BBTree fileTree, String inputText) {
		TreeFindUtil.quickFind(fileTree, inputText);
	}
}