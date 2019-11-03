package com.hus.cis.collocation.handler.counting;

import com.hus.cis.collocation.SuperData;
import com.hus.cis.collocation.data.Document;

import java.util.Enumeration;
import java.util.Hashtable;

public class CountUnigram {
	private static final int COUNT_CUTOFF = 5;
	private Hashtable<String, SuperData> oneCount;
	private int n;
	public Hashtable<String, SuperData> getOneCount() {
		return oneCount;
	}
	
	public void setOneCount(Hashtable<String, SuperData> oneCount) {
		this.oneCount = oneCount;
		n = 0;
	}
	public int getN(){
		return n;
	}
	public CountUnigram() {
		// TODO Auto-generated constructor stub
		oneCount = new Hashtable<>();
	}

	public void add(String word, Document doc){
		
			word = word.trim().replaceAll("//s+", " ").toLowerCase();
			if(oneCount.get(word) == null){
				oneCount.put(word, new SuperData(doc.getId(), 1));
				n++;
			} else {
		SuperData sd = oneCount.get(word);
		sd.setFileNames(doc.getId());
		sd.setNumberOccurrence(sd.getNumberOccurrence() + 1);
		oneCount.put(word, sd);
	}
		
	}
	private long sum;
	
	public void reload(Hashtable<String, SuperData> loadUnigram){
		oneCount = loadUnigram;
	}
	public void summary(){
		Enumeration<String> e = oneCount.keys();
		sum = 0;
		int maxCount = findMaxCount();
		while(e.hasMoreElements()){
			String temp = e.nextElement();
			SuperData sd = oneCount.get(temp);
			int count = sd.getNumberOccurrence();

			if(count <= COUNT_CUTOFF || count >= 0.9 * maxCount) {
				oneCount.remove(temp);
				n--;
			} else {
				sum += count;
			}
		}
	}
	private int findMaxCount(){
		Enumeration<String> e = oneCount.keys();
		int maxCount = -1;
		while(e.hasMoreElements()){
			String temp = e.nextElement();
			SuperData sd = oneCount.get(temp);
			int count = sd.getNumberOccurrence();
			if(count > maxCount){
				maxCount = count;
			}
		}
		return maxCount;
	}
	public long getSum(){
		return sum;
	}
	public void setN(int numberOfUnigram) {
		n = numberOfUnigram;
	}
}
