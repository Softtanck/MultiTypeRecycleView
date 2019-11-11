//package com.cheweishi.android.utils;
//
//import android.content.Intent;
//
//import com.cheweishi.android.activity.MainActivity;
//import com.cheweishi.android.chat.config.Constant;
//import com.easemob.EMCallBack;
//import com.easemob.EMEventListener;
//import com.easemob.chat.EMChatManager;
//
///**
// * 环信SDK快速初始化工具类(待完善)---处理连接冲突错误(处理类未确定)
// * 
// * @author mingdasen
// * 
// */
//public class MyHXSDKHelper extends HXSDKHelper{
//	
//	  /**
//     * EMEventListener
//     */
//    protected EMEventListener eventListener = null;
//    
//    /**
//     * 连接监听
//     */
//    @Override
//    protected void initListener(){
//        super.initListener();
//    }
//    
//    /**
//     * 被踢处理
//     */
//    @Override
//    protected void onConnectionConflict(){
//        Intent intent = new Intent(appContext, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("conflict", true);
//        appContext.startActivity(intent);
//    }
//    /**
//     * 账号被移除处理
//     */
//    @Override
//    protected void onCurrentAccountRemoved(){
//    	Intent intent = new Intent(appContext, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(Constant.ACCOUNT_REMOVED, true);
//        appContext.startActivity(intent);
//    }
//    
//    /**
//     * 退出处理
//     */
//    @Override
//    public void logout(final EMCallBack callback){
//        endCall();
//        super.logout(new EMCallBack(){
//
//            @Override
//            public void onSuccess() {
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
//     * 结束回调
//     */
//    void endCall(){
//        try {
//            EMChatManager.getInstance().endCall();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
