package com.cheweishi.android.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cheweishi.android.R;
import com.cheweishi.android.activity.ProductDetailActivity;
import com.cheweishi.android.adapter.ShopListAdapter;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.ShopListResponse;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.StringUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShopPageFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2, ShopListAdapter.OnRecyclerViewItemClickListener {

    public static final String ARG_PAGE = "ARG_PAGE";

    public static final String ARG_ID = "ARG_ID";

    private static final String ARG_NAME = "ARG_NAME";

    private String currentName;

    private int mPage;

    // 标志位，标志已经初始化完成。
    private boolean isPrepared;

    private boolean isLoaded;//是否已经联网加载过数据了

    private PullToRefreshRecyclerView recyclerView;

    private LinearLayout fl_shop_page_empty;//空布局

    private int currentPage = 1;

    private int currentId;

    private List<ShopListResponse.MsgBean> list = new ArrayList<>();

    private ShopListAdapter adapter;

    private int total;

    private boolean isHeadRefresh = false;

    private boolean isEmpty;

    private String mSortType;//当前排序类型

    private String mKeyWord;//当前关键字


    public static ShopPageFragment newInstance(int page, int id, String name) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putInt(ARG_ID, id);
        args.putString(ARG_NAME, name);
        ShopPageFragment pageFragment = new ShopPageFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        currentId = getArguments().getInt(ARG_ID);
        currentName = getArguments().getString(ARG_NAME);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fl_shop_page_empty = (LinearLayout) view.findViewById(R.id.fl_shop_page_empty);
        recyclerView = ((PullToRefreshRecyclerView) ((RelativeLayout) view).getChildAt(1));
        adapter = new ShopListAdapter(baseContext, list);
        adapter.setOnItemClickListener(this);
        recyclerView.getRefreshableView().setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.getRefreshableView().setAdapter(adapter);
        recyclerView.getRefreshableView().setEmptyView(fl_shop_page_empty);
        recyclerView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        recyclerView.setOnRefreshListener(this);
        recyclerView.getRefreshableView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) { //滑动的时候对RecycleView的优化
                if (newState == RecyclerView.SCROLL_STATE_IDLE) { // 滑动的时候暂停加载
                    Picasso.with(baseContext).resumeTag(baseContext);
                } else {
                    Picasso.with(baseContext).pauseTag(baseContext);
                }
            }
        });
        isPrepared = true;
        onVisible();
    }

    @Override
    protected void onVisible() {
        super.onVisible();
        if (!isPrepared || !isVisible) {//|| isLoaded
            return;
        }
        loading.sendEmptyMessageDelayed(0x10, 500);
    }

    @Override
    public void onDataLoading(int what) {

        if (0x10 == what && !isLoaded) {
            isLoaded = true;
            if (-1 == currentId)
                mKeyWord = currentName;
            sendPacket(0, currentId, mSortType, mKeyWord);
        } else if (0 == list.size()) {
//            EmptyTools.setEmptyView(baseContext, recyclerView);
//            EmptyTools.setImg(R.drawable.mycar_icon);
//            EmptyTools.setMessage("当前列表没有商品信息");
        }
        ProgrosDialog.closeProgrosDialog();
    }

    private void sendPacket(int type, int categoryId, String sortType, String keyWord) {
        if (0 == type)
            ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_SHOP + NetInterface.LIST + NetInterface.SUFFIX;
        Map<String, Object> params = new HashMap<>();
        params.put("userId", loginResponse.getDesc());
        params.put("token", loginResponse.getToken());
        if (-1 != categoryId)
            params.put("categoryId", categoryId);
        if (!StringUtil.isEmpty(sortType))
            params.put("sortType", sortType);
        if (!StringUtil.isEmpty(keyWord))
            params.put("searchKeyWord", keyWord);
        params.put("pageNumber", currentPage);
        params.put("pageSize", 10);
        netWorkHelper.PostJson(url, params, this);
    }


    @Override
    public void receive(String data) {
        ShopListResponse response = (ShopListResponse) GsonUtil.getInstance().convertJsonStringToObject(data, ShopListResponse.class);

        if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            ProgrosDialog.closeProgrosDialog();
            showToast(response.getDesc());
            recyclerView.onRefreshComplete();
            return;
        }
        List<ShopListResponse.MsgBean> temp = response.getMsg();

        if (null != temp && 0 < temp.size()) {
//            EmptyTools.hintEmpty();
            total = response.getPage().getTotal();
            if (isHeadRefresh) {
                list = temp;
            } else {
                list.addAll(temp);
            }
            isEmpty = false;
        } else if (!isEmpty) { // 已经添加了
            isEmpty = true;
            list = temp;
        }

        adapter.setData(list);
        loginResponse.setToken(response.getToken());
        ProgrosDialog.closeProgrosDialog();
        if (list.size() < total) {
            recyclerView.onRefreshComplete();
            recyclerView.setMode(PullToRefreshBase.Mode.BOTH);
        } else {
            recyclerView.onRefreshComplete();
            recyclerView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        }

    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        currentPage = 1;
//        list.clear();
        isHeadRefresh = true;
        if (-1 == currentId)
            mKeyWord = currentName;
        sendPacket(1, currentId, mSortType, mKeyWord);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        currentPage++;
        isHeadRefresh = false;
        if (-1 == currentId)
            mKeyWord = currentName;
        sendPacket(1, currentId, mSortType, mKeyWord);
    }


    @Override
    public void onItemClick(View view, int position) {
        Intent detail = new Intent(baseContext, ProductDetailActivity.class);
        detail.putExtra("productId", list.get(position).getId());
        startActivity(detail);
    }

    public void setSortType(int p) {
        switch (p) {
            case 0:
                mSortType = "PRICEASC";
                break;
            case 1:
                mSortType = "SALESDESC";
                break;
            case 2:
                mSortType = "RECOMMEND";
                break;
        }
        isHeadRefresh = true;
        sendPacket(0, currentId, mSortType, mKeyWord);
    }
}
