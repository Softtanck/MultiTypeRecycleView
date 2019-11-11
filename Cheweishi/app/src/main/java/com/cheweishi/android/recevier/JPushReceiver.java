package com.cheweishi.android.recevier;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import cn.jpush.android.api.JPushInterface;

import com.cheweishi.android.activity.MainNewActivity;
import com.cheweishi.android.R;
import com.cheweishi.android.activity.LoginActivity;
import com.cheweishi.android.activity.MessageCenterDetailsActivity;
import com.cheweishi.android.activity.WebActivity;
import com.cheweishi.android.entity.PushMsgResponse;
import com.cheweishi.android.tools.DBTools;
import com.cheweishi.android.tools.LoginMessageUtils;
import com.cheweishi.android.utils.ActivityControl;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.CustomDialog;

/**
 * 极光推送自定义接收器 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 *
 * @author mingdasen
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "Tanck";
    private static CustomDialog.Builder builder;
    private static CustomDialog phoneDialog;
    private static int NOTIFICATION_ID;
    private static Context mContext;


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        mContext = context;
//				+ ", extras: " + printBundle(bundle));

//		processCustomMessage(context, printBundle(bundle));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle
                    .getString(JPushInterface.EXTRA_REGISTRATION_ID);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                .getAction())) {
            Log.d(TAG,
                    "[MyReceiver] 接收到推送下来的自定义消息: "
                            + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                .getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle
                    .getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId + "   通知内容:" + bundle.getString(JPushInterface.EXTRA_EXTRA));

            // TODO 更新消息数量
            PushMsgResponse response = (PushMsgResponse) GsonUtil.getInstance().convertJsonStringToObject(bundle.getString(JPushInterface.EXTRA_EXTRA), PushMsgResponse.class);
            if (null != response.getUnreadCount() && !"".equals(response.getUnreadCount())) {
                int number = 0;
                try {
                    number = Integer.valueOf(response.getUnreadCount());
                } catch (Exception e) {
                    number = Integer.valueOf(response.getUnreadCount().substring(0, 2));
                }
                try {
                    LogHelper.d("消息数量:" + number);
                    if (0 < number) {
                        MainNewActivity.tv_msg_center_num.setVisibility(View.VISIBLE);
                        if (99 >= number)
                            MainNewActivity.tv_msg_center_num.setText(response.getUnreadCount());
                        else
                            MainNewActivity.tv_msg_center_num.setText("99+");
                    } else {
                        MainNewActivity.tv_msg_center_num.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                }
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
                .getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知" + bundle.getString(JPushInterface.EXTRA_EXTRA));
            // 打开自定义的Activity
            PushMsgResponse response = (PushMsgResponse) GsonUtil.getInstance().convertJsonStringToObject(bundle.getString(JPushInterface.EXTRA_EXTRA), PushMsgResponse.class);
            if (null != response.getType() && "NEWSMSG".equals(response.getType())) { // 新闻消息
                Intent web = new Intent(context, WebActivity.class);
                web.putExtra("id", response.getNewsId());
                web.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(web);
            } else { // 常规消息
                Intent msgDetail = new Intent(context, MessageCenterDetailsActivity.class);
                msgDetail.putExtra("title", response.getTitle());
                msgDetail.putExtra("content", response.getContent());
                msgDetail.putExtra("time", response.getTime());
                msgDetail.putExtra("id", response.getMsgId());
                msgDetail.putExtra("number", response.getUnreadCount());
                msgDetail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(msgDetail);
            }


        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
                .getAction())) {
            Log.d(TAG,
                    "[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
                            + bundle.getString(JPushInterface.EXTRA_EXTRA));
            // 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
            // 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
                .getAction())) {
            boolean connected = intent.getBooleanExtra(
                    JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction()
                    + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }


//        printBundle(bundle);
    }

    // send msg to MainActivity
    private void processCustomMessage(Context context, String bundle) {

        // if (PushDialogActivity.isForeground) {
        // String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        // String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        // Constant.CURRENT_REFRESH = Constant.JPUSH_REFRESH;
        // Intent msgIntent = new Intent();
        // msgIntent.setAction(Constant.CURRENT_REFRESH);
        // msgIntent.putExtra("bundle", bundle);
        // context.sendBroadcast(msgIntent);
        // Toast.makeText(context, bundle, Toast.LENGTH_LONG).show();
        // if (!ExampleUtil.isEmpty(extras)) {
        // try {
        // JSONObject extraJson = new JSONObject(extras);
        // if (null != extraJson && extraJson.length() > 0) {
        // msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
        // }
        // } catch (JSONException e) {
        //
        // }
        // }

        // }
    }

    /**
     * 打印所有的 intent extra 数据
     *
     * @param bundle
     * @return
     */
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(
                            bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" + myKey + " - "
                                + json.optString(myKey) + "]");
                        if (StringUtil.isEquals(json.optString("type"), "10",
                                true)) {
//							 ReLoginDialog.getInstance(
//							 ActivityControl.getActivity(0)).showDialog(
//							 "您的手机在已另一台手机上登录");
                            LoginMessageUtils.setLogined(ActivityControl.getActivity(0), false);
                            showPhoneDialog(bundle);
                        } else {
//							MessagCenterInfo centerInfo;
                            if (NOTIFICATION_ID != bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                                NOTIFICATION_ID = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//                                getMessageData(json.optInt("id"));
//								centerInfo = new MessagCenterInfo();
//								centerInfo.setId(json.optInt("id"));
//								DBTools.getInstance(ActivityControl.getActivity(0)).save(centerInfo);
//								if (ActivityControl.getActivity(0).equals(MessagerCenterActivity.class)) {
//									MessagerCenterActivity.instance.initDate();
//								}
                            }
                        }
                    }
                } catch (JSONException e) {
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    private static void showPhoneDialog(final Bundle bundle) {
        if (!StringUtil.isEmpty(ActivityControl.getActivity(0))) {
            builder = new CustomDialog.Builder(ActivityControl.getActivity(0));
            builder.setMessage("您的手机在已另一台手机上登录");
            builder.setTitle(R.string.remind);
            builder.setPositiveButton("验证登录",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            JPushInterface.clearNotificationById(ActivityControl.getActivity(0), bundle
                                    .getInt(JPushInterface.EXTRA_NOTIFICATION_ID));
                            Intent intent = new Intent(ActivityControl.getActivity(0),
                                    LoginActivity.class);
                            ActivityControl.getActivity(0).startActivity(intent);
                        }
                    });

            builder.setNegativeButton(R.string.cancel,
                    new android.content.DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            JPushInterface.clearNotificationById(ActivityControl.getActivity(0), bundle
                                    .getInt(JPushInterface.EXTRA_NOTIFICATION_ID));
                            // setRadioButtonLight();
                        }
                    });
            if (StringUtil.isEmpty(phoneDialog)) {
                phoneDialog = builder.create();
                phoneDialog.show();
            } else if (!phoneDialog.isShowing()) {
//				phoneDialog.dismiss();

                phoneDialog.show();
            }
        }
    }


}
