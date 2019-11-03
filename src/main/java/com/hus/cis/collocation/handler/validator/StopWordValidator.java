package com.hus.cis.collocation.handler.validator;

import com.hus.cis.collocation.io.ReadFile;

import java.util.ArrayList;
import java.util.Hashtable;

public class StopWordValidator {
	private Hashtable<String, Integer> stopWords = new Hashtable<>();
	private String file = "NLP_DATA/charAndSyllables/Stopwords.txt";
	ReadFile read;
	private void readStopWords(){
		read = new ReadFile();
		read.open(file);
		ArrayList<String> lines = read.read();
		read.close();
		for (String string : lines) {
			string = string.trim();
			string = string.replaceAll("//s+", " ");
			stopWords.put(string, 1);
		}
	}
	
	public boolean isStopWord(String s){
		s=s.trim();
		s.replaceAll(" ", "");
		return stopWords.containsKey(s);
	}

	public StopWordValidator() {
		readStopWords();
	}
}
