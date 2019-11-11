package com.cheweishi.android.adapter;

import com.cheweishi.android.response.BaseResponse;

/**
 * Created by tangce on 7/18/2016.
 */
public class AddCarResponse extends BaseResponse {


    /**
     * appTitle : 精诚汽车服务中心
     * vehicleId : 9
     */

    private MsgBean msg;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean {
        private String appTitle;
        private String vehicleId;

        public String getAppTitle() {
            return appTitle;
        }

        public void setAppTitle(String appTitle) {
            this.appTitle = appTitle;
        }

        public String getVehicleId() {
            return vehicleId;
        }

        public void setVehicleId(String vehicleId) {
            this.vehicleId = vehicleId;
        }
    }
}
