package com.hus.cis.collocation.data;

import com.hus.cis.collocation.io.WriteFile;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class Cluster {
	private Hashtable<Integer, ArrayList<String>> listCluster;
	
	public Cluster() {
		listCluster = new Hashtable<>();
	}
	
	public void add(Integer lable, String ngram){
		if(listCluster.get(lable) != null){
			listCluster.get(lable).add(ngram);
		} else {
			ArrayList<String> list = new ArrayList<>();
			list.add(ngram);
			listCluster.put(lable, list);
		}
	}
	
	public void write(String fileName){
		WriteFile wf = new WriteFile();
		wf.open(fileName);
		Enumeration<Integer> keys = listCluster.keys();
		while(keys.hasMoreElements()){
			Integer label = keys.nextElement();
			ArrayList<String> s = listCluster.get(label);
			if(s.size() >= 5){
				String listInString = arrayListToString(s);
				wf.write(label + "\n" + listInString);
				wf.write("\n\n******************************\n\n");
			}
		}
		wf.close();
	}
	
	public String arrayListToString(ArrayList<String> list){
		String sb = "";
		System.out.println(list.size());
		for (String string : list) {
			sb += string + ",";
		}
		return sb.toString();
	}
}
