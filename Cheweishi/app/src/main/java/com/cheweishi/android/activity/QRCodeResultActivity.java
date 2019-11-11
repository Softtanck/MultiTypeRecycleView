package com.cheweishi.android.activity;

import com.cheweishi.android.adapter.QrCodeAdapter;
import com.cheweishi.android.R;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ImgDialog;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.LoginUserInfoResponse;
import com.cheweishi.android.entity.MyCarManagerResponse;
import com.cheweishi.android.entity.QRServerResponse;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.tools.EmptyTools;
import com.cheweishi.android.tools.LoginMessageUtils;
import com.cheweishi.android.tools.RegularExpressionTools;
import com.cheweishi.android.tools.TextViewTools;
import com.cheweishi.android.utils.GsonUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * 二维码扫描结果显示
 *
 * @author mingdasen
 */
@ContentView(R.layout.activity_qrcode_result)
public class QRCodeResultActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    @ViewInject(R.id.left_action)
    private TextView left_action;

    @ViewInject(R.id.title)
    private TextView title;

    @ViewInject(R.id.lv_qr_code_car_list)
    private ListView lv_qr_code_car_list;

    private QrCodeAdapter adapter;


    private String result = "";
    private MyCarManagerResponse response;
    private ImgDialog.Builder imgBuilder;
    private ImgDialog imgDialog;

    private boolean isAddDevice = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        initView();
    }

    // TODO 获取返回结果
    private void initView() {
        left_action.setText(R.string.back);

        Bundle bundle = getIntent().getExtras();
        result = bundle.getString("result");
        if (null != bundle.getString("resultString") && !"".equals(bundle.getString("resultString"))) {
            title.setText("绑定设备车辆");
            isAddDevice = true;
        } else {
            title.setText("绑定租户");
            isAddDevice = false;
        }
        left_action.setOnClickListener(this);


        getCarData();
    }

    private void getCarData() {
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_CAR_URL + NetInterface.LIST + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put(Constant.PARAMETER_TAG, NetInterface.LIST + "QR");
        netWorkHelper.PostJson(url, param, this);
    }


    @Override
    public void receive(String TAG, String data) {
        ProgrosDialog.closeProgrosDialog();
        switch (TAG) {
            case NetInterface.LIST + "QR":
                response = (MyCarManagerResponse) GsonUtil.getInstance().convertJsonStringToObject(data, MyCarManagerResponse.class);
                if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    showToast(response.getDesc());
                    return;
                }

                if (0 < response.getMsg().size()) {
                    adapter = new QrCodeAdapter(baseContext, response.getMsg(), isAddDevice);
                    lv_qr_code_car_list.setAdapter(adapter);
                    lv_qr_code_car_list.setOnItemClickListener(this);
                } else {
                    EmptyTools.setEmptyView(this, lv_qr_code_car_list, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(baseContext, AddCarActivity.class);
                            intent.putExtra("isNeedBingd", false);
                            startActivityForResult(intent, 0x5555);
                        }
                    });
                    EmptyTools.setImg(R.drawable.mycar_icon);
                    EmptyTools.setMessage("您还没有添加车辆,点击图标添加车辆");
                }

                loginResponse.setToken(response.getToken());
//                LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
                break;

            case NetInterface.BIND_QR:
                QRServerResponse baseResponse = (QRServerResponse) GsonUtil.getInstance().convertJsonStringToObject(data, QRServerResponse.class);
                if (!baseResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    showToast(baseResponse.getDesc());
                    return;
                }




                setTitle(baseResponse.getMsg().getAppTitleName());
                loginResponse.setToken(baseResponse.getToken());
//                LoginMessageUtils.saveloginmsg(baseContext, loginResponse);


                if (baseResponse.getMsg().isIsGetCoupon()) {
                    showImgDialog();
                }else{
                    finish();
                }
                break;
        }


    }

    private boolean isExit = false;

    private void showImgDialog() {
        imgBuilder = new ImgDialog.Builder(this);
        imgBuilder.setshowCoupon(true);
        imgBuilder.setPositiveButton(R.string.customerServiceCall,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        isExit = true;
                        QRCodeResultActivity.this.finish();
                    }
                });

        imgDialog = imgBuilder.create();
        imgDialog.setCanceledOnTouchOutside(false);

        imgDialog.show();
        imgDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!isExit) {
                    QRCodeResultActivity.this.finish();
                }
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO 发送绑定请求
        if (!isAddDevice) { // 绑定租户
            sendBind(response.getMsg().get(position).getId());
        } else { // 绑定设备
            Intent intent = new Intent(QRCodeResultActivity.this, AddDeviceActivity.class);
            intent.putExtra("resultString", getIntent().getStringExtra("resultString"));
            intent.putExtra("cid", "" + response.getMsg().get(position).getId());
            intent.putExtra("BD", true);
            startActivity(intent);
            QRCodeResultActivity.this.finish();
        }
    }

    private void sendBind(int id) {
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_CAR_URL + NetInterface.BIND_QR + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("tenantId", result);
        param.put("vehicleId", id);
        param.put(Constant.PARAMETER_TAG, NetInterface.BIND_QR);
        netWorkHelper.PostJson(url, param, this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.left_action:
                QRCodeResultActivity.this.finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (0x5555 == requestCode) {
            if (RESULT_OK == resultCode) {
                getCarData(); // 更新界面
            }
        }
    }
}
