package com.cheweishi.android.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.MyorderAdapter;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.OrderResponse;
import com.cheweishi.android.tools.EmptyTools;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.widget.CustomDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MyorderActivity extends BaseActivity implements OnClickListener,
        OnRefreshListener2<ListView>, OnItemClickListener {
    @ViewInject(R.id.title)
    private TextView tvTitle;
    @ViewInject(R.id.left_action)
    private Button btnLeft;
    // @ViewInject(R.id.right_action)
    // private ImageButton right_action;
    @ViewInject(R.id.lv_myOrder)
    private PullToRefreshListView lv_myOrder;
    private ListView mListView;
    // @ViewInject(R.id.layoutNoData)
    // private LinearLayout layoutNoData;
    private List<OrderResponse.MsgBean> list = new ArrayList<OrderResponse.MsgBean>();
    private MyorderAdapter adapter;
    private int pageNumber = 1;
    private CustomDialog.Builder builder;
    private CustomDialog deleteDialog;
    private  boolean refresh = false;
    private int total = 0; //分页总数
    private boolean isHeaderRefresh = false; //是否下拉刷新
    private boolean isEmpty;

    // WashcarHistoryActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_washcar_history);
        ViewUtils.inject(this);
        init();

    }

    private void init() {
        tvTitle.setText(R.string.myorder);
        btnLeft.setText(R.string.back);
        // right_action.setImageResource(R.drawable.sousuo_icon);
        lv_myOrder.setOnRefreshListener(this);
        // 设置listview的上拉加载下拉刷新
        lv_myOrder.setMode(Mode.BOTH);
        lv_myOrder.setOnItemClickListener(this);
        // 通过pullToRefreshListView得到listview
        mListView = lv_myOrder.getRefreshableView();
        EmptyTools.setEmptyView(this, mListView);

        list = new ArrayList<OrderResponse.MsgBean>();
        adapter = new MyorderAdapter(list, this);
        mListView.setAdapter(adapter);

        connectToServer(0);
    }


    @OnClick({R.id.left_action, R.id.right_action})
    public void onClick(View arg0) {

        switch (arg0.getId()) {
            case R.id.left_action:
                finish();
                break;
            case R.id.right_action:
//                showDeleteDialog();
                break;

            default:
                break;
        }

    }



    private void connectToServer(int type) {
        if (0 == type)
            ProgrosDialog.openDialog(this);
        // TODO 发包
        String url = NetInterface.BASE_URL + NetInterface.TEMP_ORDER + NetInterface.USER_ORDER + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("pageNumber", pageNumber);
        param.put("pageSize", 10);
        param.put(Constant.PARAMETER_TAG, "normal");
        netWorkHelper.PostJson(url, param, this);
    }


    @Override
    public void receive(String TAG, String data) {

        switch (TAG) {
            case "normal":
                OrderResponse response = (OrderResponse) GsonUtil.getInstance().convertJsonStringToObject(data, OrderResponse.class);
                if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    ProgrosDialog.closeProgrosDialog();
                    showToast(response.getDesc());
                    return;
                }


                List<OrderResponse.MsgBean> temp = response.getMsg();
                if (null != temp && 0 < temp.size()) {
                    total = response.getPage().getTotal();
                    if (isHeaderRefresh) {
                        list = temp;
                    } else {
                        list.addAll(temp);
                    }

                    isEmpty = false;
                } else if (!isEmpty) { // 已经添加了
                    isEmpty = true;
                    list = temp;
                    EmptyTools.setEmptyView(baseContext, lv_myOrder);
                    EmptyTools.setImg(R.drawable.mycar_icon);
                    EmptyTools.setMessage("您当前还没有订单");
                }

                adapter.setData(list);
                loginResponse.setToken(response.getToken());
                ProgrosDialog.closeProgrosDialog();
                if (list.size() < total) {
                    lv_myOrder.onRefreshComplete();
                    lv_myOrder.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    lv_myOrder.onRefreshComplete();
                    lv_myOrder.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
//                LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
                break;
        }
    }

    @Override
    public void error(String errorMsg) {
        lv_myOrder.onRefreshComplete();
        ProgrosDialog.closeProgrosDialog();
        showToast(R.string.server_link_fault);
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        isHeaderRefresh = true;
        pageNumber = 1;
        connectToServer(1);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        isHeaderRefresh = false;
        pageNumber++;
        connectToServer(1);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//        showToast("非常抱歉,此功能正在开发中...");
//        Intent intent = new Intent(this, OrderDetailsActivity.class);
//        intent.putExtra("order_id", list.get(arg2 - 1).getCarService().getServiceCategory().getId());
//        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!refresh) {
            refresh = true;
        } else {
            // 重新连接
            list.clear();
            connectToServer(0);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EmptyTools.destory();
    }
}
