package com.cheweishi.android.tools;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.cheweishi.android.R;
import com.cheweishi.android.activity.LoginActivity;
import com.cheweishi.android.utils.ActivityControl;
import com.cheweishi.android.utils.CommonUtils;

public class DialogTool {
	private final String TAG = "DialogTool";
	private static Context applicationContext;
	private static DialogTool instance;

	public boolean isConflictDialogShow;
	public boolean isAccountRemovedDialogShow;
	private Builder conflictBuilder;
	public Dialog conflictDialog;
	// 环信账号在别处登录
	public boolean isConflict = false;
	// 环信账号被移除
	public boolean isCurrentAccountRemoved = false;

	/**
	 * 获取DialogTool类
	 * 
	 * @param context
	 * @return
	 */
	public static DialogTool getInstance(Context context) {
		applicationContext = context;
		if (instance == null) {
			instance = new DialogTool();
		}
		return instance;
	}

	/**
	 * 显示帐号在别处登录dialog
	 */
	@SuppressWarnings("static-access")
	private void newConflictDialog() {
		String st = applicationContext.getResources().getString(
				R.string.Logoff_notification);
			try {
				if (conflictBuilder == null)
					conflictBuilder = new android.app.AlertDialog.Builder(
							applicationContext);
				conflictBuilder.setTitle(st);
				conflictBuilder.setMessage(R.string.connect_conflict);
				conflictBuilder.setNegativeButton(R.string.relogin,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								conflictBuilder = null;
								LoginMessageUtils.showDialogFlag = true;
								isConflictDialogShow = false;
								isConflict = false;
								conflictBuilder = null;
								activityIsFinish();
								applicationContext
								.startActivity(new Intent(
										applicationContext,
										LoginActivity.class));
								ActivityControl.getActivity(ActivityControl.getCount() - 1)
										.overridePendingTransition(
												R.anim.score_business_query_enter,
												R.anim.score_business_query_exit);
//								if (!CommonUtils.getTopActivity(applicationContext).equals(MainActivity.class.getName())) {
//								((Activity)applicationContext).finish();
//								}
							}
						});
				conflictBuilder.setPositiveButton(R.string.already_know,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								isConflictDialogShow = false;
								isConflict = false;
								conflictBuilder = null;
								activityIsFinish();
//								if (!CommonUtils.getTopActivity(applicationContext).equals(MainActivity.class.getName())) {
//									((Activity)applicationContext).finish();
//									}
							}
						});
				conflictBuilder.setCancelable(false);
				SharedPreferences preferences = applicationContext
						.getSharedPreferences("user",
								applicationContext.MODE_PRIVATE);
				Editor editor = preferences.edit();
				editor.putString("pass", "");
				editor.commit();
				isConflictDialogShow = true;
				HXLogout();
//				LoginMessageUtils.deleteLoginMessage(applicationContext);
//				MainActivity.instance.conflictToClearLoginMessage();
				conflictDialog = conflictBuilder.create();
				isConflict = true;
				Log.i("result", "isConflict = true");
			} catch (Exception e) {
//				EMLog.e(TAG,
//						"---------color conflictBuilder error" + e.getMessage());
			}

		}
	/**
	 * 判断activity是否是mainActivity不是则finish
	 */
	private void activityIsFinish() {
//		if (CommonUtils.getTopActivity(applicationContext).equals(MainActivity.class.getName())) {
//			conflictDialog.dismiss();
//		}else {
//			applicationContext.startActivity(new Intent((Activity)applicationContext, MainActivity.class));
//			conflictDialog.dismiss();
//		}
	}
	/**
	 * 退出环信
	 */
	private void HXLogout(){
//		EMChatManager.getInstance().logout();
//		MainConstant.getInstance(applicationContext).setPassword(null);
//		MainConstant.getInstance(applicationContext).setUserName(null);
	}
	/**
	 * show下线通知提示
	 */
	public void showConflictDialog(){
		
		if (isConflictDialogShow == false) {
			newConflictDialog();
			if (conflictDialog != null && isConflictDialogShow == true && applicationContext != null && !((Activity)applicationContext).isFinishing()) {
				Log.i("result", "conflictDialog.show()");
			conflictDialog.show();
			}
		}
	}
	/**
	 * 关闭conflictDialog
	 */
	public void dismissConflictDialog(){
		if (conflictDialog != null && conflictDialog.isShowing()) {
			conflictDialog.dismiss();
		}
	}
	
	
}
