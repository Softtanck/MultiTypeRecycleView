package com.cheweishi.android.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.ProductDetailResponse;
import com.cheweishi.android.entity.ShopPayOrderNative;
import com.cheweishi.android.interfaces.ScrollViewListener;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.thirdpart.adapter.CBPageAdapter;
import com.cheweishi.android.thirdpart.holder.CBViewHolderCreator;
import com.cheweishi.android.thirdpart.holder.ProductDetailImgHolder;
import com.cheweishi.android.thirdpart.view.CBLoopViewPager;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.MyScrollView;
import com.cheweishi.android.widget.XCRoundImageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nineoldandroids.view.ViewHelper;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tangce on 8/12/2016.
 */
public class ProductDetailActivity extends BaseActivity implements ScrollViewListener, ViewPager.OnPageChangeListener, TextWatcher {

    private static final String MORE_99 = "99+";

    private static final String DEFAULT_NUMBER = "0";

    @ViewInject(R.id.left_action)
    private Button left_action; // 左标题

    @ViewInject(R.id.title)
    private TextView title; // 标题

    @ViewInject(R.id.ll_common_title_right)
    private LinearLayout right_action; // 右边

    @ViewInject(R.id.tv_common_title_number)
    public static TextView tv_common_title_number; // 购物车数量

    @ViewInject(R.id.tv_product_cart_number)
    public static TextView tv_product_cart_number;//购物车数量

    @ViewInject(R.id.sv_product_detail)
    private MyScrollView sv_product_detail;

    @ViewInject(R.id.ll_shop_detail_title)
    private LinearLayout ll_common_title;

    @ViewInject(R.id.ll_product_detail_title)
    private LinearLayout ll_product_detail_title; // 黑色返回

    @ViewInject(R.id.iv_product_detail_point)
    private ImageView iv_product_detail_point;// 图片指示灯

    @ViewInject(R.id.vp_product_detail)
    private CBLoopViewPager vp_product_detail;//滑动图片

    @ViewInject(R.id.ll_shop_buy)
    private FrameLayout ll_shop_buy; // 滑动出来的购物车

    @ViewInject(R.id.ll_product_img_txt_detail)
    private LinearLayout ll_product_img_txt_detail;//图文详情

    @ViewInject(R.id.ll_product_detail_param)
    private LinearLayout ll_product_detail_param; // 产品参数

    @ViewInject(R.id.tv_product_detail_right_more)
    private TextView tv_product_detail_right_more;//更多或暂无评价

    @ViewInject(R.id.et_product_detail_num)
    private EditText et_product_detail_num;//购买数量


    @ViewInject(R.id.iv_product_detail_num_add)
    private ImageView num_add;//增加

    @ViewInject(R.id.iv_product_detail_num_les)
    private ImageView num_les;//减少

    @ViewInject(R.id.tv_product_detail_full_name)
    private TextView tv_product_detail_full_name;//商品名字

    @ViewInject(R.id.tv_product_detail_buy_number)
    private TextView tv_product_detail_buy_number; // 当前购买人数

    @ViewInject(R.id.tv_product_detail_user_comment)
    private TextView tv_product_detail_user_comment;// 用户评论人数

    @ViewInject(R.id.xiv_product_common_user)
    private XCRoundImageView xiv_product_common_user;//评论人的头像

    @ViewInject(R.id.tv_product_common_user_name)
    private TextView tv_product_common_user_name; // 评论人的名字

    @ViewInject(R.id.tv_product_common_time)
    private TextView tv_product_common_time;// 评论人的评论时间

    @ViewInject(R.id.tv_product_common_content)
    private TextView tv_product_common_content;// 评论的内容

    @ViewInject(R.id.rl_product_detail_more)
    private RelativeLayout rl_product_detail_more;//评论视图

    @ViewInject(R.id.tv_shop_item_money)
    private TextView tv_shop_item_money; // 购买价格

    @ViewInject(R.id.tv_product_detail_biz)
    private TextView tv_product_detail_biz;//商家回复

    private int headerHeight; // 顶部标题的高度

    private List<ProductDetailResponse.MsgBean.ProductImagesBean> list = new ArrayList<>();// 图片url

    private CBPageAdapter adapter; // 图片适配器

    private int mBuynumber = 1; // 购买数量

    private int mCurrentProDuctId;//当前商品id

