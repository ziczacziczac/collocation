package com.hus.cis.collocation.handler.validator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

public class SyllablesValidator {
	private static final String SYLLABLES = "NLP_DATA/charAndSyllables/Vietnamese syllables.txt";
	private Hashtable<String, Integer> syllables;
	public SyllablesValidator() {
		syllables = new Hashtable<>();
		// TODO Auto-generated constructor stub
		readSyllable();
	}
	public void readSyllable(){
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(SYLLABLES)));
			String line = "";
			while((line = br.readLine()) != null){
				syllables.put(line, 1);
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean isSyllables(String word){
		
		return syllables.get(word) != null;
	}
}
