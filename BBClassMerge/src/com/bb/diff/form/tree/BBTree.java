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
	 * 루트 노드에 새 노드를 추가한다.
	 * 새로 추가한 노드를 리턴한다.
	 * 
	 * @param nodeName
	 * @return
	 */
//	public BBTreeNode addToRootNode(String nodeText) {
//		DefaultTreeModel model = (DefaultTreeModel) this.getModel();
//		BBTreeNode rootNode = (BBTreeNode) model.getRoot();
//		
//		BBTreeNode newTreeNode = new BBTreeNode(nodeText);
//		rootNode.add(newTreeNode);
//		
//		model.reload(rootNode);
//		return newTreeNode;
//	}
	
	/**
	 * 특정 노드에 새 노드를 추가한다.
	 * 새로 추가한 노드를 리턴한다.
	 * 
	 * @param targetNode
	 * @param nodeName
	 * @return
	 */
//	public BBTreeNode addToNode(BBTreeNode targetNode, String nodeText) {
//		DefaultTreeModel model = (DefaultTreeModel) this.getModel();
//		
//		BBTreeNode newTreeNode = new BBTreeNode(nodeText);
//		targetNode.add(newTreeNode);
//		
//		model.reload(targetNode);
//		return newTreeNode;
//	}
//	
	/**
	 * 트리를 다시 그린다.
	 */
	public void refreshTreeSet() {
		DefaultTreeModel model = (DefaultTreeModel) this.getModel();
		model.reload(getRootNode());
	}
	
}
