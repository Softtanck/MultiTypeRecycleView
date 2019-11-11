package com.cheweishi.android.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cheweishi.android.R;
import com.cheweishi.android.activity.BaseActivity;
import com.cheweishi.android.config.Constant;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信支付返回数据处理
 *
 * @author mingdasen
 */
//com.cheweishi.android.wxapi.WXPayEntryActivity
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
//	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weixinpay_result_activity);

        api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			Intent intent;
            if (resp.errCode == 0) {
                Intent mIntent = new Intent();
                Constant.CURRENT_REFRESH = Constant.WEIXIN_PAY_REFRESH;
                mIntent.setAction(Constant.REFRESH_FLAG);
                sendBroadcast(mIntent);
//				mIntent = new Intent(WXPayEntryActivity.this, RechargeActivity.class);
                showToast("支付成功");
                finish();
//				startActivity(mIntent);
            } else {
                Intent mIntent = new Intent();
                Constant.CURRENT_REFRESH = Constant.WEIXIN_PAY_FAIL_REFRESH;
                mIntent.setAction(Constant.REFRESH_FLAG);
                sendBroadcast(mIntent);
                showToast("支付失败");
                WXPayEntryActivity.this.finish();
            }
        }
    }

//	void submitLogin() {
//		if (isLogined()) {
//			RequestParams rp = new RequestParams();
//			rp.addBodyParameter("uid", loginMessage.getUid());
//			rp.addBodyParameter("key", loginMessage.getKey());
//			httpBiz.httPostData(1000008, API.LOGIN_MESSAGE_RELOGIN_URL, rp,
//					this);
//		}
//	}
//	@Override
//	public void receive(int type, String data) {
//		// TODO Auto-generated method stub
////		super.receive(type, data);
//		switch (type) {
//		case 1000008:
//			parseLogin(data);
//			break;
//		}
//	}
//
//	protected void save(String jsonObject) {
//		Gson gson = new Gson();
//		java.lang.reflect.Type type = new TypeToken<LoginMessage>() {
//		}.getType();
//		loginMessage = gson.fromJson(jsonObject, type);
//		LoginMessageUtils.saveProduct(loginMessage, this);
////		initViews();
//		Intent mIntent = new Intent();
//		Constant.CURRENT_REFRESH = Constant.LOGIN_REFRESH;
//		mIntent.setAction(Constant.REFRESH_FLAG);
//		sendBroadcast(mIntent);
//		mIntent = new Intent(WXPayEntryActivity.this, RechargeActivity.class);
//		startActivity(mIntent);
//		WXPayEntryActivity.this.finish();
//	}
//
//	private void parseLogin(String data) {
//		if (StringUtil.isEmpty(data)) {
//			return;
//		}
//		try {
//			JSONObject jsonObject = new JSONObject(data);
//			String resultStr = jsonObject.optString("operationState");
//			String resultMsg = jsonObject.optJSONObject("data")
//					.optString("msg");
//			if (StringUtil.isEquals(resultStr, API.returnSuccess, true)) {
//				save(jsonObject.optString("data"));
//			} else {
//				if (StringUtil.isEquals(resultStr, API.returnRelogin, true)) {
//					DialogTool.getInstance(this).showConflictDialog();
//				} else {
//					showToast(resultMsg);
//				}
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//
//	}

//	@Override
//	public void refreshLoginMessage() {
//		super.refreshLoginMessage();
//		
//	}
}
