package com.cheweishi.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.CouponAdapter;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.ActivityCouponResponse;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.tools.EmptyTools;
import com.cheweishi.android.tools.LoginMessageUtils;
import com.cheweishi.android.utils.GsonUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tangce on 5/11/2016.
 */
@ContentView(R.layout.activity_coupon)
public class CouponActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<ListView>, CouponAdapter.OnCouponClickListener, AdapterView.OnItemClickListener {

    @ViewInject(R.id.pl_list)
    private PullToRefreshListView pullListView;

    @ViewInject(R.id.left_action)
    private Button left_action;

    @ViewInject(R.id.title)
    private TextView title;

    private List<ActivityCouponResponse.MsgBean> list = new ArrayList<>();

    private CouponAdapter adapter;

    private int positionGet;

    private int page = 1;

    private int total;

    private boolean isHeaderRefresh = false; // 是否为下拉刷新


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        init();
    }

    private void init() {
        left_action.setText("返回");
        title.setText("优惠券中心");
        adapter = new CouponAdapter(baseContext, list);
        adapter.setOnCouponClickListener(this);
        pullListView.setAdapter(adapter);
        pullListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullListView.setOnRefreshListener(this);
        pullListView.setOnItemClickListener(this);
        getServerData(0);
    }

    private void getServerData(int type) {
        if (0 == type)
            ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_COUPON + NetInterface.GETLISTCOUPON + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("pageSize", 10);
        param.put("pageNumber", page);
        param.put(Constant.PARAMETER_TAG, NetInterface.GETLISTCOUPON);
        netWorkHelper.PostJson(url, param, this);
    }

    @Override
    public void receive(String TAG, String data) {

        pullListView.onRefreshComplete();
        switch (TAG) {
            case NetInterface.GETLISTCOUPON:

                ActivityCouponResponse couponResponse = (ActivityCouponResponse) GsonUtil.getInstance().convertJsonStringToObject(data, ActivityCouponResponse.class);

                if (!couponResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    ProgrosDialog.closeProgrosDialog();
                    showToast(couponResponse.getDesc());
                    return;
                }

                List<ActivityCouponResponse.MsgBean> temp = couponResponse.getMsg();

                if (null != temp && 0 < temp.size()) {
                    total = couponResponse.getPage().getTotal();
                    if (isHeaderRefresh) {
                        list = temp;
                    } else {
                        list.addAll(temp);
                    }
                    if (list.size() < total) {
                        pullListView.onRefreshComplete();
                        pullListView.setMode(PullToRefreshBase.Mode.BOTH);
                    } else {
                        pullListView.onRefreshComplete();
                        pullListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                    adapter.setData(list);
                } else {
                    EmptyTools.setEmptyView(baseContext, pullListView);
                    EmptyTools.setImg(R.drawable.mycar_icon);
                    EmptyTools.setMessage("当前暂时没有优惠券活动");
                    pullListView.onRefreshComplete();
                }


                loginResponse.setToken(couponResponse.getToken());
//                LoginMessageUtils.saveloginmsg(baseContext, loginResponse);

                break;
            case NetInterface.GETCOUPON: // 领取优惠券
                BaseResponse getCouponResponse = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);

                if (!getCouponResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    ProgrosDialog.closeProgrosDialog();
                    showToast(getCouponResponse.getDesc());
                    return;
                }

                if (null != list && 0 < list.size()) {
                    list.get(positionGet).setIsGet(true);
                    adapter.setData(list);
                }

                // TODO通知更新优惠券
                Constant.CURRENT_REFRESH = Constant.COUPON_REFRESH;
                Intent mIntent = new Intent();
                mIntent.setAction(Constant.REFRESH_FLAG);
                sendBroadcast(mIntent);

                loginResponse.setToken(getCouponResponse.getToken());
//                LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
                break;
        }
        ProgrosDialog.closeProgrosDialog();

    }

    @OnClick({R.id.left_action})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.left_action:
                finish();
                break;
        }

    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        isHeaderRefresh = true;
        page = 1;
        getServerData(1);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        isHeaderRefresh = false;
        page++;
        getServerData(1);
    }

    @Override
    public void onCouponClick(int id, int position) {
        positionGet = position;
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_COUPON + NetInterface.GETCOUPON + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("couponId", id);
        param.put(Constant.PARAMETER_TAG, NetInterface.GETCOUPON);
        netWorkHelper.PostJson(url, param, this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(baseContext, CouponDetailActivity.class);
        intent.putExtra("COUPON_DETAIL", list.get(position - 1).getRemark());
        startActivity(intent);
    }
}
