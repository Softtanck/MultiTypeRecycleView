package com.cheweishi.android.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.ShopOrderListDetailAdapter;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.ShopOrderListResponse;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.widget.CustomDialog;
import com.cheweishi.android.widget.UnSlidingListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tanck on 10/21/2016.
 * <p>
 * Describe:商品订单详情
 */
public class ShopOrderDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @ViewInject(R.id.left_action)
    private Button left_action;

    @ViewInject(R.id.title)
    private TextView title;

    @ViewInject(R.id.tv_shop_detail_order_consignee)
    private TextView tv_shop_detail_order_consignee;//收货人名字

    @ViewInject(R.id.tv_shop_detail_order_consignee_phone)
    private TextView tv_shop_detail_order_consignee_phone;//收货人电话

    @ViewInject(R.id.tv_shop_detail_order_consignee_address)
    private TextView tv_shop_detail_order_consignee_address;//收货地址

    @ViewInject(R.id.usl_shop_order_detail)
    private UnSlidingListView usl_shop_order_detail;//商品列表

    @ViewInject(R.id.tv_shop_order_detail_number)
    private TextView tv_shop_order_detail_number;//商品数量

    @ViewInject(R.id.tv_shop_order_detail_money)
    private TextView tv_shop_order_detail_money;//合计价格

    @ViewInject(R.id.tv_shop_order_detail_pay_way)
    private TextView tv_shop_order_detail_pay_way;//支付方式,未支付时显示当前状态

    @ViewInject(R.id.tv_shop_order_detail_sn)
    private TextView tv_shop_order_detail_sn;//商品订单号

    @ViewInject(R.id.tv_shop_order_detail_time)
    private TextView tv_shop_order_detail_time;//下单时间

    @ViewInject(R.id.tv_shop_order_detail_name)
    private TextView tv_shop_order_detail_name;//收货人名字

    @ViewInject(R.id.tv_shop_order_detail_status)
    private TextView tv_shop_order_detail_status;//订单状态

    @ViewInject(R.id.tv_shop_order_detail_pay)
    private TextView tv_shop_order_detail_pay;//支付按钮

    @ViewInject(R.id.tv_shop_order_detail_back)
    private TextView tv_shop_order_detail_back;//退款

    @ViewInject(R.id.tv_shop_order_detail_goods)
    private TextView tv_shop_order_detail_goods;//退货

    @ViewInject(R.id.tv_shop_order_detail_cancel)
    private TextView tv_shop_order_detail_cancel;//取消按钮

    @ViewInject(R.id.rl_shop_order_detail_bottom)
    private RelativeLayout rl_shop_order_detail_bottom;//底部

    private ShopOrderListResponse.MsgBean data;

    private ShopOrderListDetailAdapter adapter;

    private int currentOrderId = -1;//当前订单ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_order_detail);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        left_action.setText(R.string.back);
        title.setText(R.string.order_details);
        data = (ShopOrderListResponse.MsgBean) getIntent().getSerializableExtra("data");
        if (null == data) {
            showToast("解析数据错误");
            return;
        }
        adapter = new ShopOrderListDetailAdapter(baseContext, data.getOrderItem());
        usl_shop_order_detail.setAdapter(adapter);
        usl_shop_order_detail.setOnItemClickListener(this);
        tv_shop_detail_order_consignee.setText(data.getConsignee());
        tv_shop_detail_order_consignee_phone.setText(data.getPhone());
        tv_shop_detail_order_consignee_address.setText(data.getAddress());
        tv_shop_order_detail_number.setText("共" + data.getProductCount() + "件商品     实付:");
        tv_shop_order_detail_money.setText("￥" + data.getAmount());
        updateStatus();

        tv_shop_order_detail_sn.setText(data.getSn());
        tv_shop_order_detail_time.setText(getDate(data.getCreateDate()));
        tv_shop_order_detail_name.setText(data.getConsignee());
    }

    private void updateStatus() {
        String tempPayStatus = data.getPaymentStatus();
        String tempOrderStatus = data.getOrderStatus();
        String tempShippingStatus = data.getShippingStatus();
        if ("failure".equals(tempOrderStatus)) {
            tv_shop_order_detail_status.setText(R.string.time_out_close);
            rl_shop_order_detail_bottom.setVisibility(View.GONE);
            tv_shop_order_detail_pay_way.setText(R.string.no_pay); // TODO 判断状态.
        } else if ("unpaid".equals(tempPayStatus) && "cancelled".equals(tempOrderStatus)) {
            tv_shop_order_detail_status.setText(R.string.cancel_order);
            rl_shop_order_detail_bottom.setVisibility(View.GONE);
        } else if ("unpaid".equals(tempPayStatus)) {
            tv_shop_order_detail_status.setText(R.string.unpaid);
            tv_shop_order_detail_cancel.setVisibility(View.VISIBLE);
        } else if ("paid".equals(tempPayStatus) && "unconfirmed".equals(tempOrderStatus)) {
            tv_shop_order_detail_status.setText(R.string.paid);
            tv_shop_order_detail_pay_way.setText(R.string.no_pay);
            tv_shop_order_detail_cancel.setVisibility(View.GONE);
            tv_shop_order_detail_pay.setVisibility(View.GONE);
            tv_shop_order_detail_back.setVisibility(View.VISIBLE);
            tv_shop_order_detail_pay_way.setText(getPayWay(data.getPaymentType()));
        } else if ("paid".equals(tempPayStatus) && "confirmed".equals(tempOrderStatus)) {
            tv_shop_order_detail_status.setText(R.string.unsend);
            tv_shop_order_detail_cancel.setVisibility(View.GONE);
            tv_shop_order_detail_pay.setVisibility(View.GONE);
            tv_shop_order_detail_back.setVisibility(View.VISIBLE);
            tv_shop_order_detail_pay_way.setText(getPayWay(data.getPaymentType()));
        } else if ("shipped".equals(tempShippingStatus)) {
            tv_shop_order_detail_status.setText(R.string.unrec);
            tv_shop_order_detail_cancel.setVisibility(View.GONE);
            tv_shop_order_detail_pay.setVisibility(View.GONE);
            tv_shop_order_detail_back.setVisibility(View.VISIBLE);
            tv_shop_order_detail_pay_way.setText(getPayWay(data.getPaymentType()));
        } else if ("received".equals(tempShippingStatus)) {
            tv_shop_order_detail_status.setText(R.string.received);
            tv_shop_order_detail_cancel.setVisibility(View.GONE);
            tv_shop_order_detail_pay.setVisibility(View.GONE);
            tv_shop_order_detail_back.setVisibility(View.GONE);
            tv_shop_order_detail_goods.setVisibility(View.VISIBLE);
            tv_shop_order_detail_pay_way.setText(getPayWay(data.getPaymentType()));
        } else {
            tv_shop_order_detail_status.setText(R.string.not_no);
            rl_shop_order_detail_bottom.setVisibility(View.GONE);
        }
    }

    private int getPayWay(String payType) {
        switch (payType) {
            case "ALIPAY":
                return R.string.alipay;
            case "WECHAT":
                return R.string.wechatPay;
            case "WALLET":
                return R.string.walletPay;
        }
        return R.string.no_pay;
    }

    @OnClick({R.id.left_action, R.id.tv_shop_order_detail_cancel, R.id.tv_shop_order_detail_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_action:
                finish();
                break;
            case R.id.tv_shop_order_detail_cancel:
                currentOrderId = data.getId();
                showDeleteDialog(getString(R.string.cancel_order_desc), getString(R.string.confirm_order_cancel));
                break;
            case R.id.tv_shop_order_detail_pay:
                Intent pay = new Intent(baseContext, ShopPayActivity.class);
                pay.putExtra("orderId", String.valueOf(data.getId()));
                pay.putExtra("money", data.getAmount());
                startActivity(pay);
                break;
        }
    }

    /**
     * 转换时间
     *
     * @param time
     * @return
     */
    private String getDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(time);// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent detail = new Intent(baseContext, ProductDetailActivity.class);
        detail.putExtra("productId", data.getOrderItem().get(position).getProduct().getId());
        startActivity(detail);
    }


    public void showDeleteDialog(String msg, String buttonMsg) {
        CustomDialog.Builder builder = new CustomDialog.Builder(baseContext);
        builder.setMessage(msg);
        builder.setTitle(getString(R.string.remind));

        builder.setPositiveButton(buttonMsg,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (-1 == currentOrderId) {
                            showToast(R.string.cancel_error);
                            return;
                        }

                        cancelOrConfirm(false, currentOrderId);
                    }
                });
        builder.setNegativeButton(getString(R.string.cancel_order_cancel),
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }


    /**
     * 确认收货 or 取消订单
     *
     * @param isConfirm
     * @param orderId
     */
    private void cancelOrConfirm(boolean isConfirm, int orderId) {
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_ORDER_CREATE + NetInterface.OPT + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("orderId", orderId);
        if (isConfirm)
            param.put("oprType", "received");
        else
            param.put("oprType", "cancel");
        param.put(Constant.PARAMETER_TAG, NetInterface.OPT);
        netWorkHelper.PostJson(url, param, this);
    }


    @Override
    public void receive(String TAG, String data) {
        switch (TAG) {
            case NetInterface.OPT:
                BaseResponse baseResponse = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
                if (!baseResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    ProgrosDialog.closeProgrosDialog();
                    showToast(baseResponse.getDesc());
                    return;
                }
                loginResponse.setToken(baseResponse.getToken());
                ProgrosDialog.closeProgrosDialog();
                rl_shop_order_detail_bottom.setVisibility(View.GONE);
                tv_shop_order_detail_status.setText(R.string.cancel_order);
                break;

        }
    }
}
