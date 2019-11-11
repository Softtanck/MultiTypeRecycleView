package com.cheweishi.android.tools;

import java.io.File;

import com.cheweishi.android.dialog.ProgrosDialog;

public class FileTools {
	public static void delete(File file) {
		if (file.exists()) {
			if (file.isFile()) {

				file.delete();
				ProgrosDialog.closeProgrosDialog();
				return;
			}
			if (file.isDirectory()) {
				File[] childFiles = file.listFiles();
				if (childFiles == null || childFiles.length == 0) {
					file.delete();
					return;
				}
				for (int i = 0; i < childFiles.length; i++) {
					delete(childFiles[i]);
				}
				file.delete();
				ProgrosDialog.closeProgrosDialog();
			}
		}
	}
}
