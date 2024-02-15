package com.bb.classmerge.form;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;

public class BBMenuItem extends JMenuItem {

	public BBMenuItem() {
		super();
		this.setFont(MainForm.basicFont13);
	}

	public BBMenuItem(Action a) {
		super(a);
		this.setFont(MainForm.basicFont13);
	}

	public BBMenuItem(Icon icon) {
		super(icon);
		this.setFont(MainForm.basicFont13);
	}

	public BBMenuItem(String text, Icon icon) {
		super(text, icon);
		this.setFont(MainForm.basicFont13);
	}

	public BBMenuItem(String text, int mnemonic) {
		super(text, mnemonic);
		this.setFont(MainForm.basicFont13);
	}

	public BBMenuItem(String text) {
		super(text);
		this.setFont(MainForm.basicFont13);
	}
}