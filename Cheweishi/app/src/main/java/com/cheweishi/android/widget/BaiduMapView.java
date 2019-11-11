package com.cheweishi.android.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.model.LatLngBounds.Builder;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.PoiDetailShareURLOption;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.navisdk.BNaviPoint;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BaiduNaviManager.OnStartNavigationListener;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;
import com.cheweishi.android.activity.BNavigatorActivity;
import com.cheweishi.android.utils.StringUtil;

/**
 * Baidu map
 * 
 * @author WeiGong
 */

public class BaiduMapView {
	private final String baiduMapUrl = "http://map.baidu.com/maps/download/index.php?app=map&qudao=1009176q";
	private static final String TAG = "BaiduMapView";
	/* 兴趣点搜索 */
	private PoiSearch mPoiSearch = null;
	/* 分享链接搜索 */
	private ShareUrlSearch mShareUrlSearch = null;
	private SuggestionSearch mSuggestionSearch;
	private RoutePlanSearch routeSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private GeoCoder geoSearch;

	public List<LatLng> latLngs = new ArrayList<LatLng>();
	private BaiduMap mBaiduMap;
	private boolean isFirstLoc = true;// 是否首次定位
	private LocationClient mLocClient = null;
	private Timer carTimer = null;
	private Thread playingThread = null;
	private MapView mMapView;
	private Context mContext;
	private boolean isNavigating;

	public BaiduMapView() {
	}

	public BaiduMapView(MapView mMapView, Context mContext) {
		this.mMapView = mMapView;
		mBaiduMap = this.mMapView.getMap();
		this.mContext = mContext;
	}

	/**
	 * 发起导航 需要{@link initMap}
	 * 
	 * @param sLatitude
	 * @param sLongitude
	 * @param sAddress
	 * @param eLatitude
	 * @param eLongitude
	 * @param eAddress
	 */
	public void baiduNavigation(double sLatitude, double sLongitude,
			String sAddress, double eLatitude, double eLongitude,
			String eAddress) {
		BNaviPoint startPoint = new BNaviPoint(sLongitude, sLatitude, sAddress,
				BNaviPoint.CoordinateType.BD09_MC);
		BNaviPoint endPoint = new BNaviPoint(eLongitude, eLatitude, eAddress,
				BNaviPoint.CoordinateType.BD09_MC);
		try {
			BaiduNaviManager.getInstance().launchNavigator(
					((Activity) mContext), startPoint, endPoint,
					NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME, true,
					BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY,
					new OnStartNavigationListener() { // 跳转监听

						@Override
						public void onJumpToNavigator(Bundle configParams) {
							Intent intent = new Intent(mContext,
									BNavigatorActivity.class);
							intent.putExtras(configParams);
							mContext.startActivity(intent);
						}

						@Override
						public void onJumpToDownloader() {
						}
					});
		} catch (Exception e) {
		}
	}

	/**
	 * Draw trajectory
	 */

	public void drawTrajectory(LatLng latLng) {
		if (latLng != null) {
			latLngs.add(latLng);
		}
		if (latLngs.size() > 1) {
			PolylineOptions polyline = new PolylineOptions().points(latLngs)
					.color(Color.RED).visible(true);
			this.mBaiduMap.addOverlay(polyline);
		}

	}

	/**
	 * Draw trajectory And Set bounds
	 */

	public void drawTrajectoryBounds(LatLng latLng) {
		if (latLng != null) {
			latLngs.add(latLng);
		}
		if (latLngs.size() > 1) {
			PolylineOptions polyline = new PolylineOptions().points(latLngs)
					.color(Color.RED).visible(true);
			this.mBaiduMap.addOverlay(polyline);
		}
		LatLngBounds.Builder builder = new Builder();
		for (LatLng obj : latLngs) {
			builder.include(obj);
		}
		LatLngBounds bounds = builder.build();
		moveBoundsLatLngs(bounds);
	}

	/***
	 * 把所有的marker包裹在屏幕里面
	 * 
	 * @param latLng
	 *            经纬度
	 */
	public void moveBounds(LatLng latLng) {
		if (latLng != null) {
			latLngs.add(latLng);
		}
		LatLngBounds.Builder builder = new Builder();
		for (LatLng obj : latLngs) {
			builder.include(obj);
		}
		LatLngBounds bounds = builder.build();
		moveBoundsLatLngs(bounds);
	}

