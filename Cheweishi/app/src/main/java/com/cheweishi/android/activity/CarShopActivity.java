package com.cheweishi.android.activity;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.ShopCateGoryAdapter;
import com.cheweishi.android.adapter.ShopFragmentPagerAdapter;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.ShopTypeResponse;
import com.cheweishi.android.fragement.ShopPageFragment;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.ScreenUtils;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.BackgroundDarkPopupWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/21.
 */
public class CarShopActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {


    private Button left_action;

    private TextView title;

    private ViewPager vp_shops;//滚动

    private TabLayout tl_shop;//顶部导航

    private ShopFragmentPagerAdapter adapter;//适配器

    private LinearLayout ll_shop_top_down_array; // 顶部选择

    private BackgroundDarkPopupWindow mFilterWindow;//弹出PopuWindow

    private BackgroundDarkPopupWindow mSearchWindow;//弹出PopuWindow

    private View categoryView;//分类

    private View mSearchViewForInflate;//搜索视图

    private SearchView mSearchView;//搜索视图

    private ListView lv_shop_category;// 分类列表

    private ShopCateGoryAdapter cateGoryAdapter;//下拉分类适配

    private ImageView right_action;//右边查询


    private List<ShopTypeResponse.MsgBean> mTempData;

    private SparseArray<String> mFilterData = new SparseArray<>();//筛选条件数据

    private Toolbar toolbar;//顶部标题


//    private ShopTypeResponse response;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_shop);
        init();
    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        left_action = (Button) findViewById(R.id.left_action);

        title = (TextView) findViewById(R.id.title);

        vp_shops = (ViewPager) findViewById(R.id.vp_shop);

        tl_shop = (TabLayout) findViewById(R.id.tl_shop);

        right_action = (ImageView) findViewById(R.id.right_action);

        categoryView = View.inflate(baseContext, R.layout.shop_service_category, null);

        lv_shop_category = (ListView) categoryView.findViewById(R.id.lv_shop_category);

        mSearchViewForInflate = View.inflate(baseContext, R.layout.shop_top_search, null);

        mSearchView = (SearchView) mSearchViewForInflate.findViewById(R.id.sv_shop);

        cateGoryAdapter = new ShopCateGoryAdapter(baseContext, mFilterData);

        lv_shop_category.setAdapter(cateGoryAdapter);

        lv_shop_category.setOnItemClickListener(this);

        ll_shop_top_down_array = (LinearLayout) findViewById(R.id.ll_shop_top_down_array);

