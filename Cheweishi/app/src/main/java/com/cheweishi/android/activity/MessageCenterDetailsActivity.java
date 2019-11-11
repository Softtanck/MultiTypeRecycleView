package com.cheweishi.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.utils.GsonUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ContentView(R.layout.activity_message_center_details)
public class MessageCenterDetailsActivity extends BaseActivity implements
        OnClickListener {
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.left_action)
    private Button left_action;
    @ViewInject(R.id.right_action)
    private TextView right_action;
    @ViewInject(R.id.msg_datails_info)
    private TextView msg_datails_info;
    @ViewInject(R.id.detail_tv_type)
    private TextView detail_tv_type;
    @ViewInject(R.id.detail_tv_time)
    private TextView detail_tv_time;

    @ViewInject(R.id.tv_centent_details_body)
    private TextView tv_centent_details_body;
    @ViewInject(R.id.tv_centent_details_title)
    private TextView tv_centent_details_title;
    @ViewInject(R.id.tv_centent_time)
    private TextView tv_centent_time;
    @ViewInject(R.id.tv_centent_details_content)
    private TextView tv_centent_details;
    private String number = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        initData();
        // getMessageDetails();

    }


    private String transferLongToDate(Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
        Date date = new Date(millSec);
        return sdf.format(date);
    }


    @Override
    public void receive(String data) {
        ProgrosDialog.closeProgrosDialog();
        BaseResponse response = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
        if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            showToast(response.getDesc());
            return;
        }

        loginResponse.setToken(response.getToken());
//        LoginMessageUtils.saveloginmsg(baseContext, loginResponse);


        // TODO 更新主页消息提示UI数量
        if (!getIntent().getBooleanExtra("isRead", true)) {

            if (null != number && !"".equals(number)) {
                int msgnumber = 0;
                try {
                    msgnumber = Integer.valueOf(number);
                } catch (Exception e) {
                    msgnumber = Integer.valueOf(number.substring(0, 2));
                }
                if (0 < msgnumber && 1 != msgnumber) {
                    MainNewActivity.tv_msg_center_num.setVisibility(View.VISIBLE);
                    if (99 >= (msgnumber - 1))
                        MainNewActivity.tv_msg_center_num.setText("" + (msgnumber - 1));
                    else
                        MainNewActivity.tv_msg_center_num.setText("99+");
                } else {
                    MainNewActivity.tv_msg_center_num.setVisibility(View.GONE);
                }
            } else {
                String tnumber = MainNewActivity.tv_msg_center_num.getText().toString();
                if (null != tnumber && !"".equals(tnumber)) {
                    int mImsgNumber = 0;
                    try {
                        mImsgNumber = Integer.valueOf(tnumber);
                    } catch (Exception e) {
                        mImsgNumber = Integer.valueOf(tnumber.substring(0, 2));
                    }
                    if (0 < mImsgNumber && 1 != mImsgNumber) {
                        MainNewActivity.tv_msg_center_num.setVisibility(View.VISIBLE);
                        if (99 > (mImsgNumber - 1))
                            MainNewActivity.tv_msg_center_num.setText("" + (mImsgNumber - 1));
                        else
                            MainNewActivity.tv_msg_center_num.setText("99+");
                    } else {
                        MainNewActivity.tv_msg_center_num.setVisibility(View.GONE);
                    }
                }
            }
        }

    }

    @Override
    public void error(String errorMsg) {
        ProgrosDialog.closeProgrosDialog();
    }

    private void initData() {
        this.title.setText(R.string.msg_details);
        this.left_action.setText(R.string.back);
        number = getIntent().getStringExtra("number");
        String id = getIntent().getStringExtra("id");
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_MESSAGE + NetInterface.SET_READ_MSG + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("msgId", id);
        netWorkHelper.PostJson(url, param, this);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        long time = intent.getLongExtra("time", 0);
        tv_centent_details_title.setText(title);
        tv_centent_time.setText(transferLongToDate(time));
        tv_centent_details.setText(content);


        // MessagCenterInfo mMessagCenterInfo = (MessagCenterInfo) getIntent()
        // .getExtras().getSerializable("list");
        // this.setDataBack(mMessagCenterInfo);
        // String type = mMessagCenterInfo.getType();
        // detail_tv_type.setText(type);
        // if("1".equals(type)){
        // detail_tv_type.setText("非法启动");
        // }
        // else if("2".equals(type)){
        // detail_tv_type.setText("非法振动");
        // }
        // else if("3".equals(type)){
        // detail_tv_type.setText("obd故障报警");
        // }
        // else if("4".equals(type)){
        // detail_tv_type.setText("水温异常报警");
        // }
        // else if("5".equals(type)){
        // detail_tv_type.setText("超速报警");
        // }
        // else if("6".equals(type)){
        // detail_tv_type.setText("碰撞告警");
        // }
        // else if("7".equals(type)){
        // detail_tv_type.setText("侧翻告警");
        // }
        // else if("8".equals(type)){
        // detail_tv_type.setText("车辆启动");
        // }
        // else if("9".equals(type)){
        // detail_tv_type.setText("车辆熄火");
        // }
        // else if("10".equals(type)){
        // detail_tv_type.setText("解绑通知");
        // }
        // else if("11".equals(type)){
        // detail_tv_type.setText("保养通知");
        // }
        // this.msg_datails_info.setText(mMessagCenterInfo.getInfo());
        // this.detail_tv_time.setText(mMessagCenterInfo.getTime().substring(10,
        // mMessagCenterInfo.getTime().length()));
    }

    @OnClick({R.id.left_action})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_action:
                this.finish();
                break;
        }
    }

}
