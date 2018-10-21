package com.bb.diff.form.textarea;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.bb.diff.common.DiffConst;
import com.bb.diff.form.tree.BBTreeNode;
import com.bb.diff.map.FileContentUtil;
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
//		loadLeftFile(node);
//		loadRightFile(node);
		
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
		
		
//		boolean testResult = FileUtil.checkIsSameFileContent(content1, content2, null, null);
//		System.out.println("testResult : " + testResult);
		
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
		diffForHighlight();
		
		/**
		 * 디폴트로 최상단 보여주기
		 */
		DiffConst.leftFileContent.setScrollTop();
		DiffConst.rightFileContent.setScrollTop();

		return true;
	}
	
	
	/**
	 * 비교해서 색칠한다. (DIFF)
	 * 
	 */
//	public static void diffForHighlight() {
//		
//		int lastIndex1 = colList1.size() - 1;
//		int lastIndex2 = colList2.size() - 1;
//		
//		int lineCount1 = colList1.size();
//		int lineCount2 = colList2.size();
//		
//		int limitIndex = lastIndex1;
//		if (limitIndex < lastIndex2) {
//			limitIndex = lastIndex2;
//		}
//		
//		int curRow1 = 0;
//		int curRow2 = 0;
//		
//		Col col1 = null;
//		Col col2 = null;
//		
//		while (true) {
//			if (curRow1 > limitIndex || curRow2 > limitIndex) {
//				break;
//			}
//			
//			col1 = (lastIndex1 < curRow1) ? null : colList1.get(curRow1);
//			col2 = (lastIndex2 < curRow2) ? null : colList2.get(curRow2);
//			
//			if (col1 == null && col2 == null) {
//				curRow1++;
//				curRow2++;
//				continue;
//				
//			} else if (col1 == null) {
//				paintRightDocStrong(curRow2);
//				curRow1++;
//				curRow2++;
//				continue;
//				
//			} else if (col2 == null) {
//				paintLeftDocStrong(curRow1);
//				curRow1++;
//				curRow2++;
//				continue;
//			}
//			
//			if (!col1.getText().equals(col2.getText())) {
//				// 다른거 나오면 일단 칠한다.
////				System.err.println("다르다 [" + col1.getText() + "] [" + col2.getText() + "]");
//				paintLeftDocStrong(curRow1);
//				paintRightDocStrong(curRow2);
//				
//				boolean foundSame = false;
//				
//				int beginRow1 = curRow1;
//				int beginRow2 = curRow2;
//				int endRow1 = -1;
//				int endRow2 = -1;
//				
//				// 다른거 한 번 나오면 똑같은거 나올 때까지 찾는다.
//				outLoop : for (int k=curRow1+1; k<lineCount1; k++) {
//					inLoop : for (int p=curRow2+1; p<lineCount2; p++) {
//						col1 = colList1.get(k);
//						col2 = colList2.get(p);
//						
//						if (col1 != null && col2 != null) {
//							if (col1.getText().equals(col2.getText())) {
//								// 콘텐츠가 같으면
//								foundSame = true;
//								
//								endRow1 = k;
//								endRow2 = p;
//								
//								curRow1 = k;
//								curRow2 = p;
//								
//								break outLoop;
//							}
//						}
//					}
//				}
//				
//				if (!foundSame) {
//					// 못찾으면 끝까지 칠하기.
//					endRow1 = lineCount1;
//					endRow2 = lineCount2;
//				}
//				
//				// 지금까지 라인 전부 칠하기 (좌측)
//				for (int rr=beginRow1+1; rr<endRow1-1; rr++) {
//					paintLeftDocNormal(rr);
//				}
////				// 지금까지 라인 전부 칠하기 (우측)
//				for (int rr=beginRow2+1; rr<endRow2-1; rr++) {
//					paintRightDocNormal(rr);
//				}
//				
//				if (!foundSame) {
//					break;
//				}
//				
//			}
//			
//			curRow1++;
//			curRow2++;
//		}
//	}
	
	
	public static void diffForHighlight() {
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
		
		boolean bSearchAxisIsLeft = true;
		
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
//		
//		int lineCount1 = colList1.size();
//		int lineCount2 = colList2.size();
//		
//		int limitIndex = lastIndex1;
//		if (limitIndex < lastIndex2) {
//			limitIndex = lastIndex2;
//		}
//		
//		int curRow1 = 0;
//		int curRow2 = 0;
//		
//		Col col1 = null;
//		Col col2 = null;
//		
//		while (true) {
//			if (curRow1 > limitIndex || curRow2 > limitIndex) {
//				break;
//			}
//			
//			col1 = (lastIndex1 < curRow1) ? null : colList1.get(curRow1);
//			col2 = (lastIndex2 < curRow2) ? null : colList2.get(curRow2);
//			
//			if (col1 == null && col2 == null) {
//				curRow1++;
//				curRow2++;
//				continue;
//				
//			} else if (col1 == null) {
//				paintRightDocStrong(curRow2);
//				curRow1++;
//				curRow2++;
//				continue;
//				
//			} else if (col2 == null) {
//				paintLeftDocStrong(curRow1);
//				curRow1++;
//				curRow2++;
//				continue;
//			}
//			
//			if (!col1.getText().equals(col2.getText())) {
//				// 다른거 나오면 일단 칠한다.
////				System.err.println("다르다 [" + col1.getText() + "] [" + col2.getText() + "]");
//				paintLeftDocStrong(curRow1);
//				paintRightDocStrong(curRow2);
//				
//				boolean foundSame = false;
//				
//				int beginRow1 = curRow1;
//				int beginRow2 = curRow2;
//				int endRow1 = -1;
//				int endRow2 = -1;
//				
//				// 다른거 한 번 나오면 똑같은거 나올 때까지 찾는다.
//				outLoop : for (int k=curRow1+1; k<lineCount1; k++) {
//					inLoop : for (int p=curRow2+1; p<lineCount2; p++) {
//						col1 = colList1.get(k);
//						col2 = colList2.get(p);
//						
//						if (col1 != null && col2 != null) {
//							if (col1.getText().equals(col2.getText())) {
//								// 콘텐츠가 같으면
//								foundSame = true;
//								
//								endRow1 = k;
//								endRow2 = p;
//								
//								curRow1 = k;
//								curRow2 = p;
//								
//								break outLoop;
//							}
//						}
//					}
//				}
//				
//				if (!foundSame) {
//					// 못찾으면 끝까지 칠하기.
//					endRow1 = lineCount1;
//					endRow2 = lineCount2;
//				}
//				
//				// 지금까지 라인 전부 칠하기 (좌측)
//				for (int rr=beginRow1+1; rr<endRow1-1; rr++) {
//					paintLeftDocNormal(rr);
//				}
////				// 지금까지 라인 전부 칠하기 (우측)
//				for (int rr=beginRow2+1; rr<endRow2-1; rr++) {
//					paintRightDocNormal(rr);
//				}
//				
//				if (!foundSame) {
//					break;
//				}
//				
//			}
//			
//			curRow1++;
//			curRow2++;
//		}
		
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
