package com.bb.diff.form.tree;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

import com.bb.diff.common.CommonConst;
import com.bb.diff.decompile.DecompileUtil;
import com.bb.diff.file.FileUtil;
import com.bb.diff.map.FileContentInfo;

public class BBTreeNode extends DefaultMutableTreeNode {
	
	public String toString() {
		return this.getTitle();
	}
	
	private String title = "";
	
	private boolean isFile = false;
	private boolean isDir = false;
	
	public boolean isFile() {
		return isFile;
	}

	public void setFile(boolean isFile) {
		this.isFile = isFile;
	}

	public boolean isDir() {
		return isDir;
	}

	public void setDir(boolean isDir) {
		this.isDir = isDir;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		refreshNode(this);
	}

	public BBTreeNode(Object arg0) {
		super(arg0, true);
		
		if (arg0 != null && arg0 instanceof String) {
			setTitle(String.valueOf(arg0));
		}
	}
	
	/**
	 * 특정 노드를 새로고침 한다.
	 * @param node
	 */
	public void refreshNode(BBTreeNode node) {
		if (CommonConst.treeModel != null) {
			CommonConst.treeModel.refreshNode(node);
		}
	}
	
	/**
	 * 특정 노드를 제거한다.
	 */
	public void removeMe() {
		if (this.getParent() != null) {
			BBTreeNode parentNode = (BBTreeNode) this.getParent();
			parentNode.remove(this);
			refreshNode(this);
		}
	}
	
	private String leftAbsoulutePath = "";
	private String rightAbsoulutePath = "";

	public String getAbsoultePath(boolean isLeft) {
		if (isLeft) {
			return leftAbsoulutePath;
		} else {
			return rightAbsoulutePath;
		}
	}
	
	public void setAbsoultePath(String path, boolean isLeft) {
		if (isLeft) {
			leftAbsoulutePath = path;
		} else {
			rightAbsoulutePath = path;
		}
	}
	
	public String getLeftAbsoulutePath() {
		return leftAbsoulutePath;
	}

	public void setLeftAbsoulutePath(String leftAbsoulutePath) {
		this.leftAbsoulutePath = leftAbsoulutePath;
	}

	public String getRightAbsoulutePath() {
		return rightAbsoulutePath;
	}

	public void setRightAbsoulutePath(String rightAbsoulutePath) {
		this.rightAbsoulutePath = rightAbsoulutePath;
	}

	/**
	 * 중복없이 추가
	 * 
	 * @param childNodeTitle
	 */
	public BBTreeNode addIfNotDupl(String childNodeTitle) {
		
		BBTreeNode oneNode = null;
		
		int childCount = this.getChildCount();
		for (int i=0; i<childCount; i++) {
			oneNode = (BBTreeNode) this.getChildAt(i);
			
			if (oneNode == null) {
				continue;
			}
			
			if (oneNode.getTitle().equals(childNodeTitle)) {
				// 이미 트리에 존재함
				return oneNode;
			}
			
			if ((oneNode.getTitle()).equals(childNodeTitle + " " + TreeUtil.leftMark)) {
				// 이미 트리에 존재함
				return oneNode;
			}
			
			if ((oneNode.getTitle()).equals(childNodeTitle + " " + TreeUtil.rightMark)) {
				// 이미 트리에 존재함
				return oneNode;
			}
			
		}
		
		BBTreeNode resultNode = addByForce(childNodeTitle);
		return resultNode;
	}
	
	/**
	 * 단순히 추가
	 * 
	 * @param childNodeTitle
	 */
	public BBTreeNode addByForce(String childNodeTitle) {
		BBTreeNode childNode = new BBTreeNode(childNodeTitle);
		this.add(childNode);
		return childNode;
	}
	
	/**
	 * 절대경로를 이용해 파일 내용을 로드하고, 공통 맵에 저장해둔다.
	 * 
	 * @param beForce
	 */
	public void initFileContent(boolean isLeft, boolean beForce, String inputAbsolutePath) {
		
		String absolPath = "";
		
		if (inputAbsolutePath != null && inputAbsolutePath.length() > 0) {
			absolPath = inputAbsolutePath;
			
		} else {
			absolPath = getAbsoultePath(isLeft);
		}
		
		// 절대 경로 검사
		if (absolPath == null || absolPath.length() == 0) {
			// 절대 경로 없으면 초기화 절대 못한다.
			if (isLeft) {
				System.err.println("initFileContent : left absoulutePath is null or empty. (0)");
			} else {
				System.err.println("initFileContent : right absoulutePath is null or empty. (0)");
			}
			return;
		}

		// 클래스인지 여부 검사
		boolean isClassFile = false;
		
		if (absolPath.endsWith(".class")) {
			isClassFile = true;
		}
		
		// 기존값 검사
		FileContentInfo info = CommonConst.fileContentMap.get(absolPath);

		// 190409. 무조건 새걸로 가져온다.
		info = null;
				
		if (info == null) {
			info = new FileContentInfo();
			
		} else {
			if (isClassFile) {
				if (info.getFileDecompileContent() != null && info.getFileDecompileContent().length() > 0) {
					// 내용 존재할 경우 스킵.
					if (!beForce) {
						return;
					}
				}
			} else {
				if (info.getFileContent() != null && info.getFileContent().length() > 0) {
					// 내용 존재할 경우 스킵.
					if (!beForce) {
						return;
					}
				}
			}
		}
		
		File f = new File(absolPath);
		if (!f.exists()) {
			return;
		}
		
		// 맵에 콘텐츠 저장한다.
		StringBuffer fileContent = FileUtil.readFile(f);
		info.setFileContent(fileContent);
		CommonConst.fileContentMap.put(absolPath, info);
	}
	
