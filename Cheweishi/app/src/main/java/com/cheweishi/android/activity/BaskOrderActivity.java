package com.cheweishi.android.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.R;
import com.cheweishi.android.adapter.BaskOrderAdapter;
import com.cheweishi.android.biz.HttpBiz;
import com.cheweishi.android.config.API;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.tools.DialogTool;
import com.cheweishi.android.tools.LoginMessageUtils;
import com.cheweishi.android.tools.PhotoTools;
import com.cheweishi.android.tools.RegularExpressionTools;
import com.cheweishi.android.utils.BitmapUtils;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.StringUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BaskOrderActivity extends BaseActivity implements OnClickListener {
    @ViewInject(R.id.left_action)
    private Button left_action;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.rtb_skill)
    private RatingBar rtb_skill;
    @ViewInject(R.id.rtb_manner)
    private RatingBar rtb_manner;
    private Dialog dialog1;
    @ViewInject(R.id.rtb_environment)
    private RatingBar rtb_environment;
    @ViewInject(R.id.ed_orderName)
    private EditText ed_orderName;
    @ViewInject(R.id.rl_orderphotograph)
    private RelativeLayout rl_orderphotograph;
    private ArrayList<Bitmap> imageItem;
    public static final int TAKE_A_PICTURE = 10;
    public static final int ALBUM_REQUEST_CODE = 2;
    private Bitmap bmp;
    @ViewInject(R.id.gridView_add)
    private GridView gridView;
    private Intent intent;
    private BaskOrderAdapter adaoter;
    private String pathImage;
    private String mAlbumPicturePath = null;
    private final int UPLOAD_IMG_TYPE = 10003;
    @ViewInject(R.id.img_store_comment)
    private ImageView img_store_comment;
    @ViewInject(R.id.bt_evaluate)
    private Button bt_evaluate;
    @ViewInject(R.id.tv_evaluate_tenant_name)
    private TextView tv_evaluate_tenant_name;
    @ViewInject(R.id.tv_evaluate_tenant_service_name)
    private TextView tv_evaluate_tenant_service_name;
    @ViewInject(R.id.tv_evaluate_price)
    private TextView tv_evaluate_price;
    private boolean resetText;
    private int cursorPos;
    // 输入表情前EditText中的文本
    private String tmp;
    private float mCurrentRate;
    private String price;
    private String tenantname;
    private String servicename;
    private String tenantPhoto;
    private String tenantID;
    private String recordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_evaluate);
        ViewUtils.inject(this);

        title.setText(R.string.bask_order);
        left_action.setText(R.string.back);
        initi();
        ed_orderName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                CharSequence input = null;
                if (!resetText) {
                    if (arg3 >= 2) {
                        // 提取输入的长度大于2的文本

                        try {
                            input = arg0.subSequence(cursorPos, cursorPos
                                    + arg3);
                        } catch (Exception e) {
                            try {
                                input = arg0.subSequence(cursorPos - 1,
                                        cursorPos + arg3 - 1);
                            } catch (Exception e1) {

                            }
                        }
                        // 正则匹配是否是表情符号
                        if (input != null
                                && !RegularExpressionTools
                                .isFacingExpression(input.toString())) {
                            if (RegularExpressionTools.isChinese(input
                                    .toString())) {
                            } else if (RegularExpressionTools.isAllChar(input
                                    .toString())) {

                            } else {
                                resetText = true;
                                // 是表情符号就将文本还原为输入表情符号之前的内容
                                ed_orderName.setText(tmp);
                                ed_orderName.invalidate();
                                ed_orderName.setSelection(ed_orderName
                                        .getText().toString().length());
                                showToast(R.string.expression_notSupport);
                            }
                        }
                    }
                } else {
                    resetText = false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                if (!resetText) {
                    cursorPos = ed_orderName.getSelectionEnd();
                    tmp = arg0.toString();// 这里用s.toString()而不直接用s是因为如果用s，那么，tmp和s在内存中指向的是同一个地址，s改变了，tmp也就改变了，那么表情过滤就失败了
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });
        imageItem = new ArrayList<Bitmap>();
        adaoter = new BaskOrderAdapter(imageItem, this);
        gridView.setAdapter(adaoter);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 打开图片
        Bitmap bitmap;
        switch (requestCode) {
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
                Log.i("zou", "TAKE_A_PICTURE-resultCode:" + resultCode);
                if (resultCode == RESULT_OK) {
                    bitmap = PhotoTools.decodeUriAsBitmap(Uri.fromFile(new File(
                            PhotoTools.IMGPATH, PhotoTools.IMAGE_FILE_NAME)), this);
                    judgeBitmap(bitmap);
                }
                break;
            default:
                break;
        }

        Log.i("result", "===imageItem===" + imageItem.size());
        adaoter = new BaskOrderAdapter(imageItem, this);
        gridView.setAdapter(adaoter);
    }

    protected void onResume() {
        super.onResume();

    }

    private void initi() {
        rtb_skill = (RatingBar) findViewById(R.id.rtb_skill);
        rtb_manner = (RatingBar) findViewById(R.id.rtb_manner);
        rtb_environment = (RatingBar) findViewById(R.id.rtb_environment);

        rtb_skill
                .setOnRatingBarChangeListener(new RatingBarChangeListenerImpl());
        rtb_manner
                .setOnRatingBarChangeListener(new RatingBarChangeListenerImpl());
        rtb_environment
                .setOnRatingBarChangeListener(new RatingBarChangeListenerImpl());
//        int max1 = rtb_skill.getMax();
//        int max2 = rtb_manner.getMax();
//        int max3 = rtb_environment.getMax();
//
//        float currentRating1 = rtb_skill.getRating();
//        float currentRating2 = rtb_manner.getRating();
//        float currentRating3 = rtb_environment.getRating();


//        intent.putExtra("price", mData.get(po).getPrice());
//        intent.putExtra("tenantname", mData.get(po).getTenantName());
//        intent.putExtra("servicename", mData.get(po).getCarService().getServiceName());
//        intent.putExtra("tenantID", mData.get(po).getTenantID());
//        intent.putExtra("tenantPhoto", mData.get(po).getTenantPhoto());
        price = getIntent().getStringExtra("price");
        tenantname = getIntent().getStringExtra("tenantname");
        servicename = getIntent().getStringExtra("servicename");
        tenantID = getIntent().getStringExtra("tenantID");
        tenantPhoto = getIntent().getStringExtra("tenantPhoto");
        recordId = getIntent().getStringExtra("recordId");

        // TODO 更新UI
        tv_evaluate_price.setText(price);
        tv_evaluate_tenant_name.setText(tenantname);
        tv_evaluate_tenant_service_name.setText(servicename);
        XUtilsImageLoader.getxUtilsImageLoader(this,
                R.drawable.tianjiacar_img2x, img_store_comment,
                tenantPhoto);
    }

    @OnClick({R.id.left_action, R.id.rl_orderphotograph, R.id.xiangji,
            R.id.xiangce, R.id.quxiao, R.id.bt_evaluate})
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.left_action:
                finish();
                break;
            case R.id.rl_orderphotograph:
                if (imageItem.size() < 5) {
                    dialog();
                } else {
                    showToast("添加已满");
                }

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
                    PhotoTools.selectImageUriAfterKikat(BaskOrderActivity.this);
                } else {
                    PhotoTools.cropImageUri(BaskOrderActivity.this);
                }
                break;
            case R.id.quxiao:
                dialog1.dismiss();
                break;
            case R.id.bt_evaluate:
                sendEvaluate();
                break;
            default:
                break;
        }

    }

    private void sendEvaluate() {
        if (null == tenantID && "".equals(tenantID)) {
            showToast("评论初始化失败");
            finish();
            return;
        }

        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_EVALUATE + NetInterface.RATE + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("tenantId", tenantID);
        param.put("score", mCurrentRate);
        param.put("recordId", recordId);
        netWorkHelper.PostJson(url, param, this);

    }


    @Override
    public void receive(String data) {
        ProgrosDialog.closeProgrosDialog();
        BaseResponse response = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
        if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            showToast(response.getDesc());
            return;
        }

        showToast("提交成功");
        loginResponse.setToken(response.getToken());
        LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
        finish();
    }

    private void dialog() {

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

    private class RatingBarChangeListenerImpl implements
            OnRatingBarChangeListener {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating,
                                    boolean fromUser) {
            mCurrentRate = rating;
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
            imageItem.add(bitmap1);
            adaoter.setData(imageItem);
            // setImageView(fileName);
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }



}
