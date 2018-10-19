package com.bb.diff.map;

public class FileContentInfo {
	private StringBuffer fileContent = null;
	private StringBuffer fileDecompileContent = null;
	public StringBuffer getFileContent() {
		return fileContent;
	}
	public void setFileContent(StringBuffer fileContent) {
		if (fileContent == null || fileContent.length() == 0) {
			return;
		}
		this.fileContent = fileContent;
	}
	public StringBuffer getFileDecompileContent() {
		return fileDecompileContent;
	}
	public void setFileDecompileContent(StringBuffer fileDecompileContent) {
		this.fileDecompileContent = fileDecompileContent;
	}
	
	
	
}
