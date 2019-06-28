package com.thkmon.diff.form;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.thkmon.diff.mng.TextDiffManager;

public class DiffForm extends JFrame {

	public DiffForm() {
		
		DiffForm form = this;
		
		form.setTitle("BBDiffHelper");
		form.setBounds(0, 0, 800, 600);
		form.setLayout(null);
		form.getContentPane().setLayout(null);
		
		final JTextArea area1 = new JTextArea();
		area1.setBounds(10, 50, 370, 500);
		area1.setBackground(Color.white);
		JScrollPane scrollPane1 = new JScrollPane(area1);
		scrollPane1.setBounds(10, 50, 370, 500);
		scrollPane1.setBackground(Color.white);
		
		StringBuffer bb = new StringBuffer();
		bb.append("A").append("\n");
		bb.append("B").append("\n");
		bb.append("C").append("\n");
		bb.append("D").append("\n");
		bb.append("E").append("\n");
		area1.setText(bb.toString());
		
//		StringBuffer bb = new StringBuffer();
//		bb.append("A").append("\n");
//		bb.append("B").append("\n");
//		bb.append("C").append("\n");
//		bb.append("1").append("\n");
//		bb.append("2").append("\n");
//		bb.append("3").append("\n");
//		bb.append("E").append("\n");
//		area1.setText(bb.toString());
		
		final JTextArea area2 = new JTextArea();
		area2.setBounds(400, 50, 370, 500);
		area2.setBackground(Color.white);
		JScrollPane scrollPane2 = new JScrollPane(area2);
		scrollPane2.setBounds(400, 50, 370, 500);
		scrollPane2.setBackground(Color.white);
		
		StringBuffer bb2 = new StringBuffer();
		bb2.append("A").append("\n");
		bb2.append("B").append("\n");
		bb2.append("C").append("\n");
		bb2.append("1").append("\n");
		bb2.append("2").append("\n");
		bb2.append("3").append("\n");
		bb2.append("D").append("\n");
		area2.setText(bb2.toString());
		
//		StringBuffer bb2 = new StringBuffer();
//		bb2.append("A").append("\n");
//		bb2.append("B").append("\n");
//		bb2.append("C").append("\n");
//		bb2.append("D").append("\n");
//		bb2.append("E").append("\n");
//		area2.setText(bb2.toString());
		
		JButton button = new JButton("Diff");
		button.setBackground(Color.white);
		button.setBounds(10, 10, 150, 30);
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TextDiffManager diffMng = new TextDiffManager();
				diffMng.diffString(area1.getText(), area2.getText());
			}
		});
		
		Container pane = form.getContentPane();
		pane.add(scrollPane1);
		pane.add(scrollPane2);
		pane.add(button);
		
		form.setVisible(true);
		
		form.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("사용자 명령으로 종료합니다.");
				System.exit(0);
			}
		});
	}
}