    private ProductDetailResponse detailResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        right_action.setVisibility(View.VISIBLE);
        ll_common_title.setVisibility(View.GONE);
        title.setText(getString(R.string.shop_detail));
        left_action.setText(getString(R.string.back));
        headerHeight = getHeadHeight(ll_common_title);
        et_product_detail_num.addTextChangedListener(this);
        sv_product_detail.setScrollViewListener(this);
        vp_product_detail.setOnPageChangeListener(this);
        mCurrentProDuctId = getIntent().getIntExtra("productId", -1);
//        adapter = new CBPageAdapter(new MyHolder(), list, this);
//        vp_product_detail.setAdapter(adapter, true);
        getProductInfoPacket(mCurrentProDuctId);
    }

    @Override
    public void receive(String TAG, String data) {

        switch (TAG) {
            case NetInterface.GET_PRODUCT_INFO: // 获取商品详情
                detailResponse = (ProductDetailResponse) GsonUtil.getInstance().convertJsonStringToObject(data, ProductDetailResponse.class);
                if (!detailResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    ProgrosDialog.closeProgrosDialog();
                    showToast(detailResponse.getDesc());
                    return;
                }


                // 设置广告
                List<ProductDetailResponse.MsgBean.ProductImagesBean> tempImg = detailResponse.getMsg().getProductImages();

                if (null != tempImg && 0 < tempImg.size()) {
                    list = tempImg;
                    adapter = new CBPageAdapter(new MyHolder(), list, this);
                    vp_product_detail.setAdapter(adapter, true);
//                    adapter.setData(list);
                    drawPoint(0);
                }


                tv_product_detail_full_name.setText(detailResponse.getMsg().getFullName());
                tv_product_detail_buy_number.setText(detailResponse.getMsg().getSales() + "个人购买");
                tv_product_detail_user_comment.setText("用户评价(" + detailResponse.getMsg().getReviewCount() + ")");

                if (0 < Integer.valueOf(detailResponse.getMsg().getReviewCount())) { // 评论输了大于0的情况
                    rl_product_detail_more.setVisibility(View.VISIBLE);
                    tv_product_detail_right_more.setText(getString(R.string.get_comment_more));
                    XUtilsImageLoader.getxUtilsImageLoader(baseContext, R.drawable.info_touxiang_moren, xiv_product_common_user, detailResponse.getMsg().getReviews().get(0).getMember().getPhoto());
                    tv_product_common_user_name.setText(detailResponse.getMsg().getReviews().get(0).getMember().getUserName());
                    tv_product_common_time.setText(transferLongToDate(detailResponse.getMsg().getReviews().get(0).getCreateDate()));
                    tv_product_common_content.setText(detailResponse.getMsg().getReviews().get(0).getContent());

                    if (!StringUtil.isEmpty(detailResponse.getMsg().getReviews().get(0).getBizReply())) {
                        tv_product_detail_biz.setVisibility(View.VISIBLE);
                        tv_product_detail_biz.setText(getString(R.string.bizRely) + detailResponse.getMsg().getReviews().get(0).getBizReply());
                    }
                }

                tv_shop_item_money.setText(detailResponse.getMsg().getPrice());


                UpdateBuyCartNumber(0, Integer.valueOf(detailResponse.getMsg().getCartProductCount()));

                loginResponse.setToken(detailResponse.getToken());
                break;
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

                loginResponse.setToken(add.getToken());
                break;
        }


        ProgrosDialog.closeProgrosDialog();
    }

    @OnClick({R.id.left_action, R.id.ll_product_img_txt_detail, R.id.ll_product_detail_param, R.id.rl_product_detail_more
            , R.id.tv_product_detail_right_more, R.id.iv_product_detail_num_les, R.id.iv_product_detail_num_add,
            R.id.bt_product_detail_addCart, R.id.ll_shop_buy, R.id.ll_common_title_right, R.id.ll_product_detail_title,
            R.id.bt_product_detail_create})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_product_detail_title:
            case R.id.left_action:
                finish();
                break;
            case R.id.ll_product_img_txt_detail: // 图文详情
                Intent intent = new Intent(baseContext, ProductParamDetailActivity.class);
                intent.putExtra("data", detailResponse);
                intent.putExtra("mBuynumber", mBuynumber);
                intent.putExtra("cartNumber", tv_product_cart_number.getText().toString());
                startActivity(intent);
                break;
            case R.id.ll_product_detail_param: // 产品参数
                Intent detail = new Intent(baseContext, ProductParamDetailActivity.class);
                detail.putExtra("data", detailResponse);
                detail.putExtra("currentP", 1);
                detail.putExtra("mBuynumber", mBuynumber);
                detail.putExtra("cartNumber", tv_product_cart_number.getText().toString());
                startActivity(detail);
                break;
            case R.id.tv_product_detail_right_more://评价
            case R.id.rl_product_detail_more: // 更多评论
                if (0 >= Integer.valueOf(detailResponse.getMsg().getReviewCount()))  // 评论输了大于0的情况
                    return;
                Intent common = new Intent(baseContext, ProductParamDetailActivity.class);
                common.putExtra("currentP", 2);
                common.putExtra("mBuynumber", mBuynumber);
                common.putExtra("cartNumber", tv_product_cart_number.getText().toString());
                startActivity(common);
                break;
            case R.id.iv_product_detail_num_les: // 减少
                --mBuynumber;
                et_product_detail_num.setText(String.valueOf(0 >= mBuynumber ? 1 : mBuynumber));
                break;
            case R.id.iv_product_detail_num_add: // 增加
                ++mBuynumber;
                et_product_detail_num.setText(String.valueOf(99 < mBuynumber ? 99 : mBuynumber));
                break;
            case R.id.bt_product_detail_addCart: // 加入购物车
                addProductToBuyCartNowPacket(mCurrentProDuctId, mBuynumber);
                break;
            case R.id.ll_common_title_right://顶部购物车
            case R.id.ll_shop_buy:
                Intent buyCart = new Intent(baseContext, BuyCartActivity.class);
