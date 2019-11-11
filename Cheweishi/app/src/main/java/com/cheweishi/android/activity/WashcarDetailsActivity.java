package com.cheweishi.android.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.ExpandableListViewAdapter;
import com.cheweishi.android.adapter.WashCarCommentAdapter;
import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.ServiceDetailResponse;
import com.cheweishi.android.entity.UserCommentNative;
import com.cheweishi.android.tools.LoginMessageUtils;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.MyMapUtils;
import com.cheweishi.android.utils.ScreenUtils;
import com.cheweishi.android.widget.BaiduMapView;
import com.cheweishi.android.widget.PullScrollView;
import com.cheweishi.android.widget.UnSlidingExpandableListView;
import com.cheweishi.android.widget.UnSlidingListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nineoldandroids.view.ViewHelper;

/**
 * 商家详情
 *
 * @author Xiaojin
 */
public class WashcarDetailsActivity extends BaseActivity implements
        View.OnClickListener, ExpandableListView.OnChildClickListener, PullScrollView.OnScrollListener {
    public static final String TAG = "WashcarDetailsActivity";
    /**
     * 详情界面
     */
    public static final int INDEX_DETAIL = 1001;
    /**
     * 订单详情
     */
    public static final int INDEX_ORDER_DETAIL = 1002;

    /**
     * 来自地图
     */
    public static final int INDEX_FROM_MAP = 1003;

    /**
     * 来自列表
     */
    public static final int INDEX_FROM_LIST = 1004;
    /**
     * 来自历史记录
     */
    public static final int INDEX_FROM_HISTORY = 1005;

    /**
     * 来自我的订单列表预约中
     */
    public static final int INDEX_FROM_ING = 1006;

    private int tempHead = 0;//顶部距离

    @ViewInject(R.id.include_tenant_title)
    private View title; // 顶部视图
    @ViewInject(R.id.title)
    private TextView tvTitle;
    @ViewInject(R.id.left_action)
    private Button btnLeft;
    @ViewInject(R.id.car_iv_location)
    private ImageView car_iv_location;
    @ViewInject(R.id.tv_time)
    private TextView tv_time;
    @ViewInject(R.id.tv_time_interval)
    private TextView tv_time_interval;
    @ViewInject(R.id.car_xlocation)
    private TextView car_xlocation;
    @ViewInject(R.id.img_maintain_phone)
    private ImageView img_maintain_phone;
    @ViewInject(R.id.img_maintain_ditu)
    private ImageView img_maintain_ditu;
    @ViewInject(R.id.lv_washcar_pinglun)
    private UnSlidingListView lv_washcar_pinglun;
    @ViewInject(R.id.lv_washcar_detils)
    private UnSlidingExpandableListView lv_washcar_detils;
    @ViewInject(R.id.rel_user_comm)
    private RelativeLayout rel_user_comm;
    @ViewInject(R.id.car_tv_car_iv_location)
    private TextView car_tv_car_iv_location;
    @ViewInject(R.id.tv_phone)
    private TextView tv_phone;
    @ViewInject(R.id.psl_tenant_detail)
    private PullScrollView psl_tenant_detail; // 下拉控件
    @ViewInject(R.id.iv_tenant_detail)
    private ImageView iv_tenant_detail;//下拉大图
    @ViewInject(R.id.ll_tenant_title)
    private LinearLayout ll_tenant_title;//隐藏的返回
    private List<UserCommentNative> comments;
    private WashCarCommentAdapter commentAdapter;
    private ExpandableListViewAdapter exListAdapter;
    String id = "";
    ServiceDetailResponse washCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_washcar_malldetails);
        ViewUtils.inject(this);
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        title.setVisibility(View.GONE);
        psl_tenant_detail.setHeader(iv_tenant_detail);
        psl_tenant_detail.setOnScrollListener(this);
        // TODO 暂时没有评论
