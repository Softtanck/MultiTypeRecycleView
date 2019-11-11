package com.cheweishi.android.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.ShopPayOrderListAdapter;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.DefaultAddressResponse;
import com.cheweishi.android.entity.ShopPayOrderNative;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.CustomDialog;
import com.cheweishi.android.widget.ScrollListView;
import com.cheweishi.android.widget.UnSlidingListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tangce on 8/29/2016.
 */
public class ShopPayOrderActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @ViewInject(R.id.left_action)
    private Button left_action; // 左边标题

    @ViewInject(R.id.title)
    private TextView title; // 标题

    @ViewInject(R.id.usl_sp_order)
    private ScrollListView usl_sp_order; // 商品列表

    @ViewInject(R.id.cb_sp_order_invoice)
    private CheckBox cb_sp_order_invoice;//发票

    @ViewInject(R.id.tv_sp_order_invoice_notice)
    private TextView tv_sp_order_invoice_notice;//发票提示

    @ViewInject(R.id.ll_sp_order_invoice)
    private LinearLayout ll_sp_order_invoice;//发票内容

    @ViewInject(R.id.tv_sp_total_shop_item)
    private TextView tv_sp_total_shop_item;//商品总数

    @ViewInject(R.id.tv_sp_order_total_money)
    private TextView tv_sp_order_total_money;//总价钱

    @ViewInject(R.id.tv_sp_order_consignee)
    private TextView tv_sp_order_consignee;//收货人

    @ViewInject(R.id.tv_sp_order_consignee_phone)
    private TextView tv_sp_order_consignee_phone;//收货人电话

    @ViewInject(R.id.tv_sp_order_consignee_address)
    private TextView tv_sp_order_consignee_address;//收货人地址

    @ViewInject(R.id.tv_sp_order_consignee_title)
    private TextView tv_sp_order_consignee_title;//收货人

    @ViewInject(R.id.et_sp_order_invoice)
    private EditText et_sp_order_invoice; // 发票抬头

    @ViewInject(R.id.tv_sp_money)
    private TextView tv_sp_money;//总价钱

    private ShopPayOrderListAdapter adapter;

    private List<ShopPayOrderNative> list;

    private boolean isRefreshing = false;

    private int receiverId = -1;//收货人地址Id

    private boolean isNowBuy;//是否为立即购买

    @Override
    protected void onResume() {
        super.onResume();
        if (isRefreshing) {
            isRefreshing = false;
            getDefaultAddress(1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isRefreshing)
            isRefreshing = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_pay_order);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        left_action.setText(R.string.back);
        title.setText(R.string.confrim_order);
        isNowBuy = getIntent().getBooleanExtra("isNowBuy", false);
        list = (List<ShopPayOrderNative>) getIntent().getSerializableExtra("data");

        if (null != list && 0 < list.size()) {
            usl_sp_order.setFocusable(false);
            adapter = new ShopPayOrderListAdapter(baseContext, list);
            usl_sp_order.setAdapter(adapter);

            tv_sp_total_shop_item.setText("共计" + calcNumber() + "件商品");
            tv_sp_money.setText(String.valueOf(calcMoney()));
            tv_sp_order_total_money.setText("￥" + tv_sp_money.getText());
        }

        cb_sp_order_invoice.setOnCheckedChangeListener(this);

        getDefaultAddress(0);
    }

    private long calcMoney() {
        long temp = 0;
        for (int i = 0; i < list.size(); i++) {
            temp += Integer.valueOf(list.get(i).getMoney()) * Integer.valueOf(list.get(i).getNumber());
        }
        return temp;
    }

    private int calcNumber() {
        int temp = 0;
        for (int i = 0; i < list.size(); i++) {
            temp += Integer.valueOf(list.get(i).getNumber());
        }
        return temp;
    }

    /**
     * 获取物品集合的id
     *
     * @return
     */
    private int[] getIds() {
        if (null == list || 0 == list.size())
            return null;
        int[] ids = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ids[i] = list.get(i).getId();
        }
        return ids;
    }

    @OnClick({R.id.left_action, R.id.rl_sp_order, R.id.bt_sp_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_action:
                finish();
                break;
            case R.id.rl_sp_order: // 地址
                Intent address = new Intent(baseContext, MyReceiveAddressActivity.class);
                startActivity(address);
                break;
            case R.id.bt_sp_pay: // 确认下单
                if (-1 == receiverId) {
                    showToast("当前还没有填写收货地址");
                    return;
                }
                createOrder(getIds());
                break;
        }
    }

    private void createOrder(int[] ids) {
        if (null == ids) {
            showToast("没有选择购买的物品");
            return;
        } else if (cb_sp_order_invoice.isChecked() && StringUtil.isEmpty(et_sp_order_invoice.getText().toString())) {
            showToast("请填写发票信息");
            return;
        }
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_ORDER_CREATE + NetInterface.CREATE + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("receiverId", receiverId);
        param.put("isInvoice", cb_sp_order_invoice.isChecked());
        if (cb_sp_order_invoice.isChecked()) {
            param.put("invoiceTitle", et_sp_order_invoice.getText().toString());
        }
        if (isNowBuy) { // 是否为立即购买
            param.put("productId", list.get(0).getId());
            param.put("quantity", list.get(0).getNumber());
        } else {
            param.put("itemIds", ids);
        }
        param.put(Constant.PARAMETER_TAG, NetInterface.CREATE);
        netWorkHelper.PostJson(url, param, this);
    }

    private void getDefaultAddress(int type) {
        if (0 == type)
            ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_SHOP_ADD + NetInterface.GET_DEFAULT_ADDRESS + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put(Constant.PARAMETER_TAG, NetInterface.GET_DEFAULT_ADDRESS);
        netWorkHelper.PostJson(url, param, this);
    }


    @Override
    public void receive(String TAG, String data) {

        switch (TAG) {
            case NetInterface.GET_DEFAULT_ADDRESS: // 获取默认收货地址
                DefaultAddressResponse addressResponse = (DefaultAddressResponse) GsonUtil.getInstance().convertJsonStringToObject(data, DefaultAddressResponse.class);
                if (!addressResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    ProgrosDialog.closeProgrosDialog();
                    showToast(addressResponse.getDesc());
                    return;
                }

                if (null != addressResponse.getMsg()) { // 有地址
                    tv_sp_order_consignee_title.setText(R.string.get_goods_user);
                    tv_sp_order_consignee.setText(addressResponse.getMsg().getConsignee());
                    tv_sp_order_consignee_address.setText(addressResponse.getMsg().getAreaName() + addressResponse.getMsg().getAddress());
                    tv_sp_order_consignee_phone.setText(addressResponse.getMsg().getPhone());
                    receiverId = addressResponse.getMsg().getId();
                } else { // 也有可能是删除了,也有可能是没有填写
                    tv_sp_order_consignee_title.setText(null);
                    tv_sp_order_consignee.setText(null);
                    tv_sp_order_consignee_address.setText(null);
                    tv_sp_order_consignee_phone.setText(null);
                    showAddAddressDialog(getString(R.string.no_rece_add), getString(R.string.confirm));
                }

                loginResponse.setToken(addressResponse.getToken());
                break;
            case NetInterface.CREATE: // 创建订单
                BaseResponse createResponse = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
                if (!createResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    ProgrosDialog.closeProgrosDialog();
                    showToast(createResponse.getDesc());
                    return;
                }

                loginResponse.setToken(createResponse.getToken());
                // TODO 跳转至支付界面.并结束当前界面

                Intent pay = new Intent(baseContext, ShopPayActivity.class);
                pay.putExtra("orderId", createResponse.getDesc());
                pay.putExtra("money", tv_sp_money.getText());
                startActivity(pay);
                finish();

                break;
        }

        ProgrosDialog.closeProgrosDialog();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            tv_sp_order_invoice_notice.setVisibility(View.VISIBLE);
            ll_sp_order_invoice.setVisibility(View.VISIBLE);
        } else {
            tv_sp_order_invoice_notice.setVisibility(View.GONE);
            ll_sp_order_invoice.setVisibility(View.GONE);
        }
    }

    public void showAddAddressDialog(String msg, String buttonMsg) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage(msg);
        builder.setTitle(getString(R.string.remind));

        builder.setPositiveButton(buttonMsg,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent address = new Intent(baseContext, MyReceiveAddressActivity.class);
                        startActivity(address);
                    }
                });
        builder.setNegativeButton(getString(R.string.cancel),
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }
}
