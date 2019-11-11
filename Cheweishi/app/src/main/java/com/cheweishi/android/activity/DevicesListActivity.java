package com.cheweishi.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.DevicesListAdapter;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.DevicelistResponse;
import com.cheweishi.android.tools.EmptyTools;
import com.cheweishi.android.tools.LoginMessageUtils;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.widget.UnSlidingListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tangce on 6/19/2016.
 */
public class DevicesListActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    @ViewInject(R.id.unl_devices_list)
    private UnSlidingListView unl_devices_list; // devices list
    @ViewInject(R.id.ll_devices_list_title)
    private LinearLayout ll_devices_list_title;//顶部标题

    @ViewInject(R.id.left_action)
    private Button left_action; // 左边按钮
    @ViewInject(R.id.title)
    private TextView title;// 标题

    private List<DevicelistResponse.MsgBean> list;

    private DevicesListAdapter adapter;
    private String cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_list);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        left_action.setText(R.string.back);
        title.setText("选择设备");
        list = new ArrayList<>();
        adapter = new DevicesListAdapter(baseContext, list);
        unl_devices_list.setAdapter(adapter);
        unl_devices_list.setOnItemClickListener(this);
        cid = getIntent().getStringExtra("cid");
        getData();
    }

    private void getData() {
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_CAR_URL + NetInterface.GET_DEVICES_LIST + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put(Constant.PARAMETER_TAG, NetInterface.GET_DEVICES_LIST);
        netWorkHelper.PostJson(url, param, this);
    }

    @Override
    public void receive(String TAG, String data) {
        ProgrosDialog.closeProgrosDialog();
        switch (TAG) {
            case NetInterface.GET_DEVICES_LIST: // 获取设备列表
                DevicelistResponse response = (DevicelistResponse) GsonUtil.getInstance().convertJsonStringToObject(data, DevicelistResponse.class);
                if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    showToast(response.getDesc());
                    return;
                }

                if (null != response.getMsg() && 0 < response.getMsg().size()) {
                    ll_devices_list_title.setVisibility(View.VISIBLE);
                    list.clear();
                    list.addAll(response.getMsg());
                    adapter.setData(response.getMsg());
                } else {
                    EmptyTools.setEmptyView(this, unl_devices_list, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Intent intent = new Intent(baseContext, PayActivty.class);
//                            intent.putExtra("PAY_TYPE", true);
//                            startActivity(intent);
                            OpenCamera();
                        }
                    });
                    EmptyTools.setImg(R.drawable.mycar_icon);
                    EmptyTools.setMessage("您还没有可以绑定的设备,点击图标购买设备");
                }

                loginResponse.setToken(response.getToken());
//                LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
                break;
        }
    }

    @OnClick({R.id.left_action})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_action:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != cid) {
            if ("0".equals(cid)) { // 表示没有指定车辆Id
                Intent resultIntent = new Intent();
                resultIntent.setClass(baseContext, QRCodeResultActivity.class);
                resultIntent.putExtra("resultString", list.get(position).getDeviceNo());
                startActivity(resultIntent);
            } else { // 指定了车辆id.
                Intent resultIntent = new Intent();
                resultIntent.setClass(baseContext, AddDeviceActivity.class);
                resultIntent.putExtra("resultString", list.get(position).getDeviceNo());
                resultIntent.putExtra("cid", cid);
                startActivity(resultIntent);
            }
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EmptyTools.destory();
    }
}
