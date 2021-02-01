package com.bb.classmerge.form;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.bb.classmerge.main.BBClassDiff;
import com.bb.classmerge.util.ConsoleUtil;
import com.bb.diff.common.CommonConst;

public class MainForm extends JFrame {

	
	// 상단 클래스 폴더경로
	public static JTextField textField1 = null;
	public static JTextField textField2 = null;
	
	public static String labelText1 = "Left Classes Dir";
	public static String labelText2 = "Right Classes Dir";
	public static JButton pathButton1 = null;
	public static JButton pathButton2 = null;
	
	// 우측상단 버튼
	private static JButton decompileButton = null;
	private static JButton mergeButton = null;
	private static JButton diffButton = null;
	
	private int textFieldHeight = 35;
	private int buttonHeight = 35;
	
	// 콘솔용 텍스트영역
	public static JTextArea textArea = null;
	
	// 콘솔용 스크롤
	public static JScrollPane scrollPane = null;
	
	
	// 폰트 설정
	private Font basicFont = new Font("돋움", 0, 14);
	private Font smallFont = new Font("돋움", 0, 12);
	
	// 연한회색 색상 설정
	private Color lightGrayColor = new Color(240, 240, 240);
	
	
	private int defaultHeight = 400;
	private int textAreaTop = 120;
	private int textAreaHeightGap = 180;
	
	
	public MainForm(String title, String version) {
		
		// 폼 생성
		if (title == null) {
			title = "";
		}
		
		if (version != null && version.length() > 0) {
			title = title + "_" + version;
		}
		
		this.setLayout(null);
		this.setTitle(title);
		
		addBasicWindowListener();
		
		addLabel();
		addButton();
		addTextField();
		addTextArea();
		
		this.setBounds(0, 0, 800, defaultHeight);
		this.setVisible(true);
	}
	
	
	private void addBasicWindowListener() {
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				// 사용자 명령으로 종료
				System.out.println("사용자 명령으로 종료합니다.");
				System.exit(0);
			}
		});
		
		
		this.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				
				Rectangle rectangle = e.getComponent().getBounds();
				
				int formWidth = (int) rectangle.getWidth();
				int formHeight = (int) rectangle.getHeight();
				
				if (textField1 != null) {
					textField1.setBounds(130, 10, formWidth - 335, textFieldHeight);
				}
				
				if (textField2 != null) {
					textField2.setBounds(130, 50, formWidth - 335, textFieldHeight);
				}
				
				if (pathButton1 != null) {
					pathButton1.setBounds(formWidth - 200, 10, 35, textFieldHeight);
				}
				
				if (pathButton2 != null) {
					pathButton2.setBounds(formWidth - 200, 50, 35, textFieldHeight);
				}
				
				if (decompileButton != null) {
					decompileButton.setBounds(formWidth - 155, 10, 120, buttonHeight);
				}
				
				// 191128. 머지 버튼은 꼭 필요한지 애매하므로 숨긴다.
//				if (mergeButton != null) {
//					mergeButton.setBounds(formWidth - 155, 45, 120, buttonHeight);
//				}
				
				if (diffButton != null) {
					diffButton.setBounds(formWidth - 155, 50, 120, buttonHeight);
				}
				
				if (scrollPane != null) {
					scrollPane.setBounds(10, textAreaTop, formWidth - 45, formHeight - textAreaHeightGap);
				}
				
				if (textArea != null) {
					textArea.setBounds(10, textAreaTop, formWidth - 45, formHeight - textAreaHeightGap);
				}
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	private void addLabel() {
		// 좌측상단 레이블 추가
		JLabel label1 = new JLabel(labelText1);
		label1.setFont(basicFont);
		label1.setBounds(10, 10, 150, textFieldHeight);
		this.getContentPane().add(label1);
		
		JLabel label2 = new JLabel("(Operation Dir)");
		label2.setFont(smallFont);
		label2.setBounds(10, 25, 150, textFieldHeight);
		this.getContentPane().add(label2);
		
		// 좌측상단 레이블 추가
		JLabel label3 = new JLabel(labelText2);
		label3.setFont(basicFont);
		label3.setBounds(10, 50, 150, textFieldHeight);
		this.getContentPane().add(label3);
		
		JLabel label4 = new JLabel("(Development Dir)");
		label4.setFont(smallFont);
		label4.setBounds(10, 65, 150, textFieldHeight);
		this.getContentPane().add(label4);
	}
	
	
	private void addButton() {
		
		pathButton1 = new JButton("...");
		pathButton1.setBackground(CommonConst.buttonColor);
		pathButton1.setFont(basicFont);
		pathButton1.setBounds(600, 10, 35, textFieldHeight);
		this.getContentPane().add(pathButton1);
		
		pathButton2 = new JButton("...");
		pathButton2.setBackground(CommonConst.buttonColor);
		pathButton2.setFont(basicFont);
		pathButton2.setBounds(600, 50, 35, textFieldHeight);
		this.getContentPane().add(pathButton2);
		
		pathButton1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedPath = new DialogWrapper().openFolderDialog(textField1.getText());
				if (selectedPath != null && selectedPath.length() > 0) {
					textField1.setText(selectedPath);
				}
			}
		});
		
		pathButton2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedPath = new DialogWrapper().openFolderDialog(textField2.getText());
				if (selectedPath != null && selectedPath.length() > 0) {
					textField2.setText(selectedPath);
				}
			}
		});
		
		
		// 우측상단 버튼 추가
		decompileButton = new JButton("Decompile");
		decompileButton.setBackground(CommonConst.buttonColor);
		decompileButton.setFont(basicFont);
		decompileButton.setBounds(645, 10, 120, buttonHeight);
		this.getContentPane().add(decompileButton);
		
		// 191128. 머지 버튼은 꼭 필요한지 애매하므로 숨긴다.
		mergeButton = new JButton("Merge");
		mergeButton.setBackground(CommonConst.buttonColor);
		mergeButton.setFont(basicFont);
