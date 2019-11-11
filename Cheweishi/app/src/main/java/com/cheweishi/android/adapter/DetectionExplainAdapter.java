package com.cheweishi.android.adapter;

import java.util.List;

import com.cheweishi.android.R;
import com.cheweishi.android.entity.DetectionInfoNative;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 检测项说明适配器
 * @author mingdasen
 *
 */
public class DetectionExplainAdapter extends BaseAdapter {
	
	private Context context;
	private List<DetectionInfoNative> list;
	
	public DetectionExplainAdapter(Context context,List<DetectionInfoNative> list) {
		this.context = context;
		this.list = list;
	}
	
	@Override
	public boolean isEnabled(int position) {
		return false;
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
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_detection_explain, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.option_name);
			holder.tv_range = (TextView) convertView.findViewById(R.id.option_range);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_name.setText(list.get(position).getName());
		holder.tv_range.setText(list.get(position).getValue());
		return convertView;
	}
	
	class ViewHolder{
		TextView tv_name;
		TextView tv_range;
	}

}
