package com.cheweishi.android.activity;

import com.cheweishi.android.R;
import com.cheweishi.android.R.layout;
import com.cheweishi.android.R.menu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class IntegralHelpActivity extends BaseActivity {

	
	@ViewInject(R.id.left_action)
	private Button mLeft;
	
	@ViewInject(R.id.title)
	private TextView mtitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integral_help);
		ViewUtils.inject(this);
		init();
	}

	//初始化方法
	private void init() {
		// TODO Auto-generated method stub
		mtitle.setText(getString(R.string.integral_help));
		mLeft.setText(getResources().getString(R.string.back));
		mLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				IntegralHelpActivity.this.finish();
			}
		});
	}


}
