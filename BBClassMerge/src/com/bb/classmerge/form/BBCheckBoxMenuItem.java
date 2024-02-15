package com.bb.classmerge.form;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;

public class BBCheckBoxMenuItem extends JCheckBoxMenuItem {

	public BBCheckBoxMenuItem() {
		super();
		this.setFont(MainForm.basicFont13);
	}

	public BBCheckBoxMenuItem(Action a) {
		super(a);
		this.setFont(MainForm.basicFont13);
	}

	public BBCheckBoxMenuItem(Icon icon) {
		super(icon);
		this.setFont(MainForm.basicFont13);
	}

	public BBCheckBoxMenuItem(String text, boolean b) {
		super(text, b);
		this.setFont(MainForm.basicFont13);
	}

	public BBCheckBoxMenuItem(String text, Icon icon, boolean b) {
		super(text, icon, b);
		this.setFont(MainForm.basicFont13);
	}

	public BBCheckBoxMenuItem(String text, Icon icon) {
		super(text, icon);
		this.setFont(MainForm.basicFont13);
	}

	public BBCheckBoxMenuItem(String text) {
		super(text);
		this.setFont(MainForm.basicFont13);
	}
}