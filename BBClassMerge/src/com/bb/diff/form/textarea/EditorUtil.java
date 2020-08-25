package com.bb.diff.form.textarea;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.bb.classmerge.util.FileNameUtil;
import com.bb.diff.common.CommonConst;
import com.bb.diff.form.tree.BBTreeNode;
import com.bb.diff.form.tree.TreeUtil;
import com.bb.diff.map.FileContentUtil;
import com.bb.diff.path.PathUtil;
import com.bb.diff.prototype.Col;
import com.bb.diff.prototype.ColList;

public class EditorUtil {
	
	private static StyledDocument leftDoc = null;
	private static StyledDocument rightDoc = null;
	
	private static ColList colList1 = null;
	private static ColList colList2 = null;
	
	private static Style whiteStyle = null;
	private static Style strongStyle1 = null;
	private static Style strongStyle2 = null;
	private static Style normalStyle1 = null;
	private static Style normalStyle2 = null;
	
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
	
	public static void paintLeftDocNormal(int lineNum) {
		Col oneCol = colList1.get(lineNum);
		leftDoc.setCharacterAttributes(oneCol.getBeginCol(), oneCol.getGapCol(), normalStyle1, true);
	}
	
	public static void paintRightDocNormal(int lineNum) {
		Col oneCol = colList2.get(lineNum);
		rightDoc.setCharacterAttributes(oneCol.getBeginCol(), oneCol.getGapCol(), normalStyle2, true);
	}
	
	private static boolean setLeftPathText(String str) {
		if (str == null) {
			CommonConst.leftFilePathText.setText(CommonConst.FNULL);
			return false;
		}
		
		if (str.length() == 0) {
			CommonConst.leftFilePathText.setText(CommonConst.FEMPTY);
			return false;
		}
		
		CommonConst.leftFilePathText.setText(str);
		return true;
	}
	
	private static boolean setRightPathText(String str) {
		if (str == null) {
			CommonConst.rightFilePathText.setText(CommonConst.FNULL);
			return false;
		}
		
		if (str.length() == 0) {
			CommonConst.rightFilePathText.setText(CommonConst.FEMPTY);
			return false;
		}
		
		CommonConst.rightFilePathText.setText(str);
		return true;
	}
	
	private static void setLeftFileContentText(String str, boolean setScrollToTop) {
		if (str == null) {
			CommonConst.leftFileContent.setText(CommonConst.FNULL);
			return;
		}
		
		if (str.length() == 0) {
			CommonConst.leftFileContent.setText(CommonConst.FEMPTY);
			return;
		}
		
		CommonConst.leftFileContent.setText(str);
		
		if (setScrollToTop) {
			CommonConst.leftFileContent.setScrollTop();
		}
	}
	
	private static void setRightFileContentText(String str, boolean setScrollToTop) {
		if (str == null) {
			CommonConst.rightFileContent.setText(CommonConst.FNULL);
			return;
		}
		
		if (str.length() == 0) {
			CommonConst.rightFileContent.setText(CommonConst.FEMPTY);
			return;
		}
		
		CommonConst.rightFileContent.setText(str);
		
		if (setScrollToTop) {
			CommonConst.rightFileContent.setScrollTop();
		}
	}
	
	
	public static void loadDirByNode(BBTreeNode node, boolean bRemoveIfSame, boolean bLoop) {
		if (node == null) {
			return;
		}
		
		if (node.isDir()) {
			int childCount = node.getChildCount();
			int lastIndex = childCount - 1;
			
			if (childCount == 0 && bRemoveIfSame) {
				node.removeMe();
			}
			
			for (int i=lastIndex; i>=0; i--) {
				BBTreeNode oneNode = (BBTreeNode) node.getChildAt(i);
				if (oneNode.isFile()) {
					System.out.println("loadDirByNode : " + i + ": " + oneNode);
					EditorUtil.loadFileByNode(oneNode, bRemoveIfSame);
				} else if (oneNode.isDir()) {
					if (bLoop) {
						loadDirByNode(oneNode, bRemoveIfSame, true);
					}
				}
			}
		} else if (node.isFile()) {
			System.out.println("loadDirByNode : " + node);
			EditorUtil.loadFileByNode(node, bRemoveIfSame);
		}
	}
	
