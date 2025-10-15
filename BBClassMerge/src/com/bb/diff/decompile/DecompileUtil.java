package com.bb.diff.decompile;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.bb.classmerge.util.ConsoleUtil;
import com.bb.classmerge.util.StringUtil;
import com.bb.diff.common.CommonConst;
import com.bb.diff.file.FileUtil;
import com.bb.diff.map.FileContentInfo;
import com.bb.diff.path.PathUtil;
import com.bb.diff.prototype.StringList;

public class DecompileUtil {
	
	// jd-core는 jar 단위로 여는지라 쓸 수가 없다...
	// procyon 너무 느린데...
	
	public static StringBuffer readClassFile(String clsFilePath) {

//		DecompileOutput output = null;
		
		try {
//			NativeUtils.loadLibraryFromJar(path);
			
			clsFilePath = PathUtil.reviseStandardPath(clsFilePath);
			
			// 기존값 검사
			FileContentInfo info = CommonConst.fileContentMap.get(clsFilePath);
			
			// 파일 열기 시 항상 최신 파일을 가져오도록 처리
			if (info == null || CommonConst.ALWAYS_GET_RECENT_FILE) {
				info = new FileContentInfo();
				
			} else {
				StringBuffer buf = info.getFileDecompileContent();
				if (buf != null && buf.length() > 0) {
					return info.getFileDecompileContent();					
				}
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
				
				File javaFile = null;
				StringBuffer javaContent = null;
				if ("jad".equals(CommonConst.javaCompiler)) {
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
					
					javaFile = new File(fileNameOnly + ".java");
					while (!javaFile.exists()) {
						System.out.println("try decompile... : " + copiedClsFile.getAbsolutePath());
						Thread.sleep(100);
						cnt++;
						if (cnt > 10) {
							return new StringBuffer("fail to decompile!!");
						}
					}
		
					javaContent = FileUtil.readFile(javaFile);
					
				} else if ("CFR".equals(CommonConst.javaCompiler)) {
					String decompileContent = DecompileUtil.readClassFileContentByCFR(copiedClsFile.getAbsolutePath());
					if (decompileContent == null) {
						return new StringBuffer("");
					}
					
					javaContent = new StringBuffer();
					javaContent.append(decompileContent);
					
				} else {
					return new StringBuffer("");
				}
				
				// 클래스 핵심 라인만 비교하기 여부
				if (CommonConst.bDiffCoreContents) {
					// 클래스 핵심 라인만 가져오기 (클래스 라인, 메서드 라인, 멤버변수 라인)
					javaContent = getClassCoreLinesOnly(javaContent);
				}
				
				info.setFileContent(javaContent);
				CommonConst.fileContentMap.put(clsFilePath, info);
				
				if (javaFile != null) {
					javaFile.delete();
				}
				copiedClsFile.delete();
				
				return javaContent;
//			}
		    
		} catch (Exception e) {
			e.printStackTrace();			
			
		} finally {
		}
	    
	    return new StringBuffer("");
	}
	
	
	/**
	 * 클래스 핵심 라인만 가져오기 (클래스 라인, 메서드 라인, 멤버변수 라인)
	 * 
	 * @param buff
	 * @return
	 */
	private static StringBuffer getClassCoreLinesOnly(StringBuffer buff) {
		String str = buff.toString();
		StringBuffer resultBuff = new StringBuffer();
		
		boolean bNeedEnter = false;
		
		StringList content = StringUtil.splitMulti(str, "\r\n", "\n", "\r");
		if (content != null && content.size() > 0) {
			String oneLine = "";
			int lineCount = content.size();
			int lineLastIndex = lineCount - 1;
			for (int i=0; i<lineCount; i++) {
				oneLine = content.get(i);
				
				// trim
				oneLine = oneLine.replaceAll("^[\\s\\t\\r\\n]*", "");
				oneLine = oneLine.replaceAll("[\\s\\t\\r\\n]*$", "");
				
				if (oneLine.startsWith("public") ||
					oneLine.startsWith("private") ||
					oneLine.startsWith("protected")) {
					
					if (CommonConst.bDiffExceptingRivisionString) {
						// getCVSRevision 무시하자
						if (oneLine.indexOf("String getCVSRevision()") > -1) {
							continue;
						}
						
						// SVNFILEINFO 무시하자
						if (oneLine.indexOf("String SVNFILEINFO") > -1) {
							continue;
						}
					}
					
					if (bNeedEnter) {
						resultBuff.append("\n\n");
					}
					
					resultBuff.append(oneLine);
					bNeedEnter = true;
					
					
					// 마지막 문자열이 ), ;, { 로 끝나지 않으면 해당 문자열이 나올 때까지, 다음 라인 내용을 가져와서 붙인다.
					boolean bClassLine = StringUtil.containsOutsideDoubleQuotes(oneLine, "class");
					boolean bVariableLine = StringUtil.containsOutsideDoubleQuotes(oneLine, "=");
					boolean bMethodLine = StringUtil.containsOutsideDoubleQuotes(oneLine, "(");
					
					boolean bFoundEndOfLine = false;
					if (bClassLine) {
						// 클래스의 마지막 문자열은 {
						bFoundEndOfLine = oneLine.endsWith("{");
						
					} else if (bVariableLine) {
						// 멤버변수의 마지막 문자열은 ;
						bFoundEndOfLine = oneLine.endsWith(";");
						
					} else if (bMethodLine) {
						// 메서드의 마지막 문자열은 ) 또는 {
						bFoundEndOfLine = oneLine.endsWith(")") || oneLine.endsWith("{");
						
					} else {
						bFoundEndOfLine = true;
					}
					
					if (!bFoundEndOfLine) {
						int nextLineIdx = i + 1;
						int kLimit = nextLineIdx + 9;
						for (int k=nextLineIdx; k<kLimit; k++) {
							if (lineLastIndex < k) {
								break;
							}
							
							String nextLine = content.get(k);
							
							// trim
							nextLine = nextLine.replaceAll("^[\\s\\t\\r\\n]*", "");
							nextLine = nextLine.replaceAll("[\\s\\t\\r\\n]*$", "");
							
							if (nextLine != null && nextLine.length() > 0) {
								resultBuff.append(nextLine);
								
								if (bClassLine && nextLine.endsWith("{")) {
									// 클래스의 마지막 문자열은 {
									break;
									
								} else if (bVariableLine && nextLine.endsWith(";")) {
									// 멤버변수의 마지막 문자열은 ;
									break;
									
								} else if (bMethodLine && (nextLine.endsWith(")") || nextLine.endsWith("{"))) {
									// 메서드의 마지막 문자열은 ) 또는 {
									break;
								}
							}
						}
					}
				}
			}
		}
		
		return resultBuff;
	}
	
	public static String readClassFileContentByCFR(String filePath) {
		String decompileContent = "";
		String exeFileParam = "java -jar CFR/cfr-0.152.jar " + filePath;
		
		Process process = null;
		
		try {
			Runtime rt = Runtime.getRuntime();
			process = rt.exec(exeFileParam);

		    // 표준 출력 스트림 읽기 (CFR 디컴파일 결과)
			BufferedReader stdReader = null;
			InputStreamReader stdStream = null;
		    try {
		    	stdStream = new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8);
		        stdReader = new BufferedReader(stdStream);
		        
		        StringBuilder output = new StringBuilder();
		        String stdLine = null;
		        while ((stdLine = stdReader.readLine()) != null) {
		            output.append(stdLine).append(System.lineSeparator());
		        }

		        // 결과 콘솔 출력 (또는 파일 저장)
		        decompileContent = output.toString();
		        
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	
		    } finally {
		    	try {
			    	if (stdStream != null) {
			    		stdStream.close();
			    	}
		    	} catch (Exception e2) {}
		    	
		    	try {
			    	if (stdReader != null) {
			    		stdReader.close();
			    	}
		    	} catch (Exception e2) {}
		    }

		    // 표준 에러 스트림도 읽기 (CFR 로그용)
		    BufferedReader errReader = null;
			InputStreamReader errStream = null;
		    try {
		    	errStream = new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8);
		    	errReader = new BufferedReader(errStream);
		        String errLine = null;
		        while ((errLine = errReader.readLine()) != null) {
		        	System.err.println("DecompileUtil readClassFileContentByCFR ERROR : " + errLine);
		        }
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	
		    } finally {
		    	try {
			    	if (errStream != null) {
			    		errStream.close();
			    	}
		    	} catch (Exception e2) {}
		    	
		    	try {
			    	if (errReader != null) {
			    		errReader.close();
			    	}
		    	} catch (Exception e2) {}
		    }

		    process.waitFor();
		
		} catch (Exception e) {
			ConsoleUtil.print("decompile fail! exeFileParam == [" + exeFileParam + "]");
		    e.printStackTrace();
		    return null;
		}
		
		return decompileContent;
	}
}