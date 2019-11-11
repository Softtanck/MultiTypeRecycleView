package com.cheweishi.android.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 隐私设置
 * 
 * @author zhangq
 * 
 */
public class PrivacySettingsActivity extends BaseActivity {
	@ViewInject(R.id.title)
	private TextView tvTitle;
	@ViewInject(R.id.left_action)
	private Button tvLeft;
	@ViewInject(R.id.right_action)
	private TextView tvRight;
	@ViewInject(R.id.llayout_test)
	private LinearLayout lLayoutTest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_privacy_settings);

		init();
	}

	private void init() {
		ViewUtils.inject(this);

		tvTitle.setText("隐私设置");
		tvRight.setVisibility(View.INVISIBLE);
		tvLeft.setText("返回");

		tvLeft.setOnClickListener(onClickListener);
		lLayoutTest.setOnClickListener(onClickListener);
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.left_action:
				PrivacySettingsActivity.this.finish();
				break;
			case R.id.llayout_test:
//				UmengUpdateAgent.forceUpdate(PrivacySettingsActivity.this);
				break;
			default:
				break;
			}
		}
	};
}
