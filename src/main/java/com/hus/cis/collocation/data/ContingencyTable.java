package com.hus.cis.collocation.data;

import com.hus.cis.collocation.data.Candidate;

/**
 * @author Do Quang Dat k59 HUS_CIS
 * Store a Unigeram with some properties: contingency table, five association measure
 */
public class ContingencyTable {
	private String name;
	private int[][] observer = new int[2][2];
	private double[][] expected = new double[2][2];
	private double mle = 0, pmi = 0, tscore = 0, dice = 0, ll = 0;
	/**
	 * Constructor within candidate and size of sample
	 * @param cand : A candidates to exact unigram
	 * @param N	: Number of unigram on sample size N
	 */
	public ContingencyTable(Candidate cand, int N) {
		this.name = cand.getName();
		setObserver(cand.getFreAB(), cand.getFreA()	, cand.getFreB(), N);
		setExpected(N);
		calculateAssociationMeasure(N);
	}
	/**
	 * Constructor without prameters
	 */
	public ContingencyTable() {
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int[][] getObserver() {
		return observer;
	}
	
	/**
	 * Calculate Contingency table Observer
	 * @param freqAB : frequency of AB
	 * @param freqA	 : frequency of A include freqAB
	 * @param freqB  : frequency of B include freqAB
	 * @param N		 : Number of unigram from CollocationSample size N
	 */
	private void setObserver(int freqAB, int freqA, int freqB, int N) {
		observer = new int[2][2];
		observer[0][0] = freqAB;
		observer[0][1] = freqA;
		observer[1][0] = freqB;
		observer[1][1] = N - observer[0][0] - observer[0][1] - observer[1][0];
	}
	public double[][] getExpected() {
		return expected;
	}
	/**
	 * Calculate Contigency Table Expected
	 * @param N : Number of unigram from CollocationSample size N
	 */
	private void setExpected(int N) {
		expected = new double[2][2];
		int R1 = observer[0][0] + observer[0][1];
		int R2 = observer[1][0] + observer[1][1];
		int C1 = observer[0][0] + observer[1][0];
		int C2 = observer[0][1] + observer[1][1];
		
		expected[0][0] = expct(R1, C1, N);
		expected[0][1] = expct(R1, C2, N);
		expected[1][0] = expct(R2, C1, N);
		expected[1][1] = expct(R2, C2, N);
	}
	public double getMle() {
		return mle;
	}
	
	/**
	 * Calculate five association measure
	 * mle: Maximum Likelihood Estimates, mle = O_11 / N
	 * pmi: Pointwise Mutual Information, pmi = log(O_11 / E11)
	 * if O_11 || E_11 == 0 -> pmi = 0
	 * tscore: t-score, tscore = (O_11 - E_11) / O_11
	 * if O_11 == 0 -> tscore = 0
	 * dice: Dice, dice = 2 * O_11 / (R_1 + C_1)
	 * if(R_1 + C_1) == 0 -> dice = 0
	 * R_1 = O_11 + O_12
	 * C_1 = O_11 + O_21
	 * ll: Log-Likelihood, ll = 2 * O_ij * log(O_ij / E_ij), i,j = 1,2
	 * if(O_ij == 0) -> ll = 0
	 * @param N : Number of unigram from CollocationSample size N
	 */
	private void calculateAssociationMeasure(int N) {
		int[][] o = getObserver();
		double[][] e = getExpected();
		
		mle = o[0][0] * 1.0 / N;
		if(o[0][0] != 0 && e[0][0] != 0){
			pmi = Math.log(o[0][0] / e[0][0]);
		} 
		if(o[0][0] != 0){
			tscore = (o[0][0] - e[1][1]) / Math.sqrt(o[0][0]);
		}
		if(o[0][0] + o[0][1] + o[0][0] + o[1][0] != 0){
			dice = 2 * o[0][0] * 1.0 / (o[0][0] + o[0][1] + o[0][0] + o[1][0]);
		}
		boolean t = true;
		for (int i = 0; i < e.length; i++) {
			for (int j = 0; j < e.length; j++) {
				if(o[i][j] != 0){
					ll += o[i][j] * Math.log(o[i][j] / e[i][j]);
				} else {
					t = false;
				}
				
			}
		}
		if(t){
			ll *= 2;
		} else {
			ll = 0;
		}
	}
	public double getPmi() {
		return pmi;
	}
	
	public double getTscore() {
		return tscore;
	}
	
	public double getDice() {
		return dice;
	}
	
	public double getLl() {
		return ll;
	}
	
	
	/**
	 * Calculate expected cell
	 * @param x : Row
	 * @param y : Column
	 * @param N	: Size sample N
	 * @return : expected of a cell in contingency table expected
	 */
	public static double expct(double x, double y, int N){
		double res = 1.0;
		res *= x / N;
		res *= y / N;
		return res;
	}
	
	public String toString(String measure) {
		if(measure.compareTo("mle") == 0) return name + "," + mle;
		if(measure.compareTo("pmi") == 0) return name + "," + pmi;
		if(measure.compareTo("tscore") == 0) return name + "," + tscore;
		if(measure.compareTo("dice") == 0) return name + "," + dice;
		if(measure.compareTo("ll") == 0) return name + "," + ll;
		return name + "," + mle + "," + pmi + "," + tscore + "," + dice
				+ "," + ll;
	}
	
}
