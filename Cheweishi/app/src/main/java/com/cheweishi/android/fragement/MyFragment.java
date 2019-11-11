package com.cheweishi.android.fragement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.activity.BaseActivity;
import com.cheweishi.android.activity.CarManagerActivity;
import com.cheweishi.android.activity.IdeaReturnActivity;
import com.cheweishi.android.activity.MainNewActivity;
import com.cheweishi.android.activity.MyShopOrderActivity;
import com.cheweishi.android.activity.MyorderActivity;
import com.cheweishi.android.activity.PurseActivity;
import com.cheweishi.android.activity.PurseRedPacketsActivity;
import com.cheweishi.android.activity.ReturnActivity;
import com.cheweishi.android.activity.SetActivity;
import com.cheweishi.android.activity.UserInfoEditActivity;
import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.utils.ButtonUtils;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.CustomDialog;
import com.cheweishi.android.widget.PullScrollView;
import com.cheweishi.android.widget.XCRoundImageView;

/**
 * Created by tangce on 7/6/2016.
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {

    // 头像
    private XCRoundImageView iv_myAccountUserIcon;

    // 用户名
    private TextView tv_my_name;

    //电话号码
    private TextView tv_my_phone;

    // 设置
    private TextView tv_setting;

    //优惠券数量
    private TextView tv_coupon_number_my;

    //优惠券
    private LinearLayout ll_my_default_coupon;

    //车数量
    private TextView tv_my_user_car_number;

    //默认车牌号
    public static TextView tv_my_user_car_id;

    //车库
    private LinearLayout ll_my_default_car;

    //钱包
    private LinearLayout ll_my_money;

    //我的订单
    private LinearLayout ll_my_order;

    //商品订单
    private LinearLayout ll_my_shop_order;

    //扫一扫
    private LinearLayout ll_my_scan;

    //联系客户
    private LinearLayout ll_communicate;

    //意见反馈
    private LinearLayout ll_feed_back;

    //待付款
    private LinearLayout ll_my_shop_unpaid;

    //待发货
    private LinearLayout ll_my_shop_unsend;

    //待收货
    private LinearLayout ll_my_shop_unrec;

    //待评价
    private LinearLayout ll_my_shop_received;

    //退货/售后
    private LinearLayout ll_my_reback;

    //下拉
    private PullScrollView psl_my;

    //顶部
    private View iv_my_top;

    //广播
    private MyBroadcastReceiver broad;

    private final String DEFAULT_NUMBER = "0";

    //车数量
    private String carNumber;

    //优惠券数量
    private String couponNumber;

    private CustomDialog.Builder builder;
    private CustomDialog phoneDialog;

    private boolean isLoaded = false;

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        iv_myAccountUserIcon = (XCRoundImageView) view.findViewById(R.id.iv_myAccountUserIcon);
        tv_my_name = (TextView) view.findViewById(R.id.tv_my_name);
        tv_my_phone = (TextView) view.findViewById(R.id.tv_my_phone);
        tv_setting = (TextView) view.findViewById(R.id.tv_setting);
        tv_coupon_number_my = (TextView) view.findViewById(R.id.tv_coupon_number_my);
        tv_my_user_car_number = (TextView) view.findViewById(R.id.tv_my_user_car_number);
        tv_my_user_car_id = (TextView) view.findViewById(R.id.tv_my_user_car_id);
        ll_my_default_car = (LinearLayout) view.findViewById(R.id.ll_my_default_car);
        ll_my_money = (LinearLayout) view.findViewById(R.id.ll_my_money);
        ll_my_order = (LinearLayout) view.findViewById(R.id.ll_my_order);
        ll_my_shop_order = (LinearLayout) view.findViewById(R.id.ll_my_shop_order);
        ll_my_scan = (LinearLayout) view.findViewById(R.id.ll_my_scan);
        ll_communicate = (LinearLayout) view.findViewById(R.id.ll_communicate);
        ll_feed_back = (LinearLayout) view.findViewById(R.id.ll_feed_back);
        ll_my_default_coupon = (LinearLayout) view.findViewById(R.id.ll_my_default_coupon);
        psl_my = (PullScrollView) view.findViewById(R.id.psl_my);
        iv_my_top = view.findViewById(R.id.iv_my_top);
        ll_my_shop_unpaid = (LinearLayout) view.findViewById(R.id.ll_my_shop_unpaid);
        ll_my_shop_unsend = (LinearLayout) view.findViewById(R.id.ll_my_shop_unsend);
        ll_my_shop_unrec = (LinearLayout) view.findViewById(R.id.ll_my_shop_unrec);
        ll_my_shop_received = (LinearLayout) view.findViewById(R.id.ll_my_shop_received);
        ll_my_reback = (LinearLayout) view.findViewById(R.id.ll_my_reback);


        iv_myAccountUserIcon.setOnClickListener(this);
        tv_setting.setOnClickListener(this);
        ll_my_default_coupon.setOnClickListener(this);
        ll_my_default_car.setOnClickListener(this);
        ll_my_money.setOnClickListener(this);
        ll_my_order.setOnClickListener(this);
        ll_my_scan.setOnClickListener(this);
        ll_my_shop_order.setOnClickListener(this);
        ll_communicate.setOnClickListener(this);
        ll_feed_back.setOnClickListener(this);
        ll_my_shop_unpaid.setOnClickListener(this);
        ll_my_shop_unsend.setOnClickListener(this);
        ll_my_shop_unrec.setOnClickListener(this);
        ll_my_shop_received.setOnClickListener(this);
        ll_my_reback.setOnClickListener(this);

    }

    @Override
    public void onDataLoading(int what) {
        if (0x4 == what) {
            onLoad();
        }
    }


    private void onLoad() {
        isLoaded = true;
        psl_my.setHeader(iv_my_top);
        psl_my.setNeedScrollSync(true);
        XUtilsImageLoader.getxUtilsImageLoader(baseContext, R.drawable.info_touxiang_moren, iv_myAccountUserIcon, loginResponse.getMsg().getPhoto());
        carNumber = StringUtil.isEmpty(((MainNewActivity) getActivity()).getCarNumber()) ? DEFAULT_NUMBER : ((MainNewActivity) getActivity()).getCarNumber();
        couponNumber = StringUtil.isEmpty(((MainNewActivity) getActivity()).getCouponNumber()) ? DEFAULT_NUMBER : ((MainNewActivity) getActivity()).getCouponNumber();
        tv_coupon_number_my.setText("优惠券(" + couponNumber + ")");
        tv_my_user_car_number.setText("我的车库(" + carNumber + ")");
        String plate = loginResponse.getMsg().getDefaultVehiclePlate();
        if (!StringUtil.isEmpty(plate)) {
            tv_my_user_car_id.setVisibility(View.VISIBLE);
            tv_my_user_car_id.setText(plate);
        }

        tv_my_name.setText(StringUtil.isEmpty(loginResponse.getMsg().getNickName()) ? getResources().getString(R.string.app_name) : loginResponse.getMsg().getNickName());
        tv_my_phone.setText(loginResponse.getMsg().getUserName());

    }


    @Override
    public void onClick(View arg0) {
        if (ButtonUtils.isFastClick()) {
            return;
        }
        switch (arg0.getId()) {
            case R.id.iv_myAccountUserIcon:// 用户信息
                turnToUserDetail();
                break;
            case R.id.ll_my_default_car:// 车辆管理
                turnToCarManager();
                break;
            case R.id.ll_communicate:// 联系客服
                showPhoneDialog();
                break;
            case R.id.ll_feed_back:// 用户反馈
                turnToFeed_back();
                break;
            case R.id.ll_my_money:// 钱包
                turnToMyMoney();
                break;
            case R.id.tv_setting:// 设置
                turnToSetting();
                break;
            case R.id.ll_my_order:// 订单
                turnToMyOrder();
                break;
            case R.id.ll_my_scan: // 扫一扫
                ((BaseActivity) (baseContext)).OpenCamera(false);
                break;
            case R.id.ll_my_default_coupon: // 优惠券
                Intent intent = new Intent(baseContext,
                        PurseRedPacketsActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_my_shop_order:
                Intent shopOrderActivity = new Intent(baseContext, MyShopOrderActivity.class);
                startActivity(shopOrderActivity);
                break;
            case R.id.ll_my_shop_unpaid://待付款
                goShopOrder(1);
                break;
            case R.id.ll_my_shop_unsend://待发货
                goShopOrder(2);
                break;
            case R.id.ll_my_shop_unrec://待收货
                goShopOrder(3);
                break;
            case R.id.ll_my_shop_received://待评价
                goShopOrder(4);
                break;
            case R.id.ll_my_reback://退货/售后
                Intent returnGoods = new Intent(baseContext, ReturnActivity.class);
                startActivity(returnGoods);
                break;
        }
    }

    private void goShopOrder(int item) {
        Intent shopOrder = new Intent(baseContext, MyShopOrderActivity.class);
        shopOrder.putExtra("page", item);
        startActivity(shopOrder);
    }

    /**
     * 用户详细信息跳转
     */
    private void turnToUserDetail() {
        Intent intent = new Intent(baseContext,
                UserInfoEditActivity.class);
        startActivity(intent);
    }

    /**
     * 我的设置
     */
    private void turnToSetting() {
        Intent intent = new Intent(baseContext, SetActivity.class);
        startActivity(intent);
    }

    /**
     * 我的订单跳转
     */
    private void turnToMyOrder() {
        Intent intent = new Intent(baseContext,
                MyorderActivity.class);
        startActivity(intent);

    }

    /**
     * 车辆管理跳转
     */
    private void turnToCarManager() {
        Intent intent = new Intent(baseContext,
                CarManagerActivity.class);
        startActivity(intent);
    }


    /**
     * 钱包跳转
     */
    private void turnToMyMoney() {
        Intent intent = new Intent(baseContext,
                PurseActivity.class);
        startActivity(intent);
    }

    /**
     * 用户反馈跳转
     */
    private void turnToFeed_back() {
        Intent intent = new Intent(baseContext,
                IdeaReturnActivity.class);
        startActivity(intent);
    }

    /**
     * 联系客服对话框
     */
    private void showPhoneDialog() {
        builder = new CustomDialog.Builder(baseContext);
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
//                        if (ActivityCompat.checkSelfPermission(baseContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//                            return;
//                        }
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && !isLoaded)
            loading.sendEmptyMessage(0x4);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (broad == null) {
            broad = new MyBroadcastReceiver();
        }
        IntentFilter intentFilter = new IntentFilter(Constant.REFRESH_FLAG);
        baseContext.registerReceiver(broad, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        baseContext.unregisterReceiver(broad);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            Constant.EDIT_FLAG = false;
            if (!StringUtil.isEquals(intent.getAction(), Constant.REFRESH_FLAG, true)) {
                return;
            }
            if (StringUtil.isEquals(Constant.CURRENT_REFRESH, Constant.USER_NICK_EDIT_REFRESH, true)) { // 修改名字
                tv_my_name.setText(StringUtil.isEmpty(loginResponse.getMsg().getNickName()) ? getResources().getString(R.string.app_name) : loginResponse.getMsg().getNickName());
            } else if (StringUtil.isEquals(Constant.CURRENT_REFRESH, Constant.USER_CENTER_REFRESH, true)) { // 修改头像
                XUtilsImageLoader.getxUtilsImageLoader(baseContext, R.drawable.info_touxiang_moren, iv_myAccountUserIcon, loginResponse.getMsg().getPhoto());
            } else if (StringUtil.isEquals(Constant.CURRENT_REFRESH, Constant.CAR_MANAGER_REFRESH, true)) { // 添加了车辆
                try {
                    int carNumber = Integer.valueOf(((MainNewActivity) getActivity()).getCarNumber());
                    ((MainNewActivity) getActivity()).setCarNumber(String.valueOf(carNumber + 1));
                } catch (Exception e) {
                }
                carNumber = StringUtil.isEmpty(((MainNewActivity) getActivity()).getCarNumber()) ? DEFAULT_NUMBER : ((MainNewActivity) getActivity()).getCarNumber();
                tv_my_user_car_number.setText("我的车库(" + carNumber + ")");
            } else if (StringUtil.isEquals(Constant.CURRENT_REFRESH, Constant.COUPON_REFRESH, true)) { // 领取优惠券
                try {
                    int couponNumber = Integer.valueOf(((MainNewActivity) getActivity()).getCouponNumber());
                    ((MainNewActivity) getActivity()).setCouponNumber(String.valueOf(couponNumber + 1));
                } catch (Exception e) {
                }
                couponNumber = StringUtil.isEmpty(((MainNewActivity) getActivity()).getCouponNumber()) ? DEFAULT_NUMBER : ((MainNewActivity) getActivity()).getCouponNumber();
                tv_coupon_number_my.setText("优惠券(" + couponNumber + ")");
            }
        }
    }
}
