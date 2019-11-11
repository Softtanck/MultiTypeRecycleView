package com.cheweishi.android.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.TelephonEchargeDetailsAdapter;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.ChargeResponse;
import com.cheweishi.android.tools.EmptyTools;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.StringUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 我的余额
 *
 * @author XMh
 */
public class PurseBalanceActivity extends BaseActivity implements
        OnRefreshListener2<ListView> {
    @ViewInject(R.id.left_action)
    private Button left_action;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.tv_balance_num)
    private TextView tv_balance_num;
    private MyBroadcastReceiver broad;
    // 上拉加载下拉刷新列表
    @ViewInject(R.id.telephonechargedetils_listview)
    private PullToRefreshListView telephonechargedetils_listview;
    // item的数据
    private List<ChargeResponse.MsgBean> list = new ArrayList<>();
    // 定义一个私有的余额详情adapter
    private TelephonEchargeDetailsAdapter telephonEchargeDetailsAdapter;
    private Intent intent;
    private int pageNumber = 1;
    private int pageSize = 10;
    private ListView mListView;
    private int total = 0;
    private boolean isHeaderRefresh = false;
    private boolean isEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purse_balance);
        ViewUtils.inject(this);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EmptyTools.destory();
        unregisterReceiver(broad);
    }

    /**
     * 初始化视图
     */
    private void init() {
        title.setText(R.string.purse_my_balance);
        left_action.setText(R.string.back);
        telephonechargedetils_listview.setOnRefreshListener(this);
        telephonechargedetils_listview.setMode(Mode.BOTH);
        mListView = telephonechargedetils_listview.getRefreshableView();
        telephonEchargeDetailsAdapter = new TelephonEchargeDetailsAdapter(this, list);
        mListView.setAdapter(telephonEchargeDetailsAdapter);
        String money = getIntent().getStringExtra("money");
        if (StringUtil.isEmpty(money)) {
            tv_balance_num.setText("0.00");
        } else {
            tv_balance_num.setText(money);
        }
        request(1);
    }

    @OnClick({R.id.left_action, R.id.title, R.id.ll_purse_balance_pay, R.id.ll_purse_balance_device})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_action:// 返回
                PurseBalanceActivity.this.finish();
                break;
            case R.id.ll_purse_balance_pay:
                intent = new Intent(PurseBalanceActivity.this, PayActivty.class);
                startActivity(intent);
                break;
            case R.id.ll_purse_balance_device:
//                intent = new Intent(PurseBalanceActivity.this, PayActivty.class);
//                intent.putExtra("PAY_TYPE", true);
//                startActivity(intent);
                OpenCamera();
                break;
            default:
                break;
        }
    }

//    /**
//     * 打开相机
//     */
//    private void OpenCamera() {
//        applyAdmin(Manifest.permission.CAMERA, MY_CAMEAR_PREMESSION);
//        PackageManager pkm = getPackageManager();
//        boolean has_permission = (PackageManager.PERMISSION_GRANTED == pkm
//                .checkPermission("android.permission.CAMERA", baseContext.getPackageName()));//"packageName"));
//        if (has_permission) {
//            Intent intent = new Intent(baseContext,
//                    MipcaActivityCapture.class);
//            intent.putExtra("PAY_TYPE", true);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        } else {
//            showToast("请为该应用添加打开相机权限");
//        }
//    }

    // 网络请求方法
    private void request(int type) {
        if (0 == type)
            ProgrosDialog.openDialog(baseContext);
        if (isLogined()) {
            String url = NetInterface.BASE_URL + NetInterface.TEMP_USER_BALANCE + NetInterface.WALLET_RECORD + NetInterface.SUFFIX;
            Map<String, Object> param = new HashMap<>();
            param.put("userId", loginResponse.getDesc());
            param.put("token", loginResponse.getToken());
            param.put("walletType", "MONEY"); //  余额
            param.put("walletId", getIntent().getIntExtra("walletId", 1));
            param.put("pageSize", pageSize);
            param.put("pageNumber", pageNumber);
            netWorkHelper.PostJson(url, param, this);
        }

    }

    @Override
    public void receive(String data) {

        ChargeResponse response = (ChargeResponse) GsonUtil.getInstance().convertJsonStringToObject(data, ChargeResponse.class);
        if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            showToast(response.getDesc());
            return;
        }


        List<ChargeResponse.MsgBean> temp = response.getMsg();
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

            EmptyTools.setEmptyView(baseContext, mListView);
            EmptyTools.setImg(R.drawable.mycar_icon);
            EmptyTools.setMessage("您当前还没有记录");
        }

        telephonEchargeDetailsAdapter.setlist(list);
        tv_balance_num.setText(response.getDesc());
        loginResponse.setToken(response.getToken());
        ProgrosDialog.closeProgrosDialog();
        if (list.size() < total) {
            telephonechargedetils_listview.onRefreshComplete();
            telephonechargedetils_listview.setMode(PullToRefreshBase.Mode.BOTH);
        } else {
            telephonechargedetils_listview.onRefreshComplete();
            telephonechargedetils_listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        }
//        LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
    }

    @Override
    public void error(String errorMsg) {
        telephonechargedetils_listview.onRefreshComplete();
        ProgrosDialog.closeProgrosDialog();
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        isHeaderRefresh = true;
        pageNumber = 1;
        request(1);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        isHeaderRefresh = false;
        pageNumber++;
        request(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (broad == null) {
            broad = new MyBroadcastReceiver();
        }
        IntentFilter intentFilter = new IntentFilter(Constant.REFRESH_FLAG);
        registerReceiver(broad, intentFilter);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            if (!StringUtil.isEquals(intent.getAction(), Constant.REFRESH_FLAG,
                    true)) {
                return;
            }
            if (StringUtil.isEquals(Constant.CURRENT_REFRESH,
                    Constant.RECHARGE_REFRESH, true)) {
                Constant.EDIT_FLAG = true;
                isHeaderRefresh = true;
                pageNumber = 1;
                request(0);
            } else if (StringUtil.isEquals(Constant.CURRENT_REFRESH,
                    Constant.WEIXIN_PAY_REFRESH, true)) {
                Constant.EDIT_FLAG = true;
                isHeaderRefresh = true;
                pageNumber = 1;
                request(0);
            } else if (StringUtil.isEquals(Constant.CURRENT_REFRESH, Constant.PAY_REFRESH, true)) {
                Constant.EDIT_FLAG = true;
                isHeaderRefresh = true;
                pageNumber = 1;
                request(0);
            }
        }
    }
}
