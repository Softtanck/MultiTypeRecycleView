package com.cheweishi.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.baidu.mapapi.model.LatLng;

/**
 * 从本地sp中获取手机地理位置信息
 * 
 * @author zhangq
 * 
 */
public class MyMapUtils {

	public static final String LOCATION_PREFERENCES_NAME = "cws_location";

	public static void clearPreence(Context mContext) {
		SharedPreferences sp = mContext.getSharedPreferences(
				LOCATION_PREFERENCES_NAME, Activity.MODE_PRIVATE);
		sp.edit().clear().commit();
	}

	public static LatLng getLatLng(Context mContext) {
		double latitude = getLatitude(mContext);
		double longitude = getLongitude(mContext);

		LatLng latLng = new LatLng(latitude, longitude);
		return latLng;
	}

	public static double getLatitude(Context mContext) {
		SharedPreferences sp = mContext.getSharedPreferences(
				LOCATION_PREFERENCES_NAME, Activity.MODE_PRIVATE);
		double latitude = StringUtil.getDouble(sp.getString("latitude", null));
		return latitude;
	}

	public static double getLongitude(Context mContext) {
		SharedPreferences sp = mContext.getSharedPreferences(
				LOCATION_PREFERENCES_NAME, Activity.MODE_PRIVATE);
		double longitude = StringUtil
				.getDouble(sp.getString("longitude", null));
		return longitude;
	}

	public static String getCity(Context mContext) {
		return mContext.getSharedPreferences(LOCATION_PREFERENCES_NAME,
				Activity.MODE_PRIVATE).getString("city", "");
	}

	public static String getAddress(Context mContext) {
		return mContext.getSharedPreferences(LOCATION_PREFERENCES_NAME,
				Activity.MODE_PRIVATE).getString("address", "");
	}

	public static String getProvince(Context mContext) {
		return mContext.getSharedPreferences(LOCATION_PREFERENCES_NAME,
				Activity.MODE_PRIVATE).getString("province", "");
	}

	public static String getHistoryCity(Context mContext) {
		String str = mContext.getSharedPreferences(LOCATION_PREFERENCES_NAME,
				Activity.MODE_PRIVATE).getString("historyCity", "");
		if (str == null) {
			str = "";
		}
		return str;
	}

	

	/**
	 * 城市位置没有变化返回true
	 * 
	 * @param mContext
	 * @return
	 */
	public static boolean getcityChangeFlag(Context mContext) {
		return mContext.getSharedPreferences(LOCATION_PREFERENCES_NAME,
				Activity.MODE_PRIVATE).getBoolean("cityChangeFlag", false);
	}

	public static long getDistrict(Context mContext) {
		return mContext.getSharedPreferences(LOCATION_PREFERENCES_NAME,
				Activity.MODE_PRIVATE).getLong("district", 0);
	}

	public static float getRadius(Context mContext) {
		return mContext.getSharedPreferences(LOCATION_PREFERENCES_NAME,
				Activity.MODE_PRIVATE).getFloat("radius", 0);
	}

}
