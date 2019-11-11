package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

/**
 * Created by tangce on 4/7/2016.
 */
public class QRServerResponse extends BaseResponse {


    /**
     * appTitleName : 车的范德萨
     * isGetCoupon : true
     */

    private MsgBean msg;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        private String appTitleName;
        private boolean isGetCoupon;

        public String getAppTitleName() {
            return appTitleName;
        }

        public void setAppTitleName(String appTitleName) {
            this.appTitleName = appTitleName;
        }

        public boolean isIsGetCoupon() {
            return isGetCoupon;
        }

        public void setIsGetCoupon(boolean isGetCoupon) {
            this.isGetCoupon = isGetCoupon;
        }
    }
}
