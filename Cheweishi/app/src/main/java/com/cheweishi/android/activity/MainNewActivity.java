package com.cheweishi.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.lbsapi.auth.LBSAuthManagerListener;
import com.baidu.navisdk.BNaviEngineManager;
import com.baidu.navisdk.BaiduNaviManager;
import com.cheweishi.android.R;
import com.cheweishi.android.config.StatusBarCompat2;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.fragement.HomeFragment;
import com.cheweishi.android.fragement.MyFragment;
import com.cheweishi.android.fragement.NewsFragment;
import com.cheweishi.android.fragement.StoreFragment;
import com.cheweishi.android.utils.ActivityControl;
import com.cheweishi.android.utils.ButtonUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

/**
 * 商城版首页
 *
 * @author mingdasen
 */
@ContentView(R.layout.activity_main2)
public class MainNewActivity extends BaseActivity {

    @ViewInject(R.id.tv_home_title)
    public static TextView tv_home_title;

    public static String bindTitle;

    @ViewInject(R.id.ibtn_user)
    // title左边按钮
    public static ImageView ibtn_user;

    @ViewInject(R.id.tv_msg_center_num)
    public static TextView tv_msg_center_num;//消息数量

    @ViewInject(R.id.ll_home_header)
    private LinearLayout ll_home_header;//actionbar

    @ViewInject(R.id.ll_right_msg)
    private LinearLayout ll_right_msg; // 右边消息

    //顶部
    @ViewInject(R.id.ll_home_bottom)
    private LinearLayout ll_home_bottom;

    //首页
    @ViewInject(R.id.ll_home_bottom_index)
    private LinearLayout ll_home_bottom_index;

    //门店
    @ViewInject(R.id.ll_home_bottom_store)
    private LinearLayout ll_home_bottom_store;

    //蓝调
    @ViewInject(R.id.ll_home_bottom_news)
    private LinearLayout ll_home_bottom_news;

    //我的
    @ViewInject(R.id.ll_home_bottom_my)
    private LinearLayout ll_home_bottom_my;

    //底部字段
    @ViewInject(R.id.tv_home_bottom_index)
    private TextView tv_home_bottom_index;

    @ViewInject(R.id.tv_home_bottom_store)
    private TextView tv_home_bottom_store;

    @ViewInject(R.id.tv_home_bottom_news)
    private TextView tv_home_bottom_news;

    @ViewInject(R.id.tv_home_bottom_my)
    private TextView tv_home_bottom_my;


//    public static MainNewActivity instance;


    private HomeFragment home;
    private StoreFragment store;
    private NewsFragment news;
    private MyFragment my;

    private String carNumber;

    private String couponNumber;

    public String getCouponNumber() {
        return couponNumber;
    }

    public void setCouponNumber(String couponNumber) {
        this.couponNumber = couponNumber;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        StatusBarCompat2.setStatusBarColor(this);
//        instance = this;
//        initLocation();

        initContent();

        bindTitle = getResources().getString(R.string.app_name);

        tv_home_bottom_index.setTextColor(getResources().getColor(R.color.orange));
        iniBaiduNavi();
    }

    private void initContent() {
        home = new HomeFragment();
        store = new StoreFragment();
        news = new NewsFragment();
        my = new MyFragment();
        InitHomeFragment(R.id.fl_home_content, home, store, news, my);
//        store.setBottomHeight(ll_home_bottom.getMeasuredHeight());
    }

    /**
     * 初始化百度导航
     */
    private void iniBaiduNavi() {
        // 初始化导航引擎
        BaiduNaviManager.getInstance().initEngine(this,

                getSdcardDir(), mNaviEngineInitListener, new LBSAuthManagerListener() {
                    @Override
                    public void onAuthResult(int status, String msg) {
                    }
                });
    }


    private BNaviEngineManager.NaviEngineInitListener mNaviEngineInitListener = new BNaviEngineManager.NaviEngineInitListener() {
        public void engineInitSuccess() {
        }

        public void engineInitStart() {
        }

        public void engineInitFail() {
        }
    };
    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public void error(String errorMsg) {
        ProgrosDialog.closeProgrosDialog();
        showToast(R.string.FAIL);
    }


    private Intent intent;

