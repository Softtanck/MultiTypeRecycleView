package com.cheweishi.android.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

import com.cheweishi.android.activity.LoginActivity;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.CustomDialog;

/**
 * 登录状态异常提示dialog
 * 
 * @author 大森
 * 
 */
public class ReLoginDialog {

	private CustomDialog.Builder builder;
	private CustomDialog dialog;
	private static Context mContext;
	private static ReLoginDialog reLoginDialog;

	public static ReLoginDialog getInstance(Context context) {
		if (StringUtil.isEmpty(reLoginDialog)) {
			reLoginDialog = new ReLoginDialog();
		}
		mContext = context;
		return reLoginDialog;
	}

	public void showDialog(String message) {
		if (builder == null) {
			builder = new CustomDialog.Builder(mContext);
			builder.setTitle("提示");
			builder.setMessage(message);
			builder.setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					arg0.dismiss();
					// if
					// (!CommonUtils.getTopActivity(mContext).equals(LoginActivity.class.getName()))
					// {
					// ((Activity)mContext).finish();
					// }
				}
			});
			builder.setPositiveButton("验证登录", new OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					mContext.startActivity(new Intent(mContext,
							LoginActivity.class));
					arg0.dismiss();
//					dialog.cancel();
					// if
					// (!CommonUtils.getTopActivity(mContext).equals(LoginActivity.class.getName()))
					// {
					// ((Activity)mContext).finish();
					// }
				}
			});
		}
		dialog = builder.create();
		dialog.show();

	}

	// /**
	// * 判断activity是否是mainActivity不是则finish
	// */
	// private void activityIsFinish() {
	// if
	// (CommonUtils.getTopActivity(mContext).equals(MainActivity.class.getName()))
	// {
	// // conflictDialog.dismiss();
	// }else {
	// mContext.startActivity(new Intent((Activity)mContext,
	// MainNewActivity.class));
	// conflictDialog.dismiss();
	// }
	// }
}
