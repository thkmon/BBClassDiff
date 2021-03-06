package com.bb.classmerge.util;

import java.util.Calendar;

public class DateUtil {
	
	
	public static String getTodayDateTime() {
		Calendar cal = Calendar.getInstance();
		StringBuffer today = new StringBuffer();
		today.append(String.format("%04d", cal.get(Calendar.YEAR)));
		today.append(String.format("%02d", cal.get(Calendar.MONTH) + 1));
		today.append(String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)));
		today.append(String.format("%02d", cal.get(Calendar.HOUR_OF_DAY)));
		today.append(String.format("%02d", cal.get(Calendar.MINUTE)));
		today.append(String.format("%02d", cal.get(Calendar.SECOND)));
		today.append(String.format("%03d", cal.get(Calendar.MILLISECOND)));
		return today.toString();
	}
	
	
	public static String getTodayDateTimeToPrint() {
		Calendar cal = Calendar.getInstance();
		StringBuffer today = new StringBuffer();
		
		today.append(String.format("%04d", cal.get(Calendar.YEAR)));
		today.append("/");
		today.append(String.format("%02d", cal.get(Calendar.MONTH) + 1));
		today.append("/");
		today.append(String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)));
		today.append("/");
		
		today.append(String.format("%02d", cal.get(Calendar.HOUR_OF_DAY)));
		today.append(":");		
		today.append(String.format("%02d", cal.get(Calendar.MINUTE)));
		today.append(":");
		today.append(String.format("%02d", cal.get(Calendar.SECOND)));
		
		return today.toString();
	}
	
	
	public static String getTodayDateTimeWithUnderbar() {
		Calendar cal = Calendar.getInstance();
		StringBuffer today = new StringBuffer();
		
		today.append(String.format("%04d", cal.get(Calendar.YEAR)));
		today.append(String.format("%02d", cal.get(Calendar.MONTH) + 1));
		today.append(String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)));

		today.append("_");

		today.append(String.format("%02d", cal.get(Calendar.HOUR_OF_DAY)));
		today.append(String.format("%02d", cal.get(Calendar.MINUTE)));
		today.append(String.format("%02d", cal.get(Calendar.SECOND)));
		
		return today.toString();
	}
	
	
	public static String getTodayDate() {
		Calendar cal = Calendar.getInstance();
		StringBuffer today = new StringBuffer();
		
		today.append(String.format("%04d", cal.get(Calendar.YEAR)));
		today.append(String.format("%02d", cal.get(Calendar.MONTH) + 1));
		today.append(String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)));
		
		return today.toString();
	}
	
	
	public static String getTodayTime() {
		Calendar cal = Calendar.getInstance();
		StringBuffer today = new StringBuffer();
		
		today.append(String.format("%02d", cal.get(Calendar.HOUR_OF_DAY)));
		today.append(String.format("%02d", cal.get(Calendar.MINUTE)));
		today.append(String.format("%02d", cal.get(Calendar.SECOND)));
		
		return today.toString();
	}
}