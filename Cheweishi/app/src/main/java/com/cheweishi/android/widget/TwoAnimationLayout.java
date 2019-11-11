package com.cheweishi.android.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

@SuppressLint("ResourceAsColor")
public class TwoAnimationLayout extends LinearLayout {

	public TwoAnimationLayout(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.setBackgroundColor(Color.WHITE);
		this.getBackground().setAlpha(200);
	}

	public TwoAnimationLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	public TwoAnimationLayout(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

}
