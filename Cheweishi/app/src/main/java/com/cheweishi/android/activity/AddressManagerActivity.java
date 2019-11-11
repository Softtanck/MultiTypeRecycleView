package com.cheweishi.android.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ListDialog;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.AreaListResponse;
import com.cheweishi.android.entity.MyRecevieAddressResponse;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.tools.RegularExpressionTools;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.CustomDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tangce on 8/31/2016.
 */
public class AddressManagerActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @ViewInject(R.id.left_action)
    private Button left_action; // 左边标题

    @ViewInject(R.id.title)
    private TextView title; // 标题

    @ViewInject(R.id.right_action)
    private TextView right_action;//删除

    @ViewInject(R.id.et_adm_name)
    private EditText et_adm_name;//收货人名字

    @ViewInject(R.id.et_adm_phone)
    private EditText et_adm_phone;//收货人电话

    @ViewInject(R.id.tv_adm_area)
    private TextView tv_adm_area;//收货人地区

    @ViewInject(R.id.cb_adm_def)
    private CheckBox cb_adm_def;//是否默认

    @ViewInject(R.id.et_adm_detail_address)
    private EditText et_adm_detail_address;//收货人详细地址

    @ViewInject(R.id.et_adm_detail_zipcode)
    private EditText et_adm_detail_zipcode;//邮编

    private MyRecevieAddressResponse.MsgBean data;

    private int mAreaId = -1;//地区id

    private AreaListResponse areaListResponse;

    private ListDialog dialog;

    private int mCurrentArea = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manager);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        left_action.setText(R.string.back);

        data = (MyRecevieAddressResponse.MsgBean) getIntent().getSerializableExtra("data");
        if (null != data) {
            mCurrentArea = data.getArea().getId();
            title.setText(R.string.edit_address);
            right_action.setText(R.string.delete);
            et_adm_name.setText(data.getConsignee());
            et_adm_phone.setText(data.getPhone());
            et_adm_detail_address.setText(data.getAddress());
            tv_adm_area.setText(data.getAreaName());
            if (data.isIsDefault())
                cb_adm_def.setChecked(true);
        } else {
            title.setText(R.string.add_new_address);
        }
    }

    @OnClick({R.id.left_action, R.id.tv_adm_area, R.id.bt_adm_commit, R.id.right_action})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_action:
                finish();
                break;
            case R.id.tv_adm_area: // 地区修改
                getAreaPacket(mAreaId);
                break;
            case R.id.bt_adm_commit:// 保存
                int receiverId = -1;
                if (null != data) {
                    receiverId = data.getId();
                }
                if (StringUtil.isEmpty(et_adm_name.getText().toString().trim())) {
                    showToast(R.string.re_error);
                    return;
                } else if (RegularExpressionTools.isMobile(et_adm_phone.getText().toString().trim()) == false) {
                    showToast(R.string.tel_error);
                    return;
                } else if (-1 == mCurrentArea) {
                    showToast(R.string.area_error);
                    return;
                } else if (StringUtil.isEmpty(et_adm_detail_address.getText().toString().trim()) || 3 > et_adm_detail_address.getText().toString().trim().length()) {
                    showToast(R.string.detail_error);
                    return;
                }
                modifyAddress(receiverId, mCurrentArea, et_adm_name.getText().toString().trim(), et_adm_detail_address.getText().toString().trim(), et_adm_phone.getText().toString().trim(), cb_adm_def.isChecked(), et_adm_detail_zipcode.getText().toString().trim());
                break;
            case R.id.right_action://删除
                showDeleteDialog(getString(R.string.delete_desc), getString(R.string.confirm));
                break;
        }
    }

    @Override
    public void receive(String TAG, String data) {
        ProgrosDialog.closeProgrosDialog();
        switch (TAG) {
            case NetInterface.GET_ADDRESS:
                areaListResponse = (AreaListResponse) GsonUtil.getInstance().convertJsonStringToObject(data, AreaListResponse.class);
                if (!areaListResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    showToast(areaListResponse.getDesc());
                    return;
                }

                if (null != areaListResponse && null != areaListResponse.getMsg() && 0 < areaListResponse.getMsg().size()) {
                    showAreaDialog(areaListResponse.getMsg());
                } else {
                    mCurrentArea = mAreaId;
                    mAreaId = -1;
                }
                loginResponse.setToken(areaListResponse.getToken());
                break;
            case NetInterface.ADD_OR_EDIT://修改或者添加地址
                BaseResponse addOredit = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
                if (!addOredit.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    showToast(addOredit.getDesc());
                    return;
                }

                // TODO 通知上个页面更新

                loginResponse.setToken(addOredit.getToken());
                finish();


                break;

            case NetInterface.DELETE: // 删除收货地址
                BaseResponse delete = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
                if (!delete.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    showToast(delete.getDesc());
                    return;
                }

                //  TODO 通知上个页面更新
                loginResponse.setToken(delete.getToken());
                finish();
                break;
        }
    }

    private void deleteItem(int receiverId) {
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_SHOP_ADD + NetInterface.DELETE + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("receiverId", receiverId);
        param.put(Constant.PARAMETER_TAG, NetInterface.DELETE);
        netWorkHelper.PostJson(url, param, this);
    }

    private void getAreaPacket(int areaId) {
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_SHOP_ADD + NetInterface.GET_ADDRESS + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        if (-1 != areaId)
            param.put("areaId", areaId);
        else {
            mCurrentArea = -1;
            tv_adm_area.setText("");
        }
        param.put(Constant.PARAMETER_TAG, NetInterface.GET_ADDRESS);
        netWorkHelper.PostJson(url, param, this);
    }

    private void modifyAddress(int receiverId, int areaId, String consignee, String address, String phone, boolean isDefault, String zipCode) {
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_SHOP_ADD + NetInterface.ADD_OR_EDIT + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("areaId", areaId);
        param.put("consignee", consignee);
        param.put("address", address);
        param.put("phone", phone);
        param.put("isDefault", isDefault);
        param.put("token", loginResponse.getToken());
        param.put("zipCode", zipCode);
        if (-1 != receiverId)
            param.put("receiverId", receiverId);
        param.put(Constant.PARAMETER_TAG, NetInterface.ADD_OR_EDIT);
        netWorkHelper.PostJson(url, param, this);
    }

    private void showAreaDialog(List<AreaListResponse.MsgBean> list) {
        ListDialog.Builder builder = new ListDialog.Builder(this);
        builder.setItems(list, this);
        dialog = builder.create();
        dialog.show();
    }


    private void dismissAreaDialog() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != areaListResponse && null != areaListResponse.getMsg() && 0 < areaListResponse.getMsg().size()) {
            mAreaId = areaListResponse.getMsg().get(position).getId();
            tv_adm_area.append(areaListResponse.getMsg().get(position).getName());
            getAreaPacket(mAreaId);
        }
        dismissAreaDialog();
    }

    public void showDeleteDialog(String msg, String buttonMsg) {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage(msg);
        builder.setTitle(getString(R.string.remind));

        builder.setPositiveButton(buttonMsg,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteItem(data.getId());
                    }
                });
        builder.setNegativeButton(getString(R.string.cancel),
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }
}
