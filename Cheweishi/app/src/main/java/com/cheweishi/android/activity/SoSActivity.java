package com.cheweishi.android.activity;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.cheweishi.android.R;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.ServiceListResponse;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.tools.LoginMessageUtils;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.MyMapUtils;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.CustomDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 紧急救援
 */
public class SoSActivity extends BaseActivity implements OnClickListener,
        BDLocationListener, OnMarkerClickListener {
    @ViewInject(R.id.Sos_map)
    private MapView Sos_map;
    @ViewInject(R.id.Sos_address)
    private TextView Sos_address;
    @ViewInject(R.id.SosPhone)
    private TextView SosPhone;
    @ViewInject(R.id.left_action)
    private Button left_action;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.ll_locate)
    private LinearLayout ll_locate;
    private CustomDialog.Builder builder;
    private CustomDialog sosDialog;
    // 声明定位模式
    private LocationClient locationClient;
    // 声明定位服务客户端
    private LocationClientOption Option;
    private BaiduMap baiduMap;
    BitmapDescriptor bitmapDescriptor;
    private String city = "";
    private String district = "";
    private String key = "";
    private double latitude = 0.0;
    private double longitude = 0.0;
    private boolean flag = false;
    private String phoneNumber;
    private List<ServiceListResponse.MsgBean> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_sos);
        ViewUtils.inject(this);
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        left_action.setText(R.string.back);
        title.setText(R.string.fuwu_emergency);
        // 获取地图控制器
        baiduMap = Sos_map.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        locationClient = new LocationClient(getApplicationContext());
        Option = new LocationClientOption();
        Option.setCoorType("bd0911");// 设置返回定位坐标
        Option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        Option.setIsNeedAddress(true);// 是否需要地址信息
        Option.setNeedDeviceDirect(true);// 是否需要设备方向
        Option.setScanSpan(5000);// 设置扫描（毫秒）
        locationClient.setLocOption(Option);
        // 定位监听事件
        locationClient.registerLocationListener(this);
        baiduMap.setOnMarkerClickListener(this);

        ProgrosDialog.openDialog(this);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_HOME_URL + NetInterface.LIST + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("latitude", MyMapUtils.getLatitude(getApplicationContext()));
        param.put("longitude", MyMapUtils.getLongitude(getApplicationContext()));
        param.put("serviceCategoryId", 4); // 紧急救援
        param.put("pageSize", 5);
        param.put("pageNumber", 1);
        netWorkHelper.PostJson(url, param, this);

    }

    @Override
    public void receive(String data) {

        ServiceListResponse response = (ServiceListResponse) GsonUtil.getInstance().convertJsonStringToObject(data, ServiceListResponse.class);
        if (null == response || !response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            showToast(R.string.server_link_fault);
            return;
        }

        datalist = response.getMsg();


        loginResponse.setToken(response.getToken());
//        LoginMessageUtils.saveloginmsg(baseContext, loginResponse);

    }


    @Override
    public void receive(String TAG, String data) {
        ProgrosDialog.closeProgrosDialog();
        switch (TAG) {
            case NetInterface.SUBSCRIBE: // 预约
                BaseResponse baseResponse = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
                if (!baseResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    showToast("您已经呼叫紧急救援了,请耐心等待救援");
                    return;
                }

                turnToPhone();

                loginResponse.setToken(baseResponse.getToken());
//                LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
                break;
        }
    }


    /**
     * 拨打电话
     */
    public void turnToPhone() {
        Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                + phoneNumber));
        tel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(tel);
    }

    @Override
    public void error(String errorMsg) {
        ProgrosDialog.closeProgrosDialog();
        showToast(R.string.server_link_fault);
    }

    @Override
    protected void onStart() {
        locationClient.start();
        baiduMap.setMyLocationEnabled(true);
        super.onStart();
    }

    @Override
    protected void onStop() {
        locationClient.stop();
        baiduMap.setMyLocationEnabled(false);
        super.onStop();
    }

    @OnClick({R.id.SosPhone, R.id.left_action, R.id.ll_locate})
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.SosPhone:
                showSOSDialog(null);
                break;
            case R.id.left_action:
                finish();
                break;
            case R.id.ll_locate:
