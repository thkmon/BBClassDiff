package com.bb.diff.form.textarea;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.bb.diff.common.CommonConst;
import com.bb.diff.form.tree.BBTreeNode;
import com.bb.diff.form.tree.BBTreeNodeList;
import com.bb.diff.form.tree.TreeUtil;
import com.bb.diff.map.FileContentUtil;
import com.bb.diff.path.PathUtil;
import com.bb.diff.prototype.Col;
import com.bb.diff.prototype.ColList;
import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.Patch;
import com.thkmon.bbj3.io.file.prototype.FileContent;
import com.thkmon.bbj3.io.file.util.FileReadUtil;
import com.thkmon.bbj3.parser.buff.prototype.ParsedContent;

public class EditorUtil {
	
	private static StyledDocument leftDoc = null;
	private static StyledDocument rightDoc = null;
	
	private static ColList colList1 = null;
	private static ColList colList2 = null;
	
	private static Style whiteStyle = null;
	private static Style strongStyle1 = null;
	private static Style strongStyle2 = null;
	
	public static void paintLeftDocWhite(int lineNum) {
		Col oneCol = colList1.get(lineNum);
		leftDoc.setCharacterAttributes(oneCol.getBeginCol(), oneCol.getGapCol(), whiteStyle, true);
	}
	
	public static void paintRightDocWhite(int lineNum) {
		Col oneCol = colList2.get(lineNum);
		rightDoc.setCharacterAttributes(oneCol.getBeginCol(), oneCol.getGapCol(), whiteStyle, true);
	}
	
	public static void paintLeftDocStrong(int lineNum) {
		Col oneCol = colList1.get(lineNum);
		leftDoc.setCharacterAttributes(oneCol.getBeginCol(), oneCol.getGapCol(), strongStyle1, true);
	}
	
	public static void paintRightDocStrong(int lineNum) {
		Col oneCol = colList2.get(lineNum);
		rightDoc.setCharacterAttributes(oneCol.getBeginCol(), oneCol.getGapCol(), strongStyle2, true);
	}
	
	public static boolean checkLeftFileExists(BBTreeNode node) {
		String leftPath = node.getLeftAbsoulutePath();
		if (leftPath == null || leftPath.length() == 0) {
			return false;
		}
		
		File file = new File(leftPath);
		if (!file.exists()) {
			return false;
		}
		
		return true;
	}
	
	public static boolean checkRightFileExists(BBTreeNode node) {
		String rightPath = node.getRightAbsoulutePath();
		if (rightPath == null || rightPath.length() == 0) {
			return false;
		}
		
		File file = new File(rightPath);
		if (!file.exists()) {
			return false;
		}
		
		return true;
	}
	
	private static boolean setLeftPathText(BBTreeNode node) {
		String leftPath = node.getLeftAbsoulutePath();
		if (leftPath == null || leftPath.length() == 0) {
			CommonConst.leftFilePathText.setText(CommonConst.FEMPTY);
			return false;
		}
		
		File file = new File(leftPath);
		if (!file.exists()) {
			CommonConst.leftFilePathText.setText(CommonConst.FEMPTY);
			return false;
		}
		
		CommonConst.leftFilePathText.setText(leftPath);
		return true;
	}
	
	private static boolean setRightPathText(BBTreeNode node) {
		String rightPath = node.getRightAbsoulutePath();
		if (rightPath == null || rightPath.length() == 0) {
			CommonConst.rightFilePathText.setText(CommonConst.FEMPTY);
			return false;
		}
		
		File file = new File(rightPath);
		if (!file.exists()) {
			CommonConst.rightFilePathText.setText(CommonConst.FEMPTY);
			return false;
		}
		
		CommonConst.rightFilePathText.setText(rightPath);
		return true;
	}
	
	private static void setLeftFileContentText(String str, boolean setScrollToTop) {
		if (str == null || str.length() == 0) {
			CommonConst.leftFileContent.setText(CommonConst.FEMPTY);
			return;
		}
		
		CommonConst.leftFileContent.setText(str);
		
		if (setScrollToTop) {
			CommonConst.leftFileContent.setScrollTop();
		}
	}
	
	private static void setRightFileContentText(String str, boolean setScrollToTop) {
		if (str == null || str.length() == 0) {
			CommonConst.rightFileContent.setText(CommonConst.FEMPTY);
			return;
		}
		
		CommonConst.rightFileContent.setText(str);
		
		if (setScrollToTop) {
			CommonConst.rightFileContent.setScrollTop();
		}
	}
	
