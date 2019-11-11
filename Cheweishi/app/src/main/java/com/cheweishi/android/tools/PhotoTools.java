package com.cheweishi.android.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

@SuppressLint("NewApi")
public class PhotoTools {

	// 保存图片本地路径
	public static final String ACCOUNT_DIR = Environment
			.getExternalStorageDirectory().getPath() + "/.account/";
	public static final String ACCOUNT_MAINTRANCE_ICON_CACHE = ".icon_cache/";
	public static final String IMGPATH = ACCOUNT_DIR
			+ ACCOUNT_MAINTRANCE_ICON_CACHE;

	public static final String IMAGE_FILE_NAME = ".faceImage.jpeg";
	public static final String TMP_IMAGE_FILE_NAME = ".tmp_faceImage.jpeg";

	// // 版本比较：是否是4.4及以上版本
	// final boolean mIsKitKat = Build.VERSION.SDK_INT >=
	// Build.VERSION_CODES.KITKAT;
	// 版本比较：是否是4.4及以上版本
	public static final boolean mIsKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	// 常量定义
	public static final int TAKE_A_PICTURE = 10;
	public static final int SELECT_A_PICTURE = 20;
	public static final int SET_PICTURE = 30;
	public static final int SET_ALBUM_PICTURE_KITKAT = 40;
	public static final int SELECET_A_PICTURE_AFTER_KIKAT = 50;
	static File fileone;
	static File filetwo;

	public static void deleteFile() {
		File file1 = new File(PhotoTools.IMGPATH + PhotoTools.IMAGE_FILE_NAME);// 如果缓存目录存在的话，删除缓存目录
		FileTools.delete(file1);
		File file2 = new File(PhotoTools.IMGPATH
				+ PhotoTools.TMP_IMAGE_FILE_NAME);// 如果缓存目录存在的话，删除缓存目录
		FileTools.delete(file2);
	}

	public static void init() {
		File directory = new File(ACCOUNT_DIR);
		File imagepath = new File(IMGPATH);
		if (!directory.exists()) {
			Log.i("zou", "directory.mkdir()");
			directory.mkdir();
		}
		if (!imagepath.exists()) {
			Log.i("zou", "imagepath.mkdir()");
			imagepath.mkdir();
		}

		fileone = new File(IMGPATH, IMAGE_FILE_NAME);
		filetwo = new File(IMGPATH, TMP_IMAGE_FILE_NAME);

		try {
			if (!fileone.exists() && !filetwo.exists()) {
				fileone.createNewFile();
				filetwo.createNewFile();
			}
		} catch (Exception e) {
		}
	}

	public static Bitmap getimage(String srcPath, Activity context) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流f机比较多是800*480分辨率，所以高和宽我们设置为getBitmapSize
		float hh = 400;
		// 这里设置高度为800f
		float ww = 400;
		// 这里设置高度为800f;//
			// 这里设置宽度为480f
			// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	private static Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//
		// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 20) { //
			// 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//
			// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//
		// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//
		// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public String getSDPath(Context context) {
		File sdDir = context.getFilesDir();
		return sdDir.getPath();

	}

	/**
	 * <br>
	 * 功能简述:4.4及以上从相册选择照片 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static void SelectImageUriAfterKikat(Activity context) {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, null);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		context.startActivityForResult(intent, SELECET_A_PICTURE_AFTER_KIKAT);
	}

	/**
	 * <br>
	 * 功能简述:裁剪图片方法实现---------------------- 相册 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 */
	public static void cropImageUri(Activity context) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		// intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 400);
		intent.putExtra("outputY", 400);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		context.startActivityForResult(intent, SELECT_A_PICTURE);
	}

	/**
	 * <br>
	 * 功能简述:4.4以上裁剪图片方法实现---------------------- 相册 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static void selectImageUriAfterKikat(Activity context) {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		context.startActivityForResult(intent, SELECET_A_PICTURE_AFTER_KIKAT);
	}

	/**
	 * <br>
	 * 功能简述:裁剪图片方法实现----------------------相机 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param uri
	 */
	public static void cameraCropImageUri(Uri uri, Activity context) {
		// Toast.makeText(context, "haha", Toast.LENGTH_LONG).show();
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/jpeg");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 400);
		intent.putExtra("outputY", 400);
		intent.putExtra("scale", true);
		// if (mIsKitKat) {
		// intent.putExtra("return-data", true);
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		// } else {
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		// }
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		context.startActivityForResult(intent, SET_PICTURE);
	}

	/**
	 * <br>
	 * 功能简述: 4.4及以上改动版裁剪图片方法实现 --------------------相机 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param uri
	 */
	public static void cropImageUriAfterKikat(Uri uri, Activity context) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/jpeg");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 640);
		intent.putExtra("outputY", 640);
		intent.putExtra("scale", true);
		// intent.putExtra("return-data", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(IMGPATH, TMP_IMAGE_FILE_NAME)));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		context.startActivityForResult(intent, SET_ALBUM_PICTURE_KITKAT);
	}

	/**
	 * <br>
	 * 功能简述: <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param uri
	 * @return
	 */
	public static Bitmap decodeUriAsBitmap(Uri uri, Context context) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(context.getContentResolver()
					.openInputStream(uri));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * <br>
	 * 功能简述:4.4及以上获取图片的方法 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}
}
