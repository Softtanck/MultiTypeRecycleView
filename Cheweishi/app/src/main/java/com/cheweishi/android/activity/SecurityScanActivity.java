package com.cheweishi.android.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.CarScanResponse;
import com.cheweishi.android.entity.DTCInfoNative;
import com.cheweishi.android.entity.DetectionInfoNative;
import com.cheweishi.android.utils.DisplayUtil;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.StringUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 安全扫描
 *
 * @author mingdasen
 */
@ContentView(R.layout.activity_security_scan)
public class SecurityScanActivity extends BaseActivity {

    @ViewInject(R.id.left_action)
    private Button left_action;

    @ViewInject(R.id.title)
    private TextView title;

    @ViewInject(R.id.tv_power)
    private TextView tv_power;// 动力系统

    @ViewInject(R.id.tv_body)
    private TextView tv_body;// 车身系统

    @ViewInject(R.id.tv_chassis)
    private TextView tv_chassis;// 底盘系统

    @ViewInject(R.id.tv_electric)
    private TextView tv_electric;// 电器设备

    @ViewInject(R.id.img_car)
    private ImageView img_car;// 检测车辆外壳

    // @ViewInject(R.id.img_car_frame)
    // private ImageView img_car_frame;//检测车辆结构框架

    @ViewInject(R.id.img_scan)
    private ImageView img_scan;// 扫描图片

    @ViewInject(R.id.rl_car_frame)
    private RelativeLayout rl_car_frame;// 扫描图片

    @ViewInject(R.id.rl_detection_state)
    private LinearLayout rl_detection_state;// 扫描点击按钮

    @ViewInject(R.id.pb_detectioning)
    private ProgressBar pb_detectioning;// 扫描中bar

    @ViewInject(R.id.tv_detection_state)
    private TextView tv_detection_state;// 扫描状态说明

    // @ViewInject(R.id.img_btn_icon)
    // private ImageView img_btn_icon;//扫描按钮右边箭头

    @ViewInject(R.id.ll_betection_reslut)
    private LinearLayout ll_betection_reslut;// 检测结果容器

    @ViewInject(R.id.btn_car_report)
    private Button btn_car_report;// 车辆报告

    @ViewInject(R.id.btn_see_details)
    private Button btn_see_details;// 查看详情

    @ViewInject(R.id.img)
    private ImageView img;// 带动

    private int DETECTION_STATE = 0;// 0表示没有检测、1表示检测中、2表示检测完成
    private int DETECTION_SUSSES = 0;// 0 表示检测失败、1、表示检测成功
    private int ANIMATION_END = 0;// 0表示动画未结束 1表示动画结束
    private int GET_DATA_STATE = 0;// 0表示数据请求未完成，1表示数据请求完成

    private int TIME = 2500;// 扫描一个系统的时间
    private int SCAN_TIME = 10000;// 扫描总时间
    private int SYSTEM = 0;// 表示车辆检测的第几个系统

    private Drawable drawable1;// 动力系统的状态图标
    private Drawable drawable2;// 底盘系统的状态图标
    private Drawable drawable3;// 车身系统的状态图标
    private Drawable drawable4;// 电器设备的状态图标

    private Drawable drawable_zhengchang;// 状态正常图标
    private Drawable drawable_weixian;// 状态危险图标

    private TranslateAnimation animation1, animation2;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    setSystemScanState(SYSTEM);
                    break;
                default:
                    break;
            }

        }

    };
    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            handler.sendEmptyMessage(0);
            handler.postDelayed(this, TIME);
        }
    };
    private CarScanResponse response;
    private String deviceNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        DETECTION_STATE = 0;
        initView();
    }

    protected void setSystemScanState(int system) {
        switch (system) {
            case 0:
                setTextViewDrawable(tv_power, drawable1);
                break;
            case 1:
                setTextViewDrawable(tv_chassis, drawable2);
                break;
            case 2:
                setTextViewDrawable(tv_body, drawable3);
                break;
            case 3:
                setTextViewDrawable(tv_electric, drawable4);
                handler.removeCallbacks(runnable);
                break;

            default:
                break;
        }
        SYSTEM++;
    }

    /**
     * TextView设置drawable图片
     */
    private void setTextViewDrawable(TextView tv, Drawable drawable) {
        if (StringUtil.isEmpty(drawable)) {
            tv.setCompoundDrawables(null, null, null, null);
        } else {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
            tv.setCompoundDrawables(null, drawable, null, null);
            tv.setCompoundDrawablePadding(DisplayUtil.dip2px(this, 5));
        }
    }

    /**
     * 判断各个系统状态
     */
    private void judgeSystemState() {
        getRes();
        drawable1 = drawable_zhengchang;
        drawable2 = drawable_zhengchang;
        drawable3 = drawable_zhengchang;
        drawable4 = drawable_zhengchang;
    }

    /**
     * 获取本地资源文件
     */
    private void getRes() {
        drawable_zhengchang = getResources().getDrawable(
                R.drawable.jiance_zhengchang);
        drawable_weixian = getResources()
                .getDrawable(R.drawable.jiance_wenxian);
    }

    private void initView() {
        left_action.setText(R.string.back);
        title.setText("安全扫描");
        judgeSystemState();
    }

    @OnClick({R.id.left_action, R.id.rl_detection_state, R.id.btn_car_report,
            R.id.btn_see_details})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_action:
                finish();
                break;

            case R.id.rl_detection_state:
                switch (DETECTION_STATE) {
                    case 0:// 立即扫描
                        startScan();
                        break;
                    case 1:// 扫描中
                        // scanning();
                        break;
                    case 2:// 扫描结束
                        // endScan();
                        break;
                }
                break;

            case R.id.btn_car_report:// 查看车辆报告
