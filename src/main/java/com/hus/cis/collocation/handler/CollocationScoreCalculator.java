package com.hus.cis.collocation.handler;

import com.hus.cis.collocation.utils.CollocationUtils;
import com.hus.cis.collocation.io.DirectorySavedResult;
import com.hus.cis.collocation.io.ReadFile;
import com.hus.cis.collocation.io.WriteFile;

import java.util.ArrayList;
import java.util.HashMap;

public class CollocationScoreCalculator {
	private CollocationUtils collocation;
	private String inputFolder = DirectorySavedResult.getDirectoryToSaveResult() + "cands-nontokenized/";
	private String outputFolder = DirectorySavedResult.getDirectoryToSaveResult() + "collocation-nontokenized/";
	public CollocationScoreCalculator(int isTokenized) {
		if(isTokenized == 0){
			inputFolder =  DirectorySavedResult.getDirectoryToSaveResult() + "cands-tokenized/";
			outputFolder =  DirectorySavedResult.getDirectoryToSaveResult() + "collocation-tokenized/";
		}
	}
	
	public void calculate(String txtInput){
		collocation = new CollocationUtils(inputFolder + txtInput +".txt");
		ArrayList<String> sortedMle = collocation.resultUnigram("mle", outputFolder + txtInput + "-mle.txt");
		ArrayList<String> sortedPmi = collocation.resultUnigram("pmi", outputFolder + txtInput + "-pmi.txt");
		ArrayList<String> sortedTScore = collocation.resultUnigram("tscore", outputFolder + txtInput +"-tscore.txt");
		ArrayList<String> sortedDice = collocation.resultUnigram("dice", outputFolder + txtInput +"-dice.txt");
		ArrayList<String> sortedLL = collocation.resultUnigram("ll", outputFolder + txtInput +"-ll.txt");
		
		aggreateCollocationRank(txtInput, sortedMle, sortedPmi, sortedTScore, sortedDice, sortedLL);
		aggreateCollocationValue(txtInput, sortedMle, sortedPmi, sortedTScore, sortedDice, sortedLL);		
		
	}
	
	
	public void aggreateCollocationRank(String input, ArrayList<String> sortedMle, ArrayList<String> sortedPmi,
			ArrayList<String> sortedTScore, ArrayList<String> sortedDice, ArrayList<String> sortedLL){
		String sortedAllResultByRank = outputFolder + input + "-rank.csv";
		WriteFile wf = new WriteFile();
		wf.open(sortedAllResultByRank);
		wf.write("mle,pmi,tscore,dice,ll" + "\n");
		for(int i = 0; i < sortedMle.size(); i++) {
			wf.write(getCollocation(sortedMle.get(i)) + "," + getCollocation(sortedPmi.get(i)) + "," + getCollocation(sortedTScore.get(i)) + ","
					 + getCollocation(sortedDice.get(i)) + "," + getCollocation(sortedLL.get(i)) + "\n");
		}
		wf.close();
	}
	
	public void aggreateCollocationValue(String input, ArrayList<String> sortedMle, ArrayList<String> sortedPmi,
			ArrayList<String> sortedTScore, ArrayList<String> sortedDice, ArrayList<String> sortedLL){
		String sortedAllResultByValue = outputFolder + input + "-value.csv";
		HashMap<String, Double[]> collocationMap = new HashMap<String, Double[]>();
		collocationMap = agrreateCollocationValue(sortedMle, collocationMap, 0);
		collocationMap = agrreateCollocationValue(sortedPmi, collocationMap, 1);
		collocationMap = agrreateCollocationValue(sortedTScore, collocationMap, 2);
		collocationMap = agrreateCollocationValue(sortedDice, collocationMap,3);
		collocationMap = agrreateCollocationValue(sortedLL, collocationMap, 4);

		
		WriteFile wf = new WriteFile();
		wf.open(sortedAllResultByValue);
		wf.write("collocation,mle,pmi,tscore,dice,ll" + "\n");
		collocationMap.forEach((collocation, values) -> {
			String line = collocation + "," + values[0] + "," + values[1] + "," + values[2] + "," + values[3] + "," + values[4];
			wf.write(line + "\n");
		});
		wf.close();
	}
	
	private HashMap<String, Double[]> agrreateCollocationValue(ArrayList<String> sortedCollocation, 
			HashMap<String, Double[]> currentMap, int ith){
		for(String collocationValue: sortedCollocation) {
			String key = getCollocation(collocationValue);
			Double value = getCollocationValue(collocationValue);
			Double[] d = currentMap.get(key);
			if(d == null) {
				d = new Double[5];
			}
			d[ith] = value;
			currentMap.put(key, d);
		}
		return currentMap;
	}

	private String getCollocation(String collocationAndValue) {
		return collocationAndValue.split(",")[0];
	}
	
	private Double getCollocationValue(String collocationAndValue) {
		return Double.parseDouble(collocationAndValue.split(",")[1]);
	}
	public ArrayList<String> showCollocation(String input){
		ArrayList<String> list = new ArrayList<>();
		ReadFile rf = new ReadFile();
		
		rf.open(outputFolder + input + "-mle.txt");
		ArrayList<String> candsMLE = rf.read();
		
		rf.open(outputFolder + input + "-pmi.txt");
		ArrayList<String> candsPMI = rf.read();
		
		rf.open(outputFolder + input + "-dice.txt");
		ArrayList<String> candsDICE = rf.read();
		
		rf.open(outputFolder + input + "-tscore.txt");
		ArrayList<String> candsTSORE = rf.read();
		
		rf.open(outputFolder + input + "-ll.txt");
		ArrayList<String> candsLL = rf.read();
		
		int size = candsDICE.size();
		for (int i = 0; i < size; i++) {
			String mle = nameCollocation(candsMLE.get(i));
			String pmi = nameCollocation(candsPMI.get(i));
			String dice = nameCollocation(candsDICE.get(i));
			String tsocre = nameCollocation(candsTSORE.get(i));
			String ll = nameCollocation(candsLL.get(i));
			
			list.add(mle + "," + pmi + "," + dice + "," + tsocre + "," + ll);
		}
		
		return list;
	}
	public String nameCollocation(String cand){
		String[] parts = cand.split(",");
		return parts[0];
	}
//	public static void main(String[] args) {
//		CollocationScoreCalculator collocationUtils = new CollocationScoreCalculator(0);
//		collocationUtils.calculate("cands-bi");
//		collocationUtils.calculate("cands-tri");
//		System.out.println("Complete!");
//	}
}
