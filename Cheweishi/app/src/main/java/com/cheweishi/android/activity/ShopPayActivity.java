package com.cheweishi.android.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.PreparePayResponse;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.PayUtils;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.utils.weixinpay.WeiXinPay;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tanck on 9/28/2016.
 * <p/>
 * Describe: 商品支付界面
 */
public class ShopPayActivity extends BaseActivity implements PayUtils.OnPayListener {

    private String[] orderId; // 订单数组 : 可能是多个商家的商品一起下单的

    private String money = "0";//暂时的价钱

    private final String ALIPAY = "ALIPAY";// 阿里支付

    private final String WECHAT = "WECHAT";//微信支付

    private final String WALLET = "WALLET";//钱包支付

    private String paymentType = ALIPAY;// 支付类型

    @ViewInject(R.id.tv_shop_money)
    private TextView tv_shop_money;//显示价格

    @ViewInject(R.id.left_action)
    private Button left_action;

    @ViewInject(R.id.title)
    private TextView title;

    @ViewInject(R.id.img_alipay)
    private ImageView img_alipay;

    @ViewInject(R.id.img_weixin)
    private ImageView img_weixin;

    @ViewInject(R.id.img_wallet_pay)
    private ImageView img_wallet_pay;

    @ViewInject(R.id.tv_shop_affirm)
    private TextView tv_shop_affirm; // 支付

    private String out_trade_no;//交易流水号

    private PayUtils payUtils;//阿里支付工具类

    private WeiXinPay weixinPay;//微信支付工具类

    private MyBroadcastReceiver broad;//微信支付广播

