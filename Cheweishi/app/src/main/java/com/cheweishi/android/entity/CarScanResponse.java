package com.cheweishi.android.entity;

import com.cheweishi.android.response.BaseResponse;

import java.io.Serializable;

/**
 * Created by tangce on 4/6/2016.
 */
public class CarScanResponse extends BaseResponse implements Serializable {


    /**
     * obdspeed : null
     * obdAFR : null
     * obdWaterTemp : null
     * obdRemainingGas : 30
     * obdCTA : null
     * totalMileAge : null
     * obdTemp : null
     * obdEngRuntime : null
     * obdEngLoad : null
     * obdDate : null
     * obdRPM : null
     * obdBV : null
     * obdIFC : null
     */

    private MsgBean msg;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public static class MsgBean implements Serializable{
        private String obdspeed;
        private String obdAFR;
        private String obdWaterTemp;
        private int obdRemainingGas;
        private String obdCTA;
        private String totalMileAge;
        private String obdTemp;
        private String obdEngRuntime;
        private String obdEngLoad;
        private Long obdDate;
        private String obdRPM;
        private String obdBV;
        private String obdIFC;

        public String getObdspeed() {
            return obdspeed;
        }

        public void setObdspeed(String obdspeed) {
            this.obdspeed = obdspeed;
        }

        public String getObdAFR() {
            return obdAFR;
        }

        public void setObdAFR(String obdAFR) {
            this.obdAFR = obdAFR;
        }

        public String getObdWaterTemp() {
            return obdWaterTemp;
        }

        public void setObdWaterTemp(String obdWaterTemp) {
            this.obdWaterTemp = obdWaterTemp;
        }

        public int getObdRemainingGas() {
            return obdRemainingGas;
        }

        public void setObdRemainingGas(int obdRemainingGas) {
            this.obdRemainingGas = obdRemainingGas;
        }

        public String getObdCTA() {
            return obdCTA;
        }

        public void setObdCTA(String obdCTA) {
            this.obdCTA = obdCTA;
        }

        public String getTotalMileAge() {
            return totalMileAge;
        }

        public void setTotalMileAge(String totalMileAge) {
            this.totalMileAge = totalMileAge;
        }

        public String getObdTemp() {
            return obdTemp;
        }

        public void setObdTemp(String obdTemp) {
            this.obdTemp = obdTemp;
        }

        public String getObdEngRuntime() {
            return obdEngRuntime;
        }

        public void setObdEngRuntime(String obdEngRuntime) {
            this.obdEngRuntime = obdEngRuntime;
        }

        public String getObdEngLoad() {
            return obdEngLoad;
        }

        public void setObdEngLoad(String obdEngLoad) {
            this.obdEngLoad = obdEngLoad;
        }

        public Long getObdDate() {
            return obdDate;
        }

        public void setObdDate(Long obdDate) {
            this.obdDate = obdDate;
        }

        public String getObdRPM() {
            return obdRPM;
        }

        public void setObdRPM(String obdRPM) {
            this.obdRPM = obdRPM;
        }

        public String getObdBV() {
            return obdBV;
        }

        public void setObdBV(String obdBV) {
            this.obdBV = obdBV;
        }

        public String getObdIFC() {
            return obdIFC;
        }

        public void setObdIFC(String obdIFC) {
            this.obdIFC = obdIFC;
        }
    }
}