	/**
	 * 절대경로를 이용해 파일 내용을 디컴파일하고, 노드에 저장해둔다.
	 * 
	 * @param beForce
	 */
	public void initFileDecompileContent(boolean isLeft, boolean beForce, String inputAbsolutePath) {
		
		String absolPath = "";
		
		if (inputAbsolutePath != null && inputAbsolutePath.length() > 0) {
			absolPath = inputAbsolutePath;
			
		} else {
			absolPath = getAbsoultePath(isLeft);
		}
		
		// 절대 경로 검사
		if (absolPath == null || absolPath.length() == 0) {
			// 절대 경로 없으면 초기화 절대 못한다.
			if (isLeft) {
				System.err.println("initFileDecompileContent : left absoulutePath is null or empty. (0)");
			} else {
				System.err.println("initFileDecompileContent : right absoulutePath is null or empty. (0)");
			}
			return;
		}

		// 클래스인지 여부 검사
		boolean isClassFile = false;
		
		if (absolPath.endsWith(".class")) {
			isClassFile = true;
			
		} else {
			// 클래스가 아니면 디컴파일 경로 지정못한다.
			return;
		}
		
		// 기존값 검사
		FileContentInfo info = CommonConst.fileContentMap.get(absolPath);

		// 190409. 무조건 새걸로 가져온다.
		info = null;
				
		if (info == null) {
			info = new FileContentInfo();
			
		} else {
			if (isClassFile) {
				if (info.getFileDecompileContent() != null && info.getFileDecompileContent().length() > 0) {
					// 내용 존재할 경우 스킵.
					if (!beForce) {
						return;
					}
				}
			} else {
				if (info.getFileContent() != null && info.getFileContent().length() > 0) {
					// 내용 존재할 경우 스킵.
					if (!beForce) {
						return;
					}
				}
			}
		}
		
		// 맵에 콘텐츠 저장한다.
		StringBuffer fileDecompiledContent = DecompileUtil.readClassFile(absolPath);
		info.setFileDecompileContent(fileDecompiledContent);
		CommonConst.fileContentMap.put(absolPath, info);
	}
	
	
	public StringBuffer getFileContentString(boolean isLeft, String inputAbsolutePath) {
		
		String absolPath = "";
		
		if (inputAbsolutePath != null && inputAbsolutePath.length() > 0) {
			absolPath = inputAbsolutePath;
			
		} else {
			absolPath = getAbsoultePath(isLeft);
		}
		
		// 절대 경로 검사
		if (absolPath == null || absolPath.length() == 0) {
			// 절대 경로 없으면 파일 절대 못가져온다.
			if (isLeft) {
				System.err.println("getFileContentString : left absoulutePath is null or empty. (0)");
			} else {
				System.err.println("getFileContentString : right absoulutePath is null or empty. (0)");
			}
			return new StringBuffer("");
		}

		// 클래스인지 여부 검사
		boolean isClassFile = false;
		
		if (isLeft) {
			if (getLeftAbsoulutePath().endsWith(".class")) {
				isClassFile = true;
			}
		} else {
			if (getRightAbsoulutePath().endsWith(".class")) {
				isClassFile = true;
			}
		}
		
				
		// 기존값 검사
		FileContentInfo info = CommonConst.fileContentMap.get(absolPath);
		
		// 190409. 무조건 새걸로 가져온다.
		info = null;

		if (info == null) {
			info = new FileContentInfo();
			
		} else {
			if (isClassFile) {
				if (info.getFileDecompileContent() != null && info.getFileDecompileContent().length() > 0) {
					return info.getFileDecompileContent();
				}
				
			} else {
				if (info.getFileContent() != null && info.getFileContent().length() > 0) {
					return info.getFileContent();
				}
			}
		}
		
		// 값이 없을 경우.
		{
			StringBuffer buffer = new StringBuffer();
			
			// 인포에서 값을 가져온다.
			if (isClassFile) {
				buffer = info.getFileDecompileContent();
				if (buffer == null || buffer.length() == 0) {
					// 내용 없으므로 세팅한다.
					// System.err.println("새로 값을 세팅한다. (initFileDecompileContent 수행)");
					initFileDecompileContent(isLeft, true, absolPath);
				}
				
			} else {
				buffer = info.getFileContent();
				if (buffer == null || buffer.length() == 0) {
					// 내용 없으므로 세팅한다.
					// System.err.println("새로 값을 세팅한다. (initFileContent 수행)");
					initFileContent(isLeft, true, absolPath);
				}
			}
			
			if (buffer != null && buffer.length() > 0) {
				return buffer;
			}
		}
	
		// 세팅한 이후이니, 다시 로드해본다.
		info = CommonConst.fileContentMap.get(absolPath);
		if (info == null) {
			System.err.println("파일을 세팅할 수 없습니다. (0)");
			return new StringBuffer("");
		}
		
		StringBuffer resultBuffer = new StringBuffer();
		
		{
			// 인포에서 값을 다시 가져온다.
			if (isClassFile) {
				resultBuffer = info.getFileDecompileContent();
				if (resultBuffer == null || resultBuffer.length() == 0) {
					// 내용 없으면 안됨
					System.err.println("파일을 세팅할 수 없습니다. (1)");
				}
				
			} else {
				resultBuffer = info.getFileContent();
				if (resultBuffer == null || resultBuffer.length() == 0) {
					// 내용 없으면 안됨
					System.err.println("파일을 세팅할 수 없습니다. (2)");
				}
			}
		}
		
		if (resultBuffer != null && resultBuffer.length() > 0) {
			return resultBuffer;
		} else {
			return new StringBuffer();
		}
	}
	
	
}
