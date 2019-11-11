package com.cheweishi.android.adapter;


import com.cheweishi.android.R;
import com.cheweishi.android.entity.UserCommentNative;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class WashCarCommentAdapter extends BaseAdapter{

	private Context mContext;
	private List<UserCommentNative> mList;
	private LayoutInflater mLayoutInflater;
	
	public WashCarCommentAdapter(Context context, List<UserCommentNative> list) {
		mContext = context;
		mList = list;
		mLayoutInflater = LayoutInflater.from(mContext);
	}
	
	public void setList(List<UserCommentNative> list) {
		mList = list;
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(
					R.layout.item_washcar_comment, null);
			viewHolder.img_wash_comm_tx = (ImageView) convertView
					.findViewById(R.id.img_wash_comm_tx);
			viewHolder.tv_washcar_comm_time = (TextView) convertView
					.findViewById(R.id.tv_washcar_comm_time);
			viewHolder.tv_washcar_comm_msg = (TextView) convertView.findViewById(R.id.tv_washcar_comm_msg);
			viewHolder.tv_wash_comm_username = (TextView) convertView
					.findViewById(R.id.tv_wash_comm_username);
			viewHolder.tmg_washcar_comm_one = (ImageView) convertView
					.findViewById(R.id.tmg_washcar_comm_one);
			viewHolder.tmg_washcar_comm_two = (ImageView) convertView
					.findViewById(R.id.tmg_washcar_comm_two);
			viewHolder.tmg_washcar_comm_three = (ImageView) convertView
					.findViewById(R.id.tmg_washcar_comm_three);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_wash_comm_username.setText(mList.get(position).getUser_name());
		viewHolder.tv_washcar_comm_time.setText(mList.get(position).getTime());
		viewHolder.tv_washcar_comm_msg.setText(mList.get(position).getUserMsg());
		
		return convertView;
	}

	private class ViewHolder {
		private ImageView img_wash_comm_tx;
		private TextView tv_washcar_comm_time;
		private TextView tv_wash_comm_username;
		private TextView tv_washcar_comm_msg;
		private ImageView tmg_washcar_comm_one;
		private ImageView tmg_washcar_comm_two;
		private ImageView tmg_washcar_comm_three;
	}
}
