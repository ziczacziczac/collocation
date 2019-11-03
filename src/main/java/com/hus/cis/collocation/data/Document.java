package com.hus.cis.collocation.data;

import com.hus.cis.collocation.utils.FileNameEncoderUtils;

import java.util.ArrayList;

public class Document{
	private String id;
	private ArrayList<String> content;
	
	public Document(String id, ArrayList<String> content) {
		this.id = id;
		this.content = content;
	}
	public Document() {
	}
	public String getId() {
		return FileNameEncoderUtils.getFileEncode(id);
	}
	public void setId(String id) {
		this.id = id;
	}
	public ArrayList<String> getContent() {
		return content;
	}
	public void setContent(ArrayList<String> content) {
		this.content = content;
	}
}
