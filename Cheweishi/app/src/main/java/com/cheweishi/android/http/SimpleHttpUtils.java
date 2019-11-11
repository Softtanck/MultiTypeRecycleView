package com.cheweishi.android.http;

import java.io.File;

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

/**
 * 这个网络请求类里面没有dialog,需要自己添加
 * 
 * @author zhangq
 * 
 */
public class SimpleHttpUtils {
	private RequestParams params;
	private Context context;
	private String url;
	private Handler handler;

	public SimpleHttpUtils(Context context, RequestParams params, String url,
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
//			HttpUtils http = new HttpUtils(30 * 1000);
//			http.configCurrentHttpCacheExpiry(3000 * 10);
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
//							super.onLoading(total, current, isUploading);
//						}
//
//						@Override
//						public void onSuccess(ResponseInfo<String> arg0) {
//							Message mes = new Message();
//							mes.obj = arg0.result;
//							handler.sendMessage(mes);
//						}
//
//						@Override
//						public void onFailure(HttpException arg0, String arg1) {
//							// TODO Auto-generated method stub
//							Message mes = new Message();
//							mes.what = 404;
//							handler.sendMessage(mes);
//
//						}
//					});
//		} else {
//			// Toast.makeText(context, "当前网络不可用", Toast.LENGTH_SHORT).show();
//			handler.sendEmptyMessage(0);
//		}
//
//	}

	/**
	 * post请求
	 */
	public void PostHttpUtils() {
		if (isNetworkAvailable(context)) {
			HttpUtils http = new HttpUtils(30 * 1000);
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
							super.onLoading(total, current, isUploading);
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							Message mes = new Message();
							mes.obj = arg0.result;
							mes.what = 200;
							handler.sendMessage(mes);
						}

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							Message mes = new Message();
							mes.what = 400;
							handler.sendMessage(mes);
						}
					});
		} else {
			handler.sendEmptyMessage(0);
		}

	}

	/**
	 * 下载
	 */
	public void DownloadUtils(String path) {
		HttpUtils http = new HttpUtils();
		HttpHandler<?> handler1 = http.download(url, path, true,// 如果目标文件存在，接着未完成的部分继续下载。
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

}