	public static void loadDirByNode(BBTreeNode node, final boolean bRemoveIfSame) {
		final BBTreeNodeList treeNodeList = new BBTreeNodeList();	
		getNodeListFromDir(treeNodeList, node);
		
		final int treeNodeCount = treeNodeList.size();
		if (treeNodeCount < 1) {
			return;
		}
		
		Collections.reverse(treeNodeList);
		
		Thread thread = new Thread() {
			@Override
			public void run() {
				super.run();
				
				CommonConst.leftFilePathText.setText("");
				CommonConst.rightFilePathText.setText("");
				
				CommonConst.leftFileContent.setText("");
				CommonConst.rightFileContent.setText("");
				
				try {
					for (int i=0; i<treeNodeCount; i++) {
						loadFileByNode(treeNodeList.get(i), bRemoveIfSame, false);
						
						// 좌측 하단 프로그레스 레이블에 개수 표시
						int index = i + 1;
						// System.out.println(index + "/" + treeNodeCount);
						CommonConst.diffPointLabel.setText(index + "/" + treeNodeCount);
					}
				} finally {
					CommonConst.diffPointLabel.setText("");
				}
			}
		};
		thread.start();
	}
	
	private static void getNodeListFromDir(BBTreeNodeList treeNodeList, BBTreeNode node) {
		if (node == null) {
			return;
		}
		
		if (node.isDir()) {
			int childCount = node.getChildCount();
			if (childCount > 0) {
				for (int i=(childCount - 1); i>=0; i--) {
					BBTreeNode oneNode = (BBTreeNode) node.getChildAt(i);
					getNodeListFromDir(treeNodeList, oneNode);
				}
			} else {
				node.removeMe(true);
			}
			
		} else if (node.isFile()) {
			treeNodeList.add(node);
		}
	}
	
	/**
	 * 노드 속의 정보를 활용해서 파일 열기
	 * @param node
	 */
	public static boolean loadFileByNode(BBTreeNode node, boolean bRemoveIfSame, boolean showOnEditor) {
		
		refreshNodeInfo(node, bRemoveIfSame, showOnEditor);
		
		/**
		 * 좌측 파일 출력
		 */
		boolean leftFileExists = setLeftPathText(node);
		StringBuffer content1 = null;
		
		if (leftFileExists) {
			content1 = node.getFileContentString(true, null);
			// 내용만 비교하기 위해 캐리지리턴 무시
			if (content1 != null) {
				content1 = new StringBuffer().append(content1.toString().replace("\r", ""));
			}
			if (showOnEditor) {
				setLeftFileContentText(content1.toString(), true);
			}
		}
		
		/**
		 * 우측 파일 출력
		 */
		boolean rightFileExists = setRightPathText(node);
		StringBuffer content2 = null;
		
		if (rightFileExists) {
			content2 = node.getFileContentString(false, null);
			// 내용만 비교하기 위해 캐리지리턴 무시
			if (content2 != null) {
				content2 = new StringBuffer().append(content2.toString().replace("\r", ""));
			}
			if (showOnEditor) {
				setRightFileContentText(content2.toString(), true);
			}
		}
		
		if (!leftFileExists && !rightFileExists) {
			if (showOnEditor) {
				setLeftFileContentText("좌측 파일이 존재하지 않습니다.", true);
				setRightFileContentText("우측 파일이 존재하지 않습니다.", true);
			}
			
			if (bRemoveIfSame) {
				// 특정 노드를 삭제한다.
				node.removeMe(true);
			}
			return false;
			
		} else if (!leftFileExists) {
			if (showOnEditor) {
				setLeftFileContentText("좌측 파일이 존재하지 않습니다.", true);
			}
			return false;
			
		} else if (!rightFileExists) {
			if (showOnEditor) {
				setRightFileContentText("우측 파일이 존재하지 않습니다.", true);
			}
			return false;
		}
		
		String fileName = PathUtil.getFileNameWithExt(node.getLeftAbsoulutePath());
		if (fileName == null || fileName.length() == 0) {
			fileName = PathUtil.getFileNameWithExt(node.getRightAbsoulutePath());
		}
		
		// bCheckDiff == true일 때만 비교하자. 내용이 완전히 같을 경우 비교할 필요 없다.
		boolean bCheckDiff = shouldCheckDiff(node, content1, content2);
		
		if (showOnEditor) {
			// 색칠용
			leftDoc = CommonConst.leftFileContent.getStyledDocument();
			rightDoc = CommonConst.rightFileContent.getStyledDocument();
			
			strongStyle1 = CommonConst.leftFileContent.addStyle("1", null);
			strongStyle2 = CommonConst.rightFileContent.addStyle("2", null);
			
			whiteStyle = CommonConst.leftFileContent.addStyle("5", null);
			whiteStyle = CommonConst.rightFileContent.addStyle("6", null);
			
			StyleConstants.setBackground(whiteStyle, new Color(255, 255, 255));
			StyleConstants.setBackground(strongStyle1, new Color(248, 216, 136));
			StyleConstants.setBackground(strongStyle2, new Color(248, 216, 136));
		}
		
		/**
		 * 비교해서 색칠한다. (DIFF)
		 */
		diffForHighlight(bCheckDiff, content1, content2, node, fileName, bRemoveIfSame, showOnEditor);
		
		if (showOnEditor) {
			/**
			 * 디폴트로 최상단 보여주기
			 */
			// JTextPane onload (JTextPane resume)
			CommonConst.leftFileContent.getDocument().addDocumentListener(new DocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent e) {
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
					CommonConst.leftFileContent.setScrollTop();
				}
			});
			
