package com.cheweishi.android.utils.mapUtils;

import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.model.LatLngBounds.Builder;

import android.graphics.Color;
import android.util.Log;

/**
 * 基础地图设置<br>
 * 定位sdk:v6.0.5 基础地图sdk:v3.5.0 导航sdk:v2.0.0
 * 
 * @author zhangq
 * 
 */
public class BaseMapUtil {
	/**
	 * 地图默认缩放级别16f--200m
	 */
	public static final float DEFAULT_ZOOM_LEVEL = 16f;

	/**
	 * 地图轨迹线条默认颜色
	 */
	private static final int DEFAULT_POLYLINE_COLOR = Color.rgb(255, 0, 0);

	private BaiduMap mBaiduMap;
	private OnMarkerClickListener listener;

	public BaseMapUtil(BaiduMap mBaiduMap) {
		this.mBaiduMap = mBaiduMap;
	}

	/**
	 * 默认地图 显示
	 */
	public void setUI() {
		setUI(false, false, false, true, true);
	}

	/**
	 * 默认状态设置
	 */
	public void setMapStatus() {
		MapStatus status = new MapStatus.Builder().zoom(DEFAULT_ZOOM_LEVEL)
				.build();
		setMapStatus(true, MapStatusUpdateFactory.newMapStatus(status));
	}

	/**
	 * 设置地图显示
	 * 
	 * @param compassEnable
	 *            是否有指南针(设置false旋转后任然有)
	 * @param overLookGestureEnable
	 *            俯视手势
	 * @param rotateGestureEnable
	 *            旋转手势
	 * @param scrollGestureEnable
	 *            平移手势
	 * @param zoomGestureEnable
	 *            缩放手势
	 */
	public void setUI(boolean compassEnable, boolean overLookGestureEnable,
			boolean rotateGestureEnable, boolean scrollGestureEnable,
			boolean zoomGestureEnable) {
		/* 指南针 */
		mBaiduMap.getUiSettings().setCompassEnabled(compassEnable);

		/* 俯视手势 */
		mBaiduMap.getUiSettings().setOverlookingGesturesEnabled(
				overLookGestureEnable);
		/* 旋转手势 */
		mBaiduMap.getUiSettings().setRotateGesturesEnabled(rotateGestureEnable);
		/* 平移手势 */
		mBaiduMap.getUiSettings().setScrollGesturesEnabled(scrollGestureEnable);
		/* 缩放手势 */
		mBaiduMap.getUiSettings().setZoomGesturesEnabled(zoomGestureEnable);
	}

	/**
	 * 设置所有手势
	 * 
	 * @param allGestureEnable
	 *            所有手势
	 */
	public void setUIGesture(boolean allGestureEnable) {
		/* 所有手势 */
		mBaiduMap.getUiSettings().setAllGesturesEnabled(allGestureEnable);
	}

	/**
	 * 设置地图状态
	 * 
	 * @param witAnimation
	 * @param u
	 */
	private void setMapStatus(boolean witAnimation, MapStatusUpdate u) {
		if (witAnimation) {
			mBaiduMap.animateMapStatus(u);
		} else {
			mBaiduMap.setMapStatus(u);
		}
	}

	/**
	 * 地图中心点移动到
	 * 
	 * @param latLng
	 *            中心点位置
	 * @param witAnimation
	 *            是否带动画
	 */
	public void moveTo(LatLng latLng, boolean witAnimation) {
		setMapStatus(witAnimation, MapStatusUpdateFactory.newLatLng(latLng));
	}

	/**
	 * 移动到区域
	 * 
	 * @param list
	 * @param witAnimation
	 */
	public void moveBound(List<LatLng> list, boolean witAnimation) {
		Builder builder = new Builder();
		int size = list.size();
		for (int i = 0; i < size; i++) {

			LatLng latLng = list.get(i);
			if (latLng != null) {
				builder.include(latLng);
			} else {
			}
		}
		LatLngBounds bounds = builder.build();

		setMapStatus(witAnimation,
				MapStatusUpdateFactory.newLatLngBounds(bounds));
	}

	/**
	 * 地图缩放
	 * 
	 * @param zoom
	 */
	public void zoomTo(float zoom) {
		setMapStatus(false, MapStatusUpdateFactory.zoomTo(zoom));
	}

