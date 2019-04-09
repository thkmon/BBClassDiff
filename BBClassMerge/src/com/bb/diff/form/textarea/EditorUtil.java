package com.bb.diff.form.textarea;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.bb.diff.common.DiffConst;
import com.bb.diff.form.tree.BBTreeNode;
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
			DiffConst.leftFilePathText.setText(DiffConst.FNULL);
			return false;
		}
		
		if (str.length() == 0) {
			DiffConst.leftFilePathText.setText(DiffConst.FEMPTY);
			return false;
		}
		
		DiffConst.leftFilePathText.setText(str);
		return true;
	}
	
	private static boolean setRightPathText(String str) {
		if (str == null) {
			DiffConst.rightFilePathText.setText(DiffConst.FNULL);
			return false;
		}
		
		if (str.length() == 0) {
			DiffConst.rightFilePathText.setText(DiffConst.FEMPTY);
			return false;
		}
		
		DiffConst.rightFilePathText.setText(str);
		return true;
	}
	
	private static void setLeftFileContentText(String str, boolean setScrollToTop) {
		if (str == null) {
			DiffConst.leftFileContent.setText(DiffConst.FNULL);
			return;
		}
		
		if (str.length() == 0) {
			DiffConst.leftFileContent.setText(DiffConst.FEMPTY);
			return;
		}
		
		DiffConst.leftFileContent.setText(str);
		
		if (setScrollToTop) {
			DiffConst.leftFileContent.setScrollTop();
		}
	}
	
	private static void setRightFileContentText(String str, boolean setScrollToTop) {
		if (str == null) {
			DiffConst.rightFileContent.setText(DiffConst.FNULL);
			return;
		}
		
		if (str.length() == 0) {
			DiffConst.rightFileContent.setText(DiffConst.FEMPTY);
			return;
		}
		
		DiffConst.rightFileContent.setText(str);
		
		if (setScrollToTop) {
			DiffConst.rightFileContent.setScrollTop();
		}
	}
	
	
	/**
	 * 노드 속의 정보를 활용해서 파일 열기
	 * @param node
	 */
	public static boolean loadFileByNode(BBTreeNode node) {
		
		/**
		 * 좌측 파일 출력
		 */
		boolean leftFileExists = setLeftPathText(node.getLeftAbsoulutePath());
		
		if (!leftFileExists) {
			 setLeftFileContentText("좌측 파일 내용이 없습니다.", true);
			return false;
		}
		
		StringBuffer content1 = node.getFileContentString(true, null);
		setLeftFileContentText(content1.toString(), true);
		
		
		/**
		 * 우측 파일 출력
		 */
		boolean rightFileExists = setRightPathText(node.getRightAbsoulutePath());
		
		if (!rightFileExists) {
			setRightFileContentText("우측 파일 내용이 없습니다.", true);
			return false;
		}
		
		StringBuffer content2 = node.getFileContentString(false, null);
		setRightFileContentText(content2.toString(), true);
		
		
		String fileName = PathUtil.getFileName(node.getLeftAbsoulutePath());
		if (fileName == null || fileName.length() == 0) {
			fileName = PathUtil.getFileName(node.getRightAbsoulutePath());
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
		leftDoc = DiffConst.leftFileContent.getStyledDocument();
		rightDoc = DiffConst.rightFileContent.getStyledDocument();
		
		strongStyle1 = DiffConst.leftFileContent.addStyle("1", null);
		strongStyle2 = DiffConst.rightFileContent.addStyle("2", null);
		
		normalStyle1 = DiffConst.leftFileContent.addStyle("3", null);
		normalStyle2 = DiffConst.rightFileContent.addStyle("4", null);
		
		whiteStyle = DiffConst.leftFileContent.addStyle("5", null);
		whiteStyle = DiffConst.rightFileContent.addStyle("6", null);
		
		StyleConstants.setBackground(whiteStyle, new Color(255, 255, 255));
		StyleConstants.setBackground(strongStyle1, new Color(200, 200, 255));
		StyleConstants.setBackground(strongStyle2, new Color(200, 200, 255));
		StyleConstants.setBackground(normalStyle1, new Color(200, 200, 200));
		StyleConstants.setBackground(normalStyle2, new Color(200, 200, 200));
		
		/**
		 * 비교해서 색칠한다. (DIFF)
		 */
		diffForHighlight(fileName);
		
		/**
		 * 디폴트로 최상단 보여주기
		 */
		DiffConst.leftFileContent.setScrollTop();
		DiffConst.rightFileContent.setScrollTop();

		return true;
	}
	
	
	public static void diffForHighlight(String fileName) {
		int diffPoint = 0;
		DiffConst.diffPointList = new ArrayList<Integer>();
		DiffConst.currentDiffPointIndex = -1;
		
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
				
			} else if (bEmptyLine1 || bEmptyLine2 || !lineText1.equals(lineText2)) {
				diffPoint++;
				DiffConst.diffPointList.add(rowNum1);
				
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
				
			} else if (lineText1.equals(lineText2)) {
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
		
		
		DiffConst.diffPointLabel.setText("Diff Point : " + diffPoint);
		
		if (diffPoint == 0) {
			if (fileName != null && fileName.length() > 0) {
				DiffConst.leftFileContent.setText("내용 동일함 : " + fileName);
				DiffConst.rightFileContent.setText("내용 동일함 : " + fileName);
				
			} else {
				DiffConst.leftFileContent.setText("내용 동일함");
				DiffConst.rightFileContent.setText("내용 동일함");
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
}