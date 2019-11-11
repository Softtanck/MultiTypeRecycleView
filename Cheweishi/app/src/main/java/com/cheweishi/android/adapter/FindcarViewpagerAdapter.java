package com.cheweishi.android.adapter;

import java.util.List;

import com.cheweishi.android.utils.StringUtil;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class FindcarViewpagerAdapter extends PagerAdapter {

	private List<View> lists;


	public FindcarViewpagerAdapter(Context context, List<View> lists) {
		this.lists = lists;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getCount() {
		return 1000;
	}



	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(View container, int position) {
		if (!StringUtil.isEmpty(lists) && lists.size() > 0) {
			position %= lists.size();
		}
		if (position < 0) {
			position = lists.size() + position;
		}
		View view = lists.get(position);
		// 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
		ViewParent vp = view.getParent();
		if (vp != null) {
			ViewGroup parent = (ViewGroup) vp;
			parent.removeView(view);
		}
		((ViewGroup) container).addView(view);
		// add listeners here if necessary
		return view;
	}

}
