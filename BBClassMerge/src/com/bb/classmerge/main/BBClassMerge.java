package com.bb.classmerge.main;

import java.util.HashMap;

import com.bb.classmerge.form.MainForm;
import com.bb.classmerge.util.ConsoleUtil;
import com.bb.classmerge.util.PropertiesUtil;

public class BBClassMerge {

	public static String defaultDirPath1 = "";
	public static String defaultDirPath2 = "";
	
	public static String version = "20/02/17";
	public static String title = "BBClassMerge";
	public static MainForm mainForm = null;
	
	public static void main(String[] args) {
		
		try {
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
