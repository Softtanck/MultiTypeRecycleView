package com.cheweishi.android.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.fragement.GasStationListFragment;
import com.cheweishi.android.fragement.GasStationMapFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/****
 * 加油站
 *
 * @author lw
 */
@ContentView(R.layout.activity_gas_station)
public class GasStationActivity extends BaseActivity {
    // 定义fragment管理器
    private FragmentManager manager;
    // 定义一个FragmentTransaction
    private FragmentTransaction transaction;
    // 通过注解得到radioGroup控件
    @ViewInject(R.id.listToMap)
    private LinearLayout radioGroup;
    // 通过注解得到mRadioButtonList控件
    @ViewInject(R.id.rb_list)
    private TextView mRadioButtonList;
    // 通过注解得到mRadioButtonMap控件
    @ViewInject(R.id.rb_map)
    private TextView mRadioButtonMap;
    // 通过注解得到mBack控件
    @ViewInject(R.id.left_action)
    private TextView mBack;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.right_action)
    private ImageView right_action;

    @ViewInject(R.id.ll_map)
    private LinearLayout ll_map;//地图

    @ViewInject(R.id.ll_list)
    private LinearLayout ll_list;//列表
    // 定义列表的fragment
    private GasStationListFragment gasStationListFragment;
    // 定义地图的fragment
    private GasStationMapFragment gasStationMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
        ViewUtils.inject(this);

        //
        init();
    }

    /****
     * 初始化方法
     */
    private void init() {
        // mRadioButtonMap.setChecked(true);
        right_action.setVisibility(View.INVISIBLE);
        initFragment();
//		Log.i("result", "============" + mBack + "==========" + getString(R.string.back));
        mBack.setText(R.string.back);
        title.setText(R.string.mycar_find_gasstation);
        manager = getSupportFragmentManager();

        initTransaction();

        isAdded(gasStationMapFragment);
        showCommit(gasStationMapFragment);
        // 地图和列表切换位置时的监听
//		radioGroup.setOnClickListener((onCheckedChangeListener);
    }

    /**
     * 实例化transaction
     */
    private void initTransaction() {
        transaction = manager.beginTransaction();
    }

//	/**
//	 * 切换地图或列表时的事件监听
//	 */
//	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
//
//		@Override
//		public void onCheckedChanged(RadioGroup arg0, int arg1) {
//
//			switch (arg1) {
//			case R.id.rb_list:
//				changeFragment(gasStationListFragment);
//				break;
//			case R.id.rb_map:
//				changeFragment(gasStationMapFragment);
//				break;
//			default:
//				break;
//			}
//		}
//	};

    /**
     * 显示fragment并提交事务
     *
     * @param fragment 需要显示的fragment
     */
    private void showCommit(Fragment fragment) {
        // TODO Auto-generated method stub
        transaction.show(fragment);
        transaction.commit();

    }

    /**
     * 判断fragment是否存在
     *
     * @param fragment 判断的fragment
     */
    private void isAdded(Fragment fragment) {
        // TODO Auto-generated method stub
        if (!fragment.isAdded()) {
            transaction.add(R.id.gasstation_framelayout, fragment);
        }
    }

    /*
     * 实例化fragment
     */
    private void initFragment() {
        // TODO Auto-generated method stub
        gasStationMapFragment = new GasStationMapFragment();
        gasStationListFragment = new GasStationListFragment();
    }

    /**
     * 改变fragment的显示
     *
     * @param fragment 需要显示的fragment
     */
    protected void changeFragment(Fragment fragment) {
        // TODO Auto-generated method stub
        initTransaction();
        isAdded(fragment);
        if (gasStationMapFragment == fragment) {
            showHideCommit(fragment, gasStationListFragment);
        } else if (gasStationListFragment == fragment) {
            showHideCommit(fragment, gasStationMapFragment);
        }
    }

    /***
     * 显示和隐藏fragment
     *
     * @param fragment  显示的fragment
     * @param fragment2 隐藏的fragment
     */
    private void showHideCommit(Fragment fragment, Fragment fragment2) {
        // TODO Auto-generated method stub
        transaction.show(fragment);
        transaction.hide(fragment2);
        transaction.commitAllowingStateLoss();
    }

    @OnClick({R.id.left_action, R.id.ll_map, R.id.ll_list})
    public void btnClick(View v) {
        switch (v.getId()) {
            case R.id.left_action:
                finish();
                break;
            case R.id.ll_map:
                showBtnStatuc(0);
                changeFragment(gasStationMapFragment);
                break;
            case R.id.ll_list:
                showBtnStatuc(1);
                changeFragment(gasStationListFragment);
                break;
        }

    }

    private void showBtnStatuc(int i) {
        if (i == 0) {
            ll_map.setBackgroundResource(R.drawable.chewei_bj2);
            Drawable img_on = getResources().getDrawable(R.drawable.chewei_map_click);
            img_on.setBounds(0, 0, img_on.getMinimumWidth(), img_on.getMinimumHeight());
            mRadioButtonMap.setCompoundDrawables(img_on, null, null, null); //设置左图标
            mRadioButtonMap.setTextColor(getResources().getColor(R.color.orange));

            ll_list.setBackgroundResource(R.drawable.chewei_bj1);
            Drawable img_off = getResources().getDrawable(R.drawable.chewei_list);
            img_off.setBounds(0, 0, img_off.getMinimumWidth(), img_off.getMinimumHeight());
            mRadioButtonList.setCompoundDrawables(img_off, null, null, null); //设置左图标
            mRadioButtonList.setTextColor(getResources().getColor(R.color.text_black_colcor));
        } else if (i == 1) {
            ll_list.setBackgroundResource(R.drawable.chewei_bj2);
            Drawable img_on = getResources().getDrawable(R.drawable.chewei_list_click);
            img_on.setBounds(0, 0, img_on.getMinimumWidth(), img_on.getMinimumHeight());
            mRadioButtonList.setCompoundDrawables(img_on, null, null, null); //设置左图标
            mRadioButtonList.setTextColor(getResources().getColor(R.color.orange));

            ll_map.setBackgroundResource(R.drawable.chewei_bj1);
            Drawable img_off = getResources().getDrawable(R.drawable.chewei_map);
            img_off.setBounds(0, 0, img_off.getMinimumWidth(), img_off.getMinimumHeight());
            mRadioButtonMap.setCompoundDrawables(img_off, null, null, null); //设置左图标
            mRadioButtonMap.setTextColor(getResources().getColor(R.color.text_black_colcor));

        }
    }

    @Override
    protected void onDestroy() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                "isgasstationDraw", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences2 = getSharedPreferences(
                "isgasstationdraw", Context.MODE_PRIVATE);

        SharedPreferences sharedPreferencesgasstation = getSharedPreferences(
                "isindexgass", Context.MODE_PRIVATE);

        sharedPreferencesgasstation.edit().clear().commit();
        sharedPreferences.edit().clear().commit();
        sharedPreferences2.edit().clear().commit();
        super.onDestroy();
    }
}
