package com.cheweishi.android.adapter;

import java.util.ArrayList;

import com.cheweishi.android.R;
import com.cheweishi.android.activity.BaseActivity;
import com.cheweishi.android.activity.BaskOrderActivity;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class BaskOrderAdapter extends BaseAdapter {

	private ArrayList<Bitmap> list;
	private BaseActivity mContext;
	private LayoutInflater inflater;
	BaskOrderActivity context;

	public BaskOrderAdapter(ArrayList<Bitmap> list, BaseActivity mContext) {
		super();
		this.list = list;
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return getItem(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void setData(ArrayList<Bitmap> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		final ViewHouder houder;
		if (arg1 == null) {
			houder = new ViewHouder();
			arg1 = LayoutInflater.from(mContext).inflate(R.layout.item_bitmap,
					null);
			houder.imageView = (ImageView) arg1.findViewById(R.id.img_order4);
			houder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			houder.img_order44 = (ImageView) arg1
					.findViewById(R.id.img_order44);
			arg1.setTag(houder);
			houder.img_order44.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					list.remove(arg0);
					notifyDataSetChanged();
				}
			});

		} else {
			houder = (ViewHouder) arg1.getTag();

		}
		Log.i("result", "=====adapter===imageItem===" + list.size());
		houder.imageView.setImageBitmap(list.get(arg0));
		mContext.dealCallBackFromAdapter(arg0, list.get(arg0));
		return arg1;

	}

	class ViewHouder {
		ImageView imageView;
		ImageView img_order44;
	}

}
