package com.cheweishi.android.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.config.API;
import com.cheweishi.android.config.Config;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ImgDialog;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.LoginResponse;
import com.cheweishi.android.entity.LoginUserInfoResponse;
import com.cheweishi.android.entity.RegisterResponse;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.tools.APPTools;
import com.cheweishi.android.tools.DBTools;
import com.cheweishi.android.tools.LoginMessageUtils;
import com.cheweishi.android.tools.RegularExpressionTools;
import com.cheweishi.android.tools.SharePreferenceTools;
import com.cheweishi.android.utils.ActivityControl;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.KeyGenerator;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.ResType;
import com.lidroid.xutils.view.annotation.ResInject;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册页面
 *
 * @author mingdasen
 */
public class RegistActivity extends BaseActivity {
    @ViewInject(R.id.phonenumber)
    private EditText mPhoneNumberEditText;// 手机号码输入框
    @ViewInject(R.id.edt_code)
    private EditText mCodeEditText;// 验证吗输入框
    @ViewInject(R.id.edt_password)
    private EditText mPasswordEditText;// 密码输入框
    @ViewInject(R.id.idcard)
    private EditText mIdcardEditText;
    private TimeCount time;
    @ViewInject(R.id.linear_saoma)
    private LinearLayout mSaomaLinearLayout;
    @ViewInject(R.id.btn_getcode)
    private Button mGetcodeButton;
    @ViewInject(R.id.btn_register)
    private Button mRegisterButton;
    @ViewInject(R.id.left_action)
    private TextView leftaction;
    @ViewInject(R.id.title)
    private TextView title1;
    @ViewInject(R.id.right_action)
    private TextView rightaction;
    @ViewInject(R.id.edt_nickName)
    private EditText edt_nickName;
    private String mCode;
    @ViewInject(R.id.ll_nick)
    private LinearLayout ll_nick;
    @ViewInject(R.id.ll_phone)
    private LinearLayout ll_phone;
    @ViewInject(R.id.ll_message_remind)
    private LinearLayout ll_message_remind;
    @ViewInject(R.id.ll_vote)
    private LinearLayout ll_vote;
    @ViewInject(R.id.ll_pass)
    private LinearLayout ll_pass;
    @ViewInject(R.id.ib_password)
    private ImageButton ib_password;
    @ViewInject(R.id.tv_remind)
    private TextView tv_remind;
    @ViewInject(R.id.ll_regist_service)
    private LinearLayout ll_regist_service;
    @ViewInject(R.id.service_checkbox)
    private CheckBox service_checkbox;
    @ViewInject(R.id.tv_service)
    private TextView tv_service;
    @ViewInject(R.id.tv_notsms)
    private TextView tv_notsms;
    @ViewInject(R.id.tv_voice)
    private TextView tv_voice;
    @ResInject(id = R.string.try_try, type = ResType.String)
    private String str1;
    @ResInject(id = R.string.code_sound, type = ResType.String)
    private String str2;
    @ResInject(id = R.string.ba, type = ResType.String)
    private String str3;
    private boolean isclick = false;
    private String path = "SMS"; // 默认为短信
    private boolean passwordFlag = false;
    private boolean getCodeFlag = false;
    @ViewInject(R.id.edt_confirmpassword)
    private EditText edt_confirmpassword;
    @ViewInject(R.id.tv_error)
    private TextView tv_error;

    private boolean TIME_OUT;

    private String userId;


