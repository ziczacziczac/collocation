package com.hus.cis.collocation.test;

import com.hus.cis.collocation.handler.evaluation.CollocationEvaluation;
import com.hus.cis.collocation.io.ReadFile;
import com.hus.cis.collocation.io.WriteFile;

import java.util.ArrayList;

public class TestEvaluation {
	static final String mle = "/home/zic/Desktop/NLP_RESULT2/collocation-tokenized/cands-bi-mle.txt";
	static final String pmi = "/home/zic/Desktop/NLP_RESULT2/collocation-tokenized/cands-bi-pmi.txt";
	static final String dice = "/home/zic/Desktop/NLP_RESULT2/collocation-tokenized/cands-bi-dice.txt";
	static final String tscore = "/home/zic/Desktop/NLP_RESULT2/collocation-tokenized/cands-bi-tscore.txt";
	static final String ll = "/home/zic/Desktop/NLP_RESULT2/collocation-tokenized/cands-bi-ll.txt";
	
	public static void main(String[] args) {
		CollocationEvaluation e = new CollocationEvaluation(tscore, pmi, mle, dice, ll, "negative_v4.txt", "positive_v4.txt");
		e.getAllEvaluation();
//		processCandBi();
	}
	
	public static void processCandBi(){
		ReadFile rf = new ReadFile();
		rf.open("/home/zic/Desktop/bigram.txt");
		ArrayList<String> list = rf.read();
		rf.close();
		
		rf.open("/home/zic/Desktop/NLP_RESULT2/cands-tokenized/cands-bi.txt");
		ArrayList<String> cands = rf.read();
		rf.close();
		
		ArrayList<String> cpc = new ArrayList<>();
		for(int i = 0; i < cands.size(); i++){
			cpc.add(cands.get(i));
		}
		for (String cand : cpc) {
			String s[] = cand.split(",");
			String name = s[0];
			System.out.println(name);
			boolean t = false;
			for(int j = 0; j < list.size(); j++){
				if(list.get(j).contains(name)){
					t = true;
					break;
				}
			}
			if(!t) cands.remove(cand);
		}
		
		WriteFile wf = new WriteFile();
		wf.open("/home/zic/Desktop/NLP_RESULT2/cands-tokenized/cands-bi.txt");
		for(int i = 0; i < cands.size(); i++){
			String res = i == cands.size() - 1 ? cands.get(i) : cands.get(i) + "\n";
			wf.write(res);
		}
		wf.close();
		System.out.println(cands.size());
	}
}