//                if (hasDevice()) {
                Intent intent = new Intent(SecurityScanActivity.this, CarReportActivity.class);
                intent.putExtra("deviceNo", deviceNo);
                startActivity(intent);
//                }
                break;

            case R.id.btn_see_details:// 检测详情
                // getDetectionData();
                seeDetectDetails();
                break;
            default:
                break;
        }
    }

    /**
     * 获取检测数据
     */
    private void getDetectionData() {
        deviceNo = getIntent().getStringExtra("deviceNo");
        if (StringUtil.isEmpty(deviceNo)) {
            if (hasDevice()) {
                deviceNo = loginResponse.getMsg().getDefaultDeviceNo();
            } else {
                return;
            }
        }

        String url = NetInterface.BASE_URL + NetInterface.TEMP_OBD + NetInterface.CAR_SCAN + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("deviceNo", deviceNo);
        netWorkHelper.PostJson(url, param, this);
    }


    @Override
    public void receive(String data) {
        ProgrosDialog.closeProgrosDialog();
        response = (CarScanResponse) GsonUtil.getInstance().convertJsonStringToObject(data, CarScanResponse.class);
        if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            showToast(R.string.server_link_fault);
            DETECTION_SUSSES = 0;
            endScan();
            GET_DATA_STATE = 2;
            return;
        }


        loginResponse.setToken(response.getToken());
