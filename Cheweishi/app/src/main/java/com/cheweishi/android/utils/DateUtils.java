package com.cheweishi.android.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;

public class DateUtils {

	/**
	 * 鑾峰彇灞忓箷鐨勬瘮渚�	 * 
	 * @return
	 */
	public static float getScaledDensity(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		float value = dm.scaledDensity;
		return value;
	}

	/**
	 * 灏哾ip杞崲涓簆x
	 * 
	 * @return
	 */
	public static int dip2Px(Context context, float dip) {
		return (int) (context.getResources().getDisplayMetrics().density * dip);
	}

	/**
	 * 灏唒x杞崲涓篸ip
	 * 
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * base64缂栫爜鍚庤皟鐢�	 * 
	 * @param str
	 * @return
	 */
	public static String encodeReplace(String str) {
		str = str.replace("+", "_").replace("=", "%").replace("/", "*");
		return str;

	}

	/**
	 * base64瑙ｇ爜鍓嶈皟鐢�	 * 
	 * @param str
	 * @return
	 */
	public static String decodeReplace(String str) {
		str = str.replace("_", "+").replace("%", "=").replace("*", "/");
		return str;

	}

	/**
	 * 閫傜敤浜嶢dapter涓畝鍖朧iewHolder鐩稿叧浠ｇ爜
	 * 
	 * @param convertView
	 * @param id
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends View> T obtainView(View convertView, int id) {
		SparseArray<View> holder = (SparseArray<View>) convertView.getTag();
		if (holder == null) {
			holder = new SparseArray<View>();
			convertView.setTag(holder);
		}
		View childView = holder.get(id);
		if (childView == null) {
			childView = convertView.findViewById(id);
			holder.put(id, childView);
		}
		return (T) childView;
	}

	/**
	 * 鑾峰彇鎺т欢鐨勯珮搴︼紝濡傛灉鑾峰彇鐨勯珮搴︿负0锛屽垯閲嶆柊璁＄畻灏哄鍚庡啀杩斿洖楂樺害
	 * 
	 * @param view
	 * @return
	 */
	public static int getViewMeasuredHeight(View view) {
		calcViewMeasure(view);
		return view.getMeasuredHeight();
	}

	/**
	 * 鑾峰彇鎺т欢鐨勫搴︼紝濡傛灉鑾峰彇鐨勫搴︿负0锛屽垯閲嶆柊璁＄畻灏哄鍚庡啀杩斿洖瀹藉害
	 * 
	 * @param view
	 * @return
	 */
	public static int getViewMeasuredWidth(View view) {
		calcViewMeasure(view);
		return view.getMeasuredWidth();
	}

	/**
	 * 娴嬮噺鎺т欢鐨勫昂瀵�	 * 
	 * @param view
	 */
	public static void calcViewMeasure(View view) {
		int width = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int expandSpec = View.MeasureSpec.makeMeasureSpec(
				Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
		view.measure(width, expandSpec);
	}
	
	public static String splitDateString(String date){
		//1942骞�		
		return date.split(" ")[0];
	}
	
	/**
	 * 鑾峰彇灞忓箷瀹藉害(鍍忕礌)
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return windowManager.getDefaultDisplay().getWidth();
	}
	/**
	 * 鑾峰彇灞忓箷楂樺害(鍍忕礌)
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		return windowManager.getDefaultDisplay().getHeight();
	}
}
