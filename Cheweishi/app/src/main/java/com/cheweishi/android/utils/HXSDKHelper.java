///**
// * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *     http://www.apache.org/licenses/LICENSE-2.0
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.cheweishi.android.utils;
//
//import java.util.Iterator;
//import java.util.List;
//
//import android.app.ActivityManager;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.util.Log;
//
//import com.cheweishi.android.activity.MainActivity;
//import com.easemob.EMCallBack;
//import com.easemob.EMConnectionListener;
//import com.easemob.EMError;
//import com.easemob.chat.EMChat;
//import com.easemob.chat.EMChatManager;
//
///**
// *环信初始化工具类 
// * @author mingdasen
// *
// */
//public abstract class HXSDKHelper {
//	private static final String TAG = "result";
//    /**
//     * application context
//     */
//    protected Context appContext = null;
//    /**
//     * MyConnectionListener
//     */
//    protected EMConnectionListener connectionListener = null;
//    
//    /**
//     * password in cache
//     */
//    protected String password = null;
//    
//    /**
//     * init flag: test if the sdk has been inited before, we don't need to init again
//     */
//    private boolean sdkInited = false;
//
//    /**
//     * the global HXSDKHelper instance
//     */
//    private static HXSDKHelper me = null;
//    
//    /**
//     * the notifier
//     */
//    
//    protected HXSDKHelper(){
//        me = this;
//    }
//    
//    /**
//     *初始化环信
//     */
//    public synchronized boolean onInit(Context context){
//        if(sdkInited){
//            return true;
//        }
//        appContext = context;
//        int pid = android.os.Process.myPid();
//        String processAppName = getAppName(pid);
//        
//        Log.d(TAG, "process app name : " + processAppName);
//        // 如果app启用了远程的service，此application:onCreate会被调用2次
//        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
//        // 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
//        if (processAppName == null || !processAppName.equalsIgnoreCase("com.cheweishi.android")) {
//            Log.e(TAG, "enter the service process!");
//            // 则此application::onCreate 是被service 调用的，直接返回
//            return false;
//        }
//
//        // 初始化环信SDK,一定要先调用init()
//        EMChat.getInstance().init(context);
//        Log.d(TAG, "initialize EMChat SDK");
//                
//        initListener();
//        sdkInited = true;
//        return true;
//    }
//    
//    /**
//     * get global instance
//     * @return
//     */
//    public static HXSDKHelper getInstance(){
//        return me;
//    }
//    
//    
//    /**
//     * logout HuanXin SDK
//     */
//    public void logout(final EMCallBack callback){
//        EMChatManager.getInstance().logout(new EMCallBack(){
//
//            @Override
//            public void onSuccess() {
//                // TODO Auto-generated method stub
//                if(callback != null){
//                    callback.onSuccess();
//                }
//            }
//
//            @Override
//            public void onError(int code, String message) {
//                // TODO Auto-generated method stub
//                
//            }
//
//            @Override
//            public void onProgress(int progress, String status) {
//                // TODO Auto-generated method stub
//                if(callback != null){
//                    callback.onProgress(progress, status);
//                }
//            }
//            
//        });
//    }
//    
//    /**
//     * 检查是否已经登录过
//     * @return
//     */
//    public boolean isLogined(){
//       return EMChat.getInstance().isLoggedIn();
//    }
//    /**
//     * init HuanXin listeners
//     */
//    protected void initListener(){
//        Log.d(TAG, "init listener");
//        
//        // create the global connection listener
//        connectionListener = new EMConnectionListener() {
//            @Override
//            public void onDisconnected(int error) {
//            	if (error == EMError.USER_REMOVED) {
//            		onCurrentAccountRemoved();
//            	}else if (error == EMError.CONNECTION_CONFLICT) {
//                    onConnectionConflict();
//                }else{
//                    onConnectionDisconnected(error);
//                }
//            }
//
//            @Override
//            public void onConnected() {
//                onConnectionConnected();
//            }
//        };
//        
//        //注册连接监听
//        EMChatManager.getInstance().addConnectionListener(connectionListener);       
//    }
//
//    /**
//     * the developer can override this function to handle connection conflict error
//     */
//    protected void onConnectionConflict(){
//    	Intent intent = new Intent(appContext, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("conflict", true);
//        appContext.startActivity(intent);
//    }
//
//    
//    /**
//     * the developer can override this function to handle user is removed error
//     */
//    protected void onCurrentAccountRemoved(){}
//    
//    
//    /**
//     * handle the connection connected
//     */
//    protected void onConnectionConnected(){}
//    
//    /**
//     * handle the connection disconnect
//     * @param error see {@link EMError}
//     */
//    protected void onConnectionDisconnected(int error){}
//
//    /**
//     * check the application process name if process name is not qualified, then we think it is a service process and we will not init SDK
//     * @param pID
//     * @return
//     */
//    private String getAppName(int pID) {
//        String processName = null;
//        ActivityManager am = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
//        List l = am.getRunningAppProcesses();
//        Iterator i = l.iterator();
//        PackageManager pm = appContext.getPackageManager();
//        while (i.hasNext()) {
//            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
//            try {
//                if (info.pid == pID) {
//                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
//                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
//                    // info.processName +"  Label: "+c.toString());
//                    // processName = c.toString();
//                    processName = info.processName;
//                    return processName;
//                }
//            } catch (Exception e) {
//                // Log.d("Process", "Error>> :"+ e.toString());
//            }
//        }
//        return processName;
//    }
//}
