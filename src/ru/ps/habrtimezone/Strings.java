package ru.ps.habrtimezone;

public class Strings {
	private String str1;
	private String str2;
	public Strings()
	{
		this.setStr1("");
		this.setStr2("");
	}
	public Strings(String str1, String str2)
	{
		this.setStr1(str1);
		this.setStr2(str2);
	}
	public String getStr1() {
		return str1;
	}
	public void setStr1(String str1) {
		this.str1 = str1;
	}
	public String getStr2() {
		return str2;
	}
	public void setStr2(String str2) {
		this.str2 = str2;
	}
}
