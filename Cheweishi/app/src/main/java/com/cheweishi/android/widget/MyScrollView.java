package com.cheweishi.android.widget;

import com.cheweishi.android.interfaces.ScrollViewListener;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 去掉移动事件的scrollview
 *
 * @author zhangq<br>
 *         20150623 <br>
 *         com.cheweishi.android.widget.MyScrollVie
 */
public class MyScrollView extends ScrollView {
    private ScrollViewListener mListener;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    private boolean isMove;

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                isMove = false;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                isMove = true;
//                return false;
//            case MotionEvent.ACTION_UP:
//                if (isMove == true) {
//                    return false;
//                }
//                break;
//            default:
//                break;
//        }
//        return super.onTouchEvent(ev);
//    }
//
//    @Override
//    public void fling(int velocityY) {
//        super.fling(velocityY / 2);
//    }

    public void setScrollViewListener(ScrollViewListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != mListener)
            mListener.onScrollChanged(this, l, t, oldl, oldt);
    }
}
