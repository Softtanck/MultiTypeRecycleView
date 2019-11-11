package com.cheweishi.android.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
/**
 * 团队介绍
 * @author Xiaojin
 *
 */
public class TeamIntroduceActivity extends BaseActivity {
	@ViewInject(R.id.left_action)
	private Button btn_left;
	@ViewInject(R.id.title)
	private TextView tv_title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_introduce);
		ViewUtils.inject(TeamIntroduceActivity.this);
		tv_title.setText(R.string.team_introduction);
		btn_left.setText(R.string.back);
		btn_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
	}
}
