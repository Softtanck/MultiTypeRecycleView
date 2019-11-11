package com.cheweishi.android.widget;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;

/**
 * 
 * @author zhangq<br>
 *         com.cheweishi.android.widget.WrapSizeGridView
 * 
 */
public class WrapSizeGridView extends GridView {

	public WrapSizeGridView(Context context) {
		super(context);
	}

	public WrapSizeGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WrapSizeGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// int height = 0;
		// int width = 0;
		// int count = getNumColumns();
		// int total = getChildCount();
		// if (total == 0) {
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// return;
		// }
		// int hNum = total / count;
		//
		// for (int i = 0; i < count; i++) {
		// View child = getChildAt(0);
		// child.measure(
		// MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
		// heightMeasureSpec);
		// int w = child.getMeasuredWidth();
		// if (w > 0) {
		// width += w;
		// }
		// }
		// for (int i = 0; i < hNum; i++) {
		// View child = getChildAt(0);
		// child.measure(width,
		// MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		// int h = child.getMeasuredHeight();
		// if (h > 0) {
		// height += h;
		// }
		// }
		// widthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
		// MeasureSpec.EXACTLY);
		// heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
		// MeasureSpec.EXACTLY);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		measure(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
}
