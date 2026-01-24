package com.bb.classmerge.main;

import java.util.HashMap;

import javax.swing.UIManager;

import com.bb.classmerge.form.MainForm;
import com.bb.classmerge.util.ConsoleUtil;
import com.bb.classmerge.util.PropertiesUtil;
import com.formdev.flatlaf.FlatLightLaf;

public class BBClassDiff {

	public static String defaultDirPath1 = "";
	public static String defaultDirPath2 = "";
	
	public static String version = "20251108";
	public static String title = "BBClassDiff";
	public static MainForm mainForm = null;
	
	public static void main(String[] args) {
		
		try {
			// FlatLaf 테마 적용
			try {
				UIManager.setLookAndFeel(new FlatLightLaf());
				
				// FlatLaf에서 트리 연결선(세로선)이 보이도록 설정
				UIManager.put("Tree.paintLines", true);
				
				// 트리 선택 시 색상 변경 (#C6DCFA)
				java.awt.Color selectionColor = new java.awt.Color(198, 220, 250);
				UIManager.put("Tree.selectionBackground", selectionColor);
				UIManager.put("Tree.selectionBorderColor", selectionColor);
				UIManager.put("Tree.selectionInactiveBackground", selectionColor);
				
				// 전체 행 선택이 아닌 파일명(노드) 부분만 하이라이트 되도록 설정
				UIManager.put("Tree.wideSelection", false);
				
				// 선택 시 글자색이 흰색이 되지 않도록 검은색으로 설정
				UIManager.put("Tree.selectionForeground", java.awt.Color.BLACK);
				UIManager.put("Tree.selectionInactiveForeground", java.awt.Color.BLACK);
				
			} catch (Exception e) {
				ConsoleUtil.print("FlatLaf 테마 적용 실패: " + e.getMessage());
			}
			HashMap<String, String> optionPropMap = null;
			
			try {
				optionPropMap = PropertiesUtil.readPropertiesFile("option.properties");
			} catch (Exception ie) {
				// 무시
			}
			
			if (optionPropMap != null) {
				String leftClassesDir = optionPropMap.get("leftClassesDir");
				if (leftClassesDir != null && leftClassesDir.trim().length() > 0) {
					defaultDirPath1 = leftClassesDir.trim();
				}
				
				String rightClassesDir = optionPropMap.get("rightClassesDir");
				if (rightClassesDir != null && rightClassesDir.trim().length() > 0) {
					defaultDirPath2 = rightClassesDir.trim();
				}
			}
			
			mainForm = new MainForm(title, version);
			
		} catch (Exception e) {
			ConsoleUtil.print(e);
		}
	}
}