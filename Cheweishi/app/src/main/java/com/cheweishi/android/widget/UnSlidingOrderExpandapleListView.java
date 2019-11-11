package com.cheweishi.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ExpandableListView;
/**
 * 自定义不可滑动的ListView
 * @author mingdasen
 *
 */
//com.cheweishi.android.widget.UnSlidingOrderExpandapleListView
public class UnSlidingOrderExpandapleListView extends ExpandableListView {

	public UnSlidingOrderExpandapleListView(Context context) {
		super(context);
	}

	public UnSlidingOrderExpandapleListView(Context context, AttributeSet attrs) {

		super(context, attrs);

	}

	public UnSlidingOrderExpandapleListView(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);

	}
	
	@Override

	public boolean dispatchTouchEvent(MotionEvent ev) {
	         if (ev.getAction() == MotionEvent.ACTION_MOVE) {
	              return true;  //禁止ListView滑动

	         }
	         return super.dispatchTouchEvent(ev);

	}
	
	@Override   
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {   
        int expandSpec = MeasureSpec.makeMeasureSpec(   
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);   
        super.onMeasure(widthMeasureSpec, expandSpec);   
    } 
}
