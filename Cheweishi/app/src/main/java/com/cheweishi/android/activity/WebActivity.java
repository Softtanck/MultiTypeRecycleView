package com.cheweishi.android.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.PessanyAdapter;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.ReadNewsResponse;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.WebViewView;

import java.util.HashMap;
import java.util.Map;

/**
 * 网页
 *
 * @author zhangq
 */
public class WebActivity extends BaseActivity implements OnClickListener, TextWatcher {
    private WebView mWebView;
    private Button tvLeft;
    private TextView tvTitle;
    private ProgressBar loading;
    private String newsId;// 新闻id
    private String url;//web url
    private String title;//标题
    private View web_bottom;//顶部评论
    private EditText et_web;//评论输入
    private Button bt_web;//评论
    private TextView tv_web_comment;//评论个数
    private TextView tv_web_like;// 喜欢个数
    private int like = -1;//点赞

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        tvTitle = (TextView) findViewById(R.id.web_title);
        loading = (ProgressBar) findViewById(R.id.pb_web_loading);
        tvLeft = (Button) findViewById(R.id.left_action);


        tvLeft.setOnClickListener(this);
        tvLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                WebActivity.this.finish();
            }
        });

        newsId = getIntent().getStringExtra("id");
        url = getIntent().getExtras().getString("url");
        title = getIntent().getExtras().getString("title");
        // 是否设置标题
        if (!StringUtil.isBlank(title)) {
            tvTitle.setText(title);
        }


        if (StringUtil.isEmpty(newsId)) {
            if (StringUtil.isBlank(url)) { // &&!url.contains("http")
                finish();
                return;
            } else {
                openWebView(url);
            }
        } else { // 为新闻
            web_bottom = findViewById(R.id.web_bottom);
            et_web = (EditText) findViewById(R.id.et_web);
            bt_web = (Button) findViewById(R.id.bt_web);
            tv_web_comment = (TextView) findViewById(R.id.tv_web_comment);
            tv_web_like = (TextView) findViewById(R.id.tv_web_like);
            et_web.addTextChangedListener(this);
            tv_web_like.setOnClickListener(this);
            bt_web.setOnClickListener(this);
            sendPacketForUrl();
        }
    }

    private void sendPacketForUrl() {
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_NEWS + NetInterface.READ_NEWS + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("newsId", newsId);
        param.put(Constant.PARAMETER_TAG, NetInterface.READ_NEWS);
        netWorkHelper.PostJson(url, param, this);
    }

    @Override
    public void receive(String TAG, String data) {
        ProgrosDialog.closeProgrosDialog();
        switch (TAG) {
            case NetInterface.READ_NEWS: // 阅读新闻
                ReadNewsResponse newsResponse = (ReadNewsResponse) GsonUtil.getInstance().convertJsonStringToObject(data, ReadNewsResponse.class);
                if (!newsResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    showToast(newsResponse.getDesc());
                    return;
                }

                title = newsResponse.getMsg().getTitle();
                title = StringUtil.isEmpty(title) ? getResources().getString(R.string.see_details) : title;
                tvTitle.setText(title);
                url = newsResponse.getMsg().getContentUrl();
                if (StringUtil.isEmpty(url))
                    finish();
                web_bottom.setVisibility(View.VISIBLE);
//                tv_web_comment.setText(String.valueOf(Integer.valueOf(newsResponse.getMsg().getCommentCounts()) + 1));
                tv_web_comment.setText(newsResponse.getMsg().getCommentCounts());
                tv_web_like.setText(newsResponse.getMsg().getLikeCounts());
                openWebView(url);
                loginResponse.setToken(newsResponse.getToken());
                break;
            case NetInterface.DO_COMMENT: // 评论
                BaseResponse response = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
                if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    showToast(response.getDesc());
                    return;
                }
                et_web.setText("");
//                sendPacketForUrl();
                // TODO  直接重新加载.
                mWebView.reload();
                loginResponse.setToken(response.getToken());
                break;
            case NetInterface.DO_LIKE: // 点赞
                BaseResponse likeResponse = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
                if (!likeResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    showToast(likeResponse.getDesc());
                    return;
                }
                // UPDATE ui
                String temp = tv_web_like.getText().toString();
                temp = String.valueOf((Integer.valueOf(temp) + like));
                tv_web_like.setText(temp);
                loginResponse.setToken(likeResponse.getToken());
                break;
        }
    }

    private void openWebView(String url) {
        mWebView = (WebView) findViewById(R.id.webview);
        WebViewView webViewUtil = new WebViewView();
        webViewUtil.setWebView(mWebView);
        webViewUtil.setContext(WebActivity.this);
        webViewUtil.setJS(true);
        webViewUtil.setSelfAdaption();
        webViewUtil.setNoCache();
        webViewUtil.alarm404();
//        webViewUtil.showShowProgress(this);
        webViewUtil.setProgress(loading);
        webViewUtil.openUrl(url);
        loading.setProgress(30);
    }


    @Override
    protected void onResume() {
        try {
            mWebView.onResume();
        } catch (Exception e) {
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        try {
            mWebView.destroy();
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        try {
            mWebView.onPause();
        } catch (Exception e) {
        }
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_web: // 评论
                String content = et_web.getText().toString();
                if (StringUtil.isEmpty(content.trim())) {
                    showToast(R.string.comment_content_empty);
                    return;
                }
                doComment(content);
                break;
            case R.id.tv_web_like://点赞
                doLike();
                break;
            case R.id.left_action: // 左上角
                finish();
                break;
        }

    }

    private void doComment(String content) {
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_NEWS + NetInterface.DO_COMMENT + NetInterface.SUFFIX;
        Map<String, Object> para = new HashMap<>();
        para.put("userId", loginResponse.getDesc());
        para.put("token", loginResponse.getToken());
        para.put("newsId", newsId);
        para.put("comment", content);
        para.put(Constant.PARAMETER_TAG, NetInterface.DO_COMMENT);
        netWorkHelper.PostJson(url, para, this);
    }

    private void doLike() {
        like = -1 == like ? 1 : -1;
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_NEWS + NetInterface.DO_LIKE + NetInterface.SUFFIX;
        Map<String, Object> para = new HashMap<>();
        para.put("userId", loginResponse.getDesc());
        para.put("token", loginResponse.getToken());
        para.put("newsId", newsId);
        para.put("doLikeOpr", like);
        para.put(Constant.PARAMETER_TAG, NetInterface.DO_LIKE);
        netWorkHelper.PostJson(url, para, this);
    }


    // 设置回退
    // 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && null != mWebView && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        count = s.length();
        if (0 < count)
            bt_web.setVisibility(View.VISIBLE);
        else
            bt_web.setVisibility(View.GONE);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
