package com.cheweishi.android.activity;

import com.cheweishi.android.R;
import com.cheweishi.android.tools.APPTools;
import com.cheweishi.android.tools.AllCapTransformationMethod;
import com.cheweishi.android.tools.RegularExpressionTools;
import com.cheweishi.android.utils.MyMapUtils;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.StrericWheelAdapter;
import com.cheweishi.android.widget.WheelView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Xiaojin车辆管理-车牌号选择
 * 
 */
public class PlateChooseActivity extends BaseActivity {
	@ViewInject(R.id.ll_plate_choose_layout)
	private LinearLayout ll_plate_choose_layout;
	@ViewInject(R.id.ll_addPlateTopHide)
	private LinearLayout ll_addPlateTopHide;
	@ViewInject(R.id.tv_province)
	private Button tv_province;
	@ViewInject(R.id.et_plate)
	private EditText et_plate;
	@ViewInject(R.id.title)
	private TextView title;
	@ViewInject(R.id.left_action)
	private TextView left_action;
	@ViewInject(R.id.right_action)
	private TextView right_action;
	Button btn;
	private String longPro[];
	private String shortPro[];
	@ViewInject(R.id.pwd_cancel)
	private Button pwd_cancel;
	private String currentcity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.passw_layout);
		ViewUtils.inject(this);
		currentcity = MyMapUtils.getHistoryCity(PlateChooseActivity.this);
		if (!StringUtil.isEmpty(currentcity)
				&& (!StringUtil.isEquals(currentcity, "null", true))) {

		}
		title.setText(R.string.add_carPlate);
		left_action.setText(R.string.back);
		right_action.setText(R.string.finish);
		longPro = this.getResources().getStringArray(R.array.province_item);
		shortPro = this.getResources().getStringArray(
				R.array.province_short_item);
		// 我改的
		initWheel(R.id.passw_1,
				new String[] {
						getResources().getString(R.string.fomatCarPlate),
						getResources().getString(R.string.temporaryCarPlate) });
		initWheel(R.id.passw_2, shortPro);
		String pro = MyMapUtils.getProvince(PlateChooseActivity.this);
		boolean falg = false;
		for (int i = 0; i < longPro.length; i++) {
			if (!StringUtil.isEmpty(pro) && pro.contains(longPro[i])) {
				falg = true;
				tv_province.setText(shortPro[i]);
				break;
			}
		}
		if (falg == true) {

		} else {
			tv_province.setText(R.string.choose);
		}

		Intent intent = getIntent();
		if (intent.getStringExtra("plate") != null) {
			String plate = intent.getStringExtra("plate");
			if (plate.contains("车牌") || plate.replaceAll(" ", "").equals("")) {

			} else {
				tv_province.setText(plate.substring(0, 1));
				et_plate.setText(plate.substring(1, plate.length()));
			}
		}

		btn = (Button) findViewById(R.id.pwd_status);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv_province.setText(getWheelValue(R.id.passw_2));
				setRightDrawable(false);
				APPTools.closeBoard(PlateChooseActivity.this, et_plate);
				hideLinearLayout();
			}
		});
		pwd_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideLinearLayout();
			}
		});

		tv_province.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (ll_plate_choose_layout.getVisibility() == View.INVISIBLE) {
					setRightDrawable(true);
					APPTools.closeBoard(PlateChooseActivity.this, et_plate);
					showLinearLayout();

				} else {
					setRightDrawable(false);
					APPTools.closeBoard(PlateChooseActivity.this, et_plate);
					hideLinearLayout();

				}
			}
		});
		ll_addPlateTopHide.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		left_action.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub // Intent
				// intent)
				PlateChooseActivity.this.finish();
			}
		});
		right_action.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (et_plate.getText().toString().length() < 6
						|| tv_province.getText().toString().length() >= 2) {
					Toast.makeText(PlateChooseActivity.this,
							R.string.please_enter_rightPlate, Toast.LENGTH_LONG)
							.show();
				} else {
					if (RegularExpressionTools.isCarPlate((tv_province
							.getText().toString() + et_plate.getText()
							.toString()).toUpperCase()) == true) {
						APPTools.closeBoard(PlateChooseActivity.this, et_plate);
						Intent aintent = new Intent(PlateChooseActivity.this,
								AddCarActivity.class);
						aintent.putExtra("province", tv_province.getText()
								.toString());
						aintent.putExtra("plate", et_plate.getText().toString()
								.toUpperCase());
						/* 将数据打包到aintent Bundle 的过程略 */
						PlateChooseActivity.this.setResult(RESULT_OK, aintent); // intent)
						PlateChooseActivity.this.finish();
					} else {
						Toast.makeText(PlateChooseActivity.this,
								R.string.please_enter_rightPlate,
								Toast.LENGTH_LONG).show();
					}

				}
			}
		});
		et_plate.setTransformationMethod(new AllCapTransformationMethod());
		et_plate.setSelection(et_plate.getText().toString().length());
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		APPTools.closeBoard(PlateChooseActivity.this, et_plate);
		setContentView(R.layout.null_view);
	}

	private String getWheelValue(int id) {
		return getWheel(id).getCurrentItemValue();
	}

	// 取值的方法
	// getWheelValue(R.id.passw_1)
	// getWheelValue(R.id.passw_2)

	/**
	 * Returns wheel by Id
	 * 
	 * @param id
	 *            the wheel Id
	 * @return the wheel with passed Id 我添加的
	 * 
	 */
	private WheelView getWheel(int id) {
		return (WheelView) findViewById(id);
	}

	private void initWheel(int id, String[] strContents) {
		WheelView wheel = getWheel(id);
		wheel.setAdapter(new StrericWheelAdapter(strContents));
		wheel.setCurrentItem(0);

		wheel.setCyclic(false);
		wheel.setInterpolator(new AnticipateOvershootInterpolator());
		if (id == R.id.passw_2) {
			wheel.setCyclic(true);
		}
	}

	private void showLinearLayout() {
		final Animation animation1 = AnimationUtils.loadAnimation(
				PlateChooseActivity.this, R.anim.score_business_query_enter);
		ll_plate_choose_layout.startAnimation(animation1);
		ll_plate_choose_layout.setVisibility(View.VISIBLE);
		ll_addPlateTopHide.startAnimation(animation1);
		ll_addPlateTopHide.setVisibility(View.VISIBLE);
	}

	private void hideLinearLayout() {
		final Animation animation1 = AnimationUtils.loadAnimation(
				PlateChooseActivity.this, R.anim.score_business_query_exit);
		ll_plate_choose_layout.startAnimation(animation1);
		ll_plate_choose_layout.setVisibility(View.INVISIBLE);
		ll_addPlateTopHide.startAnimation(animation1);
		ll_addPlateTopHide.setVisibility(View.INVISIBLE);
	}

	private void setRightDrawable(boolean flag) {
		if (flag == false) {
			Drawable drawable = getResources().getDrawable(
					R.drawable.tianjiacar_right2x);

			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight()); // 设置边界

			tv_province.setCompoundDrawablesWithIntrinsicBounds(null, null,
					drawable, null);// 画在右边
		} else {
			Drawable drawable = getResources().getDrawable(
					R.drawable.tianjiacar_right2x);

			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight()); // 设置边界

			tv_province.setCompoundDrawables(null, null, drawable, null);// 画在右边
		}
	}
}
