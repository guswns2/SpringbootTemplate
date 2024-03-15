package com.jhj.template.utils;

public class StringBuilderPlus {
	private StringBuilder sb;

	public StringBuilderPlus() {
		sb = new StringBuilder();
	}

	public void append(String str) {
		sb.append(str != null ? str : "");
	}

	public void appendLine(String str) {
		sb.append(str != null ? str : "").append(System.getProperty("line.separator"));
	}

	public String toString() {
		return sb.toString();
	}

	public void insert(int offset, String str) {
		sb.insert(offset, str);
	}

	public void replace(int start, int end, String str) {
		sb.replace(start, end, str);
	}

	public void delete(int start, int end) {
		sb.delete(start, end);
	}

	public void deleteCharAt(int index) {
		sb.deleteCharAt(index);
	}

	public void reverse() {
		sb.reverse();
	}
}
