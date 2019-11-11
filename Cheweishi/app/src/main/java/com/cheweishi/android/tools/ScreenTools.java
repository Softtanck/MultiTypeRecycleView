package com.cheweishi.android.tools;

import java.lang.reflect.Method;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;

public class ScreenTools {

	/**
	 * 获取屏幕宽度
	 * @param context
	 * @return
	 */
	public static int getScreentWidth(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * 获取屏幕高度
	 * @param context
	 * @return
	 */
	public static int getScreentHeight(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	public static int getScreenWidthAndSizeInPx(Activity activity) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		int[] size = new int[2];
		size[0] = displayMetrics.widthPixels;
		size[1] = displayMetrics.heightPixels;
		return size[1];
	}

	// 1 /** 2 * 3 * 获取状态栏高度 4 * 5 * @param activity 6 * @return 7 */ 8
	public static int getStateHeight(Activity activity) {
		Rect rect = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		return rect.top;
	}

	public static int getDpi(Activity context) {
		int dpi = 0;
		Display display = context.getWindowManager().getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		@SuppressWarnings("rawtypes")
		Class c;
		try {
			c = Class.forName("android.view.Display");
			@SuppressWarnings("unchecked")
			Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
			method.invoke(display, dm);
			dpi = dm.heightPixels;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dpi;
	}
}
