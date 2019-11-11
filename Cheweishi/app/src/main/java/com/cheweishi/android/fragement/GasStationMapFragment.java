package com.cheweishi.android.fragement;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.cheweishi.android.R;
import com.cheweishi.android.activity.BaseActivity;
import com.cheweishi.android.biz.JSONCallback;
import com.cheweishi.android.config.API;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.MapMenssageDialog;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.CarDynamicResponse;
import com.cheweishi.android.entity.DistanceBeanNative;
import com.cheweishi.android.entity.LatlngBeanNative;
import com.cheweishi.android.entity.SearchResponse;
import com.cheweishi.android.tools.ReLoginDialog;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.MyMapUtils;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.utils.mapUtils.LocationUtil;
import com.cheweishi.android.widget.BaiduMapView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 加油站
 *
 * @author 刘伟
 */
public class GasStationMapFragment extends BaseFragment implements
        OnGetGeoCoderResultListener, JSONCallback {

    private GeoCoder mSearch = null;
    private GeoCoder mGeoCoder = null;
    private GeoCoder mSeGeoCoder = null;
    // 定义路况是否开启的按钮
    @ViewInject(R.id.cbox_traffic_gasstion_map)
    private CheckBox checkBox;
    // 定义一个用来装marker的列表
    private List<Marker> markerList;
    // 定义baiduMap
    private BaiduMap mBaiduMap;
    // 定义一个经纬度对象
    private LatlngBeanNative latlngBean;
    // 定义mapview
    @ViewInject(R.id.gasstation_bmapView)
    private MapView mMapView;
    // 实例化一个经纬度列表
    private List<LatLng> list = new ArrayList<LatLng>();
    private BaiduMapView mBaiduMapView;
    // 定义登陆信息类的实例
    private double lat = 0;
    private double lng = 0;
    private boolean isFirst = true;
    private boolean isF = true;
    // 加油站的名称
    @ViewInject(R.id.gasstationlistview_map_item_name)
    private TextView mNameTextView;
    // 加油站的地址
    @ViewInject(R.id.gasstationlistview_map_item_address)
    private TextView mAddressTextView;
    // 距离加油站的距离
    @ViewInject(R.id.gasstationlistview_map_item_distance)
    private TextView mDistanceTextView;
    // 选择人
    @ViewInject(R.id.gasstation_cbox_person_location)
    private RadioButton mpersonButton;
    // 选择车
    @ViewInject(R.id.gasstation_cbox_car_location)
    private RadioButton mCaRadioButton;
    //
    private boolean isDraw = true;
    private LatLng personLatLng;// 人位置
    private String address;
    @ViewInject(R.id.gasstation_linearlayout_notive)
    private LinearLayout mNetiveLayout;
    private List<Map<String, String>> listmMaps;
    // 实例化加油站图标
    private BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.jiayouzhan22x);
    // 下面的布局
    @ViewInject(R.id.gasstation_linearlayout_isyingchang)
    private LinearLayout gasstation_linearlayout_isyingchang;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferences2;
    private SharedPreferences sharedPreferencesgasstation;

    @ViewInject(R.id.rgroup)
    private RadioGroup mGroup;
    // 定义一个放大地图的按钮
    @ViewInject(R.id.gass_decrese)
    private ImageButton mGass_decrese;
    // 定义一个缩小地图的按钮
    @ViewInject(R.id.gass_increse)
    private ImageButton mGass_increse;

    private static final int CAR_CODE = 2001;
    private static final int GASS_CODE = 2002;
    private int login_type = 0;
    private boolean loginaty = false;

    private LocationUtil mLocationUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationUtil = new LocationUtil(baseContext.getApplicationContext(),
                LocationUtil.SCANSPAN_TYPE_SHORT, LocationListener);

    }

    BDLocationListener LocationListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mMapView == null || isDraw)
                return;
            double lati = location.getLatitude();
            double longi = location.getLongitude();
            new MyLocationData.Builder().accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(lati)
                    .longitude(longi).build();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gasstation_map, null);
        ViewUtils.inject(this, view);
        initGeoder();
        latlngBean = new LatlngBeanNative();
        init();
        return view;
    }

    /***
     * 实例化反编译
     */
    private void initGeoder() {
        mSeGeoCoder = GeoCoder.newInstance();
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(getGeoCoderResultListener);
        mSeGeoCoder.setOnGetGeoCodeResultListener(onGetGeoCoderResultListener);
    }

    /***
     * 初始化控件
     */
    private void init() {
        markerList = new ArrayList<Marker>();
        initBaiduMap();
        setListener();
        isCar();
    }

    /***
     * 实例化baidumap
     */
    private void initBaiduMap() {
        mBaiduMap = mMapView.getMap();
        mMapView.showZoomControls(false);
        // BaseMapUtil mBaiduMapView = new BaseMapUtil(mMapView.getMap());
        // mBaiduMapView.setUI();
        // mBaiduMapView.setMapStatus();
        // mBaiduMapView.setMyLocationEnable(true, 0);

        mBaiduMapView = new BaiduMapView(mMapView, baseContext);
    }

    /***
     * 判断是车还是人
     */
    private void isCar() {
        initSharedPreference();
        if (sharedPreferencesgasstation.getBoolean("isdexgass", false)) {
            if (sharedPreferences2.getBoolean("isdraw", false)) {
                mCaRadioButton.setChecked(true);
            } else {
                isDraw = false;
                sharedPreferences.edit().putBoolean("isDraw", isDraw).commit();
                mpersonButton.setChecked(true);
            }
        } else {
            sharedPreferences.edit().putBoolean("isDraw", isDraw).commit();
            mCaRadioButton.setChecked(true);
        }
        sharedPreferencesgasstation.edit().putBoolean("isdexgass", true)
                .commit();
    }

    /**
     * 实例化sharedpreference
     */
    private void initSharedPreference() {
        sharedPreferencesgasstation = baseContext.getSharedPreferences(
                "isindexgass", Context.MODE_PRIVATE);
        sharedPreferences = baseContext.getSharedPreferences("isgasstationDraw",
                Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("isDraw", isDraw).commit();
        sharedPreferences2 = baseContext.getSharedPreferences("isgasstationdraw",
                Context.MODE_PRIVATE);
    }

    /***
     * 绑定事件监听
     */
    private void setListener() {
        mBaiduMap.setOnMarkerClickListener(onMarkerClickListener);
        mNetiveLayout.setOnClickListener(listener);
        mGass_decrese.setOnClickListener(listener);
        mGass_increse.setOnClickListener(listener);
        checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
        mGroup.setOnCheckedChangeListener(checkedChangeListener);
    }

    private android.widget.CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new android.widget.CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            if (arg1) {
                mBaiduMap.setTrafficEnabled(true);
                showToast(getString(R.string.TrafficEnabled_open));
            } else {
                mBaiduMap.setTrafficEnabled(false);
                showToast(getString(R.string.TrafficEnabled_close));
            }
        }
    };
    // private GetLocationListener getLocationListener = new
    // GetLocationListener() {
    // @Override
    // public void getLoation(BDLocation location) {
    // if (location == null || mMapView == null || isDraw)
    // return;
    // double lati = location.getLatitude();
    // double longi = location.getLongitude();
    // new MyLocationData.Builder().accuracy(location.getRadius())
    // // 此处设置开发者获取到的方向信息，顺时针0-360
    // .direction(location.getDirection()).latitude(lati)
    // .longitude(longi).build();
    //
    // }
    // };
    private OnGetGeoCoderResultListener onGetGeoCoderResultListener = new OnGetGeoCoderResultListener() {

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (mNameTextView == null) {
                return;
            }
            bottomShowDatails(result);
            bottomShowDistance(result);
        }

        @Override
        public void onGetGeoCodeResult(GeoCodeResult arg0) {

        }
    };

    private OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.gasstation_linearlayout_notive:
                    navigation();
                    break;
                case R.id.gass_decrese:
                    mBaiduMapView.zoomTo((int) mBaiduMap.getMapStatus().zoom - 1);
                    break;
                case R.id.gass_increse:
                    mBaiduMapView.zoomTo((int) mBaiduMap.getMapStatus().zoom + 1);
                    break;
                default:
                    break;

            }

        }
    };

    private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int arg1) {
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.gasstation_cbox_car_location:
                    login_type++;
                    carAddress();
                    break;
                case R.id.gasstation_cbox_person_location:
                    person();
                    break;

                default:
                    break;
            }
        }

    };

    private OnMarkerClickListener onMarkerClickListener = new OnMarkerClickListener() {

        @Override
        public boolean onMarkerClick(Marker marker) {
            setIconMarker();
            cleckMarker(marker);
            marker.setToTop();
            return true;
        }
    };

    /***
     * 标记marker
     *
     * @param latlng
     * @param i
     */
    private void moveLatLng(LatLng latlng, int i) {

        OverlayOptions option = new MarkerOptions().position(latlng).icon(bitmap);
        Marker marker = (Marker) mBaiduMap.addOverlay(option);
        marker.setTitle(sortList.get(i).get("name"));
        if (isF) {
            checkedGasstation(marker);
        }
        markerList.add(marker);
    }

    /***
     * 底部显示距离
     *
     * @param result
     */
    protected void bottomShowDistance(ReverseGeoCodeResult result) {
        if (isDraw) {
            String distance1 = sortList.get(0).get("distance");
            mDistanceTextView.setText(distance1 + "");
        } else {
            LatLng latLng = MyMapUtils.getLatLng(baseContext);
            double distance1 = DistanceUtil.getDistance(latLng,
                    result.getLocation());
            mDistanceTextView.setText((int) distance1 + "");
        }
    }

    /****
     * 底部显示数据
     *
     * @param result
     */
    private void bottomShowDatails(ReverseGeoCodeResult result) {
        mNameTextView.setText(sortList.get(0).get("name") + "");
        address = result.getAddress();
        mAddressTextView.setText(address);
        // result = null;
        if (!StringUtil.isEmpty(result)
                && !StringUtil.isEmpty(result.getLocation())) {
            lat = result.getLocation().latitude;
            lng = result.getLocation().longitude;
        }
    }

    /****
     * 发起导航
     */
    protected void navigation() {
        if (isDraw) {
            carNavigation();
        } else {
            if (lat != 0 && lng != 0) {
                personNavigation();
            }
        }
    }

    /***
     * 选择车的位置
     */
    protected void carAddress() {

        moveToCar();
    }

    /****
     * 选择人的位置
     */
    protected void person() {
        if (!loginaty) {
            gasstation_linearlayout_isyingchang.setVisibility(View.VISIBLE);
            isFirst();
            isDraw = false;
            mBaiduMap.setMyLocationEnabled(true);
            saveDraw();
            clearText();
            moveToPerson();
        }
    }

    protected void isFirst() {
        if (!isFirst) {
            isFirst = true;
        }
        if (!isF) {
            isF = true;
        }
    }

    /***
     * 保存选中状态
     */
    protected void saveDraw() {
        sharedPreferences.edit().putBoolean("isDraw", isDraw).commit();
    }

    /***
     * 清除text内容
     */
    protected void clearText() {
        mBaiduMap.clear();
        mDistanceTextView.setText("");
        mNameTextView.setText("");
        mAddressTextView.setText("");
    }

    /***
     * 重新设置marker的图标
     */
    protected void setIconMarker() {
        for (int i = 0; i < markerList.size(); i++) {
            markerList.get(i).setIcon(bitmap);
        }
    }

    /**
     * 点击marker时变色
     *
     * @param marker
     */
    protected void cleckMarker(Marker marker) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.jiayouzhan12x);
        mNameTextView.setText(marker.getTitle());
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromBitmap(bitmap);
        marker.setIcon(bitmapDescriptor);
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(marker
                .getPosition()));
        lat = marker.getPosition().latitude;
        lng = marker.getPosition().longitude;
    }

    /***
     * 被选中的加油站
     *
     * @param marker 选中的marker
     */
    private void checkedGasstation(Marker marker) {
        Bitmap bitmaps = BitmapFactory.decodeResource(getResources(),
                R.drawable.jiayouzhan12x);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory
                .fromBitmap(bitmaps);
        marker.setIcon(bitmapDescriptor);
        isF = false;
    }

    /***
     * 以人的位置导航
     */
    protected void personNavigation() {
        BaiduMapView baiduMapView = new BaiduMapView();
        baiduMapView.initMap(baseContext);
        baiduMapView.baiduNavigation(MyMapUtils.getLatLng(baseContext).latitude,
                MyMapUtils.getLatLng(baseContext).longitude,
                MyMapUtils.getAddress(baseContext), lat, lng, address);
    }

    /***
     * 以车的位置导航
     */
    protected void carNavigation() {
        BaiduMapView baiduMapView = new BaiduMapView();
        baiduMapView.initMap(baseContext);
        if (latlngBean != null && latlngBean.getLatLng() != null) {
            baiduMapView.baiduNavigation(latlngBean.getLatLng().latitude,
                    latlngBean.getLatLng().longitude, null, lat, lng, address);
        }

    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult arg0) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

        setAddress(result);
        setDistance(result);

    }

    /***
     * 计算被选中的地址 距离车或人的距离，并显示在界面
     *
     * @param result
     */
    private void setDistance(ReverseGeoCodeResult result) {
        // TODO Auto-generated method stub
        if (isDraw) {
            double distance = DistanceUtil.getDistance(latlngBean.getLatLng(),
                    result.getLocation());
            mDistanceTextView.setText((int) distance + "");
        } else {
            LatLng latLng = MyMapUtils.getLatLng(baseContext);
            double distance = DistanceUtil.getDistance(latLng,
                    result.getLocation());
            mDistanceTextView.setText((int) distance + "");
        }
    }

    /**
     * 显示被选中的marker地址
     *
     * @param result
     */
    private void setAddress(ReverseGeoCodeResult result) {
        // TODO Auto-generated method stub
        address = result.getAddress();
        mAddressTextView.setText(address);
    }

    /****
     * json 解析
     *
     * @param result
     */
    private void pson(String result) {
        if (result == null) {
        } else {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.optString("operationState").equals("SUCCESS")) {
                    JSONObject jsonObject2 = jsonObject.optJSONObject("data");
                    JSONObject jsonObject3 = jsonObject2.optJSONObject("data");
                    if (jsonObject3.optString("status").equals("Success")) {
                        JSONArray jsonArray = jsonObject3
                                .optJSONArray("pointList");
                        analysis(jsonArray);
                        sort();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * 解析json数组
     *
     * @param jsonArray
     */
    private void analysis(JSONArray jsonArray) {
        // TODO Auto-generated method stub
        mBaiduMapView.clearBounds();
        listmMaps = new ArrayList<Map<String, String>>();
        for (int i = 0; i < jsonArray.length(); i++) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            String name = jsonArray.optJSONObject(i).optString("name");
            String cityName = jsonArray.optJSONObject(i).optString("cityName");
            String lat = jsonArray.optJSONObject(i).optJSONObject("location")
                    .optString("lat");
            String lng = jsonArray.optJSONObject(i).optJSONObject("location")
                    .optString("lng");
            String address = jsonArray.optJSONObject(i).optString("address");
            String distance = jsonArray.optJSONObject(i).optString("distance");
            String district = jsonArray.optJSONObject(i).optString("district");
            hashMap.put("name", name);
            hashMap.put("cityName", cityName);
            hashMap.put("lat", lat);
            hashMap.put("lng", lng);
            hashMap.put("address", address);
            hashMap.put("distance", distance);
            hashMap.put("district", district);
            listmMaps.add(hashMap);
        }
    }


    /***
     * 解析加油站
     */
    private void analysis(SearchResponse response) {
        mBaiduMapView.clearBounds();
        listmMaps = new ArrayList<Map<String, String>>();
        for (int i = 0; i < response.getMsg().size(); i++) {
            HashMap<String, String> hashMap = new HashMap<String, String>();
            String name = response.getMsg().get(i).getName();
            String cityName = response.getMsg().get(i).getCityName();
            String lat = "" + response.getMsg().get(i).getLocation().getLat();
            String lng = "" + response.getMsg().get(i).getLocation().getLng();
            String address = response.getMsg().get(i).getAddress();
            String distance = "" + response.getMsg().get(i).getDistance();
            String district = response.getMsg().get(i).getDistrict();
            hashMap.put("name", name);
            hashMap.put("cityName", cityName);
            hashMap.put("lat", lat);
            hashMap.put("lng", lng);
            hashMap.put("address", address);
            hashMap.put("distance", distance);
            hashMap.put("district", district);
            listmMaps.add(hashMap);
        }
    }


    /***
     * 排序，按照距离进行顺序排序
     */
    private void sort() {
        sort(listmMaps);
    }

    List<Map<String, String>> sortList;

    /***
     * 得到位置通过反编译得到地址显示出来
     */
    private void calculateAddressandDistance() {
        if (isFirst) {
            double lat = StringUtil.getDouble(sortList.get(0).get("lat")
                    .toString());
            double lng = StringUtil.getDouble(sortList.get(0).get("lng")
                    .toString());
            mSeGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(new LatLng(lat, lng)));
            isFirst = false;
        }
    }

    /***
     * 根据距离 顺序 排出对象的顺序
     *
     * @param dis
     * @param objects
     */
    private void sortDistance(double[] dis, Object[] objects) {
        for (int i = 0; i < dis.length; i++) {
            @SuppressWarnings("unchecked")
            Map<String, String> map = (Map<String, String>) objects[i];
            map.put("distance", (int) dis[i] + "");
            sortList.add(map);
        }
        for (int i = 0; i < sortList.size(); i++) {
            double lat = Double.parseDouble(sortList.get(i).get("lat"));
            double lng = Double.parseDouble(sortList.get(i).get("lng"));
            LatLng latlng = new LatLng(lat, lng);
            mBaiduMapView.moveBounds(latlng);
            moveLatLng(latlng, i);
            list.add(latlng);
        }

        markerList.get(0).setToTop();
    }

    /***
     * 每次都创建实例
     */
    private void isSortListNll() {
        if (sortList == null) {
            sortList = new ArrayList<Map<String, String>>();
        } else {
            sortList = null;
            sortList = new ArrayList<Map<String, String>>();
        }
    }

    /**
     * 冒泡排序
     *
     * @param distanceBeans 原数据
     * @param objects       对象数组
     * @param dis           距离数组
     */
    private void personObject(List<DistanceBeanNative> distanceBeans,
                              Object[] objects, double[] dis) {
        int n = distanceBeans.size();
        for (int i = 0; i < n; i++) {
            dis[i] = distanceBeans.get(i).getGetDistance();
            objects[i] = distanceBeans.get(i).getMap();
        }

        for (int i = 0; i < dis.length - 1; i++) {
            for (int j = 0; j < dis.length - 1 - i; j++) {
                if (dis[j] > dis[j + 1]) {
                    double temp = dis[j];
                    dis[j] = dis[j + 1];
                    dis[j + 1] = temp;
                    Object tempObject = objects[j];
                    objects[j] = objects[j + 1];
                    objects[j + 1] = tempObject;
                }
            }
        }
    }

    /***
     * 计算距离人的距离
     *
     * @param distanceBeans
     * @param listmMaps2
     */
    private void personDistance(List<DistanceBeanNative> distanceBeans,
                                List<Map<String, String>> listmMaps2) {
        for (int i = 0; i < listmMaps2.size(); i++) {
            DistanceBeanNative distanceBean = new DistanceBeanNative();
            double lat = StringUtil.getDouble(listmMaps2.get(i).get("lat"));
            double lng = StringUtil.getDouble(listmMaps2.get(i).get("lng"));
            if (isDraw) {
                double distance = DistanceUtil.getDistance(
                        new LatLng(lat, lng), latlngBean.getLatLng());
                distanceBean.setGetDistance(distance);
            } else {
                double distance = DistanceUtil.getDistance(
                        new LatLng(lat, lng), MyMapUtils.getLatLng(baseContext));
                distanceBean.setGetDistance(distance);

            }
            distanceBean.setMap(listmMaps2.get(i));
            distanceBeans.add(distanceBean);
        }

    }

    /**
     * 以车的位置排序加油站
     */
    private void sort(List<Map<String, String>> listmMaps) {
        // TODO Auto-generated method stub
        List<DistanceBeanNative> distanceBeans = new ArrayList<DistanceBeanNative>();
        personDistance(distanceBeans, listmMaps);
        Object[] objects = new Object[distanceBeans.size()];
        double[] dis = new double[distanceBeans.size()];
        personObject(distanceBeans, objects, dis);
        // 每次都创建实例
        isSortListNll();
        // 根据距离 顺序 排出对象的顺序
        sortDistance(dis, objects);
        // 得到位置通过反编译得到地址显示出来
        calculateAddressandDistance();
    }

    /***
     * 获得人的位置
     */
    protected void moveToPerson() {
        double lati = MyMapUtils.getLatitude(baseContext);
        double longi = MyMapUtils.getLongitude(baseContext);
        personLatLng = new LatLng(lati, longi);
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMapView.updateOritentation(personLatLng,
                R.drawable.chedongtai_person, 20, 20);
        // mBaiduMapView.setMarker(personLatLng, R.drawable.chedongtai_person);
        // moveLatlng(personLatLng);
        mBaiduMapView.moveLatLng(personLatLng);
        ProgrosDialog.openDialog(baseContext);
        request(personLatLng);
    }

    /***
     * 获得车的经纬度
     */
    protected void moveToCar() {
        getLalng();
    }

    private void getLalng() {
        if (isLogined()) {
            if (!StringUtil.isEmpty(BaseActivity.loginResponse.getMsg().getDefaultVehicle())
                    && !StringUtil.isEmpty(BaseActivity.loginResponse.getMsg().getDefaultDeviceNo())) {

                // TODO 调用车辆动态vehicleTrends接口
//                RequestParams params = new RequestParams();
//                params.addBodyParameter("cid", getcid());
//                params.addBodyParameter("uid", getUid());
//                params.addBodyParameter("mobile", getMobile());
//
//                httpBiz = new HttpBiz(baseContext);
//                ProgrosDialog.openDialog(baseContext);
//                isFirst();
//                clearText();
//                mBaiduMap.setMyLocationEnabled(false);
//                isDraw = true;
//                saveDraw();
//                httpBiz.httPostData(CAR_CODE, API.CAR_DYNAMIC_URL, params, this);


                ProgrosDialog.openDialog(baseContext);
                isFirst();
                clearText();
                mBaiduMap.setMyLocationEnabled(false);
                isDraw = true;
                saveDraw();
                String url = NetInterface.BASE_URL + NetInterface.TEMP_OBD + NetInterface.DYNAMIC + NetInterface.SUFFIX;
                Map<String, Object> param = new HashMap<>();
                param.put("userId", loginResponse.getDesc());
                param.put("token", loginResponse.getToken());
                param.put("deviceNo", loginResponse.getMsg().getDefaultDeviceNo());
                param.put(Constant.PARAMETER_TAG, NetInterface.DYNAMIC);
                netWorkHelper.PostJson(url, param, this);

            } else {
                isBand();
            }
        } else {
            isLogin();
        }

    }

    private void isBand() {
        if (login_type > 1) {
            MapMenssageDialog.OpenDialog(baseContext,
                    getString(R.string.no_band_gasstation));
            loginaty = true;
            mpersonButton.setChecked(true);
        } else {
            mpersonButton.setChecked(true);
        }
    }

    private void isLogin() {
        if (login_type > 1) {
            MapMenssageDialog.OpenDialog(baseContext,
                    getString(R.string.no_login_gasstation));
            loginaty = true;
            mpersonButton.setChecked(true);
        } else {
            mpersonButton.setChecked(true);
        }

    }


    /***
     * 在地图上标记车 的位置
     *
     * @param latLng
     */
    private void getMycar(LatLng latLng) {
        mBaiduMapView.moveLatLng(latLng);
        BitmapDescriptor myBitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.drawable.chedongtai_car2x);
        OverlayOptions option = new MarkerOptions().position(latLng).icon(
                myBitmapDescriptor);
        mBaiduMap.addOverlay(option);
    }

    OnGetGeoCoderResultListener getGeoCoderResultListener = new OnGetGeoCoderResultListener() {

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {

            if (!StringUtil.isEmpty(arg0) && null != arg0.getLocation()) {
//					&& !StringUtil.isEmpty(arg0.getAddressDetail())
//					&& !StringUtil.isEmpty(arg0.getAddressDetail().district)) {
//				String cityname = arg0.getAddressDetail().district;
//				RequestParams params = new RequestParams();
//				params.addBodyParameter("cityName", cityname);
//				params.addBodyParameter("keyWord",
//						getString(R.string.gasstationlist_address));
//				params.addBodyParameter("lat", arg0.getLocation().latitude + "");
//				params.addBodyParameter("lon", arg0.getLocation().longitude
//						+ "");
//				params.addBodyParameter("size", 20 + "");
//				params.addBodyParameter("page", 0 + "");
//				ProgrosDialog.openDialog(baseContext);
//				httpBiz = new HttpBiz(baseContext);
//				httpBiz.httPostData(GASS_CODE, API.GASSTATIONLIST_URL, params,
//						GasStationMapFragment.this);

                ProgrosDialog.openDialog(baseContext);
                String url = NetInterface.BASE_URL + NetInterface.TEMP_SEARCH + NetInterface.SEARCH + NetInterface.SUFFIX;
                Map<String, Object> param = new HashMap<>();
                param.put("userId", loginResponse.getDesc());
                param.put("token", loginResponse.getToken());
                param.put("keyWord", "加油站");
                param.put("longitude", arg0.getLocation().longitude);
                param.put("latitude", arg0.getLocation().latitude);
                param.put(Constant.PARAMETER_TAG, NetInterface.SEARCH);
                netWorkHelper.PostJson(url, param, GasStationMapFragment.this);
            } else {
                showToast("定位失败");
                ProgrosDialog.closeProgrosDialog();
            }
        }

        @Override
        public void onGetGeoCodeResult(GeoCodeResult arg0) {

        }
    };

    /***
     * 通过经纬度反编译得到所在地的区县
     *
     * @param latLng
     */
    public void request(LatLng latLng) {
        mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mLocationUtil.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mLocationUtil.onStop();
    }

    @Override
    public void onDestroy() {
        getGeoCoderResultListener = null;
        mLocationUtil.onDestory();
        mLocationUtil = null;
        LocationListener = null;
        mBaiduMap.setMyLocationEnabled(false);
        mBaiduMap.clear();
        mMapView.onDestroy();
        mMapView = null;
        mAddressTextView = null;
        mSearch.destroy();
        mGeoCoder.destroy();
        mSeGeoCoder.destroy();
        bitmap.recycle();
        bitmap = null;
        mBaiduMapView.onDestory();
        System.gc();
        super.onDestroy();
    }


    @Override
    public void receive(String TAG, String data) {
        ProgrosDialog.closeProgrosDialog();
        switch (TAG) {
            case NetInterface.SEARCH:
                SearchResponse response = (SearchResponse) GsonUtil.getInstance().convertJsonStringToObject(data, SearchResponse.class);
                if (null == response || !response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    showToast(response.getDesc());
                    return;
                }

                analysis(response);
                sort();

                loginResponse.setToken(response.getToken());
//                LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
                break;
            case NetInterface.DYNAMIC: // 车辆定位

                CarDynamicResponse carDynamicResponse = (CarDynamicResponse) GsonUtil.getInstance().convertJsonStringToObject(data, CarDynamicResponse.class);
                if (!carDynamicResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    showToast(R.string.server_link_fault);
                    return;
                }

                if (null != carDynamicResponse.getMsg())
                    isLatlng(carDynamicResponse.getMsg().getLat(), carDynamicResponse.getMsg().getLng());
                else
                    showToast(R.string.gain_car_address_error);
                loginResponse.setToken(carDynamicResponse.getToken());
//                LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
                break;
        }
    }

    private void isLatlng(double lat, double lon) {
        if (lat != 0 && lon != 0) {
            LatLng latLng = new LatLng(lat, lon);
            latlngBean.setLatLng(latLng);
            getMycar(latLng);
            request(latLng);
        }
    }

    @Override
    public void receive(int type, String data) {
        super.receive(type, data);
        switch (type) {
            case CAR_CODE:
                ProgrosDialog.closeProgrosDialog();
                getcarAddress(data);
                break;
            case GASS_CODE:
                ProgrosDialog.closeProgrosDialog();
                pson(data);
                break;
            default:
                break;
        }
    }

    private void getcarAddress(String data) {
        gasstation_linearlayout_isyingchang.setVisibility(View.VISIBLE);
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (StringUtil.isEquals(API.returnSuccess,
                    jsonObject.optString("state"), true)) {
                JSONObject jsonObject2 = jsonObject.optJSONObject("data");
                // JSONObject jsonObject3 = jsonObject2.optJSONObject("data");
                // JSONObject jsonObject4 = jsonObject3.optJSONObject("body");
                // try {
                // jsonObject4.getString("result");
                // showToast(getString(R.string.gain_car_address_error));
                // ProgrosDialog.closeProgrosDialog();
                // gasstation_linearlayout_isyingchang
                // .setVisibility(View.GONE);
                // } catch (Exception e) {

                // try {
                double lat = jsonObject2.optDouble("lat");
                double lng = jsonObject2.optDouble("lon");
                if (lat != 0 && lng != 0) {
                    LatLng latLng = new LatLng(lat, lng);
                    latlngBean.setLatLng(latLng);
                    getMycar(latLng);
                    request(latLng);
                    // } else {
                    // showToast(getString(R.string.gain_car_address_error));
                    // ProgrosDialog.closeProgrosDialog();
                    // }
                    // } catch (Exception e1) {
                    // ProgrosDialog.closeProgrosDialog();
                    // }
                }
            } else {
                ProgrosDialog.closeProgrosDialog();
                if (StringUtil.isEquals(jsonObject.optString("state"),
                        API.returnRelogin, true)) {
                    ReLoginDialog.getInstance(baseContext).showDialog(
                            jsonObject.getString("message"));
                } else {
                    showToast(jsonObject.optString("message"));
                }
                gasstation_linearlayout_isyingchang.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            ProgrosDialog.closeProgrosDialog();
        }

    }

}
