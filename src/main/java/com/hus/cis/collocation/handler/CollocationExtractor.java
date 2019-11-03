package com.hus.cis.collocation.handler;

import com.hus.cis.collocation.io.DirectorySavedResult;
import com.hus.cis.collocation.io.ReadFile;
import com.hus.cis.collocation.io.WriteFile;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class CollocationExtractor {
	private Hashtable<String, Integer> top;
	private ArrayList<ArrayList<String>> list;
	private String target = DirectorySavedResult.getDirectoryToSaveResult();
	public CollocationExtractor(String ngram, String type) {
		top = new Hashtable<>();
		list = new ArrayList<>();
		getCollocation(ngram, type);
		buildTop();
	}
	
	public void getCollocation(String ngram, String type){
		ReadFile rf = new ReadFile();
		rf.open(target + "collocation-" + type + "/cands-" + ngram +"-dice.txt");
		list.add(rf.read());
		rf.close();
		
		rf.open(target + "collocation-" + type + "/cands-" + ngram +"-mle.txt");
		list.add(rf.read());
		rf.close();
		
		rf.open(target + "collocation-" + type + "/cands-" + ngram +"-pmi.txt");
		list.add(rf.read());
		rf.close();
		
		rf.open(target + "collocation-" + type + "/cands-" + ngram +"-ll.txt");
		list.add(rf.read());
		rf.close();
		
		rf.open(target + "collocation-" + type + "/cands-" + ngram +"-tscore.txt");
		list.add(rf.read());
		rf.close();
	}
	
	public void buildTop(){
		for (int i = 0; i < 1000; i++) {
			String[] col = {list.get(0).get(i).split(",")[0],
							list.get(1).get(i).split(",")[0],
							list.get(2).get(i).split(",")[0],
							list.get(3).get(i).split(",")[0],
							list.get(4).get(i).split(",")[0]
							};
			
			for (String string : col) {
				if(top.get(string) == null){
					top.put(string, 1);
				} else {
					top.put(string, top.get(string) + 1);
				}
			}
		}
	}
	
	public void printResult(){
		WriteFile wf = new WriteFile();
		wf.open(target + "top.txt");
		Enumeration<String> enumeration = top.keys();
		
		while(enumeration.hasMoreElements()){
			String key = enumeration.nextElement();
			if(top.get(key) == 5){
				wf.write(key + "\n");
			}
		}
		wf.close();
	}
	public static void main(String[] args) {
		CollocationExtractor top = new CollocationExtractor("bi", "tokenized");
		top.printResult();
	}
}
