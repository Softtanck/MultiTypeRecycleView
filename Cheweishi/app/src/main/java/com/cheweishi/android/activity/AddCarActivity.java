package com.cheweishi.android.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.AddCarResponse;
import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.config.API;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ImgDialog;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.MyCarManagerResponse;
import com.cheweishi.android.fragement.MyFragment;
import com.cheweishi.android.tools.APPTools;
import com.cheweishi.android.tools.AllCapTransformationMethod;
import com.cheweishi.android.tools.LoginMessageUtils;
import com.cheweishi.android.tools.ReLoginDialog;
import com.cheweishi.android.tools.RegularExpressionTools;
import com.cheweishi.android.tools.ReturnBackDialogRemindTools;
import com.cheweishi.android.utils.CommonUtils;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.MyMapUtils;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.CustomDialog;
import com.cheweishi.android.widget.DateTimeSelectorDialogBuilder;
import com.cheweishi.android.widget.DateTimeSelectorDialogBuilder.OnSaveListener;
import com.cheweishi.android.widget.OnWheelChangedListener;
import com.cheweishi.android.widget.StrericWheelAdapter;
import com.cheweishi.android.widget.WheelView;
import com.cheweishi.android.widget.XCRoundImageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Xiaojin车辆管理-绑定车辆/编辑车辆
 */
public class AddCarActivity extends BaseActivity {

