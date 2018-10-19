package com.bb.classmerge.main;

import com.bb.classmerge.form.MainForm;
import com.bb.classmerge.util.ConsoleUtil;

public class BBClassMerge {

	public static String defaultDirPath1 = "C:/Users/bbmon/Desktop/부국정리/bukuk_cls/운영/classes_181012/classes";
	public static String defaultDirPath2 = "C:/Users/bbmon/Desktop/부국정리/bukuk_cls/개발/classes";
	
	public static String version = "181018";
	public static String title = "BBClassMerge";
	public static MainForm mainForm = null;
	
	public static void main(String[] args) {
		
		try {
			mainForm = new MainForm(title, version);
			
		} catch (Exception e) {
			ConsoleUtil.print(e);
		}
	}
}
