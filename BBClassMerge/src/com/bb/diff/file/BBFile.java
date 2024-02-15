package com.bb.diff.file;

import java.io.File;

public class BBFile extends File {

	public BBFile(String arg0) {
		super(arg0);
	}
	
	private StringBuffer fileContent = null;
	private StringBuffer fileDecompiledContent = null;
	
	public StringBuffer getFileContent() {
		return fileContent;
	}
	public void setFileContent(StringBuffer fileContent) {
		this.fileContent = fileContent;
	}
	public StringBuffer getFileDecompiledContent() {
		return fileDecompiledContent;
	}
	public void setFileDecompiledContent(StringBuffer fileDecompiledContent) {
		this.fileDecompiledContent = fileDecompiledContent;
	}
	
	

}
