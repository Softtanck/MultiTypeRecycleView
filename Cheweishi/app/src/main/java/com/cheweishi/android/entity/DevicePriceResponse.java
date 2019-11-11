package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

/**
 * Created by Administrator on 2016/5/23.
 */
public class DevicePriceResponse extends BaseResponse {

    private MsgBean msg;


    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        private double devicePrice;

        public double getDevicePrice() {
            return devicePrice;
        }

        public void setDevicePrice(double devicePrice) {
            this.devicePrice = devicePrice;
        }
    }
}
