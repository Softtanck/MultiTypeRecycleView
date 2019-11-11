package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

/**
 * Created by Administrator on 2016/4/11.
 */
public class PushResponse extends BaseResponse {


    /**
     * apkPath : /version/apk
     * id : 1
     * versionName : 3.02
     * updateContent : dfdfdfd
     * versionCode : 32
     */

    private MsgBean msg;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        private String apkPath;
        private int id;
        private String versionName;
        private String updateContent;
        private int versionCode;
        private boolean isForced;
        private String homeAdvUrl;
        private String carWashing;
        private String couponCounts;
        private String carCounts;

        private boolean hasCoupon;

        public String getCouponCounts() {
            return couponCounts;
        }

        public void setCouponCounts(String couponCounts) {
            this.couponCounts = couponCounts;
        }

        public String getCarCounts() {
            return carCounts;
        }

        public void setCarCounts(String carCounts) {
            this.carCounts = carCounts;
        }

        public String getCarWashing() {
            return carWashing;
        }

        public void setCarWashing(String carWashing) {
            this.carWashing = carWashing;
        }

        public String getHomeAdvUrl() {
            return homeAdvUrl;
        }

        public void setHomeAdvUrl(String homeAdvUrl) {
            this.homeAdvUrl = homeAdvUrl;
        }

        public boolean isForced() {
            return isForced;
        }

        public void setForced(boolean forced) {
            isForced = forced;
        }

        public boolean isHasCoupon() {
            return hasCoupon;
        }

        public void setHasCoupon(boolean hasCoupon) {
            this.hasCoupon = hasCoupon;
        }

        public String getApkPath() {
            return apkPath;
        }

        public void setApkPath(String apkPath) {
            this.apkPath = apkPath;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getUpdateContent() {
            return updateContent;
        }

        public void setUpdateContent(String updateContent) {
            this.updateContent = updateContent;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }
    }
}
