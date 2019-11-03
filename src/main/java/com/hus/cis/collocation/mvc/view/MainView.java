package com.hus.cis.collocation.mvc.view;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class MainView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField folderWordTokenize;
	
	private static JTextArea textArea;
	private JButton btnWordTokenizer;
	private JButton btn;
	private JButton btnExportCollocation;
	private JButton btnPreprocess;
	private JButton btnCands;
	
	public MainView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 541, 241);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 36, 339, 150);
		contentPane.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(345, 0, 178, 199);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		folderWordTokenize = new JTextField();
		folderWordTokenize.setBounds(0, 54, 93, 24);
		panel_1.add(folderWordTokenize);
		folderWordTokenize.setColumns(10);
		
		btnWordTokenizer = new JButton("Data");
		btnWordTokenizer.setBounds(0, 12, 166, 25);
		panel_1.add(btnWordTokenizer);
		
		btn = new JButton("Count ngram");
		btn.setBounds(0, 90, 166, 25);
		panel_1.add(btn);
		
		btnExportCollocation = new JButton("Export Collocation");
		btnExportCollocation.setBounds(0, 165, 166, 24);
		panel_1.add(btnExportCollocation);
		
		btnPreprocess = new JButton("Split");
		btnPreprocess.setBounds(103, 53, 63, 24);
		panel_1.add(btnPreprocess);
		
		btnCands = new JButton("Cands");
		btnCands.setBounds(0, 129, 166, 24);
		panel_1.add(btnCands);
		
		JLabel lblHistory = new JLabel("Log");
		lblHistory.setHorizontalAlignment(SwingConstants.CENTER);
		lblHistory.setBounds(0, 0, 41, 25);
		contentPane.add(lblHistory);
		
	}

	public JTextField getFolderWordTokenize() {
		return folderWordTokenize;
	}

	public void setFolderWordTokenize(JTextField folderWordTokenize) {
		this.folderWordTokenize = folderWordTokenize;
	}

	public static JTextArea getTextArea() {
		return textArea;
	}

	public JButton getBtnWordTokenizer() {
		return btnWordTokenizer;
	}

	public void setBtnWordTokenizer(JButton btnWordTokenizer) {
		this.btnWordTokenizer = btnWordTokenizer;
	}

	public JButton getBtnCountAndCutoff() {
		return btn;
	}

	public void setBtnCountAndCutoff(JButton btnCountAndCutoff) {
		this.btn = btnCountAndCutoff;
	}

	public JButton getBtnCollocation() {
		return btnExportCollocation;
	}

	public void setBtnCollocation(JButton btnCollocation) {
		this.btnExportCollocation = btnCollocation;
	}
	
	public void addActionPreprocess(ActionListener ac){
		btnPreprocess.addActionListener(ac);
	}
	
	public void addActionCountNgram(ActionListener ac){
		btn.addActionListener(ac);
	}
	public void addActionTokenized(ActionListener ac){
		btnWordTokenizer.addActionListener(ac);
	}
	public void addActionGetCandsCount(ActionListener ac){
		btnCands.addActionListener(ac);
	}
	public void addActionExportCollocationResult(ActionListener ac){
		btnExportCollocation.addActionListener(ac);
	}
}
