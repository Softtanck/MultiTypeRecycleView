/**************************************************************************************
 * [Project]
 * MyProgressDialog
 * [Package]
 * com.lxd.widgets
 * [FileName]
 * CustomProgressDialog.java
 * [Copyright]
 * Copyright 2012 LXD All Rights Reserved.
 * [History]
 * Version          Date              Author                        Record
 * --------------------------------------------------------------------------------------
 * 1.0.0           2012-4-27         lxd (rohsuton@gmail.com)        Create
 **************************************************************************************/

package com.cheweishi.android.utils;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;

import com.cheweishi.android.R;

/**
 * 执行耗时操作时弹出的dialog
 * @author 刘伟
 *
 */

public class CustomProgressDialog extends Dialog {
    private Context context = null;
    private static CustomProgressDialog customProgressDialog = null;

    private CustomProgressDialog(Context context) {
        super(context);
        try {
            this.context = context;

        } catch (Exception e) {
            // TODO: handle exception
            return;
        }
    }

    private CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    //实例化对象
    public static CustomProgressDialog getInstance(Context context) {
        customProgressDialog = new CustomProgressDialog(context, R.style.CustomProgressDialog);
        customProgressDialog.setContentView(R.layout.myprogress);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        return customProgressDialog;
    }

    public void onWindowFocusChanged(boolean hasFocus) {

        if (customProgressDialog == null) {
            return;
        }

        ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.progress);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.start();
    }

    public void onDestory() {
        if (null != customProgressDialog)
            customProgressDialog = null;
    }

}