    @Override
    protected void onResume() {
        super.onResume();
        if (broad == null) {
            broad = new MyBroadcastReceiver();
        }
        IntentFilter intentFilter = new IntentFilter(Constant.REFRESH_FLAG);
        registerReceiver(broad, intentFilter);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_pro_pay);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        left_action.setText(R.string.back);
        title.setText(R.string.wash_pay);
        String temp = getIntent().getStringExtra("orderId");
        LogHelper.d("orderIds:" + temp);
        if (StringUtil.isEmpty(temp)) {
            showToast(R.string.pay_error_notify);
//            finish();
            return;
        }
        if (temp.contains(","))
            orderId = temp.split(",");
        else {
            orderId = new String[1];
            orderId[0] = temp;
        }
//        orderId = new Long[2];
//        orderId[0]= Long.valueOf(21);
//        orderId[1]= Long.valueOf(22);
        money = getIntent().getStringExtra("money");
        tv_shop_money.setText("￥" + money);
    }

    @OnClick({R.id.tv_shop_affirm, R.id.left_action, R.id.ll_alipay, R.id.ll_weixin, R.id.ll_wallet})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_shop_affirm: // 确认支付
                tv_shop_affirm.setClickable(false);
                pay();
                break;
            case R.id.left_action: // 返回
                finish();
                break;
            case R.id.ll_alipay: // 支付宝
                paymentType = ALIPAY;
                img_alipay.setImageResource(R.drawable.dian22x);
                img_weixin.setImageResource(R.drawable.dian12x);
                img_wallet_pay.setImageResource(R.drawable.dian12x);
                break;
            case R.id.ll_weixin: // 微信
                paymentType = WECHAT;
                img_alipay.setImageResource(R.drawable.dian12x);
                img_weixin.setImageResource(R.drawable.dian22x);
                img_wallet_pay.setImageResource(R.drawable.dian12x);
                break;
            case R.id.ll_wallet: // 钱包
                paymentType = WALLET;
                img_alipay.setImageResource(R.drawable.dian12x);
                img_weixin.setImageResource(R.drawable.dian12x);
                img_wallet_pay.setImageResource(R.drawable.dian22x);
                break;
        }
    }

    @Override
    public void receive(String data) {

        PreparePayResponse payResponse = (PreparePayResponse) GsonUtil.getInstance().convertJsonStringToObject(data, PreparePayResponse.class);
        if (!payResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            tv_shop_affirm.setClickable(true);
            ProgrosDialog.closeProgrosDialog();
            showToast(payResponse.getDesc());
            return;
        }

        out_trade_no = payResponse.getMsg().getOut_trade_no();

        // 优惠券抵用完了,直接更新了.
        if (null != payResponse.getMsg() && !payResponse.getMsg().isNeedPay()) {
            loginResponse.setToken(payResponse.getToken());
            updatePacket();
            return;
        }

        switch (paymentType) {
            case ALIPAY: // 支付宝
                payUtils = new PayUtils();
                payUtils.setOutTradeNo(out_trade_no); // 设置订单号
                payUtils.setPayListener(this);
                payUtils.pay(ShopPayActivity.this, "车生活商城商品", "车生活商城商品", Double.valueOf(money));
                tv_shop_affirm.setClickable(true);
                ProgrosDialog.closeProgrosDialog();
//                        payUtils.pay(WashCarPayActivity.this, tv_pay_name.getText() + "", tv_pay_service_name.getText() + "", 0.01);
                break;
            case WECHAT:// 微信
                String prepay_id = payResponse.getMsg().getPrepay_id();
                String nonce_str = payResponse.getMsg().getNonce_str();
                LogHelper.d(prepay_id + "----" + nonce_str);
                weixinPay = WeiXinPay.getinstance(baseContext.getApplicationContext());
                weixinPay.pay(prepay_id, nonce_str);
                tv_shop_affirm.setClickable(true);
                ProgrosDialog.closeProgrosDialog();
                break;
            case WALLET://钱包
                updatePacket();
                break;
        }
        loginResponse.setToken(payResponse.getToken());
    }

    @Override
    public void receive(String TAG, String data) {
        BaseResponse baseResponse = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
        if (!baseResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            tv_shop_affirm.setClickable(true);
            ProgrosDialog.closeProgrosDialog();
            showToast(baseResponse.getDesc());
            return;
        }

        loginResponse.setToken(baseResponse.getToken());
        tv_shop_affirm.setClickable(true);
        ProgrosDialog.closeProgrosDialog();

        showToast("支付成功");
        // TODO 跳转到支付完成的界面
    }

    @Override
    public void error(String errorMsg) {
        showToast(R.string.server_link_fault);
        tv_shop_affirm.setClickable(true);
        ProgrosDialog.closeProgrosDialog();
    }

    /**
     * 支付
     */
    private void pay() {
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_ORDER_CREATE + NetInterface.PAY + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("orderIds", orderId);
        param.put("paymentType", paymentType);
        netWorkHelper.PostJson(url, param, this);
    }

    /**
     * 更新支付订单状态
     */
    private void updatePacket() {
        tv_shop_affirm.setClickable(true);
        if (ALIPAY.equals(paymentType) || ALIPAY.equals(paymentType)) { // 微信或者支付宝
            return;
        }
        if (null == orderId) {
            showToast("更新订单状态失败");
            return;
        }
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_ORDER_CREATE + NetInterface.UPDATE_PAY_STATUS + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("orderIds", orderId);
        param.put(Constant.PARAMETER_TAG, NetInterface.UPDATE_PAY_STATUS);
        netWorkHelper.PostJson(url, param, this);
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
        tv_shop_affirm.setClickable(true);
    }


    private class MyBroadcastReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            if (!StringUtil.isEquals(intent.getAction(), Constant.REFRESH_FLAG,
                    true)) {
                return;
            }
            if (StringUtil.isEquals(Constant.CURRENT_REFRESH, Constant.WEIXIN_PAY_REFRESH, true)) {
                Constant.EDIT_FLAG = true;
                updatePacket();
            } else if (StringUtil.isEquals(Constant.CURRENT_REFRESH, Constant.WEIXIN_PAY_FAIL_REFRESH, true)) {
                onPayFail();
            }
        }
    }
}
