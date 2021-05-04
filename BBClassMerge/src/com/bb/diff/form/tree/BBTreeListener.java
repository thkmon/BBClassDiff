package com.bb.diff.form.tree;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.tree.TreePath;

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
		
		// 사용자가 파일을 마우스 우클릭했을 경우 {◎} 마크를 앞에 붙여준다. 사용자가 폴더를 마우스 우클릭헀을 경우 내용 동일한 파일들을 검사해서 제거 처리한다.
		if (arg.getButton() == 3) {
			// 마우스 우클릭
			TreePath path = parentTree.getPathForLocation(arg.getX(), arg.getY());
			if (path != null) {
				BBTreeNode node = (BBTreeNode) path.getLastPathComponent();
				
				if (node.isFile()) {
					if (node.getTitle() != null) {
						if (node.getTitle().startsWith("{◎}")) {
							node.setTitle(node.getTitle().substring(3));
						} else {
							node.setTitle("{◎}" + node.getTitle());
						}
					}
				} else if (node.isDir()) {
					// 사용자가 폴더를 마우스 우클릭했을 경우
					EditorUtil.loadDirByNode(node, true);
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
}