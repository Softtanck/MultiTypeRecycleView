package com.cheweishi.android.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import org.apache.commons.codec.binary.Base64;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.cheweishi.android.activity.BaseActivity;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.entity.LoginResponse;
import com.cheweishi.android.fragement.BaseFragment;
import com.cheweishi.android.utils.disklrucahe.DiskLruCacheHelper;

public class LoginMessageUtils {
    public static boolean showDialogFlag = false;

    // public static LoginMessage loginMessage;

    public static synchronized String getMessage(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "loginData", Context.MODE_PRIVATE);
        String message = sharedPreferences.getString(key, null);
        return message;

    }

    public static void setLogined(Context context, boolean isLogined) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "loginData", Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogined", isLogined);
        editor.commit();
    }

    public static boolean isLogined(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "loginData", Context.MODE_PRIVATE);
        boolean result = sharedPreferences.getBoolean("isLogined", false);
        return result;
    }

    public static void setPush(Context context, boolean push) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "pushData", Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean("push", push);
        editor.commit();
    }

    public static boolean getPush(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "pushData", Context.MODE_PRIVATE);
        boolean result = sharedPreferences.getBoolean("push", true);
        return result;
    }

    public static void saveProduct(LoginResponse pr, Context context) {
        BaseActivity.loginResponse = pr;
        BaseFragment.loginResponse = pr;
        SharedPreferences preferences = context.getSharedPreferences("base64",
                Context.MODE_PRIVATE);
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            // 将对象写入字节流
            oos.writeObject(pr);

            // 将字节流编码成base64的字符窜
            String productBase64 = new String(Base64.encodeBase64(baos
                    .toByteArray()));
            // Toast.makeText(context, "存储成功！", Toast.LENGTH_LONG).show();
            Editor editor = preferences.edit();
            editor.putString("loginMessage", productBase64);
            editor.commit();
            oos.close();
            // Toast.makeText(LoginActivity.this, "存储成功！",
            // Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            // Toast.makeText(LoginActivity.this, "存储失败！",
            // Toast.LENGTH_LONG).show();
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            baos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Constant.loginResponse = getLoginResponse(context);
    }




    public static LoginResponse getLoginResponse(Context context) {
        LoginResponse loginMessageTemp = null;
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences(
                    "base64", Context.MODE_PRIVATE);
            String productBase64 = preferences.getString("loginMessage", "");
            if (productBase64 == "") {
                // init();
                return null;
            }
            // 读取字节
            byte[] base64 = Base64.decodeBase64(productBase64.getBytes());

            // 封装到字节流
            ByteArrayInputStream bais = new ByteArrayInputStream(base64);
            try {
                // 再次封装
                ObjectInputStream bis = new ObjectInputStream(bais);
                try {
                    // 读取对象
                    loginMessageTemp = (LoginResponse) bis.readObject();
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                bis.close();
            } catch (StreamCorruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                bais.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return loginMessageTemp;
    }

    public static void deleteLoginMessage(Context context) {
        BaseActivity.loginResponse = null;
        SharedPreferences preferences = context.getSharedPreferences("base64",
                Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.remove("test_lg");
        editor.commit();
    }


    public static void saveloginmsg(Context context, LoginResponse loginResponse) {
        try {
            DiskLruCacheHelper diskLruCacheHelper = new DiskLruCacheHelper(context);
            diskLruCacheHelper.put("test_lg", loginResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LoginResponse getloginmsg(Context context) {
        try {
            DiskLruCacheHelper diskLruCacheHelper = new DiskLruCacheHelper(context);
            LoginResponse loginResponse = diskLruCacheHelper.getAsSerializable("test_lg");
            return loginResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
