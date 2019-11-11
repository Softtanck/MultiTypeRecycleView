package com.cheweishi.android.fragement;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;

import com.cheweishi.android.R;
import com.cheweishi.android.activity.BaseActivity;
import com.cheweishi.android.activity.MainNewActivity;
import com.cheweishi.android.adapter.NewsFragmentPagerAdapter;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.NewsTypeResponse;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.ScreenUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tangce on 7/6/2016.
 */
public class NewsFragment extends BaseFragment {

    private boolean isLoaded = false;

    private ViewPager viewPager;

    private TabLayout tl_news;

    private NewsFragmentPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.vp_news);
        tl_news = (TabLayout) view.findViewById(R.id.tl_news);
    }

    @Override
    public void onDataLoading(int what) {
        if (0x2 == what) {
            isLoaded = true;
            onLoad();
        }
    }

    private void onLoad() {
        sendPacket();
    }


    @Override
    public void receive(String data) {
//        ProgrosDialog.closeProgrosDialog();
        NewsTypeResponse response = (NewsTypeResponse) GsonUtil.getInstance().convertJsonStringToObject(data, NewsTypeResponse.class);
        if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            ProgrosDialog.closeProgrosDialog();
            showToast(response.getDesc());
            return;
        }
        if (null == response.getMsg() || 0 >= response.getMsg().size()) {
            ProgrosDialog.closeProgrosDialog();
            return;
        }

        adapter = new NewsFragmentPagerAdapter(((MainNewActivity) baseContext).getSupportFragmentManager(), baseContext, response.getMsg());
        viewPager.setAdapter(adapter);
        if (5 <= response.getMsg().size())
            tl_news.setTabMode(TabLayout.MODE_SCROLLABLE);
        tl_news.setupWithViewPager(viewPager);
        loginResponse.setToken(response.getToken());
        BaseActivity.loginResponse = loginResponse;
    }

    private void sendPacket() {
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_NEWS + NetInterface.GET_NEWS_TYPES + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        netWorkHelper.PostJson(url, param, this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && !isLoaded)
            loading.sendEmptyMessage(0x2);
    }


    public void getDataForNews() {
        if (!isLoaded)
            return;
        sendPacket();
    }
}
