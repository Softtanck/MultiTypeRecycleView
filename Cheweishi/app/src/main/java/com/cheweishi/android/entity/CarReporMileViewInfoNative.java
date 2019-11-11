package com.cheweishi.android.entity;

/**
 * 车辆报告中的驾驶里程动画里程柱形图信息
 * @author mingdasen
 *
 */
public class CarReporMileViewInfoNative {
	private float Xleft;
	private float XRinth;
	private float YTop;
	private float YBottm;
	private float width;
	private float heigth;
	private String startTime;
	private String endTime;
	private String driverMile;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDriverMile() {
		return driverMile;
	}

	public void setDriverMile(String driverMile) {
		this.driverMile = driverMile;
	}

	public float getXleft() {
		return Xleft;
	}

	public void setXleft(float xleft) {
		Xleft = xleft;
	}

	public float getXRinth() {
		return XRinth;
	}

	public void setXRinth(float xRinth) {
		XRinth = xRinth;
	}

	public float getYTop() {
		return YTop;
	}

	public void setYTop(float yTop) {
		YTop = yTop;
	}

	public float getYBottm() {
		return YBottm;
	}

	public void setYBottm(float yBottm) {
		YBottm = yBottm;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeigth() {
		return heigth;
	}

	public void setHeigth(float heigth) {
		this.heigth = heigth;
	}

}
