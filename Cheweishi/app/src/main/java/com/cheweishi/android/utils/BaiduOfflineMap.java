package com.cheweishi.android.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;


/***
 * 百度地图工具类
 * @author Administrator
 *
 */

public class BaiduOfflineMap implements MKOfflineMapListener {

	private Activity  activity;
	private MKOfflineMap mOffline = null;
	
	private String cityID;

	public BaiduOfflineMap(Context context) {
		// TODO Auto-generated constructor stub
		activity= (Activity) context;
		mOffline = new MKOfflineMap();
		mOffline.init(this);
	}

	 public boolean getCityList(String cityname){
			ArrayList<MKOLSearchRecord> records1 = mOffline.getHotCityList();
			if (records1 != null) {
				for (MKOLSearchRecord r : records1) {
				if (r.cityName.equals(cityname)) {
					cityID=String.valueOf(r.cityID)	;
					return true;
				}
				}
			}
			return false;
	 }

	public void start() {
			int cityid = Integer.parseInt(cityID);
			mOffline.start(cityid);
			Toast.makeText(activity, "开始下载离线地图.", Toast.LENGTH_SHORT)
					.show();
//			updateView();
		} 
	 
	@Override
	public void onGetOfflineMapState(int type, int state) {
		// TODO Auto-generated method stub
		switch (type) {
		case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
			MKOLUpdateElement update = mOffline.getUpdateInfo(state);
			// 处理下载进度更新提示
			if (update != null) {
			}
		}
			break;
		case MKOfflineMap.TYPE_NEW_OFFLINE:
			// 有新离线地图安装
			break;
		case MKOfflineMap.TYPE_VER_UPDATE:
			// 版本更新提示
			 MKOLUpdateElement e = mOffline.getUpdateInfo(state);

			break;
		}

	}

}
