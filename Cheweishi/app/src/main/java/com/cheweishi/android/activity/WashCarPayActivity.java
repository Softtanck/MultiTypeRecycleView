package com.cheweishi.android.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.CouponAdapter;
import com.cheweishi.android.adapter.UseCouponAdapter;
import com.cheweishi.android.config.API;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.PayCouponListResponse;
import com.cheweishi.android.entity.PreparePayCouponResponse;
import com.cheweishi.android.entity.PreparePayResponse;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.tools.LoginMessageUtils;
import com.cheweishi.android.tools.ReLoginDialog;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.PayUtils;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.utils.weixinpay.WeiXinPay;
import com.cheweishi.android.widget.UnSlidingListView;
import com.cheweishi.android.wxapi.WXPayEntryActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预约支付
 *
 * @author mingdasen
 */
public class WashCarPayActivity extends BaseActivity implements PayUtils.OnPayListener, UseCouponAdapter.OnUserClickCouponListener {
    private static final String DEFAULT_AMOUNT = "0"; // 默认为0
    @ViewInject(R.id.tv_wash_pay_num)
    private TextView tv_wash_pay_num;// 价钱
    @ViewInject(R.id.left_action)
    private Button left_action;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.tv_wash_affirm)
    private TextView tv_wash_affirm;
    @ViewInject(R.id.pay_rg)
    private RadioGroup pay_rg;
    @ViewInject(R.id.rb_alipay)
    private RadioButton rb_alipay;
    @ViewInject(R.id.rb_weixin)
    private RadioButton rb_weixin;
    @ViewInject(R.id.img_weixin)
    private ImageView img_weixin;
    @ViewInject(R.id.img_alipay)
    private ImageView img_alipay;
    @ViewInject(R.id.tv_pay_name)
    // 商户名称
    private TextView tv_pay_name;
    @ViewInject(R.id.tv_pay_service_name)
    private TextView tv_pay_service_name;// 服务名称
    @ViewInject(R.id.tv_wash_money)
    private TextView tv_wash_money;
    @ViewInject(R.id.img_wallet_pay)
    private ImageView img_wallet_pay;
    @ViewInject(R.id.ll_washcar_pay)
    private LinearLayout ll_washcar_pay; // 洗车券
    @ViewInject(R.id.img_washcar_coupon)
    private ImageView img_washcar_coupon; // 洗车券CheckBox
    @ViewInject(R.id.cb_red)
    private CheckBox cb_red;
    @ViewInject(R.id.tv_red_hint)
    private TextView tv_red_hint;

    @ViewInject(R.id.rl_balance)
    private RelativeLayout rl_balance;
    @ViewInject(R.id.rl_red)
    private RelativeLayout rl_red;

    @ViewInject(R.id.cb_balance)
    private CheckBox cb_balance;
    @ViewInject(R.id.tv_balance_hint)
    private TextView tv_balance_hint;

    @ViewInject(R.id.ll_pay)
    private LinearLayout ll_pay;

    @ViewInject(R.id.unlist_washcar_pay)
    private UnSlidingListView unlist_washcar_pay; // 优惠券


    @ViewInject(R.id.rl_red_packet)
    public RelativeLayout rl_red_packet; // 红包

    @ViewInject(R.id.cb_red_packet)
    private CheckBox cb_red_packet; // 红包选择

    @ViewInject(R.id.tv_red_packet_hint)
    private TextView tv_red_packet_hint; // 红包描述

    // private Intent intent = new Intent();
    // private String num = "0.01";
    // private String out_trade_no;
    // private PayUtils payUtils;
    // private String payment_type = "zfb";// 默认支付方式
    private String red = DEFAULT_AMOUNT;// 使用的红包
    private double balance = 0.0;// 支付金额
    private double amount = 0.0;// 订单金额
    private double remainder = 0.0;// 使用的账户余额

    private String pay_type = "";
    private String order_sn = "";
    private int mScore = 0; // 我的积分
    private double mMoney = 0.0; // 我的余额
    private double mRed = 0.0;// 我的红包
    private int red_status = 0;// 0使用红包，1表示不使用红包
    private int balance_status = 0;// 0表示使用余额，1表示不使用余额
    private String seller_id = "";//商户id
    private String service_id = "";//服务id

    private PayUtils payUtils;
    private String out_trade_no;

    /**
     * 银联支付渠道
     */
    private static final String CHANNEL_UPACP = "upacp";


    /**
     * 洗车券
     */
    private static final String CHANNEL_COUPON = "WASHCOUPON";

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

    private MyBroadcastReceiver broad;
    private String price;

    // 可能是订单详情界面传递过来的
    private String recordId;
    private PreparePayCouponResponse couponListResponse;
    private int currentCouponId = -1; // 抵用优惠券ID
    private UseCouponAdapter adapter;
    private WeiXinPay weixinPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_washcar_pay);
        ViewUtils.inject(this);
        init();

    }


    /**
     * 红包、余额支付完成
     */
    private void paymentDone() {
        String store_name = tv_pay_name.getText().toString();
        String goods_name = tv_pay_service_name.getText().toString();
        String order_sn = out_trade_no;
        Intent intent = new Intent(WashCarPayActivity.this,
                OrderPaymentSuccessActivity.class);
        intent.putExtra("store_name", store_name);
        intent.putExtra("price", price);
        intent.putExtra("goods_name", goods_name);
        intent.putExtra("order_sn", order_sn);
        intent.putExtra("recordId", recordId);
        startActivity(intent);
        this.finish();
    }

    private void init() {
        left_action.setText(R.string.back);
        title.setText("支付");
        tv_pay_name.setText(getIntent().getStringExtra("seller"));
        tv_pay_service_name.setText(getIntent().getStringExtra("service"));
        tv_wash_pay_num.setText("￥" + getIntent().getStringExtra("price") + "元");
        tv_wash_money.setText("￥" + getIntent().getStringExtra("price") + "元");

        // TODO 下面三个暂时没有用.
        pay_type = getIntent().getStringExtra("type");
        order_sn = getIntent().getStringExtra("order_sn");
        seller_id = getIntent().getStringExtra("seller_id");


        // 可能是订单详情界面传递过来的
        recordId = getIntent().getStringExtra("recordId");
        service_id = getIntent().getStringExtra("service_id");

        price = getIntent().getStringExtra("price");
        if (!StringUtil.isEmpty(price)) {
            amount = StringUtil.getDouble(price);
        }

        getRedData();

        // 优惠券开关
        cb_red.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                updateMoney();
                if (arg1) {
                    if (channel.equals(CHANNEL_COUPON)) { // 如果优惠券打开,则关闭选择
                        img_alipay.setImageResource(R.drawable.dian22x);
                        img_weixin.setImageResource(R.drawable.dian12x);
                        img_wallet_pay.setImageResource(R.drawable.dian12x);
                        img_washcar_coupon.setImageResource(R.drawable.dian12x);
                        channel = CHANNEL_ALIPAY;
                    }
                    red_status = 1;
                } else {
                    red_status = 0;
                }
            }
        });

        // 洗车券开关
        cb_balance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0,
                                         boolean arg1) {

                // TODO 洗车券支付

                if (arg1) {
                    if (cb_red.isChecked()) { // 优惠券有勾选
                        cb_red.setChecked(false);
                        tv_red_hint.setText(R.string.purse_coupon);
                        unlist_washcar_pay.setVisibility(View.GONE);
                    }
                    amount = 0;
//                    tv_wash_pay_num.setText("￥" + amount + "元");
                    tv_wash_money.setText("￥" + String.format("%.2f", amount) + "元");
                    img_alipay.setImageResource(R.drawable.dian12x);
                    img_weixin.setImageResource(R.drawable.dian12x);
                    img_wallet_pay.setImageResource(R.drawable.dian12x);
                    channel = CHANNEL_COUPON;
                } else {
                    if (!StringUtil.isEmpty(price)) {
                        amount = StringUtil.getDouble(price);
                    }
//                    tv_wash_pay_num.setText("￥" + amount + "元");
                    tv_wash_money.setText("￥" + String.format("%.2f", amount) + "元");
                    img_alipay.setImageResource(R.drawable.dian22x);
                    img_weixin.setImageResource(R.drawable.dian12x);
                    img_wallet_pay.setImageResource(R.drawable.dian12x);
                    channel = CHANNEL_ALIPAY;
                }
            }
        });

        cb_red_packet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // 红包
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateMoney();
                if (isChecked) {
                    if (channel.equals(CHANNEL_COUPON)) { // 如果优惠券打开,则关闭选择
                        img_alipay.setImageResource(R.drawable.dian22x);
                        img_weixin.setImageResource(R.drawable.dian12x);
                        img_wallet_pay.setImageResource(R.drawable.dian12x);
                        img_washcar_coupon.setImageResource(R.drawable.dian12x);
                        channel = CHANNEL_ALIPAY;
                    }
//                    tv_red_packet_hint.setText(getString(R.string.washcar_purse_red) + ": ￥" + (amount >= 0 ? red: amount) + "元");
                } else {
                    tv_red_packet_hint.setText(getString(R.string.washcar_purse_red));
                }
            }
        });
    }

    /**
     * 购买用于服务器生成订单
     */
    private void prepareOrder() {
        if (null == service_id && !"".equals(service_id)) {
            showToast("订单初始化失败");
            return;
        }

        if (CHANNEL_WECHAT.equals(channel) && !WeiXinPay.getinstance(baseContext.getApplicationContext()).isWXAppInstalledAndSupported()) {
            tv_wash_affirm.setClickable(true);
            return;
        }

        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_ORDER + NetInterface.BUY_SERVICE + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("serviceId", service_id);
        if (0 != amount || CHANNEL_COUPON.equals(channel)) // 表示价格已经被洗车券抵用完了
            param.put("paymentType", channel);
        if (1 == red_status && -1 != currentCouponId) // 表示使用了优惠券
            param.put("couponId", currentCouponId);
        else {
            cb_red.setChecked(false);
            unlist_washcar_pay.setVisibility(View.GONE);
        }
        param.put("isRedPacket", cb_red_packet.isChecked());
        // 表示延迟支付
        if (null != recordId && !"".equals(recordId))
            param.put("recordId", recordId);
        param.put(Constant.PARAMETER_TAG, NetInterface.BUY_SERVICE);
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

    /**
     * 获取红包信息
     */
    protected void getRedData() {
        if (isLogined()) {
            if (null == service_id && !"".equals(service_id)) {
                showToast("订单初始化失败");
                return;
            }
            ProgrosDialog.openDialog(baseContext);
            String url = NetInterface.BASE_URL + NetInterface.TEMP_ORDER + NetInterface.GET_PAY_COUPON_RED + NetInterface.SUFFIX;
            Map<String, Object> param = new HashMap<>();
            param.put("userId", loginResponse.getDesc());
            param.put("token", loginResponse.getToken());
            param.put("serviceId", service_id);
            param.put(Constant.PARAMETER_TAG, NetInterface.PAY_COUPON);
            netWorkHelper.PostJson(url, param, this);

        } else {
            startActivity(new Intent(WashCarPayActivity.this,
                    LoginActivity.class));
            overridePendingTransition(R.anim.score_business_query_enter,
                    R.anim.score_business_query_exit);
        }
    }

    @OnClick({R.id.left_action, R.id.rb_alipay, R.id.rb_weixin,
            R.id.tv_wash_affirm, R.id.ll_alipay, R.id.ll_weixin, R.id.ll_wallet, R.id.cb_red, R.id.ll_washcar_pay
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_action:
                finish();
                break;
            case R.id.tv_wash_affirm:
                tv_wash_affirm.setClickable(false);
                prepareOrder();
                break;
            case R.id.ll_alipay:
                // payment_type = "zfb";
                img_alipay.setImageResource(R.drawable.dian22x);
                img_weixin.setImageResource(R.drawable.dian12x);
                img_washcar_coupon.setImageResource(R.drawable.dian12x);
                img_wallet_pay.setImageResource(R.drawable.dian12x);
                cb_balance.setChecked(false);
                channel = CHANNEL_ALIPAY;
                updateMoney();
                break;
            case R.id.ll_weixin:
                cb_balance.setChecked(false);
                img_weixin.setImageResource(R.drawable.dian22x);
                img_alipay.setImageResource(R.drawable.dian12x);
                img_washcar_coupon.setImageResource(R.drawable.dian12x);
                img_wallet_pay.setImageResource(R.drawable.dian12x);
                channel = CHANNEL_WECHAT;
                updateMoney();
                break;

            case R.id.ll_wallet: // 余额支付
                cb_balance.setChecked(false);
                img_weixin.setImageResource(R.drawable.dian12x);
                img_alipay.setImageResource(R.drawable.dian12x);
                img_washcar_coupon.setImageResource(R.drawable.dian12x);
                img_wallet_pay.setImageResource(R.drawable.dian22x);
                channel = CHANNEL_WALLET;
                updateMoney();
                break;
            case R.id.ll_washcar_pay: // 洗车券支付
                img_weixin.setImageResource(R.drawable.dian12x);
                img_alipay.setImageResource(R.drawable.dian12x);
                img_wallet_pay.setImageResource(R.drawable.dian12x);
                img_washcar_coupon.setImageResource(R.drawable.dian22x);
                channel = CHANNEL_COUPON;
                if (cb_red.isChecked()) { // 优惠券有勾选
                    cb_red.setChecked(false);
                    tv_red_hint.setText(R.string.purse_coupon);
                    unlist_washcar_pay.setVisibility(View.GONE);
                }

                if (cb_red_packet.isChecked()) { // 红包有勾选
                    cb_red_packet.setChecked(false);
                    tv_red_packet_hint.setText(getString(R.string.washcar_purse_red));
                }
                amount = 0;
//                tv_wash_pay_num.setText("￥" + amount + "元");
                tv_wash_money.setText("￥" + String.format("%.2f", amount) + "元");
                break;
            case R.id.cb_red:

                if (null == couponListResponse || null == couponListResponse.getMsg() || null == couponListResponse.getMsg().getCouponList() || 0 == couponListResponse.getMsg().getCouponList().size()) {
                    showToast("当前没有可用优惠券");
                    cb_red.setChecked(false);
                    return;
                }


                if (cb_red.isChecked()) { // 有选择
                    unlist_washcar_pay.setVisibility(View.VISIBLE);
                    tv_red_hint.setText(R.string.purse_coupon);
                    if (!StringUtil.isEmpty(price)) {
                        amount = StringUtil.getDouble(price);
                    }
//                    if (cb_red_packet.isChecked()) {//红包中的时候
//                        amount = amount - Double.valueOf(red) > 0 ? amount - Double.valueOf(red) : 0;
//                        tv_red_packet_hint.setText(getString(R.string.washcar_purse_red) + ": ￥" + (amount == 0 ? price : red) + "元");
//                    }
//
//                    tv_wash_pay_num.setText("￥" + amount + "元");
//                    tv_wash_money.setText("￥" + amount + "元");
                    if (null != adapter) {
                        int position = adapter.getCheckCouponPosition();
//                        if (-1 != position)
                        onCheckCoupon(position);
                    }
                } else { // 未选中


                    unlist_washcar_pay.setVisibility(View.GONE);
                    tv_red_hint.setText(R.string.purse_coupon);
                    if (!StringUtil.isEmpty(price)) {
                        amount = StringUtil.getDouble(price);
                    }

                    if (cb_red_packet.isChecked()) {//红包中的时候
                        amount = amount - Double.valueOf(red) > 0 ? amount - Double.valueOf(red) : 0;
                        tv_red_packet_hint.setText(getString(R.string.washcar_purse_red) + ": ￥" + (amount == 0 ? price : red) + "元");
                    }

//                    tv_wash_pay_num.setText("￥" + amount + "元");
                    tv_wash_money.setText("￥" + String.format("%.2f", amount) + "元");
                }
                break;
            default:
                break;
        }
    }


    private void updateMoney() {
        if (cb_red.isChecked()) { // 优惠券选中
            unlist_washcar_pay.setVisibility(View.VISIBLE);
            if (null != adapter) {
                int position = adapter.getCheckCouponPosition();
//                if (-1 != position)
                onCheckCoupon(position);
            }
        } else {
            unlist_washcar_pay.setVisibility(View.GONE);
            tv_red_hint.setText(R.string.purse_coupon);
            if (!StringUtil.isEmpty(price)) {
                amount = StringUtil.getDouble(price);
            }

            if (cb_red_packet.isChecked()) { // 有红包选中
                amount = amount - Double.valueOf(red) > 0 ? amount - Double.valueOf(red) : 0;
                tv_red_packet_hint.setText(getString(R.string.washcar_purse_red) + ": ￥" + (amount == 0 ? price : red) + "元");
            }
//            tv_wash_pay_num.setText("￥" + amount + "元");
            tv_wash_money.setText("￥" + String.format("%.2f", amount) + "元");
        }


    }

    @Override
    public void receive(String TAG, String data) {
        ProgrosDialog.closeProgrosDialog();
//        tv_wash_affirm.setClickable(true);
        switch (TAG) {
            case NetInterface.BUY_SERVICE: // 购买
                PreparePayResponse preparePayResponse = (PreparePayResponse) GsonUtil.getInstance().convertJsonStringToObject(data, PreparePayResponse.class);
                if (!preparePayResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    tv_wash_affirm.setClickable(true);
                    showToast(preparePayResponse.getDesc());
                    return;
                }

                out_trade_no = preparePayResponse.getMsg().getOut_trade_no();
                // 更新recordId
                if (null != preparePayResponse.getDesc())
                    recordId = preparePayResponse.getDesc();

                // 优惠券抵用完了,直接更新了.
                if (null != preparePayResponse.getMsg() && !preparePayResponse.getMsg().isNeedPay()) {
                    loginResponse.setToken(preparePayResponse.getToken());
//                    LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
                    updatePacket();
                    return;
                }

                switch (channel) {
                    case CHANNEL_ALIPAY: // 支付宝
                        payUtils = new PayUtils();
                        payUtils.setOutTradeNo(out_trade_no); // 设置订单号
                        payUtils.setPayListener(this);
                        payUtils.pay(WashCarPayActivity.this, tv_pay_name.getText() + "", tv_pay_service_name.getText() + "", amount);
//                        payUtils.pay(WashCarPayActivity.this, tv_pay_name.getText() + "", tv_pay_service_name.getText() + "", 0.01);
                        break;
                    case CHANNEL_WECHAT:// 微信
                        String prepay_id = preparePayResponse.getMsg().getPrepay_id();
                        String nonce_str = preparePayResponse.getMsg().getNonce_str();
                        LogHelper.d(prepay_id + "----" + nonce_str);
                        weixinPay = WeiXinPay.getinstance(baseContext.getApplicationContext());
                        weixinPay.pay(prepay_id, nonce_str);
                        break;
                    case CHANNEL_WALLET://钱包
                        updatePacket();
                        break;
                    case CHANNEL_COUPON: //洗车券
                        updatePacket();
                        break;
                }


                loginResponse.setToken(preparePayResponse.getToken());
//                LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
                break;
            case NetInterface.UPDATE_PAY_STATUS://更新支付状态
                tv_wash_affirm.setClickable(true);
                BaseResponse response = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
                if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    showToast(R.string.server_link_fault);
                    return;
                }

                // TODO 已经判断过是否为微信支付.
                showToast("支付成功");
                loginResponse.setToken(response.getToken());
//                LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
                paymentDone();
                break;

            case NetInterface.PAY_COUPON: // 支付请求可用优惠券

                couponListResponse = (PreparePayCouponResponse) GsonUtil.getInstance().convertJsonStringToObject(data, PreparePayCouponResponse.class);
                if (!couponListResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    showToast(couponListResponse.getDesc());
                    return;
                }

                if (null == couponListResponse.getMsg())
                    return;

                if (couponListResponse.getMsg().isExistWashing()) { // 有洗车券
                    ll_washcar_pay.setVisibility(View.VISIBLE);
                }

                red = couponListResponse.getMsg().getRedPacketAmount();
                if (!StringUtil.isEmpty(red) && 0 != Double.valueOf(red)) { // 有红包
                    rl_red_packet.setVisibility(View.VISIBLE);
                }


                if (null != couponListResponse.getMsg().getCouponList() && 0 < couponListResponse.getMsg().getCouponList().size()) {
                    adapter = new UseCouponAdapter(baseContext, couponListResponse.getMsg().getCouponList());
                    adapter.setOnUserClickCouponListener(this);
                    unlist_washcar_pay.setAdapter(adapter);
                }


                loginResponse.setToken(couponListResponse.getToken());
//                LoginMessageUtils.saveloginmsg(baseContext, loginResponse);

                break;

        }

    }


    @Override
    public void error(String errorMsg) {
        ProgrosDialog.closeProgrosDialog();
        tv_wash_affirm.setClickable(true);
        showToast(R.string.server_link_fault);
    }


