package com.cheweishi.android.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.biz.HttpBiz;
import com.cheweishi.android.config.API;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.ChargePayResponse;
import com.cheweishi.android.entity.DevicePriceResponse;
import com.cheweishi.android.entity.PreparePayResponse;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.tools.LoginMessageUtils;
import com.cheweishi.android.tools.ReLoginDialog;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.PayUtils;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.utils.weixinpay.WeiXinPay;
import com.cheweishi.android.widget.CustomDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付界面
 *
 * @author mingdasen
 */
@ContentView(R.layout.activity_pay_choice)
public class PayActivty extends BaseActivity implements OnClickListener,
        OnCheckedChangeListener, PayUtils.OnPayListener {
    @ViewInject(R.id.left_action)
    private TextView left_action;

    @ViewInject(R.id.title)
    private TextView title;

    @ViewInject(R.id.btn_pay)
    private Button btn_pay;
    @ViewInject(R.id.img_weixin)
    private ImageView img_weixin;
    @ViewInject(R.id.img_alipay)
    private ImageView img_alipay;
    @ViewInject(R.id.img_upacp)
    private ImageView img_upacp;
    @ViewInject(R.id.pay_rg)
    private RadioGroup pay_rg;
    @ViewInject(R.id.rb_alipay)
    private RadioButton rb_alipay;
    @ViewInject(R.id.rb_weixin)
    private RadioButton rb_weixin;
    @ViewInject(R.id.money_rg)
    private RadioGroup money_rg;
    @ViewInject(R.id.ll_pay_choice_normal)
    private LinearLayout ll_pay_choice_normal;
    @ViewInject(R.id.ll_pay_choice_device)
    private LinearLayout ll_pay_choice_device;
    @ViewInject(R.id.tv_pay_choice_device_price)
    private TextView tv_pay_choice_device_price;
    @ViewInject(R.id.rl_pay_balance)
    private RelativeLayout rl_pay_balance; // 余额支付
    @ViewInject(R.id.cb_pay_balance)
    private CheckBox cb_pay_balance; // 余额选择
    @ViewInject(R.id.ll_alipay)
    private LinearLayout ll_alipay;
    @ViewInject(R.id.ll_weixin)
    private LinearLayout ll_weixin;

    private double moneyAccount = 600f;// 默认支付金额
    private float testMoney = 0.01f;
    private String payment_type = "zfb";// 默认支付方式
    private MyBroadcastReceiver broad;

    private boolean buy_type;// 购买类型, true: 购买设备

    /**
     * 银联支付渠道
     */
    private static final String CHANNEL_UPACP = "upacp";
    /**
     * 钱包
     */
    private static final String CHANNEL_WALLET = "WALLET";
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "WECHAT";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "ALIPAY";

    private static final int REQUEST_CODE_PAYMENT = 1;

    private String channel = CHANNEL_ALIPAY;

    private PayUtils payUtils;
    private String out_trade_no;// 记录号
    private static final int RELOGINType = 10007;

    private String deviceNo;
    private CustomDialog.Builder builder;
    private CustomDialog versionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        initView();
    }

    /**
     * 界面初始化
     */
    private void initView() {
        left_action.setText(R.string.back);
        title.setText(R.string.account_pay);

        payUtils = new PayUtils();
//		left_action.setOnClickListener(this);
//		btn_pay.setOnClickListener(this);
        money_rg.setOnCheckedChangeListener(this);
        buy_type = getIntent().getBooleanExtra("PAY_TYPE", false);


        if (buy_type) {
            btn_pay.setText("确认购买");
            title.setText(R.string.devices_pay);
            deviceNo = getIntent().getStringExtra("resultString");
            if (null == deviceNo || "".equals(deviceNo)) {
                showToast("当前设备号获取不正常");
                return;
            }
            ll_pay_choice_normal.setVisibility(View.GONE);
            rl_pay_balance.setVisibility(View.VISIBLE);
            ll_pay_choice_device.setVisibility(View.VISIBLE);
            cb_pay_balance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton arg0,
                                             boolean arg1) {

                    // TODO 余额支付

                    if (cb_pay_balance.isChecked()) {
                        img_alipay.setImageResource(R.drawable.dian12x);
                        img_weixin.setImageResource(R.drawable.dian12x);
                        img_upacp.setImageResource(R.drawable.dian12x);
                        channel = CHANNEL_WALLET;
                    } else {
                        img_alipay.setImageResource(R.drawable.dian22x);
                        img_weixin.setImageResource(R.drawable.dian12x);
                        img_upacp.setImageResource(R.drawable.dian12x);
                        channel = CHANNEL_ALIPAY;
                    }
                }
            });
            initDevicePrice();
        }

    }

    private void initDevicePrice() {
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_USER_BALANCE + NetInterface.GET_DEVICE_PRICE + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put(Constant.PARAMETER_TAG, NetInterface.GET_DEVICE_PRICE);
        netWorkHelper.PostJson(url, param, this);
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

    @OnClick({R.id.btn_pay, R.id.left_action, R.id.ll_alipay, R.id.ll_weixin,
            R.id.ll_upacp})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_action:
                finish();
                break;
            case R.id.btn_pay:
                if (buy_type) // 如果是购买设备的情况
                    if (tv_pay_choice_device_price.getText().toString().equals("暂时未获得价格")) {
                        showToast("当前设备价格未设置,无法购买");
                        return;
                    }

                btn_pay.setClickable(false);

                if (CHANNEL_WECHAT.equals(channel) && !WeiXinPay.getinstance(baseContext).isWXAppInstalledAndSupported()) {
                    btn_pay.setClickable(true);
                    return;
                }


                payMoney();
                break;
            case R.id.ll_alipay:
                // payment_type = "zfb";
                img_alipay.setImageResource(R.drawable.dian22x);
                img_weixin.setImageResource(R.drawable.dian12x);
                img_upacp.setImageResource(R.drawable.dian12x);
                rb_alipay.setChecked(true);
                rb_weixin.setChecked(false);
                cb_pay_balance.setChecked(false);
//                ((RadioButton) pay_rg.findViewById(R.id.rb_alipay))
//                        .setChecked(true);
//                ((RadioButton) pay_rg.findViewById(R.id.rb_weixin))
//                        .setChecked(false);
//                ((RadioButton) pay_rg.findViewById(R.id.rb_upacp))
//                        .setChecked(false);
                channel = CHANNEL_ALIPAY;
                break;
            case R.id.ll_weixin:
                img_weixin.setImageResource(R.drawable.dian22x);
                img_alipay.setImageResource(R.drawable.dian12x);
                img_upacp.setImageResource(R.drawable.dian12x);
                // payment_type = "";
                rb_alipay.setChecked(false);
                rb_weixin.setChecked(true);
                cb_pay_balance.setChecked(false);
//                ((RadioButton) pay_rg.findViewById(R.id.rb_alipay))
//                        .setChecked(false);
//                ((RadioButton) pay_rg.findViewById(R.id.rb_weixin))
//                        .setChecked(true);
//                ((RadioButton) pay_rg.findViewById(R.id.rb_upacp))
//                        .setChecked(false);
                channel = CHANNEL_WECHAT;
                break;

            case R.id.ll_upacp:
                img_weixin.setImageResource(R.drawable.dian12x);
                img_alipay.setImageResource(R.drawable.dian12x);
                img_upacp.setImageResource(R.drawable.dian22x);
                ((RadioButton) pay_rg.findViewById(R.id.rb_alipay))
                        .setChecked(false);
                ((RadioButton) pay_rg.findViewById(R.id.rb_weixin))
                        .setChecked(false);
                ((RadioButton) pay_rg.findViewById(R.id.rb_upacp)).setChecked(true);
                channel = CHANNEL_UPACP;
                break;
            default:
                break;
        }
    }

    private void payMoney() {
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_USER_BALANCE + NetInterface.CHARGE_PAY + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("paymentType", channel);
        if (buy_type) {
            param.put("chargeType", "PD"); //CI:普通充值, PD:购买设备
            param.put("deviceNo", deviceNo);
        } else {
            param.put("chargeType", "CI");
            param.put("amount", moneyAccount);
        }
        param.put(Constant.PARAMETER_TAG, NetInterface.CHARGE_PAY);
        netWorkHelper.PostJson(url, param, this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
//            case R.id.rb_alipay:// 支付宝支付
//                // payment_type = "zfb";
//                channel = CHANNEL_ALIPAY;
//                break;
//
//            case R.id.rb_weixin:// 微信支付
//                // payment_type = "";
//                channel = CHANNEL_WECHAT;
//                break;
            case R.id.rb_money_100:// 支付金额100
                moneyAccount = 200f;
                break;
            case R.id.rb_money_300:// 支付金额300
                moneyAccount = 400f;
                break;
            case R.id.rb_money_600:// 支付金额600
                moneyAccount = 600f;
                break;
            case R.id.rb_money_1000:// 支付金额1000
                moneyAccount = 1000f;
                break;

            default:
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            PayActivty.this.finish();
        }
    };

    private String hasCar;

    @Override
    public void receive(String TAG, String data) {
        ProgrosDialog.closeProgrosDialog();
//        btn_pay.setClickable(true);
        switch (TAG) {
            case NetInterface.CHARGE_PAY:
                PreparePayResponse response = (PreparePayResponse) GsonUtil.getInstance().convertJsonStringToObject(data, PreparePayResponse.class);
                if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    showToast(response.getDesc());
                    handler.sendMessageDelayed(Message.obtain(), 1500);
//                    if (response.getCode().equals(NetInterface.RESPONSE_ERROR))
//                        finish();
                    return;
                }

                hasCar = response.getDesc();
                out_trade_no = response.getMsg().getOut_trade_no();
                switch (channel) {
                    case CHANNEL_ALIPAY: //支付宝
                        payUtils = new PayUtils();
                        payUtils.setOutTradeNo(response.getMsg().getOut_trade_no());
                        payUtils.setPayListener(this);
                        if (buy_type) {//CI:普通充值, PD:购买设备
                            payUtils.pay(PayActivty.this, "车生活", "购买设备", moneyAccount);
                        } else {
                            payUtils.pay(PayActivty.this, "车生活", "钱包充值", moneyAccount);
                        }

                        break;
                    case CHANNEL_WECHAT: // 微信
                        String prepay_id = response.getMsg().getPrepay_id();
                        String nonce_str = response.getMsg().getNonce_str();
                        LogHelper.d(prepay_id + "----" + nonce_str);
                        WeiXinPay.getinstance(this).pay(prepay_id, nonce_str);
                        break;
                    case CHANNEL_WALLET: // 钱包
                        updatePacket();
                        break;
                }


                loginResponse.setToken(response.getToken());
                LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
                break;
//            case NetInterface.PAY_CALLBACK:
//                BaseResponse baseResponse = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
//                if (!baseResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
//                    showToast(baseResponse.getDesc());
//                    return;
//                }
//
//                loginResponse.setToken(baseResponse.getToken());
//                LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
//                finish();
//                break;

            case NetInterface.GET_DEVICE_PRICE: // 获取设备价格
                DevicePriceResponse priceResponse = (DevicePriceResponse) GsonUtil.getInstance().convertJsonStringToObject(data, DevicePriceResponse.class);
                if (!priceResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    showToast(priceResponse.getDesc());
                    return;
                }

                if (null != priceResponse.getMsg()) {
                    moneyAccount = priceResponse.getMsg().getDevicePrice();
                    tv_pay_choice_device_price.setText("￥" + priceResponse.getMsg().getDevicePrice() + "元");
                }

                loginResponse.setToken(priceResponse.getToken());
                LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
                break;
            case NetInterface.BUY_DEVICE: // 购买成功后的call back
                btn_pay.setClickable(true);
                BaseResponse buyResponse = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
                if (!buyResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    showToast(buyResponse.getDesc());
                    return;
                }
//                showToast("购买成功");
                loginResponse.setToken(buyResponse.getToken());
                LoginMessageUtils.saveloginmsg(baseContext, loginResponse);

                Constant.CURRENT_REFRESH = Constant.PAY_REFRESH;
                Intent mIntent = new Intent();
                mIntent.setAction(Constant.REFRESH_FLAG);
                sendBroadcast(mIntent);
                jmp();
                break;
        }
    }

    private void jmp() {
        if (null != hasCar && !"0".equals(hasCar)) { // 有车辆
            showCustomDialog("设备已购买,现在就绑定检查爱车状态", "前往绑定", 1, this);
        } else {
            showCustomDialog("还没有添加爱车信息", "前往添加", 0, this);
        }
    }


    @Override
    public void error(String errorMsg) {
        ProgrosDialog.closeProgrosDialog();
        btn_pay.setClickable(true);
        showToast(R.string.server_link_fault);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 支付页面返回处理
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /*
                 * 处理返回值 "success" - payment succeed "fail" - payment failed
				 * "cancel" - user canceld "invalid" - payment plugin not
				 * installed
				 */
                if (StringUtil.isEquals("success", result, true)) {
                    Constant.CURRENT_REFRESH = Constant.PAY_REFRESH;
                    Intent mIntent = new Intent();
                    mIntent.setAction(Constant.REFRESH_FLAG);
                    sendBroadcast(mIntent);
                    showMsg("充值成功", "", "");
                } else if (StringUtil.isEquals("fail", result, true)) {
                    showMsg("支付失败", "", "");
                } else if (StringUtil.isEquals("cancel", result, true)) {
                    showMsg("取消支付", "", "");
                } else if (StringUtil.isEquals("invalid", result, true)) {
                    showMsg("未安装付款插件", "", "");
                }
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
            }
        }
    }

    public void showMsg(String title, String msg1, String msg2) {
        String str = title;
        if (null != msg1 && msg1.length() != 0) {
            str += "\n" + msg1;
        }
        if (null != msg2 && msg2.length() != 0) {
            str += "\n" + msg2;
        }
        AlertDialog.Builder builder = new Builder(PayActivty.this);
        builder.setMessage(str);
        builder.setTitle("提示");
        builder.setPositiveButton("确定", null);
        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!StringUtil.isEmpty(broad)) {
            unregisterReceiver(broad);
        }
    }

    @Override
    public void onPaySuccess() {
        Constant.CURRENT_REFRESH = Constant.PAY_REFRESH;
        Intent mIntent = new Intent();
        mIntent.setAction(Constant.REFRESH_FLAG);
        sendBroadcast(mIntent);
        if (buy_type)
            jmp();
    }

    @Override
    public void onPayConfirm() {

    }

    @Override
    public void onPayFail() {
        btn_pay.setClickable(true);
    }


    /**
     * 更新充值
     */
    private void updatePacket() {
        btn_pay.setClickable(true);
        if (buy_type) {
            if (channel.equals(CHANNEL_WALLET)) { // 如果购买设备且是钱包余额购买的情况下
                if (null == out_trade_no || "".equals(out_trade_no)) {
                    showToast("没有获取到记录的编号,更新购买状态失败");
                    return;
                }
                ProgrosDialog.openDialog(baseContext);
                String url = NetInterface.BASE_URL + NetInterface.TEMP_USER_BALANCE + NetInterface.BUY_DEVICE + NetInterface.SUFFIX;
                Map<String, Object> param = new HashMap<>();
                param.put("userId", loginResponse.getDesc());
                param.put("token", loginResponse.getToken());
                param.put("paymentType", channel);
                param.put("deviceNo", deviceNo);
                param.put("recordNo", out_trade_no);
                param.put(Constant.PARAMETER_TAG, NetInterface.BUY_DEVICE);
                netWorkHelper.PostJson(url, param, this);
                return;
            }
        }
        finish();
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            if (!StringUtil.isEquals(intent.getAction(), Constant.REFRESH_FLAG,
                    true)) {
                return;
            }
            if (StringUtil.isEquals(Constant.CURRENT_REFRESH, Constant.WEIXIN_PAY_REFRESH, true)) {
                Constant.EDIT_FLAG = true;
                // setNow();
                onPaySuccess();
            } else if (StringUtil.isEquals(Constant.CURRENT_REFRESH, Constant.WEIXIN_PAY_FAIL_REFRESH, true)) {
                onPayFail();
            }
        }
    }

}
