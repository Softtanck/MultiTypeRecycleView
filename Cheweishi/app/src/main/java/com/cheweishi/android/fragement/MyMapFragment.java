package com.cheweishi.android.fragement;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.cheweishi.android.R;
import com.cheweishi.android.utils.MyMapUtils;
import com.cheweishi.android.widget.BaiduMapView;

/**
 * 现在没有用到
 *
 * @author zhangq
 */
public class MyMapFragment extends BaseFragment {
    private MapView mMapView;
    private CheckBox cBoxTraffic;// 交通路况
    private RadioButton cBoxCarLocation;// 车的位置
    private RadioButton cBoxPersonLocation;// 人的位置

    private BaiduMapView mBaiduMapView;
    private LatLng carLatLng;// 车位置
    private LatLng personLatLng;// 人位置

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_car_dynamic, container,
                false);
        initView(view);
        setMapInfo();
        return view;
    }

    // @Override
    // public void onActivityCreated(Bundle savedInstanceState) {
    // super.onActivityCreated(savedInstanceState);
    // //判断账号是否被T或是在别处登陆
    // if(savedInstanceState != null &&
    // savedInstanceState.getBoolean("isConflict", false))
    // return;
    // }
    private void initView(View view) {
        mMapView = (MapView) view.findViewById(R.id.bmapView);
        cBoxTraffic = (CheckBox) view.findViewById(R.id.cbox_traffic);
        cBoxCarLocation = (RadioButton) view.findViewById(R.id.cbox_car_location);
        cBoxPersonLocation = (RadioButton) view.findViewById(R.id.cbox_person_location);

        cBoxTraffic.setOnCheckedChangeListener(checkedChangeListener);
        cBoxCarLocation.setOnCheckedChangeListener(checkedChangeListener);
        cBoxPersonLocation.setOnCheckedChangeListener(checkedChangeListener);
    }

    private void setMapInfo() {
        mMapView.showZoomControls(false);
        mBaiduMapView = new BaiduMapView(mMapView, baseContext);

        getLocation();

        mBaiduMapView.setMarker(null, R.drawable.chedongtai_car2x);
        // overManager.zoomToSpan();
        cBoxPersonLocation.setChecked(true);
        mBaiduMapView.zoomTo(17);
    }

    private void getLocation() {
        double lati = MyMapUtils.getLatitude(baseContext);
        double longi = MyMapUtils.getLongitude(baseContext);
        carLatLng = new LatLng(lati, longi);
        personLatLng = new LatLng(lati, longi);
    }

    OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton cButton, boolean isChecked) {
            switch (cButton.getId()) {
                case R.id.cbox_traffic:
                    mBaiduMapView.setTraffic(isChecked);
                    break;
                case R.id.cbox_car_location:
                    if (isChecked) {
                        cBoxPersonLocation.setChecked(false);
                        cBoxCarLocation.setChecked(true);
                        getLocation();
                        mBaiduMapView.moveLatLng(carLatLng);
                    }
                    break;
                case R.id.cbox_person_location:
                    if (isChecked) {
                        cBoxCarLocation.setChecked(false);
                        cBoxPersonLocation.setChecked(true);
                        getLocation();
                        mBaiduMapView.moveLatLng(personLatLng);
                    }
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mBaiduMapView.onDestory();
    }

}