			// JTextPane onload (JTextPane resume)
			CommonConst.rightFileContent.getDocument().addDocumentListener(new DocumentListener() {
				
				@Override
				public void removeUpdate(DocumentEvent e) {
				}
				
				@Override
				public void insertUpdate(DocumentEvent e) {
				}
				
				@Override
				public void changedUpdate(DocumentEvent e) {
					CommonConst.rightFileContent.setScrollTop();
				}
			});
		}
		
		return true;
	}
	
	/**
	 * 노드 속의 정보를 활용해서 파일 열기
	 * @param node
	 */
	public static void refreshNodeInfo(BBTreeNode node, boolean bRemoveIfSame, boolean showOnEditor) {
		
		/**
		 * 좌측 파일 출력
		 */
		boolean leftFileExists = checkLeftFileExists(node);
		
		/**
		 * 우측 파일 출력
		 */
		boolean rightFileExists = checkRightFileExists(node);
		
		/**
		 * 용량을 다시 계산한다.
		 */
		String oldTitle = node.getTitle();
		if (oldTitle.lastIndexOf("[") > -1) {
			oldTitle = oldTitle.substring(0, oldTitle.lastIndexOf("[")).trim();
		}
		
		if (leftFileExists && rightFileExists) {
			// 양쪽에 있는 파일은 용량비교해서 gap을 표시한다.
			String leftAbsolutePath = node.getLeftAbsoulutePath();
			String rightAbsolutePath = node.getRightAbsoulutePath();
			long volGap = TreeUtil.getVolumeGap(leftAbsolutePath, rightAbsolutePath);
			
			String newTitle = oldTitle + " " + "[" + volGap + "]";
			node.setTitle(newTitle);
			
		} else if (leftFileExists) {
			String newTitle = oldTitle + " " + TreeUtil.leftMark;
			node.setTitle(newTitle);
			
		} else if (rightFileExists) {
			String newTitle = oldTitle + " " + TreeUtil.rightMark;
			node.setTitle(newTitle);
			
		} else {
			
			int middleBracketIndex = oldTitle.lastIndexOf("}");
			if (middleBracketIndex > -1) {
				String newTitle = "{●}" + oldTitle.substring(middleBracketIndex + 1) + " " + TreeUtil.noneMark;
				// 사용자가 마우스 우클릭했을 경우 {◎} 마크를 앞에 붙여주는 기능이 있으므로, 이 점을 고려하여 처리한다.
				if (oldTitle.startsWith("{◎}")) {
					newTitle = "{◎}" + newTitle;
				}
				node.setTitle(newTitle);
			} else {
				String newTitle = "{●}" + oldTitle + " " + TreeUtil.noneMark;
				node.setTitle(newTitle);
			}
		}
	}
	
	/**
	 * 차이점을 검사(diff)해야 하는지 여부
	 * 
	 * @param content1
	 * @param content2
	 * @return
	 */
	private static boolean shouldCheckDiff(BBTreeNode node, StringBuffer content1, StringBuffer content2) {
		boolean bCheckDiff = true;
		
		String strContent1 = content1 != null ? content1.toString() : "";
		String strContent2 = content2 != null ? content2.toString() : "";
		
		if (strContent1.trim().equals(strContent2.trim())) {
			bCheckDiff = false;
		}
		
		// CVS/SVN 리비전 문자열 제외하고 비교하기 여부
		// 다른 부분은 모두 동일하고 CVS/SVN 리비전 정보만 불일치할 경우, 같은 내용으로 판단하도록 처리
		if (bCheckDiff) {
			if (CommonConst.bDiffExceptingRivisionString) {
				// (1) CVS 리비전 문자열을 제거한다.
				/*
				public static String getCVSRevision() {
			        return "$Revision: 1.6 $";
			    }
			    */
				String regBlank = "[\\s\\t\\r\\n]*";
				String regCVSRevision = "public static String getCVSRevision\\(\\)" + regBlank + "\\{" + regBlank + "return \"\\$Revision: [\\.0-9]* \\$\";" + regBlank + "}";
				strContent1 = strContent1.replaceAll(regCVSRevision, "");
				strContent2 = strContent2.replaceAll(regCVSRevision, "");
				
				// (2) SVN 리비전 문자열을 제거한다.
				// public static final String SVNFILEINFO = "$Id: FileName.java 2400 2019-05-03 06:52:35Z userid $";
				String regSVNRevision = "public static final String SVNFILEINFO" + regBlank + "=" + regBlank + "\"\\$Id: [a-zA-Z0-9\\.\\-:\\s]* \\$\";";
				strContent1 = strContent1.replaceAll(regSVNRevision, "");
				strContent2 = strContent2.replaceAll(regSVNRevision, "");
				
				// (3) 모든공백을 제거한다.
				strContent1 = strContent1.replaceAll("[\\s\\t\\r\\n]*", "");
				strContent2 = strContent2.replaceAll("[\\s\\t\\r\\n]*", "");
				
				if (strContent1.equals(strContent2)) {
					bCheckDiff = false;
				}
			}
		}
		
		// JAVA 파일의 경우 내용 파싱해서 주석제외하고 비교하기
		if (bCheckDiff) {
			String fileExtOnly = node.getLeftFileNameExtOnly();
			if (fileExtOnly == null || fileExtOnly.length() == 0) {
				fileExtOnly = node.getRightFileNameExtOnly();
			}
			
			if (fileExtOnly != null && fileExtOnly.equalsIgnoreCase("java")) {
				try {
					FileContent leftFileContent = FileReadUtil.getInstance().readFile(node.getLeftAbsoulutePath());
					FileContent rightFileContent = FileReadUtil.getInstance().readFile(node.getRightAbsoulutePath());
					
					ParsedContent leftParsedContent = leftFileContent.parse();
					ParsedContent rightParsedContent = rightFileContent.parse();
					
					StringBuffer leftTypeBuffer = leftParsedContent.getTypeBuffer();
					StringBuffer leftContentBuffer = leftParsedContent.getContentBuffer();
					if (leftTypeBuffer.length() == leftContentBuffer.length()) {
						for (int i=leftContentBuffer.length() - 1; i>=0; i--) {
							// 뒤에서부터 주석에 해당하는 문자열 제거
							if (leftTypeBuffer.substring(i, i+1).equalsIgnoreCase("c")) {
								leftContentBuffer.deleteCharAt(i);
							}
						}
					}
				
					StringBuffer rightTypeBuffer = rightParsedContent.getTypeBuffer();
					StringBuffer rightContentBuffer = rightParsedContent.getContentBuffer();
					if (rightTypeBuffer.length() == rightContentBuffer.length()) {
						for (int i=rightContentBuffer.length() - 1; i>=0; i--) {
							// 뒤에서부터 주석에 해당하는 문자열 제거
							if (rightTypeBuffer.substring(i, i+1).equalsIgnoreCase("c")) {
								rightContentBuffer.deleteCharAt(i);
							}
						}	
					}
					
					String leftContent = leftContentBuffer.toString().replace(" ", "").replace("\r", "").replace("\n", "").trim();
					String rightContent = rightContentBuffer.toString().replace(" ", "").replace("\r", "").replace("\n", "").trim();
					
					if (leftContent.equals(rightContent)) {
						bCheckDiff = false;
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return bCheckDiff;
	}
	
	
	private static void diffForHighlight(boolean bCheckDiff, StringBuffer content1, StringBuffer content2, BBTreeNode node, String fileName, boolean bRemoveIfSame, boolean showOnEditor) {
		
		// String fileExt = FileNameUtil.getExtensionFromPath(fileName);
		// boolean bClassFile = fileExt.equalsIgnoreCase("class");
		
		boolean isSimpleDiff = false;
		int diffPoint = 0;
		
		// bCheckDiff == true일 때만 비교하자. 내용이 완전히 같을 경우 비교할 필요 없다.
		if (bCheckDiff) {
			
			if (!showOnEditor) {
				isSimpleDiff = true;
				
			} else {
				// 에디터에 표시해야 하는 경우에만 diff 진행하기
				/**
				 * col 위치 계산해서 저장해둔다.
				 */
				colList1 = FileContentUtil.createColList(content1);
				if (colList1 == null) {
					colList1 = new ColList();
				}
				
				colList2 = FileContentUtil.createColList(content2);
				if (colList2 == null) {
					colList2 = new ColList();
				}
				
				leftDoc.setCharacterAttributes(0, leftDoc.getLength(), whiteStyle, true);
				rightDoc.setCharacterAttributes(0, rightDoc.getLength(), whiteStyle, true);
				
				CommonConst.diffPointList1 = new ArrayList<Integer>();
				CommonConst.diffPointList2 = new ArrayList<Integer>();
				CommonConst.currentDiffPointIndex = -1;
				
				String text1 = content1.toString();
		        String text2 = content2.toString();

		        // Perform diff
		        Patch<String> patch = DiffUtils.diff(Arrays.asList(text1.split("\n")), Arrays.asList(text2.split("\n")));

		        // Display diff result
		        StringBuilder diffOutput = new StringBuilder();
		        for (AbstractDelta<String> delta : patch.getDeltas()) {
		            diffOutput.append(delta).append("\n");
		            
	            	// 변경이 발생한 원본 위치
	            	int beginRow1 = delta.getSource().getPosition();
	            	int endRow1 = beginRow1;
		            List<String> lineList1 = delta.getSource().getLines();
		            if (lineList1 != null && lineList1.size() > 0) {
		            	endRow1 = beginRow1 + lineList1.size() - 1;
		            }
		            
		            for (int k=beginRow1; k<=endRow1; k++) {
		            	paintLeftDocStrong(k);
		            }
		            
	            	// 변경이 발생한 수정된 위치
	            	int beginRow2 = delta.getTarget().getPosition();
	            	int endRow2 = beginRow2;
		            List<String> lineList2 = delta.getTarget().getLines();
		            if (lineList2 != null && lineList2.size() > 0) {
		            	endRow2 = beginRow2 + lineList2.size() - 1;
		            }
		            
		            for (int k=beginRow2; k<=endRow2; k++) {
		            	paintRightDocStrong(k);
		            }
		            
		            diffPoint++;
		            
		            CommonConst.diffPointList1.add(beginRow1);
		            CommonConst.diffPointList2.add(beginRow2);
		        }
			}
		}
		
		if (showOnEditor) {
			CommonConst.diffPointLabel.setText("Diff Point : " + diffPoint);
		}
		
		if (isSimpleDiff || diffPoint > 0) {
			String mark = "";
			if (isSimpleDiff) {
				mark = "";
			} else {
				mark = "{" + diffPoint + "}";
			}
			
			// 차이점이 존재할 경우 {숫자(디프개수)} 마크를 앞에 붙여준다.
			int middleBracketIndex = node.getTitle().lastIndexOf("}");
			if (middleBracketIndex > -1) {
				String newTitle = mark + node.getTitle().substring(middleBracketIndex + 1);
				// 사용자가 마우스 우클릭했을 경우 {◎} 마크를 앞에 붙여주는 기능이 있으므로, 이 점을 고려하여 처리한다.
				if (node.getTitle().startsWith("{◎}")) {
					newTitle = "{◎}" + newTitle;
				}
				node.setTitle(newTitle);
			} else {
				String newTitle = mark + node.getTitle();
				node.setTitle(newTitle);
			}
			
		} else {
			if (fileName != null && fileName.length() > 0) {
				if (showOnEditor) {
					CommonConst.leftFileContent.setText("내용 동일함 : " + fileName);
					CommonConst.rightFileContent.setText("내용 동일함 : " + fileName);
				}
				
			} else {
				if (showOnEditor) {
					CommonConst.leftFileContent.setText("내용 동일함");
					CommonConst.rightFileContent.setText("내용 동일함");
				}
			}
			
			if (bRemoveIfSame) {
				// 특정 노드를 삭제한다.
				node.removeMe(true);
				
			} else {
				// 차이점 없이 내용 동일할 경우 {●} 마크를 앞에 붙여준다.
				if (node.getTitle() != null) {
					int middleBracketIndex = node.getTitle().lastIndexOf("}");
					if (middleBracketIndex > -1) {
						String newTitle = "{●}" + node.getTitle().substring(middleBracketIndex + 1);
						// 사용자가 마우스 우클릭했을 경우 {◎} 마크를 앞에 붙여주는 기능이 있으므로, 이 점을 고려하여 처리한다.
						if (node.getTitle().startsWith("{◎}")) {
							newTitle = "{◎}" + newTitle;
						}
						node.setTitle(newTitle);
					} else {
						String newTitle = "{●}" + node.getTitle();
						node.setTitle(newTitle);
					}
				}
			}
		}
	}
	
	
	public static boolean loadLeftFile(BBTreeNode node) {
		/**
		 * 좌측 파일 출력
		 */
		boolean leftFileExists = setLeftPathText(node);
		
		if (!leftFileExists) {
			 setLeftFileContentText("좌측 파일이 존재하지 않습니다.", true);
			return false;
		}
		
		StringBuffer con = node.getFileContentString(true, null);
		setLeftFileContentText(con.toString(), true);
		return true;
	}
	
	
	public static boolean loadRightFile(BBTreeNode node) {
		/**
		 * 우측 파일 출력
		 */
		boolean rightFileExists = setRightPathText(node);
		
		if (!rightFileExists) {
			setRightFileContentText("우측 파일이 존재하지 않습니다.", true);
			return false;
		}
		
		StringBuffer con = node.getFileContentString(false, null);
		setRightFileContentText(con.toString(), true);
		return true;
	}
	
	
	private static boolean equalsForClass(String str1, String str2, boolean bClassFile) {
		if (str1 == null) {
			str1 = "";
		}
		
		if (str2 == null) {
			str2 = "";
		}
		
		if (str1.trim().equals(str2.trim())) {
			return true;
		}
		
		if (!bClassFile || !CommonConst.bDiffConsideringBreakage) {
			return false;
		}
		
		// 클래스 파일이고, CommonConst.bDiffConsideringBreakage == true(디컴파일시 손상을 고려한 비교하기)일 경우만 아래 로직을 탄다.
		
		// 디컴파일은 동일한 파일도 어떻게 빌드되었느냐에 따라 다르게 디컴파일되기 때문에, 이를 고려하여 비교해본다.
		
		// MISSING_BLOCK_LABEL_ 고려
		if (str1.indexOf("MISSING_BLOCK_LABEL_") > -1 && str2.indexOf("MISSING_BLOCK_LABEL_") > -1) {
			String revisedStr1 = str1.replaceAll("MISSING_BLOCK_LABEL\\_[0-9]*", "");
			String revisedStr2 = str2.replaceAll("MISSING_BLOCK_LABEL\\_[0-9]*", "");
			if (revisedStr1.equals(revisedStr2)) {
				return true;
			}
		}
		
		// JVM INSTR new #96  <Class StringBuilder>; 와 JVM INSTR new #95  <Class StringBuilder>; 같게 인식하도록 처리
		if (str1.indexOf("JVM INSTR new #") > -1 && str2.indexOf("JVM INSTR new #") > -1) {
			String revisedStr1 = str1.replaceAll("#[0-9]*", "");
			String revisedStr2 = str2.replaceAll("#[0-9]*", "");
			if (revisedStr1.equals(revisedStr2)) {
				return true;
			}
		}
		
		if (str1.replace("org.w3c.dom.Document", "Document").equals(str2.replace("org.w3c.dom.Document", "Document"))) {
			return true;
		}
		
		if (str1.replace("driver.OracleResultSet", "OracleResultSet").equals(str2.replace("driver.OracleResultSet", "OracleResultSet"))) {
			return true;
		}
		
		if (str1.replace("super.", "").equals(str2.replace("super.", ""))) {
			return true;
		}
		
		// new StringBuilder 와 new StringBuffer 는 편의를 위해 적당히 비교한다.
		// 예를 들어 좌측 클래스는 StringBuilder, 우측 클래스는 단순 String 으로 컴파일 되었을 경우
		// 사실상 같은 내용인데 diff 로 체크되면 사람이 눈으로 일일히 대조해봐야 한다.
		// 문자열을 적당히 잘라내어 유사하다고 판단되면 동일한 문자열로 본다.
		if (str1.indexOf("new StringBuilder") > -1 ||
			str2.indexOf("new StringBuilder") > -1 ||
			str1.indexOf("new StringBuffer") > -1 ||
			str2.indexOf("new StringBuffer") > -1) {
			
			String tmpStr1 = str1;
			String tmpStr2 = str2;
			
			tmpStr1 = tmpStr1.replace(".append(\"\")", "");
			tmpStr2 = tmpStr2.replace(".append(\"\")", "");
			
			tmpStr1 = tmpStr1.replace("new StringBuilder", "");
			tmpStr2 = tmpStr2.replace("new StringBuilder", "");
			
			tmpStr1 = tmpStr1.replace("new StringBuffer", "");
			tmpStr2 = tmpStr2.replace("new StringBuffer", "");
			
			tmpStr1 = tmpStr1.replace(".append", "");
			tmpStr2 = tmpStr2.replace(".append", "");
			
			tmpStr1 = tmpStr1.replace("String.valueOf", "");
			tmpStr2 = tmpStr2.replace("String.valueOf", "");
			
			tmpStr1 = tmpStr1.replace(".toString()", "");
			tmpStr2 = tmpStr2.replace(".toString()", "");
			
			tmpStr1 = tmpStr1.replace("(", "").replace(")", "");
			tmpStr2 = tmpStr2.replace("(", "").replace(")", "");
			
			tmpStr1 = tmpStr1.replace(" + ", "");
			tmpStr2 = tmpStr2.replace(" + ", "");
			
			if (tmpStr1.equals(tmpStr2)) {
				return true;
			}
		}
		
		// " = (타입명[])null;" 패턴을 " = null;" 과 같다고 판단한다.
		// ex 1) saveFileName = (String[])null; 과 saveFileName = null; 을 같게 판단한다.
		// ex 2) int rt[] = (int[])null; 과 int rt[] = null; 을 같게 판단한다.
		// ex 3) String groupNameList[] = (String[])null; 과 String groupNameList[] = null; 을 같게 판단한다.
		// ex 4) File subFiles[] = (File[])null; 과 File subFiles[] = null; 을 같게 판단한다.
		// ex 5) String ret[][] = (String[][])null; 을 같게 판단한다.
		if (str1.indexOf(" = ") > -1 && str2.indexOf(" = ") > -1) {
			
			boolean b1 = str1.matches(".* = \\(.*\\[\\]\\)null;");
			boolean b2 = str2.matches(".* = \\(.*\\[\\]\\)null;");
			
			// 둘 중 하나만 패턴에 맞아 떨어져야 한다. 둘 다 패턴이 동일하면 애초에 문자열이 같았어야 했다.
			if (!(b1 && b2) && (b1 || b2)) {
				String targetString = "";
				String otherString = "";
				if (b1) {
					targetString = str1;
					otherString = str2;
				} else if (b2) {
					targetString = str2;
					otherString = str1;
				}
				
				String beginMark = " = (";
				int idx1 = targetString.indexOf(beginMark);
				int idx2 = targetString.indexOf(")", idx1 + 1);
				if (idx1 > -1 && idx2 > -1) {
					// " = (타입명[])null;" 패턴과 일치하는 문자열을 " = null;" 로 치환해서 비교해본다.
					String variableTypeArr = targetString.substring(idx1 + beginMark.length(), idx2);
					if (variableTypeArr.endsWith("[]")) {
						
						int arrCount = 0;
						String arrString = "";
						while (variableTypeArr.endsWith(arrString + "[]")) {
							arrString = arrString + "[]";
							arrCount++;
						}
						
						// variableType 은 int, String, File 등이 될 수 있다.
						String variableType = variableTypeArr.substring(0, variableTypeArr.length() - arrString.length());
						if (variableType.matches("[a-zA-Z]*")) {
							String tempStr1 = targetString.replace(" = (" + variableType + "" + arrString + ")null;", " = null;");
							
							if (tempStr1.equals(otherString)) {
								return true;
							}
						}
					}
				}
			}
		}
		
		return false;
	}
}