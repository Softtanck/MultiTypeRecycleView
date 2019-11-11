package com.cheweishi.android.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.UpRedAdapter;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.PurseIntegralResponse;
import com.cheweishi.android.tools.EmptyTools;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.widget.XCRoundImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 成长红包
 *
 * @author XMH
 */
public class UpRedIntegralActivity extends BaseActivity implements
        OnRefreshListener2<ListView> {


    @ViewInject(R.id.left_action)
    private Button left_action;
    @ViewInject(R.id.title)
    private TextView title;
    // 下拉加载的listview
    @ViewInject(R.id.integral_xlistview)
    private PullToRefreshListView mListView;
    //总积分
    @ViewInject(R.id.tv_balance_num)
    private TextView tv_balance_num;
    @ViewInject(R.id.ley_integral)
    private RelativeLayout ley_integral;

    // 列表
    private ListView listView;
    // 成长红包的适配器
    private UpRedAdapter mIntegralAdapter;


    private List<PurseIntegralResponse.MsgBean> mList;

    private int pageNumber = 1;
    private int total = 0;
    private boolean isHeaderRefresh = false;
    private boolean isEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purse_upred);
        ViewUtils.inject(this);


        mList = new ArrayList<PurseIntegralResponse.MsgBean>();
        mIntegralAdapter = new UpRedAdapter(baseContext, mList);
        mListView.setAdapter(mIntegralAdapter);
        mListView.setMode(Mode.BOTH);
        mListView.setOnRefreshListener(this);
        init();
    }

    private void init() {
        title.setText(R.string.purse_red);
        left_action.setText(getResources().getString(R.string.back));
        left_action.setOnClickListener(listener);
        ley_integral.setOnClickListener(listener);
        tv_balance_num.setText(getIntent().getStringExtra("score"));
        request(1);
    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.left_action:
                    UpRedIntegralActivity.this.finish();
                    break;
                case R.id.ley_integral: // 点击积分商城的时候
//                    getDuiBaUrl();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public void receive(String data) {

        PurseIntegralResponse response = (PurseIntegralResponse) GsonUtil.getInstance().convertJsonStringToObject(data, PurseIntegralResponse.class);
        if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            ProgrosDialog.closeProgrosDialog();
            showToast(response.getDesc());
            return;
        }

        List<PurseIntegralResponse.MsgBean> temp = response.getMsg();

        if (null != temp && 0 < temp.size()) {
            total = response.getPage().getTotal();
            if (isHeaderRefresh) {
                mList = temp;
            } else {
                mList.addAll(temp);
            }

            isEmpty = false;
        } else if (!isEmpty) { // 已经添加了
            isEmpty = true;
            mList = temp;

            EmptyTools.setEmptyView(baseContext, mListView);
            EmptyTools.setImg(R.drawable.mycar_icon);
            EmptyTools.setMessage("您当前还没有明细");
        }

        mIntegralAdapter.setList(mList);
        loginResponse.setToken(response.getToken());
//        LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
        ProgrosDialog.closeProgrosDialog();
        if (mList.size() < total) {
            mListView.onRefreshComplete();
            mListView.setMode(Mode.BOTH);
        } else {
            mListView.onRefreshComplete();
            mListView.setMode(Mode.PULL_FROM_START);
        }

    }

    @Override
    public void error(String errorMsg) {
        ProgrosDialog.closeProgrosDialog();
        mListView.onRefreshComplete();
        showToast(R.string.server_link_fault);
    }


    /**
     * 网络请求方法
     */
    private void request(int type) {
        if (0 == type)
            ProgrosDialog.openDialog(this);
        if (isLogined()) {
            String url = NetInterface.BASE_URL + NetInterface.TEMP_USER_BALANCE + NetInterface.WALLET_RECORD + NetInterface.SUFFIX;
            Map<String, Object> param = new HashMap<>();
            param.put("userId", loginResponse.getDesc());
            param.put("token", loginResponse.getToken());
            param.put("walletType", "REDPACKET"); //  红包
            param.put("walletId", getIntent().getIntExtra("walletId", 1));
            param.put("pageSize", 5);
            param.put("pageNumber", pageNumber);
            netWorkHelper.PostJson(url, param, this);
        }
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
    protected void onDestroy() {
        super.onDestroy();
        EmptyTools.destory();
    }
}
