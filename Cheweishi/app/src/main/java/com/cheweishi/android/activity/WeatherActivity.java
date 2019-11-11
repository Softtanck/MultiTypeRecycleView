package com.cheweishi.android.activity;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.cheweishi.android.R;
import com.cheweishi.android.widget.MyListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 天气界面
 * 
 * @author zhangq
 * 
 */
public class WeatherActivity extends BaseActivity {
	@ViewInject(R.id.gridview)
	private GridView mGridView;
	@ViewInject(R.id.listview)
	private MyListView mListView;

	private ArrayList<String> mData;
	private MyListAdapter listAdapter;
	private WeatherAdapter gridAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		ViewUtils.inject(this);
		initView();
		initData();
	}

	private void initData() {
		Calendar c=Calendar.getInstance();
	}

	private void initView() {
		gridAdapter = new WeatherAdapter(this);
		listAdapter = new MyListAdapter(this);
		mGridView.setAdapter(gridAdapter);
		mListView.setAdapter(listAdapter);
		calculateListViewHeight(mListView);
	}

	private void calculateListViewHeight(ListView lv) {
		int count = lv.getChildCount();
		int height = 0;
		for (int i = 0; i < count; i++) {
			View v = lv.getChildAt(i);
			height += v.getMeasuredHeight();
		}
		LayoutParams params = lv.getLayoutParams();
		params.height = height + lv.getDividerHeight() * (count - 1);
		lv.setLayoutParams(params);
	}

	class WeatherAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public WeatherAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.gridview_item_weather,
						null, false);

			}
			return convertView;
		}

	}

	class MyListAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public MyListAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.listview_item_weather,
						null, false);
			}
			return convertView;
		}

	}

}
