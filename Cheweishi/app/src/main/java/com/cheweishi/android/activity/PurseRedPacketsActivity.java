package com.cheweishi.android.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.fragement.MyCarCouponFragment;
import com.cheweishi.android.fragement.MyConpouFragment;
import com.cheweishi.android.tools.EmptyTools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 我的红包
 *
 * @author XMh
 */
public class PurseRedPacketsActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.left_action)
    private Button left_action;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.ll_mycoupon_tab)
    private LinearLayout ll_mycoupon_tab; // 选项卡
    @ViewInject(R.id.btn_coupon)
    private Button btn_coupon; // 优惠券
    @ViewInject(R.id.btn_washcar_coupon)
    private Button btn_washcar_coupon;
    private MyConpouFragment myConpouFragment;
    private MyCarCouponFragment myCarCouponFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purse_integtal);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        title.setText(R.string.purse_certificates);
        left_action.setText(R.string.back);
        addFragment();
    }

    private void addFragment() {
        myConpouFragment = new MyConpouFragment();
        myCarCouponFragment = new MyCarCouponFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_coupon_content, myCarCouponFragment).add(R.id.fl_coupon_content, myConpouFragment).hide(myCarCouponFragment).show(myConpouFragment);
        transaction.commit();
    }

    private void showFragment(Fragment show, Fragment hide) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(hide).show(show);
        transaction.commit();
    }

    @OnClick({R.id.left_action, R.id.ll_mycoupon_tab, R.id.btn_coupon, R.id.btn_washcar_coupon})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_action:// 返回
                this.finish();
                break;
            case R.id.btn_coupon:
                btn_coupon.setBackgroundResource(R.drawable.baike_btn_pink_left_f_96);
                btn_washcar_coupon.setBackgroundResource(R.drawable.baike_btn_trans_right_f_96);
                showFragment(myConpouFragment, myCarCouponFragment);
                break;
            case R.id.btn_washcar_coupon:
                btn_coupon.setBackgroundResource(R.drawable.baike_btn_trans_left_f_96);
                btn_washcar_coupon.setBackgroundResource(R.drawable.baike_btn_pink_right_f_96);
                showFragment(myCarCouponFragment, myConpouFragment);
                break;
            default:
                break;
        }
    }

    public void showTab() {
        ll_mycoupon_tab.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EmptyTools.destory();
    }
}
