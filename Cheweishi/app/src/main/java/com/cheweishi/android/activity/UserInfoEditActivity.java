package com.cheweishi.android.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.config.API;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.UpLoadPhotoResponse;
import com.cheweishi.android.tools.LangUtils;
import com.cheweishi.android.tools.LoginMessageUtils;
import com.cheweishi.android.tools.PhotoTools;
import com.cheweishi.android.tools.ReLoginDialog;
import com.cheweishi.android.utils.BitmapUtils;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.ClearEditText;
import com.cheweishi.android.widget.CustomDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 个人信息界面
 *
 * @author mingdasen，修改徐小金
 */
public class UserInfoEditActivity extends BaseActivity implements
        OnClickListener {
    @ViewInject(R.id.ll_addPlateTopHide)
    private LinearLayout ll_addPlateTopHide;
    @ViewInject(R.id.ll_plate_choose_layout)
    private LinearLayout ll_plate_choose_layout;
    @ViewInject(R.id.et_user_nick)
    private ClearEditText et_userNick;
    @ViewInject(R.id.et_user_age)
    private TextView et_userAge;
    @ViewInject(R.id.rbtn_man)
    private RadioButton man;
    @ViewInject(R.id.rbtn_girl)
    private RadioButton girl;
    @ViewInject(R.id.sp_city)
    private TextView tv_city;
    @ViewInject(R.id.tv_specialSign)
    private TextView tv_specialSign;
    @ViewInject(R.id.ll_choose_city)
    private LinearLayout ll_choose_city;
    @ViewInject(R.id.et_email)
    private ClearEditText et_email;
    @ViewInject(R.id.right_action)
    private TextView tv_save;
    @ViewInject(R.id.tv_tel)
    private TextView tv_tel;
    @ViewInject(R.id.tv_user_nick)
    private TextView tv_user_nick;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.left_action)
    private Button left_action;
    @ViewInject(R.id.ll_user_age)
    private LinearLayout ll_user_age;
    @ViewInject(R.id.img_userEdit_userIcon)
    private ImageView img_userEdit_userIcon;
    @ViewInject(R.id.ll_email)
    private LinearLayout ll_email;
    @ViewInject(R.id.ll_user_specialSign)
    private LinearLayout ll_user_specialSign;
    // private String cityId;// 城市
    // private String sex = "0";
    private Dialog dialog2;
    // private String tv_birth = "";
    // private String age = "";
    private int index_selection;
    private boolean isOperation = false;
    private CustomDialog.Builder builder;
    private Dialog dialog1;
    @ViewInject(R.id.btn_layout)
    private LinearLayout btn_layout;
    @ViewInject(R.id.origionalImg)
    private ImageView origionalImg;
    public static final int TAKE_A_PICTURE = 10;
    private String mAlbumPicturePath = null;
    private final int UPLOAD_IMG_TYPE = 10003;
    private MyBroadcastReceiver broad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_edit);
        ViewUtils.inject(this);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 注册刷新广播
        if (broad == null) {
            broad = new MyBroadcastReceiver();
        }
        IntentFilter intentFilter = new IntentFilter(Constant.REFRESH_FLAG);
        registerReceiver(broad, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broad);
    }

    /**
     * 初始化
     */
    private void init() {
        // 设置editText显示可水平滑动
        et_userNick.setHorizontallyScrolling(true);
        et_email.setHorizontallyScrolling(true);

        // 设置title的text
        tv_save.setText(R.string.finish);
        tv_save.setVisibility(View.GONE);
        title.setText(R.string.information_edit);
        left_action.setText(R.string.back);

        // 设置editText输入监听
        et_userNick.addTextChangedListener(watcher);
        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                isOperation = true;
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
        initdata();
        // connectToServer();
    }

    /**
     * 初始化数据
     */
    private void initdata() {
        if (isLogined()) {
            XUtilsImageLoader.getxUtilsImageLoader(this,
                    R.drawable.info_touxiang_moren, img_userEdit_userIcon,
                    loginResponse.getMsg().getPhoto());
            if (hasNote()) {
                tv_specialSign.setText(loginResponse.getMsg().getSignature());
            } else {
                tv_specialSign.setText("请填写");
            }
            if (hasNick()) {
                tv_user_nick.setText(loginResponse.getMsg().getNickName());
            }
            if (hasTel()) {
                try {
                    tv_tel.setText(loginResponse.getMsg().getUserName().substring(0, 3)
                            + "***"
                            + loginResponse.getMsg().getUserName().substring(
                            loginResponse.getMsg().getUserName().length() - 4,
                            loginResponse.getMsg().getUserName().length()));
                } catch (Exception e) {

                }

            }
        } else {
            tv_specialSign.setText("请填写");
            XUtilsImageLoader.getxUtilsImageLoader(this,
                    R.drawable.info_touxiang_moren, img_userEdit_userIcon, "");
        }
    }

    String strArrayOne = "";
    String strArrayTwo = "";
    private int posBefore;
    private String strBefore = "";
    /**
     * 限制昵称输入的字数和字符
     */
    TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (judge(s.toString()) == true) {
            } else {
                if (et_userNick.getSelectionEnd() >= posBefore) {
                    et_userNick.setText(cutString(s.toString().substring(
                            posBefore, et_userNick.getSelectionStart())));
                }
                et_userNick.setSelection(index_selection);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int before,
                                      int count) {
            isOperation = true;
            strBefore = s.toString();
            posBefore = et_userNick.getSelectionEnd();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 获取editText显示内容
     *
     * @param cutStr
     * @return
     */
    private String cutString(String cutStr) {
        String endStr = "";
        strArrayOne = strBefore.substring(0, posBefore);
        strArrayTwo = strBefore.substring(posBefore, strBefore.length());

        for (int i = 0; i < cutStr.length(); i++) {
            String indexStr = cutStr.substring(0, i);
            if (judge(strArrayOne + indexStr + strArrayTwo) == true) {
                endStr = strArrayOne + indexStr + strArrayTwo;
            } else {
                if (i > 1) {

                    endStr = strArrayOne + cutStr.substring(0, i - 1)
                            + strArrayTwo;
                    et_userNick.setSelection((strArrayOne + cutStr.substring(0,
                            i - 1)).length());
                    index_selection = (strArrayOne + cutStr.substring(0, i - 1))
                            .length();
                } else {
                    endStr = strArrayOne + strArrayTwo;
                    et_userNick.setSelection(strArrayOne.length());
                    index_selection = strArrayOne.length();
                }
                break;
            }
        }
        return endStr;
    }

    /**
     * 判断汉字的个数
     *
     * @param fontStr
     * @return
     */
    private int getFontCount(String fontStr) {
        int chiCount = 0;
        for (int i = 0; i < fontStr.length(); i++) {
            if (LangUtils.isChinese((fontStr.charAt(i) + ""))) {
                chiCount++;
            }
        }

        return chiCount;
    }

    /**
     * 判断字符串长度是否超出14
     *
     * @param fontStr
     * @return
     */
    private boolean judge(String fontStr) {
        int fontOne = getFontCount(fontStr);
        int fontSecond = fontStr.length() - fontOne;
        return (fontOne * 2 + fontSecond) <= 14;
    }

    @OnClick({R.id.left_action, R.id.right_action, R.id.rbtn_man,
            R.id.rbtn_girl, R.id.ll_user_age, R.id.ll_choose_city,
            R.id.xcRoundImg, R.id.origionImgBtn, R.id.xiangji, R.id.xiangce,
            R.id.quxiao, R.id.ll_user_img, R.id.ll_user_nick,
            R.id.ll_user_specialSign})
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.left_action:// 返回
                if (isOperation) {
                    showBackDialog();
                } else {
                    UserInfoEditActivity.this.finish();
                }
                break;
            case R.id.right_action:// 保存
                closeBoard(UserInfoEditActivity.this, et_userNick);
                closeBoard(UserInfoEditActivity.this, et_email);
                if (StringUtil.isEmpty(et_userNick.getText().toString())) {
                    showToast(R.string.user_nick_not_hint);
                    return;
                }
                if (!isNick(et_userNick.getText().toString())) {
                    showToast(R.string.user_nick_illegal);
                    return;
                }

                // if (!sex.equals("1") && !sex.equals("2")) {
                // showToast(R.string.change_sex);
                // return;
                // }
                // if (StringUtil.isEmpty(et_userAge.getText().toString())) {
                // showToast(R.string.change_age);
                // return;
                // }
                // if (StringUtil.isEmpty(tv_city.getText().toString())
                // || StringUtil.isEmpty(cityId)) {
                // showToast(R.string.change_city);
                // return;
                // }
                // if (StringUtil.isEmpty(et_email.getText().toString())
                // || isEmail(et_email.getText() + "")) {
                userInfoSave();
                // } else {
                // showToast(R.string.check_mailbox);
                // }
                break;
            case R.id.ll_user_nick:// 昵称
                if (isLogined()) {
                    intent = new Intent(this, UserNickActivity.class);
                    intent.putExtra("nick", loginResponse.getMsg().getNickName());
                    startActivity(intent);
                }
                break;
            case R.id.ll_user_specialSign:// 签名
                if (isLogined()) {
                    intent = new Intent(this, UserSignActivity.class);
                    intent.putExtra("sign", loginResponse.getMsg().getSignature());
                    startActivity(intent);
                }
                break;
            // case R.id.rbtn_man:// 男
            // isOperation = true;
            // closeBoard(UserInfoEditActivity.this, et_userNick);
            // closeBoard(UserInfoEditActivity.this, et_email);
            // sex = "1";
            // break;
            // case R.id.rbtn_girl:// 女
            // isOperation = true;
            // closeBoard(UserInfoEditActivity.this, et_userNick);
            // closeBoard(UserInfoEditActivity.this, et_email);
            // sex = "2";
            // break;
            // case R.id.ll_user_age:// 年龄
            // isOperation = true;
            // closeBoard(UserInfoEditActivity.this, et_userNick);
            // closeBoard(UserInfoEditActivity.this, et_email);
            // intent = new Intent(UserInfoEditActivity.this,
            // ChooseDateBirthActivity.class);
            // // intent.putExtra("note", et_userAge.getText() + "");
            // UserInfoEditActivity.this.startActivityForResult(intent, 1000);
            // // showDateView();
            // break;
            // case R.id.ll_choose_city:// 城市
            // isOperation = true;
            // closeBoard(UserInfoEditActivity.this, et_userNick);
            // closeBoard(UserInfoEditActivity.this, et_email);
            // intent = new Intent(UserInfoEditActivity.this,
            // ProvinceCityCountryActivity.class);
            // UserInfoEditActivity.this.startActivityForResult(intent, 2015);
            // break;
            case R.id.ll_user_img:
                showImgDialog();
                break;
            case R.id.origionalImg:
                dialog1.dismiss();
                break;
            case R.id.origionImgBtn:
                btn_layout.setVisibility(View.INVISIBLE);
                origionalImg.setVisibility(View.VISIBLE);
                final Animation animation1 = AnimationUtils.loadAnimation(
                        UserInfoEditActivity.this,
                        R.anim.score_business_query_enter);
                origionalImg.startAnimation(animation1);
                XUtilsImageLoader.getxUtilsImageLoader(UserInfoEditActivity.this,
                        R.drawable.info_touxiang_moren, origionalImg,
                        loginResponse.getMsg().getPhoto());
                break;

            // 调用手机相机
            case R.id.xiangji:
                PhotoTools.init();
                dialog1.dismiss();
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                        PhotoTools.IMGPATH, PhotoTools.IMAGE_FILE_NAME)));
                startActivityForResult(intent, TAKE_A_PICTURE);
                break;
            // 调用手机相册
            case R.id.xiangce:
                PhotoTools.init();
                dialog1.dismiss();
                if (PhotoTools.mIsKitKat) {
                    PhotoTools.selectImageUriAfterKikat(UserInfoEditActivity.this);
                } else {
                    PhotoTools.cropImageUri(UserInfoEditActivity.this);
                }
                break;
            case R.id.quxiao:
                dialog1.dismiss();
                break;
            default:
                break;
        }

    }

    /**
     * 判断昵称输入字符是否正确 只能输入汉字、英文、数字以及 _!#+-?字符
     */
    public boolean isNick(String nick) {
        String str = "^[\u4e00-\u9fa5\\w]+|[!#+-?]+$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(nick);
        return m.matches();
    }

    /**
     * 判断email格式是否正确
     */
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 关闭软键盘
     *
     * @param mcontext
     */
    public void closeBoard(Context mcontext) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_userNick.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 保存用户修改信息
     */
    protected void userInfoSave() {
        // String email = "";
        // if (loginResponse != null && loginResponse.getUid() != null) {
        // RequestParams params = new RequestParams();
        // params.addBodyParameter("userInfoModel.id", loginResponse.getUid());
        // params.addBodyParameter("userInfoModel.icon",
        // loginResponse.getIcon());
        // params.addBodyParameter("userInfoModel.nick_name",
        // et_userNick.getText().toString());
        // params.addBodyParameter("userInfoModel.signature",
        // tv_specialSign.getText().toString());
        // params.addBodyParameter("sex", sex);
        // params.addBodyParameter("age", age);
        // params.addBodyParameter("birth", tv_birth);
        // params.addBodyParameter("city", cityId);
        // if (et_email.getText().toString() != null) {
        // email = et_email.getText().toString();
        // } else {
        // email = "";
        // }
        // params.addBodyParameter("email", email);
        // httpBiz = new HttpBiz(UserInfoEditActivity.this);
        // ProgrosDialog.openDialog(UserInfoEditActivity.this);
        // httpBiz.httPostData(1009, API.CSH_UPDATE_USER_INFO_URL, params,
        // this);
        // }
    }

    @Override
    public void receive(int type, String data) {
        ProgrosDialog.closeProgrosDialog();
        switch (type) {
            case 1009:
                parseUserInfoSaveJOSN(data);
                // ProgrosDialog.closeProgrosDialog();
                break;
            case UPLOAD_IMG_TYPE:
                parseImgJSON(data);
                break;
            case 400:
                break;
            default:
                break;
        }
    }

    /**
     * 数据返回结果
     *
     * @param result
     */
    protected void parseUserInfoSaveJOSN(String result) {
        if (StringUtil.isEmpty(result)) {
            showToast(R.string.no_result);
        } else {
            try {
                JSONObject object = new JSONObject(result);
                if (object.optString("state").equals(API.returnSuccess)) {
                    showToast(R.string.change_success);
                    setSuccess();
                } else {
                    showToast(object.optString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 提交成功处理
     */
    private void setSuccess() {
        // String str = "";
        // if ((et_userNick.getText() + "").equals(loginResponse.getNick_name() +
        // "")) {
        // str = Constant.USER_NICK_EDIT_REFRESH_OTHER;
        // } else {
        // // loginResponse.setIcon(icon)
        // loginResponse.setNick_name(et_userNick.getText() + "");
        // loginResponse.setSignature(tv_specialSign.getText() + "");
        // LoginMessageUtils.saveProduct(loginResponse,
        // UserInfoEditActivity.this);
        // str = Constant.USER_NICK_EDIT_REFRESH;
        // }
        // Intent mIntent = new Intent();
        // Constant.CURRENT_REFRESH = str;
        // mIntent.setAction(Constant.REFRESH_FLAG);
        // mIntent.putExtra("flag", "true");
        // sendBroadcast(mIntent);
        // finish();
    }

    /**
     * 返回提示dialog
     */
    private void showBackDialog() {
        builder = new CustomDialog.Builder(UserInfoEditActivity.this);
        builder.setMessage(R.string.message_reLogin);
        builder.setTitle(R.string.remind);
        builder.setMessage(R.string.back_hint);
        builder.setPositiveButton(R.string.sure,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // et_userNick.setCursorVisible(false);
                        // closeBoard(UserInfoEditActivity.this, et_userNick);
                        UserInfoEditActivity.this.finish();

                    }
                });

        builder.setNegativeButton(R.string.cancel,
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        dialog2 = builder.create();
        dialog2.show();

    }

    @Override
    protected void onActivityResult(int arg0, int resultCode, Intent data) {
        Bitmap bitmap;

        switch (arg0) {
            // case 2015:// 城市
            // if (data != null) {
            // // String provinceId = data.getIntExtra("provinceId", 0) +
            // // "";
            // if (StringUtil.isEmpty(data.getStringExtra("cityName"))
            // && StringUtil.isEmpty(data.getStringExtra("cityId"))) {
            // tv_city.setText("");
            // cityId = "";
            // } else {
            // tv_city.setText(data.getStringExtra("cityName"));
            // cityId = data.getStringExtra("cityId");
            // }
            // }
            // break;
            // case 1000:// 年龄 生日
            // if (data != null) {
            // age = data.getStringExtra("age");
            // tv_birth = data.getStringExtra("birth");
            // et_userAge.setText(age + " 岁");
            // }
            // break;
            // case NICK_RETURN_CODE:
            // if (!StringUtil.isEmpty(data.getStringExtra("nick"))) {
            // tv_user_nick.setText(data.getStringExtra("nick"));
            // }
            // break;
            // case SIGN_RETURN_CODE:
            // if (!StringUtil.isEmpty(data.getStringExtra("sign"))) {
            // tv_specialSign.setText(data.getStringExtra("sign"));
            // }
            // break;
            case PhotoTools.SELECT_A_PICTURE:
                if (resultCode == RESULT_OK && null != data) {
                    mAlbumPicturePath = PhotoTools.getPath(getApplicationContext(),
                            data.getData());
                    bitmap = PhotoTools.decodeUriAsBitmap(
                            Uri.fromFile(new File(mAlbumPicturePath)), this);
                    judgeBitmap(bitmap);
                } else if (resultCode == RESULT_CANCELED) {
                    showToast("取消头像设置");
                }
                break;
            case PhotoTools.SELECET_A_PICTURE_AFTER_KIKAT:
                if (resultCode == RESULT_OK && null != data) {
                    mAlbumPicturePath = PhotoTools.getPath(getApplicationContext(),
                            data.getData());
                    bitmap = PhotoTools.decodeUriAsBitmap(
                            Uri.fromFile(new File(mAlbumPicturePath)), this);
                    judgeBitmap(bitmap);
                } else if (resultCode == RESULT_CANCELED) {
                    showToast("取消头像设置");
                }
                break;
            case TAKE_A_PICTURE:
                if (resultCode == RESULT_OK) {
                    bitmap = PhotoTools.decodeUriAsBitmap(Uri.fromFile(new File(
                            PhotoTools.IMGPATH, PhotoTools.IMAGE_FILE_NAME)), this);
                    judgeBitmap(bitmap);
                }
            default:
                break;
        }

    }

    /**
     * 关闭软键盘
     *
     * @param mcontext
     * @param et
     */
    public void closeBoard(Context mcontext, ClearEditText et) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 手机返回按钮监听
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isOperation) {
                showBackDialog();
            } else {
                UserInfoEditActivity.this.finish();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * @return void 返回类型
     * @throws
     * @Title: showDialog
     * @Description: TODO(dialog弹出和显示的样式)
     */
    @SuppressWarnings("deprecation")
    private void showImgDialog() {
        View view = getLayoutInflater().inflate(R.layout.person_seting_dialog,
                null);
        ViewUtils.inject(this, view);
        dialog1 = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog1.setContentView(view, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        Window window = dialog1.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog1.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.show();
    }

    private void judgeBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            showToast("取消头像设置");
        } else {
            saveBitmap(bitmap);
        }
    }

    @SuppressWarnings("static-access")
    private void saveBitmap(Bitmap bitmap) {
        new DateFormat();
        bitmap = new BitmapUtils().zoomBitmap(bitmap);
        FileOutputStream fileOutputStream = null;
        FileOutputStream fOutputStream = null;
        File file = new File(PhotoTools.IMGPATH);

        file.mkdirs();// 创建文件夹
        String fileName = PhotoTools.IMGPATH + PhotoTools.IMAGE_FILE_NAME;
        File file1 = new File(PhotoTools.ACCOUNT_DIR);// 如果缓存目录存在的话，删除缓存目录
        file1.setReadable(false);
        try {
            fileOutputStream = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);// 把数据写入文件
            Bitmap bitmap1 = PhotoTools.getimage(fileName, this);
            fOutputStream = new FileOutputStream(fileName);
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fOutputStream);

            setImageView(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (!StringUtil.isEmpty(fileOutputStream)) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } else {
                    showToast("头像设置失败，请重试...");
                }
                if (!StringUtil.isEmpty(fOutputStream)) {
                    fOutputStream.flush();
                    fOutputStream.close();
                } else {
                    showToast("头像设置失败，请重试...");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传头像
     *
     * @param pathString //TODO 修改头像包
     * @throws Exception
     */
    private void setImageView(String pathString) {
//        String url = NetInterface.HEADER_ALL + NetInterface.EDIT_USER_INFO + NetInterface.SUFFIX;
//        File file = new File(pathString);
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("photo", file);
//        params.addBodyParameter("userId", loginResponse.getDesc());
//        params.addBodyParameter("token", loginResponse.getToken());
//        httpBiz = new HttpBiz(this);
//        httpBiz.uploadMethod(UPLOAD_IMG_TYPE, params,url, this,
//                this);
        ProgrosDialog.openDialog(baseContext);

        // TODO 需要在子线程中run
        new UploadImage().execute(pathString);

    }

    private class UploadImage extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String string) {
            ProgrosDialog.closeProgrosDialog();
            if (null != string) {
                UpLoadPhotoResponse photoResponse = (UpLoadPhotoResponse) GsonUtil.getInstance().convertJsonStringToObject(string, UpLoadPhotoResponse.class);
                if (!photoResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    showToast(photoResponse.getDesc());
                    return;
                }
                showToast("上传成功");

                XUtilsImageLoader.getxUtilsImageLoader(UserInfoEditActivity.this,
                        R.drawable.info_touxiang_moren, img_userEdit_userIcon,
                        photoResponse.getMsg().getPhoto());

//                XUtilsImageLoader.getxUtilsImageLoader(UserInfoEditActivity.this,
//                        R.drawable.info_touxiang_moren, MyAccountActivity.iv_myAccountUserIcon,
//                        photoResponse.getMsg().getPhoto());
                loginResponse.getMsg().setPhoto(photoResponse.getMsg().getPhoto());
                loginResponse.setToken(photoResponse.getToken());
//                LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
                Intent mIntent = new Intent();
                Constant.CURRENT_REFRESH = Constant.USER_CENTER_REFRESH;
                mIntent.setAction(Constant.REFRESH_FLAG);
                sendBroadcast(mIntent);
            } else {
                showToast(R.string.server_link_fault);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            File file = new File(params[0]);
            try {
                String url = NetInterface.HEADER_ALL + NetInterface.USER_PHOTO + NetInterface.SUFFIX;
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url);
                LogHelper.d(url);
                MultipartEntity httpEntity = new MultipartEntity();
                httpEntity.addPart("photo", new FileBody(file));
                httpEntity.addPart("userId", new StringBody(loginResponse.getDesc()));
                httpEntity.addPart("token", new StringBody(loginResponse.getToken()));
                post.setEntity(httpEntity);
                HttpResponse response = client.execute(post);
                HttpEntity responseEntity = response.getEntity();
                if (response.getStatusLine().getStatusCode() == 200) {
                    // success
                    String responseString = EntityUtils.toString(responseEntity);
                    LogHelper.d(responseString);
                    return responseString;
                } else {
                    // fail
                    LogHelper.d(responseEntity.getContentType() + "----------upload fail" + response.getStatusLine().getStatusCode() + "--" + EntityUtils.toString(responseEntity));
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private void parseImgJSON(String result) {
        if (!StringUtil.isEmpty(result)) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String resultStr = jsonObject.optString("title");
                if (StringUtil.isEquals(resultStr, API.returnSuccess, true)) {
                    Constant.CURRENT_REFRESH = Constant.LOGIN_REFRESH;
                    String path = jsonObject.optJSONObject("data").optString(
                            "file");
//                    loginResponse.setPhoto(path);
                    LoginMessageUtils.saveProduct(loginResponse,
                            UserInfoEditActivity.this);
//                    XUtilsImageLoader.getxUtilsImageLoader(
//                            UserInfoEditActivity.this,
//                            R.drawable.info_touxiang_moren,
//                            img_userEdit_userIcon, API.DOWN_IMAGE_URL
//                                    + loginResponse.getPhoto());
//                    XUtilsImageLoader.getxUtilsImageLoader(
//                            UserInfoEditActivity.this,
//                            R.drawable.info_touxiang_moren, origionalImg,
//                            API.DOWN_IMAGE_URL + loginResponse.getPhoto());
                    // if (!Constant.EDIT_FLAG) {
                    Constant.CURRENT_REFRESH = Constant.LOGIN_REFRESH;
                    // }
                    Intent mIntent = new Intent();
                    mIntent.setAction(Constant.REFRESH_FLAG);
                    sendBroadcast(mIntent);
                    showToast("头像设置成功");

                } else if (StringUtil.isEquals(resultStr, "FAIL", true)) {
                    showToast(jsonObject.optJSONObject("data").optString("msg"));
                } else if (StringUtil.isEquals(resultStr, "RELOGIN", true)) {
                    ReLoginDialog.getInstance(this).showDialog(
                            jsonObject.optString("message"));
                } else if (StringUtil.isEquals(resultStr, "DEFAULT", true)) {
                    showToast(jsonObject.optJSONObject("data").optString("msg"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            Constant.EDIT_FLAG = false;
            if (!StringUtil.isEquals(intent.getAction(), Constant.REFRESH_FLAG,
                    true)) {
                return;
            }
            if (StringUtil.isEquals(Constant.CURRENT_REFRESH,
                    Constant.USER_NICK_EDIT_REFRESH, true)) {
                init();
            } else if (StringUtil.isEquals(Constant.CURRENT_REFRESH,
                    Constant.SPECIAL_SIGN_REFRESH, true)) {
                init();
            }
        }
    }
}
