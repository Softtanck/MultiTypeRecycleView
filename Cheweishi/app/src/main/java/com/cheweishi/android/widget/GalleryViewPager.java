package com.cheweishi.android.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

/**
 * Created by 祥宁 on 2015/6/26.
 */
public class GalleryViewPager extends ViewPager {

    private DisplayMetrics displayMetrics;

    public GalleryViewPager(Context context) {
        super(context);
        init();
    }

    public GalleryViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        displayMetrics = getContext().getResources().getDisplayMetrics();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels - getPageMargin() * 4, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

