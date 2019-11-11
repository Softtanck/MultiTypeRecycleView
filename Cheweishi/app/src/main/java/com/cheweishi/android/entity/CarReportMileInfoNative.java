package com.cheweishi.android.entity;
/**
 * 单个驾驶里程数信息
 * @author mingdasen
 *
 */
public class CarReportMileInfoNative {
	private int start;//开始时间(分)
	private int end;//结束时间(分)
	private float driverMile;//驾驶里程
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	public float getDriverMile() {
		return driverMile;
	}
	public void setDriverMile(float driverMile) {
		this.driverMile = driverMile;
	}
}
