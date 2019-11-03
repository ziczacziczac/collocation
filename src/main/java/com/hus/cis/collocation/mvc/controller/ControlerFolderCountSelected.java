package com.hus.cis.collocation.mvc.controller;

import com.hus.cis.collocation.utils.NgramUtils;
import com.hus.cis.collocation.mvc.view.SelectFolderCount;
import com.hus.cis.collocation.handler.counting.CountNgram;
import com.hus.cis.collocation.handler.counting.CountUnigram;
import com.hus.cis.collocation.mvc.view.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ControlerFolderCountSelected {
	private SelectFolderCount selectFolderCount;
	private SimpleDateFormat dateFormat;
	private CountNgram count;
	private String path;
	public ControlerFolderCountSelected() {
		selectFolderCount = new SelectFolderCount(Controller.isSelectedFolder, Controller.path);
		selectFolderCount.setVisible(true);
		
		count = new CountNgram();
		
		GetFolder getFolder = new GetFolder();
		selectFolderCount.addActionGetFolder(getFolder);
		
//		ChangeInfo changeInfo = new ChangeInfo();
//		selectFolderCount.addActionChangeInfo(changeInfo);
		
		UniAndBiFromFolder uniAndBiFromFolder = new UniAndBiFromFolder();
		selectFolderCount.addActionCountUniAndBigram(uniAndBiFromFolder);
		
		TriFromFolder triFromFolder = new TriFromFolder();
		selectFolderCount.addAcitonCountTrigram(triFromFolder);
		
		FourFromFolder fourFromFolder = new FourFromFolder();
		selectFolderCount.addAcitonCount4gram(fourFromFolder);
		
		if(Controller.path.compareTo("") != 0){
			path = Controller.path;
		}
	}

	class UniAndBiFromFolder implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			String begin = getTime();
			double sizeInMbytes = sizeInMb(path);
			boolean isTokenized = selectFolderCount.getTypeOfCorpus();
			count.setType(isTokenized);
			count.setFileInput(path);
			try {
				count.processUniBigram();
			} catch (IOException e) {
				e.printStackTrace();
			}
			int[] numberOfNgram = count.getCount();
			String end = getTime();
			MainView.getTextArea()
					.append("Count n-gram from tokenized corpus: " + sizeInMbytes + "Mb" + "\n Unigram: "
							+ numberOfNgram[0] + "\n Bigram: " + numberOfNgram[1] + "\n begin " + begin + "\n end "
							+ end + "\n\n");
		}

	}
	class TriFromFolder implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String begin = getTime();
			double sizeInMbytes = sizeInMb(path);
			NgramUtils load = new NgramUtils();
			boolean isTokenized = selectFolderCount.getTypeOfCorpus();
			load.setType(isTokenized);
			CountUnigram loadUnigram = load.loadUnigram();
			System.out.println("Unigram loaded sucessfully! " + loadUnigram.getN());
			count.setType(isTokenized);
			count.setFileInput(path);
			try {
				count.processTrigram(loadUnigram);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			String end = getTime();
			MainView.getTextArea().append("Count trigram " + sizeInMbytes + "Mb"
					+ "\n Trigram: Done!! "
					+ "\n begin " + begin + "\n end " + end + "\n\n");
		}
		
	}
	class FourFromFolder implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String begin = getTime();
			double sizeInMbytes = sizeInMb(path);
			NgramUtils load = new NgramUtils();
			boolean isTokenized = selectFolderCount.getTypeOfCorpus();
			load.setType(isTokenized);
			CountUnigram loadUnigram = load.loadUnigram();
			CountUnigram loadTriAsUnigram = load.loadNgramAsUnigram("trigram");
			count.setType(isTokenized);
			count.setFileInput(path);
			try {
				count.process4gramV3_1(loadTriAsUnigram, loadUnigram);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			String end = getTime();
			MainView.getTextArea().append("Count tetra " + sizeInMbytes + "Mb"
					+ "\n Trigram: Done!! "
					+ "\n begin " + begin + "\n end " + end + "\n\n");
		}
		
	}
	class GetFolder implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			JFileChooser choose = new JFileChooser();
			choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			choose.showOpenDialog(null);
			
			path = choose.getSelectedFile().getAbsolutePath();
			if(path != null){
				Controller.isSelectedFolder = true;
			}
			Controller.path = path;
			selectFolderCount.setTextPath().setText(path);
		}

	}
	class GetTypeCorpus implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Controller.isSelectdType = selectFolderCount.getTypeOfCorpus();
		}
		
	}
//	class ChangeInfo implements ActionListener {
//
//		@Override
//		public void actionPerformed(ActionEvent e) {
//			count.resetLoadFile();
//			selectFolderCount.getButtonFolder().setEnabled(true);
////			selectFolderCount.getCheckBoxType().setEnabled(true);
//		}
//		
//	}
	public double sizeInMb(String folder) {
		File file = new File(folder);
		double size = 0;
		for (File child : file.listFiles()) {
			size += child.length();
		}
		size = size / (1024 * 1024 * 1.0);
		return Math.round(size * 10) / 10.0;
	}

	public String getTime() {
		dateFormat = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
		return dateFormat.format(new Date());
	}
}
