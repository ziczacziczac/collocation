package com.hus.cis.collocation.handler.counting;

import com.hus.cis.collocation.data.Document;
import com.hus.cis.collocation.SuperData;

import java.util.Enumeration;
import java.util.Hashtable;

public class Count4gram {
	private static final Integer COUNT_CUTOFF = 5;
	private Hashtable<String, Hashtable<String, SuperData>> fourgram;
	public Hashtable<String, Hashtable<String, SuperData>> get4gram() {
		return fourgram;
	}
	public void set4gram(Hashtable<String, Hashtable<String, SuperData>> fourgram) {
		this.fourgram = fourgram;
	}

	private int n;
	public Count4gram() {
		fourgram = new Hashtable<>();
		n = 0;
	}
	public int getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}

	public void add(String word1, String word2, Document doc){
		word1 = word1.toLowerCase();
		word2 = word2.toLowerCase();
		if(fourgram.get(word1) == null){
			Hashtable<String, SuperData> value = new Hashtable<>();
			value.put(word2, new SuperData(doc.getId(), 1));
			fourgram.put(word1, value);
			n++;
		} else {
			if (fourgram.get(word1).get(word2) == null) {
				fourgram.get(word1).put(word2, new SuperData(doc.getId(), 1));
				n++;
			} else {
				Hashtable<String, SuperData> value = new Hashtable<>();
				value = fourgram.get(word1);
				SuperData sd = value.get(word2);
				sd.setFileNames(doc.getId());
				sd.setNumberOccurrence(sd.getNumberOccurrence() + 1);
				value.put(word2, sd);
				fourgram.put(word1, value);
			}
		}
	}
	private long sum = 0;
	public void sumary(){
		
		Enumeration<String> e = fourgram.keys();
		while(e.hasMoreElements()){
			String temp = e.nextElement();
			Enumeration<String> e2 = fourgram.get(temp).keys();
			while(e2.hasMoreElements()){
				String tempValue = e2.nextElement();
				SuperData sd = fourgram.get(temp).get(tempValue);
				Integer val = sd.getNumberOccurrence();
				if(val >= COUNT_CUTOFF){
					sum += val;
				} else {
					fourgram.get(temp).remove(tempValue);
					n--;
				}
				
			}
			if(fourgram.get(temp).isEmpty()){
				fourgram.remove(temp);
			}
		}
		
	}
	public long getSum(){
		return sum;
	}
	public void loadBigram(Hashtable<String, Hashtable<String, SuperData>> fourgram){
		this.fourgram = fourgram;
	}
	public void get(String w1, String w2){
		SuperData sd = fourgram.get(w1).get(w2);
		System.out.println(sd.toString());
	}
}
