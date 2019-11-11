package com.cheweishi.android.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cheweishi.android.R;
import com.cheweishi.android.tools.ScreenTools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

/**
 * 城市或者字幕grid选择
 * 
 * @author Xiaojin
 * 
 */
public class ProvinceAndCharGridActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	@ViewInject(R.id.gv_provinceChar)
	private GridView gview;
	@ViewInject(R.id.left_action)
	private Button left_action;
	@ViewInject(R.id.title)
	private TextView title;
	@ViewInject(R.id.right_action)
	private TextView right_action;
	private List<Map<String, Object>> data_list;
	private String provinceAndCharArray[];
	private String provinceAndCharLongArray[];
	private boolean flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_province_and_char_grid);
		ViewUtils.inject(this);
		initViews();
	}

	/**
	 * 初始化视图F
	 */
	private void initViews() {
		title.setText(R.string.title_activity_province_and_char_grid);
		left_action.setText(R.string.back);
		data_list = new ArrayList<Map<String, Object>>();
		flag = getIntent().getBooleanExtra("flag", false);
		// 新建List
		if (flag == false) {// 根据标志位获取字母数组
			provinceAndCharArray = getResources().getStringArray(
					R.array.char_array);

		} else {// 根据标志位获取省份简称数组
			provinceAndCharArray = getResources().getStringArray(
					R.array.province_short_item);
		}
		provinceAndCharLongArray = getResources().getStringArray(
				R.array.province_item);
		gview.setColumnWidth(ScreenTools.getScreentWidth(this) / 3);
		getData();
		GridViewAdapter adapter = new GridViewAdapter(this, data_list);
		// 获取数据
		gview.setAdapter(adapter);
		gview.setOnItemClickListener(this);
	}

	public void getData() {
		// cion和iconName的长度是相同的，这里任选其一都可以
		for (int i = 0; i < provinceAndCharArray.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (flag == false) {
				map.put("provinceAndChar", provinceAndCharArray[i]);
			} else {
				map.put("provinceAndChar", provinceAndCharArray[i] + "("
						+ provinceAndCharLongArray[i] + ")");
			}
			data_list.add(map);
		}
	}

	@OnClick({ R.id.left_action })
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.left_action:
			finish();
			break;
		}
	}

	class GridViewAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		public GridViewAdapter(Context context, List<Map<String, Object>> list) {
			super();
			this.list = list;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return list.size();
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
		public View getView(int position, View convertView, ViewGroup arg2) {
			Viewholder viewholder = null;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.item_provinceandchar_grid, null);
				viewholder = new Viewholder(convertView);
				convertView.setTag(viewholder);
			} else {
				viewholder = (Viewholder) convertView.getTag();
			}
			viewholder.update(list.get(position), position);
			return convertView;
		}

		public class Viewholder {
			private TextView tv_provinceAndChar;
			private TextView tv_lineView;

			public Viewholder(View convertView) {
				tv_provinceAndChar = (TextView) convertView
						.findViewById(R.id.tv_provinceAndChar);
				tv_lineView = (TextView) convertView
						.findViewById(R.id.tv_lineView);
			}

			public void update(Map<String, Object> map, int position) {
				tv_provinceAndChar.setText(map.get("provinceAndChar")
						.toString());
				setLine(position, tv_lineView);
			}
		}

		private void setLine(int position, View view) {
			int i = 0;
			i = list.size() % 3;
			if (position + i + 1 > list.size()) {
				// 最后一行分割线隐藏
				view.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		if (flag == false) {
			intent.putExtra("item", data_list.get(arg2).get("provinceAndChar")
					.toString());
		} else {
			intent.putExtra("item", provinceAndCharArray[arg2]);
		}
		setResult(RESULT_OK, intent);
		finish();
	}
}