	/**
	 * 
	 * @return
	 */
	public float getZoom() {
		return mBaiduMap.getMapStatus().zoom;
	}

	/**
	 * 开关定位图层
	 * 
	 * @param enable
	 * @param rid
	 */
	public void setMyLocationEnable(boolean enable, int rid) {
		mBaiduMap.setMyLocationEnabled(enable);
		BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
				.fromResource(rid);
		if (bitmapDescriptor == null) {
			return;
		}
		MyLocationConfiguration conf = new MyLocationConfiguration(
				LocationMode.NORMAL, false, bitmapDescriptor);
		mBaiduMap.setMyLocationConfigeration(conf);
	}

	/**
	 * 跟新我的位置信息
	 * 
	 * @param latitude
	 * @param lonitude
	 * @param accuracy
	 *            精确度
	 * @param direction
	 *            方向
	 */
	public void setMylocationData(double latitude, double lonitude,
			float accuracy, float direction) {
		MyLocationData locData = new MyLocationData.Builder()
				.latitude(latitude).longitude(lonitude).accuracy(accuracy)
				.direction(direction).build();

		mBaiduMap.setMyLocationData(locData);
	}

	/**
	 * 设置城市交通图
	 * 
	 * @param enable
	 */
	public void setTrafficEnable(boolean enable) {
		mBaiduMap.setTrafficEnabled(enable);
	}

	/**
	 * 设置简单的marker
	 * 
	 * @param latLng
	 * @param bitmapDescriptor
	 */
	public void setMarkerOverlayer(LatLng latLng,
			BitmapDescriptor bitmapDescriptor) {
		OverlayOptions option = new MarkerOptions().position(latLng).icon(
				bitmapDescriptor);
		mBaiduMap.addOverlay(option);
	}

	/**
	 * set marker
	 */
	public void setMarkerOverlayer(LatLng latlng, int icon) {
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(icon);
		// 构建MakerOption, 用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(latlng).icon(
				bitmap);
		// 在地图上添加Marker,并显示
		this.mBaiduMap.addOverlay(option);
	}

	/**
	 * set marker
	 */
	public void setMarkerOverlayer(LatLng latlng, int icon, float azimuth) {
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(icon);
		// 构建MakerOption, 用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(latlng)
				.rotate((180 - azimuth)).icon(bitmap).anchor(0.5f, 0.5f);
		// 在地图上添加Marker,并显示
		this.mBaiduMap.addOverlay(option);
	}

	/**
	 * 画轨迹并显示整段轨迹
	 * 
	 * @param list
	 */
	public void setPolyLineOverlayer(List<LatLng> list) {
		// 1 < size < 10000
		if (list == null || list.size() < 2) {
			return;
		}

		OverlayOptions options = new PolylineOptions().points(list)
				.color(DEFAULT_POLYLINE_COLOR).visible(true);
		mBaiduMap.addOverlay(options);

		moveBound(list, true);
	}

	/**
	 * 去掉所有markers
	 */
	public void removeAllMarkers() {
		mBaiduMap.clear();
	}

	public void setOnMapLoadedCallBack(OnMapLoadedCallback mapLoadedCallBack) {
		mBaiduMap.setOnMapLoadedCallback(mapLoadedCallBack);
	}

	public void calculateDistance(LatLng start, LatLng end) {

	}

	/**
	 * activity/fragment onReusume()调用
	 */
	public void onResume() {
		// TODO sth
	}

	/**
	 * activity/fragment onPause()调用
	 */
	public void onPause() {
		// TODO sth
	}

	/**
	 * activity/fragment onDestory()调用
	 */
	public void onDestory() {
		// TODO 关闭定位图层
		if(null!=mBaiduMap) {
			mBaiduMap.removeMarkerClickListener(listener);
//			mBaiduMap.clear();
			mBaiduMap = null;
		}
	}

	public Marker setMarkerOverlayer(LatLng latLng, int icon, String position) {
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(icon);
		// 构建MakerOption, 用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(latLng)
				.icon(bitmap).title(position);
		// 在地图上添加Marker,并显示
		return (Marker) this.mBaiduMap.addOverlay(option);

	}

	public void setMarkerListener(OnMarkerClickListener markerClickListener) {
		this.listener = markerClickListener;
		mBaiduMap.setOnMarkerClickListener(markerClickListener);
	}
}
