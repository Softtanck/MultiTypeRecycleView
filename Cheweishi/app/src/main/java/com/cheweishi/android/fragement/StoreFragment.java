package com.cheweishi.android.fragement;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.activity.MainNewActivity;
import com.cheweishi.android.activity.WashcarDetailsActivity;
import com.cheweishi.android.adapter.StoreCateGoryAdapter;
import com.cheweishi.android.adapter.StoreListAdapter;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.StoreListResponse;
import com.cheweishi.android.tools.EmptyTools;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.MyMapUtils;
import com.cheweishi.android.utils.ScreenUtils;
import com.cheweishi.android.widget.BackgroundDarkPopupWindow;
import com.cheweishi.android.widget.UnSlidingListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tangce on 7/6/2016.
 */
public class StoreFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, PullToRefreshListView.OnRefreshListener2 {

    private boolean isLoaded = false;

    private BackgroundDarkPopupWindow popupWindow;//弹框

    private View categoryView;//分类

    private UnSlidingListView usl_store_category;// 分类列表

    private StoreCateGoryAdapter cateGoryAdapter; // 分类适配器

    private LinearLayout ll_store_top_category; // 顶部分类

    private TextView tv_store_category_service, tv_store_category_sort;// 服务类别和排序

    private TextView tv_store_select;// 默认选择

    private ImageView iv_store_select;//默认图标

    private List<String> serviceData = new ArrayList<>();// 服务

    private List<String> sortData = new ArrayList<>();// 排序服务

    private PullToRefreshListView prl_store; // 租户列表

    private StoreListAdapter adapter; // 租户列表适配器

    private List<StoreListResponse.MsgBean> list = new ArrayList<>(); // 租户数据类表

    private int currentPage = 1; // 当前页面

    private int total; // 总共Item

    private boolean isSort = false;

    private String currentService = "2"; // 默认洗车

    private String currentSort = "DISTANCEASC"; // 默认距离

    private boolean isEmpty = false;//是否设置过空视图了

    private boolean isHeaderRefresh = false; // 是否为下拉刷新

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        ll_store_top_category = (LinearLayout) view.findViewById(R.id.ll_store_top_category);

        tv_store_category_service = (TextView) view.findViewById(R.id.tv_store_category_service);

        tv_store_category_sort = (TextView) view.findViewById(R.id.tv_store_category_sort);

        prl_store = (PullToRefreshListView) view.findViewById(R.id.prl_store);

        categoryView = View.inflate(baseContext, R.layout.store_service_category, null);

        usl_store_category = (UnSlidingListView) categoryView.findViewById(R.id.usl_store_category);

        tv_store_select = (TextView) view.findViewById(R.id.tv_store_select);

        iv_store_select = (ImageView) view.findViewById(R.id.iv_store_select);


        tv_store_category_service.setOnClickListener(this);
        tv_store_category_sort.setOnClickListener(this);
        cateGoryAdapter = new StoreCateGoryAdapter(baseContext, null, 1);
        usl_store_category.setAdapter(cateGoryAdapter);
        usl_store_category.setOnItemClickListener(this);

