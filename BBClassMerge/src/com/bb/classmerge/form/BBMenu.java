package com.bb.classmerge.form;

import javax.swing.Action;
import javax.swing.JMenu;

public class BBMenu extends JMenu {

	public BBMenu() {
		super();
		this.setFont(MainForm.basicFont13);
	}

	public BBMenu(Action a) {
		super(a);
		this.setFont(MainForm.basicFont13);
	}

	public BBMenu(String s, boolean b) {
		super(s, b);
		this.setFont(MainForm.basicFont13);
	}

	public BBMenu(String s) {
		super(s);
		this.setFont(MainForm.basicFont13);
	}
}