package com.cheweishi.android.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.tools.LoginMessageUtils;
import com.cheweishi.android.tools.SharePreferenceTools;
import com.cheweishi.android.utils.ButtonUtils;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.CustomDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * @author zhangQ
 * @version 1.0
 * @date 创建时间：2015-12-3 上午11:07:21
 * @Description:
 */
public class SetActivity extends BaseActivity implements OnClickListener,
        OnCheckedChangeListener {
    @ViewInject(R.id.cb_call_police)
    private CheckBox cb_call_police;// 报警
    // @ViewInject(R.id.cb_sound)
    // private CheckBox cb_sound;// 声音
    // @ViewInject(R.id.cb_shake)

    // private CheckBox cb_shake;// 震动
    @ViewInject(R.id.cb_push)
    private CheckBox cb_push;// 极光推送

    @ViewInject(R.id.ll_good_reputation)//五星好评
    private LinearLayout ll_good_reputation;
    @ViewInject(R.id.ll_about_my)
    private LinearLayout ll_about_my;//关于我们
    @ViewInject(R.id.ll_clear_cache)
    private LinearLayout ll_clear_cache;

    @ViewInject(R.id.tv_version)
    private TextView tv_version;
    @ViewInject(R.id.btn_setout)
    private Button btn_setout;
    @ViewInject(R.id.left_action)
    private Button left_action;

    @ViewInject(R.id.title)
    private TextView title;

    private Intent intent;
    private CustomDialog.Builder builder;
    private MyBroadcastReceiver broad;
    private String status = "";
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ViewUtils.inject(this);
        initViews();
    }


    @Override
    public void receive(String TAG, String data) {
        ProgrosDialog.closeProgrosDialog();
        switch (TAG) {
            case NetInterface.LOGOUT:
                clearData();

                break;
        }
    }

    @Override
    public void error(String errorMsg) {
        ProgrosDialog.closeProgrosDialog();
        clearData();
    }



    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (broad == null) {
            broad = new MyBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter(Constant.REFRESH_FLAG);
            registerReceiver(broad, intentFilter);
        }
    }

    /**
     * 初始化布局
     */

    private void initViews() {
        title.setText(R.string.set);
        left_action.setText(R.string.back);
        try {
            PackageInfo pi = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            // 当前版本
            tv_version.setText(pi.versionName);
        } catch (NameNotFoundException e) {

            e.printStackTrace();
        }
        if (isLogined()) {
            btn_setout.setText(R.string.logout);
        } else {
            btn_setout.setText(R.string.login);
        }

        cb_push.setOnCheckedChangeListener(this);
        if (LoginMessageUtils.getPush(baseContext)) {
            cb_push.setChecked(true);
        } else {
            cb_push.setChecked(false);
        }
    }


    // 通知后台更改推送状态
    private void messageConnectToServer(String type) {
        if (!isLogined()) {
            Intent intent = new Intent(SetActivity.this, LoginActivity.class);
            this.startActivity(intent);
            this.finish();
        } else {
            ProgrosDialog.openDialog(this);
            String url = NetInterface.HEADER_ALL + NetInterface.LOGOUT + NetInterface.SUFFIX;
            Map<String, Object> param = new HashMap<>();
            param.put("userId", loginResponse.getDesc());
            param.put("token", loginResponse.getToken());
            param.put(Constant.PARAMETER_TAG, NetInterface.LOGOUT);
            netWorkHelper.PostJson(url, param, this);
        }
    }


    private void setPushFail() {
        if (cb_push.isChecked()) {
            cb_push.setChecked(false);
        } else {
            cb_push.setChecked(true);
        }
    }

    @OnClick({R.id.ll_about_my, R.id.btn_setout, R.id.left_action,
            R.id.ll_clear_cache, R.id.ll_good_reputation})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_about_my:// 关于我们
                intent = new Intent(SetActivity.this, AboutUsNewActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_setout:// 退出登录
                logoutSet();
                break;
            case R.id.left_action:
                finish();
                break;
            case R.id.ll_clear_cache:// 清除缓存
                clearPhoneCache();
                break;
            case R.id.ll_good_reputation:// 五星好评
                // Intent startintent = new Intent("android.intent.action.MAIN");
                // startintent.addCategory("android.intent.category.APP_MARKET");
                // startintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // startActivity(startintent);
                try {
                    String str = "market://details?id=" + getPackageName();
                    Intent localIntent = new Intent("android.intent.action.VIEW");
                    localIntent.setData(Uri.parse(str));
                    startActivity(localIntent);
                } catch (Exception e) {
                    showToast("请安装浏览器");
                }

                break;

            default:
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_call_police:
                if (isChecked) {
                    // 报警设置

                } else {

                }
                break;
            // case R.id.cb_sound:
            // if (isChecked) {
            // // 声音提醒
            // } else {
            //
            // }
            //
            // break;
            // case R.id.cb_shake:
            // // 震动
            // if (isChecked) {
            // status = "1";
            // } else {
            // status = "0";
            // }
            // type = 0;
            // setReportStatus(type, status);
            // break;
            case R.id.cb_push:
                // 推送
                /**
                 * 快速点击忽略处理
                 */
                if (ButtonUtils.isFastClick()) {
                    return;
                }
                LoginMessageUtils.setPush(baseContext, isChecked);
                if (isChecked) {
                    JPushInterface.resumePush(baseContext);
                } else {
                    JPushInterface.stopPush(baseContext);
                }
                break;

            default:
                break;
        }

    }

    private void logoutSet() {
        if (!isLogined()) {
            intent = new Intent(SetActivity.this, LoginActivity.class);
            LoginMessageUtils.showDialogFlag = true;
            startActivity(intent);
            SetActivity.this.overridePendingTransition(
                    R.anim.score_business_query_enter,
                    R.anim.score_business_query_exit);
        } else {
            LoginMessageUtils.showDialogFlag = true;
            LogoutDialog();
        }
    }

    private void LogoutDialog() {
        builder = new CustomDialog.Builder(this);
        builder.setMessage(R.string.logout_dialog);
        builder.setTitle(R.string.remind);
        builder.setPositiveButton(R.string.sure,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // HXlogout();
                        // TODO 取消Jpush推送
                        JPushInterface.stopPush(baseContext);
                        logoutConnectToServer();
                    }
                });

        builder.setNegativeButton(R.string.cancel,
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();

    }


    private void logoutConnectToServer() {
        // 清楚登陆时保存的登陆密码
        SharePreferenceTools.clearPassFromUser(SetActivity.this);
        if (!isLogined()) {
            // 退出系统
            clearData();
        } else {
            // 请求退出接口
            ProgrosDialog.openDialog(this);
            String url = NetInterface.HEADER_ALL + NetInterface.LOGOUT + NetInterface.SUFFIX;
            Map<String, Object> param = new HashMap<>();
            param.put("userId", loginResponse.getDesc());
            param.put("token", loginResponse.getToken());
            param.put(Constant.PARAMETER_TAG, NetInterface.LOGOUT);
            netWorkHelper.PostJson(url, param, this);
        }
    }

    /**
     * 退出操作后清除数据
     */
    private void clearData() {
        LoginMessageUtils.setLogined(this, false);
//        btn_setout.setText(R.string.login);
        // LoginMessageUtils.deleteLoginMessage(SetActivity.this);
//        DBTools.getInstance(this).delete(LoginResponse.class);
        // +loginMessage.getMobile());
        Intent intent = new Intent(baseContext, LoginActivity.class);
        intent.putExtra("hasAccount", true);
        startActivity(intent);
//        ActivityControl.finishProgrom();
        this.finish();
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            if (!StringUtil.isEquals(intent.getAction(), Constant.REFRESH_FLAG,
                    true)) {
                return;
            }
            if (StringUtil.isEquals(Constant.CURRENT_REFRESH,
                    Constant.LOGIN_REFRESH, true)) {
                initViews();
            }
        }
    }



    /**
     * 清除缓存
     */
    private void clearPhoneCache() {

//        Double d = FileSizeUtils.getFileOrFilesSize(Constant.BASE_IMG_CATCH_DIR
//                + "cws", 3);
//
//        showToast("清除缓存" + d + "M");
//        XUtilsImageLoader.clearCache();
//        LoginMessageUtils.deleteLoginMessage(baseContext);
        showToast("清除缓存成功");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broad);
    }
}
