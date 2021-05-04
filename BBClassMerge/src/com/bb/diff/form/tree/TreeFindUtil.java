package com.bb.diff.form.tree;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreePath;

import com.bb.diff.common.CommonConst;
import com.bb.diff.form.textarea.EditorUtil;
import com.bb.diff.path.PathUtil;

public class TreeFindUtil {
	
	private static final int NODE_VALUE_PATH_EQUALS = -100000;
	private static final int NODE_VALUE_NAME_EQUALS = -90000;
	private static final int NODE_VALUE_PATH_EQUALS_IGNORE_CASE = -80000;
	private static final int NODE_VALUE_NAME_EQUALS_IGNORE_CASE = -70000;
	
	// 찾기 관련 값 초기화
	private static boolean findMode = false;
	private static BBTreeNode foundNode = null;
	private static int foundNodeValue = 99999;

	/**
	 * 빠른 찾기
	 * 
	 * @param fileTree
	 * @param inputText
	 */
	protected static void quickFind(BBTree fileTree, String inputText) {
		if (inputText == null || inputText.trim().length() == 0) {
			CommonConst.quickFinderForm.doWhenFailToFind("");
			return;
		} else {
			inputText = inputText.trim();
		}
		
		if (findMode) {
			return;
		}

		try {
			// 찾기 관련 값 초기화
			findMode = true;
			foundNode = null;
			foundNodeValue = 99999;

			BBTreeNode rootNode = fileTree.getRootNode();
			quickFindCore(rootNode, inputText);

			if ((foundNode != null) && (foundNode.isFile())) {
				if (selectTreeNode(fileTree, foundNode)) {
					EditorUtil.loadFileByNode(foundNode, false, true);

					CommonConst.quickFinderForm.close();
				}
			} else {
				CommonConst.quickFinderForm.doWhenFailToFind(inputText);
			}
			
		} finally {
			findMode = false;
		}
	}

	/**
	 * 빠른 찾기
	 * 
	 * @param node
	 * @param inputText
	 */
	private static void quickFindCore(BBTreeNode node, String inputText) {
		if ((inputText == null) || (inputText.length() == 0)) {
			return;
		}

		boolean findWithFilePath = false;
		if ((inputText.indexOf("\\") > -1) || (inputText.indexOf("/") > -1)) {
			findWithFilePath = true;
		}

		if (node.getChildCount() > 0) {
			int childCount = node.getChildCount();

			for (int i = 0; i < childCount; i++) {
				BBTreeNode oneNode = (BBTreeNode) node.getChildAt(i);
				if (oneNode.isFile()) {
					quickFindCore(oneNode, inputText);
				} else if (oneNode.isDir()) {
					quickFindCore(oneNode, inputText);
				}
			}
		} else if (node.getChildCount() == 0) {
			if (node.getCorePath() == null) {
				return;
			}

			// 파일만 찾기
			if (!node.isFile()) {
				return;
			}

			// (1) 파일패스로 찾기
			if (findWithFilePath) {
				String oneFilePath = PathUtil.reviseStandardPath(node.getCorePath());
				String inputFilePath = PathUtil.reviseStandardPath(inputText);

				if (oneFilePath.equals(inputFilePath)) {
					if (foundNodeValue > NODE_VALUE_PATH_EQUALS) {
						foundNodeValue = NODE_VALUE_PATH_EQUALS;
						foundNode = node;
					}
				} else if (oneFilePath.toLowerCase().equals(inputFilePath.toLowerCase())) {
					if (foundNodeValue > NODE_VALUE_PATH_EQUALS_IGNORE_CASE) {
						foundNodeValue = NODE_VALUE_PATH_EQUALS_IGNORE_CASE;
						foundNode = node;
					}
				} else {
					int value = getSimilarity(oneFilePath, inputFilePath, true);
					if ((-1 < value) && (foundNodeValue > value)) {
						foundNodeValue = value;
						foundNode = node;
					}
				}
			} else {
				// (2) 파일명으로 찾기
				String oneFileName = "";
				int lastSlashIndex = node.getCorePath().lastIndexOf("\\");
				if (lastSlashIndex > -1)
					oneFileName = node.getCorePath().substring(lastSlashIndex + 1);
				else {
					oneFileName = node.getCorePath();
				}

				if (oneFileName.equals(inputText)) {
					if (foundNodeValue > NODE_VALUE_NAME_EQUALS) {
						foundNodeValue = NODE_VALUE_NAME_EQUALS;
						foundNode = node;
					}
				} else if (oneFileName.toLowerCase().equals(inputText.toLowerCase())) {
					if (foundNodeValue > NODE_VALUE_NAME_EQUALS_IGNORE_CASE) {
						foundNodeValue = NODE_VALUE_NAME_EQUALS_IGNORE_CASE;
						foundNode = node;
					}
				} else {
					int value = getSimilarity(oneFileName, inputText, true);
					if ((-1 < value) && (foundNodeValue > value)) {
						foundNodeValue = value;
						foundNode = node;
					}
				}
			}
		}
	}

