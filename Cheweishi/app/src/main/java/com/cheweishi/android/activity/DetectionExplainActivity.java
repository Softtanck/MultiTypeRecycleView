package com.cheweishi.android.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.DetectionExplainAdapter;
import com.cheweishi.android.entity.DetectionInfoNative;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
/**
 * 检测项说明
 * @author mingdasen
 *
 */
public class DetectionExplainActivity extends BaseActivity{
	@ViewInject(R.id.lv_detection_explain)
	private ListView listView;
	@ViewInject(R.id.left_action)
	private Button left_action;
	@ViewInject(R.id.title)
	private TextView title;
	private ArrayList<DetectionInfoNative> list;
	private DetectionExplainAdapter adapter;
	private String[] name;
	private String[] value;
	private DetectionInfoNative detectionInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detection_explain);
		ViewUtils.inject(this);
		initView();
	}
	/**
	 * 初始化布局
	 */
	private void initView() {
		left_action.setText(R.string.back);
		title.setText(R.string.test_option_explain);
		list = new ArrayList<DetectionInfoNative>();
		setListData();
//		list = getIntent().getParcelableArrayListExtra("deteList");
		adapter = new DetectionExplainAdapter(this, list);
		listView.setAdapter(adapter);
		left_action.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	
	private void setListData(){
		name = getResources().getStringArray(R.array.detection_item_explain_name);
		value = getResources().getStringArray(R.array.detection_item_explain_range);
		for (int i = 0; i < name.length; i++) {
			detectionInfo = new DetectionInfoNative();
			detectionInfo.setName(name[i]);
			detectionInfo.setValue(value[i]);
			list.add(detectionInfo);
		}
	}
}