    @ViewInject(R.id.ll_selectCarModel)
    private LinearLayout ll_selectCarModel;
    @ViewInject(R.id.ll_selectCarPlateCode)
    private LinearLayout ll_selectCarPlateCode;
    @ViewInject(R.id.ll_boundDeviceID)
    private LinearLayout ll_boundDeviceID;
    @ViewInject(R.id.img_car_xcRoundImg)
    private XCRoundImageView img_car_xcRoundImg;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.left_action)
    private Button left_action;
    @ViewInject(R.id.tv_car_device)
    private EditText tv_car_device;
    @ViewInject(R.id.tv_storeId)
    private EditText tv_storeId;
    @ViewInject(R.id.tv_car_plate)
    private TextView tv_car_plate;
    @ViewInject(R.id.tv_car_style)
    private TextView tv_car_style;
    private String brandId = null;
    private String carModelUrl = null;
    private String modelId = null;
    private String styleId = null;
    private String carPlate = null;
    @ViewInject(R.id.bt_addCar)
    private Button bt_addCar;
    @ViewInject(R.id.tv_car_color)
    private TextView tv_car_color;
    @ViewInject(R.id.tv_car_vin)
    private EditText tv_car_vin;
    @ViewInject(R.id.tv_car_note)
    private EditText tv_car_note;
    @ViewInject(R.id.color_spinner)
    private TextView color_spinner;
    private static final List<String> colorArray = new ArrayList<String>();
    private static final List<Integer> colorIndexArray = new ArrayList<Integer>();
    private String color = null;
    private boolean addCarFlag = false;
    @ViewInject(R.id.color_status)
    Button color_status;
    @ViewInject(R.id.ll_color_choose_layout)
    private LinearLayout ll_color_choose_layout;
    @ViewInject(R.id.tv_color_flag)
    private TextView tv_color_flag;
    @ViewInject(R.id.color_go)
    private TextView color_go;
    @ViewInject(R.id.tv_color_left)
    private TextView tv_color_left;
    @ViewInject(R.id.ll_color_layout)
    private LinearLayout ll_color_layout;
    @ViewInject(R.id.ll_top_top)
    private LinearLayout ll_top_top;
    private MyCarManagerResponse.MsgBean carManagerTemp;
    @ViewInject(R.id.tv_nodevice)
    private TextView tv_nodevice;
    private AddHandler handler;
    private CustomDialog phoneDialog;
    @ViewInject(R.id.goToExtra)
    private TextView goToExtra;
    @ViewInject(R.id.ll_focus)
    private LinearLayout ll_focus;
    @ViewInject(R.id.tv_car_mile)
    private EditText tv_car_mile;
    @ViewInject(R.id.tv_last_keepFit)
    private EditText tv_last_keepFit;
    @ViewInject(R.id.tv_used_oil)
    private TextView tv_used_oil;
    @ViewInject(R.id.tv_annualSurvey)
    private TextView tv_annualSurvey;
    @ViewInject(R.id.tv_trafficSurvey)
    private TextView tv_trafficSurvey;
    @ViewInject(R.id.tv_businessSurvey)
    private TextView tv_businessSurvey;
    DateTimeSelectorDialogBuilder dateBuilder;
    @ViewInject(R.id.ll_used_oil)
    private LinearLayout ll_used_oil;
    @ViewInject(R.id.ll_annualSurvey)
    private LinearLayout ll_annualSurvey;
    @ViewInject(R.id.ll_trafficSurvey)
    private LinearLayout ll_trafficSurvey;
    @ViewInject(R.id.ll_businessSurvey)
    private LinearLayout ll_businessSurvey;
    @ViewInject(R.id.ll_mile)
    private LinearLayout ll_mile;
    @ViewInject(R.id.et_car_plate)
    private EditText et_car_plate;
    @ViewInject(R.id.bt_province)
    private TextView bt_province;
    private String longPro[];
    private String shortPro[];
    @ViewInject(R.id.bt_char)
    private TextView bt_char;
    @ViewInject(R.id.ll_last_keepFit)
    private LinearLayout ll_last_keepFit;
    private AlertDialog.Builder oilBuilder;
    private AlertDialog oilDialog;
    private String[] array = {"97#汽油", "93#汽油", "0#柴油"};
    @ViewInject(R.id.ll_color)
    private LinearLayout ll_color;
    @ViewInject(R.id.ll_vin)
    private LinearLayout ll_vin;
    @ViewInject(R.id.color_cancel)
    private Button color_cancel;
    private boolean cancelFlag = false;
    Intent pageIntent;
    private ImgDialog.Builder imgBuilder;
    private ImgDialog imgDialog;
    @ViewInject(R.id.img_vin_desc)
    private ImageView img_vin_desc;
    @ViewInject(R.id.rl_add_car_default)
    private RelativeLayout rl_add_car_default;//设置默认
    @ViewInject(R.id.cb_add_car_default)
    private CheckBox cb_add_car_default;//设置默认的按钮

    private boolean mNeedDefault = false;//是否需要默认

    private boolean isNeedBingd = true;

    private boolean mIsChecked = true;

    // private DateTimeSelectorDialog dateDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        ViewUtils.inject(this);
        isNeedBingd = getIntent().getBooleanExtra("isNeedBingd", true);
        handler = new AddHandler(this);
        initViews();
        setListeners();
    }

    /**
     * 监听返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                hideLinearLayout();
                checkCancel();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        colorArray.clear();
        colorIndexArray.clear();
        setContentView(R.layout.null_view);
        if (phoneDialog != null && phoneDialog.isShowing()) {
            phoneDialog.dismiss();
            phoneDialog = null;
        }
        handler.removeCallbacksAndMessages(null);
        if (oilDialog != null) {
            oilDialog.dismiss();
            oilDialog = null;
            oilBuilder = null;
        }
        if (dateBuilder != null) {
            dateBuilder.dismiss();
            dateBuilder = null;
        }
    }

    private void initViews() {
        oilBuilder = new AlertDialog.Builder(this);
        title.setText(R.string.add_car);
        left_action.setText(R.string.back);
        longPro = this.getResources().getStringArray(R.array.province_item);
        shortPro = this.getResources().getStringArray(
                R.array.province_short_item);
        et_car_plate.setTransformationMethod(new AllCapTransformationMethod());
        colorArray.add(getResources().getString(R.string.color_red));
        colorArray.add(getResources().getString(R.string.color_black));
        colorArray.add(getResources().getString(R.string.color_white));
        colorArray.add(getResources().getString(R.string.color_blue));
        colorArray.add(getResources().getString(R.string.color_yellow));
        colorArray.add(getResources().getString(R.string.color_green));
        colorIndexArray.add(R.color.red);
        colorIndexArray.add(R.color.black);
        colorIndexArray.add(R.color.white);
        colorIndexArray.add(R.color.blue);
        colorIndexArray.add(R.color.yellow);
        colorIndexArray.add(R.color.green);
//        initWheel(R.id.car_wheel,
//                getResources().getStringArray(R.array.color_item));
        if (getIntent().getExtras() != null
                && getIntent().getExtras().getSerializable("car") != null) {
            carManagerTemp = (MyCarManagerResponse.MsgBean) getIntent().getExtras()
                    .getSerializable("car");
            if (carManagerTemp != null) {

                // 编辑车辆判断是否编辑默认车辆
                if (carManagerTemp.isDefault()) { // 有车
                    mNeedDefault = true;
                    rl_add_car_default.setVisibility(View.GONE);
                } else { // 不是编辑的默认车辆
                    mNeedDefault = false;
                    rl_add_car_default.setVisibility(View.VISIBLE);
                }
                title.setText(R.string.car_edit);
                brandId = carManagerTemp.getVehicleFullBrand();
//                styleId = String.valueOf(carManagerTemp.getId());
                // TODO 暂时不需要
//                modelId = carManagerTemp.getBrand().getSeries();
//                color = carManagerTemp.getColor();
                carPlate = carManagerTemp.getPlate();
                if (!StringUtil.isEmpty(brandId)) {
                    tv_car_style.setText(brandId);
                }
                if (!StringUtil.isEmpty(carManagerTemp.getPlate())) {
                    tv_car_plate.setText(carManagerTemp.getPlate());
                    try {
                        bt_province.setText(carManagerTemp.getPlate()
                                .substring(0, 1));
                        bt_char.setText(carManagerTemp.getPlate().substring(1,
                                2));
                        et_car_plate.setText(carManagerTemp.getPlate()
                                .substring(2,
                                        carManagerTemp.getPlate().length()));
                    } catch (Exception e) {

                    }
                }
                if (!StringUtil.isEmpty(carManagerTemp.getDeviceNo())) {
                    ll_boundDeviceID.setVisibility(View.VISIBLE);
                    tv_car_device.setText(carManagerTemp.getDeviceNo());
                    tv_car_device.setEnabled(false);

                } else {
                    ll_boundDeviceID.setOnClickListener(listener);
                    tv_car_device.setOnClickListener(listener);
                }
                if (!StringUtil.isEmpty(carManagerTemp.getDeviceNo())) {
                    tv_storeId.setText(carManagerTemp.getDeviceNo());
                    // tv_storeId.setEnabled(false);
                } else {

                }
                // TODO 不知道是什么
//                if (!StringUtil.isEmpty(carManagerTemp.getNote())) {
                tv_car_note.setText("unknow");
//                }

                // TODO 车架号服务器没有返回
                if (!StringUtil.isEmpty(carManagerTemp.getVehicleNo())) {
                    tv_car_vin.setText(carManagerTemp.getVehicleNo());
                }

                // TODO 设置交强险
                if (!StringUtil.isEmpty(carManagerTemp.getTrafficInsuranceExpiration())) {
                    tv_trafficSurvey.setText(carManagerTemp.getTrafficInsuranceExpiration());
                }


                // TODO 商业保险 , 2016-5-20 16:03:22 不再展示
//                if (!StringUtil.isEmpty(carManagerTemp.getCommercialInsuranceExpiration())) {
//                    tv_businessSurvey.setText(carManagerTemp.getCommercialInsuranceExpiration());
//                }

                // TODO 里程
                if (!StringUtil.isEmpty(carManagerTemp.getDriveMileage())) {
                    tv_car_mile.setText(carManagerTemp.getDriveMileage());
                }

                // TODO 上次里程
                if (!StringUtil.isEmpty(carManagerTemp.getLastMaintainMileage())) {
                    tv_last_keepFit.setText(carManagerTemp.getLastMaintainMileage());
                }

                // TODO 没有颜色这个选项
//                if (!StringUtil.isEmpty(carManagerTemp.getColor())) {
//                    tv_car_color.setHint(R.string.null_hint);
//                    tv_car_color.setVisibility(View.GONE);
//                    color_spinner.setText(carManagerTemp.getColor());
//                }
                if (!StringUtil.isEmpty(carManagerTemp.getNextAnnualInspection())) {
                    tv_annualSurvey.setText(carManagerTemp.getNextAnnualInspection());
                } else {
                    Date date = new Date();
                    SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd",
                            Locale.CHINA);
                    String dateStr = sp.format(date);
                    tv_annualSurvey.setText(dateStr);
                }
                if (!StringUtil.isEmpty(carManagerTemp.getDriveMileage())) {
                    tv_car_mile.setText(carManagerTemp.getDriveMileage() + "");
                }
                if (!StringUtil.isEmpty(carManagerTemp.getLastMaintainMileage())) {
                    tv_last_keepFit.setText(carManagerTemp.getLastMaintainMileage() + "");
                }
                if (!StringUtil.isEmpty(carManagerTemp)) {
                    carModelUrl = carManagerTemp.getBrandIcon();
                }

                XUtilsImageLoader.getxUtilsImageLoader(AddCarActivity.this,
                        R.drawable.car_default, img_car_xcRoundImg,
                        carModelUrl);
//                ll_top_top.setVisibility(View.VISIBLE);
//                carPlate = carManagerTemp.getPlate();
//                brandId = carManagerTemp.getBrand().getBrand();
//                modelId = carManagerTemp.getBrand().getSeries();
//                styleId = carManagerTemp.getBrand().getModule();
//                color = carManagerTemp.getColor();
                int index = colorArray.indexOf(color);
                if (index >= 0) {
                    tv_color_flag.setBackgroundResource(colorIndexArray
                            .get(index));
                    if (color.contains(getResources().getString(
                            R.string.color_white))) {
                        ll_color_layout
                                .setBackgroundResource(R.color.gray_backgroud);
                    } else {
                        ll_color_layout.setBackgroundResource(colorIndexArray
                                .get(index));
                    }
                }
                color = getResources().getString(R.string.color_red);
            }
        } else {
            Date date = new Date();
            SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.CHINA);
            String dateStr = sp.format(date);
            tv_annualSurvey.setText(dateStr);
            tv_trafficSurvey.setText(dateStr);
//            tv_businessSurvey.setText(dateStr);


            String pro = MyMapUtils.getProvince(AddCarActivity.this);
            for (int i = 0; i < longPro.length; i++) {
                if (pro.contains(longPro[i])) {
                    bt_province.setText(shortPro[i]);
                    break;
                }
            }
//            LogHelper.d("---" + MyMapUtils.getCity(AddCarActivity.this));
            bt_char.setText("A");
            tv_color_flag.setBackgroundResource(R.color.red);
            color = getResources().getString(R.string.color_red);
            color_spinner.setText(getResources().getString(R.string.color_red));
            tv_car_color.setHint(R.string.null_hint);
            tv_car_color.setVisibility(View.GONE);

            // 判断是否有车
            if (hasCar()) { // 有车
                mNeedDefault = false;
                rl_add_car_default.setVisibility(View.VISIBLE);
            } else { // 没得车
                mNeedDefault = true;
                rl_add_car_default.setVisibility(View.GONE);
            }
        }

    }

    /**
     * 判断添加车辆中有没有控件获取焦点，获取到焦点就提示返回对话框，否则不提示
     */
    private void checkCancel() {
        if (cancelFlag == false) {
//            Constant.CURRENT_REFRESH = Constant.CAR_MANAGER_REFRESH;
//            Intent mIntent = new Intent();
//            mIntent.setAction(Constant.REFRESH_FLAG);
//            sendBroadcast(mIntent);
            this.finish();
        } else {
            ReturnBackDialogRemindTools.getInstance(this).show();
            // showBackDialog();
        }
    }

    private void setListeners() {
        // 控件点击时间监听
        img_vin_desc.setOnClickListener(listener);
        tv_storeId.setOnClickListener(listener);
        ll_selectCarModel.setOnClickListener(listener);
        bt_province.setOnClickListener(listener);
        bt_char.setOnClickListener(listener);
        bt_addCar.setOnClickListener(listener);
        left_action.setOnClickListener(listener);
        color_status.setOnClickListener(listener);
        tv_car_vin.setOnClickListener(listener);
        tv_car_note.setOnClickListener(listener);
        tv_car_mile.setOnClickListener(listener);
        tv_last_keepFit.setOnClickListener(listener);
        tv_car_vin.setOnClickListener(listener);
        color_cancel.setOnClickListener(listener);
        tv_color_left.setOnClickListener(listener);
        color_go.setOnClickListener(listener);
        tv_color_flag.setOnClickListener(listener);
        tv_nodevice.setOnClickListener(listener);
        goToExtra.setOnClickListener(listener);
        tv_car_style.setOnClickListener(listener);
        tv_car_plate.setOnClickListener(listener);
        tv_annualSurvey.setOnClickListener(listener);
        tv_trafficSurvey.setOnClickListener(listener);
        tv_businessSurvey.setOnClickListener(listener);
        tv_used_oil.setOnClickListener(listener);
        ll_annualSurvey.setOnClickListener(listener);
        ll_trafficSurvey.setOnClickListener(listener);
        ll_businessSurvey.setOnClickListener(listener);
        ll_mile.setOnClickListener(listener);
        ll_used_oil.setOnClickListener(listener);
        ll_last_keepFit.setOnClickListener(listener);
        ll_color.setOnClickListener(listener);
        ll_vin.setOnClickListener(listener);
        // 控件焦点事件监听
        tv_car_vin.setOnFocusChangeListener(foucusListener);
        tv_car_note.setOnFocusChangeListener(foucusListener);
        tv_car_mile.setOnFocusChangeListener(foucusListener);
        tv_last_keepFit.setOnFocusChangeListener(foucusListener);
//        tv_car_vin.setOnFocusChangeListener(foucusListener);// .setOnClickListener(listener);
        tv_car_device.setOnFocusChangeListener(foucusListener);
        tv_car_vin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“GO”键*/
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    /*隐藏软键盘*/
                    APPTools.closeBoard(baseContext, tv_car_vin);
                    return true;
                }
                return false;
            }
        });
        cb_add_car_default.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsChecked = isChecked;
            }
        });
    }

    /**
     * 对控件进行焦点监听
     */
    private OnFocusChangeListener foucusListener = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View arg0, boolean arg1) {
            // TODO Auto-generated method stub
            if (arg1) {// 获得焦点
                cancelFlag = true;
            } else {// 失去焦点

            }
        }

    };


    /**
     * 提交网络请求之前的判断
     */
    private void check() {
        carPlate = bt_province.getText().toString()
                + bt_char.getText().toString()
                + et_car_plate.getText().toString();
        double tv_last_keepFitValue = -0.5;
        if (!StringUtil.isEmpty(tv_last_keepFit.getText().toString())) {
            tv_last_keepFitValue = Double.parseDouble(tv_last_keepFit.getText()
                    .toString());
        }
        double tv_car_mileValue = -0.5;
        if (!StringUtil.isEmpty(tv_car_mile.getText().toString()
                .replaceAll(" ", ""))) {
            tv_car_mileValue = Double.parseDouble(tv_car_mile.getText()
                    .toString());
        }

        if (StringUtil.isEmpty(brandId)) {
            showToast(R.string.car_model_choose_not_yet);
        } else if (StringUtil.isEmpty(carPlate)) {
            showToast(R.string.car_plate_choose_not_yet);
        } else if (!RegularExpressionTools.isCarPlate(carPlate.toUpperCase())) {
            showToast("请输入正确的车牌号");
        } else if (StringUtil.isEmpty(color)) {
            showToast(R.string.car_color_choose_not_yet);
        } else if (tv_last_keepFitValue > tv_car_mileValue) {
            showToast(R.string.car_keepFit_choose_too_large);
        } else if (tv_last_keepFitValue > 0 && tv_car_mileValue < 0) {
            showToast(R.string.car_mile_choose_not_yet);
        } else if (tv_car_mileValue > 1000000f) {
            showToast(R.string.car_mile_error);
        } else if (StringUtil.isEmpty(tv_car_mile.getText().toString())) {
            showToast(R.string.car_mile_choose_not_yet);
        } else if (StringUtil.isEmpty(tv_last_keepFit.getText().toString())) {
            showToast("上次保养里程不能为空");
        } else if (StringUtil.isEmpty(tv_car_vin.getText().toString())) {
            showToast("车架号不能为空");
        } else {
            String str = tv_annualSurvey.getText().toString()
                    .replaceAll(" ", "");
            if (StringUtil.isEmpty(str)) {
                connectToServer();
            } else {
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd",
                        Locale.CHINA);
                try {
                    Date date1 = sf.parse(str);
                    Date date = new Date();
                    String dateStr = sf.format(date);
                    date = sf.parse(dateStr);
                    if ((date1.getTime() - date.getTime()) < (24 * 60 * 60 * 1000)) {
                        showToast(R.string.car_time_error);
                    } else {
                        connectToServer();
                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

    private boolean checkLast = false;


    /**
     * 控件点击事件监听
     */
    private OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent;
            switch (arg0.getId()) {
                case R.id.bt_province:
                    intent = new Intent(AddCarActivity.this,
                            ProvinceAndCharGridActivity.class);
                    intent.putExtra("flag", true);
                    startActivityForResult(intent, 10001);
                    break;
                case R.id.bt_char:
                    intent = new Intent(AddCarActivity.this,
                            ProvinceAndCharGridActivity.class);
                    intent.putExtra("flag", false);
                    startActivityForResult(intent, 10002);
                    break;
                case R.id.tv_storeId:
                    cancelFlag = true;
                    hideLinearLayout();
                    break;
                case R.id.color_cancel:
                    cancelFlag = true;
                    hideLinearLayout();
                    break;
                case R.id.ll_color_layout:
                    cancelFlag = true;
                    showORHideWheelView();
                    break;

                case R.id.ll_color:
                    cancelFlag = true;
                    showORHideWheelView();
                    break;
                case R.id.ll_vin:
                    cancelFlag = true;
                    hideLinearLayout();
                    tv_car_vin.setCursorVisible(true);
                    break;
                case R.id.ll_last_keepFit:
                    cancelFlag = true;
                    hideLinearLayout();
                    tv_last_keepFit.setCursorVisible(true);
                    break;
                case R.id.ll_mile:
                    cancelFlag = true;
                    hideLinearLayout();
                    tv_car_mile.setCursorVisible(true);
                    break;
                case R.id.tv_used_oil:
                    cancelFlag = true;
                    closeBoard(AddCarActivity.this);
                    hideLinearLayout();
                    showOilDialog();
                    break;
                case R.id.tv_last_keepFit:
                    cancelFlag = true;
                    hideLinearLayout();
                    tv_last_keepFit.setCursorVisible(true);
                    break;
                case R.id.ll_selectCarModel:
                    cancelFlag = true;
                    hideLinearLayout();
                    closeBoard(AddCarActivity.this);
                    intent = new Intent(AddCarActivity.this,
                            CarTypeCarBrandModelActivity.class);
                    AddCarActivity.this.startActivityForResult(intent, 1000);
                    break;
                case R.id.ll_annualSurvey:
                    checkLast = true;
                    cancelFlag = true;
                    closeBoard(AddCarActivity.this);
                    hideLinearLayout();
                    showDateView(tv_annualSurvey);
                    break;
                case R.id.ll_trafficSurvey:
                    cancelFlag = true;
                    closeBoard(AddCarActivity.this);
                    hideLinearLayout();
                    showDateView(tv_trafficSurvey);
                    break;
                case R.id.ll_businessSurvey:
                    cancelFlag = true;
                    closeBoard(AddCarActivity.this);
                    hideLinearLayout();
                    showDateView(tv_businessSurvey);
                    break;
                case R.id.ll_used_oil:
                    cancelFlag = true;
                    closeBoard(AddCarActivity.this);
                    hideLinearLayout();
                    showOilDialog();
                    break;
                case R.id.tv_car_mile:
                    cancelFlag = true;
                    hideLinearLayout();
                    tv_car_mile.setCursorVisible(true);
                    break;
                case R.id.tv_annualSurvey:
                    checkLast = true;
                    cancelFlag = true;
                    hideLinearLayout();
                    showDateView(tv_annualSurvey);
                    break;
                case R.id.tv_trafficSurvey:
                    cancelFlag = true;
                    hideLinearLayout();
                    showDateView(tv_trafficSurvey);
                    break;
                case R.id.tv_businessSurvey:
                    cancelFlag = true;
                    hideLinearLayout();
                    showDateView(tv_businessSurvey);
                    break;
                case R.id.tv_car_style:
                    cancelFlag = true;
                    hideLinearLayout();
                    closeBoard(AddCarActivity.this);
                    intent = new Intent(AddCarActivity.this,
                            CarTypeCarBrandModelActivity.class);
                    AddCarActivity.this.startActivityForResult(intent, 1000);
                    break;
                case R.id.ll_selectCarPlateCode:
                    cancelFlag = true;
                    hideLinearLayout();
                    closeBoard(AddCarActivity.this);
                    intent = new Intent(AddCarActivity.this,
                            PlateChooseActivity.class);

                    intent.putExtra("plate", tv_car_plate.getText());
                    AddCarActivity.this.startActivityForResult(intent, 1001);
                    break;
                case R.id.tv_car_plate:
                    cancelFlag = true;
                    hideLinearLayout();
                    closeBoard(AddCarActivity.this);
                    intent = new Intent(AddCarActivity.this,
                            PlateChooseActivity.class);
                    intent.putExtra("plate", tv_car_plate.getText());
                    AddCarActivity.this.startActivityForResult(intent, 1001);
                    break;
                case R.id.ll_boundDeviceID:
                    cancelFlag = true;
                    hideLinearLayout();
                    tv_car_device.setCursorVisible(true);
                    break;
                case R.id.bt_addCar:
                    hideLinearLayout();
                    check();
                    break;
                case R.id.left_action:
                    hideLinearLayout();
                    checkCancel();
                    break;
                case R.id.color_spinner:
                    showORHideWheelView();
                    break;
                case R.id.color_status:
                    tv_car_color.setHint(R.string.null_hint);
                    tv_car_color.setVisibility(View.GONE);
//                    color = getWheelValue(R.id.car_wheel);
//                    color_spinner.setText(getWheelValue(R.id.car_wheel));
                    int index = colorArray.indexOf(color);
                    tv_color_flag.setBackgroundResource(colorIndexArray.get(index));
                    if (color.contains(getResources().getString(
                            R.string.color_white))) {
                        ll_color_layout
                                .setBackgroundResource(R.color.gray_backgroud);
                    } else {
                        if (index > -1 && index < colorIndexArray.size()) {
                            ll_color_layout.setBackgroundResource(colorIndexArray
                                    .get(index));
                        }
                    }
                    hideLinearLayout();
                    break;
                case R.id.tv_car_device:
                    hideLinearLayout();
                    tv_car_device.setCursorVisible(true);
                    break;
                case R.id.tv_car_note:
                    hideLinearLayout();
                    tv_car_note.setCursorVisible(true);
                    break;
                case R.id.tv_car_vin:
                    cancelFlag = true;
                    hideLinearLayout();
                    tv_car_vin.setCursorVisible(true);
                    break;
                case R.id.tv_color_flag:
                    showORHideWheelView();
                    break;

                case R.id.color_go:
                    showORHideWheelView();
                    break;
                case R.id.tv_color_left:
                    showORHideWheelView();
                    break;
                case R.id.tv_nodevice:
                    if (addCarFlag == false) {
                        intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                                + "4007930888"));
                        if (ActivityCompat.checkSelfPermission(baseContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(intent);
                    } else {
                        showToast(R.string.car_information_handle);
                    }
                    break;
                case R.id.goToExtra:
                    intent = new Intent(AddCarActivity.this,
                            AddCarExtraActivity.class);
                    startActivity(intent);
                    break;
                case R.id.img_vin_desc:
                    showImgDialog();
                    break;
            }
        }

    };

    /**
     * 隐藏/弹出颜色选择控件
     */
    private void showORHideWheelView() {
        if (ll_color_choose_layout.getVisibility() == View.INVISIBLE) {
            showLinearLayout();

        } else {
            hideLinearLayout();

        }
    }

    /**
     * 提交网络请求
     */
    private void connectToServer() {

        // dialog.show();
        if (addCarFlag == false) {

            if (isLogined()) {
                addCarFlag = true;


                ProgrosDialog.openDialog(this);
                Map<String, Object> param = new HashMap<>();
                param.put("userId", loginResponse.getDesc());
                param.put("token", loginResponse.getToken());
                if (null != styleId)
                    param.put("brandDetailId", styleId);
                param.put("plateNo", carPlate);
                param.put("vehicleNo", tv_car_vin.getText().toString());
                param.put("trafficInsuranceExpiration", tv_trafficSurvey.getText());
                // TODO 不需要了
//                param.put("commercialInsuranceExpiration", tv_businessSurvey.getText());
                param.put("nextAnnualInspection", tv_annualSurvey.getText());
                param.put("driveMileage", Long.parseLong(tv_car_mile.getText().toString()));
                param.put("lastMaintainMileage", Long.parseLong(tv_last_keepFit.getText().toString()));
                if (mNeedDefault) {
                    param.put("isDefault", mNeedDefault);
                    loginResponse.getMsg().setDefaultVehiclePlate(carPlate);
                    loginResponse.getMsg().setDefaultVehicle(tv_car_style.getText().toString());
                } else {
                    param.put("isDefault", mIsChecked);
                    if (mIsChecked && null != carManagerTemp) { // 更改了
                        MyFragment.tv_my_user_car_id.setText(carManagerTemp.getPlate());
                        loginResponse.getMsg().setDefaultVehiclePlate(carManagerTemp.getPlate());
                        loginResponse.getMsg().setDefaultDeviceNo(carManagerTemp.getDeviceNo());
                        loginResponse.getMsg().setDefaultVehicle(carManagerTemp.getVehicleFullBrand());
                        loginResponse.getMsg().setDefaultVehicleIcon(carManagerTemp.getBrandIcon());
                        loginResponse.getMsg().setDefaultVehicleId(String.valueOf(carManagerTemp.getId()));
                    }
                }
                String url;
                if (null == carManagerTemp) { // 添加
                    url = NetInterface.BASE_URL + NetInterface.TEMP_CAR_URL + NetInterface.ADD + NetInterface.SUFFIX;
                } else { // 编辑
                    url = NetInterface.BASE_URL + NetInterface.TEMP_CAR_URL + NetInterface.EDIT + NetInterface.SUFFIX;
                    param.put("vehicleId", carManagerTemp.getId());
                }

                netWorkHelper.PostJson(url, param, this);

            }
        } else {
            showToast(R.string.car_information_handle);
        }
    }

    private String cid;

    @Override
    public void receive(String data) {
        ProgrosDialog.closeProgrosDialog();
        addCarFlag = false;
        AddCarResponse addCarResponse = (AddCarResponse) GsonUtil.getInstance().convertJsonStringToObject(data, AddCarResponse.class);
        if (!addCarResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            showToast(addCarResponse.getDesc());
            return;
        }


        Constant.CURRENT_REFRESH = Constant.CAR_MANAGER_REFRESH;
        Intent mIntent = new Intent();
        mIntent.setAction(Constant.REFRESH_FLAG);
        sendBroadcast(mIntent);

//        cid = response.getDesc();
        if (null == addCarResponse.getMsg()) {
            cid = addCarResponse.getDesc();
        } else {
            cid = addCarResponse.getMsg().getVehicleId();
        }
        loginResponse.setToken(addCarResponse.getToken());
//        LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
        if (isNeedBingd) {
            if (carManagerTemp == null && null != cid && !"".equals(cid)) {
                showCustomDialog(getString(R.string.no_device), "前往绑定", 1, this, cid);
            } else {
                if (null != carManagerTemp && StringUtil.isEmpty(carManagerTemp.getDeviceNo()) && !StringUtil.isEmpty(cid)) {
                    showCustomDialog(getString(R.string.no_device), "前往绑定", 1, this, cid);
                } else {
                    finish();
                }
            }
        } else {
            setResult(RESULT_OK, null);
            finish();
        }


    }


    /**
     * 绑定车辆提示dialog
     */
    private void showCustomDialog(String msg) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage(msg);
        builder.setTitle(getString(R.string.remind));
        builder.setPositiveButton(getString(R.string.home_goto_bind),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (null != carManagerTemp) { // 编辑状态下直接取ID
                            OpenCamera("" + carManagerTemp.getId());
                        } else {
                            OpenCamera(loginResponse.getMsg().getDefaultVehicleId());
                        }
                        AddCarActivity.this.finish();
                    }
                });
        builder.setNegativeButton(getString(R.string.cancel),
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AddCarActivity.this.finish();
                    }
                });

        builder.create().show();
    }


    @Override
    public void error(String errorMsg) {
        ProgrosDialog.closeProgrosDialog();
        showToast(R.string.server_link_fault);
    }

    private static class AddHandler extends Handler {
        // WeakReference to the outer class's instance.
        private WeakReference<AddCarActivity> mOuter;

        public AddHandler(AddCarActivity activity) {
            mOuter = new WeakReference<AddCarActivity>(activity);
        }

        public void handleMessage(android.os.Message msg) {
            AddCarActivity outer = mOuter.get();
            switch (msg.what) {
                case 400:
                    outer.addCarFlag = false;
                    outer.showToast(R.string.server_link_fault);
                    break;
                default:
                    outer.addCarFlag = false;
                    outer.parseAddCarJSON((String) msg.obj);
                    break;
            }
        }
    }

    /**
     * 解析Json数据
     *
     * @param result -服务器返回的Json数据
     */
    private void parseAddCarJSON(String result) {
        if (!StringUtil.isEmpty(result)) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (StringUtil.isEquals(jsonObject.optString("state"),
                        API.returnSuccess, true)) {

                    Constant.CAR_FLAF = true;
                    // JSONObject jsonObjectData = jsonObject
                    // .optJSONObject("data");
                    // feed = jsonObjectData.optInt("feed");
                    // addMsg = jsonObjectData.optString("msg");
                    // currentCid = jsonObjectData.optString("msg");
                    // if (carManagerTemp == null) {
                    // Gson gson = new Gson();
                    // java.lang.reflect.Type type = new TypeToken<CarManager>()
                    // {
                    // }.getType();
                    // carManagerTemp = gson.fromJson(
                    // jsonObjectData.optString("car"), type);
                    // submitLogin();
                    // } else {
                    // submitLogin();
                    //
                    // }
                    Constant.CURRENT_REFRESH = Constant.CAR_MANAGER_REFRESH;
                    Intent mIntent = new Intent();
                    mIntent.setAction(Constant.REFRESH_FLAG);
                    sendBroadcast(mIntent);
                    if (carManagerTemp == null) {
                        Intent intent = new Intent(AddCarActivity.this,
                                AddDeviceActivity.class);
                        intent.putExtra("cid", jsonObject.optJSONObject("data")
                                .optString("id"));
                        startActivity(intent);

                    } else {
//                        if (StringUtil.isEmpty(carManagerTemp.getDevice())) {
//                            Intent intent = new Intent(AddCarActivity.this,
//                                    AddDeviceActivity.class);
//                            intent.putExtra(
//                                    "cid",
//                                    jsonObject.optJSONObject("data").optString(
//                                            "id"));
//                            startActivity(intent);
//                        }
                    }
                    finish();

                } else if (StringUtil.isEquals(jsonObject.optString("state"),
                        API.returnRelogin, true)) {
                    ReLoginDialog.getInstance(this).showDialog(
                            jsonObject.optString("message"));

                } else {
                    // dialog.dismiss();
                    showToast(jsonObject.optString("message"));
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    /**
     * 对车型、车牌的回调
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1000:
                if (!StringUtil.isEmpty(data)) {

                    brandId = data.getStringExtra("mResultFirstId");
                    modelId = data.getStringExtra("mResultLastId");
                    styleId = data.getStringExtra("styleId");
                    carModelUrl = data.getStringExtra("carLogoUrl");
                    tv_car_style.setText(data.getStringExtra("mResultFirstName")
                            + "-" + data.getStringExtra("mResultLastName"));
                    XUtilsImageLoader.getxUtilsImageLoader(AddCarActivity.this,
                            R.drawable.car_default, img_car_xcRoundImg,
                            carModelUrl);
                    ll_top_top.setVisibility(View.VISIBLE);
                }
                break;
            case 1001:
                if (!StringUtil.isEmpty(data)) {
                    if (!StringUtil.isEmpty(data.getStringExtra("plate"))
                            && !StringUtil.isEmpty(data.getStringExtra("province"))) {
                        String plate = data.getStringExtra("plate");
                        String province = data.getStringExtra("province");
                        tv_car_plate.setText(province + plate);
                        carPlate = province + plate;
                    }
                }
                break;
            case 1002:
                handler = new AddHandler(this);
                initViews();
                setListeners();
                break;
            case 10001:
                if (!StringUtil.isEmpty(data)
                        && !StringUtil.isEmpty(data.getStringExtra("item"))) {
                    String itemData = data.getStringExtra("item");
                    bt_province.setText(itemData);
                }
                break;
            case 10002:
                if (data != null
                        && !StringUtil.isEmpty(data.getStringExtra("item"))) {
                    String itemData = data.getStringExtra("item");
                    bt_char.setText(itemData);
                }
                break;
        }
    }


    private void initWheel(int id, String[] strContents) {
        WheelView wheel = getWheel(id);
        wheel.setAdapter(new StrericWheelAdapter(strContents));
        wheel.setCurrentItem(0);
        wheel.setCyclic(false);
        wheel.setInterpolator(new AnticipateOvershootInterpolator());
//        if (id == R.id.car_wheel) {
        wheel.setCyclic(false);
        wheel.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue,
                                  int newValue) {
            }
        });
//        }
    }

    private WheelView getWheel(int id) {
        return (WheelView) findViewById(id);
    }

    private String getWheelValue(int id) {
        return getWheel(id).getCurrentItemValue();
    }

    private void showLinearLayout() {
        closeBoard(AddCarActivity.this);
        // tv_car_device.setFocusable(false);
        // tv_car_note.setFocusable(false);
        // tv_car_vin.setFocusable(false);
        final Animation animation1 = AnimationUtils.loadAnimation(
                AddCarActivity.this, R.anim.score_business_query_enter);
        ll_color_choose_layout.startAnimation(animation1);
        ll_color_choose_layout.setVisibility(View.VISIBLE);
    }

    private void hideLinearLayout() {
        if (ll_color_choose_layout.getVisibility() == View.VISIBLE) {
            final Animation animation1 = AnimationUtils.loadAnimation(
                    AddCarActivity.this, R.anim.score_business_query_exit);
            ll_color_choose_layout.startAnimation(animation1);
            ll_color_choose_layout.setVisibility(View.INVISIBLE);
        }
    }

    public void closeBoard(Context mcontext) {
        ll_focus.setFocusable(true);
        ll_focus.setFocusableInTouchMode(true);
        ll_focus.requestFocus();

    }

    TextView mTv;

    /**
     * 日期对话框
     */
    private void showDateView(TextView tv) {
        mTv = tv;
        if (dateBuilder == null) {
            dateBuilder = DateTimeSelectorDialogBuilder
                    .getInstance(AddCarActivity.this);
            dateBuilder.setOnSaveListener(saveListener);
        }
        if (StringUtil.isEquals(
                CommonUtils.getTopActivity(AddCarActivity.this),
                AddCarActivity.class.getName(), true)) {
            dateBuilder.show();
        }
    }

    /**
     * 日期保存监听
     */
    OnSaveListener saveListener = new OnSaveListener() {

        @Override
        public void onSaveSelectedDate(String selectedDate) {
            if (checkLast) {
                checkLast = false;
                tv_trafficSurvey.setText(selectedDate);
            }
            mTv.setText(selectedDate);
            if (dateBuilder.isShowing()) {
                dateBuilder.dismiss();
            }
        }
    };

    /**
     * 曾用油对话框
     */
    private void showOilDialog() {
        ListView oilListView = new ListView(this);
        oilListView.setItemsCanFocus(true);
        oilListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);// 可多选
        oilListView.setBackgroundColor(AddCarActivity.this.getResources()
                .getColor(R.color.white));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.item_oil, array);
        oilListView.setAdapter(adapter);
        oilListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                if (oilDialog != null) {
                    tv_used_oil.setText((String) arg0.getItemAtPosition(arg2));
                    oilDialog.dismiss();
                }
            }
        });
        // setContentView(oilListView);

        oilDialog = oilBuilder.create();
        oilDialog.show();
        ViewGroup.LayoutParams lp = new ViewGroup.MarginLayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        oilDialog.show();
        oilDialog.addContentView(oilListView, lp);
    }

    private void showImgDialog() {
        imgBuilder = new ImgDialog.Builder(this);
        imgBuilder.setMessage(R.string.phone_msg);
        imgBuilder.setTitle(R.string.contact_customer_service);
        imgBuilder.setPositiveButton(R.string.customerServiceCall,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        imgDialog = imgBuilder.create();
        imgDialog.show();
    }
}
