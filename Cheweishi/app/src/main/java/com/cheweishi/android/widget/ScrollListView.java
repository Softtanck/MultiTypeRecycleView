package com.cheweishi.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
/**
 *com.cheweishi.android.widget.ScrollListView
 * 嵌套到scrollView中使用的ListView
 * @author mingdasen
 *
 */
public class ScrollListView extends ListView {

	public ScrollListView(Context context) {
		this(context, null);
	}

	public ScrollListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public ScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override  
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
	    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
	            MeasureSpec.AT_MOST);  
	    super.onMeasure(widthMeasureSpec, expandSpec);  
	} 
}
