package com.cheweishi.android.entity;

import java.io.Serializable;
/**
 * checkbox 判断
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class CheckEntityNative implements Serializable {
	public boolean isCheck;

	public CheckEntityNative(boolean isCheck) {
		super();
		this.isCheck = isCheck;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	
}