    @OnClick({R.id.ibtn_user, R.id.ll_right_msg,
            R.id.ll_home_bottom_index, R.id.ll_home_bottom_store,
            R.id.ll_home_bottom_news, R.id.ll_home_bottom_my})
    public void onClick(View v) {
        /**
         * 快速点击忽略处理
         */
        if (ButtonUtils.isFastClick()) {
            return;
        }
        intent = new Intent();
        switch (v.getId()) {
            case R.id.ibtn_user:// 个人中心
                isLogin(MyAccountActivity.class);
                break;
            case R.id.ll_right_msg:// 消息中心
                isLogin(MessagerCenterActivity.class);
                break;
            case R.id.ll_home_bottom_index: //首页
                home();
                break;
            case R.id.ll_home_bottom_store: // 门店
                store();
                break;
            case R.id.ll_home_bottom_news: // 新闻
                news();
                break;
            case R.id.ll_home_bottom_my: // 我的
                my();
                break;
//            case R.id.btn_scanning:// 扫一扫
//                OpenCamera(false);
//                break;
//            case R.id.btn_my_wallet:// 我的钱包
//                isLogin(PurseActivity.class);// PurseActivity
//                break;
//            case R.id.btn_my_order:// 我的订单
//                isLogin(MyorderActivity.class);
//                break;
//            case R.id.rl_activity_area:// 活动专区
////                 intent.setClass(MainNewActivity.this,
////                 InformationSecondListActivity.class);
////                 startActivity(intent);
//                iv_home_hascoupon.setVisibility(View.GONE);
//                intent.setClass(MainNewActivity.this, CouponActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.rl_integral_mall:// 积分商城
////                 intent.setClass(MainNewActivity.this, SCActivity.class);
////                 startActivity(intent);
//                getDuiBaUrl();
//                break;
            default:
                break;
        }
    }

    public void home() {
        ((ImageView) ll_home_bottom_index.getChildAt(0)).setImageResource(R.drawable.home_home_select);
        ((ImageView) ll_home_bottom_store.getChildAt(0)).setImageResource(R.drawable.home_store);
        ((ImageView) ll_home_bottom_news.getChildAt(0)).setImageResource(R.drawable.home_news);
        ((ImageView) ll_home_bottom_my.getChildAt(0)).setImageResource(R.drawable.home_my);
        tv_home_bottom_index.setTextColor(getResources().getColor(R.color.orange));
        tv_home_bottom_store.setTextColor(getResources().getColor(R.color.gray));
        tv_home_bottom_news.setTextColor(getResources().getColor(R.color.gray));
        tv_home_bottom_my.setTextColor(getResources().getColor(R.color.gray));
        ll_right_msg.setVisibility(View.VISIBLE);
        setTitle(bindTitle);
        ll_home_header.setVisibility(View.VISIBLE);
        ChangeFragment(0, home, store, news, my);
    }

    private void store() {
        ((ImageView) ll_home_bottom_index.getChildAt(0)).setImageResource(R.drawable.home_home);
        ((ImageView) ll_home_bottom_store.getChildAt(0)).setImageResource(R.drawable.home_store_select);
        ((ImageView) ll_home_bottom_news.getChildAt(0)).setImageResource(R.drawable.home_news);
        ((ImageView) ll_home_bottom_my.getChildAt(0)).setImageResource(R.drawable.home_my);
        tv_home_bottom_index.setTextColor(getResources().getColor(R.color.gray));
        tv_home_bottom_store.setTextColor(getResources().getColor(R.color.orange));
        tv_home_bottom_news.setTextColor(getResources().getColor(R.color.gray));
        tv_home_bottom_my.setTextColor(getResources().getColor(R.color.gray));
        ll_right_msg.setVisibility(View.INVISIBLE);
        setTitle(getResources().getString(R.string.store));
        ll_home_header.setVisibility(View.VISIBLE);
        ChangeFragment(1, home, store, news, my);
    }

    public void goToCarManager() {
        Intent intent = new Intent(baseContext, CarManagerActivity.class);
        startActivity(intent);
    }

    public void loadWashCarStore() {
        store();
        store.getDataForWashCar();
    }

    public void loadNews() {
        news();
//        news.getDataForNews(); //  TODO 暂时不需要Load数据
    }

    private void news() {
        ((ImageView) ll_home_bottom_index.getChildAt(0)).setImageResource(R.drawable.home_home);
        ((ImageView) ll_home_bottom_store.getChildAt(0)).setImageResource(R.drawable.home_store);
        ((ImageView) ll_home_bottom_news.getChildAt(0)).setImageResource(R.drawable.home_news_select);
        ((ImageView) ll_home_bottom_my.getChildAt(0)).setImageResource(R.drawable.home_my);
        tv_home_bottom_index.setTextColor(getResources().getColor(R.color.gray));
        tv_home_bottom_store.setTextColor(getResources().getColor(R.color.gray));
        tv_home_bottom_news.setTextColor(getResources().getColor(R.color.orange));
        tv_home_bottom_my.setTextColor(getResources().getColor(R.color.gray));
        ll_right_msg.setVisibility(View.INVISIBLE);
        setTitle(getResources().getString(R.string.news));
        ll_home_header.setVisibility(View.VISIBLE);
        ChangeFragment(2, home, store, news, my);
    }

    private void my() {
        ((ImageView) ll_home_bottom_index.getChildAt(0)).setImageResource(R.drawable.home_home);
        ((ImageView) ll_home_bottom_store.getChildAt(0)).setImageResource(R.drawable.home_store);
        ((ImageView) ll_home_bottom_news.getChildAt(0)).setImageResource(R.drawable.home_news);
        ((ImageView) ll_home_bottom_my.getChildAt(0)).setImageResource(R.drawable.home_my_select);
        tv_home_bottom_index.setTextColor(getResources().getColor(R.color.gray));
        tv_home_bottom_store.setTextColor(getResources().getColor(R.color.gray));
        tv_home_bottom_news.setTextColor(getResources().getColor(R.color.gray));
        tv_home_bottom_my.setTextColor(getResources().getColor(R.color.orange));
        ll_right_msg.setVisibility(View.INVISIBLE);
//                setTitle("我的"); // TODO 暂时没必要了.
        ll_home_header.setVisibility(View.GONE);
        ChangeFragment(3, home, store, news, my);
    }

    public void setTitle(String desc) {
        if (null != desc && !"".equals(desc)) {
            tv_home_title.setText(desc);
        }
    }

    /**
     * 登陆才能跳转
     *
     * @param cls
     */
    private void isLogin(Class<?> cls) {
        if (!isLogined()) {
            intent.setClass(baseContext, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.score_business_query_enter,
                    R.anim.score_business_query_exit);
        } else {
            intent.setClass(baseContext, cls);
            startActivity(intent);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        instance = null;
    }

    private long exitTime = 0;

    public void ExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            showToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            ActivityControl.GG(baseContext.getApplicationContext());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (0 != currentIndex) {
//            ((ImageView) ll_home_bottom_index.getChildAt(0)).setImageResource(R.drawable.home_home_select);
//            ((ImageView) ll_home_bottom_store.getChildAt(0)).setImageResource(R.drawable.home_store);
//            ((ImageView) ll_home_bottom_news.getChildAt(0)).setImageResource(R.drawable.home_news);
//            ((ImageView) ll_home_bottom_my.getChildAt(0)).setImageResource(R.drawable.home_my);
//            tv_home_bottom_index.setTextColor(getResources().getColor(R.color.orange));
//            tv_home_bottom_store.setTextColor(getResources().getColor(R.color.gray));
//            tv_home_bottom_news.setTextColor(getResources().getColor(R.color.gray));
//            tv_home_bottom_my.setTextColor(getResources().getColor(R.color.gray));
//            ll_right_msg.setVisibility(View.VISIBLE);
//            setTitle(bindTitle);
//            ll_home_header.setVisibility(View.VISIBLE);
//            ChangeFragment(0, home, store, news, my);
            home();
            return true;
        }

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            ExitApp();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
//        LogHelper.d("onNewIntent----------onNewIntent");
        if (intent.getBooleanExtra(getResources().getString(R.string.store_wash_the_car), false)) { // 洗车的情况
            loadWashCarStore();
        } else if (intent.getBooleanExtra(getResources().getString(R.string.newnotify), false)) { // 新闻资讯
            loadNews();
        } else if (null != home) {
            home();
            home.onDataLoading(0x1);
            my.setLoaded(false);
        }
    }

    public int getBottomHeight() {
        return ll_home_bottom.getHeight();
    }


}
