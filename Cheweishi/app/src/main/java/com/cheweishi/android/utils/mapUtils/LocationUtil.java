package com.cheweishi.android.utils.mapUtils;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

/**
 * 定位工具
 * 
 * @author zhangq
 * 
 */
public class LocationUtil {
	/**
	 * 定位间隔类型-长
	 */
	public static final int SCANSPAN_TYPE_LONG = 1001;
	/**
	 * 定位间隔类型-短
	 */
	public static final int SCANSPAN_TYPE_SHORT = 1002;
	/**
	 * 定位时间-长,
	 */
	private static final int SCANSPAN_LONG = 30 * 1000;
	/**
	 * 定位时间-短
	 */
	private static final int SCANSPAN_SHORT = 5 * 1000;

	private LocationClient mlocClient = null;

	private BDLocationListener mListener;

	/**
	 * 
	 * @param context
	 * @param scanSpanType
	 *            {@link SCANSPAN_TYPE_SHORT }
	 * @param listener
	 */
	public LocationUtil(Context context, int scanSpanType,
			BDLocationListener listener) {
		this.mListener = listener;
		if (mlocClient == null) {
			syncInit(context);
		}
		mlocClient.registerLocationListener(mListener);
		if (scanSpanType == SCANSPAN_TYPE_SHORT) {
			setLocationOptions(SCANSPAN_SHORT);
		} else {
			setLocationOptions(SCANSPAN_LONG);
		}

	}

	/**
	 * 同步创建定位实例
	 * 
	 * @param context
	 */
	private synchronized void syncInit(Context context) {
		if (mlocClient == null) {
			mlocClient = new LocationClient(context);
		}
	}

	/**
	 * 设置
	 * 
	 * @param scanSpan
	 *            定位间隔
	 * 
	 */
	public void setLocationOptions(int scanSpan) {
		LocationClientOption options = new LocationClientOption();
		options.setLocationMode(LocationMode.Hight_Accuracy);
		options.setCoorType("bd09ll");
		options.setProdName("cheweishi");
		options.setScanSpan(scanSpan);
		options.setIsNeedAddress(true);
		options.setNeedDeviceDirect(true);
		mlocClient.setLocOption(options);

		mlocClient.requestLocation();
	}

	public void onStart() {
		if (!mlocClient.isStarted()) {
			mlocClient.start();
		}
	}

	public void onStop() {
		if (mlocClient != null) {
			mlocClient.stop();
		}
	}

	public void onDestory() {
		if (mlocClient == null) {
			return;
		}
		mlocClient.unRegisterLocationListener(mListener);
		mListener = null;
		mlocClient = null;
	}
}
