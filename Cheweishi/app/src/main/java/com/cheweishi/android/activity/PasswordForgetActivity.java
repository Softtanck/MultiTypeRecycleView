package com.cheweishi.android.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.cheweishi.android.R;
import com.cheweishi.android.biz.HttpBiz;
import com.cheweishi.android.config.API;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.tools.RegularExpressionTools;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.StringUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.ResType;
import com.lidroid.xutils.view.annotation.ResInject;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xiaojin修改密码
 */
public class PasswordForgetActivity extends BaseActivity implements
        OnClickListener {
    @ViewInject(R.id.pass_forget_phonenumber)
    private EditText pass_forget_phonenumber;
    @ViewInject(R.id.pass_forget_edt_code)
    private EditText pass_forget_edt_code;
    @ViewInject(R.id.btn_pass_forget_getcode)
    private Button btn_pass_forget_getcode;
    @ViewInject(R.id.btn_next)
    private Button btn_next;
    @ViewInject(R.id.ll_pass_forget_message_remind)
    private LinearLayout ll_pass_forget_message_remind;
    @ViewInject(R.id.tv_pass_forget_remind)
    private TextView tv_pass_forget_remind;
    private String mCode;
    private TimeCount time;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.left_action)
    private Button left_action;
    @ViewInject(R.id.tv_pass_forget_remind)
    private TextView tv_remind;
    @ViewInject(R.id.tv_notsms)
    private TextView tv_notSms;
    @ViewInject(R.id.tv_voice)
    private TextView tv_voice;
    @ResInject(id = R.string.try_try, type = ResType.String)
    private String str1;
    @ResInject(id = R.string.code_sound, type = ResType.String)
    private String str2;
    @ResInject(id = R.string.ba, type = ResType.String)
    private String str3;
    private String path = "SMS";


    private static final int FORGET_PASSWORD = 1;
    private boolean TIME_OUT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_forget);
        ViewUtils.inject(this);
        initViews();
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        Intent intent = getIntent();
        if (intent.getStringExtra("tel") != null) {
            String str = intent.getStringExtra("tel");
            pass_forget_phonenumber.setText(str);
            pass_forget_phonenumber.setSelection(str.length());
        }
        time = new TimeCount(60000, 1000);// 设置倒计时为一分钟
        left_action.setText(R.string.back);
        title.setText(R.string.pass_forget);
    }

    @OnClick({R.id.btn_pass_forget_getcode, R.id.btn_next, R.id.left_action,
            R.id.tv_voice})
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btn_pass_forget_getcode:// 获取验证码
                TIME_OUT = false;
                checkCode();
                break;
            case R.id.btn_next:
                checkNext();
                break;
            case R.id.left_action:
                if (time != null) {
                    time.cancel();
                }
                PasswordForgetActivity.this.finish();
                break;
            case R.id.tv_voice:// 语音验证码
                TIME_OUT = false;
                initColorTextView(false);
                tv_voice.setClickable(false);
                checkVoiceCode();
                break;
        }
    }

    /**
     * 获取语音验证码
     */
    private void checkVoiceCode() {
        String phoneNum = pass_forget_phonenumber.getText().toString();
        if (StringUtil.isEmpty(phoneNum)) {
            showToast(R.string.tel_cannot_null);
            time.cancel();
            btn_next.setText(R.string.next);
            btn_next.setClickable(true);
            btn_pass_forget_getcode.setTextColor(PasswordForgetActivity.this
                    .getApplicationContext().getResources()
                    .getColor(R.color.orange_text_color));
        } else if (!RegularExpressionTools.isMobile(phoneNum)) {
            showToast(R.string.tel_error);
            time.cancel();
            btn_next.setText(R.string.code_get);
            btn_next.setClickable(true);
            btn_pass_forget_getcode.setTextColor(PasswordForgetActivity.this
                    .getApplicationContext().getResources()
                    .getColor(R.color.orange_text_color));
        } else {
            path = "VOICE";

            btn_pass_forget_getcode.setTextColor(PasswordForgetActivity.this
                    .getApplicationContext().getResources()
                    .getColor(R.color.btn_gray_normal));
            submitPhone(phoneNum, path);

        }
    }

    /**
     * 获取短信验证码
     */
    private void checkCode() {
        String phoneNum = pass_forget_phonenumber.getText().toString();
        if (StringUtil.isEmpty(phoneNum)) {
            showToast(R.string.tel_cannot_null);
            initColorTextView(true);
        } else if (!RegularExpressionTools.isMobile(phoneNum)) {
            showToast(R.string.tel_error);
            initColorTextView(true);
        } else {
            path = "SMS";
            tv_notSms.setVisibility(View.VISIBLE);
            tv_voice.setVisibility(View.VISIBLE);
            initColorTextView(true);
            tv_voice.setClickable(true);
            time.start();
            btn_pass_forget_getcode.setTextColor(PasswordForgetActivity.this
                    .getApplicationContext().getResources()
                    .getColor(R.color.btn_gray_normal));
            submitPhone(phoneNum, path);

        }
    }

    /**
     * 设置TextView字体颜色
     */
    private void initColorTextView(boolean isclick) {
        SpannableString sp;
        sp = new SpannableString(str1 + str2 + str3);
        sp.setSpan(
                new ForegroundColorSpan(getResources().getColor(
                        R.color.text_black_colcor)), 0, str1.length(),
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        if (isclick) {
            sp.setSpan(
                    new ForegroundColorSpan(getResources().getColor(
                            R.color.orange_text_color)), str1.length(),
                    (str1 + str2).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        } else {
            sp.setSpan(
                    new ForegroundColorSpan(getResources().getColor(
                            R.color.gray_pressed)), str1.length(),
                    (str1 + str2).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        sp.setSpan(
                new ForegroundColorSpan(getResources().getColor(
                        R.color.text_black_colcor)), (str1 + str2).length(),
                (str1 + str2 + str3).length(),
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv_voice.setText(sp);
    }

    /**
     * 点击下一步
     */
    private void checkNext() {
        if(TIME_OUT){
            showToast("验证码获取超时");
            return;
        }
        String phoneNum = pass_forget_phonenumber.getText().toString()
                .replaceAll(" ", "");
        String code = pass_forget_edt_code.getText().toString()
                .replaceAll(" ", "");
        if (StringUtil.isEmpty(phoneNum)) {
            showToast(R.string.tel_cannot_null);
        } else if (!RegularExpressionTools.isMobile(phoneNum)) {
            showToast(R.string.tel_error);
        } else if (StringUtil.isEmpty(code)) {
            showToast(R.string.please_code_enter);
        } else if (!StringUtil.isEquals(code, mCode, true)) {
            showToast(R.string.code_error);
        } else {
            Intent intent = new Intent(PasswordForgetActivity.this,
                    PasswordModifyActivity.class);
            intent.putExtra("tel", phoneNum);
            startActivity(intent);
            PasswordForgetActivity.this.finish();
        }
    }

    /**
     * 请求验证码 // TODO 发包
     *
     * @param phoneNumber
     * @param path
     */
    private void submitPhone(String phoneNumber, String path) {

        // TODO old 只发一次
//        Log.i("result", "==submitPhone==" + phoneNumber + "==path=" + path);
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("called", phoneNumber);
//        params.addBodyParameter("type", "1");
//        params.addBodyParameter("path", path);
//        httpBiz.httPostData(10001, API.CSH_CODE_URL, params, this);

        String url = NetInterface.HEADER_ALL + NetInterface.SMS_TOKEN + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("mobileNo", phoneNumber);
        param.put("tokenType", "FINDPWD");
        param.put("sendType", path);
        netWorkHelper.PostJson(url, param, this);

    }

    @Override
    public void receive(String data) {
        BaseResponse baseResponse = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
        if (!baseResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            showToast(baseResponse.getDesc());
            btn_pass_forget_getcode.setClickable(true);
            btn_next.setClickable(true);
            time.cancel();
            btn_pass_forget_getcode.setText(R.string.get_again);
            btn_pass_forget_getcode.setTextColor(PasswordForgetActivity.this
                    .getApplicationContext().getResources()
                    .getColor(R.color.orange_text_color));
            btn_next.setClickable(true);
            tv_voice.setClickable(true);
            initColorTextView(true);
            return;
        }

        // TODO 解析数据...
        btn_next.setClickable(true);
        showToast(getResources().getString(R.string.code_success));
        time.start();
        mCode = baseResponse.getDesc();
        tv_remind.setText(pass_forget_phonenumber.getText()
                .toString().replaceAll(" ", ""));
    }

    @Override
    public void error(String errorMsg) {
        showToast(R.string.server_link_fault);
        btn_pass_forget_getcode.setClickable(true);
        btn_next.setClickable(true);
        time.cancel();
        btn_pass_forget_getcode.setText(R.string.get_again);
        btn_pass_forget_getcode.setTextColor(PasswordForgetActivity.this
                .getApplicationContext().getResources()
                .getColor(R.color.orange_text_color));
        btn_next.setClickable(true);
        tv_voice.setClickable(true);
        initColorTextView(true);
    }


    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_pass_forget_getcode.setClickable(false);
            btn_pass_forget_getcode.setText(millisUntilFinished / 1000
                    + getResources().getString(R.string.time_second));
        }

        @Override
        public void onFinish() {
            TIME_OUT = true;
            btn_pass_forget_getcode.setText(R.string.get_again);
            btn_pass_forget_getcode.setClickable(true);
            tv_voice.setClickable(true);
            initColorTextView(true);
            btn_pass_forget_getcode.setTextColor(PasswordForgetActivity.this
                    .getApplicationContext().getResources()
                    .getColor(R.color.orange_text_color));
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (time != null) {
                time.cancel();
            }
            PasswordForgetActivity.this.finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
