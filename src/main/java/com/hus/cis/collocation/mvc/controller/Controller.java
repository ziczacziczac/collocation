package com.hus.cis.collocation.mvc.controller;

import com.hus.cis.collocation.io.DirectorySavedResult;
import com.hus.cis.collocation.utils.PreprocessingUtils;
import com.hus.cis.collocation.io.WriteFile;
import com.hus.cis.collocation.data.Candidate;
import com.hus.cis.collocation.handler.CandidateAnalyzer;
import com.hus.cis.collocation.handler.CollocationScoreCalculator;
import com.hus.cis.collocation.mvc.view.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Controller {
	private PreprocessingUtils split;
	private MainView mainView;
	private DateFormat dateFormat;
	private CollocationScoreCalculator collocationScoreCalculator;
	private String target = DirectorySavedResult.getDirectoryToSaveResult();
	static boolean isSelectedFolder = false;
	static boolean isSelectdType = false;
	static String path = "";
	public Controller() {
		mainView = new MainView();
		mainView.setVisible(true);
		
		split = new PreprocessingUtils();

		GetFolderWordTokenized folderWordTokenized = new GetFolderWordTokenized();
		mainView.addActionTokenized(folderWordTokenized);
		
		Preprocess pp = new Preprocess();
		mainView.addActionPreprocess(pp);
		
		Count count = new Count();
		mainView.addActionCountNgram(count);
		
		
		GetCandidates getCandidates = new GetCandidates();
		mainView.addActionGetCandsCount(getCandidates);
		
		CollocationExporter showCollocations = new CollocationExporter();
		mainView.addActionExportCollocationResult(showCollocations);
		
	}
	
	
	class GetFolderWordTokenized implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String path = "";
			JFileChooser choose = new JFileChooser();
			choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			choose.showOpenDialog(null);
			
			path = choose.getSelectedFile().getAbsolutePath();
			mainView.getFolderWordTokenize().setText(path);
		}
		
	}
	
	class Preprocess implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			System.out.println("here");
			int isTokenized = JOptionPane.showConfirmDialog(null, "Did the corpus tokenized");
			String begin = getTime();
			String path = mainView.getFolderWordTokenize().getText();
			System.out.println(path);
			double sizeMbytes = sizeInMb(path);
			split.setTypeCorpus(isTokenized);
			split.process(path);
			String end = getTime();
			MainView.getTextArea().append("Line split " + sizeMbytes + "Mb" + "\n begin " + begin + "\n end " + end +"\n\n");
		}
		
	}
	class Count implements ActionListener {

		@SuppressWarnings("unused")
		@Override
		public void actionPerformed(ActionEvent e) {
			ControlerFolderCountSelected controlerFolderCountSelected = new ControlerFolderCountSelected();
		}
		
	}
	class GetCandidates implements ActionListener {

		@SuppressWarnings("static-access")
		@Override
		public void actionPerformed(ActionEvent e) {
			CandidateAnalyzer candsCount;
			int isTokenized = JOptionPane.showConfirmDialog(null, "Did the corpus tokenize??");
			String begin = getTime();
			if(isTokenized == 0){
				candsCount = new CandidateAnalyzer(true);
				ArrayList<Candidate> candsBigram = candsCount.getAnalyzeBigramCount();
				ArrayList<Candidate> candsTrigram = candsCount.getAnalyzeTrigramCount();
				ArrayList<Candidate> candsFourgram = candsCount.getAnalyzeFourgramCount();
				
				candsCount.removeNgramBelongNgram(candsBigram, candsTrigram);
				candsCount.removeNgramBelongNgram(candsTrigram, candsFourgram);
				
				WriteFile wf = new WriteFile();
				wf.open(target + "cands-tokenized/cands-bi.txt");
				wf.writeCandidates(candsBigram, candsCount.getLoadNgram().getTotalFrequencyBigram());
				wf.close();
				
				wf.open(target + "cands-tokenized/cands-tri.txt");
				wf.writeCandidates(candsTrigram, candsCount.getLoadNgram().getTotalFrequencyTrigram());
				wf.close();
				
				wf.open(target + "cands-tokenized/cands-four.txt");
				wf.writeCandidates(candsFourgram, candsCount.getLoadNgram().getTotalFrequencyFourgram());
				wf.close();
				String end = getTime();
				mainView.getTextArea().append("Get candidates from tokenized corpus: \n"
						+ "size in MB: " + sizeInMb(target + "tokenized") + "\n"
						+ "begin: " + begin + "\n" + "end: " + end);
			} else if(isTokenized == 1){
				candsCount = new CandidateAnalyzer(false);
				ArrayList<Candidate> candsBigram = candsCount.getAnalyzeBigramCount();
				ArrayList<Candidate> candsTrigram = candsCount.getAnalyzeTrigramCount();
				ArrayList<Candidate> candsFourgram = candsCount.getAnalyzeFourgramCount();
				WriteFile wf = new WriteFile();
				wf.open(target + "cands-nontokenized/cands-bi.txt");
				wf.writeCandidates(candsBigram, candsCount.getLoadNgram().getTotalFrequencyBigram());
				wf.close();
				
				wf.open(target + "cands-nontokenized/cands-tri.txt");
				wf.writeCandidates(candsTrigram, candsCount.getLoadNgram().getTotalFrequencyTrigram());
				wf.close();
				
				wf.open(target + "cands-nontokenized/cands-four.txt");
				wf.writeCandidates(candsFourgram, candsCount.getLoadNgram().getTotalFrequencyFourgram());
				wf.close();
				String end = getTime();
				mainView.getTextArea().append("Get candidates from non-tokenized corpus: \n"
						+ "size in MB: " + sizeInMb(target + "nontokenized") + "\n"
						+ "begin: " + begin + "\n" + "end: " + end);
				
			} else {
				JOptionPane.showMessageDialog(null, "If you want to try again, \n please press button one more time");
			}
			
		}
		
	}
	class CollocationExporter implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			int isTokenized = JOptionPane.showConfirmDialog(null, "Do you want to export collocation from tokenized corpus?");
			if(isTokenized == 0 || isTokenized == 1){
				try {
					collocationScoreCalculator = new CollocationScoreCalculator(isTokenized);
					collocationScoreCalculator.calculate("cands-bi");
					collocationScoreCalculator.calculate("cands-tri");
					collocationScoreCalculator.calculate("cands-four");
					
					JOptionPane.showMessageDialog(null, "The collocation results were exporeted successfully!");
				} catch(Exception exception) {
					JOptionPane.showMessageDialog(null, "The collocation results were exporeted failed cause: " + exception.getLocalizedMessage());
				}
				
				
//				ViewCollocation viewCollocation = new ViewCollocation();
//				viewCollocation.setVisible(true);
//				String fileToShow = "";
//				while(!fileToShow.equals("cands-bi") && !fileToShow.equals("cands-tri") && !fileToShow.equals("cands-four")){
//					fileToShow = JOptionPane.showInputDialog("Do you want to watch cands-bi, cands-tri, cands-four collocation? \n"
//							+ "cands-bi: Candidates of bigram ; cands-tri: Candidates of trigram; cands-four: Candidates of four gram");
//				}
//				viewCollocation.initModel(collocationScoreCalculator.showCollocation(fileToShow));
//				viewCollocation.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(null, "The view was canceled! \n If you want to see collocation: press butoon Collocation again");
			}
		}
		
	}
	
	public double sizeInMb(String folder){
		File file = new File(folder);
		System.out.println(folder);
		double size = 0;
		for (File child : file.listFiles()) {
			size += child.length();
		}
		size = size / (1024 * 1024 * 1.0);
		return Math.round(size * 10) / 10.0;
	}
	public String getTime(){
		dateFormat = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
		return dateFormat.format(new Date());
	}
}

