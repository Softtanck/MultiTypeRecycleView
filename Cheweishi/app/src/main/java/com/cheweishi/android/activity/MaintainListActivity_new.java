package com.cheweishi.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.MainListViewAdapter;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.ServiceListResponse;
import com.cheweishi.android.tools.EmptyTools;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.MyMapUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
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
 * @author zhangq
 */
@ContentView(R.layout.activity_washcar_list)
public class MaintainListActivity_new extends BaseActivity implements
        OnRefreshListener2<ListView> {
    @ViewInject(R.id.title)
    private TextView tvTitle;
    @ViewInject(R.id.left_action)
    private Button btnLeft;
    @ViewInject(R.id.listview)
    private PullToRefreshListView mListView;
    private MainListViewAdapter listViewAdapter;
    private List<ServiceListResponse.MsgBean> washcarList;

    private int page = 1;

    private int total;
    private boolean isHeaderRefresh = false; //是否下拉刷新
    private boolean isEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		/* init tools */
        ViewUtils.inject(this);

		/**/
        initView();
        getDataFromIntent(0);
    }

    private void getDataFromIntent(int type) {

        if (0 == type)
            ProgrosDialog.openDialog(this);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_HOME_URL + NetInterface.LIST + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("latitude", MyMapUtils.getLatitude(this));//维度
//            param.put("latitude", "10");//维度
        param.put("longitude", MyMapUtils.getLongitude(this));//经度
//            param.put("longitude", "10");//经度
        /**
         * 1保养
         2	洗车
         3	维修
         4	紧急救援
         5	美容
         */
        param.put("serviceCategoryId", 1); // TODO 目前只有一种
        param.put("pageSize", 5);
        param.put("pageNumber", page);
        param.put(Constant.PARAMETER_TAG, NetInterface.LIST);
        netWorkHelper.PostJson(url, param, this);
    }

    @Override
    public void receive(String TAG, String data) {
        switch (TAG) {
            case NetInterface.LIST:
                ServiceListResponse response = (ServiceListResponse) GsonUtil.getInstance().convertJsonStringToObject(data, ServiceListResponse.class);
                if (null == response || !response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    ProgrosDialog.closeProgrosDialog();
                    showToast(response.getDesc());
                    return;
                }

                List<ServiceListResponse.MsgBean> temp = response.getMsg();

                if (null != temp && 0 < temp.size()) {
                    total = response.getPage().getTotal();
                    if (isHeaderRefresh) {
                        washcarList = temp;
                    } else {
                        washcarList.addAll(temp);
                    }

                    isEmpty = false;
                } else if (!isEmpty) { // 已经添加了
                    isEmpty = true;
                    washcarList = temp;

                    EmptyTools.setEmptyView(baseContext, mListView);
                    EmptyTools.setImg(R.drawable.mycar_icon);
                    EmptyTools.setMessage("当前没有保养信息");
                }
                listViewAdapter.setData(washcarList);
                loginResponse.setToken(response.getToken());
                ProgrosDialog.closeProgrosDialog();
                if (washcarList.size() < total) {
                    mListView.onRefreshComplete();
                    mListView.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    mListView.onRefreshComplete();
                    mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
//                    LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
                break;
        }
    }


    @Override
    public void error(String errorMsg) {
        ProgrosDialog.closeProgrosDialog();
        mListView.onRefreshComplete();
        showToast(R.string.FAIL);
    }


    private void initView() {
        tvTitle.setText("到店保养");
        btnLeft.setText(R.string.back);
        mListView.setOnRefreshListener(this);
        mListView.setMode(Mode.BOTH);

        washcarList = new ArrayList<ServiceListResponse.MsgBean>();
        // TODO 修改主页而注释
        listViewAdapter = new MainListViewAdapter(this, washcarList);
        mListView.setAdapter(listViewAdapter);
    }

    @OnClick({R.id.left_action})
    public void finishActivity(View v) {
        switch (v.getId()) {
            case R.id.left_action:
                onBackPressed();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EmptyTools.destory();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        isHeaderRefresh = true;
        page = 1;
        getDataFromIntent(1);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        isHeaderRefresh = false;
        page++;
        getDataFromIntent(1);
    }
}
