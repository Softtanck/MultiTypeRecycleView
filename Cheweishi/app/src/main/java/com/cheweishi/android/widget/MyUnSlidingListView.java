package com.cheweishi.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by tangce on 4/13/2016.
 */
public class MyUnSlidingListView extends UnSlidingListView {
    public MyUnSlidingListView(Context context) {
        super(context);
    }

    public MyUnSlidingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyUnSlidingListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = meathureWidthByChilds() + getPaddingLeft() + getPaddingRight();
        super.onMeasure(MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
    }

    public int meathureWidthByChilds() {
        int maxWidth = 0;
        View view = null;
        for (int i = 0; i < getAdapter().getCount(); i++) {
            view = getAdapter().getView(i, view, this);
            view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            if (view.getMeasuredWidth() > maxWidth) {
                maxWidth = view.getMeasuredWidth();
            }
        }
        return maxWidth;
    }
}
