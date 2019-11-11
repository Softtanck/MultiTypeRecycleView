package com.cheweishi.android.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cheweishi.android.R;
import com.cheweishi.android.activity.CouponDetailActivity;
import com.cheweishi.android.adapter.MyCarCouponAdapter;
import com.cheweishi.android.adapter.MyCouponAdapter;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.MyCarCouponResponse;
import com.cheweishi.android.entity.MyCouponResponse;
import com.cheweishi.android.tools.EmptyTools;
import com.cheweishi.android.tools.LoginMessageUtils;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.StringUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tangce on 6/15/2016.
 */
public class MyCarCouponFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private PullToRefreshListView mListView;
    private List<MyCarCouponResponse.MsgBean> list;
    // 定义加载的页面数量
    private int page = 1;
    private MyCarCouponAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mycoupon, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (PullToRefreshListView) view.findViewById(R.id.xlistview_mycoupon);
        init();
    }

    private void init() {

        mListView.setMode(PullToRefreshBase.Mode.DISABLED);

        list = new ArrayList<>();
        adapter = new MyCarCouponAdapter(baseContext, list);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        getData(); // TODO 还未部署
    }


    /**
     * 获取洗车券详情数据
     */
    private void getData() {
        if (isLogined()) {
            String url = NetInterface.BASE_URL + NetInterface.TEMP_COUPON + NetInterface.WASHCARCOUPON + NetInterface.SUFFIX;
            Map<String, Object> param = new HashMap<>();
            param.put("userId", loginResponse.getDesc());
            param.put("token", loginResponse.getToken());
            netWorkHelper.PostJson(url, param, this);
        }
    }

    @Override
    public void receive(String data) {

        ProgrosDialog.closeProgrosDialog();

        MyCarCouponResponse response = (MyCarCouponResponse) GsonUtil.getInstance().convertJsonStringToObject(data, MyCarCouponResponse.class);
        if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            showToast(response.getDesc());
            return;
        }

        if (null != response.getMsg()) {
            List<MyCarCouponResponse.MsgBean> temp = response.getMsg();
            list.addAll(temp);
            if (0 < list.size()) {
                adapter.setData(list);
                mListView.onRefreshComplete();
            } else {
                mListView.onRefreshComplete();
                EmptyTools.setEmptyView(baseContext, mListView);
                EmptyTools.setImg(R.drawable.dingdanwu_icon);
                EmptyTools.setMessage("您还没有优惠券,赶快去领取吧");
                mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }
        } else {
            mListView.onRefreshComplete();
            EmptyTools.setEmptyView(baseContext, mListView);
            EmptyTools.setImg(R.drawable.dingdanwu_icon);
            EmptyTools.setMessage("您还没有优惠券,赶快去领取吧");
            mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        }


        loginResponse.setToken(response.getToken());
//        LoginMessageUtils.saveloginmsg(baseContext, loginResponse);


    }


    @Override
    public void error(String errorMsg) {
        mListView.onRefreshComplete();
        ProgrosDialog.closeProgrosDialog();
        showToast(R.string.server_link_fault);
    }


    /**
     * 显示数据
     */
    private void showData() {
        if (!StringUtil.isEmpty(list)) {
            adapter.setData(list);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(baseContext, CouponDetailActivity.class);
        intent.putExtra("COUPON_DETAIL", list.get(position - 1).getCarWashingCoupon().getRemark());
        startActivity(intent);
    }
}
