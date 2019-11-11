package com.cheweishi.android.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.Gallery;

import com.cheweishi.android.utils.LogHelper;

import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("deprecation")
public class MyGallery extends Gallery {


    private static final int timerAnimation = 1;
    private final Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case timerAnimation:
                    int position = getSelectedItemPosition();
//                    LogHelper.d("我在正在滚动:" + position);
                    if (-1 == position) {
                        destroy();
                        return;
                    }
                    onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
//                    if (position >= (getCount() % -1)) {
//                        onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
//                    } else {
//                        onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
//                    }
//                    setSelection((position + 1) % 6, true);
                    break;

                default:
                    break;
            }
        }

    };

    private Timer timer = new Timer();
    private TimerTask task = new TimerTask() {
        public void run() {
            mHandler.sendEmptyMessage(timerAnimation);
        }
    };
    private float oldX;

    public MyGallery(Context paramContext) {
        super(paramContext);
//        timer.schedule(task, 3000, 3000);
    }

    public MyGallery(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
//        timer.schedule(task, 3000, 3000);

    }

    public MyGallery(Context paramContext, AttributeSet paramAttributeSet,
                     int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
//        timer.schedule(task, 3000, 3000);

    }

    private boolean isScrollingLeft(MotionEvent paramMotionEvent1,
                                    MotionEvent paramMotionEvent2) {
        float f1, f2;
        f1 = null != paramMotionEvent1 ? paramMotionEvent1.getX() : oldX;
        f2 = null != paramMotionEvent2 ? paramMotionEvent2.getX() : 0;
//        LogHelper.d("onFling:" + f1 + "----2:" + f2);
        return f2 > f1;
    }

    @Override
    public boolean onFling(MotionEvent paramMotionEvent1,
                           MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
//        LogHelper.d("onFling");
        int keyCode;
        if (isScrollingLeft(paramMotionEvent1, paramMotionEvent2)) {
            keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
        } else {
            keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
        }
//        LogHelper.d("onFling:" + keyCode);
        onKeyDown(keyCode, null);
        start();
        return true;
    }

//    @Override
//    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
//        super.setOnItemSelectedListener(listener);
//        LogHelper.d("----");
//        start();
//    }

    public void destroy() {
        if (null != timer) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        if (null != task) {
            task.cancel();
            task = null;
        }
    }

    public void pause() {
        if (null != timer) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        if (null != task) {
            task.cancel();
            task = null;
        }
    }

    public void start() {
//        LogHelper.d("我开始滚动了");
        destroy();
        if (null == timer)
            timer = new Timer();
        if (null == task)
            task = new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(timerAnimation);
                }
            };
        timer.purge();
        timer.schedule(task, 3000, 3000);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                oldX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (10 < Math.abs(ev.getX() - oldX)) {
                    pause();
                    return true;
                }
                break;
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//                LogHelper.d("开始滚动");
//                start();
//                break;
        }

        return super.onInterceptTouchEvent(ev);
    }
}