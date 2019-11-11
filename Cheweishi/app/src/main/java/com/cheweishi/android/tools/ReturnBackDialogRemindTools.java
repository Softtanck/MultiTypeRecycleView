package com.cheweishi.android.tools;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;

import com.cheweishi.android.R;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.CustomDialog;

public class ReturnBackDialogRemindTools {

    public static CustomDialog.Builder builder;
    public static CustomDialog phoneDialog;
    public static ReturnBackDialogRemindTools instance;

    public static ReturnBackDialogRemindTools getInstance(
            final Activity mContext) {
        if (instance == null) {
            instance = new ReturnBackDialogRemindTools();
        }
        builder = new CustomDialog.Builder(mContext);
//		builder.setMessage(R.string.message_reLogin);
        builder.setTitle(R.string.remind);
        builder.setMessage(R.string.back_save_remind);
        builder.setPositiveButton(R.string.sure,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mContext.finish();
                        builder = null;
                        phoneDialog = null;
                        instance = null;
                    }
                });

        builder.setNegativeButton(R.string.cancel,
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return instance;
    }

    public void show() {
        if (!StringUtil.isEmpty(builder)) {
            phoneDialog = builder.create();
            phoneDialog.show();
        }
    }


    public static ReturnBackDialogRemindTools getCheckInstance(
            final Activity mContext) {
        if (instance == null) {
            instance = new ReturnBackDialogRemindTools();
        }
        builder = new CustomDialog.Builder(mContext);
//		builder.setMessage(R.string.message_reLogin);
        builder.setContentView(View.inflate(mContext, R.layout.insurance_check_view, null));
//        builder.setTitle(R.string.remind);
//        builder.setMessage(R.string.back_save_remind);
//        builder.setPositiveButton(R.string.sure,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        mContext.finish();
//                    }
//                });
//
//        builder.setNegativeButton(R.string.cancel,
//                new android.content.DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
        return instance;
    }
}
