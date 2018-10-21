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

import com.bb.classmerge.exception.MsgException;
import com.bb.classmerge.file.FileConverter;
import com.bb.classmerge.main.BBClassMerge;
import com.bb.classmerge.merge.MergeController;
import com.bb.classmerge.util.ConsoleUtil;
import com.bb.classmerge.util.StringUtil;
import com.bb.diff.main.DiffHelperMain;

public class MainForm extends JFrame {

	
	// 상단 클래스 폴더경로
	private JTextField textField1 = null;
	private JTextField textField2 = null;
	
	private String labelText1 = "Left Classes Dir";
	private String labelText2 = "Right Classes Dir";
	
	// 우측상단 버튼
	private JButton decompileButton = null;
	private JButton mergeButton = null;
	private JButton diffButton = null;
	
	private int textFieldHeight = 35;
	private int buttonHeight = 30;
	
	// 콘솔용 텍스트영역
	public static JTextArea textArea = null;
	
	// 콘솔용 스크롤
	private JScrollPane scrollPane = null;
	
	
	// 폰트 설정
	private Font basicFont = new Font("돋움", 0, 15);
	
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
		this.setBounds(0, 0, 800, defaultHeight);
		this.setVisible(true);
		
		addBasicWindowListener();
		
		addLabel();
		
		addButton();
		
		addTextField();
		
		addTextArea();
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
					textField1.setBounds(150, 10, formWidth - 315, textFieldHeight);
				}
				
				if (textField2 != null) {
					textField2.setBounds(150, 50, formWidth - 315, textFieldHeight);
				}
				
				if (decompileButton != null) {
					decompileButton.setBounds(formWidth - 155, 10, 120, buttonHeight);
				}
				
				if (mergeButton != null) {
					mergeButton.setBounds(formWidth - 155, 45, 120, buttonHeight);
				}
				
				if (diffButton != null) {
					diffButton.setBounds(formWidth - 155, 80, 120, buttonHeight);
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
		JLabel label = new JLabel(labelText1);
		label.setFont(basicFont);
		label.setBounds(10, 10, 150, textFieldHeight);
		this.getContentPane().add(label);
		
		// 좌측상단 레이블 추가
		JLabel label2 = new JLabel(labelText2);
		label2.setFont(basicFont);
		label2.setBounds(10, 50, 150, textFieldHeight);
		this.getContentPane().add(label2);
	}
	
	
	private void addButton() {
		// 우측상단 버튼 추가
		decompileButton = new JButton("Decompile");
		decompileButton.setFont(basicFont);
		decompileButton.setBounds(645, 10, 120, buttonHeight);
		this.getContentPane().add(decompileButton);
		
		mergeButton = new JButton("Merge");
		mergeButton.setFont(basicFont);
		mergeButton.setBounds(645, 45, 120, buttonHeight);
		this.getContentPane().add(mergeButton);
		
		diffButton = new JButton("Diff");
		diffButton.setFont(basicFont);
		diffButton.setBounds(645, 80, 120, buttonHeight);
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
				
				FileConverter fileConverter = new FileConverter();
				
				String classesDirText1 = textField1.getText();
				String classesDirText2 = textField2.getText();
				
				try {
					// 폴더경로 밸리드 체크
					fileConverter.checkDirectoryIsValid(classesDirText1);
					fileConverter.checkDirectoryIsValid(classesDirText2);
					
					String destDirPath1 = fileConverter.convertClassToJava(classesDirText1);
					String destDirPath2 = fileConverter.convertClassToJava(classesDirText2);
					
					File dir1 = new File(classesDirText1);
					File dir2 = new File(classesDirText2);
					String leftDirPath = StringUtil.revisePath(dir1.getAbsolutePath());
					String rightDirPath = StringUtil.revisePath(dir2.getAbsolutePath());
							
					ConsoleUtil.print("[" + labelText1 + "] Input Path : " + leftDirPath);
					ConsoleUtil.print("[" + labelText2 + "] Input Path : " + rightDirPath);
					ConsoleUtil.print("[" + labelText1 + "] Output Path : " + destDirPath1);
					ConsoleUtil.print("[" + labelText2 + "] Output Path : " + destDirPath2);
				
				} catch (MsgException ex) {
					ConsoleUtil.print(ex);
					
				} catch (Exception ex) {
					ConsoleUtil.print(ex);
				}
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
				
				FileConverter fileConverter = new FileConverter();
				
				String classesDirText1 = textField1.getText();
				String classesDirText2 = textField2.getText();
				
				try {
					// 폴더경로 밸리드 체크
					fileConverter.checkDirectoryIsValid(classesDirText1);
					fileConverter.checkDirectoryIsValid(classesDirText2);
					
					File dir1 = new File(classesDirText1);
					File dir2 = new File(classesDirText2);
					String leftDirPath = StringUtil.revisePath(dir1.getAbsolutePath());
					String rightDirPath = StringUtil.revisePath(dir2.getAbsolutePath());
							
					ConsoleUtil.print("[" + labelText1 + "] Input Path : " + leftDirPath);
					ConsoleUtil.print("[" + labelText2 + "] Input Path : " + rightDirPath);
					
					MergeController mergeCtrl = new MergeController();
					String destDirPath = mergeCtrl.mergeDirs(leftDirPath, rightDirPath);
					
					ConsoleUtil.print("Merged Path : " + StringUtil.revisePath(destDirPath));
					
				
				} catch (MsgException ex) {
					ConsoleUtil.print(ex);
					
				} catch (Exception ex) {
					ConsoleUtil.print(ex);
				}
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
				
				FileConverter fileConverter = new FileConverter();
				
				String classesDirText1 = textField1.getText();
				String classesDirText2 = textField2.getText();
				
				try {
					// 폴더경로 밸리드 체크
					fileConverter.checkDirectoryIsValid(classesDirText1);
					fileConverter.checkDirectoryIsValid(classesDirText2);
					
					File dir1 = new File(classesDirText1);
					File dir2 = new File(classesDirText2);
					String leftDirPath = StringUtil.revisePath(dir1.getAbsolutePath());
					String rightDirPath = StringUtil.revisePath(dir2.getAbsolutePath());
							
					ConsoleUtil.print("[" + labelText1 + "] Input Path : " + leftDirPath);
					ConsoleUtil.print("[" + labelText2 + "] Input Path : " + rightDirPath);
					
					DiffHelperMain.doDiffProcess(leftDirPath, rightDirPath);
				
				} catch (MsgException ex) {
					ConsoleUtil.print(ex);
					
				} catch (Exception ex) {
					ConsoleUtil.print(ex);
				}
			}
		});
	}
	
	
	private void addTextField() {
		// 상단 클래스 폴더경로 추가
		textField1 = new JTextField();
		textField1.setFont(basicFont);
		this.getContentPane().add(textField1);
		textField1.setText(BBClassMerge.defaultDirPath1);
		textField1.setBounds(150, 10, 485, 35);
		
		// 상단 클래스 폴더경로 추가
		textField2 = new JTextField();
		textField2.setFont(basicFont);
		this.getContentPane().add(textField2);
		textField2.setText(BBClassMerge.defaultDirPath2);
		textField2.setBounds(150, 50, 485, 35);
		
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
}