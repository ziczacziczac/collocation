package com.hus.cis.collocation.mvc.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SelectFolderCount extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JButton btnU_B;
	private JButton btnTri;
	private JTextField textField;
	private JButton btnFolder;
	private JButton btn4gram;
	private JButton btnOther;
	private boolean isSelectedType = true;
	
	public SelectFolderCount(boolean isSelectedFolder, String path) {
		setBounds(100, 100, 378, 150);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(12, 52, 286, 27);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		btnFolder = new JButton("Folder");
		btnFolder.setBounds(12, 15, 86, 25);
		contentPanel.add(btnFolder);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnU_B = new JButton("1-2gram");
				buttonPane.add(btnU_B);
			}
			{
				btnTri = new JButton("3-gram");
				btnTri.setActionCommand("OK");
				buttonPane.add(btnTri);
				getRootPane().setDefaultButton(btnTri);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				{
					btn4gram = new JButton("4-gram");
					buttonPane.add(btn4gram);
				}
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		if(isSelectedFolder){
			textField.setText(path);
		}
		
		btnFolder.setEnabled(!isSelectedFolder);
		
		btnOther = new JButton("Other");
		btnOther.setBounds(270, 15, 90, 25);
		contentPanel.add(btnOther);
	}
	public void addActionCountUniAndBigram(ActionListener ac){
		btnU_B.addActionListener(ac);
	}
	public void addAcitonCountTrigram(ActionListener ac){
		btnTri.addActionListener(ac);
	}
	public void addAcitonCount4gram(ActionListener ac){
		btn4gram.addActionListener(ac);
	}
	public void addActionGetFolder(ActionListener ac){
		btnFolder.addActionListener(ac);
	}
	public void addActionChangeInfo(ActionListener ac){
		btnOther.addActionListener(ac);
	}
	public JTextField setTextPath(){
		return textField;
	}
	public JButton getButtonFolder(){
		return btnFolder;
	}
	public boolean getTypeOfCorpus(){
		return isSelectedType;
	}
}
