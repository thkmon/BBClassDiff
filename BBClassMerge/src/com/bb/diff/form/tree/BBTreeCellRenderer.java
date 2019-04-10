package com.bb.diff.form.tree;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class BBTreeCellRenderer extends DefaultTreeCellRenderer {

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		
		leaf = false;
		
		try {
			BBTreeNode node = (BBTreeNode) value;
			if (node != null && node.isFile()) {
				leaf = true;
			} else {
				leaf = false;
			}
		} catch (Exception e) {
			// ignore
		}
		
		JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		return label;
	}
	
	
}