	/**
	 * 노드 속의 정보를 활용해서 파일 열기
	 * @param node
	 */
	public static boolean loadFileByNode(BBTreeNode node, boolean bRemoveIfSame) {
		
		/**
		 * 좌측 파일 출력
		 */
		boolean leftFileExists = setLeftPathText(node.getLeftAbsoulutePath());
		StringBuffer content1 = null;
		
		if (leftFileExists) {
			content1 = node.getFileContentString(true, null);
			setLeftFileContentText(content1.toString(), true);
		}
		
		/**
		 * 우측 파일 출력
		 */
		boolean rightFileExists = setRightPathText(node.getRightAbsoulutePath());
		StringBuffer content2 = null;
		
		if (rightFileExists) {
			content2 = node.getFileContentString(false, null);
			setRightFileContentText(content2.toString(), true);
		}
		
		
		if (!leftFileExists) {
			setLeftFileContentText("좌측 파일 내용이 없습니다.", true);
			return false;
			
		} else if (!rightFileExists) {
			setRightFileContentText("우측 파일 내용이 없습니다.", true);
			return false;
		}
		
		
		String fileName = PathUtil.getFileNameWithExt(node.getLeftAbsoulutePath());
		if (fileName == null || fileName.length() == 0) {
			fileName = PathUtil.getFileNameWithExt(node.getRightAbsoulutePath());
		}
		
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
		}
		
		/**
		 * col 위치 계산해서 저장해둔다.
		 */
		
		colList1 = FileContentUtil.createColList(content1);
		colList2 = FileContentUtil.createColList(content2);
		
		if (colList1 == null || colList1.size() == 0) {
			return false;
		}
		
		if (colList2 == null || colList2.size() == 0) {
			return false;
		}
		
		// 색칠용
		leftDoc = CommonConst.leftFileContent.getStyledDocument();
		rightDoc = CommonConst.rightFileContent.getStyledDocument();
		
		strongStyle1 = CommonConst.leftFileContent.addStyle("1", null);
		strongStyle2 = CommonConst.rightFileContent.addStyle("2", null);
		
		normalStyle1 = CommonConst.leftFileContent.addStyle("3", null);
		normalStyle2 = CommonConst.rightFileContent.addStyle("4", null);
		
		whiteStyle = CommonConst.leftFileContent.addStyle("5", null);
		whiteStyle = CommonConst.rightFileContent.addStyle("6", null);
		
		StyleConstants.setBackground(whiteStyle, new Color(255, 255, 255));
		StyleConstants.setBackground(strongStyle1, new Color(200, 200, 255));
		StyleConstants.setBackground(strongStyle2, new Color(200, 200, 255));
		StyleConstants.setBackground(normalStyle1, new Color(200, 200, 200));
		StyleConstants.setBackground(normalStyle2, new Color(200, 200, 200));

		// bCheckDiff == true일 때만 비교하자. 내용이 완전히 같을 경우 비교할 필요 없다.
		boolean bCheckDiff = true;
		if (content1 != null && content2 != null) {
			if (content1.toString().trim().equals(content2.toString().trim())) {
				bCheckDiff = false;
			}
		}

		/**
		 * 비교해서 색칠한다. (DIFF)
		 */
		diffForHighlight(bCheckDiff, node, fileName, bRemoveIfSame);
		
		/**
		 * 디폴트로 최상단 보여주기
		 */
		CommonConst.leftFileContent.setScrollTop();
		CommonConst.rightFileContent.setScrollTop();

