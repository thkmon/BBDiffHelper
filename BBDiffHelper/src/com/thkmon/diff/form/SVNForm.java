package com.thkmon.diff.form;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.thkmon.diff.util.ClipboardUtil;
import com.thkmon.diff.util.SVNLogUtil;

public class SVNForm extends JFrame {

//	public static JTextArea area1 = null;
	public static String version = "200722";
	public static JTextField textField2 = null;
	public static JTextField textField3 = null;
	
	public SVNForm() {
		final SVNForm form = this;
		
		form.setTitle("BBSvnHelper_" + version);
		form.setBounds(0, 0, 640, 250);
		form.setLayout(null);
		form.getContentPane().setLayout(null);
		
		JLabel label1 = new JLabel("SVN URL");
		label1.setBounds(20, 10, 150, 30);
		
		JLabel label2 = new JLabel("REVISION 1");
		label2.setBounds(20, 50, 150, 30);
		
		JLabel label3 = new JLabel("REVISION 2");
		label3.setBounds(20, 90, 150, 30);
		
		JLabel label4 = new JLabel("CHARSET");
		label4.setBounds(20, 130, 150, 30);
		
		final JTextField textField1 = new JTextField();
		textField1.setBounds(100, 10, 500, 30);
		
		// 클립보드 데이터에 svn으로 시작하는 주소가 있으면 초기값으로 붙여넣는다.
		String clipboard = ClipboardUtil.pasteClipboard();
		if (clipboard != null && clipboard.startsWith("svn://")) {
			textField1.setText(clipboard);
		} else {
			textField1.setText("svn://");
		}
		
		textField2 = new JTextField();
		textField2.setBounds(100, 50, 150, 30);
		
		textField3 = new JTextField();
		textField3.setBounds(100, 90, 150, 30);
		
		final JTextField textField4 = new JTextField();
		textField4.setText("UTF-8");
		textField4.setBounds(100, 130, 150, 30);
		
		JButton button1 = new JButton("Print Differences");
		button1.setBackground(Color.white);
		button1.setBounds(450, 90, 150, 30);
		
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String svnUrl = textField1.getText();
				SVNLogUtil.getSVNDiff(svnUrl, textField2.getText(), textField3.getText(), textField4.getText());
			}
		});
		
//		area1 = new JTextArea();
//		area1.setBounds(20, 140, 580, 380);
//		area1.setBackground(Color.white);
//		JScrollPane scrollPane1 = new JScrollPane(area1);
//		scrollPane1.setBounds(20, 140, 580, 380);
//		scrollPane1.setBackground(Color.white);
		
		Container pane = form.getContentPane();
		pane.add(label1);
		pane.add(label2);
		pane.add(label3);
		pane.add(label4);
		pane.add(textField1);
		pane.add(textField2);
		pane.add(textField3);
		pane.add(textField4);
		pane.add(button1);
//		pane.add(scrollPane1);
		
		JMenuBar menubar = new JMenuBar(); 
		JMenu menu1 = new JMenu("Open");
		JMenuItem menuItem = new JMenuItem("Show Diff Form");
		menu1.add(menuItem);
		// screenMenu.addSeparator();
		menubar.add(menu1);
		
		menuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				form.setVisible(false);
				DiffForm form1 = new DiffForm();
				
			}
		});

		setJMenuBar(menubar);
		
		form.setVisible(true);
		
		form.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("사용자 명령으로 종료합니다.");
				System.exit(0);
			}
		});
	}
	
}
