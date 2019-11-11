package com.cheweishi.android.entity;


/**
 * 新版首页服务gridView布局对象
 * @author mingdasen
 *
 */
public class MainGridInfoNative {
	private String name;//服务名称
	private String imgUrl;//服务图标下载地址
	private Integer imgId;//资源文件图片
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
}
