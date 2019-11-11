package com.cheweishi.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.cheweishi.android.R;
import com.cheweishi.android.config.API;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.CarDynamicResponse;
import com.cheweishi.android.entity.CarDynamicVONative;
import com.cheweishi.android.tools.ReLoginDialog;
import com.cheweishi.android.tools.SharePreferenceTools;
import com.cheweishi.android.utils.CustomProgressDialog;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.MyMapUtils;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.utils.mapUtils.BaseMapUtil;
import com.cheweishi.android.utils.mapUtils.LocationUtil;
import com.cheweishi.android.utils.mapUtils.MapSearchUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 车动态界面
 *
 * @author zhangq
 */
public class CarDynamicActivity extends BaseActivity {
    @ViewInject(R.id.left_action)
    private TextView tvLeft;
    @ViewInject(R.id.title)
    private TextView tvTitle;
    @ViewInject(R.id.right_action)
    private ImageButton ibtnRight;// 定位类型
    @ViewInject(R.id.bmapView)
    private MapView mMapView;
    @ViewInject(R.id.cbox_traffic)
    private CheckBox cBoxTraffic;// 交通路况
    @ViewInject(R.id.cbox_car_location)
    private RadioButton cBoxCarLocation;// 车的位置
    @ViewInject(R.id.cbox_person_location)
    private RadioButton cBoxPersonLocation;// 人的位置
    @ViewInject(R.id.tv_location)
    private TextView tvLocation;// 位置信息
    @ViewInject(R.id.tv_mile)
    private TextView tvMile;// 里程信息
    @ViewInject(R.id.tv_obdtime)
    private TextView tvOBDtime;// 行驶时间
    @ViewInject(R.id.tv_oil)
    private TextView tvOil;// 油耗信息
    @ViewInject(R.id.tv_speed)
    private TextView tvSpeed;// 速度信息
    @ViewInject(R.id.car_info)
    private LinearLayout lLayoutCarInfo;// 车辆信息
    @ViewInject(R.id.tv_upload_time)
    private TextView tv_upload_time;// 最后上传时间
    private CustomProgressDialog dialog;
    @ViewInject(R.id.ibtn_increse)
    private ImageView imgIncrese;//
    @ViewInject(R.id.ibtn_decrese)
    private ImageView imgDecrese;
    // @ViewInject(R.id.tv_gps)
    // private TextView tvGps;

    /**
     * 访问车信息时间间隔
     */
    private static final int UPDATE_TIME_PERSON = 30 * 1000;
    Gson gson = new Gson();
    private List<LatLng> latlngs;// 车辆轨迹信息
    private LatLng carLatLng;// 当前车位置
    private LatLng personLatLng;// 人位置
    private String cid;
    private String uid;
    private String mobile;
    private CarDynamicVONative vo;
    private Timer carTimer;//
    private boolean isDraw = true;// 控制是否显示车的轨迹
    private boolean isFired = false;// 控制车辆是否移动

