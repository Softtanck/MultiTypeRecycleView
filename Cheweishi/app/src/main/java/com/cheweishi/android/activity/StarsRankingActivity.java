package com.cheweishi.android.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.cheweishi.android.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class StarsRankingActivity extends BaseActivity {
	@ViewInject(R.id.star_ranking_layout)
	private LinearLayout star_ranking_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stars_ranking);
		ViewUtils.inject(this);
		star_ranking_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				StarsRankingActivity.this.finish();
			}

		});
	}

}
