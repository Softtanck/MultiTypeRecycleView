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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class FootmarkSettingActivity extends BaseActivity {
	
	@ViewInject(R.id.zuji_checkbox_to_checkbox)
	private CheckBox mCheckBox;
	
	@ViewInject(R.id.left_action)
	private Button mLeftAction;
	
	@ViewInject(R.id.title)
	private TextView mTitle;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_footmark_setting);
		ViewUtils.inject(this);
		setText();
		initLinener();
	}


	private void initLinener() {
		mLeftAction.setOnClickListener(listener);
		mCheckBox.setOnCheckedChangeListener(onCheckedChangeListener);
	}


	private void setText() {
		mLeftAction.setText(R.string.back);
		mTitle.setText("足迹设置");
	}
	
	
	private OnClickListener listener=new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			FootmarkSettingActivity.this.finish();
		}
	};

	private OnCheckedChangeListener onCheckedChangeListener=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			
		}
	};
}
