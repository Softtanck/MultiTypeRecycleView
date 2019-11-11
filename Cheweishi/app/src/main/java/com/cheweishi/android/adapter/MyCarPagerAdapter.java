package com.cheweishi.android.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MyCarPagerAdapter extends PagerAdapter {
	private List<View> list;

	public MyCarPagerAdapter(List<View> list) {
		this.list=list;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == obj;
	}

	@Override
	public Object instantiateItem(View view, int position) {
		((ViewPager) view).addView(list.get(position));
		return list.get(position);
	}

	@Override
	public void destroyItem(View view, int position, Object object) {
		((ViewPager) view).removeView(list.get(position));
	}
}
