package com.cheweishi.android.adapter;

import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.cheweishi.android.R;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.utils.MyMapUtils;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.BaiduMapView;

public class FindCarportListAdapter extends BaseAdapter {

	private Context context;
	private boolean isDraw;
	private LatLng latLng;
	private List<Map<String, String>> list;

	public FindCarportListAdapter(Context context,
			List<Map<String, String>> list, boolean isDraw, LatLng latLng) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
		this.isDraw = isDraw;
		this.latLng = latLng;
	}

	public void setlist(List<Map<String, String>> list, boolean isdraw,
			LatLng latLng) {
		this.list = list;
		this.isDraw = isdraw;
		this.latLng = latLng;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(
				R.layout.findcarport_listview_item, null);
		TextView address = (TextView) view
				.findViewById(R.id.findcarport_textview_address);
		TextView distance = (TextView) view
				.findViewById(R.id.findcarportlistview_item_distance);
		TextView addressdetails = (TextView) view
				.findViewById(R.id.findcarport_listview_item_addressdetails);
		TextView findcarportlistview_item_number = (TextView) view
				.findViewById(R.id.findcarportlistview_item_number);
		LinearLayout linearLayoutLocation = (LinearLayout) view
				.findViewById(R.id.findcarport_linearLayoutLocation);
		LinearLayout linearLayoutNavitave = (LinearLayout) view
				.findViewById(R.id.findcarport_linearLayoutNavitave);

		String addressString = list.get(arg0).get("name");
		String itemNumber = arg0 + 1 + ".";
		findcarportlistview_item_number.setText(itemNumber);
		address.setText(addressString);
		String disanceString = list.get(arg0).get("distance");
		distance.setText(context.getString(R.string.apart) + disanceString
				+ context.getString(R.string.meter));
		final String addressdetailsString = list.get(arg0).get("address");
		addressdetails.setText(addressdetailsString);
		linearLayoutNavitave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				double lat = StringUtil.getDouble(list.get(arg0).get("lat"));
				double lng = StringUtil.getDouble(list.get(arg0).get("lng"));
				if (lat != 0 && lng != 0) {
						BaiduMapView baiduMapView = new BaiduMapView();
						baiduMapView.initMap(context);
						if (isDraw) {
							
							if (latLng!=null) {
								baiduMapView.baiduNavigation(latLng.latitude,
										latLng.longitude,
										MyMapUtils.getAddress(context), lat, lng,
										addressdetailsString);
							}
						} else {
			
							baiduMapView.baiduNavigation(
									MyMapUtils.getLatLng(context).latitude,
									MyMapUtils.getLatLng(context).longitude,
									MyMapUtils.getAddress(context), lat, lng,
									list.get(arg0).get("address"));
						}
				}
			}
		});
		return view;
	}
}
