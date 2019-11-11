package com.cheweishi.android.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cheweishi.android.R;
import com.cheweishi.android.activity.BaseActivity;
import com.cheweishi.android.entity.PessanySearchResultNative;

public class PessanySearchResultAdpater extends BaseAdapter {
	private BaseActivity context;
	private ViewHolder viewHolder;
	private LayoutInflater mInflater;
	private List<PessanySearchResultNative> listPessanySearch;

	public PessanySearchResultAdpater(BaseActivity context,
			List<PessanySearchResultNative> listPessanySearch) {
		this.context = context;
		this.listPessanySearch = listPessanySearch;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listPessanySearch == null ? 0 : listPessanySearch.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setData(List<PessanySearchResultNative> listPessanySearch) {
		if (listPessanySearch.size() > 0) {
			this.listPessanySearch = listPessanySearch;
			this.notifyDataSetChanged();
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_pessany_search, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_peccany_reason = (TextView) convertView
					.findViewById(R.id.tv_peccany_reason);
			viewHolder.tv_peccany_time = (TextView) convertView
					.findViewById(R.id.tv_peccany_time);
			viewHolder.tv_road = (TextView) convertView
					.findViewById(R.id.tv_road);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

	class ViewHolder {
		TextView tv_peccany_reason;
		TextView tv_peccany_time;
		TextView tv_road;

	}

}
