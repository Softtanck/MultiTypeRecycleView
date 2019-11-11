package com.cheweishi.android.tools;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class APPTools {
    public static String getVersionName(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return null may be returned if the specified process not found
     */
    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }


    public static int getVersionCode(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int version = packInfo.versionCode;
            return version;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 关闭键盘
     *
     * @param mcontext
     * @param et_plate
     */
    public static void closeBoard(Context mcontext, EditText et_plate) {
        InputMethodManager imm = (InputMethodManager) mcontext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_plate.getWindowToken(), 0);
    }


    /**
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                // Log.i(String.format("Background App:",
// appProcess.processName));
// Log.i(String.format("Foreground App:",
// appProcess.processName));
                return appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND;
            }
        }
        return false;
    }

    /**
     * 判断当前应用程序处于前台还是后台
     *
     * @param context
     * @return
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;

    }

    /**
     * 获取SdCard路径
     *
     * @param context
     * @return
     */
    public static String getEnvironmentPath(Context context) {
        return Environment.getExternalStorageDirectory().getPath();
    }
}
