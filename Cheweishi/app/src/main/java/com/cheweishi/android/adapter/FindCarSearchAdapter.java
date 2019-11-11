package com.cheweishi.android.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.cheweishi.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 找车位查询
 * 
 * @author zhangq
 * 
 */
public class FindCarSearchAdapter extends BaseAdapter {
	public static final int FLAG_LOCATION = 101;

	private LayoutInflater inflater;
	private ArrayList<HashMap<String, String>> datas;
	private Holder mHolder;
	private int mFlag;

	public FindCarSearchAdapter(Context mContext,
			ArrayList<HashMap<String, String>> datas) {
		inflater = LayoutInflater.from(mContext);
		this.datas = datas;
	}

	public FindCarSearchAdapter(Context mContext,
			ArrayList<HashMap<String, String>> datas, int flag) {
		inflater = LayoutInflater.from(mContext);
		this.datas = datas;
		this.mFlag = flag;
	}

	public void clearData() {
		datas = null;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return datas == null ? 0 : datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_textview_findcar,
					null, false);
			mHolder = new Holder(convertView);
			convertView.setTag(mHolder);
		} else {
			mHolder = (Holder) convertView.getTag();
		}

		HashMap<String, String> items = datas.get(position);
		mHolder.tvFirst.setText(items.get("key"));
		mHolder.tvSecond.setHint(items.get("city") + items.get("district"));

		if (mFlag == FLAG_LOCATION) {
			mHolder.imgIcon
					.setImageResource(R.drawable.zhaochewei_locationxiangqing);
		}
		return convertView;
	}

	class Holder {
		private TextView tvFirst;
		private TextView tvSecond;
		private ImageView imgIcon;

		private Holder(View v) {
			tvFirst = (TextView) v.findViewById(R.id.tv_first);
			tvSecond = (TextView) v.findViewById(R.id.tv_second);
			imgIcon = (ImageView) v.findViewById(R.id.img_icon);
		}
	}

}
