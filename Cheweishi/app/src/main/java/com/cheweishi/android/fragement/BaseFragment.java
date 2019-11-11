package com.cheweishi.android.fragement;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cheweishi.android.activity.BaseActivity;
import com.cheweishi.android.biz.JSONCallback;
import com.cheweishi.android.entity.LoginResponse;
import com.cheweishi.android.http.NetWorkHelper;
import com.cheweishi.android.interfaces.CarReportListener;
import com.cheweishi.android.utils.StringUtil;
import com.lidroid.xutils.http.ResponseInfo;
import com.umeng.analytics.MobclickAgent;

public class BaseFragment extends Fragment implements JSONCallback {
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    protected Context baseContext;
    protected LayoutInflater inflater;
    /**
     * 这个某些需要用到
     */
    protected CarReportListener reportListener;
    private Toast mToast;
    public static LoginResponse loginResponse;

    protected NetWorkHelper netWorkHelper;

    protected Handler loading = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            onDataLoading(msg.what);
        }
    };
    protected boolean isVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
        baseContext = getContext();
        netWorkHelper = NetWorkHelper.getInstance(baseContext);
        isLogined();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        isLogined();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    /**
     * 跟新fragment
     *
     * @param date
     */
    public void updateData(String date) {

    }

    public void setCarReportListener(CarReportListener listener) {
        this.reportListener = listener;
    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * 友盟页面统计
         */
        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
        /**
         * 友盟页面统计
         */
        MobclickAgent.onPageEnd(getClass().getSimpleName());

    }

    /**
     * 返回的json数据
     */
    @Override
    public void receive(int type, String data) {
        // TODO Auto-generated method stub
    }

    /**
     * 返回的json数据
     *
     * @param TAG
     * @param data
     */
    @Override
    public void receive(String TAG, String data) {

    }

    /**
     * 返回的json数据
     *
     * @param data
     */
    @Override
    public void receive(String data) {

    }

    /**
     * 失败数据
     *
     * @param errorMsg
     */
    @Override
    public void error(String errorMsg) {

    }

    /**
     * 返回文件下载回调
     */
    @Override
    public void downFile(int type, ResponseInfo<File> arg0) {
        // TODO Auto-generated method stub

    }

    public void showToast(final String msg) {
        if (!TextUtils.isEmpty(msg)) {
            ((Activity) baseContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mToast == null) {
                        mToast = Toast.makeText(
                                baseContext.getApplicationContext(), msg,
                                Toast.LENGTH_LONG);
                    } else {
                        mToast.setText(msg);
                    }
                    mToast.show();
                }
            });
        }
    }

    public void showToast(final int msg) {
        ((Activity) baseContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(baseContext.getApplicationContext(),
                            msg, Toast.LENGTH_LONG);
                } else {
                    mToast.setText(msg);
                }
                mToast.show();
            }
        });
    }

    public boolean isLogined() {
        // if (StringUtil.isEmpty(loginMessage)) {
        // // loginMessage = LoginMessageUtils.getLoginMessage(baseContext);
        // // loginMessages = (List<LoginMessage>)
        // DBTools.getInstance(baseContext)
        // // .findAll(LoginMessage.class);
        // // if (!StringUtil.isEmpty(loginMessages)) {
        // // loginMessage = loginMessages.get(0);
        // // }
        //
        // }
        loginResponse = BaseActivity.loginResponse;
        return !(StringUtil.isEmpty(loginResponse)
                || StringUtil.isEmpty(loginResponse.getDesc()));
    }


    /**
     * 判断是否有车辆
     *
     * @return
     */
    public boolean hasCar() {
        if (isLogined()) {
            if (!StringUtil.isEmpty(loginResponse.getMsg()) && !StringUtil.isEmpty(loginResponse.getMsg().getDefaultVehiclePlate())) {
                return true;
            }

        }
        return false;
    }

    public void onDataLoading(int what) {

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
        }
    }

    /**
     * 第一次初始化
     */
    protected void onVisible() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }
}