//                searchPlace();
                break;

            default:
                break;
        }
    }

    /**
     * 紧急救援搜索
     */
    private void searchPlace() {
        Intent intent = new Intent();
        intent.setClass(SoSActivity.this, SearchActivity.class);
        intent.putExtra("type", "SOS");
        intent.putExtra("hint", "紧急救援搜索");
        startActivityForResult(intent, 1000);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg1 == RESULT_OK) {
            switch (arg0) {
                case 1000:
                    baiduMap.clear();
                    if (!StringUtil.isEmpty(arg2)) {
                        if (!StringUtil.isEmpty(arg2.getStringExtra("city"))) {
                            city = arg2.getStringExtra("city");
                        }
                        if (!StringUtil.isEmpty(arg2.getStringExtra("district"))) {
                            district = arg2.getStringExtra("district");
                        }
                        if (!StringUtil.isEmpty(arg2.getStringExtra("keyword"))) {
                            key = arg2.getStringExtra("keyword");
                        }
                        if (!StringUtil.isEmpty(arg2.getStringExtra("lat"))
                                && !StringUtil.isEmpty(arg2.getStringExtra("lon"))) {
                            latitude = StringUtil.getDouble(arg2
                                    .getStringExtra("lat"));
                            longitude = StringUtil.getDouble(arg2
                                    .getStringExtra("lon"));
                        }
                        String stradd = city + district + key;
                        setData(latitude, longitude, stradd);
                    }
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * 弹出呼叫对话框
     */
    private void showSOSDialog(final ServiceListResponse.MsgBean response) {
        if (null != response) {
            builder = new CustomDialog.Builder(this);
            builder.setTitle("救援电话");
            builder.setMessage(response.getContactPhone());
            builder.setPositiveButton(R.string.call_out,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            phoneNumber = response.getContactPhone();
                            sendSOs(response.getCarService().get(0).getService_id());


                        }
                    });

            builder.setNegativeButton(R.string.cancel,
                    new android.content.DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            // setRadioButtonLight();
                        }
                    });
            sosDialog = builder.create();
            sosDialog.show();
        }

    }


    private void sendSOs(int id) {
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_ORDER + NetInterface.SUBSCRIBE + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("serviceId", id);
        param.put("chargeStatus", "UNPAID");
        param.put(Constant.PARAMETER_TAG, NetInterface.SUBSCRIBE);
        netWorkHelper.PostJson(url, param, this);
    }

    /**
     * 添加覆盖物
     *
     * @param Latitude
     * @param Longitude
     * @param string
     */
    private void setData(double Latitude, double Longitude, String string) {
        LatLng latLng = new LatLng(Latitude, Longitude);
        baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
        bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.drawable.jiuyuan_chepaihao2x);
        OverlayOptions ooA = new MarkerOptions().position(latLng)
                .icon(bitmapDescriptor).zIndex(9).draggable(true);
        baiduMap.addOverlay(ooA);
        Sos_address.setText(string);
        // 当前位置的详细信息
    }

    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        return bitmap;
    }

    private void addData(double Latitude, double Longitude, final ServiceListResponse.MsgBean response) {
        LatLng latLng = new LatLng(Latitude, Longitude);

//        bitmapDescriptor = BitmapDescriptorFactory
//                .fromResource(R.drawable.jiuyuan_chepaihao2x);
        View view = View.inflate(baseContext, R.layout.sos_item_popu, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_sos_content);
        textView.setText("商家名字:" + response.getTenantName() + "\r\n" + "联系电话:" + response.getContactPhone());
        Bitmap bitmap = getViewBitmap(view);
        bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
        OverlayOptions ooA = new MarkerOptions().position(latLng)
                .icon(bitmapDescriptor).zIndex(1).draggable(true);
        Marker marker = (Marker) baiduMap.addOverlay(ooA);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", response);
        marker.setExtraInfo(bundle);

//        try {
//            InfoWindow mInfoWindow;
//            TextView location = new TextView(SoSActivity.this);
//            location.setBackgroundResource(R.drawable.jiuyuan_kuang);// location_tips
//            location.setText("商家名字:" + response.getTenantName() + "\r\n" + "联系电话:" + response.getContactPhone());
//            LatLng ll = new LatLng(Latitude, Longitude);
////            Point p = baiduMap.getProjection().toScreenLocation(ll);
////            p.y -= 60;
////            LatLng llInfo = baiduMap.getProjection().fromScreenLocation(p);
//            mInfoWindow = new InfoWindow(
//                    BitmapDescriptorFactory.fromView(location), ll, 10,
//                    new InfoWindow.OnInfoWindowClickListener() {
//
//                        @Override
//                        public void onInfoWindowClick() {
//                            showSOSDialog(response);
//                        }
//                    });
//            // 显示InfoWindow
//            baiduMap.showInfoWindow(mInfoWindow);
//        } catch (Exception e) {
//        }
    }

    /**
     * 定位当前位置监听
     */
    @Override
    public void onReceiveLocation(BDLocation arg0) {
        if (flag == false) {

//            setData(arg0.getLatitude(), arg0.getLongitude(), arg0.getAddrStr());
            if (null != datalist) {
                flag = true;
                ProgrosDialog.closeProgrosDialog();

                Sos_address.setText(arg0.getAddrStr());
                if (0 < datalist.size()) {
                    for (int i = 0; i < datalist.size(); i++) {
                        addData(datalist.get(i).getLatitude(), datalist.get(i).getLongitude(), datalist.get(i));
                    }

                    // 移动到最后
                    LatLng latLng = new LatLng(datalist.get(datalist.size() - 1).getLatitude(), datalist.get(datalist.size() - 1).getLongitude());
                    baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
                    MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(15);
                    baiduMap.animateMapStatus(u);
                } else {
                    setData(arg0.getLatitude(), arg0.getLongitude(), arg0.getAddrStr());
                }

            }

        }
    }

    /**
     * 覆盖物点击监听
     */
    @Override
    public boolean onMarkerClick(Marker arg0) {
//        InfoWindow mInfoWindow;
//        TextView location = new TextView(SoSActivity.this);
//        location.setBackgroundResource(R.drawable.jiuyuan_kuang);// location_tips

//        if (isLogined() && hasCar()) {
//            location.setText(loginResponse.getMsg().getDefaultVehiclePlate());
//        } else {
//            location.setText(Sos_address.getText().toString());
//        }
        Bundle bundle = arg0.getExtraInfo();
        if (null != bundle) {
            ServiceListResponse.MsgBean r = (ServiceListResponse.MsgBean) bundle.getSerializable("data");
//            location.setText(r.getTenant_name() + " - " + r.getContact_phone());
            showSOSDialog(r);
        }
//        final LatLng ll = arg0.getPosition();
//        Point p = baiduMap.getProjection().toScreenLocation(ll);
//        p.y -= 60;
//        LatLng llInfo = baiduMap.getProjection().fromScreenLocation(p);
//        mInfoWindow = new InfoWindow(
//                BitmapDescriptorFactory.fromView(location), llInfo, 10,
//                new OnInfoWindowClickListener() {
//
//                    @Override
//                    public void onInfoWindowClick() {
//                        baiduMap.hideInfoWindow();
//                    }
//                });
//        // 显示InfoWindow
//        baiduMap.showInfoWindow(mInfoWindow);

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.unRegisterLocationListener(this);
        baiduMap.removeMarkerClickListener(this);
        baiduMap.clear();
        Sos_map.removeAllViews();
        Sos_map.onDestroy();
        baiduMap = null;
        Sos_map = null;
    }
}