        adapter = new StoreListAdapter(baseContext, list);
        prl_store.setAdapter(adapter);
        prl_store.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        prl_store.setOnRefreshListener(this);
        prl_store.setOnItemClickListener(this);
    }

    private void onLoad() {
        isLoaded = true;
        initData();
    }

    private void initData() {
        serviceData.add("洗车服务");
        serviceData.add("保养服务");
        serviceData.add("美容服务");
        sortData.add("距离优先");
        sortData.add("好评优先");
        sortData.add("价格优先");
        sendPacket(0, "DISTANCEASC", "2");//默认加载洗车
    }


    /**
     * 1    保养
     * 2	洗车
     * 3	维修
     * 4	紧急救援
     * 5	美容
     *
     * @param type              0:显示全局对话框
     * @param sortType          距离
     * @param serviceCategoryId 服务id
     */
    private void sendPacket(int type, String sortType, String serviceCategoryId) {
        if (0 == type) { // 全局对话框
            ProgrosDialog.openDialog(baseContext);
        }

        String url = NetInterface.BASE_URL + NetInterface.TEMP_HOME_URL + NetInterface.LIST + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("latitude", MyMapUtils.getLatitude(baseContext.getApplicationContext()));//维度
        param.put("longitude", MyMapUtils.getLongitude(baseContext.getApplicationContext()));//经度

        param.put("serviceCategoryId", serviceCategoryId);
        param.put("sortType", sortType);
        param.put("pageSize", 5);
        param.put("pageNumber", currentPage);
        param.put(Constant.PARAMETER_TAG, NetInterface.LIST + "Store");
        netWorkHelper.PostJson(url, param, this);
    }

    @Override
    public void receive(String TAG, String data) {

        switch (TAG) {
            case NetInterface.LIST + "Store":
                StoreListResponse response = (StoreListResponse) GsonUtil.getInstance().convertJsonStringToObject(data, StoreListResponse.class);
                if (null == response || !response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    ProgrosDialog.closeProgrosDialog();
                    prl_store.onRefreshComplete();
                    showToast(response.getDesc());
                    return;
                }

                List<StoreListResponse.MsgBean> temp = response.getMsg();

                if (null != temp && 0 < temp.size()) {
                    total = response.getPage().getTotal();
                    if (isHeaderRefresh) {
                        list = temp;
                    } else {
                        list.addAll(temp);
                    }
                    if (list.size() < total) {
                        prl_store.onRefreshComplete();
                        prl_store.setMode(PullToRefreshBase.Mode.BOTH);
//                    else if (5 > list.size())
//                        prl_store.setMode(PullToRefreshBase.Mode.DISABLED);
                    } else {
                        prl_store.onRefreshComplete();
                        prl_store.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                    adapter.setData(list, currentService);
                    isEmpty = false;
                } else if (!isEmpty) {
                    isEmpty = true;
                    EmptyTools.setEmptyView(baseContext, prl_store);
                    EmptyTools.setImg(R.drawable.mycar_icon);
                    EmptyTools.setMessage("当前还没有租户信息");

                }

                loginResponse.setToken(response.getToken());
                break;
        }

        ProgrosDialog.closeProgrosDialog();
        prl_store.onRefreshComplete();
    }

    @Override
    public void error(String errorMsg) {
        ProgrosDialog.closeProgrosDialog();
        prl_store.onRefreshComplete();
        adapter.setData(list, currentService);
        showToast(R.string.server_link_fault);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getAdapter() instanceof StoreCateGoryAdapter) {// 搜索的一些条件
            if (isSort) {
                currentSort = sortData.get(position);
                switch (currentSort) {
                    case "距离优先":
                        currentSort = "DISTANCEASC";
                        break;
                    case "好评优先":
                        currentSort = "SCOREDESC";
                        break;
                    case "价格优先":
                        currentSort = "PRICEASC";
                        break;
                }
            } else {
                currentService = serviceData.get(position);
                /**
                 * 1    保养
                 * 2	洗车
                 * 3	维修
                 * 4	紧急救援
                 * 5	美容
                 */
                switch (currentService) {
                    case "洗车服务":
                        currentService = "2";
                        iv_store_select.setImageResource(R.drawable.xiche);
                        break;
                    case "保养服务":
                        currentService = "1";
                        iv_store_select.setImageResource(R.drawable.baoyang);
                        break;
                    case "美容服务":
                        currentService = "5";
                        iv_store_select.setImageResource(R.drawable.meirong);
                        break;
                }
            }

            dismissPopupWindow();
            currentPage = 1;
            list.clear();

            if ("PRICEASC".equals(currentSort))
                if ("5".equals(currentService) || "1".equals(currentService)) {
                    currentSort = "DISTANCEASC";
                    showToast("当前服务不支持价格优先筛选");
                }
            sendPacket(0, currentSort, currentService);
            showTtitl();
        } else {
            Intent intent = new Intent(baseContext, WashcarDetailsActivity.class);
            intent.putExtra("id", list.get(position - 1).getId());
            startActivity(intent);
        }
    }

    private void showTtitl() {
        String sort = null;
        String service = null;
        switch (currentSort) {
            case "DISTANCEASC":
                sort = "距离优先";
                break;
            case "SCOREDESC":
                sort = "好评优先";
                break;
            case "PRICEASC":
                sort = "价格优先";
                break;
        }
        switch (currentService) {
            case "2":
                service = "洗车服务";
                break;
            case "1":
                service = "保养服务";
                break;
            case "5": // 美容服务
                service = "美容服务";
                break;
        }
        tv_store_select.setText(service + " - " + sort);
    }


    @Override
    public void onDataLoading(int what) {
        if (0x2 == what) {
            onLoad();
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && !isLoaded)
            loading.sendEmptyMessage(0x2);
    }


    private void showPopup(View belowView, int type, List<String> data) {
        cateGoryAdapter.setData(data, type);
        if (null != popupWindow) {
            if (!popupWindow.isShowing()) // 不是正在展示的,重新展示
                popupWindow.showAsDropDown(belowView, 0, 1);
            return;
        }
        measureView(categoryView);
        popupWindow = new BackgroundDarkPopupWindow(categoryView, ScreenUtils.getScreenWidth(baseContext.getApplicationContext()), categoryView.getMeasuredHeight(), ((MainNewActivity) baseContext).getBottomHeight());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setDarkStyle(-1);
        popupWindow.setDarkColor(Color.parseColor("#a0000000"));
        popupWindow.darkFillScreen();
        popupWindow.darkBelow(belowView);
        popupWindow.showAsDropDown(belowView, 0, 1);
    }

    private void dismissPopupWindow() {
        if (null != popupWindow && popupWindow.isShowing())
            popupWindow.dismiss();
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        //headerView的宽度信息
        int childMeasureWidth = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
        int childMeasureHeight;
        if (lp.height > 0) {
            childMeasureHeight = View.MeasureSpec.makeMeasureSpec(lp.height, View.MeasureSpec.EXACTLY);
            //最后一个参数表示：适合、匹配
        } else {
            childMeasureHeight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);//未指定
        }
        //将宽和高设置给child
        child.measure(childMeasureWidth, childMeasureHeight);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_store_category_service: // 分类服务
                isSort = false;
                showPopup(ll_store_top_category, 1, serviceData);
                break;
            case R.id.tv_store_category_sort:// 排序
                isSort = true;
                showPopup(ll_store_top_category, 0, sortData);
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        dismissPopupWindow();
    }

    public void getDataForWashCar() {
        if (!isLoaded)
            return;
        currentService = "2";//洗车
        currentSort = "DISTANCEASC";
        list.clear();
        currentPage = 1;
        sendPacket(0, currentSort, currentService);
        tv_store_select.setText("洗车服务 - 距离优先");
    }


    @Override
    public void onPause() {
        super.onPause();
        dismissPopupWindow();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//        list.clear();
        isHeaderRefresh = true;
        currentPage = 1;
        sendPacket(1, currentSort, currentService);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
//        list.clear();
        currentPage++;
        isHeaderRefresh = false;
        sendPacket(1, currentSort, currentService);
    }
}
