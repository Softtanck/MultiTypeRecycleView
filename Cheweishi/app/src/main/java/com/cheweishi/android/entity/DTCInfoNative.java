package com.cheweishi.android.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 故障码
 * @author mingdasen
 *
 */
public class DTCInfoNative implements Parcelable{
	private String describe;//描述
	private String name;
	private String type;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public static final Parcelable.Creator<DTCInfoNative> CREATOR = new Creator<DTCInfoNative>() {
        public DTCInfoNative createFromParcel(Parcel source) {
        	DTCInfoNative dtcInfo = new DTCInfoNative();
        	dtcInfo.name = source.readString();  
        	dtcInfo.describe = source.readString();  
            return dtcInfo;  
        }  
        public DTCInfoNative[] newArray(int size) {
            return new DTCInfoNative[size];
        } 
	};
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int arg1) {
		parcel.writeString(name);
		parcel.writeString(describe);
	}
}
