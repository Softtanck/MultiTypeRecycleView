package com.cheweishi.android.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.ShopOrderFragmentPagerAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanck on 10/10/2016.
 * <p/>
 * Describe: 商品订单
 */
public class MyShopOrderActivity extends BaseActivity {

    @ViewInject(R.id.left_action)
    private Button left_action;

    @ViewInject(R.id.title)
    private TextView title;

    @ViewInject(R.id.tl_shop_order)
    private TabLayout tl_shop_order;//标题

    @ViewInject(R.id.vp_shop_order)
    private ViewPager vp_shop_order;//滚动内容

    private ShopOrderFragmentPagerAdapter adapter;//标题适配器

    private List<String> titles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop_order);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        left_action.setText(R.string.back);
        title.setText(R.string.my_order);
        titles.add("全部");
        titles.add("待付款");
        titles.add("待发货");
        titles.add("待收货");
        titles.add("待评价");
        adapter = new ShopOrderFragmentPagerAdapter(getSupportFragmentManager(), baseContext, titles);
        vp_shop_order.setAdapter(adapter);
        tl_shop_order.setupWithViewPager(vp_shop_order);
        int currentItem = getIntent().getIntExtra("page", 0);
        vp_shop_order.setCurrentItem(currentItem);
    }


    @OnClick({R.id.left_action})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_action:
                finish();
                break;
        }
    }
}
