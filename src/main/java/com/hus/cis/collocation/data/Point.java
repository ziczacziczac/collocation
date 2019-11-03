package com.hus.cis.collocation.data;

public class Point {
	private double precision, recall, b;
	private String type;
	public double getPrecision() {
		return precision;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public double getRecall() {
		return recall;
	}

	public void setRecall(double recall) {
		this.recall = recall;
	}

	public double getB() {
		return b;
	}

	public void setB(int lastPostion, int sizeOfNgram) {
		this.b = lastPostion * 1.0 / sizeOfNgram;
	}

	public Point(double precision, double recall, String type) {
		this.precision = precision;
		this.recall = recall;
		this.type = type;
	}

	@Override
	public String toString() {
		return "Point [precision= " + precision + ", recall= " + recall + ", type= " + type + "]";
	}
	
	
}
