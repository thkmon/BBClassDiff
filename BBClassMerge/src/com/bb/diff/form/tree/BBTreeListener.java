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
            	System.out.println("0 : leftAbsolutePath : " + node.getLeftAbsoulutePath());
            	System.out.println("0 : rightAbsolutePath : " + node.getRightAbsoulutePath());
            	
            	EditorUtil.loadFileByNode(node);
            }
        }
		
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
