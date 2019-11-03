package com.hus.cis.collocation.utils;

import java.util.Hashtable;

public class FileNameEncoderUtils {
	public static Hashtable<String, Integer> hashName = new Hashtable<>();
	private static int numberOfFile = 0;
	public static String getFileEncode(String pathName){
		if(hashName.get(pathName) == null){
			hashName.put(pathName, numberOfFile ++);
			return String.valueOf(numberOfFile);
		} else {
			return String.valueOf(hashName.get(pathName));
		}
	}
}
