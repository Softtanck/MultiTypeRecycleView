package com.cheweishi.android.dialog;
import com.cheweishi.android.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.DisplayMetrics;
import android.view.View;

public abstract class BaseDialog extends Dialog implements View.OnClickListener {

	public Context mContext;


	public BaseDialog(Context context) {
		super(context, R.style.DialogTheme);
		this.mContext = context;
	}

	public BaseDialog(Context context, int style) {
		super(context, style);
		this.mContext = context;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/** 注册每个Dialog的布局 */
		this.initContentView();
		/** 初始化View */
		this.initViews();
		/**设置Dialog大小**/
		this.setWindow();
		/** 初始化数据 */
		this.initDatas();
		/** 初始化View的监听方法 */
		this.initLists();
	}

	/** 此项必重写(参考CameraDialog) */
	public abstract void initContentView();

	/** 此项必重写(参考CameraDialog) */
	public abstract void initViews();

	public void setWindow() {
		// 适应屏幕
		DisplayMetrics mDisplayMetrics = this.getContext().getResources()
				.getDisplayMetrics();
		if (mDisplayMetrics.widthPixels < mDisplayMetrics.heightPixels) {
			int paddWidth = mDisplayMetrics.widthPixels / 6;
			getWindow().setLayout(mDisplayMetrics.widthPixels - paddWidth,
					LayoutParams.WRAP_CONTENT);
		} else {
			int paddWidth = mDisplayMetrics.widthPixels / 2;
			getWindow().setLayout(mDisplayMetrics.widthPixels - paddWidth,
					LayoutParams.WRAP_CONTENT);
		}
	}

	public void initLists() {
		
	}

	public void initDatas() {
		
	}

	public void onClick(View view) {
		
	}

}
