package com.cheweishi.android.utils;

import android.content.Context;

/**
 * 
 * @author fxsky 2012.11.12
 *
 */
public class DisplayUtil {
	/**
	 * 
	 * 
	 * @param px转换为dp
	 * @param 
	 *            
	 * @return dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 
	 * @param dip转换为px
	 * @param scale
	 * @return px
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 
	 * @param px转换为sp
	 * @param fontScale
	 * @return sp
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 
	 * @param spValue
	 * @param fontScale
	 *            
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
}