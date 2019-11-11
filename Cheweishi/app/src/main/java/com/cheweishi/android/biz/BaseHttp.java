package com.cheweishi.android.biz;

import java.io.File;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.tools.SharePreferenceTools;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * http请求类
 * 
 * @author 胡健
 */
public class BaseHttp {
	private HttpUtils mHttpClient;
	private HttpClientListener mHttpClientListener;
	public final int POSTNUM = 1;
	public final int GETNUM = 0;
	private Handler handler = new Handler();
	private Toast mToast;

	/**
	 * get和post请求
	 * 
	 * @param marker
	 *            请求标记
	 * @param url
	 *            目标地址
	 * @param mRequestParams
	 * @param callback
	 *            回调对象
	 * @param type
	 *            get post 标记
	 * @param context
	 *            上下文
	 */
	public void getPostData(int marker, String url,
			RequestParams mRequestParams, JSONCallback callback, int type,
			Context context) {
		Log.i("result", "==post数据请求==type==" + type + "=url==" + url);
		this.mHttpClient = new HttpUtils(30 * 1000);
		this.mHttpClient.setHead("123321");
		this.mHttpClient.configCurrentHttpCacheExpiry(30 * 1000);
		/** 网络客户端助手监听 */
		this.mHttpClientListener = new HttpClientListener(marker, callback);
		if (type == POSTNUM) {
			mHttpClient.send(HttpMethod.POST, url, mRequestParams,
					mHttpClientListener);
		} else if (type == GETNUM) {
			mHttpClient.send(HttpMethod.GET, url, mHttpClientListener);
		}
	}

	/**
	 * post，get请求的回调监听
	 * 
	 * @author Administrator
	 */

	private class HttpClientListener extends RequestCallBack<String> {
		JSONCallback callback;
		int type;

		public HttpClientListener(int type, JSONCallback callback) {
			this.callback = callback;
			this.type = type;
		}

		@Override
		public void onSuccess(final ResponseInfo<String> responseInfo) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.i("result", "==数据请求结果==type==" + type + "=result=="
							+ responseInfo.result);
					callback.receive(type, responseInfo.result);
				}
			});
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			// if (!TextUtils.isEmpty(text)) {
			try {
				// if (mToast == null) {
				// mToast = Toast.makeText(context, "网络请求超时",
				// Toast.LENGTH_SHORT);
				// } else {
				// mToast.setText("网络请求超时");
				// }

				// }
				ProgrosDialog.closeProgrosDialog();
				if (!com.cheweishi.android.utils.StringUtil.isEmpty(mToast)) {
					mToast.show();
				}
				// Toast.makeText(context, "网络请求超时", Toast.LENGTH_SHORT).show();
				Log.i("result", "==HttpException=" + arg0.getExceptionCode());
				Log.i("result", "==HttpException=" + arg1);
				SharePreferenceTools.phoneFlag = false;

				if (type >= 10000) {
					callback.receive(400, arg1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 下载文件
	 */
	public void DownloadUtils(final int type, String path, String URL,
			final JSONCallback mMyCallback2, final Context context) {
		HttpUtils http = new HttpUtils();
		HttpHandler<?> handler1 = http.download(URL, path, true,// 如果目标文件存在，接着未完成的部分继续下载。
				true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
				new RequestCallBack<File>() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						// TODO Auto-generated method stub
						super.onLoading(total, current, isUploading);

					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(context, "102下载失败" + arg1,
								Toast.LENGTH_SHORT).show();

					}

					@Override
					public void onSuccess(ResponseInfo<File> arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT)
								.show();
						mMyCallback2.downFile(type, arg0);
					}
				});
	}

	/**
	 * 更新
	 * 
	 * @param type
	 *            请求标记
	 * @param params
	 * @param uploadHost
	 * @param mMyCallback2
	 *            回调对象
	 * @param context
	 */
	public void uploadMethod(final int type, final RequestParams params,
			final String uploadHost, final JSONCallback mMyCallback2,
			final Context context) {
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						mMyCallback2.receive(type, responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						if (mToast == null) {
							mToast = Toast.makeText(context, "网络请求超时",
									Toast.LENGTH_SHORT);
						} else {
							mToast.setText("网络请求超时");
						}
					}
				});
	}

	public void removeHandler() {
		handler.removeCallbacksAndMessages(null);
	}
}