//		mergeButton.setBounds(645, 45, 120, buttonHeight);
//		this.getContentPane().add(mergeButton);
		
		diffButton = new JButton("Diff");
		diffButton.setBackground(CommonConst.buttonColor);
		diffButton.setFont(basicFont);
		diffButton.setBounds(645, 50, 120, buttonHeight);
		this.getContentPane().add(diffButton);
		
		
		/**
		 * Decompile 버튼 기능
		 */
		decompileButton.addActionListener(new ActionListener() {
			
			// 버튼 클릭시 이벤트 수행
			@Override
			public void actionPerformed(ActionEvent e) {

				// 아직 로딩 전이면 버튼 동작하지 않는다.
				if (textField1 == null || textField2 == null) {
					ConsoleUtil.print("Loading...");
					return;
				}
				
				DecompileThead decompileThead = new DecompileThead();
				decompileThead.start();
			}
		});
		
		
		/**
		 * Merge 버튼 기능
		 */
		 mergeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// 아직 로딩 전이면 버튼 동작하지 않는다.
				if (textField1 == null || textField2 == null) {
					ConsoleUtil.print("Loading...");
					return;
				}
				
				MergeThread mergeThread = new MergeThread();
				mergeThread.start();
			}
		});
		
		
		/**
		 * Diff 버튼 기능
		 */
		diffButton.addActionListener(new ActionListener() {
			
			// 버튼 클릭시 이벤트 수행
			@Override
			public void actionPerformed(ActionEvent e) {

				// 아직 로딩 전이면 버튼 동작하지 않는다.
				if (textField1 == null || textField2 == null) {
					ConsoleUtil.print("Loading...");
					return;
				}
				
				DiffThread diffThread = new DiffThread();
				diffThread.start();
			}
		});
	}
	
	
	private void addTextField() {
		// 상단 클래스 폴더경로 추가
		textField1 = new JTextField();
		textField1.setFont(smallFont);
		this.getContentPane().add(textField1);
		textField1.setText(BBClassDiff.defaultDirPath1);
		textField1.setBounds(130, 10, 465, 35);
		textField1.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		
		// 상단 클래스 폴더경로 추가
		textField2 = new JTextField();
		textField2.setFont(smallFont);
		this.getContentPane().add(textField2);
		textField2.setText(BBClassDiff.defaultDirPath2);
		textField2.setBounds(130, 50, 465, 35);
		textField2.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		
		// 포커싱
		textField1.requestFocus();
	}
	
	
	private void addTextArea() {
		// 하단 콘솔용 텍스트영역 추가
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBackground(lightGrayColor);
		textArea.setBounds(0, 0, 100, 100);
		textArea.setFont(basicFont);
		textArea.setLineWrap(true);
		
		scrollPane = new JScrollPane(textArea);
		scrollPane.setBackground(lightGrayColor);
		this.getContentPane().add(scrollPane);
		scrollPane.setBounds(10, textAreaTop, 755, defaultHeight - textAreaHeightGap);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	}
	
	
	public static void setFormDisable() {
		textField1.setEnabled(false);
		textField2.setEnabled(false);
		
		pathButton1.setEnabled(false);
		pathButton2.setEnabled(false);
		
		decompileButton.setEnabled(false);
		mergeButton.setEnabled(false);
		diffButton.setEnabled(false);
	}
	
	
	public static void setFormEnable() {
		textField1.setEnabled(true);
		textField2.setEnabled(true);
		
		pathButton1.setEnabled(true);
		pathButton2.setEnabled(true);
		
		decompileButton.setEnabled(true);
		mergeButton.setEnabled(true);
		diffButton.setEnabled(true);
	}
}