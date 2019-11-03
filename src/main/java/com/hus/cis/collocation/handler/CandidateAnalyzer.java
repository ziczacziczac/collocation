package com.hus.cis.collocation.handler;

import com.hus.cis.collocation.utils.NgramUtils;
import com.hus.cis.collocation.SuperData;
import com.hus.cis.collocation.data.Candidate;
import com.hus.cis.collocation.handler.counting.CountBigram;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class CandidateAnalyzer {
	private NgramUtils load;
	public CandidateAnalyzer(boolean isTokenized) {
		load = new NgramUtils();
		load.setType(isTokenized);
	}
	public NgramUtils getLoadNgram(){
		return load;
	}
	public ArrayList<Candidate> getAnalyzeBigramCount(){
		ArrayList<Candidate> cands = new ArrayList<>();
		CountBigram bigram = load.loadBigram();
		
		Hashtable<String, Hashtable<String, SuperData>> bi = bigram.getBigram();
		Enumeration<String> first = bi.keys();
		int end = 0;
		while(first.hasMoreElements()){
			String firstWord = first.nextElement();
			Enumeration<String> second = bi.get(firstWord).keys();
			int numberOfFirstWord = 0;
			int begin = cands.size();
			while(second.hasMoreElements()){
				String secondWord = second.nextElement();
				int fAB = bi.get(firstWord).get(secondWord).getNumberOccurrence();
				numberOfFirstWord += fAB;
				Candidate cand = new Candidate();
				cand.setFreAB(fAB);
				cand.setName(firstWord + " " + secondWord);
				cand.setOnFiles(bi.get(firstWord).get(secondWord).getResultSuperData());
				cands.add(cand);
				end ++;
			}
			
			for(int i = begin; i < end; i ++){
				Candidate cand = cands.get(i);
				cand.setFreA(numberOfFirstWord - cand.getFreAB());
			}
		}
		Hashtable<String, Hashtable<String, SuperData>> reverseBigram = load.getReverseBigram().getBigram();
		cands = getFreBofBigram(reverseBigram, cands);
		return cands;
	}
	public ArrayList<Candidate> getFreBofBigram(Hashtable<String, Hashtable<String, SuperData>> reverseBigram, 
			ArrayList<Candidate> cands){
		for (Candidate candidate : cands) {
			String[] tokens = candidate.getName().split(" ");
			Enumeration<String> second = reverseBigram.get(tokens[1]).keys();
			int numberOfFirstToken = 0;
			while (second.hasMoreElements()) {
				String secondWord = second.nextElement();
				numberOfFirstToken += reverseBigram.get(tokens[1]).get(secondWord).getNumberOccurrence();
			}
			candidate.setFreB(numberOfFirstToken - reverseBigram.get(tokens[1]).get(tokens[0]).getNumberOccurrence());
		}
		return cands;
	}
	public ArrayList<Candidate> getAnalyzeTrigramCount(){
		ArrayList<Candidate> cands = new ArrayList<>();
		Hashtable<String, Hashtable<String, Hashtable<String, SuperData>>> tri = load.loadTrigram().getTrigramCount();
		int end = 0;
		int numberOfFirstWord = 0;
		Enumeration<String> first = tri.keys();
		while (first.hasMoreElements()) {
			String firstWord = first.nextElement();
			Enumeration<String> second = tri.get(firstWord).keys();
			while (second.hasMoreElements()) {
				String secondWord = second.nextElement();
				Enumeration<String> third = tri.get(firstWord).get(secondWord).keys();
				int begin = cands.size();
				numberOfFirstWord = 0;
				while(third.hasMoreElements()){
					String thirdWord = third.nextElement();
					numberOfFirstWord += tri.get(firstWord).get(secondWord).get(thirdWord).getNumberOccurrence();
					Candidate cand = new Candidate();
					cand.setFreAB(tri.get(firstWord).get(secondWord).get(thirdWord).getNumberOccurrence());
					cand.setName(firstWord + " " + secondWord + " " + thirdWord);
					cand.setOnFiles(tri.get(firstWord).get(secondWord).get(thirdWord).getResultSuperData());
					cands.add(cand);
					end ++;
				}
				for (int i = begin; i < end; i++) {
					int freAB = cands.get(i).getFreAB();
					cands.get(i).setFreA(numberOfFirstWord - freAB);
				}
			}
		}
		Hashtable<String, Hashtable<String, Hashtable<String, SuperData>>> reverseTrigram = load.getReverseTrigram().getTrigramCount();
		cands = getFreBofTrigram(reverseTrigram, cands);
		
		return cands;
	}
	public ArrayList<Candidate> getFreBofTrigram(Hashtable<String, Hashtable<String, Hashtable<String, SuperData>>> reverseTrigram,
			ArrayList<Candidate> cands){
		for (Candidate candidate : cands) {
			String[] tokens = candidate.getName().split(" ");
			Enumeration<String> third = reverseTrigram.get(tokens[1]).get(tokens[2]).keys();
			int numberOfFirstWord = 0;
			while (third.hasMoreElements()) {
				String thirdWord = third.nextElement();
				numberOfFirstWord += reverseTrigram.get(tokens[1]).get(tokens[2]).get(thirdWord).getNumberOccurrence();
			}
			candidate.setFreB(numberOfFirstWord - reverseTrigram.get(tokens[1]).get(tokens[2]).get(tokens[0]).getNumberOccurrence());
		}
		return cands;
	}
	
	public ArrayList<Candidate> getAnalyzeFourgramCount(){
		ArrayList<Candidate> cands = new ArrayList<>();
		
		Hashtable<String, Hashtable<String, SuperData>> fourgram = load.load4gram().get4gram();
		Enumeration<String> firstKeys = fourgram.keys();
		while(firstKeys.hasMoreElements()){
			String first = firstKeys.nextElement();
			Enumeration<String> secondKeys = fourgram.get(first).keys();
			int end = 0, begin = cands.size();
			int numberOfFirstKey = 0;
			while(secondKeys.hasMoreElements()){
				String second = secondKeys.nextElement();
				SuperData sd = fourgram.get(first).get(second);
				
				Candidate cand = new Candidate();
				cand.setFreAB(sd.getNumberOccurrence());
				cand.setOnFiles(sd.getResultSuperData());
				cand.setName(first + " " + second);
				
				cands.add(cand);
				numberOfFirstKey += sd.getNumberOccurrence();
				end ++;
			}
			
			for(int i = begin; i < begin + end; i++){
				Candidate cand = cands.get(i);
				cand.setFreA(numberOfFirstKey - cand.getFreAB());
			}
		}
		Hashtable<String, Hashtable<String, SuperData>> reverseFourgram = load.getReverseFourgram().get4gram();
		cands = getFreBofFourgram(cands, reverseFourgram);
		return cands;
	}
	
	public ArrayList<Candidate> getFreBofFourgram(ArrayList<Candidate> cands, Hashtable<String, Hashtable<String, SuperData>> reverseFourgram){
		for (Candidate candidate : cands) {
			String[] elems = candidate.getName().split(" ");
			Enumeration<String> firstKeys = reverseFourgram.get(elems[3]).keys();
			int numberOfSecond = 0;
			while(firstKeys.hasMoreElements()){
				String first = firstKeys.nextElement();
				SuperData sd = reverseFourgram.get(elems[3]).get(first);
				numberOfSecond += sd.getNumberOccurrence();
			}
			candidate.setFreB(numberOfSecond - candidate.getFreAB());
		}
		return cands;
	}
	public void removeNgramBelongNgram(ArrayList<Candidate> ngramShort, ArrayList<Candidate> ngramLong){
		ArrayList<Candidate> cp = copyCandidates(ngramShort);
		for (Candidate candShort : cp) {
			int n = 0;
			for (Candidate candLong : ngramLong) {
				if(candLong.getName().contains(candShort.getName())) n++;
			}
			if(n == 1) ngramShort.remove(candShort);
		}
	}
	
	public ArrayList<Candidate> copyCandidates(ArrayList<Candidate> cands){
		ArrayList<Candidate> cp = new ArrayList<>();
		for (Candidate candidate : cands) {
			cp.add(candidate);
		}
		return cp;
	}
}