    private static final String REGISTER = "REG";// 注册
    private ImgDialog.Builder imgBuilder;
    private ImgDialog imgDialog;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ViewUtils.inject(this);
        initView();
    }

    private void initView() {
        time = new TimeCount(60000, 1000);
        leftaction.setText(getResources().getString(R.string.back));
        rightaction.setVisibility(View.GONE);
        title1.setText(getResources().getString(R.string.register));
    }


    @OnClick({R.id.btn_getcode, R.id.left_action, R.id.btn_register,
            R.id.linear_saoma, R.id.ib_password, R.id.tv_service, R.id.tv_voice})
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.btn_getcode:
                TIME_OUT = false;
                checkCode();
                break;
            case R.id.left_action:
                cancel();
                break;
            case R.id.btn_register:
                goOn();
                break;
            case R.id.linear_saoma:
                break;
            case R.id.ib_password:
                changePasswordVisible();
                break;
            case R.id.tv_service:
                Intent intent = new Intent(RegistActivity.this,
                        RegistServiceActivity.class);
                RegistActivity.this.startActivity(intent);
                break;
            case R.id.tv_voice:// 获取语音验证码
                TIME_OUT = false;
                path = "VOICE";
                if (time != null) {
                    time.cancel();
                }
                mGetcodeButton.setClickable(false);
                tv_voice.setClickable(false);

                isclick = false;
                mGetcodeButton.setTextColor(RegistActivity.this
                        .getApplicationContext().getResources()
                        .getColor(R.color.btn_gray_normal));
                initColorTextView(isclick);
                getCode();
                break;
            default:
                break;
        }

    }

    /**
     * 点击下一步
     */
    private void goOn() {
        if (TIME_OUT) {
            showToast("验证码超时");
            return;
        }

        if (ll_phone.getVisibility() == View.VISIBLE) {// 当前显示的为手机号码获取验证码界面
            if (StringUtil.isEmpty(mPhoneNumberEditText.getText().toString()
                    .replaceAll(" ", ""))) {
                showToast(R.string.tel_cannot_null);
            } else if (RegularExpressionTools.isMobile(mPhoneNumberEditText
                    .getText().toString().replaceAll(" ", "")) == false) {
                showToast(R.string.tel_error);
            } else if (StringUtil.isEmpty(mCodeEditText.getText().toString()
                    .replaceAll(" ", ""))) {
                showToast(R.string.please_code_enter);
            } else if (StringUtil.isEmpty(mCode)) {
                showToast(R.string.code_error);
            } else if (!mCode.equals(mCodeEditText.getText().toString())) {
                initColorTextView(true);
                tv_voice.setClickable(true);
                mGetcodeButton.setTextColor(getResources().getColor(
                        R.color.orange_text_color));
                showToast(R.string.code_error);
            } else {
                if (service_checkbox.isChecked() == true) {
                    tv_voice.setVisibility(View.GONE);
                    tv_notsms.setVisibility(View.GONE);
                    ll_message_remind.setVisibility(View.GONE);
                    ll_phone.setVisibility(View.GONE);
                    ll_vote.setVisibility(View.GONE);
                    ll_regist_service.setVisibility(View.GONE);
                    ll_pass.setVisibility(View.VISIBLE);
                    tv_error.setVisibility(View.INVISIBLE);
                    edt_confirmpassword.setText("");
                    title1.setText(R.string.pass_setting);
                    mRegisterButton.setText(R.string.idea_refer);
                    // time.cancel();
                    mGetcodeButton.setText(R.string.code_get);
                    APPTools.closeBoard(RegistActivity.this,
                            mPhoneNumberEditText);
                } else {
                    showToast(R.string.service_rule);
                }
            }
        } else if (ll_pass.getVisibility() == View.VISIBLE) {// 当前显示的为设置密码界面

            if (StringUtil.isEmpty(mPasswordEditText.getText().toString())) {
                showToast(R.string.pass_cannot_null);
            } else {
                if (mPasswordEditText.getText().toString().replaceAll(" ", "")
                        .length() < 6
                        || mPasswordEditText.getText().toString()
                        .replaceAll(" ", "").length() > 14) {
                    showToast(R.string.pass_length);
                } else {
                    if (!edt_confirmpassword.getText().toString()
                            .equals(mPasswordEditText.getText().toString())) {
                        tv_error.setVisibility(View.VISIBLE);
                    } else {
                        tv_error.setVisibility(View.INVISIBLE);
                        register();
                        APPTools.closeBoard(RegistActivity.this, edt_nickName);
                    }
                }

            }
        } else if (ll_nick.getVisibility() == View.VISIBLE) {
            if (StringUtil.isEmpty(edt_nickName.getText().toString()
                    .replaceAll(" ", ""))) {
                showToast("昵称不能为空！");
            } else {
                register();
                APPTools.closeBoard(RegistActivity.this, edt_nickName);
            }
        }
    }

    /**
     * 点击返回
     */
    private void cancel() {
        // if (time != null) {
        // time.cancel();
        // }
        if (ll_phone.getVisibility() == View.VISIBLE) {// 当前显示的为手机号码获取验证码界面
            mPhoneNumberEditText.setText("");
            // ll_phone.setVisibility(View.GONE);
            RegistActivity.this.finish();
            // ll_nick.setVisibility(View.VISIBLE);

        } else if (ll_pass.getVisibility() == View.VISIBLE) {// 当前显示的为设置密码界面
            mRegisterButton.setText(R.string.next);
            title1.setText(R.string.register);
            tv_voice.setVisibility(View.VISIBLE);
            tv_notsms.setVisibility(View.VISIBLE);
            mPasswordEditText.setText("");
            ll_pass.setVisibility(View.GONE);
            ll_phone.setVisibility(View.VISIBLE);
            ll_message_remind.setVisibility(View.VISIBLE);
            ll_regist_service.setVisibility(View.VISIBLE);
            ll_vote.setVisibility(View.VISIBLE);
        } else {
            mRegisterButton.setText(R.string.next);
            title1.setText(R.string.register);
            ll_pass.setVisibility(View.VISIBLE);
            ll_nick.setVisibility(View.GONE);
            edt_nickName.setText("");
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
     * 设置密码可见/不可见
     */
    private void changePasswordVisible() {
        if (passwordFlag == false) {
            ib_password.setImageResource(R.drawable.yan2x);
            int sect = mPasswordEditText.getSelectionEnd();
            passwordFlag = true;
            mPasswordEditText
                    .setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
            mPasswordEditText.setSelection(sect);
        } else {
            ib_password.setImageResource(R.drawable.yan12x);
            int sect = mPasswordEditText.getSelectionEnd();
            passwordFlag = false;
            mPasswordEditText
                    .setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
            mPasswordEditText.setSelection(sect);
        }
    }

    /**
     * 获取验证码验证
     */
    private void checkCode() {
        isclick = true;
        path = "SMS";
        mGetcodeButton.setClickable(false);
        if (mPhoneNumberEditText.getText().toString().equals("")) {
            showToast(getResources().getString(R.string.tel_cannot_null));
            mGetcodeButton.setClickable(true);
        } else {
            if (RegularExpressionTools.isMobile(mPhoneNumberEditText.getText()
                    .toString())) {

                getCode();
                tv_voice.setVisibility(View.VISIBLE);
                tv_notsms.setVisibility(View.VISIBLE);
            } else {
                showToast(getResources().getString(R.string.tel_error));
                mGetcodeButton.setClickable(true);
            }
        }
        initColorTextView(isclick);
    }

    /**
     * 获取验证码
     */
    protected void getCode() {
        String phoneNumber = mPhoneNumberEditText.getText().toString().trim();
        if (!StringUtil.isEmpty(phoneNumber)) {
            submitPhone(phoneNumber, path);
        }
    }

    /**
     * 注册
     */
    protected void register() {
        mRegisterButton.setClickable(false);
        String phoneNumber = mPhoneNumberEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();
        String idCard = mIdcardEditText.getText().toString().trim();
        submitData(phoneNumber, password, idCard);
    }

    /**
     * 注册 // TODO 注册发包
     *
     * @param phoneNumber
     * @param password
     * @param idCard
     */
    private void submitData(String phoneNumber, String password, String idCard) {
        LogHelper.d("=====注册请求===");
        getCodeFlag = false;

        // TODO old
//        String phoneDevice = getinfor();
//        String phoneSystem = android.os.Build.VERSION.RELEASE;// 系统版本
//        String mobileVersion = android.os.Build.MODEL;// 手机型号
//        // SharedPreferences preferences = getSharedPreferences("device_token",
//        // MODE_PRIVATE);
//        // String str = preferences.getString("device_token", "");
//        String appVersion = APPTools.getVersionName(RegistActivity.this);
//        // if (StringUtil.isEmpty(str)) {
//        // str = "no_device_token";
//        // }
//        // String urlString =
//        // "http://115.28.161.11:8080/XAI/app/cws/appRegist.do"
//        // + "?tel=" + phoneNumber + "&psw=" + password + "&imei=" + str
//        // + "&nick=" + phoneNumber + "&phone=" + phoneDevice + "&system="
//        // + phoneSystem + "&app="
//        // + APPTools.getVersionName(RegistActivity.this);
//                + password + "=appVersion=" + appVersion + "=mobileVersion="
//                + mobileVersion + "=mobileSystem=" + phoneSystem + "=imei="
//                + phoneDevice);
//        RequestParams rp = new RequestParams();
//        rp.addBodyParameter("userName", phoneNumber);
//        rp.addBodyParameter("passWord", password);
//        rp.addBodyParameter("appVersion", appVersion);// App版本信息
//        rp.addBodyParameter("mobileVersion", mobileVersion);// 手机版本信息
//        rp.addBodyParameter("mobileSystem", phoneSystem);// 手机系统版本信息
//        rp.addBodyParameter("imei", phoneDevice);// 手机唯一标识符
//        rp.addBodyParameter("type", "2");// 注册平台：1，IOS；2，Android；3，PC
//        ProgrosDialog.openDialog(RegistActivity.this);
//        httpBiz.httPostData(100002, API.CSH_REGISTER_URL, rp, this);


        // TODO new interface
        String url = NetInterface.HEADER_ALL + NetInterface.REGISTER + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userName", phoneNumber);
        password = KeyGenerator.encrypt(password);
        param.put("password", password);
        param.put("password_confirm", password);
        param.put(Constant.PARAMETER_TAG, NetInterface.REGISTER);
        netWorkHelper.PostJson(url, param, this);
    }

    /**
     * 获取验证码 // TODO 发包
     *
     * @param phoneNumber
     * @param path
     */
    private void submitPhone(String phoneNumber, String path) {
        getCodeFlag = true;
        // TODO old
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("called", phoneNumber);
//        params.addBodyParameter("type", "0");// 0注册， 1找回密码
//        params.addBodyParameter("path", path);
//        httpBiz.httPostData(100001, API.CSH_CODE_URL, params, this);
        mPhoneNumberEditText.setFocusable(false);

        // TODO new Interface
        String url = NetInterface.HEADER_ALL + NetInterface.SMS_TOKEN + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("mobileNo", phoneNumber);
        param.put("tokenType", REGISTER);
        param.put("sendType", path);
        param.put(Constant.PARAMETER_TAG, NetInterface.SMS_TOKEN);
        netWorkHelper.PostJson(url, param, this);
    }


    @Override
    public void receive(String TAG, String data) {
        ProgrosDialog.closeProgrosDialog();

        switch (TAG) {
            case NetInterface.SMS_TOKEN:
                BaseResponse base = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
                //错误的时候
                if (!base.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    showToast(base.getDesc());
                    if (getCodeFlag == true) {
                        mRegisterButton.setClickable(true);
                        time.cancel();
                        mGetcodeButton.setTextColor(RegistActivity.this
                                .getApplicationContext().getResources()
                                .getColor(R.color.orange_text_color));
                        mGetcodeButton.setText(R.string.get_again);
                    } else {
                        initColorTextView(true);
                        tv_voice.setClickable(true);
                        mRegisterButton.setClickable(true);
                    }
                    mGetcodeButton.setClickable(true);
                    return;
                }


                mGetcodeButton.setTextColor(RegistActivity.this
                        .getApplicationContext().getResources()
                        .getColor(R.color.btn_gray_normal));
                time.start();
                mCode = base.getDesc();
                tv_remind.setText(mPhoneNumberEditText.getText().toString()
                        .trim());
                showToast(getResources().getString(R.string.code_success));
                break;

            case NetInterface.REGISTER:

                RegisterResponse response = (RegisterResponse) GsonUtil.getInstance().convertJsonStringToObject(data, RegisterResponse.class);
                if (response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    LoginMessageUtils.setLogined(this, true);
                    userId = response.getDesc();
                    token = response.getToken();
                    if (null != response.getMsg()) {
                        if (response.getMsg().isIsGetCoupon()) {
                            showImgDialog();
                        } else {
                            save(response.getDesc());
                        }
                    } else {
                        save(response.getDesc());
                    }
//                    showImgDialog();


                } else {
                    showToast(response.getDesc());
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
                        save(userId);
                        isExit = true;
                        dialog.dismiss();
                    }
                });

        imgDialog = imgBuilder.create();
        imgDialog.setCanceledOnTouchOutside(false);

        imgDialog.show();
        imgDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!isExit) {
                    save(userId);
                }
            }
        });
    }

    @Override
    public void error(String errorMsg) {
        ProgrosDialog.closeProgrosDialog();
        mRegisterButton.setClickable(true);
        showToast(R.string.server_link_fault);
    }


    public String getinfor() {
        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String m_szImei = TelephonyMgr.getDeviceId();
        return m_szImei;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Config.SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String id = bundle.getString("result");
                    mIdcardEditText.setText(id);
                }
                break;

            default:
                break;
        }
    }


    /**
     * 设置电话和uid
     *
     * @param userId
     */
    protected void save(String userId) {
        SharePreferenceTools.setUser(RegistActivity.this, mPhoneNumberEditText
                .getText().toString(), mPasswordEditText.getText().toString());
//        LoginMessage loginMessage = new LoginMessage();
//        loginMessage.setUid(userId); // 存储Uid
//        loginMessage.setTel(mPhoneNumberEditText
//                .getText().toString());// 存储编辑框
        loginResponse = new LoginResponse();
        LoginUserInfoResponse loginUserInfoResponse = new LoginUserInfoResponse("", mPhoneNumberEditText.getText().toString(), "", userId, "", mPhoneNumberEditText.getText().toString(), "", "", "", "");
        loginUserInfoResponse.setId(userId);
        loginUserInfoResponse.setUserName(mPhoneNumberEditText.getText().toString());
        loginResponse.setMsg(loginUserInfoResponse);
        loginResponse.setDesc(userId);
        loginResponse.setToken(token);
        LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
        ActivityControl.removeActivityFromName(LoginActivity.class.getName());
        LoginMessageUtils.setLogined(this, true);
        startActivity(new Intent(RegistActivity.this, MainNewActivity.class));
        this.finish();
    }


    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mGetcodeButton.setClickable(false);
            mGetcodeButton.setText(millisUntilFinished / 1000
                    + getResources().getString(R.string.time_second));
        }

        @Override
        public void onFinish() {
            TIME_OUT = true;
            mGetcodeButton.setTextColor(RegistActivity.this
                    .getApplicationContext().getResources()
                    .getColor(R.color.orange_text_color));
            mGetcodeButton.setText(R.string.get_again);
            mGetcodeButton.setClickable(true);
            initColorTextView(true);
            tv_voice.setClickable(true);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(RegistActivity.this.getClass().getName()); // 统计页面
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(RegistActivity.this.getClass().getName()); // 中会保存信息
//        MobclickAgent.onPause(this);
    }

    /**
     * 对Android系统返回键进行监听
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (null != userId && !"".equals(userId)) {
            save(userId);
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            cancel();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (time != null) {
            time.cancel();
        }
    }

//	@Override
//	public void call() {
    // ProgrosDialog.closeProgrosDialog();
    // ActivityControl.removeActivityFromName(LoginActivity.class.getName());
    // startActivity(new Intent(RegistActivity.this,
    // RegistFinishActivity.class));
    // RegistActivity.this.finish();
//	}

}
