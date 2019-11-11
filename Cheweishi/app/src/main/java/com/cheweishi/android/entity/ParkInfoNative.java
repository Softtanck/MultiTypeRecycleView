package com.cheweishi.android.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 车位列表对象
 * 
 * @author mingdasen
 * 
 */
public class ParkInfoNative implements Parcelable{
	private int total;// 总车位
	private String parkID;// 停车场唯一标识
	private String picUrl;// 停车场照片url
	private String cityName;// 所属城市名
	private String addr;// 停车场地址
	private int leftNum;// 剩余车位
	private String priceUnit;// 价格单位
	private int distance;// 距离请求坐标点的距离(单位：米)
	private String price;// 价格
	private String areaName;// 所属区名
	private String name;// 停车场名称
	private String longitude;// 停车场经度
	private String latitude;// 停车场纬度
	private String priceDesc;//收费详情

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getParkID() {
		return parkID;
	}

	public void setParkID(String parkID) {
		this.parkID = parkID;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public int getLeftNum() {
		return leftNum;
	}

	public void setLeftNum(int leftNum) {
		this.leftNum = leftNum;
	}

	public String getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getPriceDesc() {
		return priceDesc;
	}

	public void setPriceDesc(String priceDesc) {
		this.priceDesc = priceDesc;
	}
	
	public static final Parcelable.Creator<ParkInfoNative> CREATOR = new Creator<ParkInfoNative>() {
        public ParkInfoNative createFromParcel(Parcel source) {
        	ParkInfoNative parkInfo = new ParkInfoNative();
        	parkInfo.total = source.readInt();  
        	parkInfo.parkID = source.readString();  
        	parkInfo.picUrl = source.readString();  
        	parkInfo.cityName = source.readString();
        	parkInfo.addr = source.readString();
        	parkInfo.leftNum = source.readInt();
        	parkInfo.priceUnit = source.readString();
        	parkInfo.distance = source.readInt();
        	parkInfo.price = source.readString();
        	parkInfo.areaName = source.readString();
        	parkInfo.name = source.readString();
        	parkInfo.longitude = source.readString();
        	parkInfo.latitude = source.readString();
        	parkInfo.priceDesc = source.readString();
            return parkInfo;  
        }  
        public ParkInfoNative[] newArray(int size) {
            return new ParkInfoNative[size];
        } 
	};
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int arg1) {
		parcel.writeInt(total);
		parcel.writeString(parkID);
		parcel.writeString(picUrl);
		parcel.writeString(cityName);
		parcel.writeString(addr);
		parcel.writeInt(leftNum);
		parcel.writeString(priceUnit);
		parcel.writeInt(distance);
		parcel.writeString(price);
		parcel.writeString(areaName);
		parcel.writeString(name);
		parcel.writeString(longitude);
		parcel.writeString(latitude);
		parcel.writeString(priceDesc);
	}
}
