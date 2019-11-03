package com.hus.cis.collocation.test;

import com.hus.cis.collocation.handler.validator.WordValidator;

import java.io.File;

public class TestChangeMark {
	public static void main(String[] args) {
		check();
	}
	public static long sizeInMb(String folder){
		File file = new File(folder);
		long size = 0;
		for (File child : file.listFiles()) {
			size += child.length();
		}
		return size / (1024 * 1024);
	}
	public static void check(){
		WordValidator cw = new WordValidator();
		System.out.println(cw.checkWord("sâm"));
	}
}
