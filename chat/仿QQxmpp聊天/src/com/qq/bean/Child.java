package com.qq.bean;

import java.io.Serializable;


import android.text.TextUtils;


@SuppressWarnings("serial")
public class Child implements Serializable{
	private String username;
	private String mood;
	
	
	public String getMood() {
		return mood;
	}
	public void setMood(String mood) {
		this.mood = mood;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
}
}