	/**
	 * 유사도 가져오기
	 * 두 문자열이 유사할수록 작은 숫자를 리턴한다(같은 문자열이면 0을 리턴).
	 * 단, 0 미만이면 유사도가 없는 문자열이다.
	 * 
	 * @param str1
	 * @param str2
	 * @param ignoreCase
	 * @return
	 */
	private static int getSimilarity(String str1, String str2, boolean ignoreCase) {
		if (str1 == null) {
			str1 = "";
		}

		if (str2 == null) {
			str2 = "";
		}

		if (ignoreCase) {
			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();
		}
		
		if (str1.equals(str2)) {
			return 0;
		}

		int idx1 = str1.indexOf(str2);
		int idx2 = str2.indexOf(str1);

		int len1 = -1;
		if (idx1 > -1) {
			len1 = str1.substring(0, idx1).length() + str1.substring(idx1 + str2.length()).length();
		}

		int len2 = -1;
		if (idx2 > -1) {
			len2 = str2.substring(0, idx2).length() + str2.substring(idx2 + str1.length()).length();
		}

		if ((len1 > -1) && (len2 > -1)) {
			return len1 < len2 ? len1 : len2;
		}
		if (len1 > -1) {
			return len1;
		}
		if (len2 > -1) {
			return len2;
		}

		return -1;
	}

	/**
	 * 특정 노드의 TreePath 객체를 만들어 가져오기
	 * 
	 * @param node
	 * @return
	 */
	private static TreePath makeTreepath(BBTreeNode node) {
		if (node == null) {
			return null;
		}

		TreePath treepath = null;

		List<BBTreeNode> nodeList = new ArrayList<BBTreeNode>();
		nodeList.add(node);

		while (node.getParent() != null) {
			node = (BBTreeNode) node.getParent();
			nodeList.add(node);
		}

		int nodeCount = nodeList.size();
		if (nodeCount > 0) {
			BBTreeNode[] nodeArr = new BBTreeNode[nodeCount];
			for (int i = 0; i < nodeCount; i++) {
				nodeArr[i] = ((BBTreeNode) nodeList.get(nodeCount - 1 - i));
			}

			treepath = new TreePath(nodeArr);
		}

		return treepath;
	}

	/**
	 * 트리 노드를 선택하기
	 * 
	 * @param fileTree
	 * @param node
	 * @return
	 */
	private static boolean selectTreeNode(BBTree fileTree, BBTreeNode node) {
		if (node == null) {
			return false;
		}

		TreePath treepath = makeTreepath(node);
		if (treepath != null) {
			fileTree.setSelectionPath(treepath);

			// 트리 위치까지 스크롤
			Rectangle bounds = fileTree.getPathBounds(treepath);
			bounds.height = fileTree.getVisibleRect().height;
			fileTree.scrollRectToVisible(bounds);
			return true;
		}

		return false;
	}
}