package com.bb.diff.form.tree;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.bb.classmerge.util.FileNameUtil;
import com.bb.diff.image.ImageIconUtil;

public class BBTreeCellRenderer extends DefaultTreeCellRenderer {

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
		
		// 확장자에 따라 트리노드 아이콘 교체하기
		try {
			Icon nodeIcon = null;
			
			boolean isFile = false;
			BBTreeNode node = (BBTreeNode) value;
			if (node != null && node.isFile()) {
				isFile = true;
				String fileExtension = FileNameUtil.getExtensionFromPath(node.getCorePath());
				nodeIcon = ImageIconUtil.getIconFromFileExtension(isFile, fileExtension);
				
			} else {
				isFile = false;
				nodeIcon = ImageIconUtil.getIconFromFileExtension(isFile, "");
			}
			
			if (nodeIcon != null) {
				setIcon(nodeIcon);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        return this;
	}
}