		return true;
	}
	
	
	public static void diffForHighlight(boolean bCheckDiff, BBTreeNode node, String fileName, boolean bRemoveIfSame) {
		
		String fileExt = FileNameUtil.getExtensionFromPath(fileName);
		boolean bClassFile = fileExt.equalsIgnoreCase("class");
		
		int diffPoint = 0;
		
		// bCheckDiff == true일 때만 비교하자. 내용이 완전히 같을 경우 비교할 필요 없다.
		if (bCheckDiff) {
			CommonConst.diffPointList = new ArrayList<Integer>();
			CommonConst.currentDiffPointIndex = -1;
			
			int rowCount1 = colList1.size();
			int rowCount2 = colList2.size();
			
			int lastRowNum1 = rowCount1 - 1;
			int lastRowNum2 = rowCount2 - 1;
			
			int bigRowCount = rowCount1;
			if (rowCount2 > bigRowCount) {
				rowCount2 = bigRowCount;
			}
			
			int rowNum1 = 0;
			int rowNum2 = 0;
			String lineText1 = null;
			String lineText2 = null;
			boolean bEmptyLine1 = false;
			boolean bEmptyLine2 = false;
			
			for (int i=0; i<bigRowCount; i++) {
				
				if ((rowNum1 > lastRowNum1) && (rowNum2 > lastRowNum2)) {
					break;
				}
				
				lineText1 = (rowNum1 > lastRowNum1) ? "" : colList1.get(rowNum1).getText();
				lineText2 = (rowNum2 > lastRowNum2) ? "" : colList2.get(rowNum2).getText();
				
				bEmptyLine1 = (lineText1 == null || lineText1.length() == 0);
				bEmptyLine2 = (lineText2 == null || lineText2.length() == 0);
				
				if (bEmptyLine1 && bEmptyLine2) {
					if (rowNum1 < rowCount1) {
						paintLeftDocWhite(rowNum1);
					}
					if (rowNum2 < rowCount2) {
						paintRightDocWhite(rowNum2);
					}
					rowNum1++;
					rowNum2++;
					continue;
					
				} else if (bEmptyLine1 || bEmptyLine2 || !(equalsForClass(lineText1, lineText2, bClassFile))) {
					diffPoint++;
					CommonConst.diffPointList.add(rowNum1);
					
					// 다른거 나오면 일단 칠한다.
					if (rowNum1 < rowCount1) {
						paintLeftDocStrong(rowNum1);
					}
					if (rowNum2 < rowCount2) {
						paintRightDocStrong(rowNum2);
					}
	
					//{{{{{
					//{{{{{
					//{{{{{
					//{{{{{
					//{{{{{
					
					int tempNum1 = rowNum1;
					int tempNum2 = rowNum2;
					HashMap<String, Integer> leftMap = new HashMap<String, Integer>();
					HashMap<String, Integer> rightMap = new HashMap<String, Integer>();
					
					int newNum1 = 0;
					int newNum2 = 0;
					boolean bFound = false;
					
					while (true) {
						if ((tempNum1 > lastRowNum1) && (tempNum2 > lastRowNum2)) {
							break;
						}
						
						lineText1 = (tempNum1 > lastRowNum1) ? "" : colList1.get(tempNum1).getText();
						lineText2 = (tempNum2 > lastRowNum2) ? "" : colList2.get(tempNum2).getText();
						
						if (lineText1 != null && lineText1.length() > 0) {
							if (rightMap.get(lineText1) != null) {
								newNum1 = tempNum1;
								newNum2 = rightMap.get(lineText1);
								
								paintLeftDocWhite(newNum1);
								paintRightDocWhite(newNum2);
								
								bFound = true;
								break;
								
							} else {
								leftMap.put(lineText1, tempNum1);
							}
						}
						
						if (lineText2 != null && lineText2.length() > 0) {
							if (leftMap.get(lineText2) != null) {
								newNum1 = leftMap.get(lineText2);
								newNum2 = tempNum2;
								
								paintLeftDocWhite(newNum1);
								paintRightDocWhite(newNum2);
								
								bFound = true;
								break;
								
							} else {
								rightMap.put(lineText2, tempNum2);
							}
						}
						
						tempNum1++;
						tempNum2++;
					}
					
					if (bFound) {
						// 찾았다면 탐색을 계속한다.
						
						// 1. 찾았다면 그 직전 라인까지 색칠해준다.
						for (int rr=rowNum1+1; rr<newNum1; rr++) {
							paintLeftDocNormal(rr);
						}
						
						for (int rr=rowNum2+1; rr<newNum2; rr++) {
							paintRightDocNormal(rr);
						}
						
						// 2. 탐색을 계속한다.
						rowNum1 = newNum1;
						rowNum2 = newNum2;
						continue;
						
					} else {
						// 찾지 못했다면 탐색을 그만둔다.
						
						// 1. 전체색칠
						for (int rr=rowNum1+1; rr<=lastRowNum1; rr++) {
							paintLeftDocNormal(rr);
						}
						
						for (int rr=rowNum2+1; rr<=lastRowNum2; rr++) {
							paintRightDocNormal(rr);
						}
						break;
					}
					
					//}}}}}
					//}}}}}
					//}}}}}
					//}}}}}
					//}}}}}				
					
				} else if (equalsForClass(lineText1, lineText2, bClassFile)) {
					if (rowNum1 < rowCount1) {
						paintLeftDocWhite(rowNum1);
					}
					if (rowNum2 < rowCount2) {
						paintRightDocWhite(rowNum2);
					}
					rowNum1++;
					rowNum2++;
					continue;
				}
			}
		}
		
		CommonConst.diffPointLabel.setText("Diff Point : " + diffPoint);
		
		if (diffPoint == 0) {
			if (fileName != null && fileName.length() > 0) {
				CommonConst.leftFileContent.setText("내용 동일함 : " + fileName);
				CommonConst.rightFileContent.setText("내용 동일함 : " + fileName);
				
			} else {
				CommonConst.leftFileContent.setText("내용 동일함");
				CommonConst.rightFileContent.setText("내용 동일함");
			}
			
			if (bRemoveIfSame) {
				// 부모 노드가 비었는지 미리 검사한다.
				boolean bParentIsEmpty = false;
				BBTreeNode parentNode = null;
				if (node.getParent() != null) {
					parentNode = (BBTreeNode) node.getParent();
					
					if (parentNode.getChildCount() < 2) {
						// 1개 일경우도 비었다고 판단한다. 왜? 곧 자식을 지울 예정이기 때문에.
						bParentIsEmpty = true;
					}
				}
				
				// 특정 노드를 삭제한다.
				node.removeMe();
				
				if (bParentIsEmpty) {
					// 부모 노드가 비었을 경우, 부모 노드를 삭제한다.
					parentNode.removeMe();
				}
				
				
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
			
		} else if (diffPoint > 0) {
			// 차이점이 존재할 경우 {숫자(디프개수)} 마크를 앞에 붙여준다.
			int middleBracketIndex = node.getTitle().lastIndexOf("}");
			if (middleBracketIndex > -1) {
				String newTitle = "{" + diffPoint + "}" + node.getTitle().substring(middleBracketIndex + 1);
				// 사용자가 마우스 우클릭했을 경우 {◎} 마크를 앞에 붙여주는 기능이 있으므로, 이 점을 고려하여 처리한다.
				if (node.getTitle().startsWith("{◎}")) {
					newTitle = "{◎}" + newTitle;
				}
				node.setTitle(newTitle);
			} else {
				String newTitle = "{" + diffPoint + "}" + node.getTitle();
				node.setTitle(newTitle);
			}
		}
	}
	
	
	public static boolean loadLeftFile(BBTreeNode node) {
		/**
		 * 좌측 파일 출력
		 */
		boolean leftFileExists = setLeftPathText(node.getLeftAbsoulutePath());
		
		if (!leftFileExists) {
			 setLeftFileContentText("좌측 파일 내용이 없습니다.", true);
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
		boolean rightFileExists = setRightPathText(node.getRightAbsoulutePath());
		
		if (!rightFileExists) {
			setRightFileContentText("우측 파일 내용이 없습니다.", true);
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
		
		if (str1.equals(str2)) {
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
		
//		if (str1.indexOf("\\uFFFD") > -1 && str2.indexOf("\\uFFFD") > -1) {
//			return true;
//		}
		
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