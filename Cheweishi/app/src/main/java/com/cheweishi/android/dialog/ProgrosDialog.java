package com.cheweishi.android.dialog;

import com.cheweishi.android.R;
import com.cheweishi.android.activity.BaseActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.ImageView;

public class ProgrosDialog extends BaseDialog {

    private static ProgrosDialog mProgrosDialog;
    private ImageView imageView;
    private AnimationDrawable animationDrawable;
    private static boolean isDismiss;

    private ProgrosDialog(Context context, int style) {
        super(context);
        isDismiss = true;
    }

    @Override
    public void setWindow() {
        mProgrosDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
    }

    public static synchronized void openDialog(Context context) {
        try {
            if (mProgrosDialog == null) {
                mProgrosDialog = new ProgrosDialog(context,
                        R.style.CustomProgressDialog);
                CanceledOnTouchOutside(false);
            }
            mProgrosDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initContentView() {
        // TODO Auto-generated method stub
        this.setContentView(R.layout.myprogress);
    }

    @Override
    public void initViews() {
        imageView = (ImageView) this.findViewById(R.id.progress);
    }

    @Override
    public void initDatas() {
        animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }

    public static void closeProgrosDialog() {
        try {
            if (mProgrosDialog != null) {
                mProgrosDialog.dismiss();
            }
            mProgrosDialog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回
     */
    @Override
    public void onBackPressed() {
        if (isDismiss == true) {
            closeProgrosDialog();
            if (null != mContext) {
                ((BaseActivity) mContext).netWorkHelper.stop();
            }
//            claoseActivity();
        }
        super.onBackPressed();
    }

    /**
     * 返回的时候同时finishActivity
     */
    private void claoseActivity() {
        ((BaseActivity) mContext).finish();
        if (mContext != null) {
            mContext = null;
        }
    }

    /**
     * 设置progress点击屏幕是否dismiss
     *
     * @param b
     */
    public static void CanceledOnTouchOutside(boolean b) {
        mProgrosDialog.setCancelable(b);
    }

    /**
     * 判断progress是否正在show
     *
     * @return
     */
    public static boolean isProgressShowing() {
        if (mProgrosDialog == null) {
            return false;
        } else {
            return mProgrosDialog.isShowing();
        }
    }

    /**
     * 设置点击系统返回键是否dismiss
     *
     * @param b
     */
    public static void setIsDismiss(boolean b) {
        isDismiss = b;
    }
}
