package com.bb.classmerge.util;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * PNG 이미지의 파란색(#0000FF)을 투명하게 처리하는 유틸리티
 * 
 * 사용법:
 * 1. Eclipse에서 이 파일을 우클릭
 * 2. Run As -> Java Application 선택
 * 3. resources/icon 및 resources/button 폴더의 모든 PNG 파일 처리
 * 4. 원본은 .backup 파일로 자동 백업됨
 */
public class ImageTransparencyUtil {
	
	/**
	 * 대상 색상이 이미지에 존재하는지 확인
	 * @param sourceImage 원본 이미지
	 * @param targetRed 대상 빨강 값
	 * @param targetGreen 대상 초록 값
	 * @param targetBlue 대상 파랑 값
	 * @param tolerance 허용 오차
	 * @return 대상 색상 존재 여부
	 */
	private static boolean hasTargetColor(BufferedImage sourceImage, int targetRed, int targetGreen, int targetBlue, int tolerance) {
		for (int y = 0; y < sourceImage.getHeight(); y++) {
			for (int x = 0; x < sourceImage.getWidth(); x++) {
				int rgb = sourceImage.getRGB(x, y);
				
				int red = (rgb >> 16) & 0xFF;
				int green = (rgb >> 8) & 0xFF;
				int blue = rgb & 0xFF;
				
				int redDiff = Math.abs(red - targetRed);
				int greenDiff = Math.abs(green - targetGreen);
				int blueDiff = Math.abs(blue - targetBlue);
				
				if (redDiff <= tolerance && greenDiff <= tolerance && blueDiff <= tolerance) {
					return true; // 대상 색상 발견
				}
			}
		}
		return false; // 대상 색상 없음
	}
	
	/**
	 * 파란색을 투명하게 변환
	 * @param sourceFile 원본 파일
	 * @param targetRed 대상 빨강 값 (0-255)
	 * @param targetGreen 대상 초록 값 (0-255)
	 * @param targetBlue 대상 파랑 값 (0-255)
	 * @param tolerance 색상 허용 오차 (0-50, 기본값 5 권장)
	 * @return 처리 성공 여부
	 */
	public static boolean makeColorTransparent(File sourceFile, int targetRed, int targetGreen, int targetBlue, int tolerance) {
		try {
			// 원본 이미지 읽기
			BufferedImage sourceImage = ImageIO.read(sourceFile);
			if (sourceImage == null) {
				System.out.println("[오류] 이미지를 읽을 수 없음: " + sourceFile.getName());
				return false;
			}
			
			// 대상 색상이 있는지 먼저 확인
			if (!hasTargetColor(sourceImage, targetRed, targetGreen, targetBlue, tolerance)) {
				System.out.println("↪ 대상 색상 없음 (건너뛔)");
				return true; // 오류가 아니므로 true 반환
			}
			
			// ARGB 타입의 새 이미지 생성 (투명도 지원)
			BufferedImage transparentImage = new BufferedImage(
				sourceImage.getWidth(), 
				sourceImage.getHeight(), 
				BufferedImage.TYPE_INT_ARGB
			);
			
			int transparentCount = 0;
			
			// 각 픽셀을 순회하며 대상 색상을 투명하게 변환
			for (int y = 0; y < sourceImage.getHeight(); y++) {
				for (int x = 0; x < sourceImage.getWidth(); x++) {
					int rgb = sourceImage.getRGB(x, y);
					
					// RGB 분리
					int red = (rgb >> 16) & 0xFF;
					int green = (rgb >> 8) & 0xFF;
					int blue = rgb & 0xFF;
					
					// 대상 색상과의 거리 계산
					int redDiff = Math.abs(red - targetRed);
					int greenDiff = Math.abs(green - targetGreen);
					int blueDiff = Math.abs(blue - targetBlue);
					
					// 대상 색상에 가까운 색상을 투명하게 처리
					if (redDiff <= tolerance && greenDiff <= tolerance && blueDiff <= tolerance) {
						// 완전 투명 (알파값 0)
						transparentImage.setRGB(x, y, 0x00FFFFFF);
						transparentCount++;
					} else {
						// 원본 색상 유지
						transparentImage.setRGB(x, y, rgb);
					}
				}
			}
			
			// 임시 파일에 저장
			File tempFile = new File(sourceFile.getParent(), sourceFile.getName() + ".tmp");
			ImageIO.write(transparentImage, "PNG", tempFile);
			
			// 원본 백업
			File backupFile = new File(sourceFile.getParent(), sourceFile.getName() + ".backup");
			if (!backupFile.exists()) {
				if (!sourceFile.renameTo(backupFile)) {
					// 백업 실패 시 복사 시도
					java.nio.file.Files.copy(sourceFile.toPath(), backupFile.toPath());
					sourceFile.delete();
				}
			} else {
				sourceFile.delete();
			}
			
			// 임시 파일을 원본 위치로 이동
			if (!tempFile.renameTo(sourceFile)) {
				java.nio.file.Files.move(tempFile.toPath(), sourceFile.toPath());
			}
			
			System.out.println("  -> 투명 픽셀: " + transparentCount + "개");
			return true;
			
		} catch (Exception e) {
			System.out.println("[오류] " + sourceFile.getName() + ": " + e.getMessage());
			return false;
		}
	}
	
	/**
	 * 디렉토리 내의 모든 PNG 파일을 처리
	 * @param dirPath 디렉토리 경로
	 * @param targetRed 대상 빨강 값 (0-255)
	 * @param targetGreen 대상 초록 값 (0-255)
	 * @param targetBlue 대상 파랑 값 (0-255)
	 * @param tolerance 색상 허용 오차 (0-50)
	 */
	public static void processDirectory(String dirPath, int targetRed, int targetGreen, int targetBlue, int tolerance) {
		File dir = new File(dirPath);
		if (!dir.exists() || !dir.isDirectory()) {
			System.out.println("[경고] 디렉토리가 존재하지 않습니다: " + dirPath);
			return;
		}
		
		File[] files = dir.listFiles((d, name) -> {
			String lower = name.toLowerCase();
			return lower.endsWith(".png") && !lower.endsWith(".backup");
		});
		
		if (files == null || files.length == 0) {
			System.out.println("[경고] PNG 파일이 없습니다: " + dirPath);
			return;
		}
		
		System.out.println();
		System.out.println("======================================");
		System.out.println("디렉토리: " + dirPath);
		System.out.println("파일 수: " + files.length);
		System.out.println("대상 색상: RGB(" + targetRed + ", " + targetGreen + ", " + targetBlue + ")");
		System.out.println("허용 오차: ±" + tolerance);
		System.out.println("======================================");
		
		int successCount = 0;
		int failCount = 0;
		
		for (File file : files) {
			System.out.print("[" + (successCount + failCount + 1) + "/" + files.length + "] " + file.getName() + " ... ");
			
			if (makeColorTransparent(file, targetRed, targetGreen, targetBlue, tolerance)) {
				successCount++;
				System.out.println("✓ 완료");
			} else {
				failCount++;
				System.out.println("✗ 실패");
			}
		}
		
		System.out.println();
		System.out.println("처리 완료: 성공 " + successCount + "개, 실패 " + failCount + "개");
		if (successCount > 0) {
			System.out.println("원본은 .backup 확장자로 백업되었습니다.");
		}
	}
	
//	/**
//	 * 메인 메서드 - Eclipse에서 실행
//	 * Run As -> Java Application으로 실행하세요
//	 */
//	public static void main(String[] args) {
//		System.out.println("\n╔═══════════════════════════════════════════╗");
//		System.out.println("║  PNG 이미지 투명도 변환 도구            ║");
//		System.out.println("║  파란색(#0000FF) -> 투명 변환           ║");
//		System.out.println("╚═══════════════════════════════════════════╝");
//		
//		// 대상 색상 설정: 파란색 #0000FF (RGB: 0, 0, 255)
//		int targetRed = 0;
//		int targetGreen = 0;
//		int targetBlue = 255;
//		
//		// 허용 오차 설정 (0~50, 값이 클수록 비슷한 색도 투명 처리)
//		int tolerance = 5;
//		
//		// icon 폴더 처리
//		processDirectory("resources/icon", targetRed, targetGreen, targetBlue, tolerance);
//		
//		// button 폴더 처리
//		processDirectory("resources/button", targetRed, targetGreen, targetBlue, tolerance);
//		
//		System.out.println();
//		System.out.println("╔═══════════════════════════════════════════╗");
//		System.out.println("║  모든 작업이 완료되었습니다!            ║");
//		System.out.println("╚═══════════════════════════════════════════╝");
//		System.out.println();
//		System.out.println("※ 문제가 있으면 .backup 파일로 복원할 수 있습니다.");
//		System.out.println("※ 백업 파일 삭제: resources 폴더에서 *.backup 파일 삭제");
//	}
}
