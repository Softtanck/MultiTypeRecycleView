package com.cheweishi.android.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.MeasureSpec;

public class BitmapUtils {
	/**
	 * 以最省内存的方式读取本地资源的图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/***
	 * 压缩图片的方法
	 * 
	 * @param bitmap
	 * @return 小于15kb的图片
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap) {
		if (StringUtil.isEmpty(bitmap)) {
			return null;
		}
		int height = bitmap.getHeight();
		int size = bitmap.getRowBytes() * bitmap.getHeight() / 1024 / 1024;
		// size >1M
		if (size > 15) {
			Matrix matrix = new Matrix();
			matrix.postScale(0.5f, 0.5f);
			Bitmap bmp = null;
			try {
				bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
						height, matrix, true);
			} catch (Exception e) {
			}
			
			safeRecycle(bitmap);
			return bmp;
		} else {
			return bitmap;
		}
	}

	/**
	 * 销毁图片
	 * 
	 * @param bitmap
	 */
	private static void safeRecycle(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
	}

	/**
	 * 保存图片
	 * 
	 * @param context
	 * @param originalUri
	 * @param name
	 * @return
	 */
	public static boolean saveImage(Context context, Uri originalUri,
			String name) {
		boolean result = true;
		String path = getImagePathWithName(context, name);
		File file = new File(path);
		FileOutputStream fileOutputStream = null;
		Bitmap bmp = null;
		try {
			bmp = zoomBitmap(getBitmapWithUri(context, originalUri));
			fileOutputStream = new FileOutputStream(file);
			if (bmp != null) {
				if (bmp.compress(Bitmap.CompressFormat.JPEG, 80,
						fileOutputStream)) {
				}
			}
		} catch (Exception e) {
			file.delete();
			e.printStackTrace();
			result = false;
		} finally {
			safeRecycle(bmp);
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 根据文件名查找路径
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static String getImagePathWithName(Context context, String name) {
		return getImagePath(context) + name + ".jpg";// Constants.FILE_END_PNG ;
	}

	/**
	 * 获取图片路径
	 * 
	 * @param context
	 * @return
	 */
	public static String getImagePath(Context context) {
		// return Environment.getExternalStorageDirectory().getAbsolutePath() +
		// Constants.FILE_IMAGE_PATH ;
		String path = context.getFilesDir().getAbsolutePath()
				+ "/umeng/fb/image/";
		createDir(path);
		return path;
	}

	public static InputStream getInputStream(String path) {
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(path);
			return inputStream;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 创建路径
	 * 
	 * @param dirPath
	 * @return 是否创建成功
	 */
	public static synchronized boolean createDir(String dirPath) {
		File dirFile = new File(dirPath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		return true;
	}

	/**
	 * 通过Uri获取指定Bitmap
	 * 
	 * @param context
	 * @param originalUri
	 * @return
	 */
	public static synchronized Bitmap getBitmapWithUri(Context context,
			Uri originalUri) throws IOException {
		ContentResolver resolver = context.getContentResolver();
		InputStream input = resolver.openInputStream(originalUri);
		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(input, null, bitmapOptions);
		input.close();
		if ((bitmapOptions.outWidth == -1) || (bitmapOptions.outHeight == -1))
			return null;
		int originalSize = (bitmapOptions.outHeight > bitmapOptions.outWidth) ? bitmapOptions.outHeight
				: bitmapOptions.outWidth;
		int screenHeight = getScreenLength(context);
		int ratio = (originalSize > screenHeight) ? (originalSize / screenHeight)
				: 1;
		bitmapOptions.inJustDecodeBounds = false;
		bitmapOptions.inSampleSize = ratio;
		input = resolver.openInputStream(originalUri);
		Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
		input.close();
		return bitmap;
	}

	/**
	 * 获取屏幕的长度
	 * 
	 * @param context
	 * @return 屏幕的长度
	 */
	private static int getScreenLength(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) (context
				.getSystemService(Context.WINDOW_SERVICE));
		wm.getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels > metrics.widthPixels ? metrics.heightPixels
				: metrics.widthPixels;
	}

	/**
	 * 压缩图片
	 * 
	 * @param bitmap
	 * @return 压缩后的图片
	 */
	public static Bitmap smallZoomBitmap(Activity context, Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		Bitmap bitmap2 = comp(context, bitmap);
		return bitmap2;
	}

	private static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 50) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	private static Bitmap comp(Activity context, Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;

		float hh = (float) (height / 9.6);// 这里设置高度为800f
		float ww = width / 9;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > width) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);


		} else if (w < h && h > height) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;

		newOpts.inSampleSize = be;// 设置缩放比例
		newOpts.inPreferredConfig = Config.RGB_565;// 降低图片从ARGB888到RGB565
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	/**
	 * inJustDecodeBounds=false 这样是因为设置为true的时候可以decode，但是设置成false就无法decode
	 * 所以还是按照true的方式来验证吧
	 * 
	 * @param context
	 * @param originalUri
	 * @return
	 */
	public static boolean isImage(Context context, Uri originalUri) {
		ContentResolver resolver = context.getContentResolver();
		Bitmap bitmap = null;
		try {
			InputStream input = resolver.openInputStream(originalUri);
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
			bitmapOptions.inJustDecodeBounds = false;
			bitmapOptions.inSampleSize = 4;
			bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
			input.close();
			if ((bitmapOptions.outWidth == -1)
					|| (bitmapOptions.outHeight == -1)) {
				return false;
			}
		} catch (Exception ignore) {
			return false;
		} finally {
			safeRecycle(bitmap);
		}
		return true;
	}

	/****
	 * 将View转换为bitmap
	 * 
	 * @param view
	 *            需要转换的view
	 * @return Bitmap
	 */
	public static Bitmap getViewBitmap(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}

	/**
	 * 把drawable转为bitmap
	 * 
	 * @param drawable
	 *            需要转为的drawable
	 * @return bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}
}
