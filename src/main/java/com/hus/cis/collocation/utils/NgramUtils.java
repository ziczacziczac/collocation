package com.hus.cis.collocation.utils;

import com.hus.cis.collocation.SuperData;
import com.hus.cis.collocation.handler.counting.Count4gram;
import com.hus.cis.collocation.handler.counting.CountBigram;
import com.hus.cis.collocation.handler.counting.CountTriGram;
import com.hus.cis.collocation.handler.counting.CountUnigram;
import com.hus.cis.collocation.io.DirectorySavedResult;
import com.hus.cis.collocation.io.ReadFile;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class NgramUtils {
	private static final int BEGIN = 2;
	private String outputUnigram;
	private String outputBigram;
	private String outputTrigram;
	private String outputFourgram;
	private CountUnigram unigram;
	private CountBigram bigram;
	private CountTriGram trigram;
	private Count4gram fourgram;
	private CountBigram reverseBigram;
	private CountTriGram reverseTrigram;
	private Count4gram reverseFourgram;
	private ReadFile rf;
	private long totalFrequencyBigram = 0;
	private long totalFrequencyTrigram = 0;
	private long totalFrequencyFourgram = 0;
	private String target = DirectorySavedResult.getDirectoryToSaveResult();
	public NgramUtils() {
		unigram = new CountUnigram();
		bigram = new CountBigram();
		trigram = new CountTriGram();
		fourgram = new Count4gram();
		
		reverseBigram = new CountBigram();
		reverseTrigram = new CountTriGram();
		reverseFourgram = new Count4gram();
	}
	public void setType(boolean isTokenized){
		if(isTokenized){
			target += "tokenized/";
		} else {
			target += "nontokenized/";
		}
		outputUnigram = target + "unigram.txt";
		outputBigram = target + "bigram.txt";
		outputTrigram = target + "trigram.txt";
		outputFourgram = target + "fourgram.txt";
	}
	public CountUnigram loadUnigram(){
		rf = new ReadFile();
		rf.open(outputUnigram);
		ArrayList<String> list = rf.read();
		rf.close();
		Hashtable<String, SuperData> uniHashTable = new Hashtable<>();
		int i = BEGIN;
		int sizeOfList = list.size();
		int average = Integer.parseInt(list.get(1));
		int numberOfUnigram = 0;
		while(i < sizeOfList){
			String[] uni = list.get(i).split(" ");
			SuperData sd = new SuperData(uni[1]);
			if(sd.getNumberOccurrence() < 4 * average){
				uniHashTable.put(uni[0], sd);
				numberOfUnigram ++;
			}
			
			i++;
		}
		unigram.setN(numberOfUnigram);
		unigram.reload(uniHashTable);
		return unigram;
	}
	
	public CountBigram loadBigram(){
		rf = new ReadFile();
		rf.open(outputBigram);
		ArrayList<String> list = rf.read();
		rf.close();
		
		Hashtable<String, Hashtable<String, SuperData>> biHashTable = new Hashtable<>();
		Hashtable<String, Hashtable<String, SuperData>> reverseBiHashTable = new Hashtable<>();
		int i = BEGIN;
		int sizOfList = list.size();
		int numberOfBigram = 0;
		while(i < sizOfList){
			String[] bi = list.get(i).split(" ");
			if(bi.length < 3){
				JOptionPane.showMessageDialog(null, "Bigram error!! " + list.get(i));
				i++;
				continue;
			}
			SuperData sd = new SuperData(bi[2]);
			if(biHashTable.get(bi[0]) == null){
				Hashtable<String, SuperData> second = new Hashtable<>();
				second.put(bi[1], sd);
				biHashTable.put(bi[0], second);
			} else {
				biHashTable.get(bi[0]).put(bi[1], sd);
			}
			numberOfBigram ++;
			totalFrequencyBigram += sd.getNumberOccurrence();
			if(reverseBiHashTable.get(bi[1]) == null){
				Hashtable<String, SuperData> second = new Hashtable<>();
				second.put(bi[0], new SuperData(bi[2]));
				reverseBiHashTable.put(bi[1], second);
			} else {
				reverseBiHashTable.get(bi[1]).put(bi[0], sd);
			}
			
			i++;
		}
		
		reverseBigram.setN(numberOfBigram);
		reverseBigram.setBigram(reverseBiHashTable);
		
		bigram.setN(numberOfBigram);
		bigram.setBigram(biHashTable);
		
		return bigram;
	}
	
	public CountTriGram loadTrigram(){
		rf = new ReadFile();
		rf.open(outputTrigram);
		ArrayList<String> list = rf.read();
		rf.close();
		
		Hashtable<String, Hashtable<String, Hashtable<String, SuperData>>> triHashTable = new Hashtable<>();
		Hashtable<String, Hashtable<String, Hashtable<String, SuperData>>> reverseTriHashTable = new Hashtable<>();
		int i = BEGIN;
		int sizOfList = list.size();
		int numberOfBigram = 0;
		while(i < sizOfList){
			String[] tri = list.get(i).split(" ");
			if(tri.length < 4){
				JOptionPane.showMessageDialog(null, "Trigram error!! " + list.get(i));
				i++;
				continue;
			}
			SuperData sd = new SuperData(tri[3]);
			if(triHashTable.get(tri[0]) == null){
				Hashtable<String, SuperData> third = new Hashtable<>();
				third.put(tri[2], sd);
				Hashtable<String, Hashtable<String, SuperData>> second = new Hashtable<>();
				second.put(tri[1], third);
				triHashTable.put(tri[0], second);
			} else if(triHashTable.get(tri[0]).get(tri[1]) == null){
				Hashtable<String, SuperData> third = new Hashtable<>();
				third.put(tri[2], sd);
				triHashTable.get(tri[0]).put(tri[1], third);
			} else {
				triHashTable.get(tri[0]).get(tri[1]).put(tri[2], sd);
			}
			numberOfBigram ++;
			totalFrequencyTrigram += sd.getNumberOccurrence();
			if(reverseTriHashTable.get(tri[1]) == null){
				Hashtable<String, SuperData> third = new Hashtable<>();
				third.put(tri[0], sd);
				Hashtable<String, Hashtable<String, SuperData>> second = new Hashtable<>();
				second.put(tri[2], third);
				reverseTriHashTable.put(tri[1], second);
			} else if(reverseTriHashTable.get(tri[1]).get(tri[2]) == null){
				Hashtable<String, SuperData> third = new Hashtable<>();
				third.put(tri[0], sd);
				reverseTriHashTable.get(tri[1]).put(tri[2], third);
			} else {
				reverseTriHashTable.get(tri[1]).get(tri[2]).put(tri[0], sd);
			}
			
			
			i++;
		}
		
		reverseTrigram.setN(numberOfBigram);
		reverseTrigram.setTrigram(reverseTriHashTable);
		
		trigram.setN(numberOfBigram);
		trigram.setTrigram(triHashTable);
		return trigram;
	}
	public Count4gram load4gram(){
		ReadFile rf = new ReadFile();
		rf.open(outputFourgram);
		ArrayList<String> list = rf.read();
		rf.close();
		
		Hashtable<String, Hashtable<String, SuperData>> fourHashTable = new Hashtable<>();
		Hashtable<String, Hashtable<String, SuperData>> reverseFourHashTable = new Hashtable<>();
		
		int i = BEGIN;
		int sizeOfList = list.size();
		int numberOfFourgram = 0;
		while(i < sizeOfList){
			String[] four = list.get(i).split(" ");
			if(four.length < 5){
				JOptionPane.showMessageDialog(null, "Fourgram error!! " + list.get(i));
				i++;
				continue;
			}
			String first = four[0] + " " + four[1] + " " + four[2];
			String second = four[3];
			SuperData sd = new SuperData(four[4]);
			
			totalFrequencyFourgram += sd.getNumberOccurrence();
			if(fourHashTable.get(first) == null){
				Hashtable<String, SuperData> secondHasTable = new Hashtable<>();
				secondHasTable.put(second, sd);
				fourHashTable.put(first, secondHasTable);
			} else {
				fourHashTable.get(first).put(second, sd);
			}
			
			if(reverseFourHashTable.get(second) == null){
				Hashtable<String, SuperData> secondHasTable = new Hashtable<>();
				secondHasTable.put(first, sd);
				reverseFourHashTable.put(second, secondHasTable);
			} else {
				reverseFourHashTable.get(second).put(first, sd);
			}
			i++;
			numberOfFourgram ++;
		}
		reverseFourgram.setN(numberOfFourgram);
		reverseFourgram.set4gram(reverseFourHashTable);
		
		fourgram.setN(numberOfFourgram);
		fourgram.set4gram(fourHashTable);
		return fourgram;
	}
	public CountUnigram loadNgramAsUnigram(String input){
		String ngramPath = "";
		if(input.compareTo("bigram") == 0){
			ngramPath = outputBigram;
		} else if(input.compareTo("trigram") == 0){
			ngramPath = outputTrigram;
		} else {
			ngramPath = outputFourgram;
		}
		CountUnigram ngramAsUni = new CountUnigram();
		ReadFile rf = new ReadFile();
		rf.open(ngramPath);
		ArrayList<String> list = rf.read();
		Hashtable<String, SuperData> ngramTriAsUni = new Hashtable<>();
		for(int i = BEGIN; i < list.size(); i ++){
			String ngram = list.get(i);
			String[] elems = ngram.split(" ");
			String tokens = ngram.substring(0, ngram.length() - elems[elems.length - 1].length() -1);
			ngramTriAsUni.put(tokens, new SuperData(elems[elems.length - 1]));
		}
		ngramAsUni.setOneCount(ngramTriAsUni);
		ngramAsUni.setN(Integer.parseInt(list.get(0)));
		return ngramAsUni;
	}
	
	public CountBigram getReverseBigram(){
		return reverseBigram;
	}
	public CountTriGram getReverseTrigram(){
		return reverseTrigram;
	}
	public Count4gram getReverseFourgram(){
		return reverseFourgram;
	}
	public long getTotalFrequencyBigram(){
		return totalFrequencyBigram;
	}
	public long getTotalFrequencyTrigram(){
		return totalFrequencyTrigram;
	}
	public long getTotalFrequencyFourgram(){
		return totalFrequencyFourgram;
	}
	public static void main(String[] args) {
		NgramUtils ngram = new NgramUtils();
		ngram.setType(false);
		ngram.load4gram();
		ngram.getReverseFourgram().get("đầy", "án lưu trữ");
	}
}