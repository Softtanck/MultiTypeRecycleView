/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cheweishi.android.config;

import android.os.Environment;
import android.util.SparseArray;
import android.view.View;

import com.cheweishi.android.entity.LoginResponse;
import com.cheweishi.android.utils.StringUtil;

import java.lang.ref.SoftReference;

public class Constant {
    public static final SparseArray<SoftReference<View>> carView = new SparseArray<>();
//    public static final SparseArray<View> carView = new SparseArray<>();

    public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
    public static final String GROUP_USERNAME = "item_groups";
    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    /**
     *
     */
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";
    /**
     * 用户删除表识
     */
    public static final String ACCOUNT_REMOVED = "account_removed";
    public static String SettingOrPhone = "Phone";
    public static int Server_Hall_index = 0;
    public static String LOGIN_REFRESH = "com.cheweishi.android.activity.LoginActivity";
    public static String COUPON_REFRESH = "com.cheweishi.android.activity.CouponActivity";
    public static String CAR_MANAGER_REFRESH = "com.cheweishi.android.activity.CarManagerActivity";
    public static String MESSAGE_CENTER_REFRESH = "com.cheweishi.android.activity.MessageCenterActivtiy";
    public static String SPECIAL_SIGN_REFRESH = "com.cheweishi.android.activtiy.UserSignActivity";
    public static String USER_CENTER_REFRESH = "com.cheweishi.android.activity.UserDetailActivity";
    public static String NET_PHONE_REFRESH = "com.cheweishi.android.activity.PhoneDailActivity";
    public static String INSURANCE_REFRESH = "com.cheweishi.android.activity.InsuranceBuyActivity";
    public static String SIGN_REFRESH = "com.cheweishi.android.activity.SignActivty";
    public static String RECHARGE_REFRESH = "com.cheweishi.android.activity.RechargeActivity";
    public static String PAY_REFRESH = "com.cheweishi.android.activity.PayActivity";
    public static String FIND_PARK_REFRESH = "com.cheweishi.android.activity.FindParkingSpaceActivity";
    public static String CANCEL_ORDER_REFRESH = "com.cheweishi.android.activity.MaintainCancelActivity";
    public static String SUCCESS_ORDER_REFRESH = "com.cheweishi.android.activity.OrderDetailsActivity.success.firststep";
    public static String SUCCESS_MIDDLE_ORDER_REFRESH = "com.cheweishi.android.activity.OrderDetailsActivity.success.secondstep";
    public static String SUCCESS_COMPLETE_ORDER_REFRESH = "com.cheweishi.android.activity.OrderDetailsActivity.success.complete";
    public static String SUCCESS_OUTOFDATELINE_ORDER_REFRESH = "com.cheweishi.android.activity.OrderDetailsActivity.outofdateline";
    public static String CANCEL_ORDER_SUCCESS_REFRESH = "com.cheweishi.android.activity.OrderDetailsActivity.outofdateline";
    public static String WEIXIN_PAY_REFRESH = "com.cheweishi.android.wxapi.WXPayEntryActivity";
    public static String WEIXIN_PAY_FAIL_REFRESH = "com.cheweishi.android.wxapi.WXPayEntryActivity.Fail";

    // public static String JPUSH_REFRESH =
    // "com.cheweishi.android.recevier.JPushReceiver";
    public static String CURRENT_REFRESH = "";
    public static String REFRESH_FLAG = "REFRESH";
    public static String CAR_MANAGER_REFRESH_FLAG = "REFRESH_CAR";
    public static boolean CALL_REQUEST = false;
    public static String USER_NICK_EDIT_REFRESH = "com.cheweishi.android.activity.UserInfoEditActivity_NICK";
    public static String USER_NICK_EDIT_REFRESH_OTHER = "com.cheweishi.android.activity.UserInfoEditActivity_OTHER";
    public static LoginResponse loginResponse;
    public static boolean EDIT_FLAG = false;
    public static boolean CAR_FLAF = false;

    // 微信支付
    public static final String APP_ID = "wx75b0585936937e4a";
    public static final String MCH_ID = "1249451301";
    public static final String API_KEY = "a09d313c915246009685239d39b3e416";
    public static int WASHCAR_COUNT = 0;
    public static String CAR_REPORT_DATE = StringUtil.getDate(
            StringUtil.getLastDate(), "yyyy-MM-dd");

    // 文件缓存目录
    public final static String BASE_IMG_CATCH_DIR = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/cheweishi/";
    public static String JPUSH_TAGS = "";


    //请求标识
    public static final String PARAMETER_TAG = "PARAMETER_TAG";

    // 密码Publick key
    public static final String SERVER_PUBLIC_KEY_STORE = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBMPGboxzPh9SApXHBKMQHF31rgB6LQBZxg3VirK9Rbp0qvgIDw+2ygZxPQAkgiK24PTWuBbw2UTNy5NxglSCsCnY8+vJXd8cwZKrBpnwXEcO0Wuh5G8Z++X0AIisMCIoiDZZwWnvqJ7a3vUQIj62qTX259s0UqvjGA7uvoDM9tQIDAQAB";

    //自动登录
    public static final String AUTO_LOGIN = "AUTO_LOGIN";
}
