package com.cheweishi.android.entity;

import com.baidu.mapapi.model.LatLng;

public class LatlngBeanNative {

	private LatLng  latLng;

	public LatLng getLatLng() {
		return latLng;
	}

	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}

	@Override
	public String toString() {
		return this.latLng.latitude+","+latLng.longitude;
	}
}
