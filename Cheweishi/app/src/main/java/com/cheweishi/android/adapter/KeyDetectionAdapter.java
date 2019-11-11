package com.cheweishi.android.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheweishi.android.R;

/**
 * 一键检测
 * 
 * @author mingdasen
 * 
 */
public class KeyDetectionAdapter extends BaseAdapter {
	private Context context;
	private List<String> list;

	public KeyDetectionAdapter(Context context, List<String> list) {
		this.context = context;
		this.list = list;
		Log.i("result", "==KeyDetectionAdapter==list.size====" + list.size());
	}

	/**
	 * 刷新列表
	 */
	public void setList(List<String> list) {
		this.list = list;
		Log.i("result", "==KeyDetectionAdapter=set=list.size====" + list.size());
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_keydetection, null);
			holder.img_status = (ImageView) convertView
					.findViewById(R.id.img_status);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_value = (TextView) convertView
					.findViewById(R.id.tv_value);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String s = null;
		double d = 0;
		switch (position) {
		case 0:
			holder.tv_name.setText("保养状态");
			s = list.get(position);
			if (s.equals("--")) {
				holder.img_status.setImageResource(R.drawable.qus2x);
			} else {
				d = Double.parseDouble(s);
				if (d < 5000) {
					holder.img_status.setImageResource(R.drawable.right2x);
				} else {
					holder.img_status.setImageResource(R.drawable.dej2x);
				}
			}
			break;
		case 1:
			holder.tv_name.setText("年检状态");
			s = list.get(position);
			if (s.equals("--")) {
				holder.img_status.setImageResource(R.drawable.qus2x);
			} else {
				d = Double.parseDouble(s);
				if (d > 15) {
					holder.img_status.setImageResource(R.drawable.right2x);
				} else {
					holder.img_status.setImageResource(R.drawable.dej2x);
				}
			}
			break;
		case 2:
			holder.tv_name.setText("总里程(km)");
			s = list.get(position);
			if (s.equals("--")) {
				holder.img_status.setImageResource(R.drawable.qus2x);
			} else {
				holder.img_status.setImageResource(R.drawable.right2x);
			}
			break;
		case 3:
			holder.tv_name.setText("空燃比系数");
			if (list.get(position).equals("--")) {
				holder.img_status.setImageResource(R.drawable.qus2x);
			} else {
				holder.img_status.setImageResource(R.drawable.right2x);
			}
			break;
		case 4:
			holder.tv_name.setText("蓄电池电压(V)");
			s = list.get(position);
			if (s.equals("--")) {
				holder.img_status.setImageResource(R.drawable.qus2x);
			} else {
				d = Double.parseDouble(s);
				if (d <= 15.0 && d >= 11.5) {
					holder.img_status.setImageResource(R.drawable.right2x);
				} else {
					holder.img_status.setImageResource(R.drawable.dej2x);
				}
			}
			break;
		case 5:
			holder.tv_name.setText("节气门开度");
			s = list.get(position);
			if (s.equals("--")) {
				holder.img_status.setImageResource(R.drawable.qus2x);
			} else {
				d = Double.parseDouble(s);
				if (d <= 100 && d >= 0) {
					holder.img_status.setImageResource(R.drawable.right2x);
				} else {
					holder.img_status.setImageResource(R.drawable.dej2x);
				}
			}
			break;
		case 6:
			holder.tv_name.setText("发动机负荷(%)");
			s = list.get(position);
			if (s.equals("--")) {
				holder.img_status.setImageResource(R.drawable.qus2x);
			} else {
				d = Double.parseDouble(s);
				if (d <= 100 && d >= 0) {
					holder.img_status.setImageResource(R.drawable.right2x);
				} else {
					holder.img_status.setImageResource(R.drawable.dej2x);
				}
			}
			break;
		case 7:
			holder.tv_name.setText("发动机运行时间(S)");
			s = list.get(position);
			if (s.equals("--")) {
				holder.img_status.setImageResource(R.drawable.qus2x);
			} else {
				holder.img_status.setImageResource(R.drawable.right2x);
			}
			break;
		case 8:
			holder.tv_name.setText("百公里油耗(L/km)");
			s = list.get(position);
			if (s.equals("--")) {
				holder.img_status.setImageResource(R.drawable.qus2x);
			} else {
				holder.img_status.setImageResource(R.drawable.right2x);
			}
			break;
		case 9:
			holder.tv_name.setText("剩余油量(L)");
			s = list.get(position);
			if (s.equals("--")) {
				holder.img_status.setImageResource(R.drawable.qus2x);
			} else {
				holder.img_status.setImageResource(R.drawable.right2x);
			}
			break;
		case 10:
			holder.tv_name.setText("转速(rad/m)");
			s = list.get(position);
			if (s.equals("--")) {
				holder.img_status.setImageResource(R.drawable.qus2x);
			} else {
				d = Double.parseDouble(s);
				if (d <= 6000 && d >= 0) {
					holder.img_status.setImageResource(R.drawable.right2x);
				} else {
					holder.img_status.setImageResource(R.drawable.dej2x);
				}
			}
			break;
		case 11:
			holder.tv_name.setText("车速(km/h)");
			s = list.get(position);
			if (s.equals("--")) {
				holder.img_status.setImageResource(R.drawable.qus2x);
			} else {
				d = Double.parseDouble(s);
				if (d <= 160 && d >= 0) {
					holder.img_status.setImageResource(R.drawable.right2x);
				} else {
					holder.img_status.setImageResource(R.drawable.dej2x);
				}
			}
			break;
		case 12:
			holder.tv_name.setText("环境温度(℃)");
			s = list.get(position);
			if (s.equals("--")) {
				holder.img_status.setImageResource(R.drawable.qus2x);
			} else {
				d = Double.parseDouble(s);
				if (d <= 60 && d >= -40) {
					holder.img_status.setImageResource(R.drawable.right2x);
				} else if (d <= 60 && d >= -40) {
					holder.img_status.setImageResource(R.drawable.dej2x);
				}
			}
			break;
		case 13:
			holder.tv_name.setText("水温(℃)");
			s = list.get(position);
			if (s.equals("--")) {
				holder.img_status.setImageResource(R.drawable.qus2x);
			} else {
				d = Double.parseDouble(s);
				if (d <= 110 && d >= -40) {
					holder.img_status.setImageResource(R.drawable.right2x);
				} else {
					holder.img_status.setImageResource(R.drawable.dej2x);
				}
			}
			break;
		case 14:
			holder.tv_name.setText("obd时间");
			if (list.get(position).equals("--")) {
				holder.img_status.setImageResource(R.drawable.qus2x);
			} else if (list.get(position) != null) {
				holder.img_status.setImageResource(R.drawable.right2x);
			}

			break;
		case 15:
			holder.tv_name.setText("故障码");
			if (list.get(position).equals("") || list.get(position) == null) {
				holder.img_status.setImageResource(R.drawable.right2x);
			} else {
				holder.img_status.setImageResource(R.drawable.dej2x);
			}
			break;
		default:
			break;
		}
		return convertView;
	}

	private class ViewHolder {
		ImageView img_status;
		TextView tv_name;
		TextView tv_value;
	}
}
