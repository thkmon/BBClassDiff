package com.bb.classmerge.form;

import java.awt.Color;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.bb.classmerge.main.BBClassDiff;
import com.bb.classmerge.util.ConsoleUtil;
import com.bb.diff.form.button.JButtonUtil;
import com.bb.diff.form.text.JTextFieldUtil;

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
	// 폰트 파일 경로
	private String fontPath = "font/D2Coding.ttf";
	
	public static Font basicFont14 = new Font("맑은 고딕", Font.PLAIN, 14);
	public static Font basicFont13 = new Font("맑은 고딕", Font.PLAIN, 13);
	public static Font basicFont12 =  new Font("맑은 고딕", Font.PLAIN, 12);
	public static Font basicFont9 =  new Font("맑은 고딕", Font.PLAIN, 9);
	
	// 연한회색 색상 설정
	private Color lightGrayColor = new Color(240, 240, 240);
	
	
	private int defaultHeight = 400;
	private int textAreaTop = 120;
	private int textAreaHeightGap = 180;
	
	
	public MainForm(String title, String version) {
		
		try {
			basicFont14 = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
			basicFont14 = basicFont14.deriveFont(Font.PLAIN, (float) 14);
		} catch (Exception e) {
			e.printStackTrace();
			basicFont14 = new Font("맑은 고딕", Font.PLAIN, 14);
		}
		
		try {
			basicFont13 = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
			basicFont13 = basicFont13.deriveFont(Font.PLAIN, (float) 13);
		} catch (Exception e) {
			e.printStackTrace();
			basicFont13 = new Font("맑은 고딕", Font.PLAIN, 13);
		}
		
		try {
			basicFont12 = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
			basicFont12 = basicFont12.deriveFont(Font.PLAIN, (float) 12);
		} catch (Exception e) {
			e.printStackTrace();
			basicFont12 = new Font("맑은 고딕", Font.PLAIN, 12);
		}
		
		try {
			basicFont9 = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath));
			basicFont9 = basicFont9.deriveFont(Font.PLAIN, (float) 9);
		} catch (Exception e) {
			e.printStackTrace();
			basicFont9 = new Font("맑은 고딕", Font.PLAIN, 9);
		}
		
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
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				
				Rectangle rectangle = e.getComponent().getBounds();
				
				int formWidth = (int) rectangle.getWidth();
				int formHeight = (int) rectangle.getHeight();
				
				if (textField1 != null) {
					textField1.setBounds(140, 10, formWidth - 345, textFieldHeight);
				}
				
				if (textField2 != null) {
					textField2.setBounds(140, 50, formWidth - 345, textFieldHeight);
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
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
	}
	
	
	private void addLabel() {
		// 좌측상단 레이블 추가
		JLabel label1 = new JLabel(labelText1);
		label1.setFont(basicFont14);
		label1.setBounds(10, 10, 150, textFieldHeight);
		this.getContentPane().add(label1);
		
		JLabel label2 = new JLabel("(Production Dir)");
		label2.setFont(basicFont12);
		label2.setBounds(10, 25, 150, textFieldHeight);
		this.getContentPane().add(label2);
		
		// 좌측상단 레이블 추가
		JLabel label3 = new JLabel(labelText2);
		label3.setFont(basicFont14);
		label3.setBounds(10, 50, 150, textFieldHeight);
		this.getContentPane().add(label3);
		
		JLabel label4 = new JLabel("(Development Dir)");
		label4.setFont(basicFont12);
		label4.setBounds(10, 65, 150, textFieldHeight);
		this.getContentPane().add(label4);
	}
	
	
	private void addButton() {
		
		pathButton1 = JButtonUtil.createNewJButton("...");
		pathButton1.setFont(basicFont14);
		pathButton1.setBounds(600, 10, 35, textFieldHeight);
		this.getContentPane().add(pathButton1);
		
		pathButton2 = JButtonUtil.createNewJButton("...");
		pathButton2.setFont(basicFont14);
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
		decompileButton = JButtonUtil.createNewJButton("Decompile");
		decompileButton.setFont(basicFont14);
		decompileButton.setBounds(645, 10, 120, buttonHeight);
		this.getContentPane().add(decompileButton);
		
		// 191128. 머지 버튼은 꼭 필요한지 애매하므로 숨긴다.
		mergeButton = JButtonUtil.createNewJButton("Merge");
		mergeButton.setFont(basicFont14);
//		mergeButton.setBounds(645, 45, 120, buttonHeight);
//		this.getContentPane().add(mergeButton);
		
		diffButton = JButtonUtil.createNewJButton("Diff");
		diffButton.setFont(basicFont14);
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
		textField1 = JTextFieldUtil.createNewJTextField();
		textField1.setFont(basicFont13);
		this.getContentPane().add(textField1);
		textField1.setText(BBClassDiff.defaultDirPath1);
		textField1.setBounds(140, 10, 455, 35);
		
		// 상단 클래스 폴더경로 추가
		textField2 = JTextFieldUtil.createNewJTextField();
		textField2.setFont(basicFont13);
		this.getContentPane().add(textField2);
		textField2.setText(BBClassDiff.defaultDirPath2);
		textField2.setBounds(140, 50, 455, 35);
		
		// 포커싱
		textField1.requestFocus();
	}
	
	
	private void addTextArea() {
		// 하단 콘솔용 텍스트영역 추가
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBackground(lightGrayColor);
		textArea.setBounds(0, 0, 100, 100);
		textArea.setFont(basicFont14);
		textArea.setLineWrap(true);
		
		scrollPane = new JScrollPane(textArea);
		scrollPane.setBackground(lightGrayColor);
		this.getContentPane().add(scrollPane);
		scrollPane.setBounds(10, textAreaTop, 755, defaultHeight - textAreaHeightGap);
		JFrameDesignUtil.setColorToJScrollPane(scrollPane);
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