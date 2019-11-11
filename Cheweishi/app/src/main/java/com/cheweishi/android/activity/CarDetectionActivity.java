package com.cheweishi.android.activity;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.CarStatusAdapter;
import com.cheweishi.android.adapter.ChangeCarAdapter;
import com.cheweishi.android.biz.HttpBiz;
import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.config.API;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.CarDetectionResponse;
import com.cheweishi.android.tools.DialogTool;
import com.cheweishi.android.tools.LoginMessageUtils;
import com.cheweishi.android.tools.ReLoginDialog;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.MyUnSlidingListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车辆检测
 *
 * @author mingdasen
 */
@ContentView(R.layout.activity_car_detection)
public class CarDetectionActivity extends BaseActivity {

    @ViewInject(R.id.left_action)
    private Button left_action;

    @ViewInject(R.id.title)
    private TextView title;

    @ViewInject(R.id.right_action)
    private TextView right_action;

    @ViewInject(R.id.tv_detdction_plate)
    private TextView tv_detdction_plate;// 车牌号

    @ViewInject(R.id.img_car_logo)
    private ImageView img_car_logo;// 车辆图标

    @ViewInject(R.id.tv_car_state)
    private TextView tv_car_state;// 车辆行驶总里程和总时间

    @ViewInject(R.id.btn_security_scan)
    private TextView btn_security_scan;// 安全扫描按钮

    @ViewInject(R.id.tv_trip_date)
    private TextView tv_trip_date;// 报告时间

    @ViewInject(R.id.oil_wear)
    private TextView oil_wear;// 当日油耗

    @ViewInject(R.id.tv_speed)
    private TextView tv_speed;// 平均速度

    @ViewInject(R.id.tv_average_iol)
    private TextView tv_average_iol;// 平均油耗

    @ViewInject(R.id.tv_mile)
    private TextView tv_mile;// 里程

    @ViewInject(R.id.tv_date)
    private TextView tv_date;// 时间

    private DeteBroadcastReceiver broad;
    private String deviceNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        initView();
    }

    private void initView() {
        left_action.setText(R.string.back);
        title.setText(R.string.mycar);
        right_action.setText(R.string.xlistview_footer_hint_normal);

        initData();
    }

    private void initData() {
//        if (isLogined() && hasDevice()) {
        String plate = getIntent().getStringExtra("devicePlate");
        String icon = getIntent().getStringExtra("deviceIcon");
        if (StringUtil.isEmpty(plate))
            plate = loginResponse.getMsg().getDefaultVehiclePlate();
        tv_detdction_plate.setText(plate);
        if (StringUtil.isEmpty(icon)) {
            if (hasBrandIcon()) {
                XUtilsImageLoader.getxUtilsImageLoader(this,
                        R.drawable.tianjiacar_img2x, img_car_logo,
                        loginResponse.getMsg().getDefaultVehicleIcon());
            } else {
                img_car_logo.setImageResource(R.drawable.main_logo);
            }
        } else {
            XUtilsImageLoader.getxUtilsImageLoader(this,
                    R.drawable.tianjiacar_img2x, img_car_logo,
                    icon);
        }
//        }
        msg.add(0, getResources().getString(R.string.car_dynamic));
        getCarReport();
    }

    /**
     * 获取车辆报告
     */
    private void getCarReport() {
        deviceNo = getIntent().getStringExtra("deviceNo");
        if (StringUtil.isEmpty(deviceNo)) {
            if (hasDevice() && null != loginResponse.getMsg().getDefaultDeviceNo()) {
                deviceNo = loginResponse.getMsg().getDefaultDeviceNo();
            } else {
                showToast("当前没有获取到设备id");
                return;
            }
        }
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_OBD + NetInterface.DETECTION + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("deviceNo", deviceNo);
        param.put("searchDate", StringUtil.getDate(StringUtil.getLastDate(), "yyyy-MM-dd"));
        netWorkHelper.PostJson(url, param, this);
    }

    private int getMinu(int data) {
        if (0 >= data)
            return 0;
        if (60 > data)
            return 1;
        data = data / 60;
        return data;
    }

    @Override
    public void receive(String data) {
        ProgrosDialog.closeProgrosDialog();
        CarDetectionResponse response = (CarDetectionResponse) GsonUtil.getInstance().convertJsonStringToObject(data, CarDetectionResponse.class);
        if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            showToast(R.string.server_link_fault);
            return;
        }

        CarDetectionResponse.MsgBean msg = response.getMsg();

        try {
            String oil = String.valueOf(msg.getFuelConsumption());
            String avgOil = String.valueOf(msg.getAverageFuelConsumption());
            String avgSpeed = String.valueOf(msg.getAverageSpeed());
            String time = String.valueOf(getMinu(msg.getRunningTime())); // 计算分钟
            String licheng = String.valueOf(msg.getMileAge());
            String total = String.valueOf(msg.getTotalMileAge());


            tv_car_state.setText("总里程" + total + "km");

            if (StringUtil.isEmpty(oil)) {
                oil_wear.setText("--");
            } else {
                oil_wear.setText(oil);
            }
            if (StringUtil.isEmpty(avgOil)) {
                tv_average_iol.setText("--" + "升/百公里");
            } else {
                tv_average_iol.setText(avgOil + "升/百公里");
            }
            if (StringUtil.isEmpty(avgSpeed)) {
                tv_speed.setText("--");
            } else {
                tv_speed.setText(avgSpeed);
            }
            if (StringUtil.isEmpty(time)) {
                tv_date.setText("--");
            } else {
                tv_date.setText(time + "分钟");
            }
            if (StringUtil.isEmpty(licheng)) {
                tv_mile.setText("--");
            } else {
                tv_mile.setText(licheng + "公里");
            }
            tv_trip_date.setText(StringUtil.getDate(
                    StringUtil.getLastDate(), "yyyy-MM-dd"));
        } catch (Exception e) {
        }
        loginResponse.setToken(response.getToken());
        LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
    }


    @Override
    public void receive(int type, String data) {
        super.receive(type, data);
        Log.i("result", "====报告数据===" + data);
        ProgrosDialog.closeProgrosDialog();
        switch (type) {
            case 10000:
                parseReport(data);
                break;
            case 400:
                showToast(R.string.FAIL);
                break;
        }
    }

    /**
     * 报告数据解析
     *
     * @param data
     */
    private void parseReport(String data) {
        try {
            JSONObject json = new JSONObject(data);
            if (StringUtil.isEquals(API.returnSuccess, json.optString("state"),
                    true)) {
                json = json.getJSONObject("data");

                String avgSpeed = json.optString("avgSpeed");
                String oil = json.optString("oil");
                String licheng = json.optString("mile");

                String time = json.optString("feeTime");
                String fee = json.optString("fee");
                String avgOil = json.optString("avgOil");
                Log.i("zzqq", "carreport===good1");
                String drivingScore = json.optString("drivingScore");
                Log.i("zzqq", "carreport===good2");
                // rid = json.optInt("rid");
                Log.i("zzqq", "carreport===good3");
                String status = json.optString("status");

                if (StringUtil.isEmpty(oil)) {
                    oil_wear.setText("--");
                } else {
                    oil_wear.setText(oil);
                }
                if (StringUtil.isEmpty(avgOil)) {
                    tv_average_iol.setText("--" + "升/百公里");
                } else {
                    tv_average_iol.setText(avgOil + "升/百公里");
                }
                if (StringUtil.isEmpty(avgSpeed)) {
                    tv_speed.setText("--");
                } else {
                    tv_speed.setText(avgSpeed);
                }
                if (StringUtil.isEmpty(time)) {
                    tv_date.setText("--");
                } else {
                    tv_date.setText(time + "分钟");
                }
                if (StringUtil.isEmpty(licheng)) {
                    tv_mile.setText("--");
                } else {
                    tv_mile.setText(licheng + "公里");
                }
                tv_trip_date.setText(StringUtil.getDate(
                        StringUtil.getLastDate(), "yyyy-MM-dd"));

            } else if (StringUtil.isEquals(API.returnRelogin,
                    json.optString("state"), true)) {
                ReLoginDialog.getInstance(this).showDialog(
                        json.optString("message"));
            } else {
                showToast(json.optString("message"));
            }
        } catch (Exception e) {
        }
    }

    @OnClick({R.id.left_action, R.id.right_action, R.id.btn_security_scan,
            R.id.rl_trip_date})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_action:// 返回
                finish();
                break;
            case R.id.right_action:// 车辆列表
//                startActivity(new Intent(CarDetectionActivity.this,
//                        CarManagerActivity.class));
                showPopupWindow(v);
                break;

            case R.id.btn_security_scan:// 安全扫描
                if (StringUtil.isEmpty(deviceNo)) {
                    if (hasDevice()) {
                        startActivity(new Intent(CarDetectionActivity.this,
                                SecurityScanActivity.class));
                    } else {
                        showToast("未绑定设备");
                    }
                } else {
                    Intent intent = new Intent(CarDetectionActivity.this, SecurityScanActivity.class);
                    intent.putExtra("deviceNo", deviceNo);
                    startActivity(intent);
                }
                break;
            case R.id.rl_trip_date:// 车辆报告
                Intent intent = new Intent(CarDetectionActivity.this, CarReportActivity.class);
                intent.putExtra("deviceNo", deviceNo);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (broad == null) {
            broad = new DeteBroadcastReceiver();
        }

        IntentFilter intentFilter = new IntentFilter(Constant.REFRESH_FLAG);
        registerReceiver(broad, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broad);
    }

    private class DeteBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (StringUtil.isEquals(Constant.CURRENT_REFRESH,
                    Constant.CAR_MANAGER_REFRESH, true)) {
                initView();
            }
        }

    }


    private MyUnSlidingListView listView;
    private CarStatusAdapter carstatusAdapter;
    private PopupWindow popupWindow;
    private List<String> msg = new ArrayList<>();

    private void showPopupWindow(View down) {
        if (null == popupWindow) {
            View view = View.inflate(baseContext, R.layout.change_car, null);
//            int screen = ScreenTools.getScreentWidth(PessanySearchActivity.this);
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            listView = (MyUnSlidingListView) view.findViewById(R.id.lv_change_car);
        }
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            return;
        }
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        carstatusAdapter = new CarStatusAdapter(baseContext, msg);
        listView.setAdapter(carstatusAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                if (popupWindow != null)
                    popupWindow.dismiss();

                // TODO 进入车辆动态界面
                Intent intent = new Intent(baseContext, CarDynamicActivity.class);
                intent.putExtra("deviceNo", deviceNo);
                startActivity(intent);
            }
        });
//        popupWindow.showAsDropDown(down, ScreenTools.getScreentWidth(PessanySearchActivity.this) / 10, 0);
        popupWindow.showAsDropDown(down, (int) (down.getWidth() / 2.5), 0);
    }

}
