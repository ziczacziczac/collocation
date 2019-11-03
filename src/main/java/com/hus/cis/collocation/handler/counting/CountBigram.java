package com.hus.cis.collocation.handler.counting;

import com.hus.cis.collocation.SuperData;
import com.hus.cis.collocation.data.Document;
import com.hus.cis.collocation.io.WriteFile;

import java.util.Enumeration;
import java.util.Hashtable;


public class CountBigram{

	private static final Integer COUNT_CUTOFF = 5;
	private Hashtable<String, Hashtable<String, SuperData>> bigram;
	public Hashtable<String, Hashtable<String, SuperData>> getBigram() {
		return bigram;
	}
	public void setBigram(Hashtable<String, Hashtable<String, SuperData>> bigram) {
		this.bigram = bigram;
	}

	private int n;
	private WriteFile write;
	public CountBigram() {
		bigram = new Hashtable<>();
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
		if(bigram.get(word1) == null){
			Hashtable<String, SuperData> value = new Hashtable<>();
			value.put(word2, new SuperData(doc.getId(), 1));
			bigram.put(word1, value);
			n++;
		} else {
			if (bigram.get(word1).get(word2) == null) {
				bigram.get(word1).put(word2, new SuperData(doc.getId(), 1));
				n++;
			} else {
				Hashtable<String, SuperData> value = new Hashtable<>();
				value = bigram.get(word1);
				SuperData sd = value.get(word2);
				sd.setFileNames(doc.getId());
				sd.setNumberOccurrence(sd.getNumberOccurrence() + 1);
				value.put(word2, sd);
				bigram.put(word1, value);
			}
		}
	}
	private long sum = 0;
	public void summary(){
		
		Enumeration<String> e = bigram.keys();
		while(e.hasMoreElements()){
			String temp = e.nextElement();
			Enumeration<String> e2 = bigram.get(temp).keys();
			while(e2.hasMoreElements()){
				String tempValue = e2.nextElement();
				SuperData sd = bigram.get(temp).get(tempValue);
				Integer val = sd.getNumberOccurrence();
				if(val >= COUNT_CUTOFF){
					sum += val;
				} else {
					bigram.get(temp).remove(tempValue);
					n--;
				}
				
			}
			if(bigram.get(temp).isEmpty()){
				bigram.remove(temp);
			}
		}
		
	}
	public long getSum(){
		return sum;
	}
	public void writeBigramToFile(CountUnigram one,String fileOutbigram){
		write = new WriteFile();
		write.open(fileOutbigram);
		String res = n+"\n";
		Enumeration<String> e = bigram.keys();
		while(e.hasMoreElements()){
			String temp = e.nextElement();
			Enumeration<String> e2 = bigram.get(temp).keys();
			String resValue = "";
			while(e2.hasMoreElements()){
				String tempValue = e2.nextElement();
				Integer val = bigram.get(temp).get(tempValue).getNumberOccurrence();
				Integer fA = one.getOneCount().get(temp).getNumberOccurrence();
				Integer fB = one.getOneCount().get(tempValue).getNumberOccurrence();
				String onFiles = bigram.get(temp).get(tempValue).getResultSuperData();
				
				resValue += temp +" " + tempValue + "," + val +","+ fA + ","+ fB + "," + onFiles + "\n";
			}
			res += resValue;
		}
		write.write(res);
		write.close();
	}
	
	public void loadBigram(Hashtable<String, Hashtable<String, SuperData>> loadBigram){
		bigram = loadBigram;
	}
	
}
