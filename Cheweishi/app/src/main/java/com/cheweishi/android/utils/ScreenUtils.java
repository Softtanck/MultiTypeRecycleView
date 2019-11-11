package com.cheweishi.android.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class ScreenUtils {
    private ScreenUtils(){
    	throw new UnsupportedOperationException("cannot be instantiated");
    }
   
	/**
	 * 获得屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context)
	{
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	/**
	 * 获得屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context)
	{
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}

    /**
     * 获得屏幕宽度
     */
    public static int getScressWidth(Context context){
    	WindowManager wm=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    	DisplayMetrics outMetrics=new DisplayMetrics();
    	wm.getDefaultDisplay().getMetrics(outMetrics);
    	return  outMetrics.widthPixels;
    }
    
    /**
     * 获取屏幕高度
     */
    public static int getScressHeight(Context context){
    	WindowManager manager=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    	DisplayMetrics displayMetrics=new DisplayMetrics();
    	manager.getDefaultDisplay().getMetrics(displayMetrics);
    	return displayMetrics.heightPixels;
    }
    
    /**
     * 获取状兰的高度
     */
    public static int getStatusHeihtt(Context context){
    	int statusHeight=-1;
    	try {
			Class<?> calzz=Class.forName("com.android.internal.R$dimen");
			Object object=calzz.newInstance();
			String string = calzz.getField("status_bar_height").get(object).toString();
			int parseInt = Integer.parseInt(string);
			statusHeight=context.getResources().getDimensionPixelSize(parseInt);
		} catch (Exception e) {
			// TODO: handle exception
		}
    	return statusHeight;
    }
    
    /**
     * 获取屏幕截图，包含状态兰
     */
    public static Bitmap snapShotWithStatusBar(Activity activity){
    	View view=activity.getWindow().getDecorView();
    	view.setDrawingCacheEnabled(true);
    	view.buildDrawingCache();
    	Bitmap bmp=view.getDrawingCache();
    	int width=getScressWidth(activity);
    	int heigth=getScressHeight(activity);
    	Bitmap bp=null;
    	bp=Bitmap.createBitmap(bmp, 0, 0, width, heigth);
    	view.destroyDrawingCache();
    	return bp;
    }
    
    /**
     * 获取当前屏幕截图，不包涵状态栏
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity){
    	View view=activity.getWindow().getDecorView();
    	view.setDrawingCacheEnabled(true);
    	view.buildDrawingCache();
    	Bitmap bmp=view.getDrawingCache();
    	Rect frame=new Rect();
    	activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
    	int statusBarHeight=frame.top;
    	int width=getScressWidth(activity);
    	int height=getStatusHeihtt(activity);
    	Bitmap bp=null;
    	bp=Bitmap.createBitmap(bmp, 0, 0, width, height-statusBarHeight);
    	view.destroyDrawingCache();
    	return bp;
    }
}
