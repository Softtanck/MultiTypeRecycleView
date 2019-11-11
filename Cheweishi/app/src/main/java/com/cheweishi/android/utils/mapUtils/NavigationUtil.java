package com.cheweishi.android.utils.mapUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.baidu.navisdk.BNaviPoint;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BaiduNaviManager.OnStartNavigationListener;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;
import com.cheweishi.android.activity.BNavigatorActivity;

public class NavigationUtil {
	private Context mContext;

	public NavigationUtil(Context mContext) {
		this.mContext = mContext;
	}

	public void startNavigation(double sLatitude, double sLongitude,
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

}
