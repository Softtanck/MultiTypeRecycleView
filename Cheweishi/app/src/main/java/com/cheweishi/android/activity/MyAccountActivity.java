package com.cheweishi.android.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.config.API;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.utils.ButtonUtils;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.CustomDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 个人中心
 *
 * @author 大森
 */
public class MyAccountActivity extends BaseActivity implements OnClickListener {
    @ViewInject(R.id.ll_myAccount_user)
    private LinearLayout ll_myAccount_user;
    @ViewInject(R.id.ll_car_manager)
    private LinearLayout ll_car_manager;
    @ViewInject(R.id.ll_net_phone)
    private LinearLayout ll_net_phone;
//    @ViewInject(R.id.ll_msg_center)
//    private LinearLayout ll_msg_center;
    @ViewInject(R.id.ll_communicate)
    private LinearLayout ll_communicate;
    @ViewInject(R.id.ll_feed_back)
    private LinearLayout ll_feed_back;
    @ViewInject(R.id.ll_my_order)
    private LinearLayout ll_my_order;
    @ViewInject(R.id.ll_my_money)
    private LinearLayout ll_my_money;
    @ViewInject(R.id.ll_setting)
    private LinearLayout ll_setting;
    @ViewInject(R.id.iv_myAccountUserIcon)
    private  ImageView iv_myAccountUserIcon;
    @ViewInject(R.id.tv_nickName)
    private TextView tv_nickName;
    @ViewInject(R.id.tv_myDefaultCar)
    private TextView tv_myDefaultCar;
    @ViewInject(R.id.tv_special_sign)
    private TextView tv_special_sign;
    @ViewInject(R.id.left_action)
    private Button left_action;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.right_action)
    private TextView right_action;
    private CustomDialog.Builder builder;
    private CustomDialog phoneDialog;
    private MyBroadcastReceiver broad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        ViewUtils.inject(this);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (broad == null) {
            broad = new MyBroadcastReceiver();
        }
        IntentFilter intentFilter = new IntentFilter(Constant.REFRESH_FLAG);
        registerReceiver(broad, intentFilter);
    }

    private void initViews() {
        title.setText(R.string.title_activity_my_account);
        left_action.setText(R.string.back);
        if (isLogined()) {
            XUtilsImageLoader.getxUtilsImageLoader(this,
                    R.drawable.info_touxiang_moren, iv_myAccountUserIcon,
                    loginResponse.getMsg().getPhoto());
            tv_nickName.setText(loginResponse.getMsg().getNickName());
            if (hasCar()) {
                tv_myDefaultCar.setText("认证座驾:"
                        + loginResponse.getMsg().getDefaultVehicle());
            } else {
                tv_myDefaultCar.setText("未查询到认证座驾");
            }
            if (hasNote()) {
                tv_special_sign.setText(loginResponse.getMsg().getSignature());
            }
        } else {
            XUtilsImageLoader.getxUtilsImageLoader(this,
                    R.drawable.info_touxiang_moren, iv_myAccountUserIcon, "");
            tv_nickName.setText("未登录");
            tv_myDefaultCar.setText("未查询到认证座驾");
        }

    }

    @OnClick({R.id.ll_myAccount_user, R.id.ll_car_manager, R.id.ll_net_phone,
           R.id.ll_communicate, R.id.ll_feed_back,
            R.id.ll_my_money, R.id.tv_special_sign, R.id.ll_setting,
            R.id.left_action, R.id.ll_my_order})
    @Override
    public void onClick(View arg0) {
        if (ButtonUtils.isFastClick()) {
            return;
        }
        switch (arg0.getId()) {
            case R.id.ll_myAccount_user:// 用户信息
                turnToUserDetail();
                break;
            case R.id.ll_car_manager:// 车辆管理
                turnToCarManager();
                break;
            case R.id.ll_net_phone:// 网络电话
                turnToNetPhone();
                break;
//            case R.id.ll_msg_center:// 消息中心
//                turnToMssageCenter();
//                break;
            case R.id.ll_communicate:// 联系客服
                showPhoneDialog();
                break;
            case R.id.ll_feed_back:// 用户反馈
                turnToFeed_back();
                break;
            case R.id.ll_my_money:// 钱包
                turnToMyMoney();
                break;
            case R.id.ll_setting:// 设置
                turnToSetting();
                break;
            case R.id.ll_my_order:// 订单
                turnToMyOrder();
                break;
            case R.id.left_action:// 返回
                finish();
        }
    }

    /**
     * 我的订单跳转
     */
    private void turnToMyOrder() {
        if (goToLoginFirst()) {
            Intent intent = new Intent(MyAccountActivity.this,
                    MyorderActivity.class);
            startActivity(intent);
        }

    }

    /**
     * 用户详细信息跳转
     */
    private void turnToUserDetail() {
        if (goToLoginFirst()) {
            Intent intent = new Intent(MyAccountActivity.this,
                    UserInfoEditActivity.class);
            startActivity(intent);
        }

    }

    /**
     * 车辆管理跳转
     */
    private void turnToCarManager() {
        if (goToLoginFirst()) {
            Intent intent = new Intent(MyAccountActivity.this,
                    CarManagerActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 消息中心跳转
     */
    private void turnToMssageCenter() {
        if (goToLoginFirst()) {
            Intent intent = new Intent(MyAccountActivity.this,
                    MessagerCenterActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 用户详细信息跳转
     */
    private void turnToNetPhone() {
        showToast("敬请期待");
    }

    /**
     * 用户反馈跳转
     */
    private void turnToFeed_back() {
        if (goToLoginFirst()) {
            Intent intent = new Intent(MyAccountActivity.this,
                    IdeaReturnActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 钱包跳转
     */
    private void turnToMyMoney() {
        if (goToLoginFirst()) {
            Intent intent = new Intent(MyAccountActivity.this,
                    PurseActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 联系客服对话框
     */
    private void showPhoneDialog() {
        builder = new CustomDialog.Builder(this);
        builder.setMessage(R.string.phone_msg);
        builder.setTitle(R.string.contact_customer_service);
        builder.setPositiveButton(R.string.customerServiceCall,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri
                                .parse("tel:"
                                        + getResources().getString(
                                        R.string.customerServicePhone)));
                        if (ActivityCompat.checkSelfPermission(baseContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(intent);

                    }
                });

        builder.setNegativeButton(R.string.cancel,
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // setRadioButtonLight();
                    }
                });
        phoneDialog = builder.create();
        phoneDialog.show();
    }

    private void turnToSetting() {
        // ProgrosDialog.openDialog(MyAccountActivity.this);
        Intent intent = new Intent(MyAccountActivity.this, SetActivity.class);
        startActivity(intent);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            if (!StringUtil.isEquals(intent.getAction(), Constant.REFRESH_FLAG,
                    true)) {
                return;
            }
            if (StringUtil.isEquals(Constant.CURRENT_REFRESH,
                    Constant.CAR_MANAGER_REFRESH, true)) {
                Constant.EDIT_FLAG = true;
                // connectToServer();
            } else if (StringUtil.isEquals(Constant.CURRENT_REFRESH,
                    Constant.USER_NICK_EDIT_REFRESH, true)) {
                Constant.EDIT_FLAG = true;
                initViews();
            } else if (StringUtil.isEquals(Constant.CURRENT_REFRESH,
                    Constant.SPECIAL_SIGN_REFRESH, true)) {
                // connectToServer();
                initViews();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broad);
//        iv_myAccountUserIcon = null;
    }
}
