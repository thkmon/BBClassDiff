package com.bb.diff.form.tree;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class BBTree extends JTree {
	
	public BBTree(TreeNode arg0) {
		super(arg0, false);
		
		this.addMouseListener(new BBTreeListener(this));
	}
	
	private JScrollPane scrollPane = null;

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

//	public DefaultMutableTreeNode getRoot() {
//		DefaultMutableTreeNode obj = (DefaultMutableTreeNode) this.treeModel.getRoot();
//		return obj;
//	}
	
	/**
		//참고용
		public void addToRoot(String nodeName) {
			DefaultTreeModel model = (DefaultTreeModel) this.getModel();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
			root.add(new DefaultMutableTreeNode(nodeName));
			model.reload(root);
		}
	**/
	
	/**
	 * 루트 노드를 얻는다.
	 * 
	 * @param nodeName
	 * @return
	 */
	public BBTreeNode getRootNode() {
		DefaultTreeModel model = (DefaultTreeModel) this.getModel();
		BBTreeNode rootNode = (BBTreeNode) model.getRoot();
		return rootNode;
	}
	
	public void setRootNodeText(String nodeName) {
		DefaultTreeModel model = (DefaultTreeModel) this.getModel();
		BBTreeNode rootNode = new BBTreeNode(nodeName);
		model.setRoot(rootNode);
		model.reload(rootNode);
	}
	
	/**
	 * 노드 새로고침. 노드를 다시 그린다.
	 * @param nodeName
	 */
	public void refreshNode(BBTreeNode node) {
		DefaultTreeModel model = (DefaultTreeModel) this.getModel();
		model.reload(node);
	}
	
	/**
	 * 트리 새로고침. 트리 전체를 다시 그린다.
	 */
	public void refreshTreeSet() {
		DefaultTreeModel model = (DefaultTreeModel) this.getModel();
		model.reload(getRootNode());
	}
	
}
