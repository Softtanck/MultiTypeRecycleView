package com.cheweishi.android.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.cheweishi.android.R;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.ParkInfoNative;
import com.cheweishi.android.entity.SearchResponse;
import com.cheweishi.android.fragement.BaseFragment;
import com.cheweishi.android.fragement.FindParkingSpaceListFragment;
import com.cheweishi.android.fragement.FindParkingSpaceMapFragment;
import com.cheweishi.android.tools.LoginMessageUtils;
import com.cheweishi.android.utils.DisplayUtil;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.MyMapUtils;
import com.cheweishi.android.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 找车位
 *
 * @author 大森
 */
@ContentView(R.layout.activity_find_parking_space)
public class FindParkingSpaceActivity extends BaseActivity {

    @ViewInject(R.id.left_action)
    private TextView left_action;

    @ViewInject(R.id.title)
    private TextView title;

    @ViewInject(R.id.right_action)
    private ImageView right_action;

    @ViewInject(R.id.ll_map)
    private LinearLayout ll_map;

    @ViewInject(R.id.rb_map)
    private TextView tv_map;

    @ViewInject(R.id.ll_list)
    private LinearLayout ll_list;

    @ViewInject(R.id.rb_list)
    private TextView tv_list;

    @ViewInject(R.id.fl_find_parking_space)
    private FrameLayout fl_find_parking_space;// fragment容器

    private static final String PROVINCE = "重庆市";
    private final int SEARCH_TYPE_CODE = 8080; // 跳转搜索界面

    private int showState = 0;// 界面显示状态，0表示显示地图 1表示显示列表

    private int RIGHT_ACTION_STATE = 0;//头部右边按钮的状态 0表示搜索背景 1表示删除背景
    private LatLng latLng;// 经纬度
    private String type;// 数据请求类型
    private final int FIND_PARK_CODE = 1808;// 数据返回参数
    private List<ParkInfoNative> listmMaps;// 地图数据

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private BaseFragment baseFragment;
    private BaseFragment mapFragment;
    private BaseFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
//        httpBiz = new HttpBiz(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        left_action.setText(R.string.back);
        title.setText("附近车位");
        right_action.setImageResource(R.drawable.zhaochewei_sousuo);
        latLng = MyMapUtils.getLatLng(getApplicationContext());
        mapFragment = new FindParkingSpaceMapFragment();
        baseFragment = mapFragment;
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
//		transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fl_find_parking_space, baseFragment);
        transaction.commit();
        moveToPerson();
//		commitFragment();
    }
//	/**
//	 * 提交fragment
//	 */
//	private void commitFragment() {
//		if (StringUtil.isEmpty(fragmentManager)) {
//			fragmentManager = getSupportFragmentManager();
//		}
//		transaction = fragmentManager.beginTransaction();
//		transaction.setCustomAnimations(android.R.anim.fade_in,
//				android.R.anim.fade_out);
//		transaction = fragmentManager.beginTransaction();
//		Bundle data = new Bundle();
//		data.putParcelableArrayList("data",
//				(ArrayList<? extends Parcelable>) listmMaps);
//		baseFragment.setArguments(data);
//		transaction.add(R.id.fl_find_parking_space, baseFragment);
//		transaction.commit();
//	}

    protected void moveToPerson() {
        // isDefult = false;
        // moveTolocation();
        if (MyMapUtils.getProvince(getApplicationContext()) != null
                && MyMapUtils.getProvince(getApplicationContext()).equals(PROVINCE)) {
            type = "cq";
        } else {
            type = "qt";
        }
        request();
    }

    @OnClick({R.id.left_action, R.id.right_action, R.id.ll_map, R.id.ll_list})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_action:// 返回
                this.finish();
                break;

            case R.id.right_action:// 搜索
                showSearchTitle();
                RIGHT_ACTION_STATE = 0;

                break;
            case R.id.ll_map:// 地图
                right_action.setVisibility(View.VISIBLE);
                showState = 0;
                showBtnStatuc(showState);
                baseFragment = new FindParkingSpaceMapFragment();
//			if (baseFragment != mapFragment) {
//				switchFragment(showState);
//			} else {// 当前是listFragment，因此，只需要将listFragment出栈即可变成mapFragment
//			// fragmentManager.popBackStack();
//				baseFragment = mapFragment;
//			}
                switchFragment(baseFragment);
                break;
            case R.id.ll_list:// 列表
                right_action.setVisibility(View.INVISIBLE);
                showState = 1;
                showBtnStatuc(showState);
                baseFragment = new FindParkingSpaceListFragment();
                switchFragment(baseFragment);
