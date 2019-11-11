package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

/**
 * Created by tangce on 5/16/2016.
 */
public class RegisterResponse extends BaseResponse {

    /**
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
        private boolean isGetCoupon;

        public boolean isIsGetCoupon() {
            return isGetCoupon;
        }

        public void setIsGetCoupon(boolean isGetCoupon) {
            this.isGetCoupon = isGetCoupon;
        }
    }
}