        mSearchView.setOnQueryTextListener(this);
        right_action.setOnClickListener(this);
        left_action.setOnClickListener(this);
        ll_shop_top_down_array.setOnClickListener(this);
        title.setText(getString(R.string.car_shop));
        left_action.setText(getString(R.string.back));
        mFilterData.put(0, "价格由低到高");
        mFilterData.put(1, "销量由高到低");
        mFilterData.put(2, "智能推荐");
        toolbar.setTitle("");// 标题的文字需在setSupportActionBar之前，不然会无效
        setSupportActionBar(toolbar);
        sendPacket();
    }

    @Override
    public void receive(String data) {
        ShopTypeResponse response = (ShopTypeResponse) GsonUtil.getInstance().convertJsonStringToObject(data, ShopTypeResponse.class);
        if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            ProgrosDialog.closeProgrosDialog();
            showToast(response.getDesc());
            return;
        }
        if (null == response.getMsg() || 0 >= response.getMsg().size()) {
            ProgrosDialog.closeProgrosDialog();
            return;
        }
        ll_shop_top_down_array.setVisibility(View.VISIBLE);
        mTempData = response.getMsg();
        adapter = new ShopFragmentPagerAdapter(getSupportFragmentManager(), baseContext, mTempData);
        vp_shops.setAdapter(adapter);
        if (5 <= mTempData.size())
            tl_shop.setTabMode(TabLayout.MODE_SCROLLABLE);
        tl_shop.setupWithViewPager(vp_shops);
        loginResponse.setToken(response.getToken());
    }

    private void sendPacket() {
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_SHOP + NetInterface.GET_SHOP_CATEGORY + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        netWorkHelper.PostJson(url, param, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_action:
                finish();
                break;
            case R.id.ll_shop_top_down_array:
                showFilterPopup(tl_shop, mFilterData);
                break;
            case R.id.right_action: // search
//                doSearch("");
                showSearchPopup(toolbar);
                break;
        }
    }

    private void doSearch(String key) {
        dismissPopupWindow();
        if(null == mTempData)
            return;
        ShopTypeResponse.MsgBean type = new ShopTypeResponse.MsgBean();
        type.setId(-1);
        type.setName(key);
        mTempData.add(type);
        adapter.setData(mTempData);
        if (5 <= mTempData.size())
            tl_shop.setTabMode(TabLayout.MODE_SCROLLABLE);
        vp_shops.setCurrentItem(mTempData.size() - 1);
    }

    private void showFilterPopup(View belowView, SparseArray<String> data) {
        cateGoryAdapter.setData(data);
        if (null != mFilterWindow) {
            if (!mFilterWindow.isShowing()) // 不是正在展示的,重新展示
                mFilterWindow.showAsDropDown(belowView, 0, 1);
            return;
        }
        measureView(categoryView);
        int height = data.size() * categoryView.getMeasuredHeight() > ScreenUtils.getScreenHeight(baseContext.getApplicationContext()) / 2 ? ScreenUtils.getScreenHeight(baseContext.getApplicationContext()) / 2 : data.size() * categoryView.getMeasuredHeight();
        mFilterWindow = new BackgroundDarkPopupWindow(categoryView, ScreenUtils.getScreenWidth(baseContext.getApplicationContext()), height, 0);
        mFilterWindow.setFocusable(true);
        mFilterWindow.setOutsideTouchable(true);
        mFilterWindow.setBackgroundDrawable(new BitmapDrawable());
        mFilterWindow.setDarkStyle(-1);
        mFilterWindow.setDarkColor(Color.parseColor("#a0000000"));
        mFilterWindow.darkFillScreen();
        mFilterWindow.darkBelow(belowView);
        mFilterWindow.showAsDropDown(belowView, 0, 1);
    }

    private void showSearchPopup(View belowView) {
        if (null != mSearchWindow) {
            if (!mSearchWindow.isShowing()) // 不是正在展示的,重新展示
                mSearchWindow.showAsDropDown(belowView, 0, 1);
//                mSearchWindow.showAtLocation(belowView, Gravity.TOP, 0, 0);
            return;
        }
        measureView(mSearchViewForInflate);
        mSearchWindow = new BackgroundDarkPopupWindow(mSearchViewForInflate, ScreenUtils.getScreenWidth(baseContext.getApplicationContext()), mSearchViewForInflate.getMeasuredHeight(), 0);
        mSearchWindow.setFocusable(true);
        mSearchWindow.setOutsideTouchable(true);
        mSearchWindow.setBackgroundDrawable(new BitmapDrawable());
        mSearchWindow.setDarkStyle(-1);
        mSearchWindow.setDarkColor(Color.parseColor("#a0000000"));
        mSearchWindow.darkFillScreen();
        mSearchWindow.darkBelow(toolbar);
        mSearchWindow.showAsDropDown(belowView, 0, 1);
//        mSearchWindow.showAtLocation(belowView, Gravity.TOP, 0, 0);
    }

    private void dismissPopupWindow() {
        if (null != mSearchWindow && mSearchWindow.isShowing()) {
            mSearchWindow.dismiss();
            mSearchView.setQuery(null, false);
        } else if (null != mFilterWindow && mFilterWindow.isShowing())
            mFilterWindow.dismiss();
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
    public void onPause() {
        super.onPause();
        dismissPopupWindow();
    }

    @Override
    public void onStop() {
        super.onStop();
        dismissPopupWindow();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogHelper.d("onItemClick:" + position);
        if (null != mFilterData && position <= (mFilterData.size() - 1)) {
            ((ShopPageFragment) adapter.getItem(vp_shops.getCurrentItem())).setSortType(position);
        }
        dismissPopupWindow();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        doSearch(query.trim());
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return null != newText && 10 < newText.length();
    }


}
