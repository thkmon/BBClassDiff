package com.bb.diff.image;

import java.awt.Image;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ImageIconUtil {
	
	/**
	 * 파일 확장자에 따라 Icon 객체 가져오기. 한 번 가져온 아이콘 객체는 iconMap 에 저장하여 비용 절약.
	 * 
	 */
	public static HashMap<String, Icon> iconMap = null;
	
	
	/**
	 * 파일 확장자에 따라 Icon 객체 가져오기. 한 번 가져온 아이콘 객체는 iconMap 에 저장하여 비용 절약.
	 * 
	 * @param isFile
	 * @param fileExtension
	 * @return
	 */
	public static Icon getIconFromFileExtension(boolean isFile, String fileExtension) {
		if (iconMap == null) {
			synchronized (ImageIconUtil.class) {
				iconMap = new HashMap<String, Icon>();
			}
		}
		
		Icon resultIcon = null;
		
		try {
			String imageIconKey = "unknown";
			
			if (isFile) {
				if (fileExtension != null && fileExtension.length() > 0) {
					imageIconKey = fileExtension;
					
					if (checkExtensionIsEqual(fileExtension, "java")) {
						imageIconKey = "java";
					
					} else if (checkExtensionIsEqual(fileExtension, "class")) {
						imageIconKey = "class";
					
					} else if (checkExtensionIsEqual(fileExtension, "jsp", "htm", "html", "php", "asp", "py")) {
						imageIconKey = "jsp";
					
					} else if (checkExtensionIsEqual(fileExtension, "js")) {
						imageIconKey = "js";
					
					} else if (checkExtensionIsEqual(fileExtension, "jpg", "jpeg", "png", "gif", "bmp", "tif", "tiff", "psd", "ai")) {
						imageIconKey = "jpg";
					
					} else if (checkExtensionIsEqual(fileExtension, "xml", "config", "conf")) {
						imageIconKey = "xml";
					
					} else if (checkExtensionIsEqual(fileExtension, "json")) {
						imageIconKey = "json";
					
					} else if (checkExtensionIsEqual(fileExtension, "ini", "properties")) {
						imageIconKey = "ini";
						
					} else if (checkExtensionIsEqual(fileExtension, "sql")) {
						imageIconKey = "sql";
					
					} else if (checkExtensionIsEqual(fileExtension, "txt")) {
						imageIconKey = "txt";
					
					} else if (checkExtensionIsEqual(fileExtension, "jar", "tar", "zip")) {
						imageIconKey = "jar";
					
					} else if (checkExtensionIsEqual(fileExtension, "exe")) {
						imageIconKey = "exe";
					
					} else if (checkExtensionIsEqual(fileExtension, "bat", "sh")) {
						imageIconKey = "bat";
					
					} else if (checkExtensionIsEqual(fileExtension, "xls", "xlsx")) {
						imageIconKey = "xls";
					
					} else if (checkExtensionIsEqual(fileExtension, "doc", "docx")) {
						imageIconKey = "doc";
					
					} else if (checkExtensionIsEqual(fileExtension, "ppt", "pptx")) {
						imageIconKey = "ppt";
					
					} else if (checkExtensionIsEqual(fileExtension, "csv")) {
						imageIconKey = "csv";
					
					} else if (checkExtensionIsEqual(fileExtension, "hwp")) {
						imageIconKey = "hwp";
					
					} else if (checkExtensionIsEqual(fileExtension, "pdf")) {
						imageIconKey = "pdf";
					
					} else {
						imageIconKey = "unknown";
					}
					
				} else {
					imageIconKey = "unknown";
				}
				
			} else {
				imageIconKey = "folder";
			}
			
			resultIcon = iconMap.get(imageIconKey);
			if (resultIcon == null) {
				synchronized (ImageIconUtil.class) {
					if (imageIconKey != null && imageIconKey.length() > 0) {
						File iconFile = new File("resources/icon/" + imageIconKey + ".png");
						if (iconFile.exists()) {
							Image iconImg = ImageIO.read(iconFile);
							if (iconImg != null) {
								Icon icon = new ImageIcon(iconImg);
								if (icon != null) {
									iconMap.put(imageIconKey, icon);
								}	
							}
						}
					}
					
				}
				
				resultIcon = iconMap.get(imageIconKey);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultIcon;
	}
	
	
	/**
	 * fileExtension 변수의 문자열과 일치하는 문자열이 checkExtensionArr 배열 내에 있는지 검사하기
	 * 
	 * @param fileExtension
	 * @param checkExtensionArr
	 * @return
	 */
	private static boolean checkExtensionIsEqual(String fileExtension, String... checkExtensionArr) {
		if (fileExtension == null || fileExtension.length() == 0) {
			return false;
		}
		
		if (checkExtensionArr == null || checkExtensionArr.length == 0) {
			return false;
		}
		
		int diffExtensionCount = checkExtensionArr.length;
		for (int i=0; i<diffExtensionCount; i++) {
			if (fileExtension.equalsIgnoreCase(checkExtensionArr[i])) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * 폴더명과 이미지 파일명으로 Icon 객체 가져오기
	 * 
	 * @param module
	 * @param imageFileName
	 * @return
	 */
	public static Icon getIconFromName(String folderName, String imageFileName) {
		try {
			if (folderName == null || folderName.length() == 0) {
				return null;
			}
			
			if (imageFileName == null || imageFileName.length() == 0) {
				return null;
			}
			
			File iconFile = new File("resources/" + folderName + "/" + imageFileName + ".png");
			if (iconFile.exists()) {
				Image iconImg = ImageIO.read(iconFile);
				if (iconImg != null) {
					Icon icon = new ImageIcon(iconImg);
					if (icon != null) {
						return icon;
					}	
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}