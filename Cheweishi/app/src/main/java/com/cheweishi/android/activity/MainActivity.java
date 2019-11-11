//package com.cheweishi.android.activity;
//
//import java.util.ArrayList;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.annotation.SuppressLint;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.content.DialogInterface.OnClickListener;
//import android.content.SharedPreferences.Editor;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.widget.SlidingPaneLayout;
//import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import cn.jpush.android.api.JPushInterface;
//
//import com.baidu.lbsapi.auth.LBSAuthManagerListener;
//import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;
//import com.baidu.navisdk.BaiduNaviManager;
//import com.cheweishi.android.R;
//import com.cheweishi.android.biz.HttpBiz;
//import com.cheweishi.android.biz.XUtilsImageLoader;
//import com.cheweishi.android.config.API;
//import com.cheweishi.android.config.Constant;
//import com.cheweishi.android.dialog.ProgrosDialog;
//import com.cheweishi.android.entity.CarManager;
//import com.cheweishi.android.entity.LoginMessage;
//import com.cheweishi.android.entity.MessagCenterInfo;
//import com.cheweishi.android.entity.Score;
//import com.cheweishi.android.fragement.MyCarFragment;
//import com.cheweishi.android.service.MyPushIntentService;
//import com.cheweishi.android.tools.DialogTool;
//import com.cheweishi.android.tools.LoginMessageUtils;
//import com.cheweishi.android.tools.ScreenTools;
//import com.cheweishi.android.tools.TextViewTools;
//import com.cheweishi.android.utils.ACache;
//import com.cheweishi.android.utils.ActivityControl;
//import com.cheweishi.android.utils.ButtonUtils;
//import com.cheweishi.android.utils.StringUtil;
//import com.cheweishi.android.widget.CustomDialog;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.lidroid.xutils.ViewUtils;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.view.annotation.ViewInject;
//import com.lidroid.xutils.view.annotation.event.OnClick;
//import com.umeng.update.UmengUpdateAgent;
//
//@SuppressLint("NewApi")
//public class MainActivity extends BaseActivity implements View.OnClickListener {
//	private MyCarFragment myCarFragment;// 我的车
//	@ViewInject(R.id.ll_userCenter)
//	private LinearLayout ll_userCenter;
//	@ViewInject(R.id.ll_sign)
//	private LinearLayout ll_sign;
//	@ViewInject(R.id.ll_car_manager)
//	private LinearLayout ll_car_manager;
//	@ViewInject(R.id.ll_net_phone)
//	private LinearLayout ll_net_phone;
//	@ViewInject(R.id.ll_msg_center)
//	private LinearLayout ll_msg_center;
//	@ViewInject(R.id.ll_communicate)
//	private LinearLayout ll_communicate;
//	@ViewInject(R.id.ll_feed_back)
//	private LinearLayout ll_feed_back;
//	@ViewInject(R.id.ll_my_money)
//	private LinearLayout ll_my_money;
//	@ViewInject(R.id.ll_menu)
//	private LinearLayout ll_menu;
//	@ViewInject(R.id.tv_myRanking)
//	private TextView tv_myRanking;
//	@ViewInject(R.id.car_img)
//	private ImageView car_img;
//	@ViewInject(R.id.tv_left_setting)
//	private TextView tv_left_setting;
//	@ViewInject(R.id.tv_signToday)
//	private TextView tv_signToday;
//	@ViewInject(R.id.img_user_icon)
//	private ImageView img_user_icon;
//	@ViewInject(R.id.tv_nickName)
//	private TextView tv_nickName;
//	@ViewInject(R.id.tv_score)
//	private TextView tv_score;
//	@ViewInject(R.id.tv_specialSign)
//	private TextView tv_specialSign;
//	@ViewInject(R.id.tv_messageCount)
//	private TextView tv_messageCount;
//	@ViewInject(R.id.main_fragment)
//	private LinearLayout main_fragment;
//	@ViewInject(R.id.tv_scoreBack)
//	private TextView tv_scoreBack;
//	@ViewInject(R.id.tv_returnMoneyBack)
//	private TextView tv_returnMoneyBack;
//	public static boolean isConflictDialogShow;
//	public static boolean isAccountRemovedDialogShow;
//	@ViewInject(R.id.ll_main_layout)
//	private LinearLayout ll_main_layout;
//	private CustomDialog.Builder builder;
//	private CustomDialog phoneDialog;
//	private String device_token = "";
////	/** 友盟推送 */
//	private PushAgent mPushAgent;
////	/** 友盟意见反馈 */
////	FeedbackAgent fb;
//	public static MainActivity instance;
//	private FragmentTransaction mFragmentTransaction;
//	private FragmentManager mFragmentManager;
//	CarManager carManagerItem = null;
//	private Drawable resideBgDrawable = null;
//	private static final int SignType = 10004;// 签到type
//	private static final int MsgType = 10005;
//	private static final int TokenType = 10006;// 保存device_token的type
//	private static final int RELOGINType = 10007;
//	// 总数量大小
//	private ArrayList<MessagCenterInfo> textlist = new ArrayList<MessagCenterInfo>();
//	private ACache mACache;
//	ArrayList<MessagCenterInfo> messageText = new ArrayList<MessagCenterInfo>();
//	private SlidingPaneLayout layout;
//	public static boolean msgRequest = false;
//	/**
//	 * 项目文件夹名称
//	 */
//	private String appFolderName = "cheweishi";
//
//	private MyBroadcastReceiver broad;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		setContentView(R.layout.container_frg);
//		ViewUtils.inject(this);
//		setLayout((SlidingPaneLayout) findViewById(R.id.sliding_pane_layout));
//		getLayout().setPanelSlideListener(panelSlideListener);
//		getLayout().setSliderFadeColor(Color.TRANSPARENT);
//		mACache = ACache.get(this);
//		instance = this;
//		httpBiz = new HttpBiz(this);
//		attachResideMenu();
//		setUpUmengFeedback();
//		// 初始化友盟推送
////		mPushAgent = PushAgent.getInstance(this);
////		// 启动推送
////		mPushAgent.onAppStart();
////		mPushAgent.enable(mRegisterCallback);
////		mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
//		// com.umeng.fb.util.Log.LOG = true;
//
//		// 初始化导航引擎
//		BaiduNaviManager.getInstance().initEngine(this, getSdcardDir(),
//				mNaviEngineInitListener, new LBSAuthManagerListener() {
//					@Override
//					public void onAuthResult(int status, String msg) {
//					}
//				});
//
//		// 友盟检查推送
//		checkUpdate();
//		HXRelevant();
//		
//		//获取极光推送
////		JPushInterface.get
////		String JPushId = JPushInterface.getRegistrationID(this);
////		Log.i("result", "=JPushRegistrationID==" + JPushId);
//
//	}
//
//	private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener() {
//		public void engineInitSuccess() {
//		}
//
//		public void engineInitStart() {
//		}
//
//		public void engineInitFail() {
//		}
//	};
//
//	private String getSdcardDir() {
//		if (Environment.getExternalStorageState().equalsIgnoreCase(
//				Environment.MEDIA_MOUNTED)) {
//			return Environment.getExternalStorageDirectory().toString();
//		}
//		return null;
//	}
//
//	private PanelSlideListener panelSlideListener = new PanelSlideListener() {
//
//		@Override
//		public void onPanelClosed(View arg0) {
//			// TODO Auto-generated method stub
//			myCarFragment.setHeadShow();
//		}
//
//		@Override
//		public void onPanelOpened(View arg0) {
//			// TODO Auto-generated method stub
//			myCarFragment.setHeadHide();
//		}
//
//		@Override
//		public void onPanelSlide(View arg0, float arg1) {
//			// TODO Auto-generated method stub
//
//		}
//
//	};
//
//	private void attachResideMenu() {
//		initViews();
//	}
//
//	public void closeLeftSide() {
//		ViewGroup.LayoutParams lp = ll_menu.getLayoutParams();
//		lp.width = (int) ((double) ScreenTools.getScreentWidth(this) * 0.0);
//		ll_menu.setLayoutParams(lp);
//		// resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
//	}
//
//	public void showLLeftSide() {
//		ViewGroup.LayoutParams lp = ll_menu.getLayoutParams();
//		lp.width = (int) ((double) ScreenTools.getScreentWidth(this) * 0.75);
//		ll_menu.setLayoutParams(lp);
//		// resideMenu.setRightSwipeDirectionDisable();
//	}
//
////	/**
////	 * 检查当前用户是否被删除
////	 */
////	public static boolean getCurrentAccountRemoved() {
////		return isCurrentAccountRemoved;
////	}
//
////	/**
////	 * 判断用户没有登录，去登录
////	 */
////	private boolean goToLoginFirst() {
////		if (StringUtil.isEmpty(loginMessage)
////				|| StringUtil.isEmpty(loginMessage.getUid())) {
////			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
////			MainActivity.this.startActivity(intent);
////			overridePendingTransition(R.anim.score_business_query_enter,
////					R.anim.score_business_query_exit);
////			return false;
////		} else {
////			return true;
////		}
////	}
//
//	/**
//	 * 联系客服对话框
//	 */
//	private void showPhoneDialog() {
//		builder = new CustomDialog.Builder(this);
//		builder.setMessage(R.string.phone_msg);
//		builder.setTitle(R.string.contact_customer_service);
//		builder.setPositiveButton(R.string.customerServiceCall,
//				new DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						Intent intent = new Intent(Intent.ACTION_CALL, Uri
//								.parse("tel:"
//										+ getResources().getString(
//												R.string.customerServicePhone)));
//						startActivity(intent);
//
//					}
//				});
//
//		builder.setNegativeButton(R.string.cancel,
//				new android.content.DialogInterface.OnClickListener() {
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						// setRadioButtonLight();
//					}
//				});
//		phoneDialog = builder.create();
//		phoneDialog.show();
//	}
//
//	public Handler mRegisterhandler = new Handler();
//	public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {
//
//		@Override
//		public void onRegistered(String arg0) {
//			mRegisterhandler.post(new Runnable() {
//
//				@Override
//				public void run() {
//					if (!StringUtil.isEmpty(mPushAgent.getRegistrationId())) {
//						device_token = mPushAgent.getRegistrationId();
//						Log.i("result", "=mRegisterCallback==device_token==="
//								+ device_token);
//						saveDeviceToken(device_token);
//					}
//				}
//			});
//		}
//	};
//
//	/**
//	 * 保存device_token
//	 */
//	private void saveDeviceToken(String device_token) {
//		SharedPreferences preferences = getSharedPreferences("device_token",
//				MODE_PRIVATE);
//		Log.i("result", "===LoginActivity.isDevice==" + LoginActivity.isDevice);
//		if (loginMessage != null && loginMessage.getUid() != null
//				&& LoginActivity.isDevice == false) {
//			RequestParams rp = new RequestParams();
//			rp.addBodyParameter("uid", loginMessage.getUid());
//			rp.addBodyParameter("key", loginMessage.getKey());
//			rp.addBodyParameter("imei", device_token);
//			HttpBiz httpBiz = new HttpBiz(MainActivity.this);
//			httpBiz.httPostData(TokenType, API.APP_UP_TOKEN_URL, rp, this);
//			Log.i("result", "=save=http=device_token==" + device_token);
//		}
//		Log.i("result",
//				"=preferences=1=device_token=="
//						+ preferences.getString("device_token", ""));
//
//		if (StringUtil.isEmpty(preferences.getString("device_token", ""))) {
//			Editor editor = preferences.edit();
//			editor.putString("device_token", device_token);
//			editor.commit();
//			Log.i("result",
//					"=preferences=2=device_token=="
//							+ preferences.getString("device_token", ""));
//
//		}
//		Log.i("result", "=save==device_token==" + device_token);
//	}
//
//	/**
//	 * 友盟自动更新
//	 */
//	private void checkUpdate() {
//		UmengUpdateAgent.setUpdateOnlyWifi(false);
//		UmengUpdateAgent.setUpdateAutoPopup(true);
//		UmengUpdateAgent.setDeltaUpdate(false);
//		UmengUpdateAgent.update(this);
//	}
//
//	/**
//	 * 设置友盟意见反馈
//	 */
////	private void setUpUmengFeedback() {
////		fb = new FeedbackAgent(this);
////		fb.sync();
////		fb.openAudioFeedback();
////		fb.openFeedbackPush();
////	}
//
//	/**
//	 * 环信账号别处登陆或被移除判断
//	 */
////	private void HXRelevant() {
////		// MyConnectionListener.getInstance(MainActivity.this).HXRelevant();
////		// 注册一个监听连接状态的listener
////		EMChatManager.getInstance().addConnectionListener(
////				new MyConnectionListener());
////		// 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
////		// SDK 会发送broadcast intent 通知UI，比如好友邀请。
////		EMChat.getInstance().setAppInited();
////	}
//
//	/**
//	 * 连接监听listener
//	 * 
//	 * 实现ConnectionListener接口
//	 */
////	public class MyConnectionListener implements EMConnectionListener {
////		@Override
////		public void onConnected() {
////			// 已连接到服务器
////		}
////
////		@Override
////		public void onDisconnected(final int error) {
////			runOnUiThread(new Runnable() {
////
////				@Override
////				public void run() {
////					if (error == EMError.USER_REMOVED) {
////						// 显示帐号已经被移除
////					} else if (error == EMError.CONNECTION_CONFLICT) {
////						// 显示帐号在其他设备登陆
////						Log.i("result", "====My=coufict===");
////						DialogTool.getInstance(instance).showConflictDialog();
////					} else {
////						if (NetUtils.hasNetwork(MainActivity.this)) {
////							// 连接不到聊天服务器
////						} else {
////							// 当前网络不可用，请检查网络设置
////						}
////					}
////				}
////			});
////		}
////	}
//
//	/**
//	 * 设置左侧滑头部
//	 */
//	private void setUpMenuTop() {
//
//		if (!isLogined()) {
//			XUtilsImageLoader.getxUtilsImageLoader(this,
//					R.drawable.info_touxiang_moren,img_user_icon, "");
//			tv_nickName.setText("未登录");
//			car_img.setVisibility(View.GONE);
//			tv_myRanking.setText("未查询到认证座驾");
//			tv_score.setText("0");
//		} else {
//			XUtilsImageLoader.getxUtilsImageLoader(this,
//					R.drawable.info_touxiang_moren,img_user_icon,
//					API.DOWN_IMAGE_URL + loginMessage.getPhoto());
//			tv_nickName.setText(loginMessage.getNick());
//			if (!hasBrandName()) {
//				car_img.setVisibility(View.GONE);
//				tv_myRanking.setText("未查询到认证座驾");
//			} else {
//				car_img.setVisibility(View.VISIBLE);
//				tv_myRanking.setText("认证座驾:"
//						+ loginMessage.getCar().getBrandName() + "-"
//						+ loginMessage.getCar().getSeriesName());
//			}
//			if (!hasScore()) {
//				tv_score.setText("0");
//			} else {
//				tv_score.setText(loginMessage.getScore().getNow() + "");
//			}
//		}
//	}
//
//	private void setUpMenuSign() {
//		if (hasNote()) {
//			tv_specialSign.setText(loginMessage.getNote());
//		} else {
//			tv_specialSign.setText(R.string.say_something);
//		}
//	}
//
//	private void setUpSetting() {
//	}
//
//	private void setUpMess(String endUrl) {
//		tv_messageCount.setText(endUrl.replaceFirst("_", ""));
//		if (!endUrl.replaceFirst("_", "").equals("")
//				|| endUrl.replaceFirst("_", "").equals("0")) {
//			tv_messageCount.setVisibility(View.VISIBLE);
//		} else {
//			tv_messageCount.setVisibility(View.GONE);
//		}
//		TextViewTools.getWidth(tv_messageCount);
//		if (myCarFragment != null) {
//			myCarFragment.setMessageNumber(endUrl.replaceFirst("_", ""));
//		}
//	}
//
//	public void setUpMenu() {
//		setUpMenuTop();
//		setUpMenuSign();
//		if (hasSign()) {
//			tv_signToday.setText("已签到");
//			tv_signToday.getBackground().setAlpha(150);
//			tv_signToday.setTextColor(0X50ffffff);
//		} else {
//			tv_signToday.setText("今日签到");
//		}
//		if (!hasAccount()) {
//			tv_scoreBack.setText(0 + "元");
//			tv_returnMoneyBack.setText(0 + "元");
//		} else {
//			tv_scoreBack.setText(loginMessage.getAccount().getCash() + "元");
//			tv_returnMoneyBack.setText(loginMessage.getAccount().getTotal()
//					+ "元");
//		}
//		setUpMess("");
//		setUpSetting();
//		if (isLogined()) {
//			ininGetHttpData();
//		}
//	}
//
//	@OnClick({ R.id.ll_main_layout, R.id.ll_my_money, R.id.tv_left_setting,
//			R.id.ll_sign, R.id.ll_userCenter, R.id.tv_signToday,
//			R.id.ll_car_manager, R.id.ll_net_phone, R.id.ll_communicate,
//			R.id.ll_feed_back, R.id.ll_msg_center })
//	@Override
//	public void onClick(View view) {
//		if (ButtonUtils.isFastClick()) {
//			return;
//		} else {
//			Intent intent;
//			if (view == ll_main_layout) {
//			} else if (view == ll_msg_center) {
//				startMessageCenter();
//			} else if (view == ll_my_money) {// 我的财富
//				if (goToLoginFirst()) {
//					intent = new Intent(MainActivity.this,
//							PurseActivity.class);//RechargeActivity
//					startActivity(intent);
//				}
//			} else if (view == tv_left_setting) {// 设置
//				intent = new Intent(MainActivity.this, SetActivity.class);
//				startActivity(intent);
//			} else if (view == ll_sign) {// 设置个性签名
//				if (goToLoginFirst()) {
//					intent = new Intent(MainActivity.this,
//							UserSignActivity.class);
//					intent.putExtra("sign", loginMessage.getNote());
//					startActivity(intent);
//				}
//			} else if (view == ll_userCenter) {// 个人中心
//				if (goToLoginFirst()) {
//					intent = new Intent(MainActivity.this,
//							MyAccountActivity.class);
//					startActivity(intent);
//				}
//			} else if (view == tv_signToday) {// 签到
//				if (goToLoginFirst()) {
//					connectToSignServer();
//				}
//			} else if (view == ll_car_manager) {// 车辆管理
//				if (goToLoginFirst()) {
//
//					intent = new Intent(MainActivity.this,
//							CarManagerActivity.class);
//					startActivity(intent);
//				}
//			} else if (view == ll_net_phone) {// 网络电话
//			} else if (view == ll_communicate) {// 联系客服
//				showPhoneDialog();
//			} else if (view == ll_feed_back) {// 意见反馈
//				intent = new Intent();
//				intent.setClass(MainActivity.this,
//						InsuranceActivity.class);
//				startActivity(intent);
//			}
//
//		}
//	}
//
//	/**
//	 * 请求签到接口
//	 */
//	private void connectToSignServer() {
//		RequestParams params = new RequestParams();
//		params.addBodyParameter("uid", loginMessage.getUid());
//		params.addBodyParameter("key", loginMessage.getKey());
//		httpBiz.httPostData(SignType, API.SIGN_URL, params, this);
//	}
//
//	/**
//	 * 签到解析
//	 */
//	private void parseSignJSON(String result) {
//		try {
//			JSONObject jo = new JSONObject(result);
//			loginMessage.setSign(1);
//			Score score = loginMessage.getScore();
//			score.setNow("" + jo.optInt("score"));
//			loginMessage.setScore(score);
//			LoginMessageUtils.saveProduct(loginMessage, MainActivity.this);
//			setUpMenu();
//			Toast.makeText(MainActivity.this, jo.optString("msg"),
//					Toast.LENGTH_LONG).show();
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 初始化视图
//	 */
//	private void initViews() {
//		// attachResideMenu();
//		ViewGroup.LayoutParams lp = ll_menu.getLayoutParams();
//		lp.width = (int) ((double) ScreenTools.getScreentWidth(this) * 0.75);
//		ll_menu.setLayoutParams(lp);
//		myCarFragment = new MyCarFragment();
//		mFragmentManager = getSupportFragmentManager();
//		mFragmentTransaction = mFragmentManager.beginTransaction();
//		if (!myCarFragment.isAdded()) {
//			mFragmentTransaction.add(R.id.main_fragment, myCarFragment);
//		}
//		Log.i("dahaha", "mFragmentTransaction:" + mFragmentTransaction);
//		mFragmentTransaction.show(myCarFragment);
//		mFragmentTransaction.commit();// 事务提交
//		submitLogin();
//	}
//
//	public class MyBroadcastReceiver extends BroadcastReceiver {
//
//		public void onReceive(Context context, Intent intent) {
//			Constant.EDIT_FLAG = false;
//			if (!StringUtil.isEquals(intent.getAction(), Constant.REFRESH_FLAG,
//					true)) {
//				return;
//			}
//			if (StringUtil.isEquals(Constant.CURRENT_REFRESH,
//					Constant.LOGIN_REFRESH, true)) {
//				refreshLogin();
//			} else if (StringUtil.isEquals(Constant.CURRENT_REFRESH,
//					Constant.CAR_MANAGER_REFRESH, true)) {
//				refreshNetPhone();
//			} else if (StringUtil.isEquals(Constant.CURRENT_REFRESH,
//					Constant.MESSAGE_CENTER_REFRESH, true)) {
//				// if (intent != null) {
//				// refreshMessageCenter(intent);
//				// }
//				refreshLogin();
//			} else if (StringUtil.isEquals(Constant.CURRENT_REFRESH,
//					Constant.SPECIAL_SIGN_REFRESH, true)) {
//				refreshSpecialSign(intent);
//			} else if (StringUtil.isEquals(Constant.CURRENT_REFRESH,
//					Constant.USER_CENTER_REFRESH, true)) {
//				refreshLogin();
//			} else if (StringUtil.isEquals(Constant.CURRENT_REFRESH,
//					Constant.NET_PHONE_REFRESH, true)) {
//
//				if (Constant.CALL_REQUEST) {
//					Constant.CALL_REQUEST = false;
//					refreshNetPhone();
//				}
//			} else if (StringUtil.isEquals(Constant.CURRENT_REFRESH,
//					Constant.USER_NICK_EDIT_REFRESH, true)) {
//
//				setUpMenuTop();
//			} else if (StringUtil.isEquals(Constant.CURRENT_REFRESH,
//					Constant.INSURANCE_REFRESH, true)) {
//				refreshLogin();
//			} else if (StringUtil.isEquals(Constant.CURRENT_REFRESH,
//					Constant.SIGN_REFRESH, true)) {
//				refreshLogin();
//			} 
//		}
//
//	}
//
//	public void startMessageCenter() {
//		if (goToLoginFirst()) {
//			Intent intent = new Intent(MainActivity.this,
//					MessagerCenterActivity.class);
//			startActivity(intent);
//		}
//	}
//
//	private void refreshLogin() {
//		ProgrosDialog.closeProgrosDialog();
//		myCarFragment.setUserImage();
//		setUpMenu();
//	}
//
//	private void refreshSpecialSign(Intent data) {
//		if (data != null && data.getStringExtra("flag").equals("true")) {
//			setUpMenuSign();
//		}
//	}
//
//	private void refreshNetPhone() {
//		submitLogin();
//	}
//
//	private void refreshMessageCenter(Intent data) {
//		Bundle extras = data.getExtras();
//		if (extras != null) {
//			int int1 = extras.getInt("position");
//			if (int1 == 0) {
//				tv_messageCount.setVisibility(View.GONE);
//				setUpMess("");
//			} else if (int1 > 0 && int1 < 99) {
//				setUpMess("_" + int1);
//			} else if (int1 >= 99) {
//				setUpMess("_" + "99+");
//			}
//		}
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//				+ Constant.CURRENT_REFRESH);
//		// showToast("haha");
//		// 注册刷新广播
//		if (broad == null) {
//			broad = new MyBroadcastReceiver();
//		}
//
//		IntentFilter intentFilter = new IntentFilter(Constant.REFRESH_FLAG);
//		registerReceiver(broad, intentFilter);
//
////		device_token = UmengRegistrar.getRegistrationId(MainActivity.this);
//		Log.i("result", "=onStart==device_token==" + device_token);
//		if (device_token != null && !device_token.equals("")) {
//			saveDeviceToken(device_token);
//		} else {
//			// 初始化友盟推送
////			mPushAgent = PushAgent.getInstance(this);
////			// 启动推送
////			mPushAgent.onAppStart();
////			mPushAgent.enable(mRegisterCallback);
////			device_token = UmengRegistrar.getRegistrationId(MainActivity.this);
//			if (device_token != null && !device_token.equals("")) {
//				saveDeviceToken(device_token);
//			}
//			Log.i("result", "=onResume==device_token==" + device_token);
//		}
//	}
//
//	public void onPause() {
//		super.onPause();
//	}
//
//	public void reconnect() {
//		setUpMenu();
//		myCarFragment.setUserImage();
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == -1) {
//			if (requestCode == -1) {
//				Intent intent = new Intent(MainActivity.this,
//						LoginActivity.class);
//				MainActivity.this.startActivity(intent);
//				overridePendingTransition(R.anim.score_business_query_enter,
//						R.anim.score_business_query_exit);
//			}
//		}
//	}
//
//	public void conflictToClearLoginMessage() {
//		loginMessage = LoginMessageUtils.getLoginMessage(this);
//		myCarFragment.setUserImage();
//		setUpMenu();
//	}
//
//	@Override
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//
//		if (getIntent().getBooleanExtra("conflict", false)
//				&& !isConflictDialogShow) {
//			Log.i("result", "====onNewIntent=coufict===");
//			DialogTool.getInstance(instance).showConflictDialog();
//		} else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false)) {
//		}
//	}
//
//	private long exitTime = 0;
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			ActivityControl.finishProgrom();
////			exit();
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//
//	public void exit() {
//		long secondTime = System.currentTimeMillis();
//		if ((secondTime - exitTime) > 2000) {
//			//if (ActivityControl.finishFlag == false) {
//				showToast(R.string.exit_system);
//				exitTime = secondTime;
//			//}
//		} else {
//			 finish();
////			ActivityControl.finishProgrom();
//			System.exit(0);
//		}
//	}
//
//	public void resetMsgCount() {
//		textlist.clear();
//		messageText.clear();
//		textlist = (ArrayList<MessagCenterInfo>) mACache
//				.getAsObject(loginMessage.getUid());
//		if (textlist == null) {
//			textlist = new ArrayList<MessagCenterInfo>();
//		}
//		int weiduSzie = getTextlistWeiduSize(textlist);
//		// weiduSzie++;
//		if (weiduSzie == 0) {
//			setUpMess("");
//		} else if (weiduSzie > 0 && weiduSzie < 99) {
//			setUpMess("_" + weiduSzie);
//		} else if (weiduSzie >= 99) {
//			setUpMess("_" + "99+");
//		}
//	}
//
//	public void ininGetHttpData() {
//		if (msgRequest == false) {
//			msgRequest = true;
//			RequestParams params = new RequestParams();
//			params.addBodyParameter("uid", loginMessage.getUid());
//			params.addBodyParameter("key", loginMessage.getKey());
//			httpBiz.httPostData(MsgType, API.MESSAGE_URL, params, this);
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	private void parseMsgJSON(String str) {
//		msgRequest = false;
//		if (loginMessage != null && loginMessage.getUid() != null) {
//			textlist.clear();
//			messageText.clear();
//			textlist = (ArrayList<MessagCenterInfo>) mACache
//					.getAsObject(loginMessage.getUid());
//			if (textlist == null) {
//				textlist = new ArrayList<MessagCenterInfo>();
//			}
//			int weiduSzie = getTextlistWeiduSize(textlist);
//			try {
//				if (!StringUtil.isEmpty(str)) {
//					JSONObject js;
//					try {
//						js = new JSONObject(str);
//						JSONArray array = js.getJSONArray("data");
//						if (array.length() > 0) {
//							for (int i = 0; i < array.length(); i++) {
//								JSONObject item = array.getJSONObject(i);
////								MessagCenterInfo centerInfo = new MessagCenterInfo(
////										item.getString("title"),
////										item.getString("time"),
////										item.getString("content"), 0,0);
////								messageText.add(centerInfo);
//							}
//						}
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//
//				Log.i("json_msg", messageText.size() + "_" + weiduSzie);
//				weiduSzie += messageText.size();
//				Log.i("json_msg", "=====weiduSzie+=========" + weiduSzie);
//				textlist.addAll(0, messageText);
//				mACache.put(loginMessage.getUid(), textlist,
//						360 * ACache.TIME_DAY);
//				if (loginMessage != null) {
//					if (weiduSzie == 0) {
//						setUpMess("");
//					} else if (weiduSzie > 0 && weiduSzie < 99) {
//						setUpMess("_" + weiduSzie);
//					} else if (weiduSzie >= 99) {
//						setUpMess("_" + "99+");
//					}
//				} else {
//					setUpMess("");
//				}
//
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * 所有未读数量
//	 */
//	public int getTextlistWeiduSize(ArrayList<MessagCenterInfo> saveList) {
//		int weidu = 0;
//		int size = saveList.size();
//		for (int i = 0; i < size; i++) {
//			MessagCenterInfo messagCenterInfo = saveList.get(i);
//			if (messagCenterInfo.getIsRead() == 0) {
//				weidu++;
//			}
//		}
//		Log.i("json_m", "===================" + weidu);
//		return weidu;
//	}
//
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		unregisterReceiver(broad);
//		setNull();
//		System.gc();
//	}
//
//	private void setNull() {
//		if (phoneDialog != null && phoneDialog.isShowing()) {
//			phoneDialog.dismiss();
//		}
//		if (resideBgDrawable != null) {
//			resideBgDrawable.setCallback(null);
//		}
//	}
//
//	/**
//	 * 返回的json数据
//	 */
//	@Override
//	public void receive(int type, String data) {
//		// TODO Auto-generated method stub
//		if (type == 400) {
//			if (msgRequest == true) {
//				msgRequest = false;
//			}
//			resetMsgCount();
//		} else {
//			if (StringUtil.isEmpty(data)) {
//				return;
//			}
//			try {
//				JSONObject jsonObject = new JSONObject(data);
//				String resultStr = jsonObject.optString("operationState");
//				String resultMsg = jsonObject.optJSONObject("data").optString(
//						"msg");
//				if (StringUtil.isEquals(resultStr, API.returnSuccess, true)) {
//					judgeJSONParse(type, jsonObject.optString("data"));
//				} else {
//					if (StringUtil.isEquals(resultStr, API.returnRelogin, true)) {
//						DialogTool.getInstance(this).showConflictDialog();
//					} else {
//						showToast(resultMsg);
//					}
//					if (type == TokenType) {
//						LoginActivity.isDevice = false;
//					}
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//
//	}
//
//	/**
//	 * 根据回调参数进行判断解析Json数据
//	 * 
//	 * @param type
//	 * @param jsonObject
//	 */
//	private void judgeJSONParse(int type, String jsonObject) {
//		switch (type) {
//		case 400:
//
//			break;
//		case SignType:
//			parseSignJSON(jsonObject);
//			break;
//		case MsgType:
//			parseMsgJSON(jsonObject);
//			break;
//		case TokenType:
//			LoginActivity.isDevice = true;
//			break;
//		case RELOGINType:
//			save(jsonObject);
//			break;
//
//		}
//	}
//
//	private void submitLogin() {
//		// TODO Auto-generated method stub
//
//		if (isLogined()) {
//			RequestParams rp = new RequestParams();
//			rp.addBodyParameter("uid", loginMessage.getUid());
//			rp.addBodyParameter("key", loginMessage.getKey());
//			httpBiz.httPostData(RELOGINType, API.LOGIN_MESSAGE_RELOGIN_URL, rp,
//					this);
//		} else {
//			// 不处理
//			setUpMenu();
//		}
//	}
//
//	static int feed;
//
//	protected void save(String jsonObject) {
//		Gson gson = new Gson();
//		java.lang.reflect.Type type = new TypeToken<LoginMessage>() {
//		}.getType();
//		loginMessage = gson.fromJson(jsonObject, type);
//		LoginMessageUtils.saveProduct(loginMessage, MainActivity.this);
//		myCarFragment.setUserImage();
//		setUpMenu();
//	}
//
//	public SlidingPaneLayout getLayout() {
//		return layout;
//	}
//
//	public void setLayout(SlidingPaneLayout layout) {
//		this.layout = layout;
//	}
//}