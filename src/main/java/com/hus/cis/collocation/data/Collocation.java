package com.hus.cis.collocation.data;

public class Collocation {
	private String name;
	private double measure;
	
	public Collocation() {
		// TODO Auto-generated constructor stub
	}
	
	public Collocation(String name, double measure) {
		this.name = name;
		this.measure = measure;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getMeasure() {
		return measure;
	}

	public void setMeasure(double measure) {
		this.measure = measure;
	}
	
	
}
