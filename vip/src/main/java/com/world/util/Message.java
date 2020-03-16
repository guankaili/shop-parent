package com.world.util;

import com.Lan;

public class Message {
	
	private boolean isSuc = false;
	private String msg;
	private String data;
	private int code  = 1;
	
	public boolean isSuc() {
		return isSuc;
	}
	public void setSuc(boolean isSuc) {
		this.isSuc = isSuc;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	//传入语言类型，进行国际化
	public void setMsg(String lan, String msg, String ... p) {
		this.msg = Lan.LanguageFormat(lan,msg,p);
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
}
