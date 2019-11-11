package com.cheweishi.android.entity;
/**
 * 车动态数据
 * @author zhangq
 *
 */
public class CarDynamicVONative {
	private String azimuth;// 方位角
	private String iconurl;//
	private String lat;
	private String lon;
	private String mile;
	private String obdtime;
	private String obdifc;//百公里油耗
	private String speed;
	private String status;
	private String tele;

	public String getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(String azimuth) {
		this.azimuth = azimuth;
	}

	public String getIconurl() {
		return iconurl;
	}

	public void setIconurl(String iconurl) {
		this.iconurl = iconurl;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getMile() {
		return mile;
	}

	public void setMile(String mile) {
		this.mile = mile;
	}

	public String getObdtime() {
		return obdtime;
	}

	public void setObdtime(String obdtime) {
		this.obdtime = obdtime;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTele() {
		return tele;
	}

	public void setTele(String tele) {
		this.tele = tele;
	}

	public String getObdifc() {
		return obdifc;
	}

	public void setObdifc(String obdifc) {
		this.obdifc = obdifc;
	}

}