    private BaseMapUtil mBaseMapUtil;
    private MapSearchUtil mMapSearchUtil;
    private LocationUtil mLocationUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_dynamic);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        // 初始化控件
        tvLeft.setText(getString(R.string.back));
        tvTitle.setText(getString(R.string.car_dynamic));
        // ibtnRight.setImageResource(R.drawable.chedongtai_daohang);

        tvLeft.setOnClickListener(onClickListener);
        // ibtnRight.setOnClickListener(onClickListener);
        cBoxTraffic.setOnCheckedChangeListener(checkedChangeListener);
        cBoxCarLocation.setOnCheckedChangeListener(checkedChangeListener);
        cBoxPersonLocation.setOnCheckedChangeListener(checkedChangeListener);
        cBoxPersonLocation.setOnClickListener(onClickListener);

        //cBoxCarLocation.setOnClickListener(onClickListener);
        imgIncrese.setOnClickListener(onClickListener);
        imgDecrese.setOnClickListener(onClickListener);

        // 设置地图配置
        setMapInfo();

        // 设置地图监听

        // 获取人的位置

        // LoginMessage message = LoginMessageUtils.getLoginMessage(this);

        latlngs = new ArrayList<LatLng>();
        try {
            uid = loginResponse.getDesc();
            cid = getIntent().getStringExtra("deviceNo");
            if (StringUtil.isEmpty(cid))
                if (hasDevice()) {// 是否有车
                    cid = loginResponse.getMsg().getDefaultDeviceNo();
                }
        } catch (Exception e) {
            // 捕获空指针错误下面处理
        }
        if (StringUtil.isEmpty(cid) || "null".equalsIgnoreCase(cid)) {
            showToast(CarDynamicActivity.this.getString(R.string.no_device));
            isDraw = false;
        }
        if (isDraw) {
            showPdialog();
            cBoxCarLocation.setChecked(true);
        } else {
            cBoxPersonLocation.setChecked(true);
        }
        carTimer = new Timer(true);
        carTimer.schedule(carTask, UPDATE_TIME_PERSON, UPDATE_TIME_PERSON);
    }

    private void setMapInfo() {
        mMapView.showZoomControls(false);
        mMapSearchUtil = new MapSearchUtil();
        mBaseMapUtil = new BaseMapUtil(mMapView.getMap());
        mLocationUtil = new LocationUtil(this,
                LocationUtil.SCANSPAN_TYPE_SHORT, locationListener);
        mBaseMapUtil.setUI();
        mBaseMapUtil.setMapStatus();
        mBaseMapUtil.setMyLocationEnable(true, R.drawable.chedongtai_person);
        // SharePreferenceTools.setLatDynamic(CarDynamicActivity.this,
        // StringUtil.getFloat(vo.getLat()));
        // SharePreferenceTools.setLngDynamic(CarDynamicActivity.this,
        // StringUtil.getFloat(vo.getLon()));
        // carLatLng = new LatLng(SharePreferenceTools.getLatDynamic(this),
        // SharePreferenceTools.getLngDynamic(this));
        //
        // latlngs.add(carLatLng);
        //
        // // %u7184%u706b “熄火” unicode
        // isFired = !"%u7184%u706b".equals(vo.getStatus());
        // // 是否在地图上画出车辆轨迹
        // if (isDraw) {
        // mMapSearchUtil.startReverseGeoCode(carLatLng,
        // onGetGeoCoderResultListener);
        // mBaseMapUtil.removeAllMarkers();
        // mBaseMapUtil.setPolyLineOverlayer(latlngs);
        // mBaseMapUtil.setMarkerOverlayer(carLatLng,
        // R.drawable.chedongtai_car2x,
        // StringUtil.getFloat(vo.getAzimuth()));
        //
        // mBaseMapUtil.moveTo(carLatLng, true);
        // }
    }

    BDLocationListener locationListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (mBaseMapUtil == null) {
                return;
            }
            mBaseMapUtil.setMylocationData(location.getLatitude(),
                    location.getLongitude(), location.getRadius(),
                    location.getDirection());
        }
    };

    /**
     * 获取人的位置
     */
    private void moveToPerson() {
        // TODO 改变位置获取方式
        double lati = MyMapUtils.getLatitude(CarDynamicActivity.this);
        double longi = MyMapUtils.getLongitude(CarDynamicActivity.this);
        personLatLng = new LatLng(lati, longi);
        mBaseMapUtil.zoomTo(BaseMapUtil.DEFAULT_ZOOM_LEVEL);
        mBaseMapUtil.moveTo(personLatLng, true);
        tvLocation.setText(MyMapUtils.getAddress(this));
    }

    /**
     * 获取车的位置并在handler里移动
     */
    private void moveToCar() {


        String url = NetInterface.BASE_URL + NetInterface.TEMP_OBD + NetInterface.DYNAMIC + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", uid);
        param.put("token", loginResponse.getToken());
        param.put("deviceNo", cid);
        netWorkHelper.PostJson(url, param, this);

//        RequestParams params = new RequestParams();
//        params.addBodyParameter("cid", cid);
//        params.addBodyParameter("uid", uid);
//        params.addBodyParameter("mobile", mobile);
//
//        SimpleHttpUtils utils = new SimpleHttpUtils(CarDynamicActivity.this,
//                params, API.CAR_DYNAMIC_URL, handler);
//        utils.PostHttpUtils();

        // httpBiz.httPostData(1000, API.CAR_DYNAMIC_URL, params, this);
    }

    @Override
    public void receive(String data) {
        disMissPDialog();

        CarDynamicResponse response = (CarDynamicResponse) GsonUtil.getInstance().convertJsonStringToObject(data, CarDynamicResponse.class);
        if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            showToast(R.string.server_link_fault);
            return;
        }

        try {
            if (StringUtil.isEmpty(response.getMsg())) {
                // 根据状态有无数据判断车辆定位是否成功
                throw new RuntimeException("car location is null");
            }
            SharePreferenceTools.setLatDynamic(CarDynamicActivity.this,
                    (float) response.getMsg().getLat());
            SharePreferenceTools.setLngDynamic(CarDynamicActivity.this,
                    (float) response.getMsg().getLng());
            carLatLng = new LatLng(response.getMsg().getLat(),
                    response.getMsg().getLng());

            latlngs.add(carLatLng);

            String acc = response.getMsg().getAcc();
            isFired = null != acc && "1".equals(acc);
            // 是否在地图上画出车辆轨迹
            if (isDraw) {
                mMapSearchUtil.startReverseGeoCode(carLatLng,
                        onGetGeoCoderResultListener);
                mBaseMapUtil.removeAllMarkers();
                mBaseMapUtil.setPolyLineOverlayer(latlngs);
                if (isFired) {
                    mBaseMapUtil.setMarkerOverlayer(carLatLng,
                            R.drawable.chedongtai_car2x,
                            StringUtil.getFloat(response.getMsg().getAzimuth() + ""));
                } else {
                    mBaseMapUtil.setMarkerOverlayer(carLatLng,
                            R.drawable.chedongtai_carstop,
                            StringUtil.getFloat(response.getMsg().getAzimuth() + ""));
                }
                mBaseMapUtil.moveTo(carLatLng, true);
                if (lLayoutCarInfo.getVisibility() == View.INVISIBLE) {
                    lLayoutCarInfo.setVisibility(View.VISIBLE);
                }
            }
            tvMile.setText(response.getMsg().getMileAge() + "km");
            // 因为要显示
//            tvOBDtime.setText(getSDate(new Date(response.getMsg().getEngineRuntime()), true));
            tvOBDtime.setText(getSDate(response.getMsg().getEngineRuntime()));
            tvSpeed.setText(getSpeed(response.getMsg().getSpeed()));
            tvOil.setText(getStringOil(response.getMsg().getAverageOil()));
            tv_upload_time.setText(getUpLoadDate(response.getMsg().getCreatetime()));
            // TODO isMode
            //if ("0".equalsIgnoreCase(isMode)) {
            // tvGps.setText("GPS");
            ibtnRight.setImageResource(R.drawable.dongtai_gps);


        } catch (Exception e) {
        }
        loginResponse.setToken(response.getToken());
