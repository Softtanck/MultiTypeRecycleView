package com.cheweishi.android.entity;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Column;

public class PessanySearchNative implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(column = "url")
	private String url;
	@Column(column = "notDeal")
	private String notDeal;
	@Column(column = "notCut")
	private String notCut;
	@Column(column = "pessanyPunish")
	private String pessanyPunish;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNotDeal() {
		return notDeal;
	}

	public void setNotDeal(String notDeal) {
		this.notDeal = notDeal;
	}

	public String getNotCut() {
		return notCut;
	}

	public void setNotCut(String notCut) {
		this.notCut = notCut;
	}

	public String getPessanyPunish() {
		return pessanyPunish;
	}

	public void setPessanyPunish(String pessanyPunish) {
		this.pessanyPunish = pessanyPunish;
	}

}
