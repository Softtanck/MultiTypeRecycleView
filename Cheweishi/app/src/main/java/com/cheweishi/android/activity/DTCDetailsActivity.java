package com.cheweishi.android.activity;

import com.cheweishi.android.R;
import com.cheweishi.android.config.Constant;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 故障详情界面
 * @author mingdasen
 *
 */
public class DTCDetailsActivity extends BaseActivity {
	@ViewInject(R.id.tv_dtc_code)
	private TextView tv_dtc_code;
	@ViewInject(R.id.tv_dtc_explain)
	private TextView tv_dtc_explain;
	@ViewInject(R.id.btn_solve_dtc)
	private Button btn_solve_dtc;
	@ViewInject(R.id.left_action)
	private Button left_action;
	@ViewInject(R.id.title)
	private TextView tv_title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dtc_details);
		ViewUtils.inject(this);
		initView();
	}
	/**
	 * 初始化
	 */
	private void initView() {
		tv_title.setText(R.string.dtc_details);
		left_action.setText(R.string.back);
		left_action.setOnClickListener(clickListener);
		btn_solve_dtc.setOnClickListener(clickListener);
		
		tv_dtc_code.setText(getIntent().getStringExtra("name"));
		tv_dtc_explain.setText(getIntent().getStringExtra("des"));
	}
	/**
	 * 点击事件
	 */
	OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.left_action://返回
				finish();
				break;
//			case R.id.btn_solve_dtc://联系专家
//				Constant.Server_Hall_index = 4;
//				Intent intent = new Intent(DTCDetailsActivity.this,DrivingActivity.class);
//				startActivity(intent);
//				break;
			default:
				break;
			}
		}
	};
}