//    /**
//     * 支付计算
//     */
//    private void redCompute(double red, double remainder, int score) {
//        // 是否显示红包抵用
//        if (red <= 0.0) {
//            rl_red.setVisibility(View.GONE);
//        } else if (red > 0.0 && red_status == 0) {
//            rl_red.setVisibility(View.VISIBLE);
//            // 红包计算
//            redCount(red);
//            double temp = balance - this.red;
//            balance = convert(temp);
////            tv_red_hint.setText("可使用红包抵用" + this.red + "元");
//        }
//        // 是否显示余额支付
//        if (remainder <= 0.0) {
//            rl_balance.setVisibility(View.GONE);
//        } else if (remainder > 0.0 && balance_status == 0) {
//            rl_balance.setVisibility(View.VISIBLE);
//            // 余额计算
//            balanceCount(remainder);
//            double temp = balance - this.remainder;
//            balance = convert(temp);
//            tv_balance_hint.setText("可使用余额支付" + this.remainder + "元");
//        }
//
//        tv_wash_money.setText("￥" + balance + "元");
//        showPay();
//    }

    /**
     * 保留两位小数
     *
     * @param value
     * @return
     */
    private double convert(double value) {
        long l1 = Math.round(value * 100); //四舍五入
        double ret = l1 / 100.0; //注意：使用 100.0 而不是 100
        return ret;
    }

