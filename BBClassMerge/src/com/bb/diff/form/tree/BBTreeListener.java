package com.bb.diff.form.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;
import javax.swing.tree.TreePath;

import com.bb.classmerge.form.BBMenuItem;
import com.bb.classmerge.util.ClipboardUtil;
import com.bb.diff.form.textarea.EditorUtil;

public class BBTreeListener implements MouseListener {

	private BBTree parentTree = null;
	
	public BBTreeListener(BBTree parentTreeObj) {
		this.parentTree = parentTreeObj;
	}
	
	/**
	 * 트리 노드 더블클릭시
	 */
	public void mouseClicked(MouseEvent arg) {
		if (arg.getClickCount() == 2) {
            TreePath path = parentTree.getPathForLocation(arg.getX(), arg.getY());
            if (path != null) {
            	BBTreeNode node = (BBTreeNode) path.getLastPathComponent();
//            	System.out.println("0 : leftAbsolutePath : " + node.getLeftAbsoulutePath());
//            	System.out.println("0 : rightAbsolutePath : " + node.getRightAbsoulutePath());

        		if (node.isFile()) {
        			EditorUtil.loadFileByNode(node, false, true);
        		}
            }
        }
		
		// 마우스 우클릭
		if (arg.getButton() == 3) {
			TreePath path = parentTree.getPathForLocation(arg.getX(), arg.getY());
			if (path != null) {
				BBTreeNode node = (BBTreeNode) path.getLastPathComponent();
				
				// 마우스 우클릭 시 트리요소부터 먼저 선택
				if (this.parentTree != null) {
					this.parentTree.setSelectionPath(path);
				}
				
				if (node.isFile()) {
					// 사용자가 파일을 마우스 우클릭했을 경우
					JPopupMenu popup = getFilePopupMenu(node, path);
					popup.show(this.parentTree, arg.getX() + 10, arg.getY());
					
				} else if (node.isDir()) {
					// 사용자가 폴더를 마우스 우클릭했을 경우
					JPopupMenu popup = getFolderPopupMenu(node, path);
					popup.show(this.parentTree, arg.getX() + 10, arg.getY());
				}
			}
		}
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}
	
	
	/**
	 * 트리의 폴더 위에서 마우스 우클릭 시 팝업메뉴
	 * 
	 * @param node
	 * @param path
	 * @return
	 */
	private JPopupMenu getFolderPopupMenu(final BBTreeNode node, final TreePath path) {
		JPopupMenu popupMenu = new JPopupMenu();
		
		final BBMenuItem subMenu1 = new BBMenuItem("하위폴더 확장 (Expand all subfolders)");
		popupMenu.add(subMenu1);
		
		subMenu1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TreeUtil.expandTreePath(path);
			}
		});
	
		final BBMenuItem subMenu2 = new BBMenuItem("하위폴더 축소 (Collapse all subfolders)");
		popupMenu.add(subMenu2);
		
		subMenu2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TreeUtil.collapseTreePath(path);
			}
		});
		
		boolean leftFileExists = EditorUtil.checkLeftFileExists(node);
		boolean rightFileExists = EditorUtil.checkRightFileExists(node);
		
		if (leftFileExists) {
			final BBMenuItem subMenu3 = new BBMenuItem("좌측 폴더경로 복사 (Copy left folder path)");
			subMenu3.setMnemonic(KeyEvent.VK_L); // 단축키 ALT + L
			popupMenu.add(subMenu3);
			
			subMenu3.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					String folderPath = node.getLeftAbsoulutePath();
					if (folderPath == null) {
						folderPath = "";
					}
					
					ClipboardUtil.copyToClipboard(folderPath);
				}
			});
		}
		
		if (rightFileExists) {
			final BBMenuItem subMenu3 = new BBMenuItem("우측 폴더경로 복사 (Copy right folder path)");
			subMenu3.setMnemonic(KeyEvent.VK_R); // 단축키 ALT + R
			popupMenu.add(subMenu3);
			
			subMenu3.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					String folderPath = node.getRightAbsoulutePath();
					if (folderPath == null) {
						folderPath = "";
					}
					
					ClipboardUtil.copyToClipboard(folderPath);
				}
			});
		}
		
		final BBMenuItem subMenu4 = new BBMenuItem("폴더명 복사 (Copy folder name)");
		subMenu4.setMnemonic(KeyEvent.VK_N); // 단축키 ALT + N
		popupMenu.add(subMenu4);
		
		subMenu4.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String folderName = node.getLeftFileNameWithExt();
				if (folderName == null || folderName.length() == 0) {
					folderName = node.getRightFileNameWithExt();
				}
				
				if (folderName != null) {
					folderName = "";
				}
				
				ClipboardUtil.copyToClipboard(folderName);
			}
		});
		
		final BBMenuItem subMenu5 = new BBMenuItem("하위폴더에서 동일한 파일 숨기기 (Hide same files from subfolders)");
		popupMenu.add(subMenu5);
		
		subMenu5.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				EditorUtil.loadDirByNode(node, true);
			}
		});
		
		return popupMenu;
	}
	
	
	/**
	 * 트리의 파일 위에서 마우스 우클릭 시 팝업메뉴
	 * 
	 * @param node
	 * @param path
	 * @return
	 */
	private JPopupMenu getFilePopupMenu(final BBTreeNode node, final TreePath path) {
		JPopupMenu popupMenu = new JPopupMenu();
		
		if (node.getTitle() != null && node.getTitle().length() > 0) {
			String subMenuTitle1 = "";
			if (node.getTitle().startsWith("{◎}")) {
				subMenuTitle1 = "마킹 제거 (Unmark to file name)";
			} else {
				subMenuTitle1 = "마킹 (Mark to file name)";
			}
			
			final BBMenuItem subMenu1 = new BBMenuItem(subMenuTitle1);
			subMenu1.setMnemonic(KeyEvent.VK_M); // 단축키 ALT + M
			popupMenu.add(subMenu1);
			
			subMenu1.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (node.getTitle() != null && node.getTitle().length() > 0) {
						if (node.getTitle().startsWith("{◎}")) {
							node.setTitle(node.getTitle().substring(3));
						} else {
							node.setTitle("{◎}" + node.getTitle());
						}
					}
				}
			});
		}
		
		boolean leftFileExists = EditorUtil.checkLeftFileExists(node);
		boolean rightFileExists = EditorUtil.checkRightFileExists(node);
		
		if (leftFileExists) {
			final BBMenuItem subMenu2 = new BBMenuItem("좌측 파일경로 복사 (Copy left file path)");
			subMenu2.setMnemonic(KeyEvent.VK_L); // 단축키 ALT + L
			popupMenu.add(subMenu2);
			
			subMenu2.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					String filePath = node.getLeftAbsoulutePath();
					if (filePath != null && filePath.length() > 0) {
						ClipboardUtil.copyToClipboard(filePath);
					}
				}
			});
		}
		
		if (rightFileExists) {
			final BBMenuItem subMenu2 = new BBMenuItem("우측 파일경로 복사 (Copy right file path)");
			subMenu2.setMnemonic(KeyEvent.VK_R); // 단축키 ALT + R
			popupMenu.add(subMenu2);
			
			subMenu2.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					String filePath = node.getRightAbsoulutePath();
					if (filePath != null && filePath.length() > 0) {
						ClipboardUtil.copyToClipboard(filePath);
					}
				}
			});
		}
		
		final BBMenuItem subMenu3 = new BBMenuItem("파일명 복사 (Copy file name)");
		subMenu3.setMnemonic(KeyEvent.VK_N); // 단축키 ALT + N
		popupMenu.add(subMenu3);
		
		subMenu3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String fileName = node.getLeftFileNameWithExt();
				if (fileName == null || fileName.length() == 0) {
					fileName = node.getRightFileNameWithExt();
				}
				
				if (fileName != null && fileName.length() > 0) {
					ClipboardUtil.copyToClipboard(fileName);
				}
			}
		});
		
		return popupMenu;
	}
}