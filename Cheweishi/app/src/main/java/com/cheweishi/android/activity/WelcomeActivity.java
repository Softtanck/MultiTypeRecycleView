package com.cheweishi.android.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.WelcomeAdapter;
import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.tools.LoginMessageUtils;
import com.cheweishi.android.tools.SharePreferenceTools;
import com.cheweishi.android.utils.ButtonUtils;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.WelcomeGallery;

import cn.jpush.android.api.JPushInterface;

/**
 * 欢迎界面
 *
 * @author mingdasen
 */
public class WelcomeActivity extends BaseActivity implements OnClickListener {
    private RelativeLayout rl_wecome;
    private Timer timer = new Timer();
    private WelcomeGallery gallery = null;
    private ArrayList<Integer> imgList;
    private ArrayList<ImageView> portImg;
    private int preSelImgIndex = 0;
    private LinearLayout ll_focus_indicator_container = null;
    private ImageView immediateExperience;
    private LayoutInflater inflater;
    private TextView tv_welcome_skip; // 跳过
    //    private TextView tv_welcome_skip_second;// 倒计时
    private ImageView iv_home_adv; // 主页广告
    private boolean mHasAdd = false; // 是否有广告
    private int mCurrentTimes = 5;
    private boolean mHadNext = false;
    private String homeUrl;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!mHadNext && null != handler) {
                tv_welcome_skip.setText("跳过(" + mCurrentTimes + ")");
                mCurrentTimes--;
                handler.sendMessageDelayed(Message.obtain(), 1000);
            } else {
                handler = null;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        LogHelper.d("welcome start time:"+System.currentTimeMillis());
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        homeUrl = SharePreferenceTools.getPhoneUrl(baseContext);
        if (!StringUtil.isEmpty(homeUrl)) { // 表示有广告
            iv_home_adv = (ImageView) findViewById(R.id.iv_home_adv);
            mHasAdd = true;
        }

        tv_welcome_skip = (TextView) findViewById(R.id.tv_welcome_skip);
//        tv_welcome_skip_second = (TextView) findViewById(R.id.tv_welcome_skip_second);
        rl_wecome = (RelativeLayout) findViewById(R.id.rl_welcome);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.welcome4, null);
        immediateExperience = (ImageView) convertView
                .findViewById(R.id.immediateExperienceBtn);
        if (immediateExperience != null) {
            immediateExperience.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (isLogined()) {
                        Intent intent = new Intent(WelcomeActivity.this,
                                MainNewActivity.class);
                        intent.putExtra("className", "WelcomeActivity");
                        startActivity(intent);
                        WelcomeActivity.this.finish();
                    } else {
                        Intent intent = new Intent(WelcomeActivity.this,
                                LoginActivity.class);
                        LoginMessageUtils.showDialogFlag = true;
                        intent.putExtra("className", "WelcomeActivity");
                        startActivity(intent);
                        WelcomeActivity.this.finish();
                    }
                }

            });
        }
        SharedPreferences preferences = getSharedPreferences("gallery",
                MODE_PRIVATE);
        if (preferences.getBoolean("galleryFlag", false) == false) { // 是否首次启动
            Editor editor = preferences.edit();
            editor.putBoolean("galleryFlag", true);
            editor.commit();
            ll_focus_indicator_container = (LinearLayout) findViewById(R.id.ll_focus_indicator_container);
            ll_focus_indicator_container.setVisibility(View.VISIBLE);
            imgList = new ArrayList<Integer>();
            imgList.add(R.layout.welcome1);
            imgList.add(R.layout.welcome2);
            imgList.add(R.layout.welcome3);
            imgList.add(R.layout.welcome4);
            InitFocusIndicatorContainer();
            gallery = (WelcomeGallery) findViewById(R.id.welcomegallery);
            gallery.setAdapter(new WelcomeAdapter(WelcomeActivity.this, imgList));
            gallery.setFocusable(true);
            gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int selIndex, long arg3) {
                    portImg.get(preSelImgIndex).setImageResource(
                            R.drawable.huanyinye_dian);
                    portImg.get(selIndex).setImageResource(
                            R.drawable.huanyinye_click_dian);
                    preSelImgIndex = selIndex;
                    if (selIndex == 2) {
                        immediateExperience.setVisibility(View.VISIBLE);

                    } else {
                        immediateExperience.setVisibility(View.GONE);
                        gallery.setOnTouchListener(null);
                    }
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

        } else {
            int delay = 3000;
            if (mHasAdd) { // 有广告
                XUtilsImageLoader.getHomeAdvImg(this,
                        -1, iv_home_adv,
                        homeUrl);
                tv_welcome_skip.setVisibility(View.VISIBLE);
//                tv_welcome_skip_second.setVisibility(View.VISIBLE);
                tv_welcome_skip.setOnClickListener(this);
                handler.sendMessageDelayed(Message.obtain(), 1000); // 更新UI
                delay = 5000;
            } else { // 无广告
                delay = 3000;
            }

            Timer timer = new Timer();// timer中有一个线程,这个线程不断执行task
            timer.schedule(task, delay);// 设置这个task在延迟三秒之后自动执行
//            if (rl_wecome != null) {
//                rl_wecome.setOnClickListener(new OnClickListener() {
//
//                    @Override
//                    public void onClick(View arg0) {
//
//                    }
//                });
//            }
        }
//        LogHelper.d("welcome end time:"+System.currentTimeMillis());
    }

    private void InitFocusIndicatorContainer() {
        portImg = new ArrayList<ImageView>();
        for (int i = 0; i < imgList.size(); i++) {
            ImageView localImageView = new ImageView(WelcomeActivity.this);
            localImageView.setId(i);
            ImageView.ScaleType localScaleType = ImageView.ScaleType.FIT_XY;
            localImageView.setScaleType(localScaleType);
            LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(36, 36);
            localImageView.setLayoutParams(localLayoutParams);
            localImageView.setPadding(5, 5, 5, 5);
            localImageView.setImageResource(R.drawable.huanyinye_dian);
            portImg.add(localImageView);
            this.ll_focus_indicator_container.addView(localImageView);
        }
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (!mHadNext)
                nextStep();
        }
    };

    private void nextStep() {
        if (isLogined() && LoginMessageUtils.isLogined(baseContext)) {
            Intent intent = new Intent(WelcomeActivity.this,
                    MainNewActivity.class);
            intent.putExtra("className", "WelcomeActivity");
            startActivity(intent);
            WelcomeActivity.this.finish();
        } else {
            Intent intent = new Intent(WelcomeActivity.this,
                    LoginActivity.class);
            intent.putExtra("className", "WelcomeActivity");
            LoginMessageUtils.showDialogFlag = true;
            startActivity(intent);
            WelcomeActivity.this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler = null;
        inflater = null;
        timer.cancel();
        timer = null;
        task.cancel();
        task = null;
        System.gc();
        if (imgList != null) {
            imgList.clear();
        }
        if (portImg != null) {
            portImg.clear();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(baseContext);
    }


    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(baseContext);
    }

    @Override
    public void onClick(View v) {
        /**
         * 快速点击忽略处理
         */
        if (ButtonUtils.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_welcome_skip:
                mHadNext = true;
                task.cancel();
                timer.cancel();
                nextStep();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }
}
