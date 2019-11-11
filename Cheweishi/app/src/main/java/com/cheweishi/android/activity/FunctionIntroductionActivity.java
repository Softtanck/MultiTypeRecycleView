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

public class FunctionIntroductionActivity extends BaseActivity {

	@ViewInject(R.id.tv_name)
	private TextView tv_name;
	@ViewInject(R.id.tv_content)
	private TextView tv_content;
	@ViewInject(R.id.ll_function)
	private LinearLayout layout;
	@ViewInject(R.id.title)
	private TextView title;
	@ViewInject(R.id.left_action)
	private Button letf_action;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_function_introduction);
		ViewUtils.inject(this);
		title.setText(R.string.function_introduction);
		letf_action.setText(R.string.back);
		letf_action.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