//			if (baseFragment != listFragment) {
//				switchFragment(showState);
//			} else {// 当前是mapFragment，因此，只需要将mapFragment出栈即可变成listFragment
//			// fragmentManager.popBackStack();
//				baseFragment = listFragment;
//			}

                break;
            default:
                break;
        }
    }

    /**
     * 搜索返回title显示
     */
    private void showSearchTitle() {
        if (RIGHT_ACTION_STATE == 0) {
            searchParking();
        } else if (RIGHT_ACTION_STATE == 1) {
            right_action.setImageResource(R.drawable.zhaochewei_sousuo);
            left_action.setText(R.string.back);
            title.setCompoundDrawables(null, null, null, null);
            title.setBackgroundResource(R.color.white);
            title.setText("附近车位");
            title.setGravity(Gravity.CENTER);
            title.setTextColor(getResources().getColor(
                    R.color.orange));
            title.setTextSize(20);
            latLng = MyMapUtils.getLatLng(getApplicationContext());
            request();
        }

    }

    /**
     * 显示搜索title
     */
    private void showSearchTitle1() {
        right_action.setImageResource(R.drawable.shan2x);
        left_action.setText("");
        Drawable sousuo = getResources().getDrawable(R.drawable.zhaochewei_sousuo);
        sousuo.setBounds(0, 0, sousuo.getMinimumWidth(), sousuo.getIntrinsicHeight());
        title.setCompoundDrawables(sousuo, null, null, null);
        title.setCompoundDrawablePadding(DisplayUtil.dip2px(this, 5));
        title.setBackgroundResource(R.drawable.zhaochewei_kuang_white);
        title.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        title.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        title.setTextColor(getResources().getColor(
                R.color.gray_pressed));
        title.setTextSize(14);
    }

    /**
     * 搜索操作
     */
    private void searchParking() {
        Intent intent = new Intent(FindParkingSpaceActivity.this,
                SearchActivity.class);
        intent.putExtra("hint", "搜索目的地附近车位");
        intent.putExtra("type", "zcw");
        startActivityForResult(intent, SEARCH_TYPE_CODE);
    }

    /**
     * 切换Fragment
     */
    private void switchFragment(BaseFragment baseFragment) {
        Log.i("result", "====showState==========" + showState);
//		if (showState == 0) {
//			if (null == mapFragment) {// 可以避免切换的时候重复创建
//				mapFragment = new FindParkingSpaceMapFragment();
//			}
//			baseFragment = mapFragment;
//		} else if (showState == 1) {
//			if (null == listFragment) {// 可以避免切换的时候重复创建
//				listFragment = new FindParkingSpaceListFragment();
//			}
//			baseFragment = listFragment;
//		}

//		if (!baseFragment.isAdded()) {
        Bundle data = new Bundle();
//			data.putParcelable("latLng", latLng);
        data.putDouble("lat", latLng.latitude);
        data.putDouble("lng", latLng.longitude);
        data.putParcelableArrayList("data",
                (ArrayList<? extends Parcelable>) listmMaps);
        transaction = fragmentManager.beginTransaction();
        baseFragment.setArguments(data);
        transaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        transaction.replace(R.id.fl_find_parking_space, baseFragment);
        transaction.commit();

//		}else {
//			transaction.show(baseFragment);
//		}
    }

    private String city = "";
    private String district = "";
    private String keyWord = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                switch (requestCode) {
                    case SEARCH_TYPE_CODE:
                        if (!StringUtil.isEmpty(data)) {
                            searchData(data);
                        }

                        break;
                }
                break;
        }
    }

    /**
     * 搜索数据处理
     *
     * @param data
     */
    private void searchData(Intent data) {
        city = data.getStringExtra("city");
        district = data.getStringExtra("district");
        keyWord = data.getStringExtra("keyWord");
        if (StringUtil.isEmpty(city)) {
            city = "";
        }
        if (StringUtil.isEmpty(district)) {
            district = "";
        }
        if (StringUtil.isEmpty(keyWord)) {
            keyWord = "";
        }
        title.setText(city + district + keyWord);
        double lat = StringUtil.getDouble(data.getStringExtra("lat"));
        double lng = StringUtil.getDouble(data.getStringExtra("lon"));
        latLng = new LatLng(lat, lng);
        RIGHT_ACTION_STATE = 1;
        showSearchTitle1();
        request();
    }

    private void showBtnStatuc(int i) {
        if (i == 0) {
            ll_map.setBackgroundResource(R.drawable.chewei_bj2);
            Drawable img_on = getResources().getDrawable(
                    R.drawable.chewei_map_click);
            img_on.setBounds(0, 0, img_on.getMinimumWidth(),
                    img_on.getMinimumHeight());
            tv_map.setCompoundDrawables(img_on, null, null, null); // 设置左图标
            tv_map.setTextColor(getResources().getColor(R.color.orange));

            ll_list.setBackgroundResource(R.drawable.chewei_bj1);
            Drawable img_off = getResources().getDrawable(
                    R.drawable.chewei_list);
            img_off.setBounds(0, 0, img_off.getMinimumWidth(),
                    img_off.getMinimumHeight());
            tv_list.setCompoundDrawables(img_off, null, null, null); // 设置左图标
            tv_list.setTextColor(getResources().getColor(
                    R.color.text_black_colcor));
        } else if (i == 1) {
            ll_list.setBackgroundResource(R.drawable.chewei_bj2);
            Drawable img_on = getResources().getDrawable(
                    R.drawable.chewei_list_click);
            img_on.setBounds(0, 0, img_on.getMinimumWidth(),
                    img_on.getMinimumHeight());
            tv_list.setCompoundDrawables(img_on, null, null, null); // 设置左图标
            tv_list.setTextColor(getResources().getColor(R.color.orange));

            ll_map.setBackgroundResource(R.drawable.chewei_bj1);
            Drawable img_off = getResources()
                    .getDrawable(R.drawable.chewei_map);
            img_off.setBounds(0, 0, img_off.getMinimumWidth(),
                    img_off.getMinimumHeight());
            tv_map.setCompoundDrawables(img_off, null, null, null); // 设置左图标
            tv_map.setTextColor(getResources().getColor(
                    R.color.text_black_colcor));

        }
    }

    /**
     * 数据请求
     */
    private void request() {
//        RequestParams params = new RequestParams();
//        // if (isDefult) {
//        // params.addBodyParameter("lat", selectLatLng.latitude + "");
//        // params.addBodyParameter("lon", selectLatLng.longitude + "");
//        // } else {
//        params.addBodyParameter("lat", latLng.latitude + "");
//        params.addBodyParameter("lon", latLng.longitude + "");
//        // }
//        params.addBodyParameter("type", type);
//        ProgrosDialog.openDialog(this);
//        httpBiz.httPostData(FIND_PARK_CODE, API.FINDCAR_URL, params, this);

        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_SEARCH + NetInterface.SEARCH + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("keyWord", "停车场");
        param.put("longitude", MyMapUtils.getLongitude(baseContext));
        param.put("latitude", MyMapUtils.getLatitude(baseContext));
        netWorkHelper.PostJson(url, param, this);
    }

    @Override
    public void receive(String data) {
        ProgrosDialog.closeProgrosDialog();
        SearchResponse response = (SearchResponse) GsonUtil.getInstance().convertJsonStringToObject(data, SearchResponse.class);
        if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            showToast(response.getDesc());
            return;
        }


        prepareData(response);

        loginResponse.setToken(response.getToken());
        LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
    }

    private void prepareData(SearchResponse response) {
        listmMaps = new ArrayList<>();
        for (int i = 0; i < response.getMsg().size(); i++) {
            ParkInfoNative parkInfo = new ParkInfoNative();
            parkInfo.setAddr(response.getMsg().get(i).getAddress());
            parkInfo.setAreaName(response.getMsg().get(i).getDistrict());
            parkInfo.setCityName(response.getMsg().get(i).getCityName());
            parkInfo.setDistance(response.getMsg().get(i).getDistance());
            parkInfo.setLatitude(response.getMsg().get(i).getLocation().getLat() + "");
            parkInfo.setLongitude(response.getMsg().get(i).getLocation().getLng() + "");
            parkInfo.setLeftNum(-1);// 不能展示剩余车位了.
            parkInfo.setName(response.getMsg().get(i).getName());
            if (null != response.getMsg().get(i).getAdditionalInformation()) {
                if (null == response.getMsg().get(i).getAdditionalInformation().getPrice() || "".equals(response.getMsg().get(i).getAdditionalInformation().getPrice()))
                    parkInfo.setPrice("--");
                else
                    parkInfo.setPrice(response.getMsg().get(i).getAdditionalInformation().getPrice());
            } else {
                parkInfo.setPrice("--");
            }
            listmMaps.add(parkInfo);
        }

        Intent mIntent = new Intent();
        Constant.CURRENT_REFRESH = Constant.FIND_PARK_REFRESH;
        mIntent.setAction(Constant.REFRESH_FLAG);
        mIntent.putExtra("lat", latLng.latitude);
        mIntent.putExtra("lng", latLng.longitude);
        mIntent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) listmMaps);
        sendBroadcast(mIntent);
    }

    @Override
    public void receive(int type, String data) {
        super.receive(type, data);
        ProgrosDialog.closeProgrosDialog();
        switch (type) {
            case FIND_PARK_CODE:
                pson(data);
                break;
            default:
                break;
        }
    }

    private void pson(String result) {
        if (StringUtil.isEmpty(result)) {
        } else {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (StringUtil.isEquals(jsonObject.optString("operationState"),
                        "SUCCESS", true)) {
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<List<ParkInfoNative>>() {
                    }.getType();
                    listmMaps = gson.fromJson(jsonObject.optJSONObject("data")
                                    .optJSONObject("data").optString("parkInfoList"),
                            type);
                    // if (listmMaps.size() > 0) {
                    // mRelativeLayout_viewPager.setVisibility(View.VISIBLE);
                    // analList(listmMaps);
                    // } else {
                    // mRelativeLayout_viewPager.setVisibility(View.INVISIBLE);
                    // }
//					commitFragment();
                    Intent mIntent = new Intent();
                    Constant.CURRENT_REFRESH = Constant.FIND_PARK_REFRESH;
                    mIntent.setAction(Constant.REFRESH_FLAG);
                    mIntent.putExtra("lat", latLng.latitude);
                    mIntent.putExtra("lng", latLng.longitude);
                    mIntent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) listmMaps);
                    sendBroadcast(mIntent);
                } else {
                    showToast(getString(R.string.no_data));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        latLng = null;
        System.gc();
    }
}
