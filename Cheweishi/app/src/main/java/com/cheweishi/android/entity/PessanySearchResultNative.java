package com.cheweishi.android.entity;

import com.lidroid.xutils.db.annotation.Column;

public class PessanySearchResultNative {
	@Column(column = "pessany_road")
	private String pessany_road;
	@Column(column = "pessany_reason")
	private String pessany_reason;
	@Column(column = "pessany_time")
	private String pessany_time;

	public String getPessany_road() {
		return pessany_road;
	}

	public void setPessany_road(String pessany_road) {
		this.pessany_road = pessany_road;
	}

	public String getPessany_reason() {
		return pessany_reason;
	}

	public void setPessany_reason(String pessany_reason) {
		this.pessany_reason = pessany_reason;
	}

	public String getPessany_time() {
		return pessany_time;
	}

	public void setPessany_time(String pessany_time) {
		this.pessany_time = pessany_time;
	}

}
