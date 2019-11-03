package com.hus.cis.collocation.io;

import java.io.File;
import java.util.ArrayList;

public class DirectorySavedResult {
	private static String path;
	public static void pathToSavedResult(){
		ReadFile rf = new ReadFile();
		rf.open(".config");
		ArrayList<String> listToCreate = rf.readConfig();
		rf.close();
		path = listToCreate.get(0);
		File mainDir = new File(path);
		if(!mainDir.exists()){
			mainDir.mkdirs();
			for(int i = 1; i < listToCreate.size(); i++){
				File file = new File(path + "/" + listToCreate.get(i).trim());
				if(!file.exists()){
					file.mkdir();
				}
			}
		}
	}
	
	public static String getDirectoryToSaveResult(){
		pathToSavedResult();
		return path;
	}
}
