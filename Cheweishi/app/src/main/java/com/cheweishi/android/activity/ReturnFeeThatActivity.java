package com.cheweishi.android.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


/****
 * 返费说明
 * @author lw
 *
 */
public class ReturnFeeThatActivity extends BaseActivity {

	@ViewInject(R.id.left_action)
	private Button left;
	
	@ViewInject(R.id.title)
	private TextView title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_return_fee_that);
		ViewUtils.inject(this);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		title.setText(getString(R.string.backmoney_help));
		left.setText(getResources().getString(R.string.back));
		left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ReturnFeeThatActivity.this.finish();
			}
		});
		
		
	}

}
