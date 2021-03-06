package com.cheweishi.android.utils;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class MyHttpUtils {
	RequestParams params;
	Context context;
	String url;
	ProgressDialog progressDialog;
	Handler handler;

	public MyHttpUtils(Context context, RequestParams params, String url,
			Handler handler) {
		// TODO Auto-generated constructor stub
		this.params = params;
		this.context = context;
		this.url = url;
		this.handler = handler;

	}

//	/**
//	 * get请求
//	 */
//	public void GetHttpUtils() {
//		if (isNetworkAvailable(context)) {
//
//			HttpUtils http = new HttpUtils(1000 * 10);
//			http.configSoTimeout(1000 * 10);
//			http.configCurrentHttpCacheExpiry(1000 * 10);
//			http.send(HttpRequest.HttpMethod.GET, url, null,
//					new RequestCallBack<String>() {
//						@Override
//						public void onStart() {
//							super.onStart();
//						}
//
//						@Override
//						public void onLoading(long total, long current,
//								boolean isUploading) {
//							// TODO Auto-generated method stub
//							super.onLoading(total, current, isUploading);
//						}
//
//						@Override
//							// TODO Auto-generated method stub
//							// progressDialog.dismiss();
//							Message mes = new Message();
//							mes.obj = arg0.result;
//						}
//
//						@Override
//						public void onFailure(HttpException arg0, String arg1) {
//							// TODO Auto-generated method stub
//							// progressDialog.dismiss();
//							Message mes = new Message();
//							mes.what = 400;
//							handler.sendMessage(mes);
//						}
//					});
//		} else {
//			Toast.makeText(context, "当前网络不可用", Toast.LENGTH_SHORT).show();
//			// progressDialog.dismiss();
//		}
//
//	}

	/**
	 * post请求
	 */
	public void PostHttpUtils() {
		if (isNetworkAvailable(context)) {
			HttpUtils http = new HttpUtils(1000 * 30);
			http.configSoTimeout(3000 * 10);
			http.configCurrentHttpCacheExpiry(30000);

			http.send(HttpRequest.HttpMethod.POST, url, params,
					new RequestCallBack<String>() {
						@Override
						public void onStart() {
							super.onStart();

						}

						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
							// TODO Auto-generated method stub
							super.onLoading(total, current, isUploading);
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							// TODO Auto-generated method stub
							// progressDialog.dismiss();
							Message mes = new Message();
							mes.obj = arg0.result;
							handler.sendMessage(mes);
						}

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// Toast.makeText(context, arg1,
							// Toast.LENGTH_LONG).show();

							// progressDialog.dismiss();
							Message mes = new Message();
							mes.what = 400;
							handler.sendMessage(mes);
						}
					});
		} else {
			Toast.makeText(context, "当前网络不可用", Toast.LENGTH_SHORT).show();
			// progressDialog.dismiss();
		}

	}

	/**
	 * 下载
	 */
	public void DownloadUtils(String path) {
		HttpUtils http = new HttpUtils();
		HttpHandler handler1 = http.download(url, path, true,// 如果目标文件存在，接着未完成的部分继续下载。
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
						progressDialog.dismiss();
						Toast.makeText(context, "下载失败" + arg1,
								Toast.LENGTH_SHORT).show();
						Message mes = new Message();
						mes.what = 400;
						handler.sendMessage(mes);
					}

					@Override
					public void onSuccess(ResponseInfo<File> arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT)
								.show();
						progressDialog.dismiss();
						Message mes = new Message();
						mes.obj = arg0;
						handler.sendMessage(mes);
					}
				});
	}

	public static boolean isNetworkAvailable(Context activity) {
		Context context = activity.getApplicationContext();
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivityManager == null) {
			return false;
		} else {
			// 获取NetworkInfo对象
			NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

			if (networkInfo != null && networkInfo.length > 0) {
				for (int i = 0; i < networkInfo.length; i++) {
					// 判断当前网络状态是否为连接状态
					if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public void uploadMethod(final RequestParams params, final String uploadHost) {
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, uploadHost, params,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						// msgTextview.setText("conn...");
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						if (isUploading) {
							// msgTextview.setText("upload: " + current + "/"+
							// total);
						} else {
							// msgTextview.setText("reply: " + current + "/"+
							// total);
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// msgTextview.setText("reply: " + responseInfo.result);
						Message mes = new Message();
						//Toast.makeText(context, responseInfo.result, Toast.LENGTH_LONG).show();
						mes.obj = responseInfo.result;
						handler.sendMessage(mes);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// msgTextview.setText(error.getExceptionCode() + ":" +
						// msg);
						Message mes = new Message();
						mes.what = 400;
						handler.sendMessage(mes);
					}
				});
	}
}
