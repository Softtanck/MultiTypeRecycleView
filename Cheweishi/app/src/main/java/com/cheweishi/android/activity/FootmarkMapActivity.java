package com.cheweishi.android.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.cheweishi.android.R;
import com.cheweishi.android.config.Config;
import com.cheweishi.android.utils.DisplayUtil;
import com.cheweishi.android.utils.StringUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;

public class FootmarkMapActivity extends BaseActivity {

	@ViewInject(R.id.footmark_baidumap)
	private MapView mMapView;
	@ViewInject(R.id.left_action)
	private Button mleftAction;

	@ViewInject(R.id.title)
	private TextView mCenterTextView;
	private BitmapDescriptor bitmap = BitmapDescriptorFactory
			.fromResource(R.drawable.zuji_dian);
	private BaiduMap mBaiduMap;
	
	
	private GeoCoder mSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_footmarkmap);
		ViewUtils.inject(this);
		//初始化反编译对象
		mSearch = GeoCoder.newInstance();
		mBaiduMap = mMapView.getMap();
		Intent intent = getIntent();
		String latlon = intent.getStringExtra("latlon");
		String isFootmark=intent.getStringExtra("DrivingBehavior");
		if (isFootmark!=null&&isFootmark.equals("DrivingBehavior")) {
			mCenterTextView.setText("详细位置");
		}else {
			mCenterTextView.setText("足迹详情");
		}
		mleftAction.setText(getResources().getString(R.string.back));

		mleftAction.setOnClickListener(listener);
		try {
			JSONObject jsonObject = new JSONObject(latlon);
			jsonArray(jsonObject);
		} catch (JSONException e) {
			analysis(latlon);
		}
		init();
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			FootmarkMapActivity.this.finish();
		}
	};

	private void analysis(String latlon) {
		// TODO Auto-generated method stub
		String[] latlons = latlon.split(",");
		double lon = StringUtil.getDouble(latlons[0]);
		double lat = StringUtil.getDouble(latlons[1]);
		LatLng latLng = new LatLng(lat, lon);
		moveLatLng(latLng);
	}

	private void init() {
		// TODO Auto-generated method stub
	
		mSearch.setOnGetGeoCodeResultListener(onGetGeoCoderResultListener);
		String url = "";
		mBaiduMap.setOnMarkerClickListener(onMarkerClickListener);
		RequestParams params = new RequestParams(HTTP.UTF_8);
		// MyHttpUtils myHttpUtils=new MyHttpUtils(FootmarkMapActivity.this,
		// params, url, handler);
		
	}

	List<LatLng> list = new ArrayList<LatLng>();

	protected void jsonArray(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		JSONObject jsonObject2 = jsonObject.optJSONObject("body");
		JSONArray jsonArray = jsonObject2.optJSONArray("results");
		//
		if (jsonArray == null) {
			return;
		} else {
			if (jsonArray.length() > 1) {
				for (int i = 0; i < jsonArray.length(); i++) {
					double lat = jsonArray.optJSONObject(i).optDouble("lat");
					double lon = jsonArray.optJSONObject(i).optDouble("lon");
					LatLng latLng = new LatLng(lat, lon);
					list.add(latLng);
				}
				float f = mBaiduMap.getMaxZoomLevel();// 19.0 最小比例尺
				MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLngZoom(
						list.get(0), f - 3);
				mBaiduMap.animateMapStatus(u1);
				BitmapDescriptor bitmap1 = BitmapDescriptorFactory
						.fromResource(R.drawable.zuji_qidian);
				OverlayOptions option1 = new MarkerOptions().position(
						list.get(0)).icon(bitmap1);
				mBaiduMap.addOverlay(option1);
				OverlayOptions ooPolyline = new PolylineOptions().width(6)
						.color(Color.RED).points(list);
				mBaiduMap.addOverlay(ooPolyline);
				BitmapDescriptor bitmap = BitmapDescriptorFactory
						.fromResource(R.drawable.zuji_zhongdian);
				OverlayOptions option = new MarkerOptions().position(
						list.get(list.size() - 1)).icon(bitmap);
				mBaiduMap.addOverlay(option);

			} else {
				Toast.makeText(this, "无数据", Toast.LENGTH_SHORT).show();
			}
		}

	}


	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
//		MobclickAgent
//				.onPageStart(FootmarkMapActivity.this.getClass().getName());
//		MobclickAgent.onResume(this);
	}

	@Override
	protected void onDestroy() {
		bitmap.recycle();
		mBaiduMap.clear();
		mMapView.onDestroy();
		super.onDestroy();
	}


	//
	private void moveLatLng(LatLng latlng) {
		OverlayOptions option = new MarkerOptions().position(latlng).icon(
				bitmap);
		mBaiduMap.addOverlay(option);
		float f = mBaiduMap.getMaxZoomLevel();// 19.0 最小比例尺
		MapStatusUpdate u1 = MapStatusUpdateFactory
				.newLatLngZoom(latlng, f - 3);
		mBaiduMap.animateMapStatus(u1);
	}
	
	private OnGetGeoCoderResultListener onGetGeoCoderResultListener=new OnGetGeoCoderResultListener() {
		
		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
			// TODO Auto-generated method stub
			String address =arg0.getAddress();
			Button button = new Button(getApplicationContext());
			button.setBackgroundResource(R.drawable.dizhitanchu);
			LatLng ll = arg0.getLocation();
			button.setText(address);
			button.setTextColor(Color.BLACK);
			button.setTextSize(12);
			InfoWindow mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47,null);
			mBaiduMap.showInfoWindow(mInfoWindow);
		}
		
		@Override
		public void onGetGeoCodeResult(GeoCodeResult arg0) {
			
		}
	};
	OnMarkerClickListener onMarkerClickListener=new OnMarkerClickListener() {
		
		@Override
		public boolean onMarkerClick(Marker arg0) {
			// TODO Auto-generated method stub
			mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(arg0.getPosition()));
			return true;
		}
	};

}
