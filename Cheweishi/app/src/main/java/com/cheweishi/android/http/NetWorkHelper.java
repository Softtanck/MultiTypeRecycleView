package com.cheweishi.android.http;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.navisdk.util.common.NetworkUtils;
import com.cheweishi.android.R;
import com.cheweishi.android.activity.BaseActivity;
import com.cheweishi.android.activity.LoginActivity;
import com.cheweishi.android.biz.JSONCallback;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.utils.ActivityControl;
import com.cheweishi.android.utils.LogHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tangce on 3/16/2016.
 */
public class NetWorkHelper {

    private static final int TIMEOUT_POST = 10000;
    private static NetWorkHelper instance;

    private Context context;

    private String RequestTag;
    private RequestQueue requestQueue;

    private NetWorkHelper(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        requestQueue = Volley.newRequestQueue(context);
    }

    public static NetWorkHelper getInstance(Context context) {
        if (null == instance) {
            synchronized (NetWorkHelper.class) {
                if (null == instance)
                    instance = new NetWorkHelper(context.getApplicationContext());
            }
        }

        return instance;
    }

    /**
     * Post请求
     *
     * @param url
     * @param params
     * @param jsonCallback
     */

    public void PostJson(final String url, final Map<String, Object> params, final JSONCallback jsonCallback) {

        if (!NetworkUtils.isNetworkAvailable(context) && null != jsonCallback) {
            jsonCallback.error("当前网络不可用");
            Toast.makeText(context.getApplicationContext(),
                    "当前网络不可用", Toast.LENGTH_SHORT).show();
            ProgrosDialog.closeProgrosDialog();
            return;
        }

        RequestTag = null;
        if (null != params.get(Constant.PARAMETER_TAG)) {
            RequestTag = params.get(Constant.PARAMETER_TAG).toString();
            params.remove(Constant.PARAMETER_TAG);
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogHelper.d("request url:" + url + "\n" + params + "\n" + jsonObject.toString());
                // TODO 超时情况
                if (NetInterface.RESPONSE_TOKEN.equals(jsonObject.optString("code")) && !url.contains("login") && !url.contains("endUser/logout")) {
                    //&& !url.contains("tenantInfo/list")
                    Toast.makeText(context.getApplicationContext(),
                            "登录超时,正在重新登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.putExtra(Constant.AUTO_LOGIN, true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent); // || !url.contains("advertisement/getAdvImage") || !url.contains("advertisement/getAdvImage")
                    if (!url.contains("tenantInfo/list") && !url.contains("advertisement/getAdvImage")) {
                        try { // 可能出现的异常
                            ((BaseActivity) context).finish();
                            ((BaseActivity) context).overridePendingTransition(R.anim.score_business_query_enter,
                                    R.anim.score_business_query_exit);
                        } catch (ClassCastException e) {
                            ActivityControl.finishActivity(ActivityControl.getCount() - 1);
                        }
                    }
                    return;
                }


                if (null == jsonCallback)
                    return;
                if (null != RequestTag) {
                    jsonCallback.receive(RequestTag, jsonObject.toString());
//                    listener.onResponse(jsonObject.toString(), RequestTag);
                } else {
                    jsonCallback.receive(jsonObject.toString());
//                    listener.onResponse(jsonObject.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (null != error.networkResponse) {
                    byte[] htmlBodyBytes = error.networkResponse.data;
                    LogHelper.d("request url:" + url + "\n" + params + "\n" + new String(htmlBodyBytes));
                }
                if (null == jsonCallback)
                    return;
                jsonCallback.error(error.toString());
//                listener.onErrorResponse(error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Accept", "application/json");

                headers.put("Content-Type", "application/json; charset=UTF-8");

                return headers;
            }
        };
        request.setRetryPolicy(getRetryPolicy());
        requestQueue.add(request);
        requestQueue.start();
    }


    /**
     * get 请求
     *
     * @param url
     * @param jsonCallback
     * @param Tag          标示
     */

    public void GetJson(final String url, final JSONCallback jsonCallback, String Tag) {


        if (!NetworkUtils.isNetworkAvailable(context) && null != jsonCallback) {
            jsonCallback.error("当前没有网络...");
            Toast.makeText(context.getApplicationContext(),
                    "当前网络不可用", Toast.LENGTH_SHORT);
            return;
        }

        RequestTag = null;
        if (null != Tag) {
            RequestTag = Tag;
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (null == jsonCallback)
                    return;
                if (null != RequestTag) {
                    jsonCallback.receive(RequestTag, jsonObject.toString());
//                    listener.onResponse(jsonObject.toString(), RequestTag);
                } else {
                    jsonCallback.receive(jsonObject.toString());
//                    listener.onResponse(jsonObject.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogHelper.d(error.getMessage());
                if (null != error.networkResponse) {
                    byte[] htmlBodyBytes = error.networkResponse.data;
                    LogHelper.d(new String(htmlBodyBytes));
                }
                if (null == jsonCallback)
                    return;
                jsonCallback.error(error.toString());
//                listener.onErrorResponse(error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Accept", "application/json");

                headers.put("Content-Type", "application/json; charset=UTF-8");

                return headers;
            }
        };
        request.setRetryPolicy(getRetryPolicy());
        requestQueue.add(request);
        requestQueue.start();
    }


    public void stop(){
        requestQueue.stop();
    }

    /**
     * 重试策略
     */

    private RetryPolicy getRetryPolicy() {
        RetryPolicy retryPolicy = new DefaultRetryPolicy(TIMEOUT_POST,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        return retryPolicy;
    }

}