	/***
	 * 清空latlng
	 */
	public void clearBounds() {
		if (latLngs != null) {
			latLngs.clear();
		}
	}

	/**
	 * 设置百度地图控制
	 * 
	 * @param mMapView
	 */
	public void setBaiduMap(BaiduMap baiduMap) {
		this.mBaiduMap = baiduMap;
	}

	/**
	 * To initialize the map
	 * 
	 * @param mMapView
	 */
	public void setMapView(MapView mMapView, int zoom) {
		this.mMapView = mMapView;
		mBaiduMap = mMapView.getMap();
		zoomTo(zoom);
	}

	/**
	 * Real-time traffic
	 * 
	 * @param mapView
	 * @param flag
	 */
	public void setTraffic(boolean flag) {
		this.mBaiduMap.setTrafficEnabled(flag);
	}

	/**
	 * set marker
	 */
	public void setMarker(LatLng latlng, int icon) {
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(icon);
		// 构建MakerOption, 用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(latlng)
				.icon(bitmap).anchor(0.5f, 0.5f);
		// 在地图上添加Marker,并显示
		this.mBaiduMap.addOverlay(option);
	}

	/**
	 * set marker
	 */
	public void setMarker(LatLng latlng, int icon, float azimuth) {
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(icon);
		// 构建MakerOption, 用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(latlng)
				.rotate((180 - azimuth)).icon(bitmap).anchor(0.5f, 0.5f);
		// 在地图上添加Marker,并显示
		this.mBaiduMap.addOverlay(option);
	}

	/**
	 * set marker
	 */
	public void setMarker(LatLng latlng, Bitmap bp) {
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(bp);
		// 构建MakerOption, 用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(latlng)
				.icon(bitmap).anchor(0.5f, 0.5f);
		// 在地图上添加Marker,并显示
		this.mBaiduMap.addOverlay(option);
	}

	/**
	 * set marker and zindex
	 * 
	 * @param i
	 * @return
	 */
	public void setMarker(LatLng latlng, int icon, int zindex) {
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(icon);
		// 构建MakerOption, 用于在地图上添加Marker
		OverlayOptions opetions = new MarkerOptions().position(latlng)
				.icon(bitmap).anchor(0.5f, 0.5f).zIndex(zindex);
		// 在地图上添加Marker,并显示
		mBaiduMap.addOverlay(opetions);
	}

	/**
	 * 添加一个动态marker
	 * 
	 * @param latlng
	 * @param icons
	 */
	public void setMarkers(LatLng latlng, int[] icons) {
		ArrayList<BitmapDescriptor> bitmaps = new ArrayList<BitmapDescriptor>();
		for (int rid : icons) {
			BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
					.fromResource(rid);
			bitmaps.add(bitmapDescriptor);
		}
		OverlayOptions options = new MarkerOptions().position(latlng)
				.icons(bitmaps).anchor(0.5f, 0.5f).period(50);
		mBaiduMap.addOverlay(options);
	}

	/**
	 * 
	 * @param latlng
	 * @param icons
	 * @param zindex
	 */
	public void setMarkers(LatLng latlng, int[] icons, int zindex) {
		ArrayList<BitmapDescriptor> bitmaps = new ArrayList<BitmapDescriptor>();
		for (int rid : icons) {
			BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
					.fromResource(rid);
			bitmaps.add(bitmapDescriptor);
		}
		OverlayOptions options = new MarkerOptions().position(latlng)
				.icons(bitmaps).anchor(0.5f, 0.5f).zIndex(zindex);
		mBaiduMap.addOverlay(options);
	}

