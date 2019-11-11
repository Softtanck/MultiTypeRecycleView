package com.cheweishi.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.ParkInfoNative;
import com.cheweishi.android.utils.MyMapUtils;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.BaiduMapView;

/**
 * 找车位适配器
 * 
 * @author mingdasen
 * 
 */
public class MyStallAdapter extends BaseAdapter implements OnClickListener {

	private Context mContext;
	private List<ParkInfoNative> list;
	private ParkInfoNative mParkInfo;

	// private int position;

	public MyStallAdapter(Context mContext, List<ParkInfoNative> list) {
		this.list = list;
		this.mContext = mContext;
	}

	public void refreshData(List<ParkInfoNative> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		// this.position = position;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_mystall, null);
			holder.park_img = (ImageView) convertView
					.findViewById(R.id.car_iv_location);
			holder.park_name = (TextView) convertView
					.findViewById(R.id.car_tv_car_iv_location);
			holder.park_distance = (TextView) convertView
					.findViewById(R.id.car_tv_length);
			holder.park_addr = (TextView) convertView
					.findViewById(R.id.car_xlocation);
			holder.park_leftNum = (TextView) convertView
					.findViewById(R.id.car_tv_num);
			holder.park_price = (TextView) convertView
					.findViewById(R.id.car_tv_money);
//			holder.call = (LinearLayout) convertView
//					.findViewById(R.id.car_lin_yuyue);
			holder.go = (TextView) convertView
					.findViewById(R.id.car_lin_daozhequ);
			holder.park_content = (LinearLayout) convertView
					.findViewById(R.id.park_content);
			holder.park_surplus = (TextView) convertView
					.findViewById(R.id.tv_num_surplus);
			holder.park_unit = (TextView) convertView
					.findViewById(R.id.price_unit);
//			holder.call.setOnClickListener(this);
			holder.go.setOnClickListener(this);
//			holder.park_content.setOnClickListener(this);
//			holder.call.setTag(position);
			holder.go.setTag(position);
//			holder.park_content.setTag(position);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
//			holder.call.setTag(position);
			holder.go.setTag(position);
//			holder.park_content.setTag(position);
		}

		if (list.size() > position) {
			mParkInfo = list.get(position);
			XUtilsImageLoader.getxUtilsImageLoader(mContext,
					R.drawable.zhaochewei_img,holder.park_img,
					mParkInfo.getPicUrl());
			holder.park_name.setText(mParkInfo.getName());
			holder.park_distance.setText(mParkInfo.getDistance() + "" + mContext.getText(R.string.distance_unit));
			holder.park_addr.setText(mParkInfo.getAddr());
			if (list.get(position).getLeftNum() == -1) {
				holder.park_leftNum.setText("--");
				holder.park_surplus.setText("");
			} else {
				holder.park_leftNum.setText(mParkInfo.getLeftNum() + "");
				holder.park_surplus.setText(R.string.surplus_park);
			}
			if ("--".equals(mParkInfo.getPrice())) {
				holder.park_unit.setText("");
			} else {
				holder.park_unit.setText(R.string.price_unit);
			}
			holder.park_price.setText(mParkInfo.getPrice());
		}
		return convertView;
	}

	private class ViewHolder {
		ImageView park_img;
		TextView park_name;
		TextView park_distance;
		TextView park_addr;
		TextView park_leftNum;
		TextView park_price;
		LinearLayout call;
		TextView go;
		LinearLayout park_content;
		TextView park_surplus;
		TextView park_unit;
	}

	@Override
	public void onClick(View view) {
//		mParkInfo = list.get((Integer) view.getTag());
		switch (view.getId()) {
//		case R.id.car_lin_yuyue:
//			break;
		case R.id.car_lin_daozhequ:
			ProgrosDialog.openDialog(mContext);
			ProgrosDialog.CanceledOnTouchOutside(true);
			BaiduMapView bb = new BaiduMapView();
			bb.initMap(mContext);
			bb.baiduNavigation(MyMapUtils.getLatitude(mContext),
					MyMapUtils.getLongitude(mContext),
					MyMapUtils.getAddress(mContext),
					StringUtil.getDouble(mParkInfo.getLatitude()),
					StringUtil.getDouble(mParkInfo.getLongitude()),
					mParkInfo.getAddr());
			ProgrosDialog.closeProgrosDialog();
			break;
		default:
			break;
		}
	}
}
