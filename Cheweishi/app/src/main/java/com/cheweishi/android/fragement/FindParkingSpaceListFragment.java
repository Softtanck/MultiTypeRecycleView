package com.cheweishi.android.fragement;

import java.util.List;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.MyStallAdapter;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.entity.ParkInfoNative;
import com.cheweishi.android.utils.StringUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * 找车位列表
 * 
 * @author 大森
 * 
 */

public class FindParkingSpaceListFragment extends BaseFragment {

	@ViewInject(R.id.mycar_xlistview)
	private ListView mycar_xlistview;
	private MyStallAdapter adapter;

	private Bundle bundle;
	private List<ParkInfoNative> parkInfos;
	
	private FindParkBroadcastReceiver broad;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_parking_space, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (!StringUtil.isEmpty(getArguments())) {
			parkInfos = getArguments().getParcelableArrayList("data");
//			latLng = new LatLng(getArguments().getDouble("lat"),getArguments().getDouble("lng"));
		}
		 adapter = new MyStallAdapter(baseContext, parkInfos);
		 mycar_xlistview.setAdapter(adapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		// 注册刷新广播
		if (broad == null) {
			broad = new FindParkBroadcastReceiver();
		}
		IntentFilter intentFilter = new IntentFilter(Constant.REFRESH_FLAG);
		baseContext.registerReceiver(broad, intentFilter);
	}

	public class FindParkBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (StringUtil.isEquals(Constant.CURRENT_REFRESH,
					Constant.FIND_PARK_REFRESH, true)) {
				parkInfos = intent.getParcelableArrayListExtra("data");
				// latLng = new LatLng(intent.getDoubleExtra("lat", 0),
				// intent.getDoubleExtra("lng", 0));
				// moveTolocation(latLng);
				adapter = new MyStallAdapter(baseContext, parkInfos);
				mycar_xlistview.setAdapter(adapter);
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		baseContext.unregisterReceiver(broad);
	}
}
