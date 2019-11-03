package com.hus.cis.collocation;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JOptionPane;

public class SuperData {
	private Hashtable<String, Integer> occurOnFile = new Hashtable<>();
	private Hashtable<Integer, StringBuilder> reverseOccurOnFile = new Hashtable<>();
	private int numberOccurrence = 0;
	public SuperData(String filePath, int numberOccurrence) {
		occurOnFile.put(getFileName(filePath), 1);
		this.numberOccurrence = numberOccurrence;
	}
	public SuperData(String superDataInString) {
		String[] elems = superDataInString.split("\\|");
		
		numberOccurrence = Integer.parseInt(elems[0]);
		for(int i = 1; i < elems.length; i++){
			if(elems[i].contains(":")){
				String e[] = elems[i].split(":");
				String listFileNameInString[] = e[0].split(",");
				for (String fileName : listFileNameInString) {
					try {
						occurOnFile.put(fileName, Integer.parseInt(e[1]));
					} catch(Exception ee){
						JOptionPane.showMessageDialog(null, superDataInString);
					}
				}
			}
		}
	}
	public void setReverseOccurOnFile(){
		Enumeration<String> fileNames = occurOnFile.keys();
		while(fileNames.hasMoreElements()){
			String fileName = fileNames.nextElement();
			Integer numberOfOccurrence = occurOnFile.get(fileName);
			if(reverseOccurOnFile.get(numberOfOccurrence) == null){
				reverseOccurOnFile.put(numberOfOccurrence, new StringBuilder(fileName));
			} else {
				StringBuilder listFileNameInString = reverseOccurOnFile.get(numberOfOccurrence);
				listFileNameInString.append(",");
				listFileNameInString.append(fileName);
				reverseOccurOnFile.put(numberOfOccurrence, listFileNameInString);
				
			}
		}
	}
	public String getResultSuperData() {
		String allFilesName = "";
		Enumeration<Integer> listNumberOccur = reverseOccurOnFile.keys();
		while(listNumberOccur.hasMoreElements()){
			Integer number = listNumberOccur.nextElement();
			allFilesName += reverseOccurOnFile.get(number) + ":" + number + "|";
		}
		return allFilesName;
	}
	
	public void setFileNames(String filePath) {
		String fileName = getFileName(filePath);
		if(occurOnFile.get(fileName) == null){
			occurOnFile.put(fileName, 1);
		} else {
			occurOnFile.put(fileName, occurOnFile.get(fileName) + 1);
		}
	}
	public int getNumberOccurrence() {
		return numberOccurrence;
	}
	public void setNumberOccurrence(int numberOccurrence) {
		this.numberOccurrence = numberOccurrence;
	}
	public String getFileName(String pathToFile){
		String[] elems = pathToFile.split("/");
		return elems[elems.length - 1];
	}
	public Hashtable<String, Integer> getOccurOnFile(){
		return occurOnFile;
	}
	@Override
	public String toString() {
		setReverseOccurOnFile();
		return numberOccurrence + "|" + getResultSuperData();
	}
	
}
