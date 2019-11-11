package com.cheweishi.android.utils.weixinpay;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.cheweishi.android.R;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.utils.weixinpay.simcpux.Constants;
import com.cheweishi.android.utils.weixinpay.simcpux.MD5;
import com.cheweishi.android.utils.weixinpay.simcpux.Util;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 微信支付操作
 *
 * @author mingdasen
 */
public class WeiXinPay {
    private static WeiXinPay instance;
    private static Context mContext;

    private static PayReq req;
    private static IWXAPI msgApi;
    private Map<String, String> resultunifiedorder;
    private boolean sIsWXAppInstalledAndSupported;
    private static StringBuffer sb;
    private String subject = "";
    private String body;
    private int price;
    private String notify_url;
    private String out_trade_no;
    private String sign;
    private String prepay_id;
    private String nonce_str;

    public static WeiXinPay getinstance(Context context) {
        if (instance == null) {
            mContext = context;
            instance = new WeiXinPay();
            msgApi = WXAPIFactory.createWXAPI(mContext, null);
            req = new PayReq();
            sb = new StringBuffer();
            msgApi.registerApp(Constant.APP_ID);
        }
        return instance;
    }

    /**
     * 微信支付操作
     */
    public void pay(String prepay_id, String nonce_str) {
        this.prepay_id = prepay_id;
        this.nonce_str = nonce_str;
//		this.sign = sign;
        genPayReq();
//		this.subject = subject;
//		this.body = body;
//		this.price = (int)(Float.parseFloat(price)*100);
//		this.notify_url = "http://115.28.161.11:8080/XAI/app/pgy/notify_url.do";
//		this.out_trade_no = out_trade_no;
//		Config.out_trade_no = out_trade_no;
//		Config.price = price;
//		Config.orderSign = MD5Tool.md5(StringUtils.getString(App.getUserPar().get("sign")+out_trade_no));

        // 第一步 生成prepay_id
//		GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
//		getPrepayId.execute();
//		String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
    }

    /**
     * 发送支付请求
     */
    private void sendPayReq() {
        msgApi.registerApp(Constant.APP_ID);
        msgApi.sendReq(req);
//		((Activity)mContext).finish();
    }

    /**
     * 设置请求参数
     */
    private void genPayReq() {

        req.appId = Constant.APP_ID;//公众账号ID
        req.partnerId = Constant.MCH_ID;//商户号
        req.prepayId = prepay_id;//resultunifiedorder.get("prepay_id");//预支付交易会话ID
        req.packageValue = "Sign=WXPay";//扩展字段
        req.nonceStr = nonce_str;//genNonceStr();//随机字符串
        req.timeStamp = String.valueOf(genTimeStamp());//时间戳


        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        sb.append("sign\n" + req.sign + "\n\n");

//		show.setText(sb.toString());
        Log.i("Tanck", sb.toString());

        Log.e("Tanck", "==1=" + signParams.toString());
        //第三步 发送支付请求
        sendPayReq();

    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constant.API_KEY);

        WeiXinPay.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = null;
        try {
            appSign = MD5.getMessageDigest(sb.toString().getBytes("utf-8")).toUpperCase();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.e("Tanck", "==2=" + appSign);
        return appSign;
    }

    /**
     * 生成prepay_id
     *
     * @author mingdasen
     */
    private class GetPrepayIdTask extends
            AsyncTask<Void, Void, Map<String, String>> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(mContext,
                    mContext.getString(R.string.app_tip),
                    mContext.getString(R.string.getting_prepayid));
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            if (dialog != null) {
                dialog.dismiss();
            }
            sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
            // show.setText(sb.toString());
            Log.i("result", "==result=sb=" + sb.toString());
            resultunifiedorder = result;
            // 第二步 支付请求参数
            genPayReq();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {

            String url = String
                    .format("https://api.mch.weixin.qq.com/pay/unifiedorder");//统一下单接口
            String entity = genProductArgs();

            Log.e("Tanck", "==3=" + entity);

            byte[] buf = Util.httpPost(url, entity);
            Log.e("Tanck", "==9=" + buf);
            String content = new String(buf);
            Log.e("Tanck", "==4=" + content);
            Map<String, String> xml = decodeXml(content);

            return xml;
        }
    }

    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            // 实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("Tanck", "==10=" + e.toString());
        }
        return null;

    }

    //
    private String genProductArgs() {
        StringBuffer xml = new StringBuffer();

        try {
            String nonceStr = genNonceStr();

            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams
                    .add(new BasicNameValuePair("appid", Constants.APP_ID));//公众账号ID
            packageParams.add(new BasicNameValuePair("body", subject));//商品描述
            packageParams
                    .add(new BasicNameValuePair("mch_id", Constants.MCH_ID));//商户号
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));//随机字符串
            packageParams.add(new BasicNameValuePair("notify_url", notify_url));//通知地址（接收微信支付异步通知回调地址）
            packageParams.add(new BasicNameValuePair("out_trade_no", out_trade_no));//商户订单号
            packageParams.add(new BasicNameValuePair("spbill_create_ip",
                    "127.0.0.1"));//终端IP（APP和网页支付提交用户端ip）
            packageParams.add(new BasicNameValuePair("total_fee", price + ""));//总金额，交易金额默认为人民币交易，接口中参数支付金额单位为【分】，参数值不能带小数。对账单中的交易金额单位为【元】。
            //外币交易的支付金额精确到币种的最小单位，参数值不能带小数点。
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));//交易类型

            sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));//签名

            String xmlstring = toXml(packageParams);
            //改变拼接之后xml字符串格式
            return new String(xmlstring.toString().getBytes(), "ISO8859-1");

        } catch (Exception e) {
            Log.e("result", "genProductArgs fail, ex = " + e.getMessage());
            return null;
        }

    }

    /**
     * 生成签名
     */

    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);

        String packageSign = null;
        try {
            packageSign = MD5.getMessageDigest(sb.toString().getBytes("utf-8"))
                    .toUpperCase();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.e("Tanck", "==5=" + packageSign);
        return packageSign;
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");

            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        Log.e("Tanck", "==6=" + sb.toString());
        return sb.toString();
    }

    /**
     * 生成随机字符串
     *
     * @return
     */
    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
                .getBytes());
    }

    /**
     * 获取时间戳
     *
     * @return
     */
    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取商户订单号
     *
     * @return
     */
    private String genOutTradNo() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
                .getBytes());
    }

    public boolean isWXAppInstalledAndSupported() {
        // LogOutput.d(TAG, "isWXAppInstalledAndSupported");
        sIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled() && msgApi.isWXAppSupportAPI();
        if (!sIsWXAppInstalledAndSupported) {
            Toast.makeText(mContext, "微信客户端未安装，无法进行支付", Toast.LENGTH_LONG).show();
        }
        return sIsWXAppInstalledAndSupported;
    }

    public void onDestory() {
        msgApi.unregisterApp();
        mContext = null;
        msgApi = null;
        req = null;
        sb = null;
        instance = null;
    }
}
