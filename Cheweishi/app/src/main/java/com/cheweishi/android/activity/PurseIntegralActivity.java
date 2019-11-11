package com.cheweishi.android.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.IntegralAdapter;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.PurseIntegralResponse;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.tools.EmptyTools;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.widget.XCRoundImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 我的积分
 *
 * @author XMH
 */
public class PurseIntegralActivity extends BaseActivity implements
        OnRefreshListener2<ListView> {


    private static final int INTEGRAL_CODE = 2001;

    @ViewInject(R.id.left_action)
    private Button left_action;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.xcRoundImg)
    private XCRoundImageView xcRoundImg;
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
    // 积分的适配器
    private IntegralAdapter mIntegralAdapter;


    private List<PurseIntegralResponse.MsgBean> mList;

    private int pageNumber = 1;
    private int total = 0;
    private boolean isHeaderRefresh = false;
    private boolean isEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purse_certificates);
        ViewUtils.inject(this);


        mList = new ArrayList<PurseIntegralResponse.MsgBean>();
        mIntegralAdapter = new IntegralAdapter(baseContext, mList);
        mListView.setAdapter(mIntegralAdapter);
        mListView.setMode(Mode.BOTH);
        mListView.setOnRefreshListener(this);
        init();
    }

    private void init() {
        title.setText(R.string.purse_my_integral);
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
                    PurseIntegralActivity.this.finish();
                    break;
                case R.id.ley_integral: // 点击积分商城的时候
                    getDuiBaUrl();
                    break;
                default:
                    break;
            }
        }
    };

    private void getDuiBaUrl() {
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_DUIBA + NetInterface.GET_DUIBA_LOGIN_URL + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put(Constant.PARAMETER_TAG, NetInterface.GET_DUIBA_LOGIN_URL);
        netWorkHelper.PostJson(url, param, this);
    }


    @Override
    public void receive(String TAG, String data) {
        ProgrosDialog.closeProgrosDialog();
        switch (TAG) {
            case NetInterface.GET_DUIBA_LOGIN_URL:
                BaseResponse duibaResponse = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
                if (!duibaResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    showToast(duibaResponse.getDesc());
                    return;
                }

                if (null != duibaResponse.getDesc() && !"".equals(duibaResponse.getDesc())) {
//                    Intent duiba = new Intent(baseContext, WebActivity.class);
//                    duiba.putExtra("url", duibaResponse.getDesc());
//                    startActivity(duiba);
                    goDuiba(duibaResponse.getDesc());
                }

                loginResponse.setToken(duibaResponse.getToken());
//                LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
                break;
        }
    }

    private void goDuiba(String url) {
        Intent intent = new Intent();
        intent.setClass(baseContext, CreditActivity.class);
        intent.putExtra("navColor", "#FFFFFF");    //配置导航条的背景颜色，请用#ffffff长格式。
        intent.putExtra("titleColor", "#484848");    //配置导航条标题的颜色，请用#ffffff长格式。
        intent.putExtra("url", url);    //配置自动登陆地址，每次需服务端动态生成。
        startActivity(intent);

        CreditActivity.creditsListener = new CreditActivity.CreditsListener() {
            /**
             * 当点击分享按钮被点击
             * @param shareUrl 分享的地址
             * @param shareThumbnail 分享的缩略图
             * @param shareTitle 分享的标题
             * @param shareSubtitle 分享的副标题
             */
            public void onShareClick(WebView webView, String shareUrl, String shareThumbnail, String shareTitle, String shareSubtitle) {
                //当分享按钮被点击时，会调用此处代码。在这里处理分享的业务逻辑。
//                new AlertDialog.Builder(webView.getContext())
//                        .setTitle("分享信息")
//                        .setItems(new String[]{"标题：" + shareTitle, "副标题：" + shareSubtitle, "缩略图地址：" + shareThumbnail, "链接：" + shareUrl}, null)
//                        .setNegativeButton("确定", null)
//                        .show();
            }

            /**
             * 当点击“请先登录”按钮唤起登录时，会调用此处代码。
             * 用户登录后，需要将CreditsActivity.IS_WAKEUP_LOGIN变量设置为true。
             * @param webView 用于登录成功后返回到当前的webview刷新登录状态。
             * @param currentUrl 当前页面的url
             */
            public void onLoginClick(WebView webView, String currentUrl) {
                //当未登录的用户点击去登录时，会调用此处代码。
                //用户登录后，需要将CreditsActivity.IS_WAKEUP_LOGIN变量设置为true。
                //为了用户登录后能回到未登录前的页面（currentUrl）。
                //当用户登录成功后，需要重新请求一次服务端，带上currentUrl。
                //用该方法中的webview变量加载请求链接。
                //服务端收到请求后在生成免登录url时，将currentUrl放入redirect参数，通知客户端302跳转到新生成的免登录URL。
//                new AlertDialog.Builder(webView.getContext())
//                        .setTitle("跳转登录")
//                        .setMessage("跳转到登录页面？")
//                        .setPositiveButton("是", null)
//                        .setNegativeButton("否", null)
//                        .show();
            }

            /**
             * 当点击“复制”按钮时，触发该方法，回调获取到券码code
             * @param webView webview对象。
             * @param code 复制的券码
             */
            public void onCopyCode(WebView webView, String code) {
                //当未登录的用户点击去登录时，会调用此处代码。
//                new AlertDialog.Builder(webView.getContext())
//                        .setTitle("复制券码")
//                        .setMessage("已复制，券码为：" + code)
//                        .setPositiveButton("是", null)
//                        .setNegativeButton("否", null)
//                        .show();
            }

            /**
             * 积分商城返回首页刷新积分时，触发该方法。
             */
            public void onLocalRefresh(WebView mWebView, String credits) {
                //String credits为积分商城返回的最新积分，不保证准确。
                //触发更新本地积分，这里建议用ajax向自己服务器请求积分值，比较准确。
//                Toast.makeText(getApplicationContext(), "触发本地刷新积分："+credits,Toast.LENGTH_SHORT).show();
                tv_balance_num.setText(credits);
            }
        };
    }

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
            EmptyTools.setMessage("您当前还没有订单");
        }

        mIntegralAdapter.setList(mList);
        loginResponse.setToken(response.getToken());
//        LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
        ProgrosDialog.closeProgrosDialog();
        if (mList.size() < total) {
            mListView.onRefreshComplete();
            mListView.setMode(PullToRefreshBase.Mode.BOTH);
        } else {
            mListView.onRefreshComplete();
            mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        }

    }

    @Override
    public void error(String errorMsg) {
        ProgrosDialog.closeProgrosDialog();
        mListView.onRefreshComplete();
        showToast(R.string.server_link_fault);
    }


    /***
     * 显示加载完成的数据
     *
     * @param msg
     */
    private void getNotInformation(String msg) {
        // TODO Auto-generated method stub
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setText(msg);
        listView.addFooterView(textView);

    }

    /***
     * 设置值
     *
     * @param dataJsonObject
     */
    protected void getValue(JSONObject dataJsonObject) {
        // TODO Auto-generated method stub
        String sign = dataJsonObject.optString("sign");
        String total = dataJsonObject.optString("total");
        String mile = dataJsonObject.optString("mile");
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
            param.put("walletType", "SCORE"); //  红包
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
        CreditActivity.creditsListener = null;
    }
}
