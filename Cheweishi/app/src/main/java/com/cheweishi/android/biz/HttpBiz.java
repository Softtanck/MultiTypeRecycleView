package com.cheweishi.android.biz;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.utils.CommonUtil;
import com.lidroid.xutils.http.RequestParams;

/**
 * 访问逻辑处理
 * 
 * @author 胡健
 */
public class HttpBiz extends BaseHttp {
	private Context context;
	/** 网络是否可用 **/
	public boolean isNetwork;
	private Toast mToast;

	public HttpBiz(Context context) {
		super();
		this.context = context;

	}

	/**
	 * POSTQ请求
	 * 
	 * @param type
	 *            标记
	 * @param url
	 *            目标地址
	 * @param mRequestParams
	 * @param callback
	 */
	public void httPostData(int type, String url, RequestParams mRequestParams,
			JSONCallback callback) {
		// Message msg = new Message();

		/** 判断是否有网 **/
		this.isNetwork = CommonUtil.getNowNetWorkState(context);
		if (isNetwork) {
			getPostData(type, url, mRequestParams, callback, POSTNUM, context);
		} else {
//			if (!TextUtils.isEmpty("当前网络不可用")) {
			handler1.sendEmptyMessage(0);
//			}
			
			// Toast.makeText(context, "当前网络不可用", 1).show();
			callback.receive(400, "");
			ProgrosDialog.closeProgrosDialog();
		}
	}

	private Handler handler1 = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (mToast == null) {
					mToast = Toast.makeText(context.getApplicationContext(),
							"当前网络不可用", Toast.LENGTH_SHORT);
				} else {
					mToast.setText("当前网络不可用");
				}
				mToast.show();
				break;
			}
		}
	};

	/**
	 * get请求
	 * 
	 * @param type
	 * @param url
	 * @param mRequestParams
	 * @param callback
	 */
	public void httpGetData(int type, String url, RequestParams mRequestParams,
			JSONCallback callback) {
		/** 判断是否有网 **/
		this.isNetwork = CommonUtil.getNowNetWorkState(context);
		if (isNetwork) {
			getPostData(type, url, mRequestParams, callback, GETNUM, context);
		} else {
			// 没网返回401
			handler1.sendEmptyMessage(0);
			callback.receive(400, "");
			ProgrosDialog.closeProgrosDialog();
		}

	}

	/**
	 * 下载文件
	 * 
	 * @param type
	 * @param path
	 * @param url
	 * @param callback
	 */
	public void httpDownloadFile(int type, String path, String url,
			JSONCallback callback) {
		if (isNetwork) {
			DownloadUtils(type, path, url, callback, context);
		} else {
			handler1.sendEmptyMessage(0);
			callback.receive(400, "");
			ProgrosDialog.closeProgrosDialog();
		}

	}

	/**
	 * 更新文件
	 * 
	 * @param type
	 * @param path
	 * @param url
	 * @param callback
	 */
	public void httpUploadMethod(int type, RequestParams mRequestParams,
			String uploadHost, JSONCallback callback) {
		if (isNetwork) {
			uploadMethod(type, mRequestParams, uploadHost, callback, context);
		} else {
			handler1.sendEmptyMessage(0);
			callback.receive(400, "");
			ProgrosDialog.closeProgrosDialog();
		}
	}

	public void removeAllHandler() {
		removeHandler();
	}
}
