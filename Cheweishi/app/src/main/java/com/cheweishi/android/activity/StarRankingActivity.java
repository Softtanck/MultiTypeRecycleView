package com.cheweishi.android.activity;

import com.cheweishi.android.R;
import com.cheweishi.android.R.layout;
import com.cheweishi.android.R.menu;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class StarRankingActivity extends BaseActivity {
	@ViewInject(R.id.start_ranking_layout)
	private LinearLayout start_ranking_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_start_ranking);
		start_ranking_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				StarRankingActivity.this.finish();
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_ranking, menu);
		return true;
	}

}
