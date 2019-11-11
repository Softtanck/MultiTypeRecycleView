package com.cheweishi.android.activity;

import com.cheweishi.android.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MaintainOrderDetilsActivity extends BaseActivity{

	
	@ViewInject(R.id.left_action)
	private Button left_action;
	@ViewInject(R.id.title)
	private TextView title;
	@ViewInject(R.id.right_action)
	private TextView right_action;
	@ViewInject(R.id.lay_saoyisao)
	private LinearLayout lay_saoyisao;
	@ViewInject(R.id.lay_service)
	private LinearLayout lay_service;
	@ViewInject(R.id.tv_order_title)
	private TextView tv_order_title;
	@ViewInject(R.id.rel_daodian)
	private RelativeLayout rel_daodian;
	@ViewInject(R.id.tv_line_right)
	private TextView tv_line_right;
	@ViewInject(R.id.tv_ok)
	private TextView tv_ok;
	@ViewInject(R.id.tv_yuyue)
	private TextView tv_yuyue;
	@ViewInject(R.id.tv_line_left)
	private TextView tv_line_left;
	
	private Intent intent;
	public static MaintainOrderDetilsActivity order;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_order);
		ViewUtils.inject(this);
		order = this;
		init();
	}
	private void init() {
		left_action.setText("返回");
		title.setText("订单详情");
		right_action.setText("取消订单");
		right_action.setTextColor(getResources().getColor(R.color.blue));
		Intent intentGet = getIntent();
		String cancel = intentGet.getStringExtra("cancel");
		if(cancel != null && cancel.equals("cancel")){
			lay_saoyisao.setVisibility(View.GONE);
			lay_service.setVisibility(View.GONE);
			right_action.setVisibility(View.INVISIBLE);
			tv_order_title.setText("订单已取消");
			rel_daodian.setVisibility(View.INVISIBLE);
			tv_ok.setText("已经取消");
			tv_ok.setTextColor(getResources().getColor(R.color.blue));
			tv_yuyue.setTextColor(getResources().getColor(R.color.blue));
		}
	}
	
	@OnClick({R.id.left_action,R.id.right_action})
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.left_action://返回
			MaintainOrderDetilsActivity.this.finish();
			break;
		case R.id.right_action:
//			intent = new Intent(MaintainOrderDetilsActivity.this,MaintainCancelActivity.class);
//			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