//                buyCart.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(buyCart);
                break;
            case R.id.bt_product_detail_create://立即购买
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
        long tempMoney = Integer.valueOf(detailResponse.getMsg().getPrice()) * Integer.valueOf(et_product_detail_num.getText().toString());
        ShopPayOrderNative shopPayOrderNative = new ShopPayOrderNative();
        shopPayOrderNative.setIcon(detailResponse.getMsg().getImage());
        shopPayOrderNative.setMoney(String.valueOf(tempMoney));
        shopPayOrderNative.setName(detailResponse.getMsg().getFullName());
        shopPayOrderNative.setNumber(et_product_detail_num.getText().toString());
        shopPayOrderNative.setId(detailResponse.getMsg().getId());
        temp.add(shopPayOrderNative);
//        }
        return temp;
    }

    /**
     * 更新购物车
     *
     * @param type   1:增加的数量  0:直接显示数量
     * @param number 数量
     */
    public static void UpdateBuyCartNumber(int type, int number) {
        if (null == tv_common_title_number || null == tv_product_cart_number)
            return;
        if (StringUtil.isEmpty(number))
            return;
        else if (0 == type && 0 == number) {
            tv_product_cart_number.setVisibility(View.GONE);
            tv_common_title_number.setVisibility(View.GONE);
            tv_product_cart_number.setText(DEFAULT_NUMBER);
            tv_common_title_number.setText(DEFAULT_NUMBER);
            return;
        }

        if (1 == type) { // 非直接显示
            try {
                if (!StringUtil.isEmpty(tv_product_cart_number.getText().toString()))
                    number += Integer.valueOf(tv_product_cart_number.getText().toString());
            } catch (Exception e) {
                number = 100;//标识超过99 flag
            }
        }
        tv_product_cart_number.setVisibility(View.VISIBLE);
        tv_common_title_number.setVisibility(View.VISIBLE);
        if (99 < number) {
            tv_product_cart_number.setText(MORE_99);
            tv_common_title_number.setText(MORE_99);
        } else {
            tv_product_cart_number.setText(String.valueOf(number));
            tv_common_title_number.setText(String.valueOf(number));
        }
    }

    /**
     * 把毫秒转化成日期
     *
     * @param millSec(毫秒数)
     * @return
     */
    private String transferLongToDate(Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(millSec);
        return sdf.format(date);
    }


    @Override
    public void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx, int oldy) {
        if (0 != y) { // 向上滚动
            ll_common_title.setVisibility(View.VISIBLE);
            float alpha = y * 1.0f / headerHeight;
            if (1.0f < alpha)
                alpha = 1.0f;
            else if (0.0f > alpha)
                alpha = 0.0f;
            else if (0.0f < alpha && 1.0f > alpha) {
                ll_product_detail_title.setVisibility(View.GONE);
                ll_shop_buy.setVisibility(View.GONE);
            }
            ViewHelper.setAlpha(ll_common_title, alpha);
        } else if (0 == y) {
            ll_common_title.setVisibility(View.GONE);
            ll_product_detail_title.setVisibility(View.VISIBLE);
            ll_shop_buy.setVisibility(View.VISIBLE);
        }

    }

    private void drawPoint(int position) {
        if (null == list || 0 == list.size())
            return;
        int radius = 10; // 半径
        int spacing = 50; // 点之间间隔
        Bitmap points = Bitmap.createBitmap(radius * 2 + spacing * (list.size() - 1), radius * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(points);
        Paint paint = new Paint();
        paint.setAntiAlias(true); // 设置画笔为无锯齿
        paint.setStyle(Paint.Style.FILL); // 实心
        for (int i = 0; i < list.size(); i++) {
            paint.setColor(Color.GRAY);
            if (position == i) // 设置选中项为白色
                paint.setColor(getResources().getColor(R.color.deep_orange));
            canvas.drawCircle(radius + spacing * i, radius, radius, paint);
        }
        iv_product_detail_point.setImageBitmap(points);
    }


    private int getHeadHeight(LinearLayout view) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (0 >= s.length()) {
            mBuynumber = 1;
            et_product_detail_num.setText(String.valueOf(mBuynumber));
        }
        mBuynumber = StringUtil.isEmpty(s.toString()) ? 1 : Integer.valueOf(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {
    }


    private class MyHolder implements CBViewHolderCreator<ProductDetailImgHolder> {

        @Override
        public ProductDetailImgHolder createHolder() {
            return new ProductDetailImgHolder();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        drawPoint(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /**
     * 根据商品id获取商品详细
     *
     * @param id 商品id
     */
    private void getProductInfoPacket(int id) {
        if (-1 == id) {
            showToast(getString(R.string.get_product_info_error));
            return;
        }

        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_SHOP + NetInterface.GET_PRODUCT_INFO + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("productId", id);
        param.put(Constant.PARAMETER_TAG, NetInterface.GET_PRODUCT_INFO);
        netWorkHelper.PostJson(url, param, this);
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


}
