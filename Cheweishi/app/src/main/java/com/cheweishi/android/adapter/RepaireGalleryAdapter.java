package com.cheweishi.android.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Gallery.LayoutParams;

import com.cheweishi.android.R;
import com.cheweishi.android.biz.XUtilsImageLoader;

public class RepaireGalleryAdapter extends BaseAdapter {

	private Context _context;
	private List<Integer> imgList;
	private List<String> imgListStr;
	private int returnCount = -1;

	public RepaireGalleryAdapter(Context context, List<Integer> imgList) {
		_context = context;
		this.imgList = imgList;
	}

	public RepaireGalleryAdapter(Context context, List<String> imgList,
			int returnCount) {
		_context = context;
		this.imgListStr = imgList;
		this.returnCount = returnCount;
	}

	public int getCount() {
		if (returnCount == -1) {
			return Integer.MAX_VALUE;
		} else {
			return imgList == null ? 0 : imgList.size();
		}

	}

	public Object getItem(int position) {

		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			ImageView imageView = new ImageView(_context);
			imageView.setAdjustViewBounds(true);
			// imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			convertView = imageView;
			viewHolder.imageView = (ImageView) convertView;
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// if (imgListStr != null && imgListStr.size() > 0) {
		// viewHolder.imageView.setImageResource(imgList.get(position
		// % imgListStr.size()));
		// }

		if (imgListStr != null && imgListStr.size() > 0) {
			XUtilsImageLoader.getxUtilsImageLoader(_context,
					R.drawable.repaire_img,viewHolder.imageView,
					"http://115.28.161.11:8080/XAI/appDownLoad/downLoadPhoto?path="
							+ imgListStr.get(position % imgListStr.size()));
		}
		return convertView;
	}

	private static class ViewHolder {
		ImageView imageView;
	}
}
