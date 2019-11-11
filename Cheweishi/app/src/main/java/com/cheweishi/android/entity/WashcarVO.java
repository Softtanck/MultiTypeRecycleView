package com.cheweishi.android.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 洗车店
 * 
 * @author zhangq
 * 
 */
public class WashcarVO implements Parcelable {
	private String cwId;
	private String des;
	private String lon;
	private String address;
	private String tel;
	private String name;
	private String mile;
	private String pic;
	private String lat;
	private String date;
	private String time;
	private String carNumber;
	/**
	 * 下单后才有
	 */
	private String uno;

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getCwId() {
		return cwId;
	}

	public void setCwId(String cwId) {
		this.cwId = cwId;
	}

	public String getUno() {
		return uno;
	}

	public void setUno(String uno) {
		this.uno = uno;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMile() {
		return mile;
	}

	public void setMile(String mile) {
		this.mile = mile;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(address);
		dest.writeString(cwId);
		dest.writeString(des);
		dest.writeString(lat);
		dest.writeString(lon);
		dest.writeString(mile);
		dest.writeString(name);
		dest.writeString(pic);
		dest.writeString(tel);
		dest.writeString(uno);
		dest.writeString(date);
		dest.writeString(time);
		dest.writeString(carNumber);
	}

	public static final Parcelable.Creator<WashcarVO> CREATOR = new Creator<WashcarVO>() {

		@Override
		public WashcarVO[] newArray(int size) {

			return null;
		}

		@Override
		public WashcarVO createFromParcel(Parcel source) {
			WashcarVO vo = new WashcarVO();
			vo.address = source.readString();
			vo.cwId = source.readString();
			vo.des = source.readString();
			vo.lat = source.readString();
			vo.lon = source.readString();
			vo.mile = source.readString();
			vo.name = source.readString();
			vo.pic = source.readString();
			vo.tel = source.readString();
			vo.uno = source.readString();
			vo.date = source.readString();
			vo.time = source.readString();
			vo.carNumber = source.readString();
			return vo;
		}
	};

}
