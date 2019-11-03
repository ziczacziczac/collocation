package com.hus.cis.collocation.data;

import com.hus.cis.collocation.io.ReadFile;

import java.util.ArrayList;

public class ListCollocation {
	private ArrayList<Collocation> listCollocation = new ArrayList<>();
	private String type;
	private ReadFile rf;
	
	public ListCollocation(String fileName) {
		readCollocation(fileName);
		type = getTypeFromFileName(fileName);
	}

	private void readCollocation(String fileName){
		rf = new ReadFile();
		rf.open(fileName);
		ArrayList<String> list = rf.read();
		rf.close();
		
		for (String line : list) {
			String[] elems = line.split(",");
			Collocation coll = new Collocation(elems[0], Double.parseDouble(elems[1]));
			listCollocation.add(coll);
		}
	}
	
	private String getTypeFromFileName(String fileName){
		String[] elems = fileName.split("/");
		return elems[elems.length - 1].replaceAll(".txt", "");
	}

	public ArrayList<Collocation> getListCollocation() {
		return listCollocation;
	}

	public String getType() {
		return type;
	}
	
}
