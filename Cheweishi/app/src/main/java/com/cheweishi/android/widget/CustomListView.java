package com.cheweishi.android.widget;

import android.content.Context;
import android.widget.ListView;

public class CustomListView extends ListView{

	public CustomListView(Context context) {
		super(context);
	}
	 @Override
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
	        super.onMeasure(widthMeasureSpec, expandSpec);
	    }
}
