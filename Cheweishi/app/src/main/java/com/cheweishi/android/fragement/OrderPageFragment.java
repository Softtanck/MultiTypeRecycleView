package com.cheweishi.android.fragement;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.ShopOrderListAdapter;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.ShopOrderListResponse;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.tools.EmptyTools;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.CustomDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrderPageFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2, ShopOrderListAdapter.OrderProcessListener {

    public static final String ARG_ID = "ARG_ID";

    private int mPage;

    // 标志位，标志已经初始化完成。
    private boolean isPrepared;

    private boolean isLoaded;//是否已经联网加载过数据了

    private PullToRefreshListView listView;

    private int currentPage = 1;

    private int currentId;

    private List<ShopOrderListResponse.MsgBean> list = new ArrayList<>();

    private ShopOrderListResponse.MsgBean bean;

    private ShopOrderListAdapter adapter;

    private int total;

    private boolean isHeadRefresh = false;

    private boolean isFirstLoad = false;

    private boolean isEmpty;

    private int currentOrderId = -1;//当前订单Id

    private boolean isRefresh;

    private static boolean newRefresh = false;

    @Override
    public void onResume() {
        super.onResume();
        if (!isRefresh) {
            isRefresh = true;
        } else {
            if (newRefresh) {//防止重复发包
                newRefresh = !newRefresh;
                return;
            }
            newRefresh = true;
            // 重新连接
            list.clear();
            reLoad();
        }
    }

    public static OrderPageFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        OrderPageFragment pageFragment = new OrderPageFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentId = getArguments().getInt(ARG_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_shop_order_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (PullToRefreshListView) view.findViewById(R.id.prl_order);
        adapter = new ShopOrderListAdapter(baseContext, list);
        adapter.setListener(this);
        listView.setAdapter(adapter);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listView.setOnRefreshListener(this);
//        listView.setOnItemClickListener(this);
        isPrepared = true;
        onVisible();
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        if (!isPrepared || !isVisible) {//|| isLoaded
            return;
        }
//        isLoaded = true;
//        LogHelper.d("onVisible:" + mPage);

        loading.sendEmptyMessageDelayed(0x10, 500);
    }

    @Override
    public void onDataLoading(int what) {

        if (0x10 == what && !isLoaded) {
            isLoaded = true;
            isFirstLoad = true;//用于第一次加载获取list的total
            sendPacket(0);
        } else if (0 == list.size()) {
            EmptyTools.setEmptyView(baseContext, listView);
            EmptyTools.setImg(R.drawable.mycar_icon);
            EmptyTools.setMessage("当前列表没有订单信息");
        }
    }

    /**
     * 退货
     *
     * @param orderId
     * @param orderItemId
     */
    private void applyReturns(int orderId, String orderItemId) {
        String[] temp;
        if (StringUtil.isEmpty(orderItemId))
            return;
        else if (orderItemId.contains(","))
            temp = orderItemId.split(",");
        else {
            temp = new String[1];
            temp[0] = orderItemId;
        }
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_ORDER_CREATE + NetInterface.RETURN + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("orderId", orderId);
        param.put("orderItemIds", temp);
        param.put(Constant.PARAMETER_TAG, NetInterface.RETURN);
        netWorkHelper.PostJson(url, param, this);
    }

    /**
     * 确认收货 or 取消订单
     *
     * @param isConfirm
     * @param orderId
     */
    private void cancelOrConfirm(boolean isConfirm, int orderId) {
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_ORDER_CREATE + NetInterface.OPT + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("orderId", orderId);
        if (isConfirm)
            param.put("oprType", "received");
        else
            param.put("oprType", "cancel");
        param.put(Constant.PARAMETER_TAG, NetInterface.OPT);
        netWorkHelper.PostJson(url, param, this);
    }

    /**
     * 订单列表
     *
     * @param type
     */
    private void sendPacket(int type) {
        if (0 == type)
            ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_ORDER_CREATE + NetInterface.LIST + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("pageNumber", currentPage);
        param.put("pageSize", 5);
        param.put("status", currentId);
        netWorkHelper.PostJson(url, param, this);
    }

    @Override
    public void receive(String TAG, String data) {
        switch (TAG) {
            case NetInterface.OPT://取消.确认
                BaseResponse baseResponse = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
                if (!baseResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    ProgrosDialog.closeProgrosDialog();
                    showToast(baseResponse.getDesc());
                    return;
                }
                loginResponse.setToken(baseResponse.getToken());
                ProgrosDialog.closeProgrosDialog();
                reLoad();
                break;
            case NetInterface.RETURN://退货
                BaseResponse backResponse = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
                if (!backResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    ProgrosDialog.closeProgrosDialog();
                    showToast(backResponse.getDesc());
                    return;
                }


                loginResponse.setToken(backResponse.getToken());
                ProgrosDialog.closeProgrosDialog();
                reLoad();
                break;

        }
    }

    @Override
    public void receive(String data) {

        ShopOrderListResponse response = (ShopOrderListResponse) GsonUtil.getInstance().convertJsonStringToObject(data, ShopOrderListResponse.class);

        if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            ProgrosDialog.closeProgrosDialog();
            showToast(response.getDesc());
            listView.onRefreshComplete();
            return;
        }
        List<ShopOrderListResponse.MsgBean> temp = response.getMsg();

        if (null != temp && 0 < temp.size()) {
//            EmptyTools.hintEmpty();
            if (isHeadRefresh) {
                total = response.getPage().getTotal();
                list = handleList(temp);
            } else {
                if (isFirstLoad) {
                    total = response.getPage().getTotal();
                    isFirstLoad = !isFirstLoad;
                }
                list.addAll(handleList(temp));
            }
            isEmpty = false;
        } else if (!isEmpty) { // 已经添加了
            isEmpty = true;
            list = handleList(temp);
//            adapter.setData(list);
            EmptyTools.setEmptyView(baseContext, listView);
            EmptyTools.setImg(R.drawable.mycar_icon);
            EmptyTools.setMessage("当前列表没有订单信息");
        }
        adapter.setData(list);
        loginResponse.setToken(response.getToken());
        ProgrosDialog.closeProgrosDialog();
        if (list.size() < total) {
            listView.onRefreshComplete();
            listView.setMode(PullToRefreshBase.Mode.BOTH);
        } else {
            listView.onRefreshComplete();
            listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        }
    }

    /**
     * 拆分订单
     *
     * @param list
     * @return
     */
    private List<ShopOrderListResponse.MsgBean> handleList(List<ShopOrderListResponse.MsgBean> list) {
        if (null == list || 0 == list.size())
            return list;
        List<ShopOrderListResponse.MsgBean> temp = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ++total;
            bean = list.get(i);
            bean.setType(1);
            temp.add(bean);//先add一个Head
            for (int j = 0; j < list.get(i).getOrderItem().size(); j++) {
                ++total;
                bean = (ShopOrderListResponse.MsgBean) bean.clone();
                bean.setType(2);
                bean.setOrderPosition(j);
                temp.add(bean);
            }
//            ++total;
            bean = (ShopOrderListResponse.MsgBean) bean.clone();
            bean.setType(3);
            temp.add(bean);
        }
        return temp;
    }

    @Override
    public void error(String errorMsg) {
        ProgrosDialog.closeProgrosDialog();
        listView.onRefreshComplete();
        showToast(R.string.server_link_fault);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        reLoad();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        currentPage++;
        isHeadRefresh = false;
        sendPacket(1);
    }

    @Override
    public void onCancel(int position) {
        currentOrderId = list.get(position).getId();
        showDeleteDialog(getString(R.string.cancel_order_desc), getString(R.string.confirm_order_cancel));
    }

    @Override
    public void onReceive(int position) {
        currentOrderId = list.get(position).getId();
        cancelOrConfirm(true, currentOrderId);
    }

    @Override
    public void onBackGoods(int position) {
        currentOrderId = list.get(position).getId();
        applyReturns(currentOrderId, adapter.getOrderId(position));
    }

    private void reLoad() {
        currentPage = 1;
        isHeadRefresh = true;
        sendPacket(1);
    }

    private void showDeleteDialog(String msg, String buttonMsg) {
        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
        builder.setMessage(msg);
        builder.setTitle(getString(R.string.remind));

        builder.setPositiveButton(buttonMsg,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (-1 == currentOrderId) {
                            showToast(R.string.cancel_error);
                            return;
                        }

                        cancelOrConfirm(false, currentOrderId);
                    }
                });
        builder.setNegativeButton(getString(R.string.cancel_order_cancel),
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }
}
