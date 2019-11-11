package com.cheweishi.android.entity;

import com.lidroid.xutils.db.annotation.Column;

public class DrvingScoreNative {
	private String mile;
	private long feeTime;
	private int drivingScore;

	private int rapidAcceleration;// 急加速次数
	private int brokenOn;// 急刹车次数
	private int suddenTurn;// 急转弯次数
	private int fatigueDriving;// 疲劳驾驶次数

	public int getRapidAcceleration() {
		return rapidAcceleration;
	}

	public void setRapidAcceleration(int rapidAcceleration) {
		this.rapidAcceleration = rapidAcceleration;
	}

	public int getBrokenOn() {
		return brokenOn;
	}

	public void setBrokenOn(int brokenOn) {
		this.brokenOn = brokenOn;
	}

	public int getSuddenTurn() {
		return suddenTurn;
	}

	public void setSuddenTurn(int suddenTurn) {
		this.suddenTurn = suddenTurn;
	}

	public int getFatigueDriving() {
		return fatigueDriving;
	}

	public void setFatigueDriving(int fatigueDriving) {
		this.fatigueDriving = fatigueDriving;
	}

	public int getDrivingScore() {
		return drivingScore;
	}

	public void setDrivingScore(int drivingScore) {
		this.drivingScore = drivingScore;
	}

	public String getMile() {
		return mile;
	}

	public void setMile(String mile) {
		this.mile = mile;
	}

	public long getFeeTime() {
		return feeTime;
	}

	public void setFeeTime(long feeTime) {
		this.feeTime = feeTime;
	}

}