	/**
	 * 点击图标监听
	 * 
	 * @param handler
	 */
	public void setMakerListener(final Handler handler) {

		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(final Marker _marker) {
				handler.post(new Runnable() {
					public void run() {
						if (_marker.getZIndex() == -1) {
							return;
						}

						Message message = new Message();
						message.what = _marker.getZIndex();
						message.obj = _marker;
						handler.sendMessage(message);
					}

				});

				return false;
			}

		});
	}

	public void setLatLngs(List<LatLng> latLngs) {
		this.latLngs = latLngs;
	}

	/**
	 * Remove all the markers
	 */
	public void removeAllMarks() {
		this.mBaiduMap.clear();
	}

	/**
	 * 发起定位
	 */

	public void requestLocation() {
		if (mLocClient != null && mLocClient.isStarted()) {
			isFirstLoc = true;
			mLocClient.requestLocation();
		}
	}

	/**
	 * 在线建议查询
	 */

	public void suggestSearch(String city, String keyword,
			final MapCallBack mapcallback) {
		boolean initListener = false;
		if (mSuggestionSearch == null) {
			initListener = true;
			mSuggestionSearch = SuggestionSearch.newInstance();
		}

		mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
				.city(city).keyword(keyword));

		// 设置在线建议查询监听者
		if (initListener) {
			mSuggestionSearch
					.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {

						@Override
						public void onGetSuggestionResult(
								SuggestionResult suggestionresult) {
							// 未找到相关结果
							if (suggestionresult == null
									|| suggestionresult.getAllSuggestions() == null) {
								return;
							}

							// 获取在线建议结果
							try {
								mapcallback.suggestionSearch(suggestionresult);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					});
		}

	}

	/**
	 * 在使用SDK各组件之前初始化mContext信息，传入ApplicationContext 注意该方法要再setContentView方法之前实现
	 * 
	 * @param mContext
	 */
	public void initMap(Context mContext) {
		this.mContext = mContext;
	}

	/**
	 * Interest city search
	 * 
	 * @param isShowMarker
	 *            是否插入坐标到地图上
	 */

	public void interestSearch(final String city, final String interest,
			final MapCallBack mapCallBack, boolean isShowMarker) {
		mPoiSearch = PoiSearch.newInstance();
		poiSubmenu(mPoiSearch, mapCallBack, isShowMarker);
		mPoiSearch.searchInCity(new PoiCitySearchOption().city(city).keyword(
				interest));
	}

	public void poiSearchCity(final String city, final String interest,
			final MapCallBack mapCallBack, boolean isShowMarker) {
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(null);
		mPoiSearch.searchInCity(new PoiCitySearchOption().city(city).keyword(
				interest));
	}

	/**
	 * 打开外部导航导航
	 * 
	 * 如果要使用，需要删除导航包，只要除百度导航外的其它包，就可以使用
	 * 
	 */
	// public void openBaiduNavigation(double sLat, double sLon,String sAddress,
	// double eLat, double eLon, String eAddress) {
	// LatLng pt1 = new LatLng(sLat, sLon);
	// LatLng pt2 = new LatLng(eLat, eLon);
	// // 构建 导航参数
	// NaviPara para = new NaviPara();
	// para.startPoint = pt1;
	// para.startName = sAddress;
	// para.endPoint = pt2;
	// para.endName = eAddress;
	//
	// try {
	//
	// BaiduMapNavigation.openBaiduMapNavi(para, mContext);
	//
	// } catch (BaiduMapAppNotSupportNaviException e) {
	// e.printStackTrace();
	// AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
	// builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
	// builder.setTitle("提示");
	// builder.setPositiveButton("确认", new OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.dismiss();
	// BaiduMapNavigation.getLatestBaiduMapApp(mContext);
	// }
	// });
	//
	// builder.setNegativeButton("取消", new OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.dismiss();
	// }
	// });
	//
	// builder.create().show();
	// }
	// }

	/**
	 * 打开外部百度地图
	 */
	public void openBaiduMap() {
		try {
			PackageManager packageManager = mContext.getPackageManager();
			Intent intent = new Intent();
			intent = packageManager
					.getLaunchIntentForPackage("com.baidu.BaiduMap");
			mContext.startActivity(intent);

		} catch (Exception e) {
			e.printStackTrace();
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
			builder.setTitle("提示");
			builder.setPositiveButton("确认",
					new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							Intent it = new Intent(Intent.ACTION_VIEW, Uri
									.parse(baiduMapUrl));
							mContext.startActivity(it);
						}
					});

			builder.setNegativeButton("取消",
					new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

			builder.create().show();
		}
	}

	public void onDestory() {
		if (carTimer != null) {
			Log.i("onDestory", "取消定时任务");
			carTimer.cancel();
		}

		if (playingThread != null) {
			playingThread.interrupt();
		}
		if (mPoiSearch != null) {
			mPoiSearch.destroy();
		}
		if (mShareUrlSearch != null) {
			mShareUrlSearch.destroy();
		}
		// 关闭定位图层
		if (mLocClient != null && mBaiduMap != null) {
			mBaiduMap.setMyLocationEnabled(false);
		}
		// 退出时销毁定位
		if (mLocClient != null) {
			mLocClient.stop();
		}

		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();
			Log.i("百度地图定位", "停止成功！");
		}

		if (myLocationListener != null) {
			mLocationClient.unRegisterLocationListener(myLocationListener);
			Log.i("百度地图定位", "取消监听成功！");
		}
	}

	/**
	 * Parsing the address
	 */

	public void parsingTheAddress(LatLng latlng, final MapCallBack mapCallBack) {
		geoSearch = GeoCoder.newInstance();

		ReverseGeoCodeOption option = new ReverseGeoCodeOption();
		option.location(latlng);
		geoSearch.reverseGeoCode(option);

		geoSearch
				.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

					@Override
					public void onGetReverseGeoCodeResult(
							ReverseGeoCodeResult result) {
						try {
							// There is no the retrieved results
							if (result == null
									|| result.error != SearchResult.ERRORNO.NO_ERROR) {
								return;
							}
							mapCallBack.address(result);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onGetGeoCodeResult(GeoCodeResult arg0) {

					}
				});
	}

	/**
	 * 默认5000米 NearBy Search
	 */

	public void poiNearBySearch(final String interest,
			final MapCallBack mapCallBack, final LatLng latLng) {

		mPoiSearch = PoiSearch.newInstance();
		poiSubmenu(mPoiSearch, mapCallBack, false);

		PoiNearbySearchOption poiNearBy = new PoiNearbySearchOption();
		// poiNearBy.sortType(PoiSortType.comprehensive);
		poiNearBy.keyword(interest);
		poiNearBy.location(latLng);
		// 米
		poiNearBy.radius(20000000);
		mPoiSearch.searchNearby(poiNearBy);
	}

	private PoiSearch poiSubmenu(PoiSearch PoiSearch,
			final MapCallBack mapCallBack, final boolean isShowMarker) {
		mPoiSearch
				.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {

					@Override
					public void onGetPoiDetailResult(PoiDetailResult result) {
						if (result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
							Log.i(TAG, "抱歉，未找到结果1");
							Toast.makeText(mContext, "抱歉，没有详细介绍！",
									Toast.LENGTH_SHORT).show();
						}

						if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

							// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
							String strInfo = "在";
							// for (CityInfo cityInfo : result
							// .getSuggestCityList()) {
							// strInfo += cityInfo.city;
							// strInfo += ",";
							// }
							strInfo += "找到结果";
						}

					}

					@Override
					public void onGetPoiResult(PoiResult result) {
						try {
							if (result == null
									|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {

								if (isShowMarker) {
									Log.i(TAG, "抱歉，未找到结果2");
									Toast.makeText(mContext, "抱歉，未找到结果",
											Toast.LENGTH_SHORT).show();
								}
							} else if (result.error == SearchResult.ERRORNO.NO_ERROR) {
								if (isShowMarker) {
									mBaiduMap.clear();
									PoiOverlay overlay = new MyPoiOverlay(
											mBaiduMap);
									mBaiduMap.setOnMarkerClickListener(overlay);
									overlay.setData(result);
									overlay.addToMap();
									overlay.zoomToSpan();
								} else {
									mapCallBack.interestSearch(result, 0);
								}
								return;
							}

							if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

								// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
								String strInfo = "在";
								for (CityInfo cityInfo : result
										.getSuggestCityList()) {
									strInfo += cityInfo.city;
									strInfo += ",";
								}
								strInfo += "找到结果";
								// Toast.makeText(mContext, strInfo,
								// Toast.LENGTH_LONG).show();
								mapCallBack.interestSearch(result, 0);
							} else {
								Log.i(TAG, "外省取不到结果");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					class MyPoiOverlay extends PoiOverlay {

						public MyPoiOverlay(BaiduMap baiduMap) {
							super(baiduMap);
						}

						@Override
						public boolean onPoiClick(int index) {
							super.onPoiClick(index);
							try {
								PoiInfo poi = getPoiResult().getAllPoi().get(
										index);
								if (poi.hasCaterDetails) {
									Log.i(TAG, "poi.hasCaterDetails:" + poi.uid);
									mPoiSearch
											.searchPoiDetail((new PoiDetailSearchOption())
													.poiUid(poi.uid));
								}
								mapCallBack.interestSearch(getPoiResult(),
										index);
							} catch (Exception e) {
								e.printStackTrace();
							}
							return true;
						}
					}

				});
		return PoiSearch;
	}

	/**
	 * poi share
	 * 
	 * @author mac
	 * 
	 */

	public void poiShare(String uid) {
		mShareUrlSearch = ShareUrlSearch.newInstance();
		mShareUrlSearch
				.setOnGetShareUrlResultListener(new OnGetShareUrlResultListener() {

					@Override
					public void onGetPoiDetailShareUrlResult(
							ShareUrlResult result) {

					}

					@Override
					public void onGetLocationShareUrlResult(
							ShareUrlResult result) {

					}
				});
		mShareUrlSearch.requestPoiDetailShareUrl(new PoiDetailShareURLOption()
				.poiUid(uid));
	}

	private MapCallBack realTimeCallBack;// 实时请求回调函数

	/**
	 * 请求手机定位
	 */
	public void requestLocation1() {
		if (mLocationClient != null && mLocationClient.isStarted())
			mLocationClient.requestLocation();
		else
			Log.i("LocSDK4", "LocationCleint is null or not started");
	}

	/**
	 * 打开外部导航导航
	 * 
	 * 如果要使用，需要删除导航包，只要除百度导航外的其它包，就可以使用
	 * 
	 */
	// public void openBaiduNavigation(double sLat, double sLon,String sAddress,
	// double eLat, double eLon, String eAddress) {
	// LatLng pt1 = new LatLng(sLat, sLon);
	// LatLng pt2 = new LatLng(eLat, eLon);
	// // 构建 导航参数
	// NaviPara para = new NaviPara();
	// para.startPoint = pt1;
	// para.startName = sAddress;
	// para.endPoint = pt2;
	// para.endName = eAddress;
	//
	// try {
	//
	// BaiduMapNavigation.openBaiduMapNavi(para, mContext);
	//
	// } catch (BaiduMapAppNotSupportNaviException e) {
	// e.printStackTrace();
	// AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
	// builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
	// builder.setTitle("提示");
	// builder.setPositiveButton("确认", new OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.dismiss();
	// BaiduMapNavigation.getLatestBaiduMapApp(mContext);
	// }
	// });
	//
	// builder.setNegativeButton("取消", new OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.dismiss();
	// }
	// });
	//
	// builder.create().show();
	// }
	// }

	// LocationClientOption option;

	public void telephoneLocation(Context applicationContext, int scanSpan,
			MapCallBack mapCallback) {
		mLocationClient = new LocationClient(applicationContext); // 声明LocationClient类
		// 定位选择
		LocationClientOption option = new LocationClientOption();
		// option.setLocationMode(LocationMode.Hight_Accuracy); //设置定位模式
		option.setCoorType("bd09ll");
		option.setProdName("heiche");
		option.setScanSpan(scanSpan); // 设置发起定位请求的间隔时间为30s
		option.setIsNeedAddress(true); // 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true); // 返回的定位结果包含手机机头的方向

		myLocationListener = new MyLocationListener(mapCallback);
		mLocationClient.registerLocationListener(myLocationListener);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	public void setLocationOptions(int scanSpan) {
		if (mLocationClient == null) {
			return;
		}
		mLocationClient.stop();
		LocationClientOption option = mLocationClient.getLocOption();
		// option.setLocationMode(LocationMode.Hight_Accuracy); //设置定位模式
		option.setCoorType("bd09ll");
		option.setProdName("heiche");
		option.setScanSpan(scanSpan); // 设置发起定位请求的间隔时间为30s
		option.setIsNeedAddress(true); // 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true); // 返回的定位结果包含手机机头的方向

		mLocationClient.setLocOption(option);
		mLocationClient.start();

	}

	/**
	 * Move to the marker
	 */
	public void moveLatLng(LatLng latLng) {
		this.mBaiduMap.animateMapStatus(MapStatusUpdateFactory
				.newLatLng(latLng));

	}

	/**
	 * Move to the marker
	 */
	public void moveLatLng(LatLng latLng, float f) {
		MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLngZoom(latLng, f);
		this.mBaiduMap.animateMapStatus(u1);

	}

	public void moveLatlng(LatLng latLng, BitmapDescriptor bitmap, float f) {
		if (!StringUtil.isEmpty(latLng) && !StringUtil.isEmpty(bitmap)) {
			OverlayOptions option = new MarkerOptions().position(latLng).icon(
					bitmap);
			mBaiduMap.addOverlay(option);
			MapStatusUpdate u1 = MapStatusUpdateFactory
					.newLatLngZoom(latLng, f);
			mBaiduMap.animateMapStatus(u1);
		}
	}

	/**
	 * 更新图标位置、方向
	 * 
	 * @param latlng
	 * @param rotate
	 */
	public void updateOritentation(LatLng latlng, int icon, float rotate,
			float accuracy) {
		// mBaiduMap.setMyLocationEnabled(true);这一句我写到了activity里面
		MyLocationData locationData = new MyLocationData.Builder()
				.latitude(latlng.latitude).longitude(latlng.longitude)
				.accuracy(accuracy).direction(rotate).build();
		BaiduMapView.this.mBaiduMap.setMyLocationData(locationData);
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(icon);
		MyLocationConfiguration config = new MyLocationConfiguration(
				LocationMode.NORMAL, true, bitmap);
		mBaiduMap.setMyLocationConfigeration(config);
	}

	/**
	 * 移动到所有经纬度的范围
	 */
	public void moveBoundsLatLngs(LatLngBounds bounds) {
		// this.mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
		if (!StringUtil.isEmpty(bounds)) {
			MapStatusUpdate statusUpdate = MapStatusUpdateFactory
					.newLatLngBounds(bounds);
			if (!StringUtil.isEmpty(this.mBaiduMap)
					&& !StringUtil.isEmpty(statusUpdate)) {
				try {
					this.mBaiduMap.setMapStatus(statusUpdate);
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 百度定位
	 */
	public void myLocation(final MapCallBack mapCallBack) {

		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(mContext);
		mLocClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				// map view 销毁后不在处理新接收的位置
				if (location == null || mMapView == null)
					return;
				// 请求新定位用的
				// LatLng latLng = new LatLng(location.getLatitude(),
				// location.getLongitude());
				// latLngs.add(latLng);
				// 把地址信息传回主界面
				if (location != null) {
					try {
						mapCallBack.address(location);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (isFirstLoc) {
					MyLocationData locData = new MyLocationData.Builder()
							.accuracy(location.getRadius())
							// 此处设置开发者获取到的方向信息，顺时针0-360
							.direction(location.getDirection())
							.latitude(location.getLatitude())
							.longitude(location.getLongitude()).build();
					mBaiduMap.setMyLocationData(locData);

					isFirstLoc = false;
					LatLng ll = new LatLng(location.getLatitude(), location
							.getLongitude());
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
					mBaiduMap.animateMapStatus(u);
				}

			}

		});
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	private LocationClient mLocationClient = null;
	private BDLocationListener myLocationListener = null;

	class MyLocationListener implements BDLocationListener {
		private MapCallBack mapCallback;

		public MyLocationListener(MapCallBack mapCallback) {
			this.mapCallback = mapCallback;
		}

		@Override
		public void onReceiveLocation(BDLocation bdLocation) {
			if (bdLocation == null)
				return;
			mapCallback.telephoneLocation(bdLocation);
		}

	}

	/**
	 * Map callback
	 * 
	 * @author mac
	 * 
	 */
	public interface MapCallBack {
		void address(BDLocation location) throws Exception;

		void address(ReverseGeoCodeResult result) throws Exception;

		void interestSearch(PoiResult poiResults, int index)
				throws Exception;

		void suggestionSearch(SuggestionResult suggestionresult)
				throws Exception;

		void navigationStatus(boolean status);

		void telephoneLocation(BDLocation location);
	}

	/**
	 * Set the map size
	 */
	public void zoomTo(int size) {
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(size));
	}

}
