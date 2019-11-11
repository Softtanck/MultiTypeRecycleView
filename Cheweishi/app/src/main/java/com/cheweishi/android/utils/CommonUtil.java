package com.cheweishi.android.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 公共工具类,处理(是否联网,配置网络,获得版本号,获得屏幕长和宽,定义菜单按钮大小,按时间分配图片名字,微信分享)
 */
public class CommonUtil {

	/**
	 * 查看是否联网
	 */
	public static boolean getNowNetWorkState(final Context mContext) {
		boolean netStatus = false;
		ConnectivityManager connectionManager = (ConnectivityManager) mContext
				.getSystemService(Activity.CONNECTIVITY_SERVICE);
		// 获取代表联网状态的NetWorkInfo对象
		NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
		// 获取当前的网络连接是否可用
		if (networkInfo != null) {
			netStatus = networkInfo.isAvailable();
		}
		return netStatus;
	}
	// 判断WIFI网络是否可用的方法
	public static boolean isOpenWifi(Context mContext) {
		ConnectivityManager connManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return mWifi.isConnected();
	}
	// 设置网络
	public static void openIntent(final Context mContext) {
		// 当网络不可用时，跳转到网络设置页面
		Builder b = new AlertDialog.Builder(mContext)
				.setIcon(android.R.drawable.stat_notify_error)
				.setTitle("没有可用的网络").setMessage("是否对网络进行设置？");
		b.setPositiveButton("是", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Intent intent = new Intent();
				intent.setClassName("com.android.settings",
						"com.android.settings.Settings");
				mContext.startActivity(intent);

			}
		}).setNeutralButton("否", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		}).show();
	}

	/**
	 * 获取版本号
	 */
	public static int getCode(Context mContext) {
		// 获取当前软件包信息
		PackageInfo pi;
		int versionCode = 0;
		try {
			pi = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);
			versionCode = pi.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 获得屏幕长和宽
	 * 
	 * @return int[]{宽,高}
	 */
	public static int[] screenWidthHeight(Context context) {
		/**
		 * 通过该参数获取屏幕宽度
		 */
		DisplayMetrics dm = context.getResources().getDisplayMetrics();// 获取屏幕宽度
		int widthPixels = dm.widthPixels;
		int heightPixels = dm.heightPixels;
		return new int[] { widthPixels, heightPixels };

	}

	/**
	 * 更改菜单大小
	 * 
	 * @param context
	 * @param imgs
	 */
	public static void changeImageMenu(Context context, ImageView... imgs) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		int width = metrics.widthPixels / 8;
		int height = metrics.heightPixels / 6;
		for (int i = 0; i < imgs.length; i++) {
			imgs[i].setLayoutParams(new LinearLayout.LayoutParams(width, height));
		}
	}

	/**
	 * 自定义图片名字
	 * 
	 * @return
	 */
	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	/**
	 * 获取手机系统SDK版本
	 * 
	 * @return 如API 17 则返回 17
	 */
	public static int getSDKVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}

	/** 调整屏幕背景的颜色值 */
	public static Bitmap setImageBitmapColor(float src[], Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Bitmap bmp = Bitmap.createBitmap(width, height, bitmap.getConfig());
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint();
		ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.set(src);
		paint.setColorFilter(new ColorMatrixColorFilter(src));
		// 通过指定了RGBA矩阵的Paint把原图画到空白图片上
		canvas.drawBitmap(bitmap, new Matrix(), paint);
		return bmp;
	}
	/**
	 * 定义一个全局变量 lastClickTime, 用来记录最后点击的时间.
	 * 每次点击前需要进行判断, 用lastClickTime 和当前时间想比较，并且更新最后点击时间，
	 * 若小于临界值，则算无效点击，不触发事件
	 */
	private static long lastClickTime;
	public static boolean isFastDoubleClick(long howlong) {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < howlong) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
}
