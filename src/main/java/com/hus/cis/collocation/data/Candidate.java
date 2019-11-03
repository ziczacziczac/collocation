package com.hus.cis.collocation.data;

/**
 * @author Do Quang Dat k59 HUS_CIS
 * Store Candidates with some properties: Frequency of A, B and AB	
 */
public class Candidate {
	private String name;
	private int freA, freB, freAB;
	private String onFiles;
	public String getOnFiles() {
		return onFiles;
	}
	public void setOnFiles(String onFiles) {
		this.onFiles = onFiles;
	}
	/**
	 * 	Constructor with parameters
	 * @param name : The name of unigram
	 * @param freA : The frequency of element A
	 * @param freB : the frequency of element B
	 * @param freAB : The frequency of AB
	 */
	public Candidate(String name, int freA, int freB, int freAB) {
		 
		this.name = name;
		this.freA = freA;
		this.freB = freB;
		this.freAB = freAB;
	}
	/**
	 *	Constructor without parameter.
	 */
	public Candidate() {
		// TODO Auto-generated constructor stub
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getFreA() {
		return freA;
	}
	public void setFreA(int freA) {
		this.freA = freA;
	}
	public int getFreB() {
		return freB;
	}
	public void setFreB(int freB) {
		this.freB = freB;
	}
	public int getFreAB() {
		return freAB;
	}
	public void setFreAB(int freAB) {
		this.freAB = freAB;
	}
	@Override
	public String toString() {
		return name + "," + freA + "," + freB + "," + freAB + "," + onFiles ;
	}
	
}
