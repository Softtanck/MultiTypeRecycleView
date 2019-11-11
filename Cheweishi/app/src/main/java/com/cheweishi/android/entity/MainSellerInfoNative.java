package com.cheweishi.android.entity;

import java.util.List;

/**
 * 首页底部商家信息
 * @author mingdasen
 *
 */
public class MainSellerInfoNative {
	private String id;
	private String imgUrl;//商家图片url
	private Integer imgId;//商家图片id
	private String name;//商家名称
	private int evaluateImg;//评价星级img
	private String evaluate;//评价
	private String address;//地址
	private String distance;//距离
	private String appoint;//指定
	private String nearby;//附近
	private double lat;//纬度
	private double lon;//经度
	private List<MainSellerServiceInfoNative> services;//服务内容列表
	
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public Integer getImgId() {
		return imgId;
	}
	public void setImgId(Integer imgId) {
		this.imgId = imgId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getEvaluateImg() {
		return evaluateImg;
	}
	public void setEvaluateImg(int evaluateImg) {
		this.evaluateImg = evaluateImg;
	}
	public String getEvaluate() {
		return evaluate;
	}
	public void setEvaluate(String evaluate) {
		this.evaluate = evaluate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getAppoint() {
		return appoint;
	}
	public void setAppoint(String appoint) {
		this.appoint = appoint;
	}
	public String getNearby() {
		return nearby;
	}
	public void setNearby(String nearby) {
		this.nearby = nearby;
	}
	public List<MainSellerServiceInfoNative> getServices() {
		return services;
	}
	public void setServices(List<MainSellerServiceInfoNative> services) {
		this.services = services;
	}
}
