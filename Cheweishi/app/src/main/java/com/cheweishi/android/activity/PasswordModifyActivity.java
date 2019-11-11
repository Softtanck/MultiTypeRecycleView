package com.cheweishi.android.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.biz.HttpBiz;
import com.cheweishi.android.config.API;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.KeyGenerator;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.FontAwesomeView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xiaojin修改密码
 */
public class PasswordModifyActivity extends BaseActivity implements
        OnClickListener {
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.left_action)
    private Button left_action;
    @ViewInject(R.id.ll__modify_pass)
    private LinearLayout ll__modify_pass;
    @ViewInject(R.id.ib_modify_password)
    private ImageButton ib_modify_password;
    @ViewInject(R.id.btn_modify)
    private Button btn_modify;
    @ViewInject(R.id.edt_modify_oldpassword)
    private EditText edt_modify_oldpassword;
    @ViewInject(R.id.edt_modity_password)
    private EditText edt_modity_password;
    @ViewInject(R.id.tv_setNewPass)
    private TextView tv_setNewPass;
    @ViewInject(R.id.tv_ok)
    private TextView tv_ok;
    @ViewInject(R.id.img_line1)
    private ImageView img_line1;
    @ViewInject(R.id.img_setNewPass)
    private FontAwesomeView img_setNewPass;
    @ViewInject(R.id.img_line2)
    private ImageView img_line2;
    @ViewInject(R.id.img_ok)
    private FontAwesomeView img_ok;
    @ViewInject(R.id.img_setpass_ok)
    private ImageView img_setPassOk;
    @ViewInject(R.id.tv_newpass_login)
    private TextView tv_newpass_login;
    @ViewInject(R.id.ll_time)
    private LinearLayout ll_time;
    @ViewInject(R.id.tv_time)
    private TextView tv_time;
    @ViewInject(R.id.ll_modify_oldpassword)
    private LinearLayout ll_modify_oldpassword;
    private TimeCount time;
    private boolean passwordFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_modify);
        ViewUtils.inject(this);
        initViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        left_action.setText(R.string.back);
        title.setText(R.string.pass_forget);
        img_line1.setImageResource(R.drawable.mima_line2);
        img_setNewPass.setBackgroundResource(R.color.main_blue);
        tv_setNewPass.setTextColor(getResources().getColor(
                R.color.orange_text_color));
        time = new TimeCount(3000, 1000);
    }

    @OnClick({R.id.btn_modify, R.id.left_action, R.id.ib_modify_password})
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btn_modify:
                check();
                break;
            case R.id.left_action:
                time.cancel();
                PasswordModifyActivity.this.finish();
                break;
            case R.id.ib_modify_password:
                if (passwordFlag == false) {
                    ib_modify_password.setImageResource(R.drawable.yan2x);
                    int sect = edt_modity_password.getSelectionEnd();
                    passwordFlag = true;
                    edt_modity_password
                            .setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());
                    edt_modity_password.setSelection(sect);
                } else {
                    ib_modify_password.setImageResource(R.drawable.yan12x);
                    int sect = edt_modity_password.getSelectionEnd();
                    passwordFlag = false;
                    edt_modity_password
                            .setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());
                    edt_modity_password.setSelection(sect);
                }
                break;
        }
    }

    /**
     * 修改密码验证
     */
    private void check() {
        String password = edt_modity_password.getText().toString()
                .replaceAll(" ", "");
        String oldPass = edt_modify_oldpassword.getText().toString()
                .replaceAll(" ", "");
        if (StringUtil.isEmpty(password)) {
            showToast(R.string.pass_length);
        } else if (password.length() < 6 || password.length() > 14) {
            showToast(R.string.pass_length);
        } else if (StringUtil.isEmpty(oldPass)) {
            showToast(R.string.confirm_pass_cannot_null);
        } else if (!(StringUtil.isEquals(password, oldPass, true))) {
            showToast(R.string.not_same);
        } else {
            btn_modify.setClickable(false);
            // submitData();
            submitData(oldPass);
        }
    }

    /**
     * 修改密码请求服务器 // TODO 发包
     *
     * @param password
     */
    private void submitData(String password) {
        ProgrosDialog.openDialog(this);
        btn_modify.setClickable(false);
        RequestParams rp = new RequestParams();
        if (!StringUtil.isEmpty(getIntent().getStringExtra("tel"))
                && (!StringUtil.isEmpty(getIntent().getStringExtra("tel")))) {
//			rp.addBodyParameter("userName", getIntent().getStringExtra("tel"));
//			rp.addBodyParameter("passWord", password);
//			httpBiz.httPostData(10001, API.CSH_UPDATE_PASSWORD_URL, rp, this);

            String url = NetInterface.HEADER_ALL + NetInterface.RESET_PASSWORD + NetInterface.SUFFIX;
            Map<String, Object> param = new HashMap<>();
            param.put("userName", getIntent().getStringExtra("tel"));
            password = KeyGenerator.encrypt(password);
            param.put("password", password);
            param.put("password_confirm", password);
            netWorkHelper.PostJson(url, param, this);
        } else {
            showToast(R.string.tel_error);
        }
    }


    @Override
    public void receive(String data) {
        ProgrosDialog.closeProgrosDialog();
        BaseResponse baseResponse = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
        if (!baseResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            btn_modify.setClickable(true);
            showToast(baseResponse.getDesc());
            return;
        }


        // TODO 修改密码
        btn_modify.setClickable(true);
        showToast(R.string.pass_success);
        SharedPreferences preferences = PasswordModifyActivity.this
                .getSharedPreferences("user", MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putString("pass", "");
        editor.commit();
        okShow();
    }


    @Override
    public void error(String errorMsg) {
        ProgrosDialog.closeProgrosDialog();
        btn_modify.setClickable(true);
        showToast(R.string.server_link_fault);
    }



    /**
     * 密码重置成功操作
     */
    @SuppressLint("NewApi")
    private void okShow() {
        ll__modify_pass.setVisibility(View.GONE);
        ll_modify_oldpassword.setVisibility(View.GONE);
        btn_modify.setVisibility(View.GONE);
        img_setPassOk.setVisibility(View.VISIBLE);
        tv_newpass_login.setVisibility(View.VISIBLE);
        ll_time.setVisibility(View.VISIBLE);
        img_line2.setImageResource(R.drawable.mima_line2);
        img_ok.setBackgroundResource(R.color.main_blue);
//		img_ok.invalidate();
        tv_ok.setTextColor(getResources().getColor(R.color.main_blue));
        time.start();
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_time.setText(millisUntilFinished / 1000 + "");
        }

        @Override
        public void onFinish() {
            PasswordModifyActivity.this.finish();
        }
    }

    /**
     * 对Android系统返回键进行监听
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            time.cancel();
            PasswordModifyActivity.this.finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