//        LoginMessageUtils.saveloginmsg(baseContext, loginResponse);

        DETECTION_SUSSES = 1;
        endScan();
        GET_DATA_STATE = 1;
    }

    @Override
    public void error(String errorMsg) {
        ProgrosDialog.closeProgrosDialog();
        GET_DATA_STATE = 1;
        DETECTION_SUSSES = 0;
        showToast(R.string.server_link_fault);
    }


    // private ArrayList<String> infoList;// 检测信息
    // private ArrayList<AkeytestInfo> Akeyone;// 第一个
    // private ArrayList<AkeytestInfo> Akeytwo;// 第二个
    // private ArrayList<AkeytestInfo> Akeythere;// 第三个
    // private AkeyTextAllInfo mAkeyTextAllInfo;// 一键检测所有信息
    private List<DetectionInfoNative> detectionInfos;
    private List<DTCInfoNative> dtcInfos;


    /**
     * 查看详情
     */
    private void seeDetectDetails() {
        if (!StringUtil.isEmpty(response)) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", response);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            intent.setClass(SecurityScanActivity.this,
                    DetactionInfoActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 扫描结束
     */
    private void endScan() {
        if (DETECTION_SUSSES == 0 && ANIMATION_END == 1) {
            showDetectionAgain();
        } else if (DETECTION_SUSSES == 1 && ANIMATION_END == 1) {
            showDetectionResult();
        }
    }

    /**
     * 检测失败处理
     */
    private void showDetectionAgain() {
        showToast("车辆检测失败");
        tv_power.setVisibility(View.INVISIBLE);
        tv_body.setVisibility(View.INVISIBLE);
        tv_chassis.setVisibility(View.INVISIBLE);
        tv_electric.setVisibility(View.INVISIBLE);
        setTextViewDrawable(tv_power, null);
        setTextViewDrawable(tv_body, null);
        setTextViewDrawable(tv_chassis, null);
        setTextViewDrawable(tv_electric, null);
        tv_detection_state.setText("重新检查");
        pb_detectioning.setVisibility(View.GONE);
    }

    /**
     * 弹出查看扫描结果按钮
     */
    private void showDetectionResult() {
        img_scan.setVisibility(View.GONE);
        rl_detection_state.setVisibility(View.INVISIBLE);
        displaySystemStatus();
        // tv_detection_state.setText("扫描结束");
        pb_detectioning.setVisibility(View.GONE);
        Animation animation = AnimationUtils.loadAnimation(this,
                R.anim.detection_scan_down);
        ll_betection_reslut.setVisibility(View.VISIBLE);
        animation.setFillAfter(true);
        ll_betection_reslut.startAnimation(animation);
    }

    /**
     * 立即扫描操作
     */
    private void startScan() {
        DETECTION_STATE = 1;
        getDetectionData();
        showScanState();
        scanAnimation();
    }

    /**
     * 扫描动画
     */
    private void scanAnimation() {
        // img_scan.setVisibility(View.VISIBLE);

        animation1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation1.setDuration(SCAN_TIME);
        animation2 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation2.setDuration(SCAN_TIME);

        // Animation animation = AnimationUtils.loadAnimation(this,
        // R.anim.detection_scan_down_car);
        animation1.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                img_car.setVisibility(View.VISIBLE);
                img_scan.setVisibility(View.VISIBLE);
                img_scan.setAnimation(animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (DETECTION_SUSSES == 0 && GET_DATA_STATE == 1) {
                    img_scan.setVisibility(View.VISIBLE);
                    rl_detection_state.setVisibility(View.VISIBLE);
                    tv_detection_state.setText("扫描中...");
                    DETECTION_STATE = 0;
                    pb_detectioning.setVisibility(View.VISIBLE);
                    // showDetectionResult();
                } else if (DETECTION_SUSSES == 1 && GET_DATA_STATE == 1) {
                    img_scan.setVisibility(View.GONE);
                    rl_detection_state.setVisibility(View.INVISIBLE);
                    // tv_detection_state.setText("扫描结束");
                    pb_detectioning.setVisibility(View.GONE);
                    showDetectionResult();
                    DETECTION_STATE = 2;
                }
                ANIMATION_END = 1;
            }
        });
        rl_car_frame.startAnimation(animation1);
        img_car.startAnimation(animation2);
        handler.postDelayed(runnable, TIME);
    }

    /**
     * 显示扫描时的控件
     */
    private void showScanState() {
        tv_power.setVisibility(View.VISIBLE);
        tv_body.setVisibility(View.VISIBLE);
        tv_chassis.setVisibility(View.VISIBLE);
        tv_electric.setVisibility(View.VISIBLE);
        tv_detection_state.setText("扫描中...");
        pb_detectioning.setVisibility(View.VISIBLE);
    }

    /**
     * 系统状态显示
     */

    private void displaySystemStatus() {
        if (!StringUtil.isEmpty(detectionInfos)) {
            if (StringUtil
                    .isEquals("0", detectionInfos.get(1).getFault(), true)
                    && StringUtil.isEquals("0", detectionInfos.get(3)
                    .getFault(), true)
                    && StringUtil.isEquals("0", detectionInfos.get(4)
                    .getFault(), true)
                    && StringUtil.isEquals("0", detectionInfos.get(5)
                    .getFault(), true)
                    && StringUtil.isEquals("0", detectionInfos.get(8)
                    .getFault(), true)) {
                setTextViewDrawable(tv_power, drawable_zhengchang);
            } else {
                setTextViewDrawable(tv_power, drawable_weixian);
            }

            if (StringUtil
                    .isEquals("0", detectionInfos.get(9).getFault(), true)
                    && StringUtil.isEquals("0", detectionInfos.get(10)
                    .getFault(), true)
                    && StringUtil.isEquals("0", detectionInfos.get(11)
                    .getFault(), true)
                    && (!StringUtil.isEmpty(dtcInfos) && dtcInfos.size() > 0
                    && (StringUtil.isEmpty(dtcInfos.get(0).getName()) || StringUtil.isEquals("null", dtcInfos.get(0).getName(), true)))) {
                setTextViewDrawable(tv_body, drawable_zhengchang);
            } else {
                setTextViewDrawable(tv_body, drawable_weixian);
            }
            if (StringUtil
                    .isEquals("0", detectionInfos.get(2).getFault(), true)) {
                setTextViewDrawable(tv_electric, drawable_zhengchang);
            } else {
                setTextViewDrawable(tv_electric, drawable_weixian);
            }
        }

    }

}
