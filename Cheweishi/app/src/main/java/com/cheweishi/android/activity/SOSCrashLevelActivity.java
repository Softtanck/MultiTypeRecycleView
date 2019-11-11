package com.cheweishi.android.activity;

import com.cheweishi.android.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class SOSCrashLevelActivity extends BaseActivity implements
		OnClickListener, OnCheckedChangeListener {

	@ViewInject(R.id.rg_sos_crashLevel)
	private RadioGroup rg_sos_crashLevel;
	@ViewInject(R.id.title)
	private TextView title;
	@ViewInject(R.id.left_action)
	private TextView left_action;
	@ViewInject(R.id.right_action)
	private TextView right_action;
	private int level;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_soscrash_level);
		ViewUtils.inject(this);
		initViews();
		setListeners();
	}

	private void initViews() {
		level = getIntent().getIntExtra("crashLevel", 0);
		if (level == 1) {
			((RadioButton) rg_sos_crashLevel
					.findViewById(R.id.rb_sos_crashLevel_one)).setChecked(true);
		} else if (level == 2) {
			((RadioButton) rg_sos_crashLevel
					.findViewById(R.id.rb_sos_crashLevel_two)).setChecked(true);
		} else {
			((RadioButton) rg_sos_crashLevel
					.findViewById(R.id.rb_sos_crashLevel_three))
					.setChecked(true);
		}
		title.setText(R.string.sos_crash_level);
		left_action.setText(R.string.back);

	}

	private void setListeners() {
		rg_sos_crashLevel.setOnCheckedChangeListener(this);
		left_action.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.left_action:
			finish();
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		switch (arg1) {
		case R.id.rb_sos_crashLevel_one:
			level = 1;
			break;
		case R.id.rb_sos_crashLevel_two:
			level = 2;
			break;
		case R.id.rb_sos_crashLevel_three:
			level = 3;
			break;
		}
		Intent intent = new Intent();
		intent.putExtra("crashLevel", level);
		setResult(101, intent);
		finish();
	}

}
