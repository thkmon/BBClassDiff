package com.bb.diff.decompile;

import java.io.File;
import java.util.concurrent.TimeUnit;

import com.bb.diff.common.DiffConst;
import com.bb.diff.file.FileUtil;
import com.bb.diff.map.FileContentInfo;
import com.bb.diff.path.PathUtil;
//import com.strobel.decompiler.Decompiler;

public class DecompileUtil {
	
	// jd-core는 jar 단위로 여는지라 쓸 수가 없다...
	// procyon 너무 느린데...
	
	public static StringBuffer readClassFile(String clsFilePath) {

//		DecompileOutput output = null;
		
		try {
//			NativeUtils.loadLibraryFromJar(path);
			
			clsFilePath = PathUtil.reviseStandardPath(clsFilePath);
			
			// 기존값 검사
			FileContentInfo info = DiffConst.fileContentMap.get(clsFilePath);
			if (info != null) {
				StringBuffer buf = info.getFileDecompileContent();
				if (buf != null && buf.length() > 0) {
					return info.getFileDecompileContent();					
				}
				
			} else {
				info = new FileContentInfo();
			}
			
			File clsFile = new File(clsFilePath);
			if (!clsFile.exists()) {
				System.err.println("decompile error! " + clsFile.getAbsolutePath());
				return new StringBuffer("");
			}
			
			// procyon-decompiler
//			boolean useProcyon = false;
//			boolean useJd158 = !useProcyon;
			
//			if (useProcyon) {
//				
//				output = new DecompileOutput();
//				Decompiler decompiler = new Decompiler();
//			    com.strobel.decompiler.Decompiler.decompile(clsFilePath, output);
//			    
//			    if (output.getBuffer() == null || output.getBuffer().length() == 0) {
//			    	return new StringBuffer("");
//			    }
//			    
//			    info.setFileContent(output.getBuffer());
//			    CConst.fileContentMap.put(clsFilePath, info);
//			    
//			    return output.getBuffer();
//			    
//			} else {
				
//				System.out.println("디컴파일 시도 : " + clsFile.getAbsolutePath());
				
				// 이름 변경
				String fileNameOnly = clsFilePath;
				int lastSlash = fileNameOnly.lastIndexOf("\\");
				if (lastSlash > -1) {
					fileNameOnly = fileNameOnly.substring(lastSlash + 1);
				}
				
				int lastDot = fileNameOnly.lastIndexOf(".");
				if (lastDot > -1) {
					fileNameOnly = fileNameOnly.substring(0, lastDot);
				}
				
				FileUtil.copyFile(clsFile.getAbsolutePath(), fileNameOnly + ".class");
				
				File copiedClsFile = new File(fileNameOnly + ".class");
				if (!copiedClsFile.exists()) {
					System.err.println("copy for decompile error! " + copiedClsFile.getAbsolutePath());
					return new StringBuffer("");
				}
				
				Runtime rt = Runtime.getRuntime();
				String exeFile = "jad158g/jad.exe -o -sjava " + copiedClsFile.getAbsolutePath();
				
				Process process = null;
				
				try {
					process = rt.exec(exeFile);
				    process.getErrorStream().close();
				    process.getInputStream().close();
				    process.getOutputStream().close();
				    process.waitFor();
				
				} catch (Exception e) {
					System.err.println("decompile fail! exeFile: " + exeFile);
				    e.printStackTrace();
				    return new StringBuffer("");
				}

				int cnt = 0;
				
				File javaFile = new File(fileNameOnly + ".java");
				while (!javaFile.exists()) {
					System.out.println("try decompile... : " + copiedClsFile.getAbsolutePath());
					Thread.sleep(100);
					cnt++;
					if (cnt > 10) {
						return new StringBuffer("fail to decompile!!");
					}
				}

				StringBuffer javaContent = FileUtil.readFile(javaFile);
				
				info.setFileContent(javaContent);
//				System.out.println(javaContent.toString());
				DiffConst.fileContentMap.put(clsFilePath, info);
				
				javaFile.delete();
				copiedClsFile.delete();
				
				return javaContent;
//			}
		    
		} catch (Exception e) {
			e.printStackTrace();			
			
		} finally {
		}
	    
	    return new StringBuffer("");
	}
}
