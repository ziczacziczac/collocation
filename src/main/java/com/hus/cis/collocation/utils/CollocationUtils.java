package com.hus.cis.collocation.utils;

import com.hus.cis.collocation.data.Candidate;
import com.hus.cis.collocation.data.ContingencyTable;
import com.hus.cis.collocation.io.ReadFile;
import com.hus.cis.collocation.io.WriteFile;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Do Quang Dat k59 HUS_CIS
 * Handling Unigram(Bigram, Trigram): read file, filter and sort result by one measure
 */
public class CollocationUtils {
	private String fileinput = "";
	private List<Candidate> cans = null;
	private ArrayList<ContingencyTable> contingencyTables = null;
	private ReadFile rf;
	private WriteFile wf;
	//N la so luong cac cap tu
	private static int N;
	
	/**
	 * Create a new HandlingUnigram although read file input, remove candidates
	 *  occurred once time and build a list of unigrams
	 * @param input : path of file input
	 */
	public CollocationUtils(String input) {
		this.fileinput = input;
		input();
		buildListUnigram();
	}
	/**
	 * Read input from file
	 */
	public void input(){
		rf = new ReadFile();
		rf.open(fileinput);
		ArrayList<String> list = rf.read();
		rf.close();
		cans = new ArrayList<>();
		N = Integer.parseInt(list.get(0));
		for (int i = 1; i < list.size(); i++) {
			String strs[] = list.get(i).split(",");
			Candidate cand = new Candidate();
			cand.setName(strs[0]);
			cand.setFreA(Integer.valueOf(strs[1]));
			cand.setFreB(Integer.valueOf(strs[2]));
			cand.setFreAB(Integer.valueOf(strs[3]));
			cans.add(cand);
		}
	}
	
	/**
	 * Build a list of unigram form list of candidates
	 * each element of unigrams have to calculate all properties
	 */
	public void buildListUnigram(){
		contingencyTables = new ArrayList<>();
		for (int i = 0; i < cans.size(); i++) {
			Candidate cand = cans.get(i);
			ContingencyTable contingencyTable = new ContingencyTable(cand, N);
			contingencyTables.add(contingencyTable);
		}
	}
	/**
	 * Get the result after calculate a unigram.
	 * if parameter measure is null, the result is not sorted.
	 * if parameter measure is one of five measure, this measure is the standard of sorting
	 * @param measure : measure for compare two unigram
	 * @param pathfile : path of file ouput
	 */
	public ArrayList<String> resultUnigram(String measure, String pathfile){
		ArrayList<String> sortedCollocation = new ArrayList<String>();
		if(measure != null){
			quickSort(measure);
		}
		for (ContingencyTable contingencyTable : contingencyTables) {
			String collocation =  contingencyTable.toString(measure);
			sortedCollocation.add(collocation);
		}
		return sortedCollocation;
	}
	
	public void quickSort(String measure){
		int u = 0, v = contingencyTables.size() - 1;
		sort(contingencyTables, u, v, measure);
	}
	public void sort(ArrayList<ContingencyTable> unis, int u, int v,String measure){
		if(u >= v) return;
		int j = partion(contingencyTables, u, v, measure);
		sort(contingencyTables, u, j-1, measure);
		sort(contingencyTables, j+1, v, measure);
	}
	private int partion(ArrayList<ContingencyTable> uni, int u, int v, String measure){
		int i = u, j = v+1;
		while(true){
			while(compare(uni.get(++i),uni.get(u), measure)){
				if(i == v) break;
			}
			while(compare(uni.get(u), uni.get(--j), measure)){
				if(j == u) break;
			}
			if(i >= j){
				break;
			}
			swap(uni, i, j);
		}
		swap(uni, u, j);
		return j;
	}
	/**
	 * @param u : a unigram from list unigrams
	 * @param v : a unigram from list unigrams
	 * @param measure : the measure is the standard for sorting
	 * @return
	 */
	private boolean compare(ContingencyTable u, ContingencyTable v, String measure){
		if(measure == "mle"){
			return u.getMle() > v.getMle();
		} else if(measure == "pmi"){
			return u.getPmi() > v.getPmi();
		} else if(measure == "tscore"){
			return u.getTscore() > v.getTscore();
		} else if(measure == "dice"){
			return u.getDice() > v.getDice();
		} else {
			return u.getLl() > v.getLl();
		}
	}
	/**
	 * Change position between i and j in list unigrams
	 * @param uni : list of unigram
	 * @param i	: position i
	 * @param j : position j
	 */
	private void swap(ArrayList<ContingencyTable> uni, int i, int j){
		ContingencyTable tem = uni.get(i);
		uni.set(i, uni.get(j));
		uni.set(j, tem);
	}
}