//        comments = new ArrayList<UserCommentNativeNative>();
//        commentAdapter = new WashCarCommentAdapter(this, comments);
//        lv_washcar_pinglun.setAdapter(commentAdapter);
        tvTitle.setText("商家详情");
        btnLeft.setText("返回");
        id = String.valueOf(getIntent().getIntExtra("id", 0));
        getHistoryData();

    }

    // 设置品牌group点击无事件，让expandableListView可以保持一直打开的状态
    private OnGroupClickListener onGroupClickListener = new OnGroupClickListener() {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v,
                                    int groupPosition, long id) {
            return true;
        }
    };

    /**
     * 请求服务器，获取商家详情
     */
    private void getHistoryData() {
        if (!isLogined()) {
            return;
        }
        ProgrosDialog.openDialog(this);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_HOME_URL + NetInterface.GET_TENANT_INFO + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("tenantId", id);
        netWorkHelper.PostJson(url, param, this);

    }

    @Override
    public void receive(String data) {
        ProgrosDialog.closeProgrosDialog();

        washCar = (ServiceDetailResponse) GsonUtil.getInstance().convertJsonStringToObject(data, ServiceDetailResponse.class);
        if (!washCar.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            showToast(washCar.getDesc());
//            psl_tenant_detail.scrollTo(0,0);
            psl_tenant_detail.smoothScrollTo(0, 0);
            return;
        }

        setData();
//        psl_tenant_detail.scrollTo(0,0);
        psl_tenant_detail.smoothScrollTo(0, 0);
        loginResponse.setToken(washCar.getToken());
        LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
    }

    @Override
    public void error(String errorMsg) {
        ProgrosDialog.closeProgrosDialog();
        showToast(R.string.server_link_fault);
    }


    @OnClick(R.id.left_action)
    public void finishActivity(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * 获取从服务器解析出的数据对象并填充视图
     */
    private void setData() {
        XUtilsImageLoader.getxUtilsImageLoader(this, R.drawable.udesk_defalut_image_loading, R.drawable.udesk_defualt_failure,
                iv_tenant_detail, washCar.getMsg().getPhoto());
        XUtilsImageLoader.getxUtilsImageLoader(this, R.drawable.udesk_defalut_image_loading, R.drawable.udesk_defualt_failure,
                car_iv_location, washCar.getMsg().getPhoto());
        car_tv_car_iv_location.setText(washCar.getMsg().getTenantName());
        if (null != washCar.getMsg().getBusinessTime())
            tv_time_interval.setText(String.valueOf(washCar.getMsg().getBusinessTime()));
        if (null != washCar.getMsg().getAddress())
            car_xlocation.setText(String.valueOf(washCar.getMsg().getAddress()));
        exListAdapter = new ExpandableListViewAdapter(this,
                washCar.getMsg(), washCar.getMsg().getTenantName());
        lv_washcar_detils.setOnGroupClickListener(onGroupClickListener);
        lv_washcar_detils.setAdapter(exListAdapter);
        lv_washcar_detils.setOnChildClickListener(this);
        for (int i = 0; i < washCar.getMsg().getCarServices().size(); i++) {
            lv_washcar_detils.expandGroup(i);
        }
        tempHead = getHeight(exListAdapter) - (ScreenUtils.getScreenHeight(baseContext) - psl_tenant_detail.getChildAt(0).getHeight());
//        LogHelper.d("height:" + tempHead);
        tempHead = 0 < tempHead ? tempHead : 0;
        tempHead /= 2;
//        LogHelper.d("height:" + tempHead);
    }

    private int getHeight(ExpandableListViewAdapter adapter) {
        int totalHeight = 0;
        for (int i = 0; i < adapter.getGroupCount(); i++) {   //listAdapter.getCount()返回数据项的数目
            View groupItem = adapter.getGroupView(i, true, null, lv_washcar_detils);
            groupItem.measure(0, 0);
            totalHeight += groupItem.getMeasuredHeight();
            for (int j = 0; j < adapter.getChildrenCount(i); j++) {
                View listItem = adapter.getChildView(i, j, false, null, lv_washcar_detils);
                listItem.measure(0, 0);  //计算子项View 的宽高
                totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
            }
        }
        return totalHeight;
    }

    @OnClick({R.id.left_action, R.id.rel_user_comm, R.id.img_maintain_phone,
            R.id.img_maintain_ditu, R.id.car_iv_location, R.id.ll_tenant_title, R.id.iv_tenant_detail})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_user_comm:// 评论
                Intent intent = new Intent(WashcarDetailsActivity.this,
                        AllCommentActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_tenant_title:
            case R.id.left_action:
                finish();
                break;
            case R.id.img_maintain_phone:// 拨打电话
                turnToPhone();
                break;
            case R.id.img_maintain_ditu:// 导航
                turnToNav();
                break;
            case R.id.iv_tenant_detail:
            case R.id.car_iv_location://商家头像
                if (null != washCar && null != washCar.getMsg().getTenantImages() && 0 >= washCar.getMsg().getTenantImages().size()) {
                    return;
                }
                Intent tenantHead = new Intent(baseContext, TenantHeadImgActivity.class);
                tenantHead.putExtra("data", washCar);
                startActivity(tenantHead);
                break;
            default:
                break;
        }
    }

    /**
     * 导航
     */
    private void turnToNav() {
        BaiduMapView baiduMapView = new BaiduMapView();
        baiduMapView.initMap(this);
        baiduMapView.baiduNavigation(MyMapUtils.getLatitude(this),
                MyMapUtils.getLongitude(this), MyMapUtils.getAddress(this),
                washCar.getMsg().getLatitude(), washCar.getMsg().getLongitude(), String.valueOf(washCar.getMsg().getAddress()));
    }

    /**
     * 拨打电话
     */
    public void turnToPhone() {
        Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                + washCar.getMsg().getContactPhone()));
        tel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(tel);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        //  TODO 增加一个新的页面
        String desc = washCar.getMsg().getCarServices().get(groupPosition).getSubServices().get(childPosition).getServiceDesc();
        String img = washCar.getMsg().getCarServices().get(groupPosition).getSubServices().get(childPosition).getImgPath();
//        if (null == img && null == desc)
//            return false;
        LogHelper.d("onChildClick:" + washCar.getMsg().getCarServices().get(groupPosition).getSubServices().get(childPosition).getServiceName());
        Intent intent = new Intent(baseContext, ServiceDetailActivity.class);
        intent.putExtra("img", img);
        intent.putExtra("desc", desc);
        startActivity(intent);
        return true;
    }

    @Override
    public void onScroll(int y) {
//        LogHelper.d("onScroll:" + y);
        if (0 != y) { // 向上滚动
            title.setVisibility(View.VISIBLE);
            float alpha = y * 1.0f / tempHead;
            if (1.0f < alpha)
                alpha = 1.0f;
            else if (0.0f > alpha)
                alpha = 0.0f;
            else if (0.0f < alpha && 1.0f > alpha)
                ll_tenant_title.setVisibility(View.GONE);
            ViewHelper.setAlpha(title, alpha);
        } else if (0 == y) {
            title.setVisibility(View.GONE);
            ll_tenant_title.setVisibility(View.VISIBLE);
//            iv_tenant_title.setVisibility(View.VISIBLE);
        }
    }
}
