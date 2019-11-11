package com.cheweishi.android.biz;

import java.io.File;

import android.os.Environment;

public class FileUtil {
	/**
	 * 获取 sdcard中缓存路径
	 */
	public static String getAndroidPath(String SDpath) {
		String path = Environment.getExternalStorageDirectory().getPath()
				+ SDpath;
		File file = new File(path);
		// 若父目录不存在 则创建目录
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}
}
