package com.cheweishi.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * scrollview 里面的listview
 * 
 * @author zhangq<br>
 *         com.cheweishi.android.widget.MyListView
 */
public class MyListView extends ListView {

	public MyListView(Context context) {
		super(context);
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
