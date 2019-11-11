package com.cheweishi.android.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * 
 * @author zhangq
 *         <p>
 *         com.cheweishi.android.widget.WrapContentHeightViewPager
 *         </p>
 */
public class WrapContentHeightViewPager extends ViewPager {
	/**
	 * 默认两行
	 */
	private int rowNumber = 2;

	public WrapContentHeightViewPager(Context context) {
		super(context);
	}

	public WrapContentHeightViewPager(Context context, int rowNumber) {
		super(context);
		this.rowNumber = rowNumber;
	}

	public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int height = 0;
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			child.measure(widthMeasureSpec,
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			int h = child.getMeasuredHeight();
			if (h > 0) {
				height = h * rowNumber;
			}
		}
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
				MeasureSpec.EXACTLY);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