//    /**
//     * 显示在线支付
//     */
//    private void showPay() {
//        // 是否显示在线支付
////		if (balance > 0) {
////			ll_pay.setVisibility(View.VISIBLE);
////		} else {
////			ll_pay.setVisibility(View.GONE);
////		}
//        ll_pay.setVisibility(View.VISIBLE);
//    }

//    /**
//     * 余额计算
//     *
//     * @param remainder
//     */
//    private void balanceCount(double remainder) {
//        if (remainder >= balance) {
//            this.remainder = convert(balance);
//        } else {
//            this.remainder = 0.0;
//        }
//    }

//    /**
//     * 红包的计算
//     *
//     * @param red
//     * @return
//     */
//    private void redCount(double red) {
//        if (StringUtil.isEquals("px", pay_type, true)) {
//            if (red >= balance) {
//                this.red = convert(balance);
//            } else {
//                this.red = convert(red);
//            }
//        } else {
//            if (red >= balance * 0.15) {
//                this.red = convert(balance);
//            } else {
//                this.red = convert(red);
//            }
//        }
//    }


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
                    getPaySuccessData();
                    showToast("支付成功");
                } else {

                    if (StringUtil.isEquals("fail", result, true)) {
                        showMsg("支付失败", "", "");
                    } else if (StringUtil.isEquals("cancel", result, true)) {
                        showMsg("取消支付", "", "");
                    } else if (StringUtil.isEquals("invalid", result, true)) {
                        showMsg("未安装付款插件", "", "");
                    }
//                    String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
//                    String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                }
            }
        }
    }

    /**
     * 获取在线支付成功后的订单数据
     */
    private void getPaySuccessData() {
        updatePacket();
    }

    public void showMsg(String title, String msg1, String msg2) {
        String str = title;
        if (null != msg1 && msg1.length() != 0) {
            str += "\n" + msg1;
        }
        if (null != msg2 && msg2.length() != 0) {
            str += "\n" + msg2;
        }
        Builder builder = new Builder(WashCarPayActivity.this);
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
        if (null != weixinPay)
            weixinPay.onDestory();
    }

    @Override
    public void onPaySuccess() {
        // TODO 调用状态接口
        updatePacket();


    }

    @Override
    public void onPayConfirm() {

    }

    @Override
    public void onPayFail() {
        tv_wash_affirm.setClickable(true);
    }


    private void updatePacket() {
        tv_wash_affirm.setClickable(true);
        if (CHANNEL_ALIPAY.equals(channel) || CHANNEL_WECHAT.equals(channel)) { // 微信或者支付宝
            if (0 < amount) { // 价钱大于0,就发起过微信和支付宝
                // TODO 跳转到支付完成
                paymentDone();
                return;
            }
        }
        if (null == recordId && "".equals(recordId)) {
            showToast("更新订单状态失败");
            return;
        }
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_ORDER + NetInterface.UPDATE_PAY_STATUS + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("recordId", recordId);
        param.put("chargeStatus", "PAID");//已支付
        param.put(Constant.PARAMETER_TAG, NetInterface.UPDATE_PAY_STATUS);
        netWorkHelper.PostJson(url, param, this);
    }

    @Override
    public void onCheckCoupon(int position) {
        if (-1 == position) {
            amount = Double.valueOf(price);
            if (cb_red_packet.isChecked()) {
                amount = amount - Double.valueOf(red) > 0 ? amount - Double.valueOf(red) : 0;
                tv_red_packet_hint.setText(getString(R.string.washcar_purse_red) + ": ￥" + (amount == 0 ? price : red) + "元");
            }
            tv_red_hint.setText(getString(R.string.purse_coupon) + ": ￥0元");
//            tv_wash_pay_num.setText("￥" + amount + "元");
            tv_wash_money.setText("￥" + String.format("%.2f", amount) + "元");
            return;
        }

        amount = Double.valueOf(price);

        if (cb_red_packet.isChecked()) { // 红包中了,先计算红包
            amount = amount - Double.valueOf(red) > 0 ? amount - Double.valueOf(red) : 0;
            tv_red_packet_hint.setText(getString(R.string.washcar_purse_red) + ": ￥" + (amount == 0 ? price : red) + "元");
        }


        if (0 == amount) {
            tv_red_hint.setText(getString(R.string.purse_coupon) + ": ￥0元");
            adapter.setPositionChecked(position, false);
            showToast("红包已经全额抵扣，无需再使用优惠券");
            return;
//            cb_red.setChecked(false);
        } else {
            double couponMoney = Double.valueOf(couponListResponse.getMsg().getCouponList().get(position).getCoupon().getAmount());
            tv_red_hint.setText(getString(R.string.purse_coupon) + ": ￥" + (couponMoney > amount ? amount : couponMoney) + "元");
            amount = calcMoney(amount, couponMoney);
        }
//        tv_wash_pay_num.setText("￥" + String.format("%.2f", amount) + "元");
        tv_wash_money.setText("￥" + String.format("%.2f", amount) + "元");
        currentCouponId = couponListResponse.getMsg().getCouponList().get(position).getId();
    }

    private double calcMoney(double price, double couponMoney) {
        amount = (price - couponMoney) < 0 ? 0 : (price - couponMoney);
        return amount;
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            if (!StringUtil.isEquals(intent.getAction(), Constant.REFRESH_FLAG,
                    true)) {
                return;
            }
            if (StringUtil.isEquals(Constant.CURRENT_REFRESH, Constant.WEIXIN_PAY_REFRESH, true)) {
                Constant.EDIT_FLAG = true;
                getPaySuccessData();
            } else if (StringUtil.isEquals(Constant.CURRENT_REFRESH, Constant.WEIXIN_PAY_FAIL_REFRESH, true)) {
                onPayFail();
            }
        }
    }
}
