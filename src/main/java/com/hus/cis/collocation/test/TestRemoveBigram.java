package com.hus.cis.collocation.test;

import com.hus.cis.collocation.data.Candidate;
import com.hus.cis.collocation.handler.CandidateAnalyzer;

import java.util.ArrayList;

public class TestRemoveBigram {
	public static void main(String[] args) {
		CandidateAnalyzer ac = new CandidateAnalyzer(true);
		ArrayList<Candidate> bigram = ac.getAnalyzeBigramCount();
		ArrayList<Candidate> fourgram = ac.getAnalyzeFourgramCount();
		ArrayList<Candidate> trigram = ac.getAnalyzeTrigramCount();
		System.out.println(bigram.size());
		System.out.println(trigram.size());
		ac.removeNgramBelongNgram(bigram, trigram);
		ac.removeNgramBelongNgram(trigram, fourgram);
//		System.out.println(bigram.size());
		System.out.println(trigram.size());
//		for (Candidate candidate : bigram) {
//			System.out.println(candidate.toString());
//		}
	}
}
