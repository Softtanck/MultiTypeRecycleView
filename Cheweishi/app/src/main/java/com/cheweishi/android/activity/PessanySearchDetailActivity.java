package com.cheweishi.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.cheweishi.android.R;
import com.cheweishi.android.entity.PessanyResponse;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by Tanck on 2016/4/10.
 */
public class PessanySearchDetailActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.left_action)
    private Button left_action;
    @ViewInject(R.id.tv_pessany_detail_plate)
    private TextView tv_pessany_detail_plate;
    @ViewInject(R.id.tv_pessany_detail_score)
    private TextView tv_pessany_detail_score;
    @ViewInject(R.id.tv_pessany_detail_money)
    private TextView tv_pessany_detail_money;
    @ViewInject(R.id.tv_pessany_detail_content)
    private TextView tv_pessany_detail_content;
    @ViewInject(R.id.tv_pessany_detail_address)
    private TextView tv_pessany_detail_address;
    @ViewInject(R.id.tv_pessany_detail_site)
    private TextView tv_pessany_detail_site;
    @ViewInject(R.id.tv_pessany_detail_date)
    private TextView tv_pessany_detail_date;
    @ViewInject(R.id.pessany_map)
    private MapView mapView;

    private PessanyResponse.MsgBean data;
    private BaiduMap baiduMap;
    private BitmapDescriptor bitmapDescriptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pessany_detail);
        ViewUtils.inject(this);
        data = (PessanyResponse.MsgBean) getIntent().getSerializableExtra("PessanySearchNative");
        if (null == data) {
            showToast("页面初始化失败,请重试");
            return;
        }
        left_action.setText(R.string.back);

        // TODO 暂时未加上
//        intMap(data.getLat(), data.getLng());
        setdata(data);
    }

    private void intMap(double Latitude, double Longitude) {
        baiduMap = mapView.getMap();
        LatLng latLng = new LatLng(Latitude, Longitude);
        baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
        bitmapDescriptor = BitmapDescriptorFactory
                .fromResource(R.drawable.jiuyuan_chepaihao2x);
        OverlayOptions ooA = new MarkerOptions().position(latLng)
                .icon(bitmapDescriptor).zIndex(9).draggable(true);
        baiduMap.addOverlay(ooA);
    }


    private void setdata(PessanyResponse.MsgBean data) {
        tv_pessany_detail_plate.setText(data.getPlate());
        tv_pessany_detail_score.setText("" + data.getScore());
        tv_pessany_detail_money.setText("" + data.getFinesAmount());
        tv_pessany_detail_address.setText(data.getIllegalAddress());
        tv_pessany_detail_content.setText(data.getIllegalContent());
        tv_pessany_detail_date.setText(data.getIllegalDate());
        tv_pessany_detail_site.setText(data.getProcessingSite());
    }

    @OnClick({R.id.left_action})
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.left_action:
                finish();
                break;
        }
    }
}
