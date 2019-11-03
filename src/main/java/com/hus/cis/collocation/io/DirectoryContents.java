package com.hus.cis.collocation.io;


import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;

public class DirectoryContents {
	/**
	 *get the whole file from the directory
	 *return: list name of file
	 * */
	public static ArrayList<String> getFileTxt(String currentDirectory) {
		// TODO Auto-generated method stub
		ArrayList<String> txtPath = new ArrayList<String>();
		File directory = new File(currentDirectory);
		File[] allFilesAndDirs = directory.listFiles();
		Stack<File> fileStack = new Stack<>();
		fileStack.push(new File(currentDirectory));
		while (!fileStack.empty()){
			File file = fileStack.pop();
			if(file.isDirectory()) {
				File[] files = file.listFiles();
				for(File f : files) {
					if(f.isDirectory()) {
						fileStack.push(f);
					} else {
						if(!f.getName().contains(".aspx"))
							txtPath.add(f.getAbsolutePath());
					}
				}
			}
		}
		
		return txtPath;
	}

	public static void main(String[] args) throws IOException {
		System.out.println(getFileTxt("C:\\Users\\ADMIN\\Documents\\Medical Corpora\\HelloBacSy").size());
	}
}
