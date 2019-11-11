package com.cheweishi.android.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.biz.HttpBiz;
import com.cheweishi.android.config.API;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.LoginUserInfoResponse;
import com.cheweishi.android.entity.ModifyUserInfoResponse;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.tools.APPTools;
import com.cheweishi.android.tools.DBTools;
import com.cheweishi.android.tools.LoginMessageUtils;
import com.cheweishi.android.tools.ReturnBackDialogRemindTools;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.StringUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改昵称s
 *
 * @author Xiaojin
 */
public class UserNickActivity extends BaseActivity implements OnClickListener {

    @ViewInject(R.id.left_action)
    private Button left_action;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.right_action)
    private TextView right_action;
    @ViewInject(R.id.et_nick)
    private EditText et_nick;
    private String preNick = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_nick);
        ViewUtils.inject(this);
        initViews();
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        title.setText(R.string.title_activity_user_nick);
        left_action.setText(R.string.back);
        right_action.setText(R.string.finish);
        String nick = getIntent().getStringExtra("nick");
        if (!StringUtil.isEmpty(nick)) {
            preNick = nick;
            et_nick.setText(nick);
            et_nick.setSelection(nick.length());
        }
    }

    @OnClick({R.id.left_action, R.id.right_action})
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.left_action:
                APPTools.closeBoard(this, et_nick);
                if (StringUtil.isEquals(preNick, et_nick.getText().toString(),
                        false)) {

                    finish();
                } else {
                    ReturnBackDialogRemindTools.getInstance(this).show();
                }

                break;
            case R.id.right_action:
                APPTools.closeBoard(this, et_nick);
                connectToNickServer();
                break;
        }
    }

    /**
     * 昵称提交服务器
     */
    // TODO 昵称发包
    private void connectToNickServer() {
        String nick = et_nick.getText().toString();
        if (StringUtil.isEmpty(nick)) {
            showToast("修改昵称不能为空");
            return;
        }
//        if (isLogined()) {
//            httpBiz = new HttpBiz(this);
//            RequestParams rp = new RequestParams();
//            rp.addBodyParameter("uid", loginMessage.getUid());
//            rp.addBodyParameter("nick_name", et_nick.getText().toString());
//            ProgrosDialog.openDialog(UserNickActivity.this);
//            httpBiz.httPostData(10009, API.CSH_UPDATE_USER_NICK_URL, rp, this);
        String url = NetInterface.HEADER_ALL + NetInterface.EDIT_USER_INFO + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("nickName", nick);
        netWorkHelper.PostJson(url, param, this);
//        }
    }

    @Override
    public void receive(String data) {

        ModifyUserInfoResponse response = (ModifyUserInfoResponse) GsonUtil.getInstance().convertJsonStringToObject(data, ModifyUserInfoResponse.class);

        if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            showToast(response.getDesc());
            return;
        }
//        LoginUserInfoResponse userInfoResponse = loginResponse.getMsg();
//        userInfoResponse.setNickName(response.getMsg().getNickName());
        loginResponse.getMsg().setNickName(response.getMsg().getNickName());
        loginResponse.setToken(response.getToken());
//        loginResponse.setMsg(userInfoResponse);
//        LoginMessageUtils.saveProduct(loginResponse, baseContext);
//        DBTools.getInstance(baseContext).save(loginResponse);
        Constant.CURRENT_REFRESH = Constant.USER_NICK_EDIT_REFRESH;
        Intent mIntent = new Intent();
        mIntent.setAction(Constant.REFRESH_FLAG);
        sendBroadcast(mIntent);
        finish();
    }

    @Override
    public void error(String errorMsg) {
        showToast(R.string.server_link_fault);
    }



    /**
     * 控制Android系统返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                APPTools.closeBoard(this, et_nick);
                if (!et_nick.getText().toString().equals(preNick)) {
                    ReturnBackDialogRemindTools.getInstance(this).show();
                } else {
                    finish();
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
