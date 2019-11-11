package com.cheweishi.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.widget.DateSelectorNotFrameWheelView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 选择出生日期
 * 
 * @author mingdasen
 * 
 */
public class ChooseDateBirthActivity extends BaseActivity {
	@ViewInject(R.id.left_action)
	private Button btn_back;
	@ViewInject(R.id.title)
	private TextView tv_title;
	@ViewInject(R.id.right_action)
	private TextView tv_finish;
	@ViewInject(R.id.tv_age)
	private TextView tv_age;
	@ViewInject(R.id.wv_selectorDate)
	private DateSelectorNotFrameWheelView wheelView;
	private String age = "0";
	private String birth = "";
	public static ChooseDateBirthActivity instance;//activity实例
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_date_birth);
		ViewUtils.inject(this);
		intiView();
		instance = this;
	}

	/**
	 * 初始化布局
	 */
	private void intiView() {
		btn_back.setText(R.string.back);
		tv_title.setText(R.string.choice_barth_date);
		tv_finish.setText(R.string.finish);
		btn_back.setOnClickListener(btnClick);
		tv_finish.setOnClickListener(btnClick);
	}
	/**
	 * 点击监听事件
	 */
	OnClickListener btnClick = new OnClickListener() {

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.left_action://返回
				finish();
				break;
			case R.id.right_action://完成
				Intent intent = new Intent();
				intent.putExtra("age", age);
				intent.putExtra("birth", birth);
				setResult(RESULT_OK, intent);
				finish();
				break;
			default:
				break;
			}
		}
	};
	
	/**
	 * 设置年龄和生日
	 * @param age
	 * @param birth
	 */
	public void setAgeText(int age, String birth){
		this.age = age + "";
		this.birth = birth;
		if (tv_age != null) {
			tv_age.setText(this.age + "");
		}
	}
}
