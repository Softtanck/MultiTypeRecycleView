package com.cheweishi.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.ProductParamFragmentPagerAdapter;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.ProductDetailResponse;
import com.cheweishi.android.entity.ShopPayOrderNative;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.StringUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tangce on 8/22/2016.
 */
public class ProductParamDetailActivity extends BaseActivity {

    @ViewInject(R.id.left_action)
    private Button left_action;

    @ViewInject(R.id.title)
    private TextView title;


    @ViewInject(R.id.ll_common_title_right)
    private LinearLayout right_action; // 右边

    @ViewInject(R.id.tv_common_title_number)
    public static TextView tv_common_title_number; // 购物车数量

    @ViewInject(R.id.vp_param_shop)
    private ViewPager vp_param_shop; // 滚动视图

    @ViewInject(R.id.tl_param_shop)
    private TabLayout tl_param_shop; // 滚动顶部标题

    private ProductParamFragmentPagerAdapter adapter;//整个适配器

    private List<String> list = new ArrayList<>();//标题数据
    private int mCurrentProDuctId = -1;

    private int mBuynumber;//购买数量

    private static final String DEFAULT_NUMBER = "0";

    private static final String MORE_99 = "99+";

    private ProductDetailResponse detailResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_param);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        right_action.setVisibility(View.VISIBLE);
        left_action.setText(getString(R.string.back));
        title.setText(getString(R.string.detail));
        list.add("图文详情");
        list.add("产品参数");
        list.add("用户评价");
        adapter = new ProductParamFragmentPagerAdapter(getSupportFragmentManager(), list, ((ProductDetailResponse) getIntent().getSerializableExtra("data")));
        vp_param_shop.setAdapter(adapter);
        tl_param_shop.setupWithViewPager(vp_param_shop);
        vp_param_shop.setOffscreenPageLimit(3);
        vp_param_shop.setCurrentItem(getIntent().getIntExtra("currentP", 0));
        detailResponse = (ProductDetailResponse) getIntent().getSerializableExtra("data");
        if (null == detailResponse) {
            showToast(R.string.init_fail);
            return;
        }
        mCurrentProDuctId = detailResponse.getMsg().getId();
        mBuynumber = getIntent().getIntExtra("mBuynumber", 1);


        String temp = getIntent().getStringExtra("cartNumber");
        if (!StringUtil.isEmpty(temp) && !"0".equals(temp))
            tv_common_title_number.setVisibility(View.VISIBLE);
        tv_common_title_number.setText(temp);
    }

    @OnClick({R.id.left_action, R.id.bt_param_bottom_add, R.id.ll_common_title_right, R.id.bt_param_bottom_buy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_action:
                finish();
                break;
            case R.id.bt_param_bottom_add: // 加入购物车
                addProductToBuyCartNowPacket(mCurrentProDuctId, mBuynumber);
                break;
            case R.id.ll_common_title_right://购物车
                Intent buyCart = new Intent(baseContext, BuyCartActivity.class);
                startActivity(buyCart);
                break;
            case R.id.bt_param_bottom_buy://立即购买
                nowBuy();
                break;
        }

    }

    /**
     * 立即购买
     */
    private void nowBuy() {
        Intent intent = new Intent(baseContext, ShopPayOrderActivity.class);
        intent.putExtra("data", (Serializable) getPayList());
        intent.putExtra("isNowBuy", true);
        startActivity(intent);
    }


    /**
     * 获取立即购买的信息
     *
     * @return
     */
    private List<ShopPayOrderNative> getPayList() {
        if (null == detailResponse || null == detailResponse.getMsg())
            return null;
        List<ShopPayOrderNative> temp = new ArrayList<>();
//        if (null != detailResponse && null != detailResponse.getMsg()) {
        long tempMoney = Integer.valueOf(detailResponse.getMsg().getPrice()) * Integer.valueOf(tv_common_title_number.getText().toString());
        ShopPayOrderNative shopPayOrderNative = new ShopPayOrderNative();
        shopPayOrderNative.setIcon(detailResponse.getMsg().getImage());
        shopPayOrderNative.setMoney(String.valueOf(tempMoney));
        shopPayOrderNative.setName(detailResponse.getMsg().getFullName());
        shopPayOrderNative.setNumber(tv_common_title_number.getText().toString());
        shopPayOrderNative.setId(detailResponse.getMsg().getId());
        temp.add(shopPayOrderNative);
//        }
        return temp;
    }

    @Override
    public void receive(String TAG, String data) {
        switch (TAG) {
            case NetInterface.ADD:// 添加到购物车
                BaseResponse add = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
                if (!add.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    ProgrosDialog.closeProgrosDialog();
                    showToast(add.getDesc());
                    return;
                }

                //更新数量
                showToast(getString(R.string.add_cart_success));

                UpdateBuyCartNumber(1, mBuynumber);

                ProductDetailActivity.UpdateBuyCartNumber(1, mBuynumber);

                loginResponse.setToken(add.getToken());
                break;
        }
        ProgrosDialog.closeProgrosDialog();
    }

    /**
     * 加入购物车
     *
     * @param id     商品id
     * @param number 购买数量
     */
    private void addProductToBuyCartNowPacket(int id, int number) {
        if (-1 == id) {
            showToast(getString(R.string.get_product_info_error));
            return;
        }

        String url = NetInterface.BASE_URL + NetInterface.TEMP_SHOP_CART + NetInterface.ADD + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("productId", id);
        param.put("quantity", number);
        param.put(Constant.PARAMETER_TAG, NetInterface.ADD);
        netWorkHelper.PostJson(url, param, this);
    }


    /**
     * 更新购物车
     *
     * @param type   1:增加的数量  0:直接显示数量
     * @param number 数量
     */
    public static void UpdateBuyCartNumber(int type, int number) {
        if (null == tv_common_title_number)
            return;
        if (StringUtil.isEmpty(number))
            return;
        else if (0 == type && 0 == number) {
            tv_common_title_number.setVisibility(View.GONE);
            tv_common_title_number.setText(DEFAULT_NUMBER);
            return;
        }

        if (1 == type) { // 非直接显示
            try {
                if (!StringUtil.isEmpty(tv_common_title_number.getText().toString()))
                    number += Integer.valueOf(tv_common_title_number.getText().toString());
            } catch (Exception e) {
                number = 100;//标识超过99 flag
            }
        }
        tv_common_title_number.setVisibility(View.VISIBLE);
        if (99 < number) {
            tv_common_title_number.setText(MORE_99);
        } else {
            tv_common_title_number.setText(String.valueOf(number));
        }
    }
}
