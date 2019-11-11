package com.cheweishi.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.MainGridViewAdapter;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.LoginResponse;
import com.cheweishi.android.entity.MainGridInfoNative;
import com.cheweishi.android.utils.ButtonUtils;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.widget.UnslidingGridView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tangce on 7/25/2016.
 */
public class HomeMoreActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private String[] name = {"买车险", "洗车", "紧急救援", "保养", "找加油站", "美容",
            "车辆数据", "违章查询", "找车位", "资讯"};
    private int[] icon = {R.drawable.xian, R.drawable.xiche,
            R.drawable.jinjijiuyuan, R.drawable.baoyang, R.drawable.jiayouzhan,
            R.drawable.meirong, R.drawable.dongtai, R.drawable.weizhang,
            R.drawable.chewei, R.drawable.home_more};

    // 列表
    @ViewInject(R.id.gv_more_service)
    private UnslidingGridView gv_more_service;

    //左边按钮
    @ViewInject(R.id.left_action)
    private Button left_action;

    //标题
    @ViewInject(R.id.title)
    private TextView title;
    //列表适配器
    private MainGridViewAdapter gridViewAdapter;
    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_more);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        title.setText(getResources().getString(R.string.more_car));
        left_action.setText(getResources().getString(R.string.back));
        List<MainGridInfoNative> gridInfos = new ArrayList<MainGridInfoNative>();
        for (int i = 0; i < 10; i++) {
            MainGridInfoNative gridInfo = new MainGridInfoNative();
            gridInfo.setName(name[i]);
            gridInfo.setImgId(icon[i]);
//            gridInfo.setImgUrl("asdasdas");// TODO:暂无可配图片地址
            gridInfos.add(gridInfo);
        }
        gridViewAdapter = new MainGridViewAdapter(baseContext, gridInfos, false);
        gv_more_service.setAdapter(gridViewAdapter);
        gv_more_service.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /**
         * 快速点击忽略处理
         */
        if (ButtonUtils.isFastClick()) {
            return;
        }
        switch (position) {
            case 0:// 买车险
                Intent intent = new Intent(baseContext, WebActivity.class);
                intent.putExtra("url", NetInterface.INSURANCE);
                startActivity(intent);
                break;
            case 1:// 洗车
                isLogin(WashcarListActivity.class);
                break;
            case 2:// 紧急救援
                Intent sos = new Intent(baseContext, SoSActivity.class);
                startActivity(sos);
                break;
            case 3:// 保养
                isLogin(MaintainListActivity_new.class);
                break;
            case 4:// 找加油站
                isLogin(GasStationActivity.class);
                break;
            case 5:// 美容
                isLogin(BeautyListActivity_new.class);
                break;
            case 6:// 车辆数据
                updateCache("CAR_DETECTION");
                break;
            case 7:// 违章代办
                updateCache("PESSANY");
                break;
            case 8:// 找车位
                isLogin(FindParkingSpaceActivity.class);
                break;
            case 9:// 资讯
                Intent home = new Intent(baseContext, MainNewActivity.class);
                home.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                home.putExtra(getResources().getString(R.string.newnotify), true);
                startActivity(home);
                finish();
                break;
        }
    }

    @Override
    public void error(String errorMsg) {
        ProgrosDialog.closeProgrosDialog();
        showToast(R.string.server_link_fault);
    }

    @Override
    public void receive(String TAG, String data) {
        ProgrosDialog.closeProgrosDialog();
        switch (TAG) {
            case "SOS": // 紧急救援
                ProgrosDialog.closeProgrosDialog();
                LoginResponse sos = (LoginResponse) GsonUtil.getInstance().convertJsonStringToObject(data, LoginResponse.class);
                loginResponse = sos;
                BaseActivity.loginResponse = loginResponse;
//                LoginMessageUtils.saveloginmsg(baseContext, sos);
                isLoginOrHasCar(SoSActivity.class);
                break;
            case "CAR_DYNAMIC":// 车辆动态
                ProgrosDialog.closeProgrosDialog();
                LoginResponse carDynamic = (LoginResponse) GsonUtil.getInstance().convertJsonStringToObject(data, LoginResponse.class);
                loginResponse = carDynamic;
                BaseActivity.loginResponse = loginResponse;
//                LoginMessageUtils.saveloginmsg(baseContext, carDynamic);
                isLoginOrHasCar(CarDynamicActivity.class);
                break;
            case "CAR_DETECTION":// 一键检测
                ProgrosDialog.closeProgrosDialog();
                LoginResponse carDetection = (LoginResponse) GsonUtil.getInstance().convertJsonStringToObject(data, LoginResponse.class);
                loginResponse = carDetection;
                BaseActivity.loginResponse = loginResponse;
//                LoginMessageUtils.saveloginmsg(baseContext, carDetection);
                isLoginOrHasCar(CarDetectionActivity.class);
                break;
            case "PESSANY":// 违章查询
                ProgrosDialog.closeProgrosDialog();
                LoginResponse pessany = (LoginResponse) GsonUtil.getInstance().convertJsonStringToObject(data, LoginResponse.class);
                loginResponse = pessany;
                BaseActivity.loginResponse = loginResponse;
//                LoginMessageUtils.saveloginmsg(baseContext, pessany);
                isLoginOrHasCar_New(PessanySearchActivity.class);
                break;
        }
    }

    /**
     * 对是否登陆和是否有车处理
     *
     * @param cls
     */
    private void isLoginOrHasCar(Class<?> cls) {
        if (!isLogined()) {
            intent.setClass(baseContext, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.score_business_query_enter, R.anim.score_business_query_exit);
        } else if (!hasCar()) {
            showCustomDialog("你还没有添加车辆", "添加车辆", 0);
            return;
        } else if (!hasDevice()) {
            showCustomDialog(getString(R.string.home_no_device), "前往绑定", 1);
            return;
        } else {
            intent.setClass(baseContext, cls);
            startActivity(intent);
        }
    }

    /**
     * 是否有车新判断
     *
     * @param cls
     */
    private void isLoginOrHasCar_New(Class<?> cls) {
        if (!isLogined()) {
            intent.setClass(baseContext, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.score_business_query_enter,
                    R.anim.score_business_query_exit);
        } else if (!hasCar()) {
            showCustomDialog("你还没有添加车辆", "添加车辆", 0);
            return;
        } else {
            intent.setClass(baseContext, cls);
            startActivity(intent);
        }
    }

    /**
     * 登陆才能跳转
     *
     * @param cls
     */
    private void isLogin(Class<?> cls) {
        if (!isLogined()) {
            intent.setClass(baseContext, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.score_business_query_enter, R.anim.score_business_query_exit);
        } else {
            intent.setClass(baseContext, cls);
            startActivity(intent);
        }
    }

    private void updateCache(String tag) {
        if (isLogined()) {
            ProgrosDialog.openDialog(baseContext);
            String url = NetInterface.HEADER_ALL + NetInterface.UPDATE_CACHE + NetInterface.SUFFIX;
            Map<String, Object> param = new HashMap<>();
            param.put("userId", loginResponse.getDesc());
            param.put("token", loginResponse.getToken());
            param.put(Constant.PARAMETER_TAG, tag);
            netWorkHelper.PostJson(url, param, this);
        }
    }

    @OnClick(R.id.left_action)
    @Override
    public void onClick(View v) {
        finish();
    }
}
