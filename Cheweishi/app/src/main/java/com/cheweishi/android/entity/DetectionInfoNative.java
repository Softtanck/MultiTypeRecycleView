package com.cheweishi.android.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 一键检测
 * @author mingdasen
 *
 */
public class DetectionInfoNative implements Parcelable{
	private String name;//检测项名称
	private String fault;//检测项状态
	private String value;//检测项当前值
	private String type;//类型
	private String unit;//单位
	

	public String getFault() {
		return fault;
	}

	public void setFault(String fault) {
		this.fault = fault;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


	public static final Parcelable.Creator<DetectionInfoNative> CREATOR = new Creator<DetectionInfoNative>() {
        public DetectionInfoNative createFromParcel(Parcel source) {
        	DetectionInfoNative detectionInfo = new DetectionInfoNative();
        	detectionInfo.name = source.readString();  
        	detectionInfo.fault = source.readString();  
            detectionInfo.value = source.readString();  
            detectionInfo.type = source.readString();
            detectionInfo.unit = source.readString();
            return detectionInfo;  
        }  
        public DetectionInfoNative[] newArray(int size) {
            return new DetectionInfoNative[size];
        } 
	};
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int arg1) {
		parcel.writeString(name);
		parcel.writeString(fault);
		parcel.writeString(value);
		parcel.writeString(type);
		parcel.writeString(unit);
	}

}
