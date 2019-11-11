package com.cheweishi.android.activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.cheweishi.android.R;
import com.cheweishi.android.biz.HttpBiz;
import com.cheweishi.android.config.API;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.tools.LoginMessageUtils;
import com.cheweishi.android.tools.ReLoginDialog;
import com.cheweishi.android.tools.RegularExpressionTools;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.StringUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangQ
 * @version 1.0
 * @date 创建时间：2015-12-2 下午5:38:48
 * @Description:
 */
public class IdeaReturnActivity extends BaseActivity implements OnClickListener {
    @ViewInject(R.id.idea_ed)
    private EditText idea_ed;
    @ViewInject(R.id.idea_btn)
    private Button idea_btn;
    private HttpBiz httpBiz;
    private String submit = "";
    @ViewInject(R.id.left_action)
    private Button left_action;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.tv_idea_count)
    private TextView tv_idea_count;
    private boolean resetText;
    private int cursorPos;
    // 输入表情前EditText中的文本
    private String tmp;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.idea_return);
        ViewUtils.inject(this);
        init();
        idea_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                tv_idea_count.setText(arg0.length() + "/100");
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
                                idea_ed.setText(tmp);
                                tv_idea_count.setText(tmp.length() + "/100");
                                idea_ed.invalidate();
                                idea_ed.setSelection(idea_ed.getText()
                                        .toString().length());
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
                    cursorPos = idea_ed.getSelectionEnd();
                    tmp = arg0.toString();// 这里用s.toString()而不直接用s是因为如果用s，那么，tmp和s在内存中指向的是同一个地址，s改变了，tmp也就改变了，那么表情过滤就失败了
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void init() {
        title.setText(R.string.feed_back);
        left_action.setText(R.string.back);
        // goToLoginFirst();
        if (!isLogined()) {
            Intent intent = new Intent();
            intent.setClass(IdeaReturnActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.score_business_query_enter,
                    R.anim.score_business_query_exit);
        }
    }

    @OnClick({R.id.idea_btn, R.id.left_action})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.idea_btn:
                submit = idea_ed.getText().toString();
                if (StringUtil.isEmpty(submit.trim())) {
                    showToast("输入不能为空！");
                    return;

                } else {
                    ProgrosDialog.openDialog(this);
                    String url = NetInterface.BASE_URL + NetInterface.TEMP_FEEDBACK + NetInterface.ADD + NetInterface.SUFFIX;
                    Map<String, Object> param = new HashMap<>();
                    param.put("userId", loginResponse.getDesc());
                    param.put("token", loginResponse.getToken());
                    param.put("content", submit);
                    netWorkHelper.PostJson(url, param, this);
                }
                break;
            case R.id.left_action:
                finish();
                break;

            default:
                break;
        }

    }

    @Override
    public void receive(String data) {
        ProgrosDialog.closeProgrosDialog();
        BaseResponse baseResponse = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
        if (!baseResponse.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            showToast(baseResponse.getDesc());
            return;
        }
        showToast("提交成功");
        loginResponse.setToken(baseResponse.getToken());
//        LoginMessageUtils.saveloginmsg(baseContext, loginResponse);
        IdeaReturnActivity.this.finish();
    }


}
