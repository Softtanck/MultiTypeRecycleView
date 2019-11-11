//package com.cheweishi.android.service;
//
//import org.android.agoo.client.BaseConstants;
//import org.json.JSONObject;
//
//import android.annotation.SuppressLint;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.support.v4.app.NotificationCompat;
//
//import com.cheweishi.android.R;
//import com.cheweishi.android.activity.CarDynamicActivity;
//import com.cheweishi.android.activity.CarManagerActivity;
//import com.cheweishi.android.activity.MainActivity;
//import com.cheweishi.android.activity.PushDialogActivity;
//import com.cheweishi.android.entity.LoginMessage;
//import com.cheweishi.android.tools.APPTools;
//import com.cheweishi.android.tools.LoginMessageUtils;
//import com.cheweishi.android.tools.SharePreferenceTools;
//import com.cheweishi.android.utils.CommonUtils;
//import com.umeng.common.message.Log;
//import com.umeng.fb.push.FeedbackPush;
//import com.umeng.message.UTrack;
//import com.umeng.message.UmengBaseIntentService;
//import com.umeng.message.entity.UMessage;
//
///**
// * Umeng推送服务
// * 
// * @author lucas
// * 
// */
//public class MyPushIntentService extends UmengBaseIntentService {
//	private static final String TAG = MyPushIntentService.class.getName();
//	private int msgType;// 0 表示广播、1表示警告、2 表示设备解绑 、3 表示年检、保养
//	private String msgValue = "";// 车牌
//	private Context mContext;
//	private String msgText;
//	private int builder_id;
//	private String carId = "";
////	private String device = "";
////	private String cid = "";
//	private String msgTitle = "";
//	private LoginMessage loginMessage;
//
//	@Override
//	public void onCreate() {
//		super.onCreate();
//
//	}
//
//	@Override
//	protected void onMessage(Context context, Intent intent) {
//		super.onMessage(context, intent);
//		loginMessage = LoginMessageUtils
//				.getLoginMessage(getApplicationContext());
//		//判断是否是意见反馈消息推送，是则return，否则继续处理
//		if (FeedbackPush.getInstance(context).onFBMessage(intent)) {
//			return;
//		}
//		
//		mContext = context;
//		try {
//			String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
//			android.util.Log.i("result", "pushMessage=" + message);
//			UMessage msg = new UMessage(new JSONObject(message));
//			msgType = Integer.parseInt(msg.extra.get("msgType"));
//			SharePreferenceTools.setPushed(mContext);
//			if (msgType == 0) {
//			} else {
//				msgValue = msg.extra.get("msgValue");
//				if (msgType == 1) {
//					carId = msg.extra.get("carId");
//				}
//				if (msgType == 3) {
////					cid = msg.extra.get("cid");
////					device = msg.extra.get("device");
//				}
//			}
//			msgTitle = msg.title;
//			msgText = msg.text;
//			builder_id = msg.builder_id;
//			if (!APPTools.isApplicationBroughtToBackground(mContext)) {
//				setNotification(context, msg);
//			}
//			// code to handle message here
//			// ...
//			UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
//			UTrack.getInstance(getApplicationContext()).trackRegister();
//		} catch (Exception e) {
//			Log.e(TAG, e.getMessage());
//		}
//		MainActivity.instance.ininGetHttpData();
//		pushDataHandle(context, msgType, msgValue);
//	}
//	/**
//	 * 设置notification
//	 * @param context
//	 * @param msg
//	 */
//	@SuppressLint("InlinedApi")
//	private void setNotification(Context context, UMessage msg) {
//		NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//				context);
//		mBuilder.setContentTitle(msg.title)
//				// 设置通知栏标题
//				.setContentText(msg.text)
//				// <span style="font-family: Arial;">/设置通知栏显示内容</span>
//				.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) // 设置通知栏点击意图
//				// .setNumber(number) //设置通知集合的数量
//				.setTicker(msg.ticker) // 通知首次出现在通知栏，带上升动画效果的
//				.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
//				.setPriority(Notification.PRIORITY_DEFAULT) // 设置该通知优先级
//				.setAutoCancel(true)// 设置这个标志当用户单击面板就可以让通知将自动取消
//				.setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//				.setDefaults(Notification.DEFAULT_VIBRATE)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
//				// Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音
//				// // requires VIBRATE permission
//				.setSmallIcon(R.drawable.app_icon);// 设置通知小ICON
//		if (loginMessage != null && loginMessage.getUid() != null
//				&& !loginMessage.getUid().equals("")) {
//			mNotificationManager.notify(builder_id, mBuilder.build());
//		}
//	}
//
//	/**
//	 * 推送消息相关处理
//	 * 
//	 * @param msgType2
//	 * @param msgValue2
//	 */
//	private void pushDataHandle(Context context, int msgType, String msgValue) {
//		switch (msgType) {
//		case 0:// 广播
//			pushBroadcastHandle();
//			break;
//		case 1:// 警告
//			pushWarningHandle(context, msgValue);
//			break;
//		case 2:// 设备解绑
//			pushUnbundHandle(context, msgValue);
//			break;
//		case 3:// 年检、保养
//			pushDetectionHandle(context, msgValue);
//		default:
//			break;
//		}
//	}
//
//	/**
//	 * 车辆保养推送消息处理
//	 * 
//	 * @param context
//	 * @param msgValue2
//	 */
//	private void pushDetectionHandle(Context context, String msgValue2) {
//		if (APPTools.isApplicationBroughtToBackground(MyPushIntentService.this)) {
//			Intent intent = new Intent(getBaseContext(),
//					PushDialogActivity.class);
//			intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
//			intent.putExtra("msgType", msgType);
//			intent.putExtra("msgValue", msgValue);
//			intent.putExtra("builder_id", builder_id);
//			intent.putExtra("title", msgTitle);
//			intent.putExtra("msgContent", msgText);
////			if (CommonUtils.getTopActivity(getBaseContext()).equals(
////					MessagerCenterActivity.class.getName())) {
////				intent.putExtra("type", 1);
////			}else if(CommonUtils.getTopActivity(getBaseContext()).equals(
////					DetactionInfoActivity.class.getName())){
////				intent.putExtra("type", 3);
////			}else {
////				intent.putExtra("type", 2);
////			}
//			getBaseContext().startActivity(intent);
//		}
//	}
//
//	/**
//	 * 设备解绑消息处理
//	 * 
//	 * @param msgValue2
//	 */
//	private void pushUnbundHandle(Context context, String msgValue) {
//		if (APPTools.isApplicationBroughtToBackground(MyPushIntentService.this)) {
//			Intent intent = new Intent(getBaseContext(),
//					PushDialogActivity.class);
//			intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
//			intent.putExtra("msgType", msgType);
//			intent.putExtra("msgValue", msgValue);
//			intent.putExtra("builder_id", builder_id);
//			intent.putExtra("title", msgTitle);
//			intent.putExtra("msgContent", msgText);
//			if (CommonUtils.getTopActivity(getBaseContext()).equals(
//					CarManagerActivity.class.getName())) {
//				intent.putExtra("type", 1);
//			} else {
//				intent.putExtra("type", 2);
//			}
//			getBaseContext().startActivity(intent);
//		}
//	}
//
//	/**
//	 * 警告消息处理
//	 * 
//	 * @param msgValue2
//	 */
//	private void pushWarningHandle(Context context, String msgValue) {
//		if (APPTools.isApplicationBroughtToBackground(MyPushIntentService.this)) {
//			Intent intent = new Intent(getBaseContext(),
//					PushDialogActivity.class);
//			intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
//			intent.putExtra("msgType", msgType);
//			if (CommonUtils.getTopActivity(getBaseContext()).equals(
//					CarDynamicActivity.class.getName())) {
//				intent.putExtra("type", 1);
//			} else {
//				intent.putExtra("type", 2);
//			}
//			intent.putExtra("carId", carId);
//			intent.putExtra("msgValue", msgValue);
//			intent.putExtra("title", msgTitle);
//			intent.putExtra("msgContent", msgText);
//			intent.putExtra("builder_id", builder_id);
//			getBaseContext().startActivity(intent);
//		}
//	}
//
//	/**
//	 * 广播处理
//	 */
//	private void pushBroadcastHandle() {
//	}
//	
//	/**
//	 * 设置PandingIntent
//	 * @param flags
//	 * @return
//	 */
//	public PendingIntent getDefalutIntent(int flags) {
//		// 构建一个Intent
//		Intent dialogIntent = null;
//		dialogIntent = new Intent(getBaseContext(), MainActivity.class);
//		
////		if (msgType == 2) {
////			dialogIntent = new Intent(getBaseContext(),
////					CarManagerActivity.class);
////		} else if (msgType == 1) {
////			// if (CommonUtils.getTopActivity(getBaseContext()).equals(
////			// CarDynamicActivity.class.getName())) {
////			// dialogIntent = new Intent();
////			// } else {
////			dialogIntent = new Intent(getBaseContext(),
////					CarDynamicActivity.class);
////			dialogIntent.putExtra("carId", carId);
////			// }
////		} else if (!APPTools.isApplicationBroughtToBackground(MyPushIntentService.this)) {
////			dialogIntent = new Intent(Intent.ACTION_MAIN);
////			dialogIntent.addCategory(Intent.CATEGORY_LAUNCHER);
////			dialogIntent.setClass(this, WelcomeActivity.class);
////			dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
////					| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
////		} else if (msgType == 3) {
////			// if (CommonUtils.getTopActivity(getBaseContext()).equals(
////			// KeyDetectionActivity.class.getName())) {
////			// dialogIntent = new Intent();
////			//
////			// } else {
////			dialogIntent = new Intent(Intent.ACTION_MAIN);
////			dialogIntent.addCategory(Intent.CATEGORY_LAUNCHER);
////			dialogIntent.setClass(this, KeyDetectionActivity.class);
////			dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
////					| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
////			dialogIntent.putExtra("cid", cid);
////			dialogIntent.putExtra("device", device);
////			// }
////		}else if (msgType == 0) {
////			dialogIntent = new Intent();
////		}
//		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1,
//				dialogIntent, flags);
//		return pendingIntent;
//	}
//}
