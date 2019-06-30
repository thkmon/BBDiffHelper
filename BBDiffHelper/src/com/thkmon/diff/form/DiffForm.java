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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.thkmon.diff.mng.TextDiffManager;
import com.thkmon.diff.util.ClipboardUtil;

public class DiffForm extends JFrame {

	public static JTextArea consoleArea = null;
	
	public DiffForm() {
		
		int areaTop = 100;
		int areaHeight = 300;
		
		DiffForm form = this;
		
		form.setTitle("BBDiffHelper");
		form.setBounds(0, 0, 800, 700);
		form.setLayout(null);
		form.getContentPane().setLayout(null);
		
		final JTextArea area1 = new JTextArea();
		area1.setBounds(10, areaTop, 370, areaHeight);
		area1.setBackground(Color.white);
		JScrollPane scrollPane1 = new JScrollPane(area1);
		scrollPane1.setBounds(10, areaTop, 370, areaHeight);
		scrollPane1.setBackground(Color.white);
		
		final JTextArea area2 = new JTextArea();
		area2.setBounds(400, areaTop, 370, areaHeight);
		area2.setBackground(Color.white);
		JScrollPane scrollPane2 = new JScrollPane(area2);
		scrollPane2.setBounds(400, areaTop, 370, areaHeight);
		scrollPane2.setBackground(Color.white);
		
		consoleArea = new JTextArea();
		consoleArea.setBounds(400, areaTop + areaHeight + 10, 370, areaHeight);
		consoleArea.setBackground(Color.white);
		JScrollPane scrollPane3 = new JScrollPane(consoleArea);
		scrollPane3.setBounds(10, areaTop + areaHeight + 10, 760, 230);
		scrollPane3.setBackground(Color.white);
		
		JButton diffButton = new JButton("Diff");
		diffButton.setBackground(Color.white);
		diffButton.setBounds(10, 10, 150, 30);
		
		diffButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TextDiffManager diffMng = new TextDiffManager();
				diffMng.diffString(area2.getText(), area1.getText());
			}
		});
		
		JButton clearButton = new JButton("Clear");
		clearButton.setBackground(Color.white);
		clearButton.setBounds(170, 10, 150, 30);
		
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				area1.setText("");
				area2.setText("");
				consoleArea.setText("");
			}
		});
		
		JLabel label1 = new JLabel("TO-BE");
		label1.setBounds(10, 60, 150, 30);
		
		JButton pasteButton1 = new JButton("Paste");
		pasteButton1.setBackground(Color.white);
		pasteButton1.setBounds(50, 60, 150, 30);
		
		pasteButton1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = ClipboardUtil.pasteClipboard();
				if (str != null && str.length() > 0) {
					area1.setText(str);
				}
			}
		});
		
		JLabel label2 = new JLabel("AS-IS");
		label2.setBounds(400, 60, 150, 30);
		
		JButton pasteButton2 = new JButton("Paste");
		pasteButton2.setBackground(Color.white);
		pasteButton2.setBounds(440, 60, 150, 30);
		
		pasteButton2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = ClipboardUtil.pasteClipboard();
				if (str != null && str.length() > 0) {
					area2.setText(str);
				}
			}
		});
		
		Container pane = form.getContentPane();
		pane.add(diffButton);
		pane.add(clearButton);
		pane.add(label1);
		pane.add(pasteButton1);
		pane.add(label2);
		pane.add(pasteButton2);
		pane.add(scrollPane1);
		pane.add(scrollPane2);
		pane.add(scrollPane3);
		
		form.setVisible(true);
		
		form.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("사용자 명령으로 종료합니다.");
				System.exit(0);
			}
		});
	}
}
