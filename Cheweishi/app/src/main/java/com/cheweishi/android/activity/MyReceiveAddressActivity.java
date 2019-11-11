package com.cheweishi.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.MyReceiveAddressAdapter;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.MyRecevieAddressResponse;
import com.cheweishi.android.tools.EmptyTools;
import com.cheweishi.android.utils.GsonUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tangce on 8/31/2016.
 */
public class MyReceiveAddressActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2<ListView> {

    @ViewInject(R.id.left_action)
    private Button left_action; // 左边标题

    @ViewInject(R.id.title)
    private TextView title; // 标题

    @ViewInject(R.id.pl_address)
    private PullToRefreshListView pl_address;//下拉地址

    private MyReceiveAddressAdapter addressAdapter;

    private List<MyRecevieAddressResponse.MsgBean> list = new ArrayList<>();

    private int page = 1;

    private int total;

    private boolean isHeaderRefresh = false; // 是否为下拉刷新

    private boolean isRefreshing = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (isRefreshing) {
            isRefreshing = false;
            isHeaderRefresh = true;
            page = 1;
            getReceiveAddressPacket(1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isRefreshing)
            isRefreshing = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_my_address);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        left_action.setText(R.string.back);
        title.setText(R.string.receive_address);
        addressAdapter = new MyReceiveAddressAdapter(baseContext, list);
        pl_address.setAdapter(addressAdapter);
        pl_address.setOnRefreshListener(this);
        getReceiveAddressPacket(0);
    }


    private void getReceiveAddressPacket(int type) {
        if (0 == type)
            ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_SHOP_ADD + NetInterface.LIST + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("pageSize", 10);
        param.put("pageNumber", page);
        netWorkHelper.PostJson(url, param, this);
    }

    @Override
    public void receive(String data) {
        MyRecevieAddressResponse addressResponse = (MyRecevieAddressResponse) GsonUtil.getInstance().convertJsonStringToObject(data, MyRecevieAddressResponse.class);

        if (!addressResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            ProgrosDialog.closeProgrosDialog();
            pl_address.onRefreshComplete();
            showToast(addressResponse.getDesc());
            return;
        }

        List<MyRecevieAddressResponse.MsgBean> temp = addressResponse.getMsg();

        if (null != temp && 0 < temp.size()) {
            total = addressResponse.getPage().getTotal();
            if (isHeaderRefresh) {
                list = temp;
            } else {
                list.addAll(temp);
            }
            if (list.size() < total) {
                pl_address.onRefreshComplete();
                pl_address.setMode(PullToRefreshBase.Mode.BOTH);
            } else {
                pl_address.onRefreshComplete();
                pl_address.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            }

        } else {
            list = temp;
            EmptyTools.setEmptyView(baseContext, pl_address);
            EmptyTools.setImg(R.drawable.mycar_icon);
            EmptyTools.setMessage("您还没有添加收货地址");
            pl_address.onRefreshComplete();
        }

        addressAdapter.setData(list);
        loginResponse.setToken(addressResponse.getToken());
        pl_address.onRefreshComplete();
        ProgrosDialog.closeProgrosDialog();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        isHeaderRefresh = true;
        page = 1;
        getReceiveAddressPacket(1);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        isHeaderRefresh = false;
        page++;
        getReceiveAddressPacket(1);
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
