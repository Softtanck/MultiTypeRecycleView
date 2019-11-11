package com.cheweishi.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.cheweishi.android.utils.LogHelper;

/**
 * 自定义不可滑动的ListView
 *
 * @author mingdasen
 */
//com.cheweishi.android.widget.UnSlidingExpandableListView
public class UnSlidingExpandableListView extends ExpandableListView {

    int oldY;

    public UnSlidingExpandableListView(Context context) {
        super(context);
    }

    public UnSlidingExpandableListView(Context context, AttributeSet attrs) {

        super(context, attrs);

    }

    public UnSlidingExpandableListView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

    }

//	@Override
//
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//	         if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//	              return true;  //禁止ListView滑动
//
//	         }
//	         return super.dispatchTouchEvent(ev);
//
//	}

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        LogHelper.d("onInterceptTouchEvent");
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                oldY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (10 >= Math.abs(ev.getY() - oldY)) // 小于10才拦截
                    return true;//拦截
                break;

        }
        return super.onInterceptTouchEvent(ev);
    }

}
