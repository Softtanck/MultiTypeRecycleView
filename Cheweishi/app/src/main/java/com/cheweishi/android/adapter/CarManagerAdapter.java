package com.cheweishi.android.adapter;

import java.lang.ref.SoftReference;
import java.util.List;

import com.cheweishi.android.R;
import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.entity.MyCarManagerResponse;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.XCRoundImageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CarManagerAdapter extends PagerAdapter {
    private Context context;

    private SparseArray<View> views = new SparseArray<>();

    private List<MyCarManagerResponse.MsgBean> list;

    @ViewInject(R.id.tv_car_manager_item_carid)
    private TextView tv_car_manager_item_carid;

    //车牌
    @ViewInject(R.id.tv_car_manager_item_plate)
    private TextView tv_car_manager_item_plate;

    //车名字
    @ViewInject(R.id.tv_car_manager_item_carname)
    private TextView tv_car_manager_item_carname;

    //车图标
    @ViewInject(R.id.iv_car_manager_item_car_icon)
    private XCRoundImageView iv_car_manager_item_car_icon;

    //是否默认icon
    @ViewInject(R.id.iv_car_manager_item_default)
    private ImageView iv_car_manager_item_default;

    //行驶里程
    @ViewInject(R.id.tv_car_manager_item_mile)
    private TextView tv_car_manager_item_mile;

    //上次行驶里程
    @ViewInject(R.id.tv_car_manager_item_last_keepFit)
    private TextView tv_car_manager_item_last_keepFit;

    //下次年检时间
    @ViewInject(R.id.tv_car_manager_item_annualSurvey)
    private TextView tv_car_manager_item_annualSurvey;

    //交保险到期时间
    @ViewInject(R.id.tv_car_manager_item_trafficSurvey)
    private TextView tv_car_manager_item_trafficSurvey;

    //车架号
    @ViewInject(R.id.tv_car_manager_item_vin)
    private TextView tv_car_manager_item_vin;

    //车辆动态
    @ViewInject(R.id.ll_car_manager_item_car_dynamic)
    private LinearLayout ll_car_manager_item_car_dynamic;

    //一键检测
    @ViewInject(R.id.ll_car_manager_item_yjjc)
    private LinearLayout ll_car_manager_item_yjjc;

    //车名字
    @ViewInject(R.id.tv_car_manager_item_style)
    private TextView tv_car_manager_item_style;

    // 设备号
    @ViewInject(R.id.ll_car_manager_item_car_device)
    private LinearLayout ll_car_manager_item_car_device;

    //设备
    @ViewInject(R.id.tv_car_manager_item_device)
    private TextView tv_car_manager_item_device;

    public interface CarManagerListener {
        void onCarLineClick(int type, int position);
    }

    //车动态车数据
    private CarManagerListener listener;

    public CarManagerAdapter(Context context, List<MyCarManagerResponse.MsgBean> list, CarManagerListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        init();
    }

    public void setData(List<MyCarManagerResponse.MsgBean> list) {
        this.list = list;
        init();
        notifyDataSetChanged();
    }

    private void init() {
        if (null != list && null != context) {
            views.clear();
            View view = null;
            for (int i = 0; i < list.size(); i++) {
                SoftReference<View> softReference = Constant.carView.get(i);
                if (null == softReference) {
                    view = View.inflate(context, R.layout.car_manager_item, null);
                    Constant.carView.put(i, new SoftReference<View>(view));
                } else {
                    view = softReference.get();
                    if (null == view) {
                        view = View.inflate(context, R.layout.car_manager_item, null);
                        Constant.carView.put(i, new SoftReference<View>(view));
                    }
                }
                views.put(i, view);
            }
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position);
        if (null != view.getParent()) {
            ((ViewGroup)view.getParent()).removeView(view);
        }
        handlerView(view, position);
        container.addView(view);
        return view;
    }

    private void handlerView(View view, final int p) {
        ViewUtils.inject(this, view);
        XUtilsImageLoader.getxUtilsImageLoader(context, R.drawable.tianjiacar_img2x, iv_car_manager_item_car_icon, list.get(p).getBrandIcon());
        tv_car_manager_item_carid.setText(list.get(p).getPlate());
        tv_car_manager_item_plate.setText(list.get(p).getPlate());
        tv_car_manager_item_carname.setText(list.get(p).getVehicleFullBrand());
        tv_car_manager_item_style.setText(list.get(p).getVehicleFullBrand());
        if (list.get(p).isDefault())
            iv_car_manager_item_default.setVisibility(View.VISIBLE);
        else
            iv_car_manager_item_default.setVisibility(View.GONE);
        tv_car_manager_item_mile.setText(list.get(p).getDriveMileage());
        tv_car_manager_item_last_keepFit.setText(list.get(p).getLastMaintainMileage());
        tv_car_manager_item_annualSurvey.setText(list.get(p).getNextAnnualInspection());
        tv_car_manager_item_trafficSurvey.setText(list.get(p).getTrafficInsuranceExpiration());
        tv_car_manager_item_vin.setText(list.get(p).getVehicleNo());
        if (!StringUtil.isEmpty(list.get(p).getDeviceNo())) {
            ll_car_manager_item_car_device.setVisibility(View.VISIBLE);
            tv_car_manager_item_device.setText(list.get(p).getDeviceNo());
        } else {
            ll_car_manager_item_car_device.setVisibility(View.GONE);
        }

//        ll_car_manager_item_car_dynamic.setTag(p);
//        ll_car_manager_item_yjjc.setTag(p);
        ll_car_manager_item_car_dynamic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCarLineClick(0, p);
            }
        });
        ll_car_manager_item_yjjc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCarLineClick(1,p);
            }
        });

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }


//    @Override
//    public void onClick(View v) {
//        if (null == listener)
//            return;
//        switch (v.getId()) {
//            case R.id.ll_car_manager_item_car_dynamic: // 车辆动态
//                listener.onCarLineClick(0, (Integer) ll_car_manager_item_car_dynamic.getTag());
//                break;
//            case R.id.ll_car_manager_item_yjjc: // 一键检测
//                listener.onCarLineClick(1, (Integer) ll_car_manager_item_yjjc.getTag());
//                break;
//        }
//    }


}
