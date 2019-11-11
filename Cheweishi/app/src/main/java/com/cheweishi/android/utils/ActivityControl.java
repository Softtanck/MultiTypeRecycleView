package com.cheweishi.android.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.cheweishi.android.activity.LoginActivity;
import com.umeng.analytics.MobclickAgent;

public class ActivityControl {
    private static List<Activity> acitivityList = new ArrayList<Activity>();
//	public static boolean finishFlag = false;

    public static void addActivity(Activity activity) {
        acitivityList.add(activity);
    }

    public static void removeActivity(Activity activity) {
        acitivityList.remove(activity);
    }

    public static void stayActivity(Activity activity1, Activity activity2) {
        for (Activity activity : acitivityList) {
            if (null != activity && activity != activity1
                    && activity != activity2) {
                activity.finish();
            }
        }
    }

    public static void finishProgrom() {
        for (Activity activity : acitivityList) {
            if (null != activity && !(activity instanceof LoginActivity)) {
                activity.finish();
            }
        }
//		finishFlag = true;
        // System.exit(0);
//		android.os.Process.killProcess(android.os.Process.myPid()); // TODO
    }

    public static void GG(Context context) {
        for (Activity activity : acitivityList) {
            if (null != activity && !(activity instanceof LoginActivity)) {
                activity.finish();
            }
        }
//		finishFlag = true;
        MobclickAgent.onKillProcess(context);
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid()); // TODO
    }

    public static int getCount() {
        return acitivityList.size();
    }

    public static void finishActivity(int i) {
        if (i >= 0 && i < acitivityList.size()) {
            acitivityList.get(i).finish();
        }
    }

    public static Activity getActivity(int i) {
        if (i >= 0 && i < acitivityList.size()) {
            return acitivityList.get(i);
        } else {
            return null;
        }
    }

    public static void removeActivityFromName(String name) {

        for (Activity activity : acitivityList) {
            if (null != activity && name.equals(activity.getClass().getName())) {
                activity.finish();
                break;
            }
        }
    }

}
