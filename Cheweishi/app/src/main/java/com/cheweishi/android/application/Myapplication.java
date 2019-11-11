package com.cheweishi.android.application;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.navisdk.ui.routeguide.subview.L;
import com.cheweishi.android.R;
import com.cheweishi.android.tools.APPTools;
import com.cheweishi.android.utils.ActivityControl;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.MyMapUtils;
import com.cheweishi.android.utils.ScreenUtils;
import com.cheweishi.android.utils.mapUtils.LocationUtil;
import com.cheweishi.android.widget.CustomDialog;
import com.umeng.analytics.MobclickAgent;

public class Myapplication extends Application implements
        UncaughtExceptionHandler {
//    public static Context applicationContext;
//    private static Myapplication instance;
    // login user name
    public final String PREF_USERNAME = "username";
    //	public static PushAgent mPushAgent;
    private CustomDialog.Builder builder;

    /**
     * 定位工具
     */
    private LocationUtil mLocationUtil;
    private SharedPreferences spLocation;
    private String historyCity;

    private String specialLcoationChongqing;
    private String specialLcoationBeijing;
    private String specialLcoationTianjin;
    private String specialLcoationShanghai;
    private String specialLcoationHongKong;
    private String specialLcoationAomen;


    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
//	public static String currentUserNick = "";
//	public static MyHXSDKHelper hxSDKHelper = new MyHXSDKHelper();
    @Override
    public void onCreate() { // TODO 百度sdk导致多次初始化
        super.onCreate();
//        LogHelper.d("app start time:" + System.currentTimeMillis());
        String processName = APPTools.getProcessName(this, android.os.Process.myPid());
        if (processName != null) {
            boolean defaultProcess = processName.equals(getPackageName());
            if (defaultProcess) {
                //必要的初始化资源操作
                MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

                // baidu初始化
                SDKInitializer.initialize(this);
//		umengInit();
//		HXinit();
                initLocation();
                mLocationUtil = new LocationUtil(this, LocationUtil.SCANSPAN_TYPE_LONG,
                        locationListener);
                mLocationUtil.onStart();


                JPushInit();//极光推送初始化
            }
        }


//		Thread.setDefaultUncaughtExceptionHandler(this);
//        Thread.setDefaultUncaughtExceptionHandler(this);
        //
//		 CrashHandler crashHandler=CrashHandler.getInstance();
//		 crashHandler.init(getApplicationContext());
//        LogHelper.d("app end time:" + System.currentTimeMillis() );
    }


    /**
     * 百度定位监听
     */
    BDLocationListener locationListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // 重庆市-渝中区
            historyCity = location.getProvince();
            boolean cityChangeFlag = false;
            if (historyCity != null
                    && (historyCity.contains(specialLcoationChongqing)
                    || historyCity.contains(specialLcoationBeijing)
                    || historyCity.contains(specialLcoationHongKong)
                    || historyCity.contains(specialLcoationTianjin)
                    || historyCity.contains(specialLcoationAomen) || historyCity
                    .contains(specialLcoationShanghai))) {
                historyCity = location.getCity() + "-" + location.getDistrict();
            } else if (historyCity != null) {
                historyCity = location.getProvince() + "-" + location.getCity();
            }
            String preHisCity = spLocation.getString("historyCity", null);
            if (preHisCity != null && preHisCity.equals(historyCity)) {
                cityChangeFlag = true;
            }

            spLocation.edit()
                    .putString("latitude", location.getLatitude() + "")
                    .putString("longitude", location.getLongitude() + "")
                    .putString("address", location.getAddrStr())
                    .putString("province", location.getProvince())
                    .putString("district", location.getDistrict())
                    .putString("historyCity", historyCity)
                    .putBoolean("cityChangeFlag", cityChangeFlag)
                    .putFloat("radius", location.getRadius())
                    .putString("city", location.getCity()).commit();

        }
    };

    /**
     * 定位相关初始化
     */
    private void initLocation() {
        spLocation = getSharedPreferences(MyMapUtils.LOCATION_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        specialLcoationChongqing = this
                .getString(R.string.special_location_chongqing);
        specialLcoationBeijing = this
                .getString(R.string.special_location_beijing);
        specialLcoationTianjin = this
                .getString(R.string.special_location_tianjin);
        specialLcoationShanghai = this
                .getString(R.string.special_location_shanghai);
        specialLcoationHongKong = this
                .getString(R.string.special_location_hongkong);
        specialLcoationAomen = this.getString(R.string.special_location_aomen);
    }

    /**
     * 极光推送
     */
    private void JPushInit() {
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
//		JPushInterface.resumePush(applicationContext);
//		JPushInterface.stopPush(applicationContext);
    }

//	private void umengInit() {
//		mPushAgent = PushAgent.getInstance(this);
//		mPushAgent.setDebugMode(false);
//		FeedbackPush.getInstance(this).init(UmengFbConversationActivity.class,
//				true);
    /**
     * 该Handler是在IntentService中被调用，故 1.
     * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK 2.
     * IntentService里的onHandleIntent方法是并不处于主线程中，因此，如果需调用到主线程，需如下所示;
     * 或者可以直接启动Service
     * */
//		UmengMessageHandler messageHandler = new UmengMessageHandler() {
//			@Override
//			public void dealWithCustomMessage(final Context context,
//					final UMessage msg) {
//				new Handler(getMainLooper()).post(new Runnable() {
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						UTrack.getInstance(getApplicationContext())
//								.trackMsgClick(msg);
//						UTrack.getInstance(getApplicationContext())
//								.trackRegister();
//						Toast.makeText(context, msg.custom, Toast.LENGTH_LONG)
//								.show();
//					}
//				});
//			}
//
//			@Override
//			public Notification getNotification(Context context, UMessage msg) {
//				switch (msg.builder_id) {
//				case 1:
//					NotificationCompat.Builder builder = new NotificationCompat.Builder(
//							context);
//					RemoteViews myNotificationView = new RemoteViews(
//							context.getPackageName(),
//							R.layout.notification_view);
//					myNotificationView.setTextViewText(R.id.notification_title,
//							msg.title);
//					myNotificationView.setTextViewText(R.id.notification_text,
//							msg.text);
//					myNotificationView.setImageViewBitmap(
//							R.id.notification_large_icon,
//							getLargeIcon(context, msg));
//					myNotificationView.setImageViewResource(
//							R.id.notification_small_icon,
//							getSmallIconId(context, msg));
//					builder.setContent(myNotificationView);
//					builder.setAutoCancel(true);
//					Notification mNotification = builder.build();
//					// 由于Android
//					// v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
//					mNotification.contentView = myNotificationView;
//					return mNotification;
//				default:
//					// 默认为0，若填写的builder_id并不存在，也使用默认。
//					return super.getNotification(context, msg);
//				}
//			}
//		};
//
//		mPushAgent.setMessageHandler(messageHandler);
//
//		/**
//		 * 该Handler是在BroadcastReceiver中被调用，故
//		 * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
//		 * */
//		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
//			@Override
//			public void dealWithCustomAction(Context context, UMessage msg) {
//				Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
//			}
//		};
//		mPushAgent.setNotificationClickHandler(notificationClickHandler);
//	}

    /**
     * 初始化环信
     */
//	private void HXinit() {
//		EMChat.getInstance().setAutoLogin(false);
//		hxSDKHelper.onInit(applicationContext);
//	}
//    public static Myapplication getInstance() {
//        return instance;
//    }

    /**
     * 退出登录,清空数据
     */
//	public void logout(final EMCallBack emCallBack) {
//		// 先调用sdk logout，在清理app中自己的数据
//		hxSDKHelper.logout(emCallBack);
//	}
//    public void showDialog(String text, String title) {
//        builder = new CustomDialog.Builder(Myapplication.instance);
//        builder.setMessage(text);
//        builder.setTitle(title);
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.create().show();
//
//    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // TODO Auto-generated method stub
        if (!handleException(e)) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e1) {
            }
            Intent intent = new Intent();
            intent.setClassName("com.cheweishi.android",// TODO com.cheweishi.android
                    "com.cheweishi.android.activity.WelcomeActivity");
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    getApplicationContext(), 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT); // TODO updte current
            Toast.makeText(getApplicationContext(), "抱歉，程序出现错误.即将退出重启",
                    Toast.LENGTH_LONG).show();
            AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            mAlarmManager.set(AlarmManager.RTC,
                    System.currentTimeMillis() + 1000, pendingIntent);
            ActivityControl.GG(getApplicationContext());

        }

    }

    private boolean handleException(Throwable e) {

        if (e == null) {
            return false;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "很抱歉,程序出现异常,即将重启.",
                        Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        return true;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mLocationUtil.onStop();
    }
}