//        LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
    }

    @Override
    public void error(String errorMsg) {
        disMissPDialog();
        tvLocation.setText("");
        showToast(getString(R.string.server_link_fault));
    }

    /**
     * 车辆信息处理
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 400) {
                disMissPDialog();
                tvLocation.setText("");
                showToast(getString(R.string.server_link_fault));
                return;
            }

            String str = (String) msg.obj;
            try {
                JSONObject js = new JSONObject(str);

                if (API.returnSuccess.equalsIgnoreCase(js.optString("state"))) {
                    //js = js.getJSONObject("data");
//					Date dateStart = StringUtil.getDate(js.optString("start"),
//							"yyyy-MM-dd HH:mm:ss.SS");
//					Date dateEnd = StringUtil.getDate(js.optString("end"),
//							"yyyy-MM-dd HH:mm:ss.SS");

                    //String isMode = js.optString("isMode");
                    //js = js.optJSONObject("data");
//					js = js.optJSONObject("body");
//					str = js.toString();
                    vo = gson.fromJson(js.optString("data"), CarDynamicVONative.class);
                    if (StringUtil.isEmpty(vo.getStatus())) {
                        // 根据状态有无数据判断车辆定位是否成功
                        throw new RuntimeException("car location is null");
                    }
                    SharePreferenceTools.setLatDynamic(CarDynamicActivity.this,
                            StringUtil.getFloat(vo.getLat()));
                    SharePreferenceTools.setLngDynamic(CarDynamicActivity.this,
                            StringUtil.getFloat(vo.getLon()));
                    carLatLng = new LatLng(StringUtil.getDouble(vo.getLat()),
                            StringUtil.getDouble(vo.getLon()));

                    latlngs.add(carLatLng);

                    // %u7184%u706b “熄火” unicode
                    isFired = !"%u7184%u706b".equals(vo.getStatus());
                    // 是否在地图上画出车辆轨迹
                    if (isDraw) {
                        mMapSearchUtil.startReverseGeoCode(carLatLng,
                                onGetGeoCoderResultListener);
                        mBaseMapUtil.removeAllMarkers();
                        mBaseMapUtil.setPolyLineOverlayer(latlngs);
                        if (isFired) {
                            mBaseMapUtil.setMarkerOverlayer(carLatLng,
                                    R.drawable.chedongtai_car2x,
                                    StringUtil.getFloat(vo.getAzimuth()));
                        } else {
                            mBaseMapUtil.setMarkerOverlayer(carLatLng,
                                    R.drawable.chedongtai_carstop,
                                    StringUtil.getFloat(vo.getAzimuth()));
                        }
                        mBaseMapUtil.moveTo(carLatLng, true);
                        if (lLayoutCarInfo.getVisibility() == View.INVISIBLE) {
                            lLayoutCarInfo.setVisibility(View.VISIBLE);
                        }
                    }
                    tvMile.setText(getmile(vo.getMile()));
                    //tvOBDtime.setText(getSDate(dateStart, dateEnd, isFired));
                    tvSpeed.setText(getSpeed(vo.getSpeed()));
                    //tvOil.setText(getStringOil(vo.getObdifc()));
                    // TODO isMode
                    //if ("0".equalsIgnoreCase(isMode)) {
                    // tvGps.setText("GPS");
                    ibtnRight.setImageResource(R.drawable.dongtai_gps);
//					} else {
//						// tvGps.setText("GPRS");
//						ibtnRight.setImageResource(R.drawable.dongtai_gprs);
//					}

                } else if (StringUtil.isEquals(API.returnRelogin,
                        js.optString("state"), true)) {
                    tvLocation.setText("");
                    ReLoginDialog.getInstance(CarDynamicActivity.this)
                            .showDialog(js.optString("message"));
                } else {
                    tvLocation.setText("");
                    showToast(js.optString("message"));
                }
            } catch (Exception e) {
                e.printStackTrace();
//				try {
//					cBoxPersonLocation.setChecked(true);
//				} catch (Exception e1) {
//				}
            }
            disMissPDialog();
        }

    };
    private OnGetGeoCoderResultListener onGetGeoCoderResultListener = new OnGetGeoCoderResultListener() {

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            String sCar = result.getAddress();
            sCar = StringUtil.isEmpty(sCar) ? " " : sCar;
            tvLocation.setText(sCar);
        }

        @Override
        public void onGetGeoCodeResult(GeoCodeResult arg0) {

        }
    };

    /**
     * 处理速度
     *
     * @param str
     * @return
     */
    private String getSpeed(String str) {
        if (str == null || "".equals(str) || "0".equals(str)
                || "null".equalsIgnoreCase(str)) {
            return "0km/h";
        }
        return StringUtil.getDouble(str) + "km/h";
    }

    /**
     * 处理耗油
     *
     * @param str
     * @return
     */
    private String getStringOil(String str) {
        if (str == null || "".equals(str) || "0".equals(str)
                || "null".equalsIgnoreCase(str)) {
            return "0L/100km";
        }
        return str + "L/100km";
    }

    /**
     * 处理时间
     *
     * @param startTime
     * @param endTime   暂时没用到
     * @param isMoving
     * @return
     */
    private String getSDate(Date startTime, Date endTime, boolean isMoving) {
        if (startTime == null || isMoving == false) {
            return "- -:- -:- -";
            // startTime = StringUtil.getDate("2015:04:25:12:22",
            // "yyyy:MM:dd:HH:mm");
        }
        long time1 = Calendar.getInstance().getTimeInMillis();
        long time2 = startTime.getTime();
        long diff = Math.abs(time1 - time2);
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
                * (1000 * 60 * 60))
                / (1000 * 60);
        long second = (diff - days * (1000 * 60 * 60 * 24) - hours
                * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;
        if (days == 0) {
            return hours + "h:" + minutes + "m:" + second + "s";
        }
        return days + "d:" + hours + "h:" + minutes + "m";
    }

    private String getSDate(long time) {

        long hour = time / 60 / 60;
        long minutes = time / 60 - hour * 60;
        long second = time - hour * 60 * 60 - minutes * 60;

        return hour + ":" + minutes + ":" + second;
    }

    private String getUpLoadDate(long time) {

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date curDate = new Date(time);// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    private String getSDate(Date endTime, boolean isMoving) {
        if (endTime == null || isMoving == false) {
            return "- -:- -:- -";
            // startTime = StringUtil.getDate("2015:04:25:12:22",
            // "yyyy:MM:dd:HH:mm");
        }
        long time2 = endTime.getTime();
        long diff = time2;
        long days = diff / (1000 * 60 * 60 * 24);
        long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
                * (1000 * 60 * 60))
                / (1000 * 60);
        long second = (diff - days * (1000 * 60 * 60 * 24) - hours
                * (1000 * 60 * 60) - minutes * (1000 * 60)) / 1000;
        if (days == 0) {
            return hours + "h:" + minutes + "m:" + second + "s";
        }
        return days + "d:" + hours + "h:" + minutes + "m";
    }

    /**
     * 处理里程
     *
     * @param mile
     * @return
     */
    private String getmile(String mile) {
        if (StringUtil.isEmpty(mile) || "null".equalsIgnoreCase(mile)) {
            return "0km";
        }
        int i = StringUtil.getInt(mile);
        return i / 1000.00 + "km";
    }

    private void showPdialog() {
        if (dialog == null) {
            dialog = CustomProgressDialog.getInstance(CarDynamicActivity.this);
        }
        dialog.show();
    }

    private void disMissPDialog() {
        if (dialog != null) {
            dialog.onDestory();
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 取消timer
     */
    private void cancelCarTimer() {
        if (carTimer != null) {
            carTimer.cancel();
            carTimer.purge();
            carTimer = null;
        }
        if (carTask != null) {
            carTask.cancel();
            carTask = null;
        }
    }

    private TimerTask carTask = new TimerTask() {

        @Override
        public void run() {
            moveToCar();
        }
    };

    OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton cButton, boolean isChecked) {
            switch (cButton.getId()) {
                case R.id.cbox_traffic:
                    mBaseMapUtil.setTrafficEnable(isChecked);
                    if (isChecked) {
                        showToast(getString(R.string.TrafficEnabled_open));
                    } else {
                        showToast(getString(R.string.TrafficEnabled_close));
                    }
                    break;
                case R.id.cbox_car_location:
                    if (isChecked) {
                        isDraw = true;
                        moveToCar();
                    }
                    break;
                case R.id.cbox_person_location:
                    if (isChecked) {
                        isDraw = false;
                        moveToPerson();
                    }
                    break;

                default:
                    break;
            }
        }

    };

    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            Intent intent = null;

            switch (view.getId()) {
                case R.id.left_action:
                    finish();
                    break;
                case R.id.right_action:
                    // TODO 不支持GPS
//                    intent = new Intent(CarDynamicActivity.this,
//                            NavigationActivity.class);
//                    intent.putExtra("isLogined", true);
//                    intent.putExtra("hasCar", true);
                    break;
                case R.id.cbox_person_location:
                    moveToPerson();
                    break;
                case R.id.cbox_car_location:
                    moveToCar();
                    break;
                case R.id.ibtn_increse:
                    mBaseMapUtil.zoomTo(mBaseMapUtil.getZoom() + 1);
                    break;
                case R.id.ibtn_decrese:
                    mBaseMapUtil.zoomTo(mBaseMapUtil.getZoom() - 1);
                    break;
                default:
                    break;
            }
            if (intent != null) {
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        mLocationUtil.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carTimer = new Timer(true);
        mMapView.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationUtil.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelCarTimer();
        carTimer = null;
        carTask = null;
        mMapView.onDestroy();
        handler.removeCallbacksAndMessages(null);
        mLocationUtil.onDestory();
        mLocationUtil = null;
        if (mMapSearchUtil != null) {
            mMapSearchUtil.onDestory();
            mMapSearchUtil = null;
        }
        if (mBaseMapUtil != null) {
            mBaseMapUtil.onDestory();
            mBaseMapUtil = null;
        }
        disMissPDialog();
        checkedChangeListener = null;
        handler = null;
    }

}
