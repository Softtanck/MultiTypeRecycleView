package com.cheweishi.android.activity;

import com.cheweishi.android.R;
import com.cheweishi.android.config.Constant;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import android.os.Bundle;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 注册完成
 * 
 * @author Xiaojin
 * 
 */
public class RegistFinishActivity extends BaseActivity implements
		OnClickListener {

	@ViewInject(R.id.btn_add_car_cancel)
	private Button btn_add_car_cancel;
	@ViewInject(R.id.btn_add_car)
	private Button btn_add_car;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist_finish);
		ViewUtils.inject(this);
	}

	@OnClick({ R.id.btn_add_car, R.id.btn_add_car_cancel })
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_add_car:// 绑定设备
			goToAdd();
			break;
		case R.id.btn_add_car_cancel:// 先逛逛
			goToMain();
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stu
		return false;
	}

	private void goToAdd() {
		// 发送广播 刷新主界面界面
		Intent mIntent = new Intent();
		Constant.CURRENT_REFRESH = Constant.LOGIN_REFRESH;
		mIntent.setAction(Constant.REFRESH_FLAG);
		sendBroadcast(mIntent);
		Intent intent = new Intent(RegistFinishActivity.this,
				AddCarActivity.class);
		intent.putExtra("AddCarActivtiy", "AddCarActivtiy");
		startActivity(intent);
		RegistFinishActivity.this.finish();
	}

	private void goToMain() {
		// 发送广播 刷新主界面界面
		Intent mIntent = new Intent();
		Constant.CURRENT_REFRESH = Constant.LOGIN_REFRESH;
		mIntent.setAction(Constant.REFRESH_FLAG);
		sendBroadcast(mIntent);
		RegistFinishActivity.this.finish();
	}
}
