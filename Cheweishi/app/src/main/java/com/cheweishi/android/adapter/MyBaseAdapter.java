package com.cheweishi.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * adapter父类
 * 
 * @author zhangq
 * 
 * @param <T>
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {
	protected List<T> mData;
	protected Context mContext;
	protected LayoutInflater mInflater;

	public MyBaseAdapter(List<T> mData, Context mContext) {
		this.mContext = mContext;
		this.mData = mData;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mData == null ? 0 : mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	/**
	 * 返回值为0，需要时重写
	 */
	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public abstract View getView(int position, View convertView,
			ViewGroup parent);